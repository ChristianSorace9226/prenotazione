package it.access.prenotazione.service.impl;

import it.access.prenotazione.config.AppValue;
import it.access.prenotazione.dto.PrenotazioneDTO;
import it.access.prenotazione.mapper.PrenotazioneMapper;
import it.access.prenotazione.model.entity.Prenotazione;
import it.access.prenotazione.model.repository.PrenotazioneRepository;
import it.access.prenotazione.service.resource.PrenotazioneServiceResource;
import it.access.prenotazione.util.PrenotazioneUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Getter
@Setter
public class PrenotazioneServiceImpl implements PrenotazioneServiceResource {

    private final PrenotazioneMapper prenotazioneMapper;
    private final PrenotazioneRepository prenotazioneRepository;
    private final RestTemplate restTemplate;
    private final AppValue appValue;
    private final PrenotazioneUtil prenotazioneUtil;

    public PrenotazioneServiceImpl(PrenotazioneMapper prenotazioneMapper, PrenotazioneRepository prenotazioneRepository, RestTemplate restTemplate, AppValue appValue, PrenotazioneUtil prenotazioneUtil) {
        this.prenotazioneMapper = prenotazioneMapper;
        this.prenotazioneRepository = prenotazioneRepository;
        this.restTemplate = restTemplate;
        this.appValue = appValue;
        this.prenotazioneUtil = prenotazioneUtil;
    }

    @Transactional
    @Override
    public String prenota(PrenotazioneDTO request, String token) {
        String codicePrenotazione = prenotazioneUtil.createPrenotazione(request, token);
        if (codicePrenotazione != null) {
            Prenotazione nuovaPrenotazione = prenotazioneRepository.findByCodice(codicePrenotazione)
                    .orElseThrow(() -> new RuntimeException("Prenotazione non riuscita"));
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(appValue.getSaveCodPrenotazioneIntoUser())
                    .queryParam("userId",  nuovaPrenotazione.getUserId())
                    .queryParam("codice", codicePrenotazione);
            Boolean codeSaved = restTemplate.getForObject(builder.toUriString(), Boolean.class);

            if (Boolean.TRUE.equals(codeSaved)) {
                return "Prenotazione avvenuta con successo con codice: " + nuovaPrenotazione.getCodice();
            } else {
                return "Ops... Qualcosa è andato storto";
            }
        }
        return "Prenotazione non possibile: codice già in uso.";

    }

    @Transactional
    @Override
    public PrenotazioneDTO modificaPrenotazione(String codice, PrenotazioneDTO prenotazione) {
        Prenotazione prenotazioneEntity = prenotazioneRepository.findByCodice(codice)
                .orElseThrow(() -> new RuntimeException("Codice non trovato o non corretto"));
        if (prenotazione.getCodice() != null) {
            prenotazioneEntity.setCodice(prenotazione.getCodice());
        }
        prenotazioneEntity.setUpdatedAt(LocalDateTime.now());
        prenotazioneRepository.save(prenotazioneEntity);
        return prenotazioneMapper.toDto(prenotazioneEntity);
    }

    @Override
    public PrenotazioneDTO getPrenotazione(String codice) {
        Prenotazione prenotazione = prenotazioneRepository.findByCodice(codice)
                .orElseThrow(() -> new RuntimeException("Codice non trovato o non corretto"));
        return prenotazioneMapper.toDto(prenotazione);
    }

    @Transactional
    @Override
    public String cancellaPrenotazione(String codice) {
        if (prenotazioneRepository.findByCodice(codice).isPresent()) {
            prenotazioneRepository.deleteByCodice(codice);
            return "Prenotazione cancellata correttamente.";
        }
        return "Codice non trovato o non corretto.";
    }

    @Override
    public List<PrenotazioneDTO> getAllPrenotazioni() {
        List<Prenotazione> prenotazioni = prenotazioneRepository.findAll();
        return prenotazioneMapper.toDtoList(prenotazioni);
    }
}
