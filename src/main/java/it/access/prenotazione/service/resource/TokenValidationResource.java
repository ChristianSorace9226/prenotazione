package it.access.prenotazione.service.resource;

import it.access.prenotazione.response.CustomResponse;
import jakarta.annotation.Nonnull;

public interface TokenValidationResource {
    public CustomResponse<Boolean> isValidToken(@Nonnull String authorizationHeader, @Nonnull String requestURI);
}
