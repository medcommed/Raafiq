package ma.raafiq.service.mapper;

import ma.raafiq.domain.Famille;
import ma.raafiq.service.dto.FamilleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Famille} and its DTO {@link FamilleDTO}.
 */
@Mapper(componentModel = "spring", uses = { AppUserMapper.class, EnfantMapper.class, MotifRefusMapper.class, ProvinceMapper.class })
public interface FamilleMapper extends EntityMapper<FamilleDTO, Famille> {
    @Mapping(target = "appUser", source = "appUser", qualifiedByName = "id")
    @Mapping(target = "enfant", source = "enfant", qualifiedByName = "id")
    @Mapping(target = "motifRefus", source = "motifRefus", qualifiedByName = "id")
    @Mapping(target = "province", source = "province", qualifiedByName = "id")
    FamilleDTO toDto(Famille s);
}
