package it.access.prenotazione.controller;

import it.access.prenotazione.dto.PrenotazioneDTO;
import it.access.prenotazione.exception.InvalidCodeException;
import it.access.prenotazione.exception.InvalidTokenException;
import it.access.prenotazione.response.CustomResponse;
import it.access.prenotazione.service.resource.PrenotazioneServiceResource;
import jakarta.persistence.NoResultException;
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
    public ResponseEntity<CustomResponse<String>> creaPrenotazione(@RequestHeader("Authorization") String token,
                                                                           @RequestBody PrenotazioneDTO request) {
        try {
            String response = prenotazioneServiceResource.prenota(request, token);
            return ResponseEntity.ok(CustomResponse.success(response));
        } catch (InvalidTokenException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(CustomResponse.error(HttpStatus.UNAUTHORIZED.value(), e.getMessage()));
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                   .body(CustomResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        }
    }

    @PutMapping("/update/{codice}")
    public ResponseEntity<CustomResponse<PrenotazioneDTO>> modificaPrenotazione(@RequestHeader("Authorization") String token,
                                                                @PathVariable String codice,
                                                                @RequestBody PrenotazioneDTO prenotazione) {
        try {
            return ResponseEntity.ok(CustomResponse.success(prenotazioneServiceResource.modificaPrenotazione(codice, prenotazione)));
        } catch (InvalidCodeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(CustomResponse.error(HttpStatus.NOT_FOUND.value(), e.getMessage()));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                   .body(CustomResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        }
    }

    @GetMapping("/prenotazioni/{codice}")
    public ResponseEntity<CustomResponse<PrenotazioneDTO>> getPrenotazioneByCodice(@RequestHeader("Authorization") String token,
                                                                   @PathVariable String codice) {
        try {
            return ResponseEntity.ok(CustomResponse.success(prenotazioneServiceResource.getPrenotazione(codice)));
        }  catch (InvalidCodeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(CustomResponse.error(HttpStatus.NOT_FOUND.value(), e.getMessage()));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CustomResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        }
    }

    @DeleteMapping("/cancella/{codice}")
    public ResponseEntity<CustomResponse<String>> cancellaPrenotazione(@RequestHeader("Authorization") String token,
                                                       @PathVariable String codice) {
        try {
            return ResponseEntity.ok(CustomResponse.success(prenotazioneServiceResource.cancellaPrenotazione(codice)));
        }  catch (InvalidCodeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(CustomResponse.error(HttpStatus.NOT_FOUND.value(), e.getMessage()));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CustomResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        }
    }

    @GetMapping("/prenotazioni/get-all")
    public ResponseEntity<CustomResponse<List<PrenotazioneDTO>>> getAllPrenotazioni(@RequestHeader("Authorization") String token) {
        try {
            return ResponseEntity.ok(CustomResponse.success(prenotazioneServiceResource.getAllPrenotazioni()));
        } catch (NoResultException e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(CustomResponse.error(HttpStatus.NO_CONTENT.value(), e.getMessage()));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                   .body(CustomResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        }
    }

}
