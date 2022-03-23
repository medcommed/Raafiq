package ma.raafiq.service;

import java.util.Optional;
import ma.raafiq.domain.AppRole;
import ma.raafiq.repository.AppRoleRepository;
import ma.raafiq.service.dto.AppRoleDTO;
import ma.raafiq.service.mapper.AppRoleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AppRole}.
 */
@Service
@Transactional
public class AppRoleService {

    private final Logger log = LoggerFactory.getLogger(AppRoleService.class);

    private final AppRoleRepository appRoleRepository;

    private final AppRoleMapper appRoleMapper;

    public AppRoleService(AppRoleRepository appRoleRepository, AppRoleMapper appRoleMapper) {
        this.appRoleRepository = appRoleRepository;
        this.appRoleMapper = appRoleMapper;
    }

    /**
     * Save a appRole.
     *
     * @param appRoleDTO the entity to save.
     * @return the persisted entity.
     */
    public AppRoleDTO save(AppRoleDTO appRoleDTO) {
        log.debug("Request to save AppRole : {}", appRoleDTO);
        AppRole appRole = appRoleMapper.toEntity(appRoleDTO);
        appRole = appRoleRepository.save(appRole);
        return appRoleMapper.toDto(appRole);
    }

    /**
     * Partially update a appRole.
     *
     * @param appRoleDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AppRoleDTO> partialUpdate(AppRoleDTO appRoleDTO) {
        log.debug("Request to partially update AppRole : {}", appRoleDTO);

        return appRoleRepository
            .findById(appRoleDTO.getId())
            .map(existingAppRole -> {
                appRoleMapper.partialUpdate(existingAppRole, appRoleDTO);

                return existingAppRole;
            })
            .map(appRoleRepository::save)
            .map(appRoleMapper::toDto);
    }

    /**
     * Get all the appRoles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AppRoleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AppRoles");
        return appRoleRepository.findAll(pageable).map(appRoleMapper::toDto);
    }

    /**
     * Get one appRole by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AppRoleDTO> findOne(Long id) {
        log.debug("Request to get AppRole : {}", id);
        return appRoleRepository.findById(id).map(appRoleMapper::toDto);
    }

    /**
     * Delete the appRole by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete AppRole : {}", id);
        appRoleRepository.deleteById(id);
    }
}
