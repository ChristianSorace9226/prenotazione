package it.access.prenotazione.filter;

import it.access.prenotazione.service.resource.TokenValidationResource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@AllArgsConstructor
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

    private final TokenValidationResource tokenValidationResource;


    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain chain) throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                Boolean isValid = tokenValidationResource.isValidToken(authorizationHeader, request.getRequestURI());
                if (Boolean.TRUE.equals(isValid)) {
                    chain.doFilter(request, response);
                }
            } catch (RuntimeException e) {
                String[] messaggio = e.getMessage().substring(26).split(" : ");
                messaggio[1] = messaggio[1].substring(1, messaggio[1].length() - 1);
                response.setStatus(Integer.parseInt(messaggio[0]));
                response.getWriter().write(messaggio[1]);
            }
        }
    }
}
