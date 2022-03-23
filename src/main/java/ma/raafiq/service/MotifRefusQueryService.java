package ma.raafiq.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import ma.raafiq.domain.*; // for static metamodels
import ma.raafiq.domain.MotifRefus;
import ma.raafiq.repository.MotifRefusRepository;
import ma.raafiq.service.criteria.MotifRefusCriteria;
import ma.raafiq.service.dto.MotifRefusDTO;
import ma.raafiq.service.mapper.MotifRefusMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link MotifRefus} entities in the database.
 * The main input is a {@link MotifRefusCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MotifRefusDTO} or a {@link Page} of {@link MotifRefusDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MotifRefusQueryService extends QueryService<MotifRefus> {

    private final Logger log = LoggerFactory.getLogger(MotifRefusQueryService.class);

    private final MotifRefusRepository motifRefusRepository;

    private final MotifRefusMapper motifRefusMapper;

    public MotifRefusQueryService(MotifRefusRepository motifRefusRepository, MotifRefusMapper motifRefusMapper) {
        this.motifRefusRepository = motifRefusRepository;
        this.motifRefusMapper = motifRefusMapper;
    }

    /**
     * Return a {@link List} of {@link MotifRefusDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MotifRefusDTO> findByCriteria(MotifRefusCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<MotifRefus> specification = createSpecification(criteria);
        return motifRefusMapper.toDto(motifRefusRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link MotifRefusDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MotifRefusDTO> findByCriteria(MotifRefusCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MotifRefus> specification = createSpecification(criteria);
        return motifRefusRepository.findAll(specification, page).map(motifRefusMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MotifRefusCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<MotifRefus> specification = createSpecification(criteria);
        return motifRefusRepository.count(specification);
    }

    /**
     * Function to convert {@link MotifRefusCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MotifRefus> createSpecification(MotifRefusCriteria criteria) {
        Specification<MotifRefus> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), MotifRefus_.id));
            }
            if (criteria.getLibeleAr() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLibeleAr(), MotifRefus_.libeleAr));
            }
            if (criteria.getLibeleFr() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLibeleFr(), MotifRefus_.libeleFr));
            }
            if (criteria.getBeneficiaireId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBeneficiaireId(),
                            root -> root.join(MotifRefus_.beneficiaires, JoinType.LEFT).get(Beneficiaire_.id)
                        )
                    );
            }
            if (criteria.getFamilleId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getFamilleId(), root -> root.join(MotifRefus_.familles, JoinType.LEFT).get(Famille_.id))
                    );
            }
            if (criteria.getProfessionnelId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getProfessionnelId(),
                            root -> root.join(MotifRefus_.professionnels, JoinType.LEFT).get(Professionnel_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
