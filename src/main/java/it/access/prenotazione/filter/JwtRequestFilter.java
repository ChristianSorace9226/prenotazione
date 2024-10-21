package it.access.prenotazione.filter;

import it.access.prenotazione.config.AppValue;
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
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

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

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", authorizationHeader);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<Boolean> responseValid = restTemplate.exchange(
                    appValue.getExternalServiceUrl(),  // URL del microservizio di validazione JWT
                    HttpMethod.GET,                    // Metodo GET per la validazione del token
                    entity,                            // Passa l'header con il token
                    Boolean.class                      // Il tipo di risposta atteso (es: true o false)
            );

            isValid = responseValid.getBody();  // Verifica se il token è valido
            if (Boolean.TRUE.equals(isValid)) {
                chain.doFilter(request, response);
            }
        }
    }
}
