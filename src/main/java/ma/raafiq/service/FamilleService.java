package ma.raafiq.service;

import java.util.Optional;
import ma.raafiq.domain.Famille;
import ma.raafiq.repository.FamilleRepository;
import ma.raafiq.service.dto.FamilleDTO;
import ma.raafiq.service.mapper.FamilleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Famille}.
 */
@Service
@Transactional
public class FamilleService {

    private final Logger log = LoggerFactory.getLogger(FamilleService.class);

    private final FamilleRepository familleRepository;

    private final FamilleMapper familleMapper;

    public FamilleService(FamilleRepository familleRepository, FamilleMapper familleMapper) {
        this.familleRepository = familleRepository;
        this.familleMapper = familleMapper;
    }

    /**
     * Save a famille.
     *
     * @param familleDTO the entity to save.
     * @return the persisted entity.
     */
    public FamilleDTO save(FamilleDTO familleDTO) {
        log.debug("Request to save Famille : {}", familleDTO);
        Famille famille = familleMapper.toEntity(familleDTO);
        famille = familleRepository.save(famille);
        return familleMapper.toDto(famille);
    }

    /**
     * Partially update a famille.
     *
     * @param familleDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FamilleDTO> partialUpdate(FamilleDTO familleDTO) {
        log.debug("Request to partially update Famille : {}", familleDTO);

        return familleRepository
            .findById(familleDTO.getId())
            .map(existingFamille -> {
                familleMapper.partialUpdate(existingFamille, familleDTO);

                return existingFamille;
            })
            .map(familleRepository::save)
            .map(familleMapper::toDto);
    }

    /**
     * Get all the familles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<FamilleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Familles");
        return familleRepository.findAll(pageable).map(familleMapper::toDto);
    }

    /**
     * Get one famille by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FamilleDTO> findOne(Long id) {
        log.debug("Request to get Famille : {}", id);
        return familleRepository.findById(id).map(familleMapper::toDto);
    }

    /**
     * Delete the famille by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Famille : {}", id);
        familleRepository.deleteById(id);
    }
}
