package it.access.prenotazione.service.impl;

import it.access.prenotazione.dto.PrenotazioneDTO;
import it.access.prenotazione.mapper.PrenotazioneMapper;
import it.access.prenotazione.model.entity.Prenotazione;
import it.access.prenotazione.model.repository.PrenotazioneRepository;
import it.access.prenotazione.service.resource.PrenotazioneServiceResource;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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
        if (prenotazioneRepository.findByCodice(request.getCodice()).isEmpty()) {
            Prenotazione nuovaPrenotazione = prenotazioneRepository.save(prenotazioneMapper.toEntity(request));
            return "Prenotazione avvenuta con successo con codice: " + nuovaPrenotazione.getCodice();
        }
        return "Prenotazione non possibile: codice già in uso.";
    }

    @Override
    public PrenotazioneDTO modificaPrenotazione(String codice, PrenotazioneDTO prenotazione) {
        Prenotazione prenotazioneEntity = prenotazioneRepository.findByCodice(codice)
                .orElseThrow(() -> new RuntimeException("Codice non trovato o non corretto"));
        prenotazioneEntity.setVersion(prenotazione.getVersion());
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
