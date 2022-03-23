package ma.raafiq.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import ma.raafiq.IntegrationTest;
import ma.raafiq.domain.AppRole;
import ma.raafiq.domain.AppUser;
import ma.raafiq.domain.Beneficiaire;
import ma.raafiq.domain.Famille;
import ma.raafiq.domain.Professionnel;
import ma.raafiq.domain.Province;
import ma.raafiq.repository.AppUserRepository;
import ma.raafiq.service.criteria.AppUserCriteria;
import ma.raafiq.service.dto.AppUserDTO;
import ma.raafiq.service.mapper.AppUserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AppUserResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AppUserResourceIT {

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final Instant DEFAULT_DATE_CREATION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_CREATION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATE_MODIFICATION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_MODIFICATION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_ENTITE = "AAAAAAAAAA";
    private static final String UPDATED_ENTITE = "BBBBBBBBBB";

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM = "BBBBBBBBBB";

    private static final String DEFAULT_TELEPHONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEPHONE = "BBBBBBBBBB";

    private static final String DEFAULT_USER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_USER_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/app-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private AppUserMapper appUserMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAppUserMockMvc;

    private AppUser appUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppUser createEntity(EntityManager em) {
        AppUser appUser = new AppUser()
            .active(DEFAULT_ACTIVE)
            .dateCreation(DEFAULT_DATE_CREATION)
            .dateModification(DEFAULT_DATE_MODIFICATION)
            .email(DEFAULT_EMAIL)
            .entite(DEFAULT_ENTITE)
            .nom(DEFAULT_NOM)
            .password(DEFAULT_PASSWORD)
            .prenom(DEFAULT_PRENOM)
            .telephone(DEFAULT_TELEPHONE)
            .userName(DEFAULT_USER_NAME);
        return appUser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppUser createUpdatedEntity(EntityManager em) {
        AppUser appUser = new AppUser()
            .active(UPDATED_ACTIVE)
            .dateCreation(UPDATED_DATE_CREATION)
            .dateModification(UPDATED_DATE_MODIFICATION)
            .email(UPDATED_EMAIL)
            .entite(UPDATED_ENTITE)
            .nom(UPDATED_NOM)
            .password(UPDATED_PASSWORD)
            .prenom(UPDATED_PRENOM)
            .telephone(UPDATED_TELEPHONE)
            .userName(UPDATED_USER_NAME);
        return appUser;
    }

    @BeforeEach
    public void initTest() {
        appUser = createEntity(em);
    }

    @Test
    @Transactional
    void createAppUser() throws Exception {
        int databaseSizeBeforeCreate = appUserRepository.findAll().size();
        // Create the AppUser
        AppUserDTO appUserDTO = appUserMapper.toDto(appUser);
        restAppUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appUserDTO)))
            .andExpect(status().isCreated());

        // Validate the AppUser in the database
        List<AppUser> appUserList = appUserRepository.findAll();
        assertThat(appUserList).hasSize(databaseSizeBeforeCreate + 1);
        AppUser testAppUser = appUserList.get(appUserList.size() - 1);
        assertThat(testAppUser.getActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testAppUser.getDateCreation()).isEqualTo(DEFAULT_DATE_CREATION);
        assertThat(testAppUser.getDateModification()).isEqualTo(DEFAULT_DATE_MODIFICATION);
        assertThat(testAppUser.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testAppUser.getEntite()).isEqualTo(DEFAULT_ENTITE);
        assertThat(testAppUser.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testAppUser.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testAppUser.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testAppUser.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
        assertThat(testAppUser.getUserName()).isEqualTo(DEFAULT_USER_NAME);
    }

    @Test
    @Transactional
    void createAppUserWithExistingId() throws Exception {
        // Create the AppUser with an existing ID
        appUser.setId(1L);
        AppUserDTO appUserDTO = appUserMapper.toDto(appUser);

        int databaseSizeBeforeCreate = appUserRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAppUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appUserDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AppUser in the database
        List<AppUser> appUserList = appUserRepository.findAll();
        assertThat(appUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = appUserRepository.findAll().size();
        // set the field null
        appUser.setActive(null);

        // Create the AppUser, which fails.
        AppUserDTO appUserDTO = appUserMapper.toDto(appUser);

        restAppUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appUserDTO)))
            .andExpect(status().isBadRequest());

        List<AppUser> appUserList = appUserRepository.findAll();
        assertThat(appUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = appUserRepository.findAll().size();
        // set the field null
        appUser.setEmail(null);

        // Create the AppUser, which fails.
        AppUserDTO appUserDTO = appUserMapper.toDto(appUser);

        restAppUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appUserDTO)))
            .andExpect(status().isBadRequest());

        List<AppUser> appUserList = appUserRepository.findAll();
        assertThat(appUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPasswordIsRequired() throws Exception {
        int databaseSizeBeforeTest = appUserRepository.findAll().size();
        // set the field null
        appUser.setPassword(null);

        // Create the AppUser, which fails.
        AppUserDTO appUserDTO = appUserMapper.toDto(appUser);

        restAppUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appUserDTO)))
            .andExpect(status().isBadRequest());

        List<AppUser> appUserList = appUserRepository.findAll();
        assertThat(appUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUserNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = appUserRepository.findAll().size();
        // set the field null
        appUser.setUserName(null);

        // Create the AppUser, which fails.
        AppUserDTO appUserDTO = appUserMapper.toDto(appUser);

        restAppUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appUserDTO)))
            .andExpect(status().isBadRequest());

        List<AppUser> appUserList = appUserRepository.findAll();
        assertThat(appUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAppUsers() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList
        restAppUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(DEFAULT_DATE_CREATION.toString())))
            .andExpect(jsonPath("$.[*].dateModification").value(hasItem(DEFAULT_DATE_MODIFICATION.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].entite").value(hasItem(DEFAULT_ENTITE)))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)))
            .andExpect(jsonPath("$.[*].userName").value(hasItem(DEFAULT_USER_NAME)));
    }

    @Test
    @Transactional
    void getAppUser() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

        // Get the appUser
        restAppUserMockMvc
            .perform(get(ENTITY_API_URL_ID, appUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(appUser.getId().intValue()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.dateCreation").value(DEFAULT_DATE_CREATION.toString()))
            .andExpect(jsonPath("$.dateModification").value(DEFAULT_DATE_MODIFICATION.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.entite").value(DEFAULT_ENTITE))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD))
            .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM))
            .andExpect(jsonPath("$.telephone").value(DEFAULT_TELEPHONE))
            .andExpect(jsonPath("$.userName").value(DEFAULT_USER_NAME));
    }

    @Test
    @Transactional
    void getAppUsersByIdFiltering() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

        Long id = appUser.getId();

        defaultAppUserShouldBeFound("id.equals=" + id);
        defaultAppUserShouldNotBeFound("id.notEquals=" + id);

        defaultAppUserShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAppUserShouldNotBeFound("id.greaterThan=" + id);

        defaultAppUserShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAppUserShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAppUsersByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where active equals to DEFAULT_ACTIVE
        defaultAppUserShouldBeFound("active.equals=" + DEFAULT_ACTIVE);

        // Get all the appUserList where active equals to UPDATED_ACTIVE
        defaultAppUserShouldNotBeFound("active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllAppUsersByActiveIsNotEqualToSomething() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where active not equals to DEFAULT_ACTIVE
        defaultAppUserShouldNotBeFound("active.notEquals=" + DEFAULT_ACTIVE);

        // Get all the appUserList where active not equals to UPDATED_ACTIVE
        defaultAppUserShouldBeFound("active.notEquals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllAppUsersByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where active in DEFAULT_ACTIVE or UPDATED_ACTIVE
        defaultAppUserShouldBeFound("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE);

        // Get all the appUserList where active equals to UPDATED_ACTIVE
        defaultAppUserShouldNotBeFound("active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllAppUsersByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where active is not null
        defaultAppUserShouldBeFound("active.specified=true");

        // Get all the appUserList where active is null
        defaultAppUserShouldNotBeFound("active.specified=false");
    }

    @Test
    @Transactional
    void getAllAppUsersByDateCreationIsEqualToSomething() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where dateCreation equals to DEFAULT_DATE_CREATION
        defaultAppUserShouldBeFound("dateCreation.equals=" + DEFAULT_DATE_CREATION);

        // Get all the appUserList where dateCreation equals to UPDATED_DATE_CREATION
        defaultAppUserShouldNotBeFound("dateCreation.equals=" + UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    void getAllAppUsersByDateCreationIsNotEqualToSomething() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where dateCreation not equals to DEFAULT_DATE_CREATION
        defaultAppUserShouldNotBeFound("dateCreation.notEquals=" + DEFAULT_DATE_CREATION);

        // Get all the appUserList where dateCreation not equals to UPDATED_DATE_CREATION
        defaultAppUserShouldBeFound("dateCreation.notEquals=" + UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    void getAllAppUsersByDateCreationIsInShouldWork() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where dateCreation in DEFAULT_DATE_CREATION or UPDATED_DATE_CREATION
        defaultAppUserShouldBeFound("dateCreation.in=" + DEFAULT_DATE_CREATION + "," + UPDATED_DATE_CREATION);

        // Get all the appUserList where dateCreation equals to UPDATED_DATE_CREATION
        defaultAppUserShouldNotBeFound("dateCreation.in=" + UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    void getAllAppUsersByDateCreationIsNullOrNotNull() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where dateCreation is not null
        defaultAppUserShouldBeFound("dateCreation.specified=true");

        // Get all the appUserList where dateCreation is null
        defaultAppUserShouldNotBeFound("dateCreation.specified=false");
    }

    @Test
    @Transactional
    void getAllAppUsersByDateModificationIsEqualToSomething() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where dateModification equals to DEFAULT_DATE_MODIFICATION
        defaultAppUserShouldBeFound("dateModification.equals=" + DEFAULT_DATE_MODIFICATION);

        // Get all the appUserList where dateModification equals to UPDATED_DATE_MODIFICATION
        defaultAppUserShouldNotBeFound("dateModification.equals=" + UPDATED_DATE_MODIFICATION);
    }

    @Test
    @Transactional
    void getAllAppUsersByDateModificationIsNotEqualToSomething() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where dateModification not equals to DEFAULT_DATE_MODIFICATION
        defaultAppUserShouldNotBeFound("dateModification.notEquals=" + DEFAULT_DATE_MODIFICATION);

        // Get all the appUserList where dateModification not equals to UPDATED_DATE_MODIFICATION
        defaultAppUserShouldBeFound("dateModification.notEquals=" + UPDATED_DATE_MODIFICATION);
    }

    @Test
    @Transactional
    void getAllAppUsersByDateModificationIsInShouldWork() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where dateModification in DEFAULT_DATE_MODIFICATION or UPDATED_DATE_MODIFICATION
        defaultAppUserShouldBeFound("dateModification.in=" + DEFAULT_DATE_MODIFICATION + "," + UPDATED_DATE_MODIFICATION);

        // Get all the appUserList where dateModification equals to UPDATED_DATE_MODIFICATION
        defaultAppUserShouldNotBeFound("dateModification.in=" + UPDATED_DATE_MODIFICATION);
    }

    @Test
    @Transactional
    void getAllAppUsersByDateModificationIsNullOrNotNull() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where dateModification is not null
        defaultAppUserShouldBeFound("dateModification.specified=true");

        // Get all the appUserList where dateModification is null
        defaultAppUserShouldNotBeFound("dateModification.specified=false");
    }

    @Test
    @Transactional
    void getAllAppUsersByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where email equals to DEFAULT_EMAIL
        defaultAppUserShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the appUserList where email equals to UPDATED_EMAIL
        defaultAppUserShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllAppUsersByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where email not equals to DEFAULT_EMAIL
        defaultAppUserShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the appUserList where email not equals to UPDATED_EMAIL
        defaultAppUserShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllAppUsersByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultAppUserShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the appUserList where email equals to UPDATED_EMAIL
        defaultAppUserShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllAppUsersByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where email is not null
        defaultAppUserShouldBeFound("email.specified=true");

        // Get all the appUserList where email is null
        defaultAppUserShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllAppUsersByEmailContainsSomething() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where email contains DEFAULT_EMAIL
        defaultAppUserShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the appUserList where email contains UPDATED_EMAIL
        defaultAppUserShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllAppUsersByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where email does not contain DEFAULT_EMAIL
        defaultAppUserShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the appUserList where email does not contain UPDATED_EMAIL
        defaultAppUserShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllAppUsersByEntiteIsEqualToSomething() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where entite equals to DEFAULT_ENTITE
        defaultAppUserShouldBeFound("entite.equals=" + DEFAULT_ENTITE);

        // Get all the appUserList where entite equals to UPDATED_ENTITE
        defaultAppUserShouldNotBeFound("entite.equals=" + UPDATED_ENTITE);
    }

    @Test
    @Transactional
    void getAllAppUsersByEntiteIsNotEqualToSomething() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where entite not equals to DEFAULT_ENTITE
        defaultAppUserShouldNotBeFound("entite.notEquals=" + DEFAULT_ENTITE);

        // Get all the appUserList where entite not equals to UPDATED_ENTITE
        defaultAppUserShouldBeFound("entite.notEquals=" + UPDATED_ENTITE);
    }

    @Test
    @Transactional
    void getAllAppUsersByEntiteIsInShouldWork() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where entite in DEFAULT_ENTITE or UPDATED_ENTITE
        defaultAppUserShouldBeFound("entite.in=" + DEFAULT_ENTITE + "," + UPDATED_ENTITE);

        // Get all the appUserList where entite equals to UPDATED_ENTITE
        defaultAppUserShouldNotBeFound("entite.in=" + UPDATED_ENTITE);
    }

    @Test
    @Transactional
    void getAllAppUsersByEntiteIsNullOrNotNull() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where entite is not null
        defaultAppUserShouldBeFound("entite.specified=true");

        // Get all the appUserList where entite is null
        defaultAppUserShouldNotBeFound("entite.specified=false");
    }

    @Test
    @Transactional
    void getAllAppUsersByEntiteContainsSomething() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where entite contains DEFAULT_ENTITE
        defaultAppUserShouldBeFound("entite.contains=" + DEFAULT_ENTITE);

        // Get all the appUserList where entite contains UPDATED_ENTITE
        defaultAppUserShouldNotBeFound("entite.contains=" + UPDATED_ENTITE);
    }

    @Test
    @Transactional
    void getAllAppUsersByEntiteNotContainsSomething() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where entite does not contain DEFAULT_ENTITE
        defaultAppUserShouldNotBeFound("entite.doesNotContain=" + DEFAULT_ENTITE);

        // Get all the appUserList where entite does not contain UPDATED_ENTITE
        defaultAppUserShouldBeFound("entite.doesNotContain=" + UPDATED_ENTITE);
    }

    @Test
    @Transactional
    void getAllAppUsersByNomIsEqualToSomething() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where nom equals to DEFAULT_NOM
        defaultAppUserShouldBeFound("nom.equals=" + DEFAULT_NOM);

        // Get all the appUserList where nom equals to UPDATED_NOM
        defaultAppUserShouldNotBeFound("nom.equals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllAppUsersByNomIsNotEqualToSomething() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where nom not equals to DEFAULT_NOM
        defaultAppUserShouldNotBeFound("nom.notEquals=" + DEFAULT_NOM);

        // Get all the appUserList where nom not equals to UPDATED_NOM
        defaultAppUserShouldBeFound("nom.notEquals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllAppUsersByNomIsInShouldWork() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where nom in DEFAULT_NOM or UPDATED_NOM
        defaultAppUserShouldBeFound("nom.in=" + DEFAULT_NOM + "," + UPDATED_NOM);

        // Get all the appUserList where nom equals to UPDATED_NOM
        defaultAppUserShouldNotBeFound("nom.in=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllAppUsersByNomIsNullOrNotNull() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where nom is not null
        defaultAppUserShouldBeFound("nom.specified=true");

        // Get all the appUserList where nom is null
        defaultAppUserShouldNotBeFound("nom.specified=false");
    }

    @Test
    @Transactional
    void getAllAppUsersByNomContainsSomething() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where nom contains DEFAULT_NOM
        defaultAppUserShouldBeFound("nom.contains=" + DEFAULT_NOM);

        // Get all the appUserList where nom contains UPDATED_NOM
        defaultAppUserShouldNotBeFound("nom.contains=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllAppUsersByNomNotContainsSomething() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where nom does not contain DEFAULT_NOM
        defaultAppUserShouldNotBeFound("nom.doesNotContain=" + DEFAULT_NOM);

        // Get all the appUserList where nom does not contain UPDATED_NOM
        defaultAppUserShouldBeFound("nom.doesNotContain=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllAppUsersByPasswordIsEqualToSomething() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where password equals to DEFAULT_PASSWORD
        defaultAppUserShouldBeFound("password.equals=" + DEFAULT_PASSWORD);

        // Get all the appUserList where password equals to UPDATED_PASSWORD
        defaultAppUserShouldNotBeFound("password.equals=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void getAllAppUsersByPasswordIsNotEqualToSomething() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where password not equals to DEFAULT_PASSWORD
        defaultAppUserShouldNotBeFound("password.notEquals=" + DEFAULT_PASSWORD);

        // Get all the appUserList where password not equals to UPDATED_PASSWORD
        defaultAppUserShouldBeFound("password.notEquals=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void getAllAppUsersByPasswordIsInShouldWork() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where password in DEFAULT_PASSWORD or UPDATED_PASSWORD
        defaultAppUserShouldBeFound("password.in=" + DEFAULT_PASSWORD + "," + UPDATED_PASSWORD);

        // Get all the appUserList where password equals to UPDATED_PASSWORD
        defaultAppUserShouldNotBeFound("password.in=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void getAllAppUsersByPasswordIsNullOrNotNull() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where password is not null
        defaultAppUserShouldBeFound("password.specified=true");

        // Get all the appUserList where password is null
        defaultAppUserShouldNotBeFound("password.specified=false");
    }

    @Test
    @Transactional
    void getAllAppUsersByPasswordContainsSomething() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where password contains DEFAULT_PASSWORD
        defaultAppUserShouldBeFound("password.contains=" + DEFAULT_PASSWORD);

        // Get all the appUserList where password contains UPDATED_PASSWORD
        defaultAppUserShouldNotBeFound("password.contains=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void getAllAppUsersByPasswordNotContainsSomething() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where password does not contain DEFAULT_PASSWORD
        defaultAppUserShouldNotBeFound("password.doesNotContain=" + DEFAULT_PASSWORD);

        // Get all the appUserList where password does not contain UPDATED_PASSWORD
        defaultAppUserShouldBeFound("password.doesNotContain=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void getAllAppUsersByPrenomIsEqualToSomething() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where prenom equals to DEFAULT_PRENOM
        defaultAppUserShouldBeFound("prenom.equals=" + DEFAULT_PRENOM);

        // Get all the appUserList where prenom equals to UPDATED_PRENOM
        defaultAppUserShouldNotBeFound("prenom.equals=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    void getAllAppUsersByPrenomIsNotEqualToSomething() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where prenom not equals to DEFAULT_PRENOM
        defaultAppUserShouldNotBeFound("prenom.notEquals=" + DEFAULT_PRENOM);

        // Get all the appUserList where prenom not equals to UPDATED_PRENOM
        defaultAppUserShouldBeFound("prenom.notEquals=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    void getAllAppUsersByPrenomIsInShouldWork() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where prenom in DEFAULT_PRENOM or UPDATED_PRENOM
        defaultAppUserShouldBeFound("prenom.in=" + DEFAULT_PRENOM + "," + UPDATED_PRENOM);

        // Get all the appUserList where prenom equals to UPDATED_PRENOM
        defaultAppUserShouldNotBeFound("prenom.in=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    void getAllAppUsersByPrenomIsNullOrNotNull() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where prenom is not null
        defaultAppUserShouldBeFound("prenom.specified=true");

        // Get all the appUserList where prenom is null
        defaultAppUserShouldNotBeFound("prenom.specified=false");
    }

    @Test
    @Transactional
    void getAllAppUsersByPrenomContainsSomething() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where prenom contains DEFAULT_PRENOM
        defaultAppUserShouldBeFound("prenom.contains=" + DEFAULT_PRENOM);

        // Get all the appUserList where prenom contains UPDATED_PRENOM
        defaultAppUserShouldNotBeFound("prenom.contains=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    void getAllAppUsersByPrenomNotContainsSomething() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where prenom does not contain DEFAULT_PRENOM
        defaultAppUserShouldNotBeFound("prenom.doesNotContain=" + DEFAULT_PRENOM);

        // Get all the appUserList where prenom does not contain UPDATED_PRENOM
        defaultAppUserShouldBeFound("prenom.doesNotContain=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    void getAllAppUsersByTelephoneIsEqualToSomething() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where telephone equals to DEFAULT_TELEPHONE
        defaultAppUserShouldBeFound("telephone.equals=" + DEFAULT_TELEPHONE);

        // Get all the appUserList where telephone equals to UPDATED_TELEPHONE
        defaultAppUserShouldNotBeFound("telephone.equals=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllAppUsersByTelephoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where telephone not equals to DEFAULT_TELEPHONE
        defaultAppUserShouldNotBeFound("telephone.notEquals=" + DEFAULT_TELEPHONE);

        // Get all the appUserList where telephone not equals to UPDATED_TELEPHONE
        defaultAppUserShouldBeFound("telephone.notEquals=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllAppUsersByTelephoneIsInShouldWork() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where telephone in DEFAULT_TELEPHONE or UPDATED_TELEPHONE
        defaultAppUserShouldBeFound("telephone.in=" + DEFAULT_TELEPHONE + "," + UPDATED_TELEPHONE);

        // Get all the appUserList where telephone equals to UPDATED_TELEPHONE
        defaultAppUserShouldNotBeFound("telephone.in=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllAppUsersByTelephoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where telephone is not null
        defaultAppUserShouldBeFound("telephone.specified=true");

        // Get all the appUserList where telephone is null
        defaultAppUserShouldNotBeFound("telephone.specified=false");
    }

    @Test
    @Transactional
    void getAllAppUsersByTelephoneContainsSomething() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where telephone contains DEFAULT_TELEPHONE
        defaultAppUserShouldBeFound("telephone.contains=" + DEFAULT_TELEPHONE);

        // Get all the appUserList where telephone contains UPDATED_TELEPHONE
        defaultAppUserShouldNotBeFound("telephone.contains=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllAppUsersByTelephoneNotContainsSomething() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where telephone does not contain DEFAULT_TELEPHONE
        defaultAppUserShouldNotBeFound("telephone.doesNotContain=" + DEFAULT_TELEPHONE);

        // Get all the appUserList where telephone does not contain UPDATED_TELEPHONE
        defaultAppUserShouldBeFound("telephone.doesNotContain=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllAppUsersByUserNameIsEqualToSomething() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where userName equals to DEFAULT_USER_NAME
        defaultAppUserShouldBeFound("userName.equals=" + DEFAULT_USER_NAME);

        // Get all the appUserList where userName equals to UPDATED_USER_NAME
        defaultAppUserShouldNotBeFound("userName.equals=" + UPDATED_USER_NAME);
    }

    @Test
    @Transactional
    void getAllAppUsersByUserNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where userName not equals to DEFAULT_USER_NAME
        defaultAppUserShouldNotBeFound("userName.notEquals=" + DEFAULT_USER_NAME);

        // Get all the appUserList where userName not equals to UPDATED_USER_NAME
        defaultAppUserShouldBeFound("userName.notEquals=" + UPDATED_USER_NAME);
    }

    @Test
    @Transactional
    void getAllAppUsersByUserNameIsInShouldWork() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where userName in DEFAULT_USER_NAME or UPDATED_USER_NAME
        defaultAppUserShouldBeFound("userName.in=" + DEFAULT_USER_NAME + "," + UPDATED_USER_NAME);

        // Get all the appUserList where userName equals to UPDATED_USER_NAME
        defaultAppUserShouldNotBeFound("userName.in=" + UPDATED_USER_NAME);
    }

    @Test
    @Transactional
    void getAllAppUsersByUserNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where userName is not null
        defaultAppUserShouldBeFound("userName.specified=true");

        // Get all the appUserList where userName is null
        defaultAppUserShouldNotBeFound("userName.specified=false");
    }

    @Test
    @Transactional
    void getAllAppUsersByUserNameContainsSomething() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where userName contains DEFAULT_USER_NAME
        defaultAppUserShouldBeFound("userName.contains=" + DEFAULT_USER_NAME);

        // Get all the appUserList where userName contains UPDATED_USER_NAME
        defaultAppUserShouldNotBeFound("userName.contains=" + UPDATED_USER_NAME);
    }

    @Test
    @Transactional
    void getAllAppUsersByUserNameNotContainsSomething() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where userName does not contain DEFAULT_USER_NAME
        defaultAppUserShouldNotBeFound("userName.doesNotContain=" + DEFAULT_USER_NAME);

        // Get all the appUserList where userName does not contain UPDATED_USER_NAME
        defaultAppUserShouldBeFound("userName.doesNotContain=" + UPDATED_USER_NAME);
    }

    @Test
    @Transactional
    void getAllAppUsersByAppRoleIsEqualToSomething() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);
        AppRole appRole;
        if (TestUtil.findAll(em, AppRole.class).isEmpty()) {
            appRole = AppRoleResourceIT.createEntity(em);
            em.persist(appRole);
            em.flush();
        } else {
            appRole = TestUtil.findAll(em, AppRole.class).get(0);
        }
        em.persist(appRole);
        em.flush();
        appUser.setAppRole(appRole);
        appUserRepository.saveAndFlush(appUser);
        Long appRoleId = appRole.getId();

        // Get all the appUserList where appRole equals to appRoleId
        defaultAppUserShouldBeFound("appRoleId.equals=" + appRoleId);

        // Get all the appUserList where appRole equals to (appRoleId + 1)
        defaultAppUserShouldNotBeFound("appRoleId.equals=" + (appRoleId + 1));
    }

    @Test
    @Transactional
    void getAllAppUsersByProvinceIsEqualToSomething() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);
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
        appUser.setProvince(province);
        appUserRepository.saveAndFlush(appUser);
        Long provinceId = province.getId();

        // Get all the appUserList where province equals to provinceId
        defaultAppUserShouldBeFound("provinceId.equals=" + provinceId);

        // Get all the appUserList where province equals to (provinceId + 1)
        defaultAppUserShouldNotBeFound("provinceId.equals=" + (provinceId + 1));
    }

    @Test
    @Transactional
    void getAllAppUsersByBeneficiaireIsEqualToSomething() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);
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
        appUser.addBeneficiaire(beneficiaire);
        appUserRepository.saveAndFlush(appUser);
        Long beneficiaireId = beneficiaire.getId();

        // Get all the appUserList where beneficiaire equals to beneficiaireId
        defaultAppUserShouldBeFound("beneficiaireId.equals=" + beneficiaireId);

        // Get all the appUserList where beneficiaire equals to (beneficiaireId + 1)
        defaultAppUserShouldNotBeFound("beneficiaireId.equals=" + (beneficiaireId + 1));
    }

    @Test
    @Transactional
    void getAllAppUsersByFamilleIsEqualToSomething() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);
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
        appUser.addFamille(famille);
        appUserRepository.saveAndFlush(appUser);
        Long familleId = famille.getId();

        // Get all the appUserList where famille equals to familleId
        defaultAppUserShouldBeFound("familleId.equals=" + familleId);

        // Get all the appUserList where famille equals to (familleId + 1)
        defaultAppUserShouldNotBeFound("familleId.equals=" + (familleId + 1));
    }

    @Test
    @Transactional
    void getAllAppUsersByProfessionnelIsEqualToSomething() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);
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
        appUser.addProfessionnel(professionnel);
        appUserRepository.saveAndFlush(appUser);
        Long professionnelId = professionnel.getId();

        // Get all the appUserList where professionnel equals to professionnelId
        defaultAppUserShouldBeFound("professionnelId.equals=" + professionnelId);

        // Get all the appUserList where professionnel equals to (professionnelId + 1)
        defaultAppUserShouldNotBeFound("professionnelId.equals=" + (professionnelId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAppUserShouldBeFound(String filter) throws Exception {
        restAppUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(DEFAULT_DATE_CREATION.toString())))
            .andExpect(jsonPath("$.[*].dateModification").value(hasItem(DEFAULT_DATE_MODIFICATION.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].entite").value(hasItem(DEFAULT_ENTITE)))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)))
            .andExpect(jsonPath("$.[*].userName").value(hasItem(DEFAULT_USER_NAME)));

        // Check, that the count call also returns 1
        restAppUserMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAppUserShouldNotBeFound(String filter) throws Exception {
        restAppUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAppUserMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAppUser() throws Exception {
        // Get the appUser
        restAppUserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAppUser() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

        int databaseSizeBeforeUpdate = appUserRepository.findAll().size();

        // Update the appUser
        AppUser updatedAppUser = appUserRepository.findById(appUser.getId()).get();
        // Disconnect from session so that the updates on updatedAppUser are not directly saved in db
        em.detach(updatedAppUser);
        updatedAppUser
            .active(UPDATED_ACTIVE)
            .dateCreation(UPDATED_DATE_CREATION)
            .dateModification(UPDATED_DATE_MODIFICATION)
            .email(UPDATED_EMAIL)
            .entite(UPDATED_ENTITE)
            .nom(UPDATED_NOM)
            .password(UPDATED_PASSWORD)
            .prenom(UPDATED_PRENOM)
            .telephone(UPDATED_TELEPHONE)
            .userName(UPDATED_USER_NAME);
        AppUserDTO appUserDTO = appUserMapper.toDto(updatedAppUser);

        restAppUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, appUserDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appUserDTO))
            )
            .andExpect(status().isOk());

        // Validate the AppUser in the database
        List<AppUser> appUserList = appUserRepository.findAll();
        assertThat(appUserList).hasSize(databaseSizeBeforeUpdate);
        AppUser testAppUser = appUserList.get(appUserList.size() - 1);
        assertThat(testAppUser.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testAppUser.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
        assertThat(testAppUser.getDateModification()).isEqualTo(UPDATED_DATE_MODIFICATION);
        assertThat(testAppUser.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testAppUser.getEntite()).isEqualTo(UPDATED_ENTITE);
        assertThat(testAppUser.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testAppUser.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testAppUser.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testAppUser.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testAppUser.getUserName()).isEqualTo(UPDATED_USER_NAME);
    }

    @Test
    @Transactional
    void putNonExistingAppUser() throws Exception {
        int databaseSizeBeforeUpdate = appUserRepository.findAll().size();
        appUser.setId(count.incrementAndGet());

        // Create the AppUser
        AppUserDTO appUserDTO = appUserMapper.toDto(appUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, appUserDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppUser in the database
        List<AppUser> appUserList = appUserRepository.findAll();
        assertThat(appUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAppUser() throws Exception {
        int databaseSizeBeforeUpdate = appUserRepository.findAll().size();
        appUser.setId(count.incrementAndGet());

        // Create the AppUser
        AppUserDTO appUserDTO = appUserMapper.toDto(appUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppUser in the database
        List<AppUser> appUserList = appUserRepository.findAll();
        assertThat(appUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAppUser() throws Exception {
        int databaseSizeBeforeUpdate = appUserRepository.findAll().size();
        appUser.setId(count.incrementAndGet());

        // Create the AppUser
        AppUserDTO appUserDTO = appUserMapper.toDto(appUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppUserMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appUserDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AppUser in the database
        List<AppUser> appUserList = appUserRepository.findAll();
        assertThat(appUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAppUserWithPatch() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

        int databaseSizeBeforeUpdate = appUserRepository.findAll().size();

        // Update the appUser using partial update
        AppUser partialUpdatedAppUser = new AppUser();
        partialUpdatedAppUser.setId(appUser.getId());

        partialUpdatedAppUser
            .active(UPDATED_ACTIVE)
            .dateCreation(UPDATED_DATE_CREATION)
            .entite(UPDATED_ENTITE)
            .nom(UPDATED_NOM)
            .telephone(UPDATED_TELEPHONE)
            .userName(UPDATED_USER_NAME);

        restAppUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAppUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAppUser))
            )
            .andExpect(status().isOk());

        // Validate the AppUser in the database
        List<AppUser> appUserList = appUserRepository.findAll();
        assertThat(appUserList).hasSize(databaseSizeBeforeUpdate);
        AppUser testAppUser = appUserList.get(appUserList.size() - 1);
        assertThat(testAppUser.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testAppUser.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
        assertThat(testAppUser.getDateModification()).isEqualTo(DEFAULT_DATE_MODIFICATION);
        assertThat(testAppUser.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testAppUser.getEntite()).isEqualTo(UPDATED_ENTITE);
        assertThat(testAppUser.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testAppUser.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testAppUser.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testAppUser.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testAppUser.getUserName()).isEqualTo(UPDATED_USER_NAME);
    }

    @Test
    @Transactional
    void fullUpdateAppUserWithPatch() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

        int databaseSizeBeforeUpdate = appUserRepository.findAll().size();

        // Update the appUser using partial update
        AppUser partialUpdatedAppUser = new AppUser();
        partialUpdatedAppUser.setId(appUser.getId());

        partialUpdatedAppUser
            .active(UPDATED_ACTIVE)
            .dateCreation(UPDATED_DATE_CREATION)
            .dateModification(UPDATED_DATE_MODIFICATION)
            .email(UPDATED_EMAIL)
            .entite(UPDATED_ENTITE)
            .nom(UPDATED_NOM)
            .password(UPDATED_PASSWORD)
            .prenom(UPDATED_PRENOM)
            .telephone(UPDATED_TELEPHONE)
            .userName(UPDATED_USER_NAME);

        restAppUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAppUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAppUser))
            )
            .andExpect(status().isOk());

        // Validate the AppUser in the database
        List<AppUser> appUserList = appUserRepository.findAll();
        assertThat(appUserList).hasSize(databaseSizeBeforeUpdate);
        AppUser testAppUser = appUserList.get(appUserList.size() - 1);
        assertThat(testAppUser.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testAppUser.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
        assertThat(testAppUser.getDateModification()).isEqualTo(UPDATED_DATE_MODIFICATION);
        assertThat(testAppUser.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testAppUser.getEntite()).isEqualTo(UPDATED_ENTITE);
        assertThat(testAppUser.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testAppUser.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testAppUser.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testAppUser.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testAppUser.getUserName()).isEqualTo(UPDATED_USER_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingAppUser() throws Exception {
        int databaseSizeBeforeUpdate = appUserRepository.findAll().size();
        appUser.setId(count.incrementAndGet());

        // Create the AppUser
        AppUserDTO appUserDTO = appUserMapper.toDto(appUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, appUserDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(appUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppUser in the database
        List<AppUser> appUserList = appUserRepository.findAll();
        assertThat(appUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAppUser() throws Exception {
        int databaseSizeBeforeUpdate = appUserRepository.findAll().size();
        appUser.setId(count.incrementAndGet());

        // Create the AppUser
        AppUserDTO appUserDTO = appUserMapper.toDto(appUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(appUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppUser in the database
        List<AppUser> appUserList = appUserRepository.findAll();
        assertThat(appUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAppUser() throws Exception {
        int databaseSizeBeforeUpdate = appUserRepository.findAll().size();
        appUser.setId(count.incrementAndGet());

        // Create the AppUser
        AppUserDTO appUserDTO = appUserMapper.toDto(appUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppUserMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(appUserDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AppUser in the database
        List<AppUser> appUserList = appUserRepository.findAll();
        assertThat(appUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAppUser() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

        int databaseSizeBeforeDelete = appUserRepository.findAll().size();

        // Delete the appUser
        restAppUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, appUser.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AppUser> appUserList = appUserRepository.findAll();
        assertThat(appUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
