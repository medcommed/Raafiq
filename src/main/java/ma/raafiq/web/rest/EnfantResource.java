package ma.raafiq.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import ma.raafiq.repository.EnfantRepository;
import ma.raafiq.service.EnfantQueryService;
import ma.raafiq.service.EnfantService;
import ma.raafiq.service.criteria.EnfantCriteria;
import ma.raafiq.service.dto.EnfantDTO;
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
 * REST controller for managing {@link ma.raafiq.domain.Enfant}.
 */
@RestController
@RequestMapping("/api")
public class EnfantResource {

    private final Logger log = LoggerFactory.getLogger(EnfantResource.class);

    private static final String ENTITY_NAME = "enfant";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EnfantService enfantService;

    private final EnfantRepository enfantRepository;

    private final EnfantQueryService enfantQueryService;

    public EnfantResource(EnfantService enfantService, EnfantRepository enfantRepository, EnfantQueryService enfantQueryService) {
        this.enfantService = enfantService;
        this.enfantRepository = enfantRepository;
        this.enfantQueryService = enfantQueryService;
    }

    /**
     * {@code POST  /enfants} : Create a new enfant.
     *
     * @param enfantDTO the enfantDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new enfantDTO, or with status {@code 400 (Bad Request)} if the enfant has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/enfants")
    public ResponseEntity<EnfantDTO> createEnfant(@Valid @RequestBody EnfantDTO enfantDTO) throws URISyntaxException {
        log.debug("REST request to save Enfant : {}", enfantDTO);
        if (enfantDTO.getId() != null) {
            throw new BadRequestAlertException("A new enfant cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EnfantDTO result = enfantService.save(enfantDTO);
        return ResponseEntity
            .created(new URI("/api/enfants/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /enfants/:id} : Updates an existing enfant.
     *
     * @param id the id of the enfantDTO to save.
     * @param enfantDTO the enfantDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated enfantDTO,
     * or with status {@code 400 (Bad Request)} if the enfantDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the enfantDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/enfants/{id}")
    public ResponseEntity<EnfantDTO> updateEnfant(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EnfantDTO enfantDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Enfant : {}, {}", id, enfantDTO);
        if (enfantDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, enfantDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!enfantRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EnfantDTO result = enfantService.save(enfantDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, enfantDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /enfants/:id} : Partial updates given fields of an existing enfant, field will ignore if it is null
     *
     * @param id the id of the enfantDTO to save.
     * @param enfantDTO the enfantDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated enfantDTO,
     * or with status {@code 400 (Bad Request)} if the enfantDTO is not valid,
     * or with status {@code 404 (Not Found)} if the enfantDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the enfantDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/enfants/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EnfantDTO> partialUpdateEnfant(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EnfantDTO enfantDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Enfant partially : {}, {}", id, enfantDTO);
        if (enfantDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, enfantDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!enfantRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EnfantDTO> result = enfantService.partialUpdate(enfantDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, enfantDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /enfants} : get all the enfants.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of enfants in body.
     */
    @GetMapping("/enfants")
    public ResponseEntity<List<EnfantDTO>> getAllEnfants(
        EnfantCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Enfants by criteria: {}", criteria);
        Page<EnfantDTO> page = enfantQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /enfants/count} : count all the enfants.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/enfants/count")
    public ResponseEntity<Long> countEnfants(EnfantCriteria criteria) {
        log.debug("REST request to count Enfants by criteria: {}", criteria);
        return ResponseEntity.ok().body(enfantQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /enfants/:id} : get the "id" enfant.
     *
     * @param id the id of the enfantDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the enfantDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/enfants/{id}")
    public ResponseEntity<EnfantDTO> getEnfant(@PathVariable Long id) {
        log.debug("REST request to get Enfant : {}", id);
        Optional<EnfantDTO> enfantDTO = enfantService.findOne(id);
        return ResponseUtil.wrapOrNotFound(enfantDTO);
    }

    /**
     * {@code DELETE  /enfants/:id} : delete the "id" enfant.
     *
     * @param id the id of the enfantDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/enfants/{id}")
    public ResponseEntity<Void> deleteEnfant(@PathVariable Long id) {
        log.debug("REST request to delete Enfant : {}", id);
        enfantService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
