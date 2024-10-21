package it.access.prenotazione.controller;

import it.access.prenotazione.config.AppValue;
import it.access.prenotazione.dto.PrenotazioneDTO;
import it.access.prenotazione.service.resource.PrenotazioneServiceResource;
import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/prenotazione")
@AllArgsConstructor
public class PrenotazioneController {

    private final PrenotazioneServiceResource prenotazioneServiceResource;

    @PostMapping("/richiesta")
    public ResponseEntity<String> creaPrenotazione(@RequestBody PrenotazioneDTO request, @RequestHeader("Authorization") String token) {

        // Logica per la creazione della prenotazione
        String response = prenotazioneServiceResource.prenota(request);
        return ResponseEntity.ok(response);
    }


}
