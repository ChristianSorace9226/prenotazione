package it.access.prenotazione.model.repository;

import it.access.prenotazione.model.entity.Prenotazione;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrenotazioneRepository extends JpaRepository<Prenotazione,Long> {
}
