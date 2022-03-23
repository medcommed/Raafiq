package ma.raafiq.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import ma.raafiq.domain.*; // for static metamodels
import ma.raafiq.domain.Famille;
import ma.raafiq.repository.FamilleRepository;
import ma.raafiq.service.criteria.FamilleCriteria;
import ma.raafiq.service.dto.FamilleDTO;
import ma.raafiq.service.mapper.FamilleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Famille} entities in the database.
 * The main input is a {@link FamilleCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FamilleDTO} or a {@link Page} of {@link FamilleDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FamilleQueryService extends QueryService<Famille> {

    private final Logger log = LoggerFactory.getLogger(FamilleQueryService.class);

    private final FamilleRepository familleRepository;

    private final FamilleMapper familleMapper;

    public FamilleQueryService(FamilleRepository familleRepository, FamilleMapper familleMapper) {
        this.familleRepository = familleRepository;
        this.familleMapper = familleMapper;
    }

    /**
     * Return a {@link List} of {@link FamilleDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FamilleDTO> findByCriteria(FamilleCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Famille> specification = createSpecification(criteria);
        return familleMapper.toDto(familleRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FamilleDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FamilleDTO> findByCriteria(FamilleCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Famille> specification = createSpecification(criteria);
        return familleRepository.findAll(specification, page).map(familleMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FamilleCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Famille> specification = createSpecification(criteria);
        return familleRepository.count(specification);
    }

    /**
     * Function to convert {@link FamilleCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Famille> createSpecification(FamilleCriteria criteria) {
        Specification<Famille> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Famille_.id));
            }
            if (criteria.getAdresse() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAdresse(), Famille_.adresse));
            }
            if (criteria.getBenef2019() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBenef2019(), Famille_.benef2019));
            }
            if (criteria.getBenef2020() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBenef2020(), Famille_.benef2020));
            }
            if (criteria.getCin() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCin(), Famille_.cin));
            }
            if (criteria.getDateCreation() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateCreation(), Famille_.dateCreation));
            }
            if (criteria.getDateModification() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateModification(), Famille_.dateModification));
            }
            if (criteria.getDateNaissance() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateNaissance(), Famille_.dateNaissance));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Famille_.email));
            }
            if (criteria.getEtat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEtat(), Famille_.etat));
            }
            if (criteria.getExplicationRefus() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExplicationRefus(), Famille_.explicationRefus));
            }
            if (criteria.getNbrEnfants() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNbrEnfants(), Famille_.nbrEnfants));
            }
            if (criteria.getNiveauScolarite() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNiveauScolarite(), Famille_.niveauScolarite));
            }
            if (criteria.getNom() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNom(), Famille_.nom));
            }
            if (criteria.getNomFr() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomFr(), Famille_.nomFr));
            }
            if (criteria.getNumeroDossier() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNumeroDossier(), Famille_.numeroDossier));
            }
            if (criteria.getPrenom() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPrenom(), Famille_.prenom));
            }
            if (criteria.getPrenomFr() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPrenomFr(), Famille_.prenomFr));
            }
            if (criteria.getProfession() != null) {
                specification = specification.and(buildStringSpecification(criteria.getProfession(), Famille_.profession));
            }
            if (criteria.getSelectionner() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSelectionner(), Famille_.selectionner));
            }
            if (criteria.getSexe() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSexe(), Famille_.sexe));
            }
            if (criteria.getTelephone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTelephone(), Famille_.telephone));
            }
            if (criteria.getAutreBenef2019() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAutreBenef2019(), Famille_.autreBenef2019));
            }
            if (criteria.getAutreBenef2020() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAutreBenef2020(), Famille_.autreBenef2020));
            }
            if (criteria.getRelationFamiliale() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRelationFamiliale(), Famille_.relationFamiliale));
            }
            if (criteria.getAppUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getAppUserId(), root -> root.join(Famille_.appUser, JoinType.LEFT).get(AppUser_.id))
                    );
            }
            if (criteria.getEnfantId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getEnfantId(), root -> root.join(Famille_.enfant, JoinType.LEFT).get(Enfant_.id))
                    );
            }
            if (criteria.getMotifRefusId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getMotifRefusId(),
                            root -> root.join(Famille_.motifRefus, JoinType.LEFT).get(MotifRefus_.id)
                        )
                    );
            }
            if (criteria.getProvinceId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getProvinceId(), root -> root.join(Famille_.province, JoinType.LEFT).get(Province_.id))
                    );
            }
        }
        return specification;
    }
}
