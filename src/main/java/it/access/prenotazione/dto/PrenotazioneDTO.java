package it.access.prenotazione.dto;

import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PrenotazioneDTO {

    private Long id;
    private String codice;
    private Long userId;

}
