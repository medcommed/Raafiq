package ma.raafiq.service.mapper;

import ma.raafiq.domain.Professionnel;
import ma.raafiq.service.dto.ProfessionnelDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Professionnel} and its DTO {@link ProfessionnelDTO}.
 */
@Mapper(componentModel = "spring", uses = { AppUserMapper.class, EnfantMapper.class, MotifRefusMapper.class, ProvinceMapper.class })
public interface ProfessionnelMapper extends EntityMapper<ProfessionnelDTO, Professionnel> {
    @Mapping(target = "appUser", source = "appUser", qualifiedByName = "id")
    @Mapping(target = "enfant", source = "enfant", qualifiedByName = "id")
    @Mapping(target = "motifRefus", source = "motifRefus", qualifiedByName = "id")
    @Mapping(target = "province", source = "province", qualifiedByName = "id")
    ProfessionnelDTO toDto(Professionnel s);
}
