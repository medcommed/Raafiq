package ma.raafiq.service.mapper;

import ma.raafiq.domain.MotifRefus;
import ma.raafiq.service.dto.MotifRefusDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MotifRefus} and its DTO {@link MotifRefusDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MotifRefusMapper extends EntityMapper<MotifRefusDTO, MotifRefus> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MotifRefusDTO toDtoId(MotifRefus motifRefus);
}
