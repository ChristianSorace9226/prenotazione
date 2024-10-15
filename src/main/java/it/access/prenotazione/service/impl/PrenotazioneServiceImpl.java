package it.access.prenotazione.service.impl;

import it.access.prenotazione.dto.PrenotazioneDTO;
import it.access.prenotazione.mapper.PrenotazioneMapper;
import it.access.prenotazione.model.entity.Prenotazione;
import it.access.prenotazione.model.repository.PrenotazioneRepository;
import it.access.prenotazione.service.resource.PrenotazioneServiceResource;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
public class PrenotazioneServiceImpl implements PrenotazioneServiceResource {

    private final PrenotazioneMapper prenotazioneMapper;
    private final PrenotazioneRepository prenotazioneRepository;

    public PrenotazioneServiceImpl(PrenotazioneMapper prenotazioneMapper, PrenotazioneRepository prenotazioneRepository) {
        this.prenotazioneMapper = prenotazioneMapper;
        this.prenotazioneRepository = prenotazioneRepository;
    }

    @Override
    public String prenota(PrenotazioneDTO request) {
        Prenotazione nuovaPrenotazione = prenotazioneRepository.save(prenotazioneMapper.toEntity(request));
        return "Prenotazione avvenuta con successo con codice: " + nuovaPrenotazione.getCodice();
    }
}
