package it.access.prenotazione.service.impl;

import it.access.prenotazione.config.AppValue;
import it.access.prenotazione.service.resource.TokenValidationResource;
import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@AllArgsConstructor
public class TokenValidationService implements TokenValidationResource {

    private final AppValue appValue;
    private final RestTemplate restTemplate;

    @Override
    public Boolean isValidToken(@Nonnull String authorizationHeader, @Nonnull String requestURI) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", authorizationHeader);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            String validationUrl = UriComponentsBuilder
                    .fromHttpUrl(appValue.getHasAccessUrl())
                    .queryParam("uri", requestURI)
                    .toUriString();

            ResponseEntity<Boolean> responseValid = restTemplate.exchange(
                    validationUrl,
                    HttpMethod.GET,
                    entity,
                    Boolean.class
            );

            return responseValid.getBody();
        } catch (RestClientException e) {
            throw new RuntimeException("Errore di comunicazione: " + e);
        }
    }
}

