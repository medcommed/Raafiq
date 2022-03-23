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
import ma.raafiq.domain.Province;
import ma.raafiq.domain.Region;
import ma.raafiq.repository.RegionRepository;
import ma.raafiq.service.criteria.RegionCriteria;
import ma.raafiq.service.dto.RegionDTO;
import ma.raafiq.service.mapper.RegionMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link RegionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RegionResourceIT {

    private static final String DEFAULT_LIBELE_AR = "AAAAAAAAAA";
    private static final String UPDATED_LIBELE_AR = "BBBBBBBBBB";

    private static final String DEFAULT_LIBELE_FR = "AAAAAAAAAA";
    private static final String UPDATED_LIBELE_FR = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/regions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private RegionMapper regionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRegionMockMvc;

    private Region region;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Region createEntity(EntityManager em) {
        Region region = new Region().libeleAr(DEFAULT_LIBELE_AR).libeleFr(DEFAULT_LIBELE_FR);
        return region;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Region createUpdatedEntity(EntityManager em) {
        Region region = new Region().libeleAr(UPDATED_LIBELE_AR).libeleFr(UPDATED_LIBELE_FR);
        return region;
    }

    @BeforeEach
    public void initTest() {
        region = createEntity(em);
    }

    @Test
    @Transactional
    void createRegion() throws Exception {
        int databaseSizeBeforeCreate = regionRepository.findAll().size();
        // Create the Region
        RegionDTO regionDTO = regionMapper.toDto(region);
        restRegionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(regionDTO)))
            .andExpect(status().isCreated());

        // Validate the Region in the database
        List<Region> regionList = regionRepository.findAll();
        assertThat(regionList).hasSize(databaseSizeBeforeCreate + 1);
        Region testRegion = regionList.get(regionList.size() - 1);
        assertThat(testRegion.getLibeleAr()).isEqualTo(DEFAULT_LIBELE_AR);
        assertThat(testRegion.getLibeleFr()).isEqualTo(DEFAULT_LIBELE_FR);
    }

    @Test
    @Transactional
    void createRegionWithExistingId() throws Exception {
        // Create the Region with an existing ID
        region.setId(1L);
        RegionDTO regionDTO = regionMapper.toDto(region);

        int databaseSizeBeforeCreate = regionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRegionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(regionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Region in the database
        List<Region> regionList = regionRepository.findAll();
        assertThat(regionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRegions() throws Exception {
        // Initialize the database
        regionRepository.saveAndFlush(region);

        // Get all the regionList
        restRegionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(region.getId().intValue())))
            .andExpect(jsonPath("$.[*].libeleAr").value(hasItem(DEFAULT_LIBELE_AR)))
            .andExpect(jsonPath("$.[*].libeleFr").value(hasItem(DEFAULT_LIBELE_FR)));
    }

    @Test
    @Transactional
    void getRegion() throws Exception {
        // Initialize the database
        regionRepository.saveAndFlush(region);

        // Get the region
        restRegionMockMvc
            .perform(get(ENTITY_API_URL_ID, region.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(region.getId().intValue()))
            .andExpect(jsonPath("$.libeleAr").value(DEFAULT_LIBELE_AR))
            .andExpect(jsonPath("$.libeleFr").value(DEFAULT_LIBELE_FR));
    }

    @Test
    @Transactional
    void getRegionsByIdFiltering() throws Exception {
        // Initialize the database
        regionRepository.saveAndFlush(region);

        Long id = region.getId();

        defaultRegionShouldBeFound("id.equals=" + id);
        defaultRegionShouldNotBeFound("id.notEquals=" + id);

        defaultRegionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRegionShouldNotBeFound("id.greaterThan=" + id);

        defaultRegionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRegionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRegionsByLibeleArIsEqualToSomething() throws Exception {
        // Initialize the database
        regionRepository.saveAndFlush(region);

        // Get all the regionList where libeleAr equals to DEFAULT_LIBELE_AR
        defaultRegionShouldBeFound("libeleAr.equals=" + DEFAULT_LIBELE_AR);

        // Get all the regionList where libeleAr equals to UPDATED_LIBELE_AR
        defaultRegionShouldNotBeFound("libeleAr.equals=" + UPDATED_LIBELE_AR);
    }

    @Test
    @Transactional
    void getAllRegionsByLibeleArIsNotEqualToSomething() throws Exception {
        // Initialize the database
        regionRepository.saveAndFlush(region);

        // Get all the regionList where libeleAr not equals to DEFAULT_LIBELE_AR
        defaultRegionShouldNotBeFound("libeleAr.notEquals=" + DEFAULT_LIBELE_AR);

        // Get all the regionList where libeleAr not equals to UPDATED_LIBELE_AR
        defaultRegionShouldBeFound("libeleAr.notEquals=" + UPDATED_LIBELE_AR);
    }

    @Test
    @Transactional
    void getAllRegionsByLibeleArIsInShouldWork() throws Exception {
        // Initialize the database
        regionRepository.saveAndFlush(region);

        // Get all the regionList where libeleAr in DEFAULT_LIBELE_AR or UPDATED_LIBELE_AR
        defaultRegionShouldBeFound("libeleAr.in=" + DEFAULT_LIBELE_AR + "," + UPDATED_LIBELE_AR);

        // Get all the regionList where libeleAr equals to UPDATED_LIBELE_AR
        defaultRegionShouldNotBeFound("libeleAr.in=" + UPDATED_LIBELE_AR);
    }

    @Test
    @Transactional
    void getAllRegionsByLibeleArIsNullOrNotNull() throws Exception {
        // Initialize the database
        regionRepository.saveAndFlush(region);

        // Get all the regionList where libeleAr is not null
        defaultRegionShouldBeFound("libeleAr.specified=true");

        // Get all the regionList where libeleAr is null
        defaultRegionShouldNotBeFound("libeleAr.specified=false");
    }

    @Test
    @Transactional
    void getAllRegionsByLibeleArContainsSomething() throws Exception {
        // Initialize the database
        regionRepository.saveAndFlush(region);

        // Get all the regionList where libeleAr contains DEFAULT_LIBELE_AR
        defaultRegionShouldBeFound("libeleAr.contains=" + DEFAULT_LIBELE_AR);

        // Get all the regionList where libeleAr contains UPDATED_LIBELE_AR
        defaultRegionShouldNotBeFound("libeleAr.contains=" + UPDATED_LIBELE_AR);
    }

    @Test
    @Transactional
    void getAllRegionsByLibeleArNotContainsSomething() throws Exception {
        // Initialize the database
        regionRepository.saveAndFlush(region);

        // Get all the regionList where libeleAr does not contain DEFAULT_LIBELE_AR
        defaultRegionShouldNotBeFound("libeleAr.doesNotContain=" + DEFAULT_LIBELE_AR);

        // Get all the regionList where libeleAr does not contain UPDATED_LIBELE_AR
        defaultRegionShouldBeFound("libeleAr.doesNotContain=" + UPDATED_LIBELE_AR);
    }

    @Test
    @Transactional
    void getAllRegionsByLibeleFrIsEqualToSomething() throws Exception {
        // Initialize the database
        regionRepository.saveAndFlush(region);

        // Get all the regionList where libeleFr equals to DEFAULT_LIBELE_FR
        defaultRegionShouldBeFound("libeleFr.equals=" + DEFAULT_LIBELE_FR);

        // Get all the regionList where libeleFr equals to UPDATED_LIBELE_FR
        defaultRegionShouldNotBeFound("libeleFr.equals=" + UPDATED_LIBELE_FR);
    }

    @Test
    @Transactional
    void getAllRegionsByLibeleFrIsNotEqualToSomething() throws Exception {
        // Initialize the database
        regionRepository.saveAndFlush(region);

        // Get all the regionList where libeleFr not equals to DEFAULT_LIBELE_FR
        defaultRegionShouldNotBeFound("libeleFr.notEquals=" + DEFAULT_LIBELE_FR);

        // Get all the regionList where libeleFr not equals to UPDATED_LIBELE_FR
        defaultRegionShouldBeFound("libeleFr.notEquals=" + UPDATED_LIBELE_FR);
    }

    @Test
    @Transactional
    void getAllRegionsByLibeleFrIsInShouldWork() throws Exception {
        // Initialize the database
        regionRepository.saveAndFlush(region);

        // Get all the regionList where libeleFr in DEFAULT_LIBELE_FR or UPDATED_LIBELE_FR
        defaultRegionShouldBeFound("libeleFr.in=" + DEFAULT_LIBELE_FR + "," + UPDATED_LIBELE_FR);

        // Get all the regionList where libeleFr equals to UPDATED_LIBELE_FR
        defaultRegionShouldNotBeFound("libeleFr.in=" + UPDATED_LIBELE_FR);
    }

    @Test
    @Transactional
    void getAllRegionsByLibeleFrIsNullOrNotNull() throws Exception {
        // Initialize the database
        regionRepository.saveAndFlush(region);

        // Get all the regionList where libeleFr is not null
        defaultRegionShouldBeFound("libeleFr.specified=true");

        // Get all the regionList where libeleFr is null
        defaultRegionShouldNotBeFound("libeleFr.specified=false");
    }

    @Test
    @Transactional
    void getAllRegionsByLibeleFrContainsSomething() throws Exception {
        // Initialize the database
        regionRepository.saveAndFlush(region);

        // Get all the regionList where libeleFr contains DEFAULT_LIBELE_FR
        defaultRegionShouldBeFound("libeleFr.contains=" + DEFAULT_LIBELE_FR);

        // Get all the regionList where libeleFr contains UPDATED_LIBELE_FR
        defaultRegionShouldNotBeFound("libeleFr.contains=" + UPDATED_LIBELE_FR);
    }

    @Test
    @Transactional
    void getAllRegionsByLibeleFrNotContainsSomething() throws Exception {
        // Initialize the database
        regionRepository.saveAndFlush(region);

        // Get all the regionList where libeleFr does not contain DEFAULT_LIBELE_FR
        defaultRegionShouldNotBeFound("libeleFr.doesNotContain=" + DEFAULT_LIBELE_FR);

        // Get all the regionList where libeleFr does not contain UPDATED_LIBELE_FR
        defaultRegionShouldBeFound("libeleFr.doesNotContain=" + UPDATED_LIBELE_FR);
    }

    @Test
    @Transactional
    void getAllRegionsByProvinceIsEqualToSomething() throws Exception {
        // Initialize the database
        regionRepository.saveAndFlush(region);
        Province province;
        if (TestUtil.findAll(em, Province.class).isEmpty()) {
            province = ProvinceResourceIT.createEntity(em);
            em.persist(province);
            em.flush();
        } else {
            province = TestUtil.findAll(em, Province.class).get(0);
        }
        em.persist(province);
        em.flush();
        region.addProvince(province);
        regionRepository.saveAndFlush(region);
        Long provinceId = province.getId();

        // Get all the regionList where province equals to provinceId
        defaultRegionShouldBeFound("provinceId.equals=" + provinceId);

        // Get all the regionList where province equals to (provinceId + 1)
        defaultRegionShouldNotBeFound("provinceId.equals=" + (provinceId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRegionShouldBeFound(String filter) throws Exception {
        restRegionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(region.getId().intValue())))
            .andExpect(jsonPath("$.[*].libeleAr").value(hasItem(DEFAULT_LIBELE_AR)))
            .andExpect(jsonPath("$.[*].libeleFr").value(hasItem(DEFAULT_LIBELE_FR)));

        // Check, that the count call also returns 1
        restRegionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRegionShouldNotBeFound(String filter) throws Exception {
        restRegionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRegionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRegion() throws Exception {
        // Get the region
        restRegionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRegion() throws Exception {
        // Initialize the database
        regionRepository.saveAndFlush(region);

        int databaseSizeBeforeUpdate = regionRepository.findAll().size();

        // Update the region
        Region updatedRegion = regionRepository.findById(region.getId()).get();
        // Disconnect from session so that the updates on updatedRegion are not directly saved in db
        em.detach(updatedRegion);
        updatedRegion.libeleAr(UPDATED_LIBELE_AR).libeleFr(UPDATED_LIBELE_FR);
        RegionDTO regionDTO = regionMapper.toDto(updatedRegion);

        restRegionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, regionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(regionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Region in the database
        List<Region> regionList = regionRepository.findAll();
        assertThat(regionList).hasSize(databaseSizeBeforeUpdate);
        Region testRegion = regionList.get(regionList.size() - 1);
        assertThat(testRegion.getLibeleAr()).isEqualTo(UPDATED_LIBELE_AR);
        assertThat(testRegion.getLibeleFr()).isEqualTo(UPDATED_LIBELE_FR);
    }

    @Test
    @Transactional
    void putNonExistingRegion() throws Exception {
        int databaseSizeBeforeUpdate = regionRepository.findAll().size();
        region.setId(count.incrementAndGet());

        // Create the Region
        RegionDTO regionDTO = regionMapper.toDto(region);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRegionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, regionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(regionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Region in the database
        List<Region> regionList = regionRepository.findAll();
        assertThat(regionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRegion() throws Exception {
        int databaseSizeBeforeUpdate = regionRepository.findAll().size();
        region.setId(count.incrementAndGet());

        // Create the Region
        RegionDTO regionDTO = regionMapper.toDto(region);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRegionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(regionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Region in the database
        List<Region> regionList = regionRepository.findAll();
        assertThat(regionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRegion() throws Exception {
        int databaseSizeBeforeUpdate = regionRepository.findAll().size();
        region.setId(count.incrementAndGet());

        // Create the Region
        RegionDTO regionDTO = regionMapper.toDto(region);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRegionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(regionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Region in the database
        List<Region> regionList = regionRepository.findAll();
        assertThat(regionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRegionWithPatch() throws Exception {
        // Initialize the database
        regionRepository.saveAndFlush(region);

        int databaseSizeBeforeUpdate = regionRepository.findAll().size();

        // Update the region using partial update
        Region partialUpdatedRegion = new Region();
        partialUpdatedRegion.setId(region.getId());

        partialUpdatedRegion.libeleAr(UPDATED_LIBELE_AR);

        restRegionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRegion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRegion))
            )
            .andExpect(status().isOk());

        // Validate the Region in the database
        List<Region> regionList = regionRepository.findAll();
        assertThat(regionList).hasSize(databaseSizeBeforeUpdate);
        Region testRegion = regionList.get(regionList.size() - 1);
        assertThat(testRegion.getLibeleAr()).isEqualTo(UPDATED_LIBELE_AR);
        assertThat(testRegion.getLibeleFr()).isEqualTo(DEFAULT_LIBELE_FR);
    }

    @Test
    @Transactional
    void fullUpdateRegionWithPatch() throws Exception {
        // Initialize the database
        regionRepository.saveAndFlush(region);

        int databaseSizeBeforeUpdate = regionRepository.findAll().size();

        // Update the region using partial update
        Region partialUpdatedRegion = new Region();
        partialUpdatedRegion.setId(region.getId());

        partialUpdatedRegion.libeleAr(UPDATED_LIBELE_AR).libeleFr(UPDATED_LIBELE_FR);

        restRegionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRegion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRegion))
            )
            .andExpect(status().isOk());

        // Validate the Region in the database
        List<Region> regionList = regionRepository.findAll();
        assertThat(regionList).hasSize(databaseSizeBeforeUpdate);
        Region testRegion = regionList.get(regionList.size() - 1);
        assertThat(testRegion.getLibeleAr()).isEqualTo(UPDATED_LIBELE_AR);
        assertThat(testRegion.getLibeleFr()).isEqualTo(UPDATED_LIBELE_FR);
    }

    @Test
    @Transactional
    void patchNonExistingRegion() throws Exception {
        int databaseSizeBeforeUpdate = regionRepository.findAll().size();
        region.setId(count.incrementAndGet());

        // Create the Region
        RegionDTO regionDTO = regionMapper.toDto(region);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRegionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, regionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(regionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Region in the database
        List<Region> regionList = regionRepository.findAll();
        assertThat(regionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRegion() throws Exception {
        int databaseSizeBeforeUpdate = regionRepository.findAll().size();
        region.setId(count.incrementAndGet());

        // Create the Region
        RegionDTO regionDTO = regionMapper.toDto(region);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRegionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(regionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Region in the database
        List<Region> regionList = regionRepository.findAll();
        assertThat(regionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRegion() throws Exception {
        int databaseSizeBeforeUpdate = regionRepository.findAll().size();
        region.setId(count.incrementAndGet());

        // Create the Region
        RegionDTO regionDTO = regionMapper.toDto(region);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRegionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(regionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Region in the database
        List<Region> regionList = regionRepository.findAll();
        assertThat(regionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRegion() throws Exception {
        // Initialize the database
        regionRepository.saveAndFlush(region);

        int databaseSizeBeforeDelete = regionRepository.findAll().size();

        // Delete the region
        restRegionMockMvc
            .perform(delete(ENTITY_API_URL_ID, region.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Region> regionList = regionRepository.findAll();
        assertThat(regionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
