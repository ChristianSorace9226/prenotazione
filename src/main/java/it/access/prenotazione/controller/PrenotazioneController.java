package it.access.prenotazione.controller;

import it.access.prenotazione.dto.PrenotazioneDTO;
import it.access.prenotazione.service.resource.PrenotazioneServiceResource;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/prenotazione")
@AllArgsConstructor
public class PrenotazioneController {

    private final RestTemplate restTemplate;
    private final PrenotazioneServiceResource prenotazioneServiceResource;

    @PostMapping("/richiesta")
    public ResponseEntity<String> creaPrenotazione(@RequestBody PrenotazioneDTO request,
                                                   @RequestHeader("Authorization") String token) {
        // Verifica del token di autenticazione
        String authServiceUrl = "http://localhost:8080/api/token/validate";
        Boolean isValid;

        try {
            isValid = restTemplate.getForObject(authServiceUrl + "?token=" + token, Boolean.class);
        } catch (RestClientException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore nella comunicazione con il servizio di autenticazione");
        }

        if (isValid == null || !isValid) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token non valido");
        }

        // Logica per la creazione della prenotazione
        String response = prenotazioneServiceResource.prenota(request);
        return ResponseEntity.ok(response);
    }



}
