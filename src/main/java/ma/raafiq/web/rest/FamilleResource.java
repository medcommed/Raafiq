package ma.raafiq.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import ma.raafiq.repository.FamilleRepository;
import ma.raafiq.service.FamilleQueryService;
import ma.raafiq.service.FamilleService;
import ma.raafiq.service.criteria.FamilleCriteria;
import ma.raafiq.service.dto.FamilleDTO;
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
 * REST controller for managing {@link ma.raafiq.domain.Famille}.
 */
@RestController
@RequestMapping("/api")
public class FamilleResource {

    private final Logger log = LoggerFactory.getLogger(FamilleResource.class);

    private static final String ENTITY_NAME = "famille";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FamilleService familleService;

    private final FamilleRepository familleRepository;

    private final FamilleQueryService familleQueryService;

    public FamilleResource(FamilleService familleService, FamilleRepository familleRepository, FamilleQueryService familleQueryService) {
        this.familleService = familleService;
        this.familleRepository = familleRepository;
        this.familleQueryService = familleQueryService;
    }

    /**
     * {@code POST  /familles} : Create a new famille.
     *
     * @param familleDTO the familleDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new familleDTO, or with status {@code 400 (Bad Request)} if the famille has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/familles")
    public ResponseEntity<FamilleDTO> createFamille(@Valid @RequestBody FamilleDTO familleDTO) throws URISyntaxException {
        log.debug("REST request to save Famille : {}", familleDTO);
        if (familleDTO.getId() != null) {
            throw new BadRequestAlertException("A new famille cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FamilleDTO result = familleService.save(familleDTO);
        return ResponseEntity
            .created(new URI("/api/familles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /familles/:id} : Updates an existing famille.
     *
     * @param id the id of the familleDTO to save.
     * @param familleDTO the familleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated familleDTO,
     * or with status {@code 400 (Bad Request)} if the familleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the familleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/familles/{id}")
    public ResponseEntity<FamilleDTO> updateFamille(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FamilleDTO familleDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Famille : {}, {}", id, familleDTO);
        if (familleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, familleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!familleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FamilleDTO result = familleService.save(familleDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, familleDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /familles/:id} : Partial updates given fields of an existing famille, field will ignore if it is null
     *
     * @param id the id of the familleDTO to save.
     * @param familleDTO the familleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated familleDTO,
     * or with status {@code 400 (Bad Request)} if the familleDTO is not valid,
     * or with status {@code 404 (Not Found)} if the familleDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the familleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/familles/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FamilleDTO> partialUpdateFamille(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FamilleDTO familleDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Famille partially : {}, {}", id, familleDTO);
        if (familleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, familleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!familleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FamilleDTO> result = familleService.partialUpdate(familleDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, familleDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /familles} : get all the familles.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of familles in body.
     */
    @GetMapping("/familles")
    public ResponseEntity<List<FamilleDTO>> getAllFamilles(
        FamilleCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Familles by criteria: {}", criteria);
        Page<FamilleDTO> page = familleQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /familles/count} : count all the familles.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/familles/count")
    public ResponseEntity<Long> countFamilles(FamilleCriteria criteria) {
        log.debug("REST request to count Familles by criteria: {}", criteria);
        return ResponseEntity.ok().body(familleQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /familles/:id} : get the "id" famille.
     *
     * @param id the id of the familleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the familleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/familles/{id}")
    public ResponseEntity<FamilleDTO> getFamille(@PathVariable Long id) {
        log.debug("REST request to get Famille : {}", id);
        Optional<FamilleDTO> familleDTO = familleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(familleDTO);
    }

    /**
     * {@code DELETE  /familles/:id} : delete the "id" famille.
     *
     * @param id the id of the familleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/familles/{id}")
    public ResponseEntity<Void> deleteFamille(@PathVariable Long id) {
        log.debug("REST request to delete Famille : {}", id);
        familleService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
