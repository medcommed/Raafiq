package ma.raafiq.service;

import java.util.Optional;
import ma.raafiq.domain.MotifRefus;
import ma.raafiq.repository.MotifRefusRepository;
import ma.raafiq.service.dto.MotifRefusDTO;
import ma.raafiq.service.mapper.MotifRefusMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link MotifRefus}.
 */
@Service
@Transactional
public class MotifRefusService {

    private final Logger log = LoggerFactory.getLogger(MotifRefusService.class);

    private final MotifRefusRepository motifRefusRepository;

    private final MotifRefusMapper motifRefusMapper;

    public MotifRefusService(MotifRefusRepository motifRefusRepository, MotifRefusMapper motifRefusMapper) {
        this.motifRefusRepository = motifRefusRepository;
        this.motifRefusMapper = motifRefusMapper;
    }

    /**
     * Save a motifRefus.
     *
     * @param motifRefusDTO the entity to save.
     * @return the persisted entity.
     */
    public MotifRefusDTO save(MotifRefusDTO motifRefusDTO) {
        log.debug("Request to save MotifRefus : {}", motifRefusDTO);
        MotifRefus motifRefus = motifRefusMapper.toEntity(motifRefusDTO);
        motifRefus = motifRefusRepository.save(motifRefus);
        return motifRefusMapper.toDto(motifRefus);
    }

    /**
     * Partially update a motifRefus.
     *
     * @param motifRefusDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MotifRefusDTO> partialUpdate(MotifRefusDTO motifRefusDTO) {
        log.debug("Request to partially update MotifRefus : {}", motifRefusDTO);

        return motifRefusRepository
            .findById(motifRefusDTO.getId())
            .map(existingMotifRefus -> {
                motifRefusMapper.partialUpdate(existingMotifRefus, motifRefusDTO);

                return existingMotifRefus;
            })
            .map(motifRefusRepository::save)
            .map(motifRefusMapper::toDto);
    }

    /**
     * Get all the motifRefuses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MotifRefusDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MotifRefuses");
        return motifRefusRepository.findAll(pageable).map(motifRefusMapper::toDto);
    }

    /**
     * Get one motifRefus by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MotifRefusDTO> findOne(Long id) {
        log.debug("Request to get MotifRefus : {}", id);
        return motifRefusRepository.findById(id).map(motifRefusMapper::toDto);
    }

    /**
     * Delete the motifRefus by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete MotifRefus : {}", id);
        motifRefusRepository.deleteById(id);
    }
}
