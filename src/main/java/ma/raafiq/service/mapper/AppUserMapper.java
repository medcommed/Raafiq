package ma.raafiq.service.mapper;

import ma.raafiq.domain.AppUser;
import ma.raafiq.service.dto.AppUserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AppUser} and its DTO {@link AppUserDTO}.
 */
@Mapper(componentModel = "spring", uses = { AppRoleMapper.class, ProvinceMapper.class })
public interface AppUserMapper extends EntityMapper<AppUserDTO, AppUser> {
    @Mapping(target = "appRole", source = "appRole", qualifiedByName = "id")
    @Mapping(target = "province", source = "province", qualifiedByName = "id")
    AppUserDTO toDto(AppUser s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AppUserDTO toDtoId(AppUser appUser);
}
