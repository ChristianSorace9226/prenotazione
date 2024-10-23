package it.access.prenotazione.service.resource;

import it.access.prenotazione.dto.PrenotazioneDTO;

import java.util.List;

public interface PrenotazioneServiceResource {

    String prenota(PrenotazioneDTO request);

    PrenotazioneDTO modificaPrenotazione(String codice, PrenotazioneDTO prenotazione);

    PrenotazioneDTO getPrenotazione(String codice);

    String cancellaPrenotazione(String codice);

    List<PrenotazioneDTO> getAllPrenotazioni();
}
