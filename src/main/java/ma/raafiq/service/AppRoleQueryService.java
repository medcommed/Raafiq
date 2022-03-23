package ma.raafiq.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import ma.raafiq.domain.*; // for static metamodels
import ma.raafiq.domain.AppRole;
import ma.raafiq.repository.AppRoleRepository;
import ma.raafiq.service.criteria.AppRoleCriteria;
import ma.raafiq.service.dto.AppRoleDTO;
import ma.raafiq.service.mapper.AppRoleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link AppRole} entities in the database.
 * The main input is a {@link AppRoleCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AppRoleDTO} or a {@link Page} of {@link AppRoleDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AppRoleQueryService extends QueryService<AppRole> {

    private final Logger log = LoggerFactory.getLogger(AppRoleQueryService.class);

    private final AppRoleRepository appRoleRepository;

    private final AppRoleMapper appRoleMapper;

    public AppRoleQueryService(AppRoleRepository appRoleRepository, AppRoleMapper appRoleMapper) {
        this.appRoleRepository = appRoleRepository;
        this.appRoleMapper = appRoleMapper;
    }

    /**
     * Return a {@link List} of {@link AppRoleDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AppRoleDTO> findByCriteria(AppRoleCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AppRole> specification = createSpecification(criteria);
        return appRoleMapper.toDto(appRoleRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AppRoleDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AppRoleDTO> findByCriteria(AppRoleCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AppRole> specification = createSpecification(criteria);
        return appRoleRepository.findAll(specification, page).map(appRoleMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AppRoleCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AppRole> specification = createSpecification(criteria);
        return appRoleRepository.count(specification);
    }

    /**
     * Function to convert {@link AppRoleCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AppRole> createSpecification(AppRoleCriteria criteria) {
        Specification<AppRole> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AppRole_.id));
            }
            if (criteria.getLibeleAr() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLibeleAr(), AppRole_.libeleAr));
            }
            if (criteria.getLibeleFr() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLibeleFr(), AppRole_.libeleFr));
            }
            if (criteria.getAppUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getAppUserId(), root -> root.join(AppRole_.appUsers, JoinType.LEFT).get(AppUser_.id))
                    );
            }
        }
        return specification;
    }
}
