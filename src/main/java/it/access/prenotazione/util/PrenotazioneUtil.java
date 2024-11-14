package it.access.prenotazione.util;

import it.access.prenotazione.config.AppValue;
import it.access.prenotazione.dto.PrenotazioneDTO;
import it.access.prenotazione.exception.InvalidTokenException;
import it.access.prenotazione.mapper.PrenotazioneMapper;
import it.access.prenotazione.model.entity.Prenotazione;
import it.access.prenotazione.model.repository.PrenotazioneRepository;
import it.access.prenotazione.response.CustomResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@Setter
@Getter
@Slf4j
public class PrenotazioneUtil {

    private final PrenotazioneRepository prenotazioneRepository;
    private final PrenotazioneMapper prenotazioneMapper;
    private final AppValue appValue;
    private final RestTemplate restTemplate;

    public PrenotazioneUtil(PrenotazioneRepository prenotazioneRepository, PrenotazioneMapper prenotazioneMapper, AppValue appValue, RestTemplate restTemplate) {
        this.prenotazioneRepository = prenotazioneRepository;
        this.prenotazioneMapper = prenotazioneMapper;
        this.appValue = appValue;
        this.restTemplate = restTemplate;
    }

    public CustomResponse<String> createPrenotazione(PrenotazioneDTO request, String token) {
        Prenotazione nuovaPrenotazione = prenotazioneMapper.toEntity(request);
        token = token.substring(7);
        CustomResponse<Long> userId;

        try {
            ResponseEntity<CustomResponse<Long>> responseEntity = restTemplate.exchange(
                    appValue.getGetUserId() + token,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {
                    }
            );
            userId = responseEntity.getBody();
        } catch (RestClientException e) {
            return CustomResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
        if (userId == null || userId.getResponse() == null) {
            throw new InvalidTokenException("UserId non trovato o nullo per il token: " + token);
        }
        nuovaPrenotazione.setUserId(userId.getResponse());
        nuovaPrenotazione.setPrenotato(true);
        nuovaPrenotazione.setCreatedAt(LocalDateTime.now());
        nuovaPrenotazione.setUpdatedAt(nuovaPrenotazione.getCreatedAt());
        nuovaPrenotazione.setCodice(UUID.randomUUID().toString());

        nuovaPrenotazione.setVidimato(false); // todo: Implementare metodo verifica vidimazione

        prenotazioneRepository.save(nuovaPrenotazione);
        return CustomResponse.success(nuovaPrenotazione.getCodice());
    }
}
