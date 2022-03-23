package ma.raafiq.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import ma.raafiq.domain.*; // for static metamodels
import ma.raafiq.domain.Professionnel;
import ma.raafiq.repository.ProfessionnelRepository;
import ma.raafiq.service.criteria.ProfessionnelCriteria;
import ma.raafiq.service.dto.ProfessionnelDTO;
import ma.raafiq.service.mapper.ProfessionnelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Professionnel} entities in the database.
 * The main input is a {@link ProfessionnelCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProfessionnelDTO} or a {@link Page} of {@link ProfessionnelDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProfessionnelQueryService extends QueryService<Professionnel> {

    private final Logger log = LoggerFactory.getLogger(ProfessionnelQueryService.class);

    private final ProfessionnelRepository professionnelRepository;

    private final ProfessionnelMapper professionnelMapper;

    public ProfessionnelQueryService(ProfessionnelRepository professionnelRepository, ProfessionnelMapper professionnelMapper) {
        this.professionnelRepository = professionnelRepository;
        this.professionnelMapper = professionnelMapper;
    }

    /**
     * Return a {@link List} of {@link ProfessionnelDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProfessionnelDTO> findByCriteria(ProfessionnelCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Professionnel> specification = createSpecification(criteria);
        return professionnelMapper.toDto(professionnelRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ProfessionnelDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProfessionnelDTO> findByCriteria(ProfessionnelCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Professionnel> specification = createSpecification(criteria);
        return professionnelRepository.findAll(specification, page).map(professionnelMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProfessionnelCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Professionnel> specification = createSpecification(criteria);
        return professionnelRepository.count(specification);
    }

    /**
     * Function to convert {@link ProfessionnelCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Professionnel> createSpecification(ProfessionnelCriteria criteria) {
        Specification<Professionnel> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Professionnel_.id));
            }
            if (criteria.getAdresse() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAdresse(), Professionnel_.adresse));
            }
            if (criteria.getBenef2019() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBenef2019(), Professionnel_.benef2019));
            }
            if (criteria.getBenef2020() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBenef2020(), Professionnel_.benef2020));
            }
            if (criteria.getCin() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCin(), Professionnel_.cin));
            }
            if (criteria.getDateCreation() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateCreation(), Professionnel_.dateCreation));
            }
            if (criteria.getDateModification() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateModification(), Professionnel_.dateModification));
            }
            if (criteria.getDateNaissance() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateNaissance(), Professionnel_.dateNaissance));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Professionnel_.email));
            }
            if (criteria.getEtat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEtat(), Professionnel_.etat));
            }
            if (criteria.getExplicationRefus() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getExplicationRefus(), Professionnel_.explicationRefus));
            }
            if (criteria.getNbrEnfants() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNbrEnfants(), Professionnel_.nbrEnfants));
            }
            if (criteria.getNiveauScolarite() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNiveauScolarite(), Professionnel_.niveauScolarite));
            }
            if (criteria.getNom() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNom(), Professionnel_.nom));
            }
            if (criteria.getNomFr() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomFr(), Professionnel_.nomFr));
            }
            if (criteria.getNumeroDossier() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNumeroDossier(), Professionnel_.numeroDossier));
            }
            if (criteria.getPrenom() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPrenom(), Professionnel_.prenom));
            }
            if (criteria.getPrenomFr() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPrenomFr(), Professionnel_.prenomFr));
            }
            if (criteria.getProfession() != null) {
                specification = specification.and(buildStringSpecification(criteria.getProfession(), Professionnel_.profession));
            }
            if (criteria.getSelectionner() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSelectionner(), Professionnel_.selectionner));
            }
            if (criteria.getSexe() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSexe(), Professionnel_.sexe));
            }
            if (criteria.getTelephone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTelephone(), Professionnel_.telephone));
            }
            if (criteria.getLieuTravailProfessionnel() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getLieuTravailProfessionnel(), Professionnel_.lieuTravailProfessionnel)
                    );
            }
            if (criteria.getSpecialiteProfessionnel() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getSpecialiteProfessionnel(), Professionnel_.specialiteProfessionnel)
                    );
            }
            if (criteria.getAppUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAppUserId(),
                            root -> root.join(Professionnel_.appUser, JoinType.LEFT).get(AppUser_.id)
                        )
                    );
            }
            if (criteria.getEnfantId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getEnfantId(), root -> root.join(Professionnel_.enfant, JoinType.LEFT).get(Enfant_.id))
                    );
            }
            if (criteria.getMotifRefusId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getMotifRefusId(),
                            root -> root.join(Professionnel_.motifRefus, JoinType.LEFT).get(MotifRefus_.id)
                        )
                    );
            }
            if (criteria.getProvinceId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getProvinceId(),
                            root -> root.join(Professionnel_.province, JoinType.LEFT).get(Province_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
