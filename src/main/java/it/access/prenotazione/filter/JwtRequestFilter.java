package it.access.prenotazione.filter;

import it.access.prenotazione.config.AppValue;
import it.access.prenotazione.util.BaseUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
@AllArgsConstructor
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

    private final AppValue appValue;

    private final RestTemplate restTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain chain) throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {

            // Verifica del token di autenticazione
            Boolean isValid;

            try {
                HttpHeaders headers = new HttpHeaders();
                headers.set("Authorization", authorizationHeader);
                HttpEntity<String> entity = new HttpEntity<>(headers);

                // Passa request.getRequestURI() come variabile di percorso
                // Costruzione dell'URL usando UriComponentsBuilder
                String validationUrl = UriComponentsBuilder
                        .fromHttpUrl(appValue.getHasAccessUrl()) // Base URL che include già /jwt/has-access
                        .queryParam("uri", request.getRequestURI())
                        .toUriString(); // Converte in String

                ResponseEntity<Boolean> responseValid = restTemplate.exchange(
                        validationUrl,
                        HttpMethod.GET,
                        entity,
                        Boolean.class
                );

                isValid = responseValid.getBody();  // Verifica se il token è valido
                if (Boolean.TRUE.equals(isValid)) {
                    chain.doFilter(request, response);
                }
            } catch (RestClientException e) {
                throw new RuntimeException("Errore di comunicazione: " + e);
            } catch (IOException e) {
                throw new RuntimeException("Errore interno: " + e);
            } catch (ServletException e) {
                throw new RuntimeException("Errore servlet: " + e);
            }
        }
    }
}
