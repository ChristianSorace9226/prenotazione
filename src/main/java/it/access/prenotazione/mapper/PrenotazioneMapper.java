package it.access.prenotazione.mapper;

import it.access.prenotazione.dto.PrenotazioneDTO;
import it.access.prenotazione.model.entity.Prenotazione;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class PrenotazioneMapper {

    public abstract Prenotazione toEntity(PrenotazioneDTO prenotazioneDTO);
    public abstract PrenotazioneDTO toDto(Prenotazione prenotazione);

    public abstract List<Prenotazione> toEntityList(List<PrenotazioneDTO> prenotazioneDTOList);
    public abstract List<PrenotazioneDTO> toDtoList(List<Prenotazione> prenotazioneList);

}
