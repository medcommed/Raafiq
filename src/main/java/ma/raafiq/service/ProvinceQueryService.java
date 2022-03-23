package ma.raafiq.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import ma.raafiq.domain.*; // for static metamodels
import ma.raafiq.domain.Province;
import ma.raafiq.repository.ProvinceRepository;
import ma.raafiq.service.criteria.ProvinceCriteria;
import ma.raafiq.service.dto.ProvinceDTO;
import ma.raafiq.service.mapper.ProvinceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Province} entities in the database.
 * The main input is a {@link ProvinceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProvinceDTO} or a {@link Page} of {@link ProvinceDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProvinceQueryService extends QueryService<Province> {

    private final Logger log = LoggerFactory.getLogger(ProvinceQueryService.class);

    private final ProvinceRepository provinceRepository;

    private final ProvinceMapper provinceMapper;

    public ProvinceQueryService(ProvinceRepository provinceRepository, ProvinceMapper provinceMapper) {
        this.provinceRepository = provinceRepository;
        this.provinceMapper = provinceMapper;
    }

    /**
     * Return a {@link List} of {@link ProvinceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProvinceDTO> findByCriteria(ProvinceCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Province> specification = createSpecification(criteria);
        return provinceMapper.toDto(provinceRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ProvinceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProvinceDTO> findByCriteria(ProvinceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Province> specification = createSpecification(criteria);
        return provinceRepository.findAll(specification, page).map(provinceMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProvinceCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Province> specification = createSpecification(criteria);
        return provinceRepository.count(specification);
    }

    /**
     * Function to convert {@link ProvinceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Province> createSpecification(ProvinceCriteria criteria) {
        Specification<Province> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Province_.id));
            }
            if (criteria.getLibeleAr() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLibeleAr(), Province_.libeleAr));
            }
            if (criteria.getLibeleFr() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLibeleFr(), Province_.libeleFr));
            }
            if (criteria.getRegionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getRegionId(), root -> root.join(Province_.region, JoinType.LEFT).get(Region_.id))
                    );
            }
            if (criteria.getAppUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getAppUserId(), root -> root.join(Province_.appUsers, JoinType.LEFT).get(AppUser_.id))
                    );
            }
            if (criteria.getBeneficiaireId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBeneficiaireId(),
                            root -> root.join(Province_.beneficiaires, JoinType.LEFT).get(Beneficiaire_.id)
                        )
                    );
            }
            if (criteria.getFamilleId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getFamilleId(), root -> root.join(Province_.familles, JoinType.LEFT).get(Famille_.id))
                    );
            }
            if (criteria.getProfessionnelId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getProfessionnelId(),
                            root -> root.join(Province_.professionnels, JoinType.LEFT).get(Professionnel_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
