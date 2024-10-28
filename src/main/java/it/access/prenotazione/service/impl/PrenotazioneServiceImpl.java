package it.access.prenotazione.service.impl;

import it.access.prenotazione.config.AppValue;
import it.access.prenotazione.dto.PrenotazioneDTO;
import it.access.prenotazione.mapper.PrenotazioneMapper;
import it.access.prenotazione.model.entity.Prenotazione;
import it.access.prenotazione.model.repository.PrenotazioneRepository;
import it.access.prenotazione.service.resource.PrenotazioneServiceResource;
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

    public PrenotazioneServiceImpl(PrenotazioneMapper prenotazioneMapper, PrenotazioneRepository prenotazioneRepository, RestTemplate restTemplate, AppValue appValue) {
        this.prenotazioneMapper = prenotazioneMapper;
        this.prenotazioneRepository = prenotazioneRepository;
        this.restTemplate = restTemplate;
        this.appValue = appValue;
    }

    @Transactional
    @Override
    public String prenota(PrenotazioneDTO request, String token) {
        if (prenotazioneRepository.findByCodice(request.getCodice()).isEmpty()) {
            Prenotazione nuovaPrenotazione = prenotazioneMapper.toEntity(request);
            token = token.substring(7);
            Long userId = restTemplate.getForObject(appValue.getGetUserId() + token, Long.class);
            nuovaPrenotazione.setUserId(userId);
            nuovaPrenotazione.setCreatedAt(LocalDateTime.now());
            nuovaPrenotazione.setUpdatedAt(nuovaPrenotazione.getCreatedAt());
            prenotazioneRepository.save(nuovaPrenotazione);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(appValue.getSaveCodPrenotazioneIntoUser())
                    .queryParam("userId", userId)
                    .queryParam("codice", nuovaPrenotazione.getCodice());
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
        prenotazioneEntity.setUpdatedAt(LocalDateTime.now());
        if (prenotazione.getCodice() != null) {
            prenotazioneEntity.setCodice(prenotazione.getCodice());
        }
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
