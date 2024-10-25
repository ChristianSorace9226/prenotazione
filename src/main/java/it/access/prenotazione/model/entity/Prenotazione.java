package it.access.prenotazione.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "prenotazione")
public class Prenotazione {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @LastModifiedDate
    @Column(name = "data_modifica_record")
    private LocalDateTime updatedAt;

    @CreatedDate
    @Column(name = "data_inserimento_record")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Version
    @Column(name = "optlck")
    private Long version;

    @Column(name = "codice")
    private String codice;

	@Column(name = "flag_prenotato")
    private boolean prenotato;

	@Column(name = "flag_vidimato")
    private boolean vidimato;

    @Column(name = "user_id")
    private Long userId;


//	@ManyToOne(fetch = FetchType.EAGER)
//	@JoinColumn(name = "id_posto")
//	Posto posto;
//
//	@ManyToOne(fetch = FetchType.EAGER)
//	@JoinColumn(name = "id_planning")
//	Planning planning;

//	@ManyToOne(fetch = FetchType.EAGER)
//	@JoinColumn(name = "id_fermata_salita")
//	FermataDirettriceCorsa fermataSalita;
//
//	@ManyToOne(fetch = FetchType.EAGER)
//	@JoinColumn(name = "id_fermata_discesa")
//	FermataDirettriceCorsa fermataDiscesa;
}
