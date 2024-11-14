package it.access.prenotazione.service.impl;

import it.access.prenotazione.config.AppValue;
import it.access.prenotazione.dto.PrenotazioneDTO;
import it.access.prenotazione.exception.InvalidCodeException;
import it.access.prenotazione.mapper.PrenotazioneMapper;
import it.access.prenotazione.model.entity.Prenotazione;
import it.access.prenotazione.model.repository.PrenotazioneRepository;
import it.access.prenotazione.response.CustomResponse;
import it.access.prenotazione.service.resource.PrenotazioneServiceResource;
import it.access.prenotazione.util.PrenotazioneUtil;
import jakarta.persistence.NoResultException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
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
        CustomResponse<String> codicePrenotazione = prenotazioneUtil.createPrenotazione(request, token);

        if (!codicePrenotazione.getErrorMessage().isEmpty()) {
            throw new RuntimeException(codicePrenotazione.getErrorMessage());
        }
        Prenotazione nuovaPrenotazione = prenotazioneRepository.findByCodice(codicePrenotazione.getResponse())
                .orElseThrow(() -> new RuntimeException("Prenotazione non riuscita"));
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(appValue.getSaveCodPrenotazioneIntoUser())
                .queryParam("userId", nuovaPrenotazione.getUserId())
                .queryParam("codice", codicePrenotazione.getResponse());
        CustomResponse<Boolean> codeSaved;

        ResponseEntity<CustomResponse<Boolean>> codeSavedCall = restTemplate.exchange(builder.toUriString(),
                HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                });
        codeSaved = codeSavedCall.getBody();

        int codeSavedResult = codeSaved.getResult();
        Boolean codeSavedResponse = codeSaved.getResponse();
        String errorMessage = codeSaved.getErrorMessage();

        if (!errorMessage.isEmpty()) {
            throw new RuntimeException(errorMessage);
        }

        if (Boolean.TRUE.equals(codeSavedResponse)) {
            return "Prenotazione avvenuta: " + codicePrenotazione.getResponse();
        } else {
            throw new RuntimeException(errorMessage);
        }
    }

    @Transactional
    @Override
    public PrenotazioneDTO modificaPrenotazione(String codice, PrenotazioneDTO prenotazione) {
        Prenotazione prenotazioneEntity = prenotazioneRepository.findByCodice(codice)
                .orElseThrow(() -> new InvalidCodeException("Codice non trovato o non corretto"));
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
                .orElseThrow(() -> new InvalidCodeException("Codice non trovato o non corretto"));
        return prenotazioneMapper.toDto(prenotazione);
    }

    @Transactional
    @Override
    public String cancellaPrenotazione(String codice) {
        Prenotazione prenotzione = prenotazioneRepository.findByCodice(codice)
                .orElseThrow(() -> new InvalidCodeException("Codice non trovato o non corretto."));
        prenotazioneRepository.deleteByCodice(codice);
        return "Prenotazione cancellata correttamente.";
    }

    @Override
    public List<PrenotazioneDTO> getAllPrenotazioni() {
        List<Prenotazione> prenotazioni = prenotazioneRepository.findAll();
        if (prenotazioni.isEmpty()) {
            throw new NoResultException("Nessuna prenotazione trovata.");
        }
        return prenotazioneMapper.toDtoList(prenotazioni);
    }
}
