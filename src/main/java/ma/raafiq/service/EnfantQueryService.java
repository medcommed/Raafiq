package ma.raafiq.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import ma.raafiq.domain.*; // for static metamodels
import ma.raafiq.domain.Enfant;
import ma.raafiq.repository.EnfantRepository;
import ma.raafiq.service.criteria.EnfantCriteria;
import ma.raafiq.service.dto.EnfantDTO;
import ma.raafiq.service.mapper.EnfantMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Enfant} entities in the database.
 * The main input is a {@link EnfantCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EnfantDTO} or a {@link Page} of {@link EnfantDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EnfantQueryService extends QueryService<Enfant> {

    private final Logger log = LoggerFactory.getLogger(EnfantQueryService.class);

    private final EnfantRepository enfantRepository;

    private final EnfantMapper enfantMapper;

    public EnfantQueryService(EnfantRepository enfantRepository, EnfantMapper enfantMapper) {
        this.enfantRepository = enfantRepository;
        this.enfantMapper = enfantMapper;
    }

    /**
     * Return a {@link List} of {@link EnfantDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EnfantDTO> findByCriteria(EnfantCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Enfant> specification = createSpecification(criteria);
        return enfantMapper.toDto(enfantRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link EnfantDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EnfantDTO> findByCriteria(EnfantCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Enfant> specification = createSpecification(criteria);
        return enfantRepository.findAll(specification, page).map(enfantMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EnfantCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Enfant> specification = createSpecification(criteria);
        return enfantRepository.count(specification);
    }

    /**
     * Function to convert {@link EnfantCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Enfant> createSpecification(EnfantCriteria criteria) {
        Specification<Enfant> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Enfant_.id));
            }
            if (criteria.getDateDiagnostic() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateDiagnostic(), Enfant_.dateDiagnostic));
            }
            if (criteria.getDateNaissance() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateNaissance(), Enfant_.dateNaissance));
            }
            if (criteria.getDegreAutisme() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDegreAutisme(), Enfant_.degreAutisme));
            }
            if (criteria.getMutualiste() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMutualiste(), Enfant_.mutualiste));
            }
            if (criteria.getNom() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNom(), Enfant_.nom));
            }
            if (criteria.getNomFr() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomFr(), Enfant_.nomFr));
            }
            if (criteria.getNomMedecin() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomMedecin(), Enfant_.nomMedecin));
            }
            if (criteria.getPrenom() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPrenom(), Enfant_.prenom));
            }
            if (criteria.getPrenomfr() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPrenomfr(), Enfant_.prenomfr));
            }
            if (criteria.getScolariser() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getScolariser(), Enfant_.scolariser));
            }
            if (criteria.getSexe() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSexe(), Enfant_.sexe));
            }
            if (criteria.getSpecialiteMedecin() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSpecialiteMedecin(), Enfant_.specialiteMedecin));
            }
            if (criteria.getBeneficiaireId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBeneficiaireId(),
                            root -> root.join(Enfant_.beneficiaires, JoinType.LEFT).get(Beneficiaire_.id)
                        )
                    );
            }
            if (criteria.getFamilleId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getFamilleId(), root -> root.join(Enfant_.familles, JoinType.LEFT).get(Famille_.id))
                    );
            }
            if (criteria.getProfessionnelId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getProfessionnelId(),
                            root -> root.join(Enfant_.professionnels, JoinType.LEFT).get(Professionnel_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
