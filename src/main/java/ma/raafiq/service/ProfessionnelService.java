package ma.raafiq.service;

import java.util.Optional;
import ma.raafiq.domain.Professionnel;
import ma.raafiq.repository.ProfessionnelRepository;
import ma.raafiq.service.dto.ProfessionnelDTO;
import ma.raafiq.service.mapper.ProfessionnelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Professionnel}.
 */
@Service
@Transactional
public class ProfessionnelService {

    private final Logger log = LoggerFactory.getLogger(ProfessionnelService.class);

    private final ProfessionnelRepository professionnelRepository;

    private final ProfessionnelMapper professionnelMapper;

    public ProfessionnelService(ProfessionnelRepository professionnelRepository, ProfessionnelMapper professionnelMapper) {
        this.professionnelRepository = professionnelRepository;
        this.professionnelMapper = professionnelMapper;
    }

    /**
     * Save a professionnel.
     *
     * @param professionnelDTO the entity to save.
     * @return the persisted entity.
     */
    public ProfessionnelDTO save(ProfessionnelDTO professionnelDTO) {
        log.debug("Request to save Professionnel : {}", professionnelDTO);
        Professionnel professionnel = professionnelMapper.toEntity(professionnelDTO);
        professionnel = professionnelRepository.save(professionnel);
        return professionnelMapper.toDto(professionnel);
    }

    /**
     * Partially update a professionnel.
     *
     * @param professionnelDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProfessionnelDTO> partialUpdate(ProfessionnelDTO professionnelDTO) {
        log.debug("Request to partially update Professionnel : {}", professionnelDTO);

        return professionnelRepository
            .findById(professionnelDTO.getId())
            .map(existingProfessionnel -> {
                professionnelMapper.partialUpdate(existingProfessionnel, professionnelDTO);

                return existingProfessionnel;
            })
            .map(professionnelRepository::save)
            .map(professionnelMapper::toDto);
    }

    /**
     * Get all the professionnels.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ProfessionnelDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Professionnels");
        return professionnelRepository.findAll(pageable).map(professionnelMapper::toDto);
    }

    /**
     * Get one professionnel by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProfessionnelDTO> findOne(Long id) {
        log.debug("Request to get Professionnel : {}", id);
        return professionnelRepository.findById(id).map(professionnelMapper::toDto);
    }

    /**
     * Delete the professionnel by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Professionnel : {}", id);
        professionnelRepository.deleteById(id);
    }
}
