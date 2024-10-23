package it.access.prenotazione.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class AppValue {

    @Value("${external.service.url}")
    private String hasAccessUrl;

}
