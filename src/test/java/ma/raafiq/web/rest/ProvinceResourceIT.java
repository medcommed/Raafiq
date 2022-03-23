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
import ma.raafiq.domain.AppUser;
import ma.raafiq.domain.Beneficiaire;
import ma.raafiq.domain.Famille;
import ma.raafiq.domain.Professionnel;
import ma.raafiq.domain.Province;
import ma.raafiq.domain.Region;
import ma.raafiq.repository.ProvinceRepository;
import ma.raafiq.service.criteria.ProvinceCriteria;
import ma.raafiq.service.dto.ProvinceDTO;
import ma.raafiq.service.mapper.ProvinceMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ProvinceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProvinceResourceIT {

    private static final String DEFAULT_LIBELE_AR = "AAAAAAAAAA";
    private static final String UPDATED_LIBELE_AR = "BBBBBBBBBB";

    private static final String DEFAULT_LIBELE_FR = "AAAAAAAAAA";
    private static final String UPDATED_LIBELE_FR = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/provinces";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProvinceRepository provinceRepository;

    @Autowired
    private ProvinceMapper provinceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProvinceMockMvc;

    private Province province;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Province createEntity(EntityManager em) {
        Province province = new Province().libeleAr(DEFAULT_LIBELE_AR).libeleFr(DEFAULT_LIBELE_FR);
        return province;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Province createUpdatedEntity(EntityManager em) {
        Province province = new Province().libeleAr(UPDATED_LIBELE_AR).libeleFr(UPDATED_LIBELE_FR);
        return province;
    }

    @BeforeEach
    public void initTest() {
        province = createEntity(em);
    }

    @Test
    @Transactional
    void createProvince() throws Exception {
        int databaseSizeBeforeCreate = provinceRepository.findAll().size();
        // Create the Province
        ProvinceDTO provinceDTO = provinceMapper.toDto(province);
        restProvinceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(provinceDTO)))
            .andExpect(status().isCreated());

        // Validate the Province in the database
        List<Province> provinceList = provinceRepository.findAll();
        assertThat(provinceList).hasSize(databaseSizeBeforeCreate + 1);
        Province testProvince = provinceList.get(provinceList.size() - 1);
        assertThat(testProvince.getLibeleAr()).isEqualTo(DEFAULT_LIBELE_AR);
        assertThat(testProvince.getLibeleFr()).isEqualTo(DEFAULT_LIBELE_FR);
    }

    @Test
    @Transactional
    void createProvinceWithExistingId() throws Exception {
        // Create the Province with an existing ID
        province.setId(1L);
        ProvinceDTO provinceDTO = provinceMapper.toDto(province);

        int databaseSizeBeforeCreate = provinceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProvinceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(provinceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Province in the database
        List<Province> provinceList = provinceRepository.findAll();
        assertThat(provinceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProvinces() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList
        restProvinceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(province.getId().intValue())))
            .andExpect(jsonPath("$.[*].libeleAr").value(hasItem(DEFAULT_LIBELE_AR)))
            .andExpect(jsonPath("$.[*].libeleFr").value(hasItem(DEFAULT_LIBELE_FR)));
    }

    @Test
    @Transactional
    void getProvince() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get the province
        restProvinceMockMvc
            .perform(get(ENTITY_API_URL_ID, province.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(province.getId().intValue()))
            .andExpect(jsonPath("$.libeleAr").value(DEFAULT_LIBELE_AR))
            .andExpect(jsonPath("$.libeleFr").value(DEFAULT_LIBELE_FR));
    }

    @Test
    @Transactional
    void getProvincesByIdFiltering() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        Long id = province.getId();

        defaultProvinceShouldBeFound("id.equals=" + id);
        defaultProvinceShouldNotBeFound("id.notEquals=" + id);

        defaultProvinceShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProvinceShouldNotBeFound("id.greaterThan=" + id);

        defaultProvinceShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProvinceShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProvincesByLibeleArIsEqualToSomething() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where libeleAr equals to DEFAULT_LIBELE_AR
        defaultProvinceShouldBeFound("libeleAr.equals=" + DEFAULT_LIBELE_AR);

        // Get all the provinceList where libeleAr equals to UPDATED_LIBELE_AR
        defaultProvinceShouldNotBeFound("libeleAr.equals=" + UPDATED_LIBELE_AR);
    }

    @Test
    @Transactional
    void getAllProvincesByLibeleArIsNotEqualToSomething() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where libeleAr not equals to DEFAULT_LIBELE_AR
        defaultProvinceShouldNotBeFound("libeleAr.notEquals=" + DEFAULT_LIBELE_AR);

        // Get all the provinceList where libeleAr not equals to UPDATED_LIBELE_AR
        defaultProvinceShouldBeFound("libeleAr.notEquals=" + UPDATED_LIBELE_AR);
    }

    @Test
    @Transactional
    void getAllProvincesByLibeleArIsInShouldWork() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where libeleAr in DEFAULT_LIBELE_AR or UPDATED_LIBELE_AR
        defaultProvinceShouldBeFound("libeleAr.in=" + DEFAULT_LIBELE_AR + "," + UPDATED_LIBELE_AR);

        // Get all the provinceList where libeleAr equals to UPDATED_LIBELE_AR
        defaultProvinceShouldNotBeFound("libeleAr.in=" + UPDATED_LIBELE_AR);
    }

    @Test
    @Transactional
    void getAllProvincesByLibeleArIsNullOrNotNull() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where libeleAr is not null
        defaultProvinceShouldBeFound("libeleAr.specified=true");

        // Get all the provinceList where libeleAr is null
        defaultProvinceShouldNotBeFound("libeleAr.specified=false");
    }

    @Test
    @Transactional
    void getAllProvincesByLibeleArContainsSomething() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where libeleAr contains DEFAULT_LIBELE_AR
        defaultProvinceShouldBeFound("libeleAr.contains=" + DEFAULT_LIBELE_AR);

        // Get all the provinceList where libeleAr contains UPDATED_LIBELE_AR
        defaultProvinceShouldNotBeFound("libeleAr.contains=" + UPDATED_LIBELE_AR);
    }

    @Test
    @Transactional
    void getAllProvincesByLibeleArNotContainsSomething() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where libeleAr does not contain DEFAULT_LIBELE_AR
        defaultProvinceShouldNotBeFound("libeleAr.doesNotContain=" + DEFAULT_LIBELE_AR);

        // Get all the provinceList where libeleAr does not contain UPDATED_LIBELE_AR
        defaultProvinceShouldBeFound("libeleAr.doesNotContain=" + UPDATED_LIBELE_AR);
    }

    @Test
    @Transactional
    void getAllProvincesByLibeleFrIsEqualToSomething() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where libeleFr equals to DEFAULT_LIBELE_FR
        defaultProvinceShouldBeFound("libeleFr.equals=" + DEFAULT_LIBELE_FR);

        // Get all the provinceList where libeleFr equals to UPDATED_LIBELE_FR
        defaultProvinceShouldNotBeFound("libeleFr.equals=" + UPDATED_LIBELE_FR);
    }

    @Test
    @Transactional
    void getAllProvincesByLibeleFrIsNotEqualToSomething() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where libeleFr not equals to DEFAULT_LIBELE_FR
        defaultProvinceShouldNotBeFound("libeleFr.notEquals=" + DEFAULT_LIBELE_FR);

        // Get all the provinceList where libeleFr not equals to UPDATED_LIBELE_FR
        defaultProvinceShouldBeFound("libeleFr.notEquals=" + UPDATED_LIBELE_FR);
    }

    @Test
    @Transactional
    void getAllProvincesByLibeleFrIsInShouldWork() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where libeleFr in DEFAULT_LIBELE_FR or UPDATED_LIBELE_FR
        defaultProvinceShouldBeFound("libeleFr.in=" + DEFAULT_LIBELE_FR + "," + UPDATED_LIBELE_FR);

        // Get all the provinceList where libeleFr equals to UPDATED_LIBELE_FR
        defaultProvinceShouldNotBeFound("libeleFr.in=" + UPDATED_LIBELE_FR);
    }

    @Test
    @Transactional
    void getAllProvincesByLibeleFrIsNullOrNotNull() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where libeleFr is not null
        defaultProvinceShouldBeFound("libeleFr.specified=true");

        // Get all the provinceList where libeleFr is null
        defaultProvinceShouldNotBeFound("libeleFr.specified=false");
    }

    @Test
    @Transactional
    void getAllProvincesByLibeleFrContainsSomething() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where libeleFr contains DEFAULT_LIBELE_FR
        defaultProvinceShouldBeFound("libeleFr.contains=" + DEFAULT_LIBELE_FR);

        // Get all the provinceList where libeleFr contains UPDATED_LIBELE_FR
        defaultProvinceShouldNotBeFound("libeleFr.contains=" + UPDATED_LIBELE_FR);
    }

    @Test
    @Transactional
    void getAllProvincesByLibeleFrNotContainsSomething() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where libeleFr does not contain DEFAULT_LIBELE_FR
        defaultProvinceShouldNotBeFound("libeleFr.doesNotContain=" + DEFAULT_LIBELE_FR);

        // Get all the provinceList where libeleFr does not contain UPDATED_LIBELE_FR
        defaultProvinceShouldBeFound("libeleFr.doesNotContain=" + UPDATED_LIBELE_FR);
    }

    @Test
    @Transactional
    void getAllProvincesByRegionIsEqualToSomething() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);
        Region region;
        if (TestUtil.findAll(em, Region.class).isEmpty()) {
            region = RegionResourceIT.createEntity(em);
            em.persist(region);
            em.flush();
        } else {
            region = TestUtil.findAll(em, Region.class).get(0);
        }
        em.persist(region);
        em.flush();
        province.setRegion(region);
        provinceRepository.saveAndFlush(province);
        Long regionId = region.getId();

        // Get all the provinceList where region equals to regionId
        defaultProvinceShouldBeFound("regionId.equals=" + regionId);

        // Get all the provinceList where region equals to (regionId + 1)
        defaultProvinceShouldNotBeFound("regionId.equals=" + (regionId + 1));
    }

    @Test
    @Transactional
    void getAllProvincesByAppUserIsEqualToSomething() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);
        AppUser appUser;
        if (TestUtil.findAll(em, AppUser.class).isEmpty()) {
            appUser = AppUserResourceIT.createEntity(em);
            em.persist(appUser);
            em.flush();
        } else {
            appUser = TestUtil.findAll(em, AppUser.class).get(0);
        }
        em.persist(appUser);
        em.flush();
        province.addAppUser(appUser);
        provinceRepository.saveAndFlush(province);
        Long appUserId = appUser.getId();

        // Get all the provinceList where appUser equals to appUserId
        defaultProvinceShouldBeFound("appUserId.equals=" + appUserId);

        // Get all the provinceList where appUser equals to (appUserId + 1)
        defaultProvinceShouldNotBeFound("appUserId.equals=" + (appUserId + 1));
    }

    @Test
    @Transactional
    void getAllProvincesByBeneficiaireIsEqualToSomething() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);
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
        province.addBeneficiaire(beneficiaire);
        provinceRepository.saveAndFlush(province);
        Long beneficiaireId = beneficiaire.getId();

        // Get all the provinceList where beneficiaire equals to beneficiaireId
        defaultProvinceShouldBeFound("beneficiaireId.equals=" + beneficiaireId);

        // Get all the provinceList where beneficiaire equals to (beneficiaireId + 1)
        defaultProvinceShouldNotBeFound("beneficiaireId.equals=" + (beneficiaireId + 1));
    }

    @Test
    @Transactional
    void getAllProvincesByFamilleIsEqualToSomething() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);
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
        province.addFamille(famille);
        provinceRepository.saveAndFlush(province);
        Long familleId = famille.getId();

        // Get all the provinceList where famille equals to familleId
        defaultProvinceShouldBeFound("familleId.equals=" + familleId);

        // Get all the provinceList where famille equals to (familleId + 1)
        defaultProvinceShouldNotBeFound("familleId.equals=" + (familleId + 1));
    }

    @Test
    @Transactional
    void getAllProvincesByProfessionnelIsEqualToSomething() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);
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
        province.addProfessionnel(professionnel);
        provinceRepository.saveAndFlush(province);
        Long professionnelId = professionnel.getId();

        // Get all the provinceList where professionnel equals to professionnelId
        defaultProvinceShouldBeFound("professionnelId.equals=" + professionnelId);

        // Get all the provinceList where professionnel equals to (professionnelId + 1)
        defaultProvinceShouldNotBeFound("professionnelId.equals=" + (professionnelId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProvinceShouldBeFound(String filter) throws Exception {
        restProvinceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(province.getId().intValue())))
            .andExpect(jsonPath("$.[*].libeleAr").value(hasItem(DEFAULT_LIBELE_AR)))
            .andExpect(jsonPath("$.[*].libeleFr").value(hasItem(DEFAULT_LIBELE_FR)));

        // Check, that the count call also returns 1
        restProvinceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProvinceShouldNotBeFound(String filter) throws Exception {
        restProvinceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProvinceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProvince() throws Exception {
        // Get the province
        restProvinceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProvince() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        int databaseSizeBeforeUpdate = provinceRepository.findAll().size();

        // Update the province
        Province updatedProvince = provinceRepository.findById(province.getId()).get();
        // Disconnect from session so that the updates on updatedProvince are not directly saved in db
        em.detach(updatedProvince);
        updatedProvince.libeleAr(UPDATED_LIBELE_AR).libeleFr(UPDATED_LIBELE_FR);
        ProvinceDTO provinceDTO = provinceMapper.toDto(updatedProvince);

        restProvinceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, provinceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(provinceDTO))
            )
            .andExpect(status().isOk());

        // Validate the Province in the database
        List<Province> provinceList = provinceRepository.findAll();
        assertThat(provinceList).hasSize(databaseSizeBeforeUpdate);
        Province testProvince = provinceList.get(provinceList.size() - 1);
        assertThat(testProvince.getLibeleAr()).isEqualTo(UPDATED_LIBELE_AR);
        assertThat(testProvince.getLibeleFr()).isEqualTo(UPDATED_LIBELE_FR);
    }

    @Test
    @Transactional
    void putNonExistingProvince() throws Exception {
        int databaseSizeBeforeUpdate = provinceRepository.findAll().size();
        province.setId(count.incrementAndGet());

        // Create the Province
        ProvinceDTO provinceDTO = provinceMapper.toDto(province);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProvinceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, provinceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(provinceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Province in the database
        List<Province> provinceList = provinceRepository.findAll();
        assertThat(provinceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProvince() throws Exception {
        int databaseSizeBeforeUpdate = provinceRepository.findAll().size();
        province.setId(count.incrementAndGet());

        // Create the Province
        ProvinceDTO provinceDTO = provinceMapper.toDto(province);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProvinceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(provinceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Province in the database
        List<Province> provinceList = provinceRepository.findAll();
        assertThat(provinceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProvince() throws Exception {
        int databaseSizeBeforeUpdate = provinceRepository.findAll().size();
        province.setId(count.incrementAndGet());

        // Create the Province
        ProvinceDTO provinceDTO = provinceMapper.toDto(province);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProvinceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(provinceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Province in the database
        List<Province> provinceList = provinceRepository.findAll();
        assertThat(provinceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProvinceWithPatch() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        int databaseSizeBeforeUpdate = provinceRepository.findAll().size();

        // Update the province using partial update
        Province partialUpdatedProvince = new Province();
        partialUpdatedProvince.setId(province.getId());

        partialUpdatedProvince.libeleAr(UPDATED_LIBELE_AR);

        restProvinceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProvince.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProvince))
            )
            .andExpect(status().isOk());

        // Validate the Province in the database
        List<Province> provinceList = provinceRepository.findAll();
        assertThat(provinceList).hasSize(databaseSizeBeforeUpdate);
        Province testProvince = provinceList.get(provinceList.size() - 1);
        assertThat(testProvince.getLibeleAr()).isEqualTo(UPDATED_LIBELE_AR);
        assertThat(testProvince.getLibeleFr()).isEqualTo(DEFAULT_LIBELE_FR);
    }

    @Test
    @Transactional
    void fullUpdateProvinceWithPatch() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        int databaseSizeBeforeUpdate = provinceRepository.findAll().size();

        // Update the province using partial update
        Province partialUpdatedProvince = new Province();
        partialUpdatedProvince.setId(province.getId());

        partialUpdatedProvince.libeleAr(UPDATED_LIBELE_AR).libeleFr(UPDATED_LIBELE_FR);

        restProvinceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProvince.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProvince))
            )
            .andExpect(status().isOk());

        // Validate the Province in the database
        List<Province> provinceList = provinceRepository.findAll();
        assertThat(provinceList).hasSize(databaseSizeBeforeUpdate);
        Province testProvince = provinceList.get(provinceList.size() - 1);
        assertThat(testProvince.getLibeleAr()).isEqualTo(UPDATED_LIBELE_AR);
        assertThat(testProvince.getLibeleFr()).isEqualTo(UPDATED_LIBELE_FR);
    }

    @Test
    @Transactional
    void patchNonExistingProvince() throws Exception {
        int databaseSizeBeforeUpdate = provinceRepository.findAll().size();
        province.setId(count.incrementAndGet());

        // Create the Province
        ProvinceDTO provinceDTO = provinceMapper.toDto(province);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProvinceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, provinceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(provinceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Province in the database
        List<Province> provinceList = provinceRepository.findAll();
        assertThat(provinceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProvince() throws Exception {
        int databaseSizeBeforeUpdate = provinceRepository.findAll().size();
        province.setId(count.incrementAndGet());

        // Create the Province
        ProvinceDTO provinceDTO = provinceMapper.toDto(province);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProvinceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(provinceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Province in the database
        List<Province> provinceList = provinceRepository.findAll();
        assertThat(provinceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProvince() throws Exception {
        int databaseSizeBeforeUpdate = provinceRepository.findAll().size();
        province.setId(count.incrementAndGet());

        // Create the Province
        ProvinceDTO provinceDTO = provinceMapper.toDto(province);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProvinceMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(provinceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Province in the database
        List<Province> provinceList = provinceRepository.findAll();
        assertThat(provinceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProvince() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        int databaseSizeBeforeDelete = provinceRepository.findAll().size();

        // Delete the province
        restProvinceMockMvc
            .perform(delete(ENTITY_API_URL_ID, province.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Province> provinceList = provinceRepository.findAll();
        assertThat(provinceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
