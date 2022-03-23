package ma.raafiq.service;

import java.util.Optional;
import ma.raafiq.domain.Beneficiaire;
import ma.raafiq.repository.BeneficiaireRepository;
import ma.raafiq.service.dto.BeneficiaireDTO;
import ma.raafiq.service.mapper.BeneficiaireMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Beneficiaire}.
 */
@Service
@Transactional
public class BeneficiaireService {

    private final Logger log = LoggerFactory.getLogger(BeneficiaireService.class);

    private final BeneficiaireRepository beneficiaireRepository;

    private final BeneficiaireMapper beneficiaireMapper;

    public BeneficiaireService(BeneficiaireRepository beneficiaireRepository, BeneficiaireMapper beneficiaireMapper) {
        this.beneficiaireRepository = beneficiaireRepository;
        this.beneficiaireMapper = beneficiaireMapper;
    }

    /**
     * Save a beneficiaire.
     *
     * @param beneficiaireDTO the entity to save.
     * @return the persisted entity.
     */
    public BeneficiaireDTO save(BeneficiaireDTO beneficiaireDTO) {
        log.debug("Request to save Beneficiaire : {}", beneficiaireDTO);
        Beneficiaire beneficiaire = beneficiaireMapper.toEntity(beneficiaireDTO);
        beneficiaire = beneficiaireRepository.save(beneficiaire);
        return beneficiaireMapper.toDto(beneficiaire);
    }

    /**
     * Partially update a beneficiaire.
     *
     * @param beneficiaireDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BeneficiaireDTO> partialUpdate(BeneficiaireDTO beneficiaireDTO) {
        log.debug("Request to partially update Beneficiaire : {}", beneficiaireDTO);

        return beneficiaireRepository
            .findById(beneficiaireDTO.getId())
            .map(existingBeneficiaire -> {
                beneficiaireMapper.partialUpdate(existingBeneficiaire, beneficiaireDTO);

                return existingBeneficiaire;
            })
            .map(beneficiaireRepository::save)
            .map(beneficiaireMapper::toDto);
    }

    /**
     * Get all the beneficiaires.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<BeneficiaireDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Beneficiaires");
        return beneficiaireRepository.findAll(pageable).map(beneficiaireMapper::toDto);
    }

    /**
     * Get one beneficiaire by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BeneficiaireDTO> findOne(Long id) {
        log.debug("Request to get Beneficiaire : {}", id);
        return beneficiaireRepository.findById(id).map(beneficiaireMapper::toDto);
    }

    /**
     * Delete the beneficiaire by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Beneficiaire : {}", id);
        beneficiaireRepository.deleteById(id);
    }
}
