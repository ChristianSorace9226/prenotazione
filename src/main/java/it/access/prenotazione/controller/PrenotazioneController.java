package it.access.prenotazione.controller;

import it.access.prenotazione.dto.PrenotazioneDTO;
import it.access.prenotazione.exception.InvalidTokenException;
import it.access.prenotazione.service.resource.PrenotazioneServiceResource;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/prenotazione")
@AllArgsConstructor
public class PrenotazioneController {

    private final PrenotazioneServiceResource prenotazioneServiceResource;

    @PostMapping("/prenota")
    public ResponseEntity<String> creaPrenotazione(@RequestHeader("Authorization") String token,
                                                   @RequestBody PrenotazioneDTO request) {
        try {
            String response = prenotazioneServiceResource.prenota(request, token);
            return ResponseEntity.ok(response);
        } catch (InvalidTokenException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Il token non è valido(controller)");
        }
    }

    @PutMapping("/update/{codice}")
    public ResponseEntity<PrenotazioneDTO> modificaPrenotazione(@RequestHeader("Authorization") String token,
                                                                @PathVariable String codice,
                                                                @RequestBody PrenotazioneDTO prenotazione) {
        PrenotazioneDTO updatedPrenotazione = prenotazioneServiceResource.modificaPrenotazione(codice, prenotazione);
        return ResponseEntity.ok(updatedPrenotazione);
    }

    @GetMapping("/prenotazioni/{codice}")
    public ResponseEntity<PrenotazioneDTO> getPrenotazioneByCodice(@RequestHeader("Authorization") String token,
                                                                   @PathVariable String codice) {
        PrenotazioneDTO prenotazione = prenotazioneServiceResource.getPrenotazione(codice);
        return ResponseEntity.ok(prenotazione);
    }

    @DeleteMapping("/cancella/{codice}")
    public ResponseEntity<String> cancellaPrenotazione(@RequestHeader("Authorization") String token,
                                                       @PathVariable String codice) {
        String response = prenotazioneServiceResource.cancellaPrenotazione(codice);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/prenotazioni/get-all")
    public ResponseEntity<List<PrenotazioneDTO>> getAllPrenotazioni(@RequestHeader("Authorization") String token) {
        List<PrenotazioneDTO> prenotazioni = prenotazioneServiceResource.getAllPrenotazioni();
        return ResponseEntity.ok(prenotazioni);
    }

}
