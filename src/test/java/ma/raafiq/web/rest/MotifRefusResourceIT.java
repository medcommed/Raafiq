package ma.raafiq.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import ma.raafiq.IntegrationTest;
import ma.raafiq.domain.Beneficiaire;
import ma.raafiq.domain.Famille;
import ma.raafiq.domain.MotifRefus;
import ma.raafiq.domain.Professionnel;
import ma.raafiq.repository.MotifRefusRepository;
import ma.raafiq.service.criteria.MotifRefusCriteria;
import ma.raafiq.service.dto.MotifRefusDTO;
import ma.raafiq.service.mapper.MotifRefusMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link MotifRefusResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MotifRefusResourceIT {

    private static final String DEFAULT_LIBELE_AR = "AAAAAAAAAA";
    private static final String UPDATED_LIBELE_AR = "BBBBBBBBBB";

    private static final String DEFAULT_LIBELE_FR = "AAAAAAAAAA";
    private static final String UPDATED_LIBELE_FR = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/motif-refuses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MotifRefusRepository motifRefusRepository;

    @Autowired
    private MotifRefusMapper motifRefusMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMotifRefusMockMvc;

    private MotifRefus motifRefus;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MotifRefus createEntity(EntityManager em) {
        MotifRefus motifRefus = new MotifRefus().libeleAr(DEFAULT_LIBELE_AR).libeleFr(DEFAULT_LIBELE_FR);
        return motifRefus;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MotifRefus createUpdatedEntity(EntityManager em) {
        MotifRefus motifRefus = new MotifRefus().libeleAr(UPDATED_LIBELE_AR).libeleFr(UPDATED_LIBELE_FR);
        return motifRefus;
    }

    @BeforeEach
    public void initTest() {
        motifRefus = createEntity(em);
    }

    @Test
    @Transactional
    void createMotifRefus() throws Exception {
        int databaseSizeBeforeCreate = motifRefusRepository.findAll().size();
        // Create the MotifRefus
        MotifRefusDTO motifRefusDTO = motifRefusMapper.toDto(motifRefus);
        restMotifRefusMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(motifRefusDTO)))
            .andExpect(status().isCreated());

        // Validate the MotifRefus in the database
        List<MotifRefus> motifRefusList = motifRefusRepository.findAll();
        assertThat(motifRefusList).hasSize(databaseSizeBeforeCreate + 1);
        MotifRefus testMotifRefus = motifRefusList.get(motifRefusList.size() - 1);
        assertThat(testMotifRefus.getLibeleAr()).isEqualTo(DEFAULT_LIBELE_AR);
        assertThat(testMotifRefus.getLibeleFr()).isEqualTo(DEFAULT_LIBELE_FR);
    }

    @Test
    @Transactional
    void createMotifRefusWithExistingId() throws Exception {
        // Create the MotifRefus with an existing ID
        motifRefus.setId(1L);
        MotifRefusDTO motifRefusDTO = motifRefusMapper.toDto(motifRefus);

        int databaseSizeBeforeCreate = motifRefusRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMotifRefusMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(motifRefusDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MotifRefus in the database
        List<MotifRefus> motifRefusList = motifRefusRepository.findAll();
        assertThat(motifRefusList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMotifRefuses() throws Exception {
        // Initialize the database
        motifRefusRepository.saveAndFlush(motifRefus);

        // Get all the motifRefusList
        restMotifRefusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(motifRefus.getId().intValue())))
            .andExpect(jsonPath("$.[*].libeleAr").value(hasItem(DEFAULT_LIBELE_AR)))
            .andExpect(jsonPath("$.[*].libeleFr").value(hasItem(DEFAULT_LIBELE_FR)));
    }

    @Test
    @Transactional
    void getMotifRefus() throws Exception {
        // Initialize the database
        motifRefusRepository.saveAndFlush(motifRefus);

        // Get the motifRefus
        restMotifRefusMockMvc
            .perform(get(ENTITY_API_URL_ID, motifRefus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(motifRefus.getId().intValue()))
            .andExpect(jsonPath("$.libeleAr").value(DEFAULT_LIBELE_AR))
            .andExpect(jsonPath("$.libeleFr").value(DEFAULT_LIBELE_FR));
    }

    @Test
    @Transactional
    void getMotifRefusesByIdFiltering() throws Exception {
        // Initialize the database
        motifRefusRepository.saveAndFlush(motifRefus);

        Long id = motifRefus.getId();

        defaultMotifRefusShouldBeFound("id.equals=" + id);
        defaultMotifRefusShouldNotBeFound("id.notEquals=" + id);

        defaultMotifRefusShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMotifRefusShouldNotBeFound("id.greaterThan=" + id);

        defaultMotifRefusShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMotifRefusShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMotifRefusesByLibeleArIsEqualToSomething() throws Exception {
        // Initialize the database
        motifRefusRepository.saveAndFlush(motifRefus);

        // Get all the motifRefusList where libeleAr equals to DEFAULT_LIBELE_AR
        defaultMotifRefusShouldBeFound("libeleAr.equals=" + DEFAULT_LIBELE_AR);

        // Get all the motifRefusList where libeleAr equals to UPDATED_LIBELE_AR
        defaultMotifRefusShouldNotBeFound("libeleAr.equals=" + UPDATED_LIBELE_AR);
    }

    @Test
    @Transactional
    void getAllMotifRefusesByLibeleArIsNotEqualToSomething() throws Exception {
        // Initialize the database
        motifRefusRepository.saveAndFlush(motifRefus);

        // Get all the motifRefusList where libeleAr not equals to DEFAULT_LIBELE_AR
        defaultMotifRefusShouldNotBeFound("libeleAr.notEquals=" + DEFAULT_LIBELE_AR);

        // Get all the motifRefusList where libeleAr not equals to UPDATED_LIBELE_AR
        defaultMotifRefusShouldBeFound("libeleAr.notEquals=" + UPDATED_LIBELE_AR);
    }

    @Test
    @Transactional
    void getAllMotifRefusesByLibeleArIsInShouldWork() throws Exception {
        // Initialize the database
        motifRefusRepository.saveAndFlush(motifRefus);

        // Get all the motifRefusList where libeleAr in DEFAULT_LIBELE_AR or UPDATED_LIBELE_AR
        defaultMotifRefusShouldBeFound("libeleAr.in=" + DEFAULT_LIBELE_AR + "," + UPDATED_LIBELE_AR);

        // Get all the motifRefusList where libeleAr equals to UPDATED_LIBELE_AR
        defaultMotifRefusShouldNotBeFound("libeleAr.in=" + UPDATED_LIBELE_AR);
    }

    @Test
    @Transactional
    void getAllMotifRefusesByLibeleArIsNullOrNotNull() throws Exception {
        // Initialize the database
        motifRefusRepository.saveAndFlush(motifRefus);

        // Get all the motifRefusList where libeleAr is not null
        defaultMotifRefusShouldBeFound("libeleAr.specified=true");

        // Get all the motifRefusList where libeleAr is null
        defaultMotifRefusShouldNotBeFound("libeleAr.specified=false");
    }

    @Test
    @Transactional
    void getAllMotifRefusesByLibeleArContainsSomething() throws Exception {
        // Initialize the database
        motifRefusRepository.saveAndFlush(motifRefus);

        // Get all the motifRefusList where libeleAr contains DEFAULT_LIBELE_AR
        defaultMotifRefusShouldBeFound("libeleAr.contains=" + DEFAULT_LIBELE_AR);

        // Get all the motifRefusList where libeleAr contains UPDATED_LIBELE_AR
        defaultMotifRefusShouldNotBeFound("libeleAr.contains=" + UPDATED_LIBELE_AR);
    }

    @Test
    @Transactional
    void getAllMotifRefusesByLibeleArNotContainsSomething() throws Exception {
        // Initialize the database
        motifRefusRepository.saveAndFlush(motifRefus);

        // Get all the motifRefusList where libeleAr does not contain DEFAULT_LIBELE_AR
        defaultMotifRefusShouldNotBeFound("libeleAr.doesNotContain=" + DEFAULT_LIBELE_AR);

        // Get all the motifRefusList where libeleAr does not contain UPDATED_LIBELE_AR
        defaultMotifRefusShouldBeFound("libeleAr.doesNotContain=" + UPDATED_LIBELE_AR);
    }

    @Test
    @Transactional
    void getAllMotifRefusesByLibeleFrIsEqualToSomething() throws Exception {
        // Initialize the database
        motifRefusRepository.saveAndFlush(motifRefus);

        // Get all the motifRefusList where libeleFr equals to DEFAULT_LIBELE_FR
        defaultMotifRefusShouldBeFound("libeleFr.equals=" + DEFAULT_LIBELE_FR);

        // Get all the motifRefusList where libeleFr equals to UPDATED_LIBELE_FR
        defaultMotifRefusShouldNotBeFound("libeleFr.equals=" + UPDATED_LIBELE_FR);
    }

    @Test
    @Transactional
    void getAllMotifRefusesByLibeleFrIsNotEqualToSomething() throws Exception {
        // Initialize the database
        motifRefusRepository.saveAndFlush(motifRefus);

        // Get all the motifRefusList where libeleFr not equals to DEFAULT_LIBELE_FR
        defaultMotifRefusShouldNotBeFound("libeleFr.notEquals=" + DEFAULT_LIBELE_FR);

        // Get all the motifRefusList where libeleFr not equals to UPDATED_LIBELE_FR
        defaultMotifRefusShouldBeFound("libeleFr.notEquals=" + UPDATED_LIBELE_FR);
    }

    @Test
    @Transactional
    void getAllMotifRefusesByLibeleFrIsInShouldWork() throws Exception {
        // Initialize the database
        motifRefusRepository.saveAndFlush(motifRefus);

        // Get all the motifRefusList where libeleFr in DEFAULT_LIBELE_FR or UPDATED_LIBELE_FR
        defaultMotifRefusShouldBeFound("libeleFr.in=" + DEFAULT_LIBELE_FR + "," + UPDATED_LIBELE_FR);

        // Get all the motifRefusList where libeleFr equals to UPDATED_LIBELE_FR
        defaultMotifRefusShouldNotBeFound("libeleFr.in=" + UPDATED_LIBELE_FR);
    }

    @Test
    @Transactional
    void getAllMotifRefusesByLibeleFrIsNullOrNotNull() throws Exception {
        // Initialize the database
        motifRefusRepository.saveAndFlush(motifRefus);

        // Get all the motifRefusList where libeleFr is not null
        defaultMotifRefusShouldBeFound("libeleFr.specified=true");

        // Get all the motifRefusList where libeleFr is null
        defaultMotifRefusShouldNotBeFound("libeleFr.specified=false");
    }

    @Test
    @Transactional
    void getAllMotifRefusesByLibeleFrContainsSomething() throws Exception {
        // Initialize the database
        motifRefusRepository.saveAndFlush(motifRefus);

        // Get all the motifRefusList where libeleFr contains DEFAULT_LIBELE_FR
        defaultMotifRefusShouldBeFound("libeleFr.contains=" + DEFAULT_LIBELE_FR);

        // Get all the motifRefusList where libeleFr contains UPDATED_LIBELE_FR
        defaultMotifRefusShouldNotBeFound("libeleFr.contains=" + UPDATED_LIBELE_FR);
    }

    @Test
    @Transactional
    void getAllMotifRefusesByLibeleFrNotContainsSomething() throws Exception {
        // Initialize the database
        motifRefusRepository.saveAndFlush(motifRefus);

        // Get all the motifRefusList where libeleFr does not contain DEFAULT_LIBELE_FR
        defaultMotifRefusShouldNotBeFound("libeleFr.doesNotContain=" + DEFAULT_LIBELE_FR);

        // Get all the motifRefusList where libeleFr does not contain UPDATED_LIBELE_FR
        defaultMotifRefusShouldBeFound("libeleFr.doesNotContain=" + UPDATED_LIBELE_FR);
    }

    @Test
    @Transactional
    void getAllMotifRefusesByBeneficiaireIsEqualToSomething() throws Exception {
        // Initialize the database
        motifRefusRepository.saveAndFlush(motifRefus);
        Beneficiaire beneficiaire;
        if (TestUtil.findAll(em, Beneficiaire.class).isEmpty()) {
            beneficiaire = BeneficiaireResourceIT.createEntity(em);
            em.persist(beneficiaire);
            em.flush();
        } else {
            beneficiaire = TestUtil.findAll(em, Beneficiaire.class).get(0);
        }
        em.persist(beneficiaire);
        em.flush();
        motifRefus.addBeneficiaire(beneficiaire);
        motifRefusRepository.saveAndFlush(motifRefus);
        Long beneficiaireId = beneficiaire.getId();

        // Get all the motifRefusList where beneficiaire equals to beneficiaireId
        defaultMotifRefusShouldBeFound("beneficiaireId.equals=" + beneficiaireId);

        // Get all the motifRefusList where beneficiaire equals to (beneficiaireId + 1)
        defaultMotifRefusShouldNotBeFound("beneficiaireId.equals=" + (beneficiaireId + 1));
    }

    @Test
    @Transactional
    void getAllMotifRefusesByFamilleIsEqualToSomething() throws Exception {
        // Initialize the database
        motifRefusRepository.saveAndFlush(motifRefus);
        Famille famille;
        if (TestUtil.findAll(em, Famille.class).isEmpty()) {
            famille = FamilleResourceIT.createEntity(em);
            em.persist(famille);
            em.flush();
        } else {
            famille = TestUtil.findAll(em, Famille.class).get(0);
        }
        em.persist(famille);
        em.flush();
        motifRefus.addFamille(famille);
        motifRefusRepository.saveAndFlush(motifRefus);
        Long familleId = famille.getId();

        // Get all the motifRefusList where famille equals to familleId
        defaultMotifRefusShouldBeFound("familleId.equals=" + familleId);

        // Get all the motifRefusList where famille equals to (familleId + 1)
        defaultMotifRefusShouldNotBeFound("familleId.equals=" + (familleId + 1));
    }

    @Test
    @Transactional
    void getAllMotifRefusesByProfessionnelIsEqualToSomething() throws Exception {
        // Initialize the database
        motifRefusRepository.saveAndFlush(motifRefus);
        Professionnel professionnel;
        if (TestUtil.findAll(em, Professionnel.class).isEmpty()) {
            professionnel = ProfessionnelResourceIT.createEntity(em);
            em.persist(professionnel);
            em.flush();
        } else {
            professionnel = TestUtil.findAll(em, Professionnel.class).get(0);
        }
        em.persist(professionnel);
        em.flush();
        motifRefus.addProfessionnel(professionnel);
        motifRefusRepository.saveAndFlush(motifRefus);
        Long professionnelId = professionnel.getId();

        // Get all the motifRefusList where professionnel equals to professionnelId
        defaultMotifRefusShouldBeFound("professionnelId.equals=" + professionnelId);

        // Get all the motifRefusList where professionnel equals to (professionnelId + 1)
        defaultMotifRefusShouldNotBeFound("professionnelId.equals=" + (professionnelId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMotifRefusShouldBeFound(String filter) throws Exception {
        restMotifRefusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(motifRefus.getId().intValue())))
            .andExpect(jsonPath("$.[*].libeleAr").value(hasItem(DEFAULT_LIBELE_AR)))
            .andExpect(jsonPath("$.[*].libeleFr").value(hasItem(DEFAULT_LIBELE_FR)));

        // Check, that the count call also returns 1
        restMotifRefusMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMotifRefusShouldNotBeFound(String filter) throws Exception {
        restMotifRefusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMotifRefusMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMotifRefus() throws Exception {
        // Get the motifRefus
        restMotifRefusMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMotifRefus() throws Exception {
        // Initialize the database
        motifRefusRepository.saveAndFlush(motifRefus);

        int databaseSizeBeforeUpdate = motifRefusRepository.findAll().size();

        // Update the motifRefus
        MotifRefus updatedMotifRefus = motifRefusRepository.findById(motifRefus.getId()).get();
        // Disconnect from session so that the updates on updatedMotifRefus are not directly saved in db
        em.detach(updatedMotifRefus);
        updatedMotifRefus.libeleAr(UPDATED_LIBELE_AR).libeleFr(UPDATED_LIBELE_FR);
        MotifRefusDTO motifRefusDTO = motifRefusMapper.toDto(updatedMotifRefus);

        restMotifRefusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, motifRefusDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(motifRefusDTO))
            )
            .andExpect(status().isOk());

        // Validate the MotifRefus in the database
        List<MotifRefus> motifRefusList = motifRefusRepository.findAll();
        assertThat(motifRefusList).hasSize(databaseSizeBeforeUpdate);
        MotifRefus testMotifRefus = motifRefusList.get(motifRefusList.size() - 1);
        assertThat(testMotifRefus.getLibeleAr()).isEqualTo(UPDATED_LIBELE_AR);
        assertThat(testMotifRefus.getLibeleFr()).isEqualTo(UPDATED_LIBELE_FR);
    }

    @Test
    @Transactional
    void putNonExistingMotifRefus() throws Exception {
        int databaseSizeBeforeUpdate = motifRefusRepository.findAll().size();
        motifRefus.setId(count.incrementAndGet());

        // Create the MotifRefus
        MotifRefusDTO motifRefusDTO = motifRefusMapper.toDto(motifRefus);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMotifRefusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, motifRefusDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(motifRefusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MotifRefus in the database
        List<MotifRefus> motifRefusList = motifRefusRepository.findAll();
        assertThat(motifRefusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMotifRefus() throws Exception {
        int databaseSizeBeforeUpdate = motifRefusRepository.findAll().size();
        motifRefus.setId(count.incrementAndGet());

        // Create the MotifRefus
        MotifRefusDTO motifRefusDTO = motifRefusMapper.toDto(motifRefus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMotifRefusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(motifRefusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MotifRefus in the database
        List<MotifRefus> motifRefusList = motifRefusRepository.findAll();
        assertThat(motifRefusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMotifRefus() throws Exception {
        int databaseSizeBeforeUpdate = motifRefusRepository.findAll().size();
        motifRefus.setId(count.incrementAndGet());

        // Create the MotifRefus
        MotifRefusDTO motifRefusDTO = motifRefusMapper.toDto(motifRefus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMotifRefusMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(motifRefusDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MotifRefus in the database
        List<MotifRefus> motifRefusList = motifRefusRepository.findAll();
        assertThat(motifRefusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMotifRefusWithPatch() throws Exception {
        // Initialize the database
        motifRefusRepository.saveAndFlush(motifRefus);

        int databaseSizeBeforeUpdate = motifRefusRepository.findAll().size();

        // Update the motifRefus using partial update
        MotifRefus partialUpdatedMotifRefus = new MotifRefus();
        partialUpdatedMotifRefus.setId(motifRefus.getId());

        partialUpdatedMotifRefus.libeleAr(UPDATED_LIBELE_AR).libeleFr(UPDATED_LIBELE_FR);

        restMotifRefusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMotifRefus.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMotifRefus))
            )
            .andExpect(status().isOk());

        // Validate the MotifRefus in the database
        List<MotifRefus> motifRefusList = motifRefusRepository.findAll();
        assertThat(motifRefusList).hasSize(databaseSizeBeforeUpdate);
        MotifRefus testMotifRefus = motifRefusList.get(motifRefusList.size() - 1);
        assertThat(testMotifRefus.getLibeleAr()).isEqualTo(UPDATED_LIBELE_AR);
        assertThat(testMotifRefus.getLibeleFr()).isEqualTo(UPDATED_LIBELE_FR);
    }

    @Test
    @Transactional
    void fullUpdateMotifRefusWithPatch() throws Exception {
        // Initialize the database
        motifRefusRepository.saveAndFlush(motifRefus);

        int databaseSizeBeforeUpdate = motifRefusRepository.findAll().size();

        // Update the motifRefus using partial update
        MotifRefus partialUpdatedMotifRefus = new MotifRefus();
        partialUpdatedMotifRefus.setId(motifRefus.getId());

        partialUpdatedMotifRefus.libeleAr(UPDATED_LIBELE_AR).libeleFr(UPDATED_LIBELE_FR);

        restMotifRefusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMotifRefus.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMotifRefus))
            )
            .andExpect(status().isOk());

        // Validate the MotifRefus in the database
        List<MotifRefus> motifRefusList = motifRefusRepository.findAll();
        assertThat(motifRefusList).hasSize(databaseSizeBeforeUpdate);
        MotifRefus testMotifRefus = motifRefusList.get(motifRefusList.size() - 1);
        assertThat(testMotifRefus.getLibeleAr()).isEqualTo(UPDATED_LIBELE_AR);
        assertThat(testMotifRefus.getLibeleFr()).isEqualTo(UPDATED_LIBELE_FR);
    }

    @Test
    @Transactional
    void patchNonExistingMotifRefus() throws Exception {
        int databaseSizeBeforeUpdate = motifRefusRepository.findAll().size();
        motifRefus.setId(count.incrementAndGet());

        // Create the MotifRefus
        MotifRefusDTO motifRefusDTO = motifRefusMapper.toDto(motifRefus);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMotifRefusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, motifRefusDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(motifRefusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MotifRefus in the database
        List<MotifRefus> motifRefusList = motifRefusRepository.findAll();
        assertThat(motifRefusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMotifRefus() throws Exception {
        int databaseSizeBeforeUpdate = motifRefusRepository.findAll().size();
        motifRefus.setId(count.incrementAndGet());

        // Create the MotifRefus
        MotifRefusDTO motifRefusDTO = motifRefusMapper.toDto(motifRefus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMotifRefusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(motifRefusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MotifRefus in the database
        List<MotifRefus> motifRefusList = motifRefusRepository.findAll();
        assertThat(motifRefusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMotifRefus() throws Exception {
        int databaseSizeBeforeUpdate = motifRefusRepository.findAll().size();
        motifRefus.setId(count.incrementAndGet());

        // Create the MotifRefus
        MotifRefusDTO motifRefusDTO = motifRefusMapper.toDto(motifRefus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMotifRefusMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(motifRefusDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MotifRefus in the database
        List<MotifRefus> motifRefusList = motifRefusRepository.findAll();
        assertThat(motifRefusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMotifRefus() throws Exception {
        // Initialize the database
        motifRefusRepository.saveAndFlush(motifRefus);

        int databaseSizeBeforeDelete = motifRefusRepository.findAll().size();

        // Delete the motifRefus
        restMotifRefusMockMvc
            .perform(delete(ENTITY_API_URL_ID, motifRefus.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MotifRefus> motifRefusList = motifRefusRepository.findAll();
        assertThat(motifRefusList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
