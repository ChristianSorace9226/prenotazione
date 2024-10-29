package it.access.prenotazione.service.resource;

import jakarta.annotation.Nonnull;

public interface TokenValidationResource {
    public Boolean isValidToken(@Nonnull String authorizationHeader, @Nonnull String requestURI);
}
