package it.access.prenotazione.util;

import it.access.prenotazione.config.AppValue;
import it.access.prenotazione.dto.PrenotazioneDTO;
import it.access.prenotazione.mapper.PrenotazioneMapper;
import it.access.prenotazione.model.entity.Prenotazione;
import it.access.prenotazione.model.repository.PrenotazioneRepository;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
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

    public String createPrenotazione(PrenotazioneDTO request, String token) {
        if (prenotazioneRepository.findByCodice(request.getCodice()).isEmpty()) {
            Prenotazione nuovaPrenotazione = prenotazioneMapper.toEntity(request);
            token = token.substring(7);
            Long userId = restTemplate.getForObject(appValue.getGetUserId() + token, Long.class);
            nuovaPrenotazione.setUserId(userId);
            nuovaPrenotazione.setPrenotato(true);
            nuovaPrenotazione.setCreatedAt(LocalDateTime.now());
            nuovaPrenotazione.setUpdatedAt(nuovaPrenotazione.getCreatedAt());
            nuovaPrenotazione.setCodice(UUID.randomUUID().toString());

            nuovaPrenotazione.setVidimato(false); // todo: Implementare metodo verifica vidimazione

            prenotazioneRepository.save(nuovaPrenotazione);
            return nuovaPrenotazione.getCodice();
        }
        return null;
    }
}
