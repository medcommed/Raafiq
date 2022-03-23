package ma.raafiq.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import ma.raafiq.domain.*; // for static metamodels
import ma.raafiq.domain.Beneficiaire;
import ma.raafiq.repository.BeneficiaireRepository;
import ma.raafiq.service.criteria.BeneficiaireCriteria;
import ma.raafiq.service.dto.BeneficiaireDTO;
import ma.raafiq.service.mapper.BeneficiaireMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Beneficiaire} entities in the database.
 * The main input is a {@link BeneficiaireCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link BeneficiaireDTO} or a {@link Page} of {@link BeneficiaireDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BeneficiaireQueryService extends QueryService<Beneficiaire> {

    private final Logger log = LoggerFactory.getLogger(BeneficiaireQueryService.class);

    private final BeneficiaireRepository beneficiaireRepository;

    private final BeneficiaireMapper beneficiaireMapper;

    public BeneficiaireQueryService(BeneficiaireRepository beneficiaireRepository, BeneficiaireMapper beneficiaireMapper) {
        this.beneficiaireRepository = beneficiaireRepository;
        this.beneficiaireMapper = beneficiaireMapper;
    }

    /**
     * Return a {@link List} of {@link BeneficiaireDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BeneficiaireDTO> findByCriteria(BeneficiaireCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Beneficiaire> specification = createSpecification(criteria);
        return beneficiaireMapper.toDto(beneficiaireRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link BeneficiaireDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BeneficiaireDTO> findByCriteria(BeneficiaireCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Beneficiaire> specification = createSpecification(criteria);
        return beneficiaireRepository.findAll(specification, page).map(beneficiaireMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BeneficiaireCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Beneficiaire> specification = createSpecification(criteria);
        return beneficiaireRepository.count(specification);
    }

    /**
     * Function to convert {@link BeneficiaireCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Beneficiaire> createSpecification(BeneficiaireCriteria criteria) {
        Specification<Beneficiaire> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Beneficiaire_.id));
            }
            if (criteria.getTypeBeneficiare() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTypeBeneficiare(), Beneficiaire_.typeBeneficiare));
            }
            if (criteria.getAdresse() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAdresse(), Beneficiaire_.adresse));
            }
            if (criteria.getBenef2019() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBenef2019(), Beneficiaire_.benef2019));
            }
            if (criteria.getBenef2020() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBenef2020(), Beneficiaire_.benef2020));
            }
            if (criteria.getCin() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCin(), Beneficiaire_.cin));
            }
            if (criteria.getDateCreation() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateCreation(), Beneficiaire_.dateCreation));
            }
            if (criteria.getDateModification() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateModification(), Beneficiaire_.dateModification));
            }
            if (criteria.getDateNaissance() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateNaissance(), Beneficiaire_.dateNaissance));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Beneficiaire_.email));
            }
            if (criteria.getEtat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEtat(), Beneficiaire_.etat));
            }
            if (criteria.getExplicationRefus() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExplicationRefus(), Beneficiaire_.explicationRefus));
            }
            if (criteria.getNbrEnfants() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNbrEnfants(), Beneficiaire_.nbrEnfants));
            }
            if (criteria.getNiveauScolarite() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNiveauScolarite(), Beneficiaire_.niveauScolarite));
            }
            if (criteria.getNom() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNom(), Beneficiaire_.nom));
            }
            if (criteria.getNomFr() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomFr(), Beneficiaire_.nomFr));
            }
            if (criteria.getNumeroDossier() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNumeroDossier(), Beneficiaire_.numeroDossier));
            }
            if (criteria.getPrenom() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPrenom(), Beneficiaire_.prenom));
            }
            if (criteria.getPrenomFr() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPrenomFr(), Beneficiaire_.prenomFr));
            }
            if (criteria.getProfession() != null) {
                specification = specification.and(buildStringSpecification(criteria.getProfession(), Beneficiaire_.profession));
            }
            if (criteria.getSelectionner() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSelectionner(), Beneficiaire_.selectionner));
            }
            if (criteria.getSexe() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSexe(), Beneficiaire_.sexe));
            }
            if (criteria.getTelephone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTelephone(), Beneficiaire_.telephone));
            }
            if (criteria.getAutreBenef2019() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAutreBenef2019(), Beneficiaire_.autreBenef2019));
            }
            if (criteria.getAutreBenef2020() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAutreBenef2020(), Beneficiaire_.autreBenef2020));
            }
            if (criteria.getRelationFamiliale() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getRelationFamiliale(), Beneficiaire_.relationFamiliale));
            }
            if (criteria.getLieuTravailProfessionnel() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getLieuTravailProfessionnel(), Beneficiaire_.lieuTravailProfessionnel)
                    );
            }
            if (criteria.getSpecialiteProfessionnel() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getSpecialiteProfessionnel(), Beneficiaire_.specialiteProfessionnel)
                    );
            }
            if (criteria.getAppUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAppUserId(),
                            root -> root.join(Beneficiaire_.appUser, JoinType.LEFT).get(AppUser_.id)
                        )
                    );
            }
            if (criteria.getEnfantId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getEnfantId(), root -> root.join(Beneficiaire_.enfant, JoinType.LEFT).get(Enfant_.id))
                    );
            }
            if (criteria.getMotifRefusId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getMotifRefusId(),
                            root -> root.join(Beneficiaire_.motifRefus, JoinType.LEFT).get(MotifRefus_.id)
                        )
                    );
            }
            if (criteria.getProvinceId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getProvinceId(),
                            root -> root.join(Beneficiaire_.province, JoinType.LEFT).get(Province_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
