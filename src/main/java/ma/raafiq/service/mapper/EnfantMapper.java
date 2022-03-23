package ma.raafiq.service.mapper;

import ma.raafiq.domain.Enfant;
import ma.raafiq.service.dto.EnfantDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Enfant} and its DTO {@link EnfantDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface EnfantMapper extends EntityMapper<EnfantDTO, Enfant> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EnfantDTO toDtoId(Enfant enfant);
}
