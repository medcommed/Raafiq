package ma.raafiq.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import ma.raafiq.repository.MotifRefusRepository;
import ma.raafiq.service.MotifRefusQueryService;
import ma.raafiq.service.MotifRefusService;
import ma.raafiq.service.criteria.MotifRefusCriteria;
import ma.raafiq.service.dto.MotifRefusDTO;
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
 * REST controller for managing {@link ma.raafiq.domain.MotifRefus}.
 */
@RestController
@RequestMapping("/api")
public class MotifRefusResource {

    private final Logger log = LoggerFactory.getLogger(MotifRefusResource.class);

    private static final String ENTITY_NAME = "motifRefus";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MotifRefusService motifRefusService;

    private final MotifRefusRepository motifRefusRepository;

    private final MotifRefusQueryService motifRefusQueryService;

    public MotifRefusResource(
        MotifRefusService motifRefusService,
        MotifRefusRepository motifRefusRepository,
        MotifRefusQueryService motifRefusQueryService
    ) {
        this.motifRefusService = motifRefusService;
        this.motifRefusRepository = motifRefusRepository;
        this.motifRefusQueryService = motifRefusQueryService;
    }

    /**
     * {@code POST  /motif-refuses} : Create a new motifRefus.
     *
     * @param motifRefusDTO the motifRefusDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new motifRefusDTO, or with status {@code 400 (Bad Request)} if the motifRefus has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/motif-refuses")
    public ResponseEntity<MotifRefusDTO> createMotifRefus(@Valid @RequestBody MotifRefusDTO motifRefusDTO) throws URISyntaxException {
        log.debug("REST request to save MotifRefus : {}", motifRefusDTO);
        if (motifRefusDTO.getId() != null) {
            throw new BadRequestAlertException("A new motifRefus cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MotifRefusDTO result = motifRefusService.save(motifRefusDTO);
        return ResponseEntity
            .created(new URI("/api/motif-refuses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /motif-refuses/:id} : Updates an existing motifRefus.
     *
     * @param id the id of the motifRefusDTO to save.
     * @param motifRefusDTO the motifRefusDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated motifRefusDTO,
     * or with status {@code 400 (Bad Request)} if the motifRefusDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the motifRefusDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/motif-refuses/{id}")
    public ResponseEntity<MotifRefusDTO> updateMotifRefus(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MotifRefusDTO motifRefusDTO
    ) throws URISyntaxException {
        log.debug("REST request to update MotifRefus : {}, {}", id, motifRefusDTO);
        if (motifRefusDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, motifRefusDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!motifRefusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MotifRefusDTO result = motifRefusService.save(motifRefusDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, motifRefusDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /motif-refuses/:id} : Partial updates given fields of an existing motifRefus, field will ignore if it is null
     *
     * @param id the id of the motifRefusDTO to save.
     * @param motifRefusDTO the motifRefusDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated motifRefusDTO,
     * or with status {@code 400 (Bad Request)} if the motifRefusDTO is not valid,
     * or with status {@code 404 (Not Found)} if the motifRefusDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the motifRefusDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/motif-refuses/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MotifRefusDTO> partialUpdateMotifRefus(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MotifRefusDTO motifRefusDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update MotifRefus partially : {}, {}", id, motifRefusDTO);
        if (motifRefusDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, motifRefusDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!motifRefusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MotifRefusDTO> result = motifRefusService.partialUpdate(motifRefusDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, motifRefusDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /motif-refuses} : get all the motifRefuses.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of motifRefuses in body.
     */
    @GetMapping("/motif-refuses")
    public ResponseEntity<List<MotifRefusDTO>> getAllMotifRefuses(
        MotifRefusCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get MotifRefuses by criteria: {}", criteria);
        Page<MotifRefusDTO> page = motifRefusQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /motif-refuses/count} : count all the motifRefuses.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/motif-refuses/count")
    public ResponseEntity<Long> countMotifRefuses(MotifRefusCriteria criteria) {
        log.debug("REST request to count MotifRefuses by criteria: {}", criteria);
        return ResponseEntity.ok().body(motifRefusQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /motif-refuses/:id} : get the "id" motifRefus.
     *
     * @param id the id of the motifRefusDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the motifRefusDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/motif-refuses/{id}")
    public ResponseEntity<MotifRefusDTO> getMotifRefus(@PathVariable Long id) {
        log.debug("REST request to get MotifRefus : {}", id);
        Optional<MotifRefusDTO> motifRefusDTO = motifRefusService.findOne(id);
        return ResponseUtil.wrapOrNotFound(motifRefusDTO);
    }

    /**
     * {@code DELETE  /motif-refuses/:id} : delete the "id" motifRefus.
     *
     * @param id the id of the motifRefusDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/motif-refuses/{id}")
    public ResponseEntity<Void> deleteMotifRefus(@PathVariable Long id) {
        log.debug("REST request to delete MotifRefus : {}", id);
        motifRefusService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
