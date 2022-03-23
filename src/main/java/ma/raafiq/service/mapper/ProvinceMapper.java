package ma.raafiq.service.mapper;

import ma.raafiq.domain.Province;
import ma.raafiq.service.dto.ProvinceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Province} and its DTO {@link ProvinceDTO}.
 */
@Mapper(componentModel = "spring", uses = { RegionMapper.class })
public interface ProvinceMapper extends EntityMapper<ProvinceDTO, Province> {
    @Mapping(target = "region", source = "region", qualifiedByName = "id")
    ProvinceDTO toDto(Province s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProvinceDTO toDtoId(Province province);
}
