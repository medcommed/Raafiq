package ma.raafiq.service.mapper;

import ma.raafiq.domain.AppRole;
import ma.raafiq.service.dto.AppRoleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AppRole} and its DTO {@link AppRoleDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AppRoleMapper extends EntityMapper<AppRoleDTO, AppRole> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AppRoleDTO toDtoId(AppRole appRole);
}
