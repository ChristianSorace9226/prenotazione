package it.access.prenotazione.util;

import it.access.prenotazione.config.AppValue;
import org.springframework.web.client.RestTemplate;

public abstract class BaseUtil {

    protected AppValue appValue;
    protected RestTemplate restTemplate;

}
