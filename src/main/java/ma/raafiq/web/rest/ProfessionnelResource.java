package ma.raafiq.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import ma.raafiq.repository.ProfessionnelRepository;
import ma.raafiq.service.ProfessionnelQueryService;
import ma.raafiq.service.ProfessionnelService;
import ma.raafiq.service.criteria.ProfessionnelCriteria;
import ma.raafiq.service.dto.ProfessionnelDTO;
import ma.raafiq.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ma.raafiq.domain.Professionnel}.
 */
@RestController
@RequestMapping("/api")
public class ProfessionnelResource {

    private final Logger log = LoggerFactory.getLogger(ProfessionnelResource.class);

    private static final String ENTITY_NAME = "professionnel";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProfessionnelService professionnelService;

    private final ProfessionnelRepository professionnelRepository;

    private final ProfessionnelQueryService professionnelQueryService;

    public ProfessionnelResource(
        ProfessionnelService professionnelService,
        ProfessionnelRepository professionnelRepository,
        ProfessionnelQueryService professionnelQueryService
    ) {
        this.professionnelService = professionnelService;
        this.professionnelRepository = professionnelRepository;
        this.professionnelQueryService = professionnelQueryService;
    }

    /**
     * {@code POST  /professionnels} : Create a new professionnel.
     *
     * @param professionnelDTO the professionnelDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new professionnelDTO, or with status {@code 400 (Bad Request)} if the professionnel has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/professionnels")
    public ResponseEntity<ProfessionnelDTO> createProfessionnel(@Valid @RequestBody ProfessionnelDTO professionnelDTO)
        throws URISyntaxException {
        log.debug("REST request to save Professionnel : {}", professionnelDTO);
        if (professionnelDTO.getId() != null) {
            throw new BadRequestAlertException("A new professionnel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProfessionnelDTO result = professionnelService.save(professionnelDTO);
        return ResponseEntity
            .created(new URI("/api/professionnels/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /professionnels/:id} : Updates an existing professionnel.
     *
     * @param id the id of the professionnelDTO to save.
     * @param professionnelDTO the professionnelDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated professionnelDTO,
     * or with status {@code 400 (Bad Request)} if the professionnelDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the professionnelDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/professionnels/{id}")
    public ResponseEntity<ProfessionnelDTO> updateProfessionnel(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProfessionnelDTO professionnelDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Professionnel : {}, {}", id, professionnelDTO);
        if (professionnelDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, professionnelDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!professionnelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProfessionnelDTO result = professionnelService.save(professionnelDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, professionnelDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /professionnels/:id} : Partial updates given fields of an existing professionnel, field will ignore if it is null
     *
     * @param id the id of the professionnelDTO to save.
     * @param professionnelDTO the professionnelDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated professionnelDTO,
     * or with status {@code 400 (Bad Request)} if the professionnelDTO is not valid,
     * or with status {@code 404 (Not Found)} if the professionnelDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the professionnelDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/professionnels/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProfessionnelDTO> partialUpdateProfessionnel(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProfessionnelDTO professionnelDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Professionnel partially : {}, {}", id, professionnelDTO);
        if (professionnelDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, professionnelDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!professionnelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProfessionnelDTO> result = professionnelService.partialUpdate(professionnelDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, professionnelDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /professionnels} : get all the professionnels.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of professionnels in body.
     */
    @GetMapping("/professionnels")
    public ResponseEntity<List<ProfessionnelDTO>> getAllProfessionnels(
        ProfessionnelCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Professionnels by criteria: {}", criteria);
        Page<ProfessionnelDTO> page = professionnelQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /professionnels/count} : count all the professionnels.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/professionnels/count")
    public ResponseEntity<Long> countProfessionnels(ProfessionnelCriteria criteria) {
        log.debug("REST request to count Professionnels by criteria: {}", criteria);
        return ResponseEntity.ok().body(professionnelQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /professionnels/:id} : get the "id" professionnel.
     *
     * @param id the id of the professionnelDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the professionnelDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/professionnels/{id}")
    public ResponseEntity<ProfessionnelDTO> getProfessionnel(@PathVariable Long id) {
        log.debug("REST request to get Professionnel : {}", id);
        Optional<ProfessionnelDTO> professionnelDTO = professionnelService.findOne(id);
        return ResponseUtil.wrapOrNotFound(professionnelDTO);
    }

    /**
     * {@code DELETE  /professionnels/:id} : delete the "id" professionnel.
     *
     * @param id the id of the professionnelDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/professionnels/{id}")
    public ResponseEntity<Void> deleteProfessionnel(@PathVariable Long id) {
        log.debug("REST request to delete Professionnel : {}", id);
        professionnelService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
