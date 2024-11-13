package it.access.prenotazione.filter;

import it.access.prenotazione.response.CustomResponse;
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
                CustomResponse<Boolean> isValidCall = tokenValidationResource.isValidToken(authorizationHeader, request.getRequestURI());

                int isValidResult = isValidCall.getResult();
                Boolean isValid = isValidCall.getResponse();
                String isValidError = isValidCall.getErrorMessage();

                if (Boolean.TRUE.equals(isValid)) {
                    chain.doFilter(request, response);
                } else {
                    response.setStatus(isValidResult);
                    response.getWriter().write(isValidError);
                }

            } catch (RuntimeException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write(e.getMessage());
            }
        }
    }
}
