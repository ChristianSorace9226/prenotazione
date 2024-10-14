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
    Long id;

    @LastModifiedDate
    @Column(name = "data_modifica_record")
    LocalDateTime updatedAt;

    @CreatedDate
    @Column(name = "data_inserimento_record")
    LocalDateTime createdAt = LocalDateTime.now();

    @Version
    @Column(name = "optlck")
    Long version;

    @Column(name = "codice")
    String codice;

//	@Column(name = "flag_prenotato")
//	boolean prenotato;
//
//	@Column(name = "flag_vidimato")
//	boolean vidimato;
//
//	@ManyToOne(fetch = FetchType.EAGER)
//	@JoinColumn(name = "id_posto")
//	Posto posto;
//
//	@ManyToOne(fetch = FetchType.EAGER)
//	@JoinColumn(name = "id_planning")
//	Planning planning;
//
//	@ManyToOne(fetch = FetchType.EAGER)
//	@JoinColumn(name = "id_dip_cliente")
//	DipCliente dipCliente;
//
//	@ManyToOne(fetch = FetchType.EAGER)
//	@JoinColumn(name = "id_fermata_salita")
//	FermataDirettriceCorsa fermataSalita;
//
//	@ManyToOne(fetch = FetchType.EAGER)
//	@JoinColumn(name = "id_fermata_discesa")
//	FermataDirettriceCorsa fermataDiscesa;
}
