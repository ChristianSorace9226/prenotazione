package it.access.prenotazione.dto;

import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PrenotazioneDTO {

    Long id;
    LocalDateTime updatedAt;
    LocalDateTime createdAt = LocalDateTime.now();
    Long version;
    String codice;

}
