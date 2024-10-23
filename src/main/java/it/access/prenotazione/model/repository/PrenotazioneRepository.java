package it.access.prenotazione.model.repository;

import it.access.prenotazione.model.entity.Prenotazione;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PrenotazioneRepository extends JpaRepository<Prenotazione,Long> {
    Optional<Prenotazione> findByCodice(String codice);
    void deleteByCodice(String codice);
}
