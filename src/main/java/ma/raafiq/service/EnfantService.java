package ma.raafiq.service;

import java.util.Optional;
import ma.raafiq.domain.Enfant;
import ma.raafiq.repository.EnfantRepository;
import ma.raafiq.service.dto.EnfantDTO;
import ma.raafiq.service.mapper.EnfantMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Enfant}.
 */
@Service
@Transactional
public class EnfantService {

    private final Logger log = LoggerFactory.getLogger(EnfantService.class);

    private final EnfantRepository enfantRepository;

    private final EnfantMapper enfantMapper;

    public EnfantService(EnfantRepository enfantRepository, EnfantMapper enfantMapper) {
        this.enfantRepository = enfantRepository;
        this.enfantMapper = enfantMapper;
    }

    /**
     * Save a enfant.
     *
     * @param enfantDTO the entity to save.
     * @return the persisted entity.
     */
    public EnfantDTO save(EnfantDTO enfantDTO) {
        log.debug("Request to save Enfant : {}", enfantDTO);
        Enfant enfant = enfantMapper.toEntity(enfantDTO);
        enfant = enfantRepository.save(enfant);
        return enfantMapper.toDto(enfant);
    }

    /**
     * Partially update a enfant.
     *
     * @param enfantDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EnfantDTO> partialUpdate(EnfantDTO enfantDTO) {
        log.debug("Request to partially update Enfant : {}", enfantDTO);

        return enfantRepository
            .findById(enfantDTO.getId())
            .map(existingEnfant -> {
                enfantMapper.partialUpdate(existingEnfant, enfantDTO);

                return existingEnfant;
            })
            .map(enfantRepository::save)
            .map(enfantMapper::toDto);
    }

    /**
     * Get all the enfants.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<EnfantDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Enfants");
        return enfantRepository.findAll(pageable).map(enfantMapper::toDto);
    }

    /**
     * Get one enfant by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EnfantDTO> findOne(Long id) {
        log.debug("Request to get Enfant : {}", id);
        return enfantRepository.findById(id).map(enfantMapper::toDto);
    }

    /**
     * Delete the enfant by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Enfant : {}", id);
        enfantRepository.deleteById(id);
    }
}
