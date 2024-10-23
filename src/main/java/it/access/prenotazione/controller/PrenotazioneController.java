package it.access.prenotazione.controller;

import it.access.prenotazione.config.AppValue;
import it.access.prenotazione.dto.PrenotazioneDTO;
import it.access.prenotazione.service.resource.PrenotazioneServiceResource;
import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/prenotazione")
@AllArgsConstructor
public class PrenotazioneController {

    private final PrenotazioneServiceResource prenotazioneServiceResource;

    @PostMapping("/richiesta")
    public ResponseEntity<String> creaPrenotazione(@RequestHeader("Authorization") String token, @RequestBody PrenotazioneDTO request) {

        // Logica per la creazione della prenotazione
        String response = prenotazioneServiceResource.prenota(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{codice}")
    public ResponseEntity<PrenotazioneDTO> modificaPrenotazione(@RequestHeader("Authorization") String token, @PathVariable String codice, @RequestBody PrenotazioneDTO prenotazione) {
        PrenotazioneDTO updatedPrenotazione = prenotazioneServiceResource.modificaPrenotazione(codice, prenotazione);
        return ResponseEntity.ok(updatedPrenotazione);
    }

    @GetMapping("/prenotazioni/{codice}")
    public ResponseEntity<PrenotazioneDTO> getPrenotazioneByCodice(@RequestHeader("Authorization") String token, @PathVariable String codice) {
        PrenotazioneDTO prenotazione = prenotazioneServiceResource.getPrenotazione(codice);
        return ResponseEntity.ok(prenotazione);
    }

    @DeleteMapping("/cancella/{codice}")
    public ResponseEntity<String> cancellaPrenotazione(@RequestHeader("Authorization") String token, @PathVariable String codice) {
        String response = prenotazioneServiceResource.cancellaPrenotazione(codice);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/prenotazioni/get-all")
    public ResponseEntity<List<PrenotazioneDTO>> getAllPrenotazioni(@RequestHeader("Authorization") String token) {
        List<PrenotazioneDTO> prenotazioni = prenotazioneServiceResource.getAllPrenotazioni();
        return ResponseEntity.ok(prenotazioni);
    }

}
