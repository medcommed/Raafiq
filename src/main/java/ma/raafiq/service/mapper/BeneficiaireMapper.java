package ma.raafiq.service.mapper;

import ma.raafiq.domain.Beneficiaire;
import ma.raafiq.service.dto.BeneficiaireDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Beneficiaire} and its DTO {@link BeneficiaireDTO}.
 */
@Mapper(componentModel = "spring", uses = { AppUserMapper.class, EnfantMapper.class, MotifRefusMapper.class, ProvinceMapper.class })
public interface BeneficiaireMapper extends EntityMapper<BeneficiaireDTO, Beneficiaire> {
    @Mapping(target = "appUser", source = "appUser", qualifiedByName = "id")
    @Mapping(target = "enfant", source = "enfant", qualifiedByName = "id")
    @Mapping(target = "motifRefus", source = "motifRefus", qualifiedByName = "id")
    @Mapping(target = "province", source = "province", qualifiedByName = "id")
    BeneficiaireDTO toDto(Beneficiaire s);
}
