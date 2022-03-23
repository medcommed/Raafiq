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
import ma.raafiq.domain.AppRole;
import ma.raafiq.domain.AppUser;
import ma.raafiq.repository.AppRoleRepository;
import ma.raafiq.service.criteria.AppRoleCriteria;
import ma.raafiq.service.dto.AppRoleDTO;
import ma.raafiq.service.mapper.AppRoleMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AppRoleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AppRoleResourceIT {

    private static final String DEFAULT_LIBELE_AR = "AAAAAAAAAA";
    private static final String UPDATED_LIBELE_AR = "BBBBBBBBBB";

    private static final String DEFAULT_LIBELE_FR = "AAAAAAAAAA";
    private static final String UPDATED_LIBELE_FR = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/app-roles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AppRoleRepository appRoleRepository;

    @Autowired
    private AppRoleMapper appRoleMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAppRoleMockMvc;

    private AppRole appRole;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppRole createEntity(EntityManager em) {
        AppRole appRole = new AppRole().libeleAr(DEFAULT_LIBELE_AR).libeleFr(DEFAULT_LIBELE_FR);
        return appRole;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppRole createUpdatedEntity(EntityManager em) {
        AppRole appRole = new AppRole().libeleAr(UPDATED_LIBELE_AR).libeleFr(UPDATED_LIBELE_FR);
        return appRole;
    }

    @BeforeEach
    public void initTest() {
        appRole = createEntity(em);
    }

    @Test
    @Transactional
    void createAppRole() throws Exception {
        int databaseSizeBeforeCreate = appRoleRepository.findAll().size();
        // Create the AppRole
        AppRoleDTO appRoleDTO = appRoleMapper.toDto(appRole);
        restAppRoleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appRoleDTO)))
            .andExpect(status().isCreated());

        // Validate the AppRole in the database
        List<AppRole> appRoleList = appRoleRepository.findAll();
        assertThat(appRoleList).hasSize(databaseSizeBeforeCreate + 1);
        AppRole testAppRole = appRoleList.get(appRoleList.size() - 1);
        assertThat(testAppRole.getLibeleAr()).isEqualTo(DEFAULT_LIBELE_AR);
        assertThat(testAppRole.getLibeleFr()).isEqualTo(DEFAULT_LIBELE_FR);
    }

    @Test
    @Transactional
    void createAppRoleWithExistingId() throws Exception {
        // Create the AppRole with an existing ID
        appRole.setId(1L);
        AppRoleDTO appRoleDTO = appRoleMapper.toDto(appRole);

        int databaseSizeBeforeCreate = appRoleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAppRoleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appRoleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AppRole in the database
        List<AppRole> appRoleList = appRoleRepository.findAll();
        assertThat(appRoleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAppRoles() throws Exception {
        // Initialize the database
        appRoleRepository.saveAndFlush(appRole);

        // Get all the appRoleList
        restAppRoleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appRole.getId().intValue())))
            .andExpect(jsonPath("$.[*].libeleAr").value(hasItem(DEFAULT_LIBELE_AR)))
            .andExpect(jsonPath("$.[*].libeleFr").value(hasItem(DEFAULT_LIBELE_FR)));
    }

    @Test
    @Transactional
    void getAppRole() throws Exception {
        // Initialize the database
        appRoleRepository.saveAndFlush(appRole);

        // Get the appRole
        restAppRoleMockMvc
            .perform(get(ENTITY_API_URL_ID, appRole.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(appRole.getId().intValue()))
            .andExpect(jsonPath("$.libeleAr").value(DEFAULT_LIBELE_AR))
            .andExpect(jsonPath("$.libeleFr").value(DEFAULT_LIBELE_FR));
    }

    @Test
    @Transactional
    void getAppRolesByIdFiltering() throws Exception {
        // Initialize the database
        appRoleRepository.saveAndFlush(appRole);

        Long id = appRole.getId();

        defaultAppRoleShouldBeFound("id.equals=" + id);
        defaultAppRoleShouldNotBeFound("id.notEquals=" + id);

        defaultAppRoleShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAppRoleShouldNotBeFound("id.greaterThan=" + id);

        defaultAppRoleShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAppRoleShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAppRolesByLibeleArIsEqualToSomething() throws Exception {
        // Initialize the database
        appRoleRepository.saveAndFlush(appRole);

        // Get all the appRoleList where libeleAr equals to DEFAULT_LIBELE_AR
        defaultAppRoleShouldBeFound("libeleAr.equals=" + DEFAULT_LIBELE_AR);

        // Get all the appRoleList where libeleAr equals to UPDATED_LIBELE_AR
        defaultAppRoleShouldNotBeFound("libeleAr.equals=" + UPDATED_LIBELE_AR);
    }

    @Test
    @Transactional
    void getAllAppRolesByLibeleArIsNotEqualToSomething() throws Exception {
        // Initialize the database
        appRoleRepository.saveAndFlush(appRole);

        // Get all the appRoleList where libeleAr not equals to DEFAULT_LIBELE_AR
        defaultAppRoleShouldNotBeFound("libeleAr.notEquals=" + DEFAULT_LIBELE_AR);

        // Get all the appRoleList where libeleAr not equals to UPDATED_LIBELE_AR
        defaultAppRoleShouldBeFound("libeleAr.notEquals=" + UPDATED_LIBELE_AR);
    }

    @Test
    @Transactional
    void getAllAppRolesByLibeleArIsInShouldWork() throws Exception {
        // Initialize the database
        appRoleRepository.saveAndFlush(appRole);

        // Get all the appRoleList where libeleAr in DEFAULT_LIBELE_AR or UPDATED_LIBELE_AR
        defaultAppRoleShouldBeFound("libeleAr.in=" + DEFAULT_LIBELE_AR + "," + UPDATED_LIBELE_AR);

        // Get all the appRoleList where libeleAr equals to UPDATED_LIBELE_AR
        defaultAppRoleShouldNotBeFound("libeleAr.in=" + UPDATED_LIBELE_AR);
    }

    @Test
    @Transactional
    void getAllAppRolesByLibeleArIsNullOrNotNull() throws Exception {
        // Initialize the database
        appRoleRepository.saveAndFlush(appRole);

        // Get all the appRoleList where libeleAr is not null
        defaultAppRoleShouldBeFound("libeleAr.specified=true");

        // Get all the appRoleList where libeleAr is null
        defaultAppRoleShouldNotBeFound("libeleAr.specified=false");
    }

    @Test
    @Transactional
    void getAllAppRolesByLibeleArContainsSomething() throws Exception {
        // Initialize the database
        appRoleRepository.saveAndFlush(appRole);

        // Get all the appRoleList where libeleAr contains DEFAULT_LIBELE_AR
        defaultAppRoleShouldBeFound("libeleAr.contains=" + DEFAULT_LIBELE_AR);

        // Get all the appRoleList where libeleAr contains UPDATED_LIBELE_AR
        defaultAppRoleShouldNotBeFound("libeleAr.contains=" + UPDATED_LIBELE_AR);
    }

    @Test
    @Transactional
    void getAllAppRolesByLibeleArNotContainsSomething() throws Exception {
        // Initialize the database
        appRoleRepository.saveAndFlush(appRole);

        // Get all the appRoleList where libeleAr does not contain DEFAULT_LIBELE_AR
        defaultAppRoleShouldNotBeFound("libeleAr.doesNotContain=" + DEFAULT_LIBELE_AR);

        // Get all the appRoleList where libeleAr does not contain UPDATED_LIBELE_AR
        defaultAppRoleShouldBeFound("libeleAr.doesNotContain=" + UPDATED_LIBELE_AR);
    }

    @Test
    @Transactional
    void getAllAppRolesByLibeleFrIsEqualToSomething() throws Exception {
        // Initialize the database
        appRoleRepository.saveAndFlush(appRole);

        // Get all the appRoleList where libeleFr equals to DEFAULT_LIBELE_FR
        defaultAppRoleShouldBeFound("libeleFr.equals=" + DEFAULT_LIBELE_FR);

        // Get all the appRoleList where libeleFr equals to UPDATED_LIBELE_FR
        defaultAppRoleShouldNotBeFound("libeleFr.equals=" + UPDATED_LIBELE_FR);
    }

    @Test
    @Transactional
    void getAllAppRolesByLibeleFrIsNotEqualToSomething() throws Exception {
        // Initialize the database
        appRoleRepository.saveAndFlush(appRole);

        // Get all the appRoleList where libeleFr not equals to DEFAULT_LIBELE_FR
        defaultAppRoleShouldNotBeFound("libeleFr.notEquals=" + DEFAULT_LIBELE_FR);

        // Get all the appRoleList where libeleFr not equals to UPDATED_LIBELE_FR
        defaultAppRoleShouldBeFound("libeleFr.notEquals=" + UPDATED_LIBELE_FR);
    }

    @Test
    @Transactional
    void getAllAppRolesByLibeleFrIsInShouldWork() throws Exception {
        // Initialize the database
        appRoleRepository.saveAndFlush(appRole);

        // Get all the appRoleList where libeleFr in DEFAULT_LIBELE_FR or UPDATED_LIBELE_FR
        defaultAppRoleShouldBeFound("libeleFr.in=" + DEFAULT_LIBELE_FR + "," + UPDATED_LIBELE_FR);

        // Get all the appRoleList where libeleFr equals to UPDATED_LIBELE_FR
        defaultAppRoleShouldNotBeFound("libeleFr.in=" + UPDATED_LIBELE_FR);
    }

    @Test
    @Transactional
    void getAllAppRolesByLibeleFrIsNullOrNotNull() throws Exception {
        // Initialize the database
        appRoleRepository.saveAndFlush(appRole);

        // Get all the appRoleList where libeleFr is not null
        defaultAppRoleShouldBeFound("libeleFr.specified=true");

        // Get all the appRoleList where libeleFr is null
        defaultAppRoleShouldNotBeFound("libeleFr.specified=false");
    }

    @Test
    @Transactional
    void getAllAppRolesByLibeleFrContainsSomething() throws Exception {
        // Initialize the database
        appRoleRepository.saveAndFlush(appRole);

        // Get all the appRoleList where libeleFr contains DEFAULT_LIBELE_FR
        defaultAppRoleShouldBeFound("libeleFr.contains=" + DEFAULT_LIBELE_FR);

        // Get all the appRoleList where libeleFr contains UPDATED_LIBELE_FR
        defaultAppRoleShouldNotBeFound("libeleFr.contains=" + UPDATED_LIBELE_FR);
    }

    @Test
    @Transactional
    void getAllAppRolesByLibeleFrNotContainsSomething() throws Exception {
        // Initialize the database
        appRoleRepository.saveAndFlush(appRole);

        // Get all the appRoleList where libeleFr does not contain DEFAULT_LIBELE_FR
        defaultAppRoleShouldNotBeFound("libeleFr.doesNotContain=" + DEFAULT_LIBELE_FR);

        // Get all the appRoleList where libeleFr does not contain UPDATED_LIBELE_FR
        defaultAppRoleShouldBeFound("libeleFr.doesNotContain=" + UPDATED_LIBELE_FR);
    }

    @Test
    @Transactional
    void getAllAppRolesByAppUserIsEqualToSomething() throws Exception {
        // Initialize the database
        appRoleRepository.saveAndFlush(appRole);
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
        appRole.addAppUser(appUser);
        appRoleRepository.saveAndFlush(appRole);
        Long appUserId = appUser.getId();

        // Get all the appRoleList where appUser equals to appUserId
        defaultAppRoleShouldBeFound("appUserId.equals=" + appUserId);

        // Get all the appRoleList where appUser equals to (appUserId + 1)
        defaultAppRoleShouldNotBeFound("appUserId.equals=" + (appUserId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAppRoleShouldBeFound(String filter) throws Exception {
        restAppRoleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appRole.getId().intValue())))
            .andExpect(jsonPath("$.[*].libeleAr").value(hasItem(DEFAULT_LIBELE_AR)))
            .andExpect(jsonPath("$.[*].libeleFr").value(hasItem(DEFAULT_LIBELE_FR)));

        // Check, that the count call also returns 1
        restAppRoleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAppRoleShouldNotBeFound(String filter) throws Exception {
        restAppRoleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAppRoleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAppRole() throws Exception {
        // Get the appRole
        restAppRoleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAppRole() throws Exception {
        // Initialize the database
        appRoleRepository.saveAndFlush(appRole);

        int databaseSizeBeforeUpdate = appRoleRepository.findAll().size();

        // Update the appRole
        AppRole updatedAppRole = appRoleRepository.findById(appRole.getId()).get();
        // Disconnect from session so that the updates on updatedAppRole are not directly saved in db
        em.detach(updatedAppRole);
        updatedAppRole.libeleAr(UPDATED_LIBELE_AR).libeleFr(UPDATED_LIBELE_FR);
        AppRoleDTO appRoleDTO = appRoleMapper.toDto(updatedAppRole);

        restAppRoleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, appRoleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appRoleDTO))
            )
            .andExpect(status().isOk());

        // Validate the AppRole in the database
        List<AppRole> appRoleList = appRoleRepository.findAll();
        assertThat(appRoleList).hasSize(databaseSizeBeforeUpdate);
        AppRole testAppRole = appRoleList.get(appRoleList.size() - 1);
        assertThat(testAppRole.getLibeleAr()).isEqualTo(UPDATED_LIBELE_AR);
        assertThat(testAppRole.getLibeleFr()).isEqualTo(UPDATED_LIBELE_FR);
    }

    @Test
    @Transactional
    void putNonExistingAppRole() throws Exception {
        int databaseSizeBeforeUpdate = appRoleRepository.findAll().size();
        appRole.setId(count.incrementAndGet());

        // Create the AppRole
        AppRoleDTO appRoleDTO = appRoleMapper.toDto(appRole);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppRoleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, appRoleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appRoleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppRole in the database
        List<AppRole> appRoleList = appRoleRepository.findAll();
        assertThat(appRoleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAppRole() throws Exception {
        int databaseSizeBeforeUpdate = appRoleRepository.findAll().size();
        appRole.setId(count.incrementAndGet());

        // Create the AppRole
        AppRoleDTO appRoleDTO = appRoleMapper.toDto(appRole);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppRoleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appRoleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppRole in the database
        List<AppRole> appRoleList = appRoleRepository.findAll();
        assertThat(appRoleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAppRole() throws Exception {
        int databaseSizeBeforeUpdate = appRoleRepository.findAll().size();
        appRole.setId(count.incrementAndGet());

        // Create the AppRole
        AppRoleDTO appRoleDTO = appRoleMapper.toDto(appRole);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppRoleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appRoleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AppRole in the database
        List<AppRole> appRoleList = appRoleRepository.findAll();
        assertThat(appRoleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAppRoleWithPatch() throws Exception {
        // Initialize the database
        appRoleRepository.saveAndFlush(appRole);

        int databaseSizeBeforeUpdate = appRoleRepository.findAll().size();

        // Update the appRole using partial update
        AppRole partialUpdatedAppRole = new AppRole();
        partialUpdatedAppRole.setId(appRole.getId());

        partialUpdatedAppRole.libeleAr(UPDATED_LIBELE_AR);

        restAppRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAppRole.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAppRole))
            )
            .andExpect(status().isOk());

        // Validate the AppRole in the database
        List<AppRole> appRoleList = appRoleRepository.findAll();
        assertThat(appRoleList).hasSize(databaseSizeBeforeUpdate);
        AppRole testAppRole = appRoleList.get(appRoleList.size() - 1);
        assertThat(testAppRole.getLibeleAr()).isEqualTo(UPDATED_LIBELE_AR);
        assertThat(testAppRole.getLibeleFr()).isEqualTo(DEFAULT_LIBELE_FR);
    }

    @Test
    @Transactional
    void fullUpdateAppRoleWithPatch() throws Exception {
        // Initialize the database
        appRoleRepository.saveAndFlush(appRole);

        int databaseSizeBeforeUpdate = appRoleRepository.findAll().size();

        // Update the appRole using partial update
        AppRole partialUpdatedAppRole = new AppRole();
        partialUpdatedAppRole.setId(appRole.getId());

        partialUpdatedAppRole.libeleAr(UPDATED_LIBELE_AR).libeleFr(UPDATED_LIBELE_FR);

        restAppRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAppRole.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAppRole))
            )
            .andExpect(status().isOk());

        // Validate the AppRole in the database
        List<AppRole> appRoleList = appRoleRepository.findAll();
        assertThat(appRoleList).hasSize(databaseSizeBeforeUpdate);
        AppRole testAppRole = appRoleList.get(appRoleList.size() - 1);
        assertThat(testAppRole.getLibeleAr()).isEqualTo(UPDATED_LIBELE_AR);
        assertThat(testAppRole.getLibeleFr()).isEqualTo(UPDATED_LIBELE_FR);
    }

    @Test
    @Transactional
    void patchNonExistingAppRole() throws Exception {
        int databaseSizeBeforeUpdate = appRoleRepository.findAll().size();
        appRole.setId(count.incrementAndGet());

        // Create the AppRole
        AppRoleDTO appRoleDTO = appRoleMapper.toDto(appRole);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, appRoleDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(appRoleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppRole in the database
        List<AppRole> appRoleList = appRoleRepository.findAll();
        assertThat(appRoleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAppRole() throws Exception {
        int databaseSizeBeforeUpdate = appRoleRepository.findAll().size();
        appRole.setId(count.incrementAndGet());

        // Create the AppRole
        AppRoleDTO appRoleDTO = appRoleMapper.toDto(appRole);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(appRoleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppRole in the database
        List<AppRole> appRoleList = appRoleRepository.findAll();
        assertThat(appRoleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAppRole() throws Exception {
        int databaseSizeBeforeUpdate = appRoleRepository.findAll().size();
        appRole.setId(count.incrementAndGet());

        // Create the AppRole
        AppRoleDTO appRoleDTO = appRoleMapper.toDto(appRole);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppRoleMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(appRoleDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AppRole in the database
        List<AppRole> appRoleList = appRoleRepository.findAll();
        assertThat(appRoleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAppRole() throws Exception {
        // Initialize the database
        appRoleRepository.saveAndFlush(appRole);

        int databaseSizeBeforeDelete = appRoleRepository.findAll().size();

        // Delete the appRole
        restAppRoleMockMvc
            .perform(delete(ENTITY_API_URL_ID, appRole.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AppRole> appRoleList = appRoleRepository.findAll();
        assertThat(appRoleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
