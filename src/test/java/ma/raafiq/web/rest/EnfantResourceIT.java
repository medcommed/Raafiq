package ma.raafiq.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import ma.raafiq.IntegrationTest;
import ma.raafiq.domain.Beneficiaire;
import ma.raafiq.domain.Enfant;
import ma.raafiq.domain.Famille;
import ma.raafiq.domain.Professionnel;
import ma.raafiq.repository.EnfantRepository;
import ma.raafiq.service.criteria.EnfantCriteria;
import ma.raafiq.service.dto.EnfantDTO;
import ma.raafiq.service.mapper.EnfantMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link EnfantResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EnfantResourceIT {

    private static final LocalDate DEFAULT_DATE_DIAGNOSTIC = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DIAGNOSTIC = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_DIAGNOSTIC = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_DATE_NAISSANCE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_NAISSANCE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_NAISSANCE = LocalDate.ofEpochDay(-1L);

    private static final Integer DEFAULT_DEGRE_AUTISME = 1;
    private static final Integer UPDATED_DEGRE_AUTISME = 2;
    private static final Integer SMALLER_DEGRE_AUTISME = 1 - 1;

    private static final Integer DEFAULT_MUTUALISTE = 1;
    private static final Integer UPDATED_MUTUALISTE = 2;
    private static final Integer SMALLER_MUTUALISTE = 1 - 1;

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_NOM_FR = "AAAAAAAAAA";
    private static final String UPDATED_NOM_FR = "BBBBBBBBBB";

    private static final String DEFAULT_NOM_MEDECIN = "AAAAAAAAAA";
    private static final String UPDATED_NOM_MEDECIN = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOMFR = "AAAAAAAAAA";
    private static final String UPDATED_PRENOMFR = "BBBBBBBBBB";

    private static final Integer DEFAULT_SCOLARISER = 1;
    private static final Integer UPDATED_SCOLARISER = 2;
    private static final Integer SMALLER_SCOLARISER = 1 - 1;

    private static final Integer DEFAULT_SEXE = 1;
    private static final Integer UPDATED_SEXE = 2;
    private static final Integer SMALLER_SEXE = 1 - 1;

    private static final String DEFAULT_SPECIALITE_MEDECIN = "AAAAAAAAAA";
    private static final String UPDATED_SPECIALITE_MEDECIN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/enfants";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EnfantRepository enfantRepository;

    @Autowired
    private EnfantMapper enfantMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEnfantMockMvc;

    private Enfant enfant;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Enfant createEntity(EntityManager em) {
        Enfant enfant = new Enfant()
            .dateDiagnostic(DEFAULT_DATE_DIAGNOSTIC)
            .dateNaissance(DEFAULT_DATE_NAISSANCE)
            .degreAutisme(DEFAULT_DEGRE_AUTISME)
            .mutualiste(DEFAULT_MUTUALISTE)
            .nom(DEFAULT_NOM)
            .nomFr(DEFAULT_NOM_FR)
            .nomMedecin(DEFAULT_NOM_MEDECIN)
            .prenom(DEFAULT_PRENOM)
            .prenomfr(DEFAULT_PRENOMFR)
            .scolariser(DEFAULT_SCOLARISER)
            .sexe(DEFAULT_SEXE)
            .specialiteMedecin(DEFAULT_SPECIALITE_MEDECIN);
        return enfant;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Enfant createUpdatedEntity(EntityManager em) {
        Enfant enfant = new Enfant()
            .dateDiagnostic(UPDATED_DATE_DIAGNOSTIC)
            .dateNaissance(UPDATED_DATE_NAISSANCE)
            .degreAutisme(UPDATED_DEGRE_AUTISME)
            .mutualiste(UPDATED_MUTUALISTE)
            .nom(UPDATED_NOM)
            .nomFr(UPDATED_NOM_FR)
            .nomMedecin(UPDATED_NOM_MEDECIN)
            .prenom(UPDATED_PRENOM)
            .prenomfr(UPDATED_PRENOMFR)
            .scolariser(UPDATED_SCOLARISER)
            .sexe(UPDATED_SEXE)
            .specialiteMedecin(UPDATED_SPECIALITE_MEDECIN);
        return enfant;
    }

    @BeforeEach
    public void initTest() {
        enfant = createEntity(em);
    }

    @Test
    @Transactional
    void createEnfant() throws Exception {
        int databaseSizeBeforeCreate = enfantRepository.findAll().size();
        // Create the Enfant
        EnfantDTO enfantDTO = enfantMapper.toDto(enfant);
        restEnfantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(enfantDTO)))
            .andExpect(status().isCreated());

        // Validate the Enfant in the database
        List<Enfant> enfantList = enfantRepository.findAll();
        assertThat(enfantList).hasSize(databaseSizeBeforeCreate + 1);
        Enfant testEnfant = enfantList.get(enfantList.size() - 1);
        assertThat(testEnfant.getDateDiagnostic()).isEqualTo(DEFAULT_DATE_DIAGNOSTIC);
        assertThat(testEnfant.getDateNaissance()).isEqualTo(DEFAULT_DATE_NAISSANCE);
        assertThat(testEnfant.getDegreAutisme()).isEqualTo(DEFAULT_DEGRE_AUTISME);
        assertThat(testEnfant.getMutualiste()).isEqualTo(DEFAULT_MUTUALISTE);
        assertThat(testEnfant.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testEnfant.getNomFr()).isEqualTo(DEFAULT_NOM_FR);
        assertThat(testEnfant.getNomMedecin()).isEqualTo(DEFAULT_NOM_MEDECIN);
        assertThat(testEnfant.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testEnfant.getPrenomfr()).isEqualTo(DEFAULT_PRENOMFR);
        assertThat(testEnfant.getScolariser()).isEqualTo(DEFAULT_SCOLARISER);
        assertThat(testEnfant.getSexe()).isEqualTo(DEFAULT_SEXE);
        assertThat(testEnfant.getSpecialiteMedecin()).isEqualTo(DEFAULT_SPECIALITE_MEDECIN);
    }

    @Test
    @Transactional
    void createEnfantWithExistingId() throws Exception {
        // Create the Enfant with an existing ID
        enfant.setId(1L);
        EnfantDTO enfantDTO = enfantMapper.toDto(enfant);

        int databaseSizeBeforeCreate = enfantRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEnfantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(enfantDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Enfant in the database
        List<Enfant> enfantList = enfantRepository.findAll();
        assertThat(enfantList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEnfants() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList
        restEnfantMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(enfant.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateDiagnostic").value(hasItem(DEFAULT_DATE_DIAGNOSTIC.toString())))
            .andExpect(jsonPath("$.[*].dateNaissance").value(hasItem(DEFAULT_DATE_NAISSANCE.toString())))
            .andExpect(jsonPath("$.[*].degreAutisme").value(hasItem(DEFAULT_DEGRE_AUTISME)))
            .andExpect(jsonPath("$.[*].mutualiste").value(hasItem(DEFAULT_MUTUALISTE)))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].nomFr").value(hasItem(DEFAULT_NOM_FR)))
            .andExpect(jsonPath("$.[*].nomMedecin").value(hasItem(DEFAULT_NOM_MEDECIN)))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].prenomfr").value(hasItem(DEFAULT_PRENOMFR)))
            .andExpect(jsonPath("$.[*].scolariser").value(hasItem(DEFAULT_SCOLARISER)))
            .andExpect(jsonPath("$.[*].sexe").value(hasItem(DEFAULT_SEXE)))
            .andExpect(jsonPath("$.[*].specialiteMedecin").value(hasItem(DEFAULT_SPECIALITE_MEDECIN)));
    }

    @Test
    @Transactional
    void getEnfant() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get the enfant
        restEnfantMockMvc
            .perform(get(ENTITY_API_URL_ID, enfant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(enfant.getId().intValue()))
            .andExpect(jsonPath("$.dateDiagnostic").value(DEFAULT_DATE_DIAGNOSTIC.toString()))
            .andExpect(jsonPath("$.dateNaissance").value(DEFAULT_DATE_NAISSANCE.toString()))
            .andExpect(jsonPath("$.degreAutisme").value(DEFAULT_DEGRE_AUTISME))
            .andExpect(jsonPath("$.mutualiste").value(DEFAULT_MUTUALISTE))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.nomFr").value(DEFAULT_NOM_FR))
            .andExpect(jsonPath("$.nomMedecin").value(DEFAULT_NOM_MEDECIN))
            .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM))
            .andExpect(jsonPath("$.prenomfr").value(DEFAULT_PRENOMFR))
            .andExpect(jsonPath("$.scolariser").value(DEFAULT_SCOLARISER))
            .andExpect(jsonPath("$.sexe").value(DEFAULT_SEXE))
            .andExpect(jsonPath("$.specialiteMedecin").value(DEFAULT_SPECIALITE_MEDECIN));
    }

    @Test
    @Transactional
    void getEnfantsByIdFiltering() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        Long id = enfant.getId();

        defaultEnfantShouldBeFound("id.equals=" + id);
        defaultEnfantShouldNotBeFound("id.notEquals=" + id);

        defaultEnfantShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEnfantShouldNotBeFound("id.greaterThan=" + id);

        defaultEnfantShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEnfantShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEnfantsByDateDiagnosticIsEqualToSomething() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList where dateDiagnostic equals to DEFAULT_DATE_DIAGNOSTIC
        defaultEnfantShouldBeFound("dateDiagnostic.equals=" + DEFAULT_DATE_DIAGNOSTIC);

        // Get all the enfantList where dateDiagnostic equals to UPDATED_DATE_DIAGNOSTIC
        defaultEnfantShouldNotBeFound("dateDiagnostic.equals=" + UPDATED_DATE_DIAGNOSTIC);
    }

    @Test
    @Transactional
    void getAllEnfantsByDateDiagnosticIsNotEqualToSomething() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList where dateDiagnostic not equals to DEFAULT_DATE_DIAGNOSTIC
        defaultEnfantShouldNotBeFound("dateDiagnostic.notEquals=" + DEFAULT_DATE_DIAGNOSTIC);

        // Get all the enfantList where dateDiagnostic not equals to UPDATED_DATE_DIAGNOSTIC
        defaultEnfantShouldBeFound("dateDiagnostic.notEquals=" + UPDATED_DATE_DIAGNOSTIC);
    }

    @Test
    @Transactional
    void getAllEnfantsByDateDiagnosticIsInShouldWork() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList where dateDiagnostic in DEFAULT_DATE_DIAGNOSTIC or UPDATED_DATE_DIAGNOSTIC
        defaultEnfantShouldBeFound("dateDiagnostic.in=" + DEFAULT_DATE_DIAGNOSTIC + "," + UPDATED_DATE_DIAGNOSTIC);

        // Get all the enfantList where dateDiagnostic equals to UPDATED_DATE_DIAGNOSTIC
        defaultEnfantShouldNotBeFound("dateDiagnostic.in=" + UPDATED_DATE_DIAGNOSTIC);
    }

    @Test
    @Transactional
    void getAllEnfantsByDateDiagnosticIsNullOrNotNull() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList where dateDiagnostic is not null
        defaultEnfantShouldBeFound("dateDiagnostic.specified=true");

        // Get all the enfantList where dateDiagnostic is null
        defaultEnfantShouldNotBeFound("dateDiagnostic.specified=false");
    }

    @Test
    @Transactional
    void getAllEnfantsByDateDiagnosticIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList where dateDiagnostic is greater than or equal to DEFAULT_DATE_DIAGNOSTIC
        defaultEnfantShouldBeFound("dateDiagnostic.greaterThanOrEqual=" + DEFAULT_DATE_DIAGNOSTIC);

        // Get all the enfantList where dateDiagnostic is greater than or equal to UPDATED_DATE_DIAGNOSTIC
        defaultEnfantShouldNotBeFound("dateDiagnostic.greaterThanOrEqual=" + UPDATED_DATE_DIAGNOSTIC);
    }

    @Test
    @Transactional
    void getAllEnfantsByDateDiagnosticIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList where dateDiagnostic is less than or equal to DEFAULT_DATE_DIAGNOSTIC
        defaultEnfantShouldBeFound("dateDiagnostic.lessThanOrEqual=" + DEFAULT_DATE_DIAGNOSTIC);

        // Get all the enfantList where dateDiagnostic is less than or equal to SMALLER_DATE_DIAGNOSTIC
        defaultEnfantShouldNotBeFound("dateDiagnostic.lessThanOrEqual=" + SMALLER_DATE_DIAGNOSTIC);
    }

    @Test
    @Transactional
    void getAllEnfantsByDateDiagnosticIsLessThanSomething() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList where dateDiagnostic is less than DEFAULT_DATE_DIAGNOSTIC
        defaultEnfantShouldNotBeFound("dateDiagnostic.lessThan=" + DEFAULT_DATE_DIAGNOSTIC);

        // Get all the enfantList where dateDiagnostic is less than UPDATED_DATE_DIAGNOSTIC
        defaultEnfantShouldBeFound("dateDiagnostic.lessThan=" + UPDATED_DATE_DIAGNOSTIC);
    }

    @Test
    @Transactional
    void getAllEnfantsByDateDiagnosticIsGreaterThanSomething() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList where dateDiagnostic is greater than DEFAULT_DATE_DIAGNOSTIC
        defaultEnfantShouldNotBeFound("dateDiagnostic.greaterThan=" + DEFAULT_DATE_DIAGNOSTIC);

        // Get all the enfantList where dateDiagnostic is greater than SMALLER_DATE_DIAGNOSTIC
        defaultEnfantShouldBeFound("dateDiagnostic.greaterThan=" + SMALLER_DATE_DIAGNOSTIC);
    }

    @Test
    @Transactional
    void getAllEnfantsByDateNaissanceIsEqualToSomething() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList where dateNaissance equals to DEFAULT_DATE_NAISSANCE
        defaultEnfantShouldBeFound("dateNaissance.equals=" + DEFAULT_DATE_NAISSANCE);

        // Get all the enfantList where dateNaissance equals to UPDATED_DATE_NAISSANCE
        defaultEnfantShouldNotBeFound("dateNaissance.equals=" + UPDATED_DATE_NAISSANCE);
    }

    @Test
    @Transactional
    void getAllEnfantsByDateNaissanceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList where dateNaissance not equals to DEFAULT_DATE_NAISSANCE
        defaultEnfantShouldNotBeFound("dateNaissance.notEquals=" + DEFAULT_DATE_NAISSANCE);

        // Get all the enfantList where dateNaissance not equals to UPDATED_DATE_NAISSANCE
        defaultEnfantShouldBeFound("dateNaissance.notEquals=" + UPDATED_DATE_NAISSANCE);
    }

    @Test
    @Transactional
    void getAllEnfantsByDateNaissanceIsInShouldWork() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList where dateNaissance in DEFAULT_DATE_NAISSANCE or UPDATED_DATE_NAISSANCE
        defaultEnfantShouldBeFound("dateNaissance.in=" + DEFAULT_DATE_NAISSANCE + "," + UPDATED_DATE_NAISSANCE);

        // Get all the enfantList where dateNaissance equals to UPDATED_DATE_NAISSANCE
        defaultEnfantShouldNotBeFound("dateNaissance.in=" + UPDATED_DATE_NAISSANCE);
    }

    @Test
    @Transactional
    void getAllEnfantsByDateNaissanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList where dateNaissance is not null
        defaultEnfantShouldBeFound("dateNaissance.specified=true");

        // Get all the enfantList where dateNaissance is null
        defaultEnfantShouldNotBeFound("dateNaissance.specified=false");
    }

    @Test
    @Transactional
    void getAllEnfantsByDateNaissanceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList where dateNaissance is greater than or equal to DEFAULT_DATE_NAISSANCE
        defaultEnfantShouldBeFound("dateNaissance.greaterThanOrEqual=" + DEFAULT_DATE_NAISSANCE);

        // Get all the enfantList where dateNaissance is greater than or equal to UPDATED_DATE_NAISSANCE
        defaultEnfantShouldNotBeFound("dateNaissance.greaterThanOrEqual=" + UPDATED_DATE_NAISSANCE);
    }

    @Test
    @Transactional
    void getAllEnfantsByDateNaissanceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList where dateNaissance is less than or equal to DEFAULT_DATE_NAISSANCE
        defaultEnfantShouldBeFound("dateNaissance.lessThanOrEqual=" + DEFAULT_DATE_NAISSANCE);

        // Get all the enfantList where dateNaissance is less than or equal to SMALLER_DATE_NAISSANCE
        defaultEnfantShouldNotBeFound("dateNaissance.lessThanOrEqual=" + SMALLER_DATE_NAISSANCE);
    }

    @Test
    @Transactional
    void getAllEnfantsByDateNaissanceIsLessThanSomething() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList where dateNaissance is less than DEFAULT_DATE_NAISSANCE
        defaultEnfantShouldNotBeFound("dateNaissance.lessThan=" + DEFAULT_DATE_NAISSANCE);

        // Get all the enfantList where dateNaissance is less than UPDATED_DATE_NAISSANCE
        defaultEnfantShouldBeFound("dateNaissance.lessThan=" + UPDATED_DATE_NAISSANCE);
    }

    @Test
    @Transactional
    void getAllEnfantsByDateNaissanceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList where dateNaissance is greater than DEFAULT_DATE_NAISSANCE
        defaultEnfantShouldNotBeFound("dateNaissance.greaterThan=" + DEFAULT_DATE_NAISSANCE);

        // Get all the enfantList where dateNaissance is greater than SMALLER_DATE_NAISSANCE
        defaultEnfantShouldBeFound("dateNaissance.greaterThan=" + SMALLER_DATE_NAISSANCE);
    }

    @Test
    @Transactional
    void getAllEnfantsByDegreAutismeIsEqualToSomething() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList where degreAutisme equals to DEFAULT_DEGRE_AUTISME
        defaultEnfantShouldBeFound("degreAutisme.equals=" + DEFAULT_DEGRE_AUTISME);

        // Get all the enfantList where degreAutisme equals to UPDATED_DEGRE_AUTISME
        defaultEnfantShouldNotBeFound("degreAutisme.equals=" + UPDATED_DEGRE_AUTISME);
    }

    @Test
    @Transactional
    void getAllEnfantsByDegreAutismeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList where degreAutisme not equals to DEFAULT_DEGRE_AUTISME
        defaultEnfantShouldNotBeFound("degreAutisme.notEquals=" + DEFAULT_DEGRE_AUTISME);

        // Get all the enfantList where degreAutisme not equals to UPDATED_DEGRE_AUTISME
        defaultEnfantShouldBeFound("degreAutisme.notEquals=" + UPDATED_DEGRE_AUTISME);
    }

    @Test
    @Transactional
    void getAllEnfantsByDegreAutismeIsInShouldWork() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList where degreAutisme in DEFAULT_DEGRE_AUTISME or UPDATED_DEGRE_AUTISME
        defaultEnfantShouldBeFound("degreAutisme.in=" + DEFAULT_DEGRE_AUTISME + "," + UPDATED_DEGRE_AUTISME);

        // Get all the enfantList where degreAutisme equals to UPDATED_DEGRE_AUTISME
        defaultEnfantShouldNotBeFound("degreAutisme.in=" + UPDATED_DEGRE_AUTISME);
    }

    @Test
    @Transactional
    void getAllEnfantsByDegreAutismeIsNullOrNotNull() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList where degreAutisme is not null
        defaultEnfantShouldBeFound("degreAutisme.specified=true");

        // Get all the enfantList where degreAutisme is null
        defaultEnfantShouldNotBeFound("degreAutisme.specified=false");
    }

    @Test
    @Transactional
    void getAllEnfantsByDegreAutismeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList where degreAutisme is greater than or equal to DEFAULT_DEGRE_AUTISME
        defaultEnfantShouldBeFound("degreAutisme.greaterThanOrEqual=" + DEFAULT_DEGRE_AUTISME);

        // Get all the enfantList where degreAutisme is greater than or equal to UPDATED_DEGRE_AUTISME
        defaultEnfantShouldNotBeFound("degreAutisme.greaterThanOrEqual=" + UPDATED_DEGRE_AUTISME);
    }

    @Test
    @Transactional
    void getAllEnfantsByDegreAutismeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList where degreAutisme is less than or equal to DEFAULT_DEGRE_AUTISME
        defaultEnfantShouldBeFound("degreAutisme.lessThanOrEqual=" + DEFAULT_DEGRE_AUTISME);

        // Get all the enfantList where degreAutisme is less than or equal to SMALLER_DEGRE_AUTISME
        defaultEnfantShouldNotBeFound("degreAutisme.lessThanOrEqual=" + SMALLER_DEGRE_AUTISME);
    }

    @Test
    @Transactional
    void getAllEnfantsByDegreAutismeIsLessThanSomething() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList where degreAutisme is less than DEFAULT_DEGRE_AUTISME
        defaultEnfantShouldNotBeFound("degreAutisme.lessThan=" + DEFAULT_DEGRE_AUTISME);

        // Get all the enfantList where degreAutisme is less than UPDATED_DEGRE_AUTISME
        defaultEnfantShouldBeFound("degreAutisme.lessThan=" + UPDATED_DEGRE_AUTISME);
    }

    @Test
    @Transactional
    void getAllEnfantsByDegreAutismeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList where degreAutisme is greater than DEFAULT_DEGRE_AUTISME
        defaultEnfantShouldNotBeFound("degreAutisme.greaterThan=" + DEFAULT_DEGRE_AUTISME);

        // Get all the enfantList where degreAutisme is greater than SMALLER_DEGRE_AUTISME
        defaultEnfantShouldBeFound("degreAutisme.greaterThan=" + SMALLER_DEGRE_AUTISME);
    }

    @Test
    @Transactional
    void getAllEnfantsByMutualisteIsEqualToSomething() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList where mutualiste equals to DEFAULT_MUTUALISTE
        defaultEnfantShouldBeFound("mutualiste.equals=" + DEFAULT_MUTUALISTE);

        // Get all the enfantList where mutualiste equals to UPDATED_MUTUALISTE
        defaultEnfantShouldNotBeFound("mutualiste.equals=" + UPDATED_MUTUALISTE);
    }

    @Test
    @Transactional
    void getAllEnfantsByMutualisteIsNotEqualToSomething() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList where mutualiste not equals to DEFAULT_MUTUALISTE
        defaultEnfantShouldNotBeFound("mutualiste.notEquals=" + DEFAULT_MUTUALISTE);

        // Get all the enfantList where mutualiste not equals to UPDATED_MUTUALISTE
        defaultEnfantShouldBeFound("mutualiste.notEquals=" + UPDATED_MUTUALISTE);
    }

    @Test
    @Transactional
    void getAllEnfantsByMutualisteIsInShouldWork() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList where mutualiste in DEFAULT_MUTUALISTE or UPDATED_MUTUALISTE
        defaultEnfantShouldBeFound("mutualiste.in=" + DEFAULT_MUTUALISTE + "," + UPDATED_MUTUALISTE);

        // Get all the enfantList where mutualiste equals to UPDATED_MUTUALISTE
        defaultEnfantShouldNotBeFound("mutualiste.in=" + UPDATED_MUTUALISTE);
    }

    @Test
    @Transactional
    void getAllEnfantsByMutualisteIsNullOrNotNull() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList where mutualiste is not null
        defaultEnfantShouldBeFound("mutualiste.specified=true");

        // Get all the enfantList where mutualiste is null
        defaultEnfantShouldNotBeFound("mutualiste.specified=false");
    }

    @Test
    @Transactional
    void getAllEnfantsByMutualisteIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList where mutualiste is greater than or equal to DEFAULT_MUTUALISTE
        defaultEnfantShouldBeFound("mutualiste.greaterThanOrEqual=" + DEFAULT_MUTUALISTE);

        // Get all the enfantList where mutualiste is greater than or equal to UPDATED_MUTUALISTE
        defaultEnfantShouldNotBeFound("mutualiste.greaterThanOrEqual=" + UPDATED_MUTUALISTE);
    }

    @Test
    @Transactional
    void getAllEnfantsByMutualisteIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList where mutualiste is less than or equal to DEFAULT_MUTUALISTE
        defaultEnfantShouldBeFound("mutualiste.lessThanOrEqual=" + DEFAULT_MUTUALISTE);

        // Get all the enfantList where mutualiste is less than or equal to SMALLER_MUTUALISTE
        defaultEnfantShouldNotBeFound("mutualiste.lessThanOrEqual=" + SMALLER_MUTUALISTE);
    }

    @Test
    @Transactional
    void getAllEnfantsByMutualisteIsLessThanSomething() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList where mutualiste is less than DEFAULT_MUTUALISTE
        defaultEnfantShouldNotBeFound("mutualiste.lessThan=" + DEFAULT_MUTUALISTE);

        // Get all the enfantList where mutualiste is less than UPDATED_MUTUALISTE
        defaultEnfantShouldBeFound("mutualiste.lessThan=" + UPDATED_MUTUALISTE);
    }

    @Test
    @Transactional
    void getAllEnfantsByMutualisteIsGreaterThanSomething() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList where mutualiste is greater than DEFAULT_MUTUALISTE
        defaultEnfantShouldNotBeFound("mutualiste.greaterThan=" + DEFAULT_MUTUALISTE);

        // Get all the enfantList where mutualiste is greater than SMALLER_MUTUALISTE
        defaultEnfantShouldBeFound("mutualiste.greaterThan=" + SMALLER_MUTUALISTE);
    }

    @Test
    @Transactional
    void getAllEnfantsByNomIsEqualToSomething() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList where nom equals to DEFAULT_NOM
        defaultEnfantShouldBeFound("nom.equals=" + DEFAULT_NOM);

        // Get all the enfantList where nom equals to UPDATED_NOM
        defaultEnfantShouldNotBeFound("nom.equals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllEnfantsByNomIsNotEqualToSomething() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList where nom not equals to DEFAULT_NOM
        defaultEnfantShouldNotBeFound("nom.notEquals=" + DEFAULT_NOM);

        // Get all the enfantList where nom not equals to UPDATED_NOM
        defaultEnfantShouldBeFound("nom.notEquals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllEnfantsByNomIsInShouldWork() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList where nom in DEFAULT_NOM or UPDATED_NOM
        defaultEnfantShouldBeFound("nom.in=" + DEFAULT_NOM + "," + UPDATED_NOM);

        // Get all the enfantList where nom equals to UPDATED_NOM
        defaultEnfantShouldNotBeFound("nom.in=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllEnfantsByNomIsNullOrNotNull() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList where nom is not null
        defaultEnfantShouldBeFound("nom.specified=true");

        // Get all the enfantList where nom is null
        defaultEnfantShouldNotBeFound("nom.specified=false");
    }

    @Test
    @Transactional
    void getAllEnfantsByNomContainsSomething() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList where nom contains DEFAULT_NOM
        defaultEnfantShouldBeFound("nom.contains=" + DEFAULT_NOM);

        // Get all the enfantList where nom contains UPDATED_NOM
        defaultEnfantShouldNotBeFound("nom.contains=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllEnfantsByNomNotContainsSomething() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList where nom does not contain DEFAULT_NOM
        defaultEnfantShouldNotBeFound("nom.doesNotContain=" + DEFAULT_NOM);

        // Get all the enfantList where nom does not contain UPDATED_NOM
        defaultEnfantShouldBeFound("nom.doesNotContain=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllEnfantsByNomFrIsEqualToSomething() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList where nomFr equals to DEFAULT_NOM_FR
        defaultEnfantShouldBeFound("nomFr.equals=" + DEFAULT_NOM_FR);

        // Get all the enfantList where nomFr equals to UPDATED_NOM_FR
        defaultEnfantShouldNotBeFound("nomFr.equals=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllEnfantsByNomFrIsNotEqualToSomething() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList where nomFr not equals to DEFAULT_NOM_FR
        defaultEnfantShouldNotBeFound("nomFr.notEquals=" + DEFAULT_NOM_FR);

        // Get all the enfantList where nomFr not equals to UPDATED_NOM_FR
        defaultEnfantShouldBeFound("nomFr.notEquals=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllEnfantsByNomFrIsInShouldWork() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList where nomFr in DEFAULT_NOM_FR or UPDATED_NOM_FR
        defaultEnfantShouldBeFound("nomFr.in=" + DEFAULT_NOM_FR + "," + UPDATED_NOM_FR);

        // Get all the enfantList where nomFr equals to UPDATED_NOM_FR
        defaultEnfantShouldNotBeFound("nomFr.in=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllEnfantsByNomFrIsNullOrNotNull() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList where nomFr is not null
        defaultEnfantShouldBeFound("nomFr.specified=true");

        // Get all the enfantList where nomFr is null
        defaultEnfantShouldNotBeFound("nomFr.specified=false");
    }

    @Test
    @Transactional
    void getAllEnfantsByNomFrContainsSomething() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList where nomFr contains DEFAULT_NOM_FR
        defaultEnfantShouldBeFound("nomFr.contains=" + DEFAULT_NOM_FR);

        // Get all the enfantList where nomFr contains UPDATED_NOM_FR
        defaultEnfantShouldNotBeFound("nomFr.contains=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllEnfantsByNomFrNotContainsSomething() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList where nomFr does not contain DEFAULT_NOM_FR
        defaultEnfantShouldNotBeFound("nomFr.doesNotContain=" + DEFAULT_NOM_FR);

        // Get all the enfantList where nomFr does not contain UPDATED_NOM_FR
        defaultEnfantShouldBeFound("nomFr.doesNotContain=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllEnfantsByNomMedecinIsEqualToSomething() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList where nomMedecin equals to DEFAULT_NOM_MEDECIN
        defaultEnfantShouldBeFound("nomMedecin.equals=" + DEFAULT_NOM_MEDECIN);

        // Get all the enfantList where nomMedecin equals to UPDATED_NOM_MEDECIN
        defaultEnfantShouldNotBeFound("nomMedecin.equals=" + UPDATED_NOM_MEDECIN);
    }

    @Test
    @Transactional
    void getAllEnfantsByNomMedecinIsNotEqualToSomething() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList where nomMedecin not equals to DEFAULT_NOM_MEDECIN
        defaultEnfantShouldNotBeFound("nomMedecin.notEquals=" + DEFAULT_NOM_MEDECIN);

        // Get all the enfantList where nomMedecin not equals to UPDATED_NOM_MEDECIN
        defaultEnfantShouldBeFound("nomMedecin.notEquals=" + UPDATED_NOM_MEDECIN);
    }

    @Test
    @Transactional
    void getAllEnfantsByNomMedecinIsInShouldWork() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList where nomMedecin in DEFAULT_NOM_MEDECIN or UPDATED_NOM_MEDECIN
        defaultEnfantShouldBeFound("nomMedecin.in=" + DEFAULT_NOM_MEDECIN + "," + UPDATED_NOM_MEDECIN);

        // Get all the enfantList where nomMedecin equals to UPDATED_NOM_MEDECIN
        defaultEnfantShouldNotBeFound("nomMedecin.in=" + UPDATED_NOM_MEDECIN);
    }

    @Test
    @Transactional
    void getAllEnfantsByNomMedecinIsNullOrNotNull() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList where nomMedecin is not null
        defaultEnfantShouldBeFound("nomMedecin.specified=true");

        // Get all the enfantList where nomMedecin is null
        defaultEnfantShouldNotBeFound("nomMedecin.specified=false");
    }

    @Test
    @Transactional
    void getAllEnfantsByNomMedecinContainsSomething() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList where nomMedecin contains DEFAULT_NOM_MEDECIN
        defaultEnfantShouldBeFound("nomMedecin.contains=" + DEFAULT_NOM_MEDECIN);

        // Get all the enfantList where nomMedecin contains UPDATED_NOM_MEDECIN
        defaultEnfantShouldNotBeFound("nomMedecin.contains=" + UPDATED_NOM_MEDECIN);
    }

    @Test
    @Transactional
    void getAllEnfantsByNomMedecinNotContainsSomething() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList where nomMedecin does not contain DEFAULT_NOM_MEDECIN
        defaultEnfantShouldNotBeFound("nomMedecin.doesNotContain=" + DEFAULT_NOM_MEDECIN);

        // Get all the enfantList where nomMedecin does not contain UPDATED_NOM_MEDECIN
        defaultEnfantShouldBeFound("nomMedecin.doesNotContain=" + UPDATED_NOM_MEDECIN);
    }

    @Test
    @Transactional
    void getAllEnfantsByPrenomIsEqualToSomething() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList where prenom equals to DEFAULT_PRENOM
        defaultEnfantShouldBeFound("prenom.equals=" + DEFAULT_PRENOM);

        // Get all the enfantList where prenom equals to UPDATED_PRENOM
        defaultEnfantShouldNotBeFound("prenom.equals=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    void getAllEnfantsByPrenomIsNotEqualToSomething() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList where prenom not equals to DEFAULT_PRENOM
        defaultEnfantShouldNotBeFound("prenom.notEquals=" + DEFAULT_PRENOM);

        // Get all the enfantList where prenom not equals to UPDATED_PRENOM
        defaultEnfantShouldBeFound("prenom.notEquals=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    void getAllEnfantsByPrenomIsInShouldWork() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList where prenom in DEFAULT_PRENOM or UPDATED_PRENOM
        defaultEnfantShouldBeFound("prenom.in=" + DEFAULT_PRENOM + "," + UPDATED_PRENOM);

        // Get all the enfantList where prenom equals to UPDATED_PRENOM
        defaultEnfantShouldNotBeFound("prenom.in=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    void getAllEnfantsByPrenomIsNullOrNotNull() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList where prenom is not null
        defaultEnfantShouldBeFound("prenom.specified=true");

        // Get all the enfantList where prenom is null
        defaultEnfantShouldNotBeFound("prenom.specified=false");
    }

    @Test
    @Transactional
    void getAllEnfantsByPrenomContainsSomething() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList where prenom contains DEFAULT_PRENOM
        defaultEnfantShouldBeFound("prenom.contains=" + DEFAULT_PRENOM);

        // Get all the enfantList where prenom contains UPDATED_PRENOM
        defaultEnfantShouldNotBeFound("prenom.contains=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    void getAllEnfantsByPrenomNotContainsSomething() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList where prenom does not contain DEFAULT_PRENOM
        defaultEnfantShouldNotBeFound("prenom.doesNotContain=" + DEFAULT_PRENOM);

        // Get all the enfantList where prenom does not contain UPDATED_PRENOM
        defaultEnfantShouldBeFound("prenom.doesNotContain=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    void getAllEnfantsByPrenomfrIsEqualToSomething() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList where prenomfr equals to DEFAULT_PRENOMFR
        defaultEnfantShouldBeFound("prenomfr.equals=" + DEFAULT_PRENOMFR);

        // Get all the enfantList where prenomfr equals to UPDATED_PRENOMFR
        defaultEnfantShouldNotBeFound("prenomfr.equals=" + UPDATED_PRENOMFR);
    }

    @Test
    @Transactional
    void getAllEnfantsByPrenomfrIsNotEqualToSomething() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList where prenomfr not equals to DEFAULT_PRENOMFR
        defaultEnfantShouldNotBeFound("prenomfr.notEquals=" + DEFAULT_PRENOMFR);

        // Get all the enfantList where prenomfr not equals to UPDATED_PRENOMFR
        defaultEnfantShouldBeFound("prenomfr.notEquals=" + UPDATED_PRENOMFR);
    }

    @Test
    @Transactional
    void getAllEnfantsByPrenomfrIsInShouldWork() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList where prenomfr in DEFAULT_PRENOMFR or UPDATED_PRENOMFR
        defaultEnfantShouldBeFound("prenomfr.in=" + DEFAULT_PRENOMFR + "," + UPDATED_PRENOMFR);

        // Get all the enfantList where prenomfr equals to UPDATED_PRENOMFR
        defaultEnfantShouldNotBeFound("prenomfr.in=" + UPDATED_PRENOMFR);
    }

    @Test
    @Transactional
    void getAllEnfantsByPrenomfrIsNullOrNotNull() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList where prenomfr is not null
        defaultEnfantShouldBeFound("prenomfr.specified=true");

        // Get all the enfantList where prenomfr is null
        defaultEnfantShouldNotBeFound("prenomfr.specified=false");
    }

    @Test
    @Transactional
    void getAllEnfantsByPrenomfrContainsSomething() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList where prenomfr contains DEFAULT_PRENOMFR
        defaultEnfantShouldBeFound("prenomfr.contains=" + DEFAULT_PRENOMFR);

        // Get all the enfantList where prenomfr contains UPDATED_PRENOMFR
        defaultEnfantShouldNotBeFound("prenomfr.contains=" + UPDATED_PRENOMFR);
    }

    @Test
    @Transactional
    void getAllEnfantsByPrenomfrNotContainsSomething() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList where prenomfr does not contain DEFAULT_PRENOMFR
        defaultEnfantShouldNotBeFound("prenomfr.doesNotContain=" + DEFAULT_PRENOMFR);

        // Get all the enfantList where prenomfr does not contain UPDATED_PRENOMFR
        defaultEnfantShouldBeFound("prenomfr.doesNotContain=" + UPDATED_PRENOMFR);
    }

    @Test
    @Transactional
    void getAllEnfantsByScolariserIsEqualToSomething() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList where scolariser equals to DEFAULT_SCOLARISER
        defaultEnfantShouldBeFound("scolariser.equals=" + DEFAULT_SCOLARISER);

        // Get all the enfantList where scolariser equals to UPDATED_SCOLARISER
        defaultEnfantShouldNotBeFound("scolariser.equals=" + UPDATED_SCOLARISER);
    }

    @Test
    @Transactional
    void getAllEnfantsByScolariserIsNotEqualToSomething() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList where scolariser not equals to DEFAULT_SCOLARISER
        defaultEnfantShouldNotBeFound("scolariser.notEquals=" + DEFAULT_SCOLARISER);

        // Get all the enfantList where scolariser not equals to UPDATED_SCOLARISER
        defaultEnfantShouldBeFound("scolariser.notEquals=" + UPDATED_SCOLARISER);
    }

    @Test
    @Transactional
    void getAllEnfantsByScolariserIsInShouldWork() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList where scolariser in DEFAULT_SCOLARISER or UPDATED_SCOLARISER
        defaultEnfantShouldBeFound("scolariser.in=" + DEFAULT_SCOLARISER + "," + UPDATED_SCOLARISER);

        // Get all the enfantList where scolariser equals to UPDATED_SCOLARISER
        defaultEnfantShouldNotBeFound("scolariser.in=" + UPDATED_SCOLARISER);
    }

    @Test
    @Transactional
    void getAllEnfantsByScolariserIsNullOrNotNull() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList where scolariser is not null
        defaultEnfantShouldBeFound("scolariser.specified=true");

        // Get all the enfantList where scolariser is null
        defaultEnfantShouldNotBeFound("scolariser.specified=false");
    }

    @Test
    @Transactional
    void getAllEnfantsByScolariserIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList where scolariser is greater than or equal to DEFAULT_SCOLARISER
        defaultEnfantShouldBeFound("scolariser.greaterThanOrEqual=" + DEFAULT_SCOLARISER);

        // Get all the enfantList where scolariser is greater than or equal to UPDATED_SCOLARISER
        defaultEnfantShouldNotBeFound("scolariser.greaterThanOrEqual=" + UPDATED_SCOLARISER);
    }

    @Test
    @Transactional
    void getAllEnfantsByScolariserIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList where scolariser is less than or equal to DEFAULT_SCOLARISER
        defaultEnfantShouldBeFound("scolariser.lessThanOrEqual=" + DEFAULT_SCOLARISER);

        // Get all the enfantList where scolariser is less than or equal to SMALLER_SCOLARISER
        defaultEnfantShouldNotBeFound("scolariser.lessThanOrEqual=" + SMALLER_SCOLARISER);
    }

    @Test
    @Transactional
    void getAllEnfantsByScolariserIsLessThanSomething() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList where scolariser is less than DEFAULT_SCOLARISER
        defaultEnfantShouldNotBeFound("scolariser.lessThan=" + DEFAULT_SCOLARISER);

        // Get all the enfantList where scolariser is less than UPDATED_SCOLARISER
        defaultEnfantShouldBeFound("scolariser.lessThan=" + UPDATED_SCOLARISER);
    }

    @Test
    @Transactional
    void getAllEnfantsByScolariserIsGreaterThanSomething() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList where scolariser is greater than DEFAULT_SCOLARISER
        defaultEnfantShouldNotBeFound("scolariser.greaterThan=" + DEFAULT_SCOLARISER);

        // Get all the enfantList where scolariser is greater than SMALLER_SCOLARISER
        defaultEnfantShouldBeFound("scolariser.greaterThan=" + SMALLER_SCOLARISER);
    }

    @Test
    @Transactional
    void getAllEnfantsBySexeIsEqualToSomething() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList where sexe equals to DEFAULT_SEXE
        defaultEnfantShouldBeFound("sexe.equals=" + DEFAULT_SEXE);

        // Get all the enfantList where sexe equals to UPDATED_SEXE
        defaultEnfantShouldNotBeFound("sexe.equals=" + UPDATED_SEXE);
    }

    @Test
    @Transactional
    void getAllEnfantsBySexeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList where sexe not equals to DEFAULT_SEXE
        defaultEnfantShouldNotBeFound("sexe.notEquals=" + DEFAULT_SEXE);

        // Get all the enfantList where sexe not equals to UPDATED_SEXE
        defaultEnfantShouldBeFound("sexe.notEquals=" + UPDATED_SEXE);
    }

    @Test
    @Transactional
    void getAllEnfantsBySexeIsInShouldWork() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList where sexe in DEFAULT_SEXE or UPDATED_SEXE
        defaultEnfantShouldBeFound("sexe.in=" + DEFAULT_SEXE + "," + UPDATED_SEXE);

        // Get all the enfantList where sexe equals to UPDATED_SEXE
        defaultEnfantShouldNotBeFound("sexe.in=" + UPDATED_SEXE);
    }

    @Test
    @Transactional
    void getAllEnfantsBySexeIsNullOrNotNull() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList where sexe is not null
        defaultEnfantShouldBeFound("sexe.specified=true");

        // Get all the enfantList where sexe is null
        defaultEnfantShouldNotBeFound("sexe.specified=false");
    }

    @Test
    @Transactional
    void getAllEnfantsBySexeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList where sexe is greater than or equal to DEFAULT_SEXE
        defaultEnfantShouldBeFound("sexe.greaterThanOrEqual=" + DEFAULT_SEXE);

        // Get all the enfantList where sexe is greater than or equal to UPDATED_SEXE
        defaultEnfantShouldNotBeFound("sexe.greaterThanOrEqual=" + UPDATED_SEXE);
    }

    @Test
    @Transactional
    void getAllEnfantsBySexeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList where sexe is less than or equal to DEFAULT_SEXE
        defaultEnfantShouldBeFound("sexe.lessThanOrEqual=" + DEFAULT_SEXE);

        // Get all the enfantList where sexe is less than or equal to SMALLER_SEXE
        defaultEnfantShouldNotBeFound("sexe.lessThanOrEqual=" + SMALLER_SEXE);
    }

    @Test
    @Transactional
    void getAllEnfantsBySexeIsLessThanSomething() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList where sexe is less than DEFAULT_SEXE
        defaultEnfantShouldNotBeFound("sexe.lessThan=" + DEFAULT_SEXE);

        // Get all the enfantList where sexe is less than UPDATED_SEXE
        defaultEnfantShouldBeFound("sexe.lessThan=" + UPDATED_SEXE);
    }

    @Test
    @Transactional
    void getAllEnfantsBySexeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList where sexe is greater than DEFAULT_SEXE
        defaultEnfantShouldNotBeFound("sexe.greaterThan=" + DEFAULT_SEXE);

        // Get all the enfantList where sexe is greater than SMALLER_SEXE
        defaultEnfantShouldBeFound("sexe.greaterThan=" + SMALLER_SEXE);
    }

    @Test
    @Transactional
    void getAllEnfantsBySpecialiteMedecinIsEqualToSomething() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList where specialiteMedecin equals to DEFAULT_SPECIALITE_MEDECIN
        defaultEnfantShouldBeFound("specialiteMedecin.equals=" + DEFAULT_SPECIALITE_MEDECIN);

        // Get all the enfantList where specialiteMedecin equals to UPDATED_SPECIALITE_MEDECIN
        defaultEnfantShouldNotBeFound("specialiteMedecin.equals=" + UPDATED_SPECIALITE_MEDECIN);
    }

    @Test
    @Transactional
    void getAllEnfantsBySpecialiteMedecinIsNotEqualToSomething() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList where specialiteMedecin not equals to DEFAULT_SPECIALITE_MEDECIN
        defaultEnfantShouldNotBeFound("specialiteMedecin.notEquals=" + DEFAULT_SPECIALITE_MEDECIN);

        // Get all the enfantList where specialiteMedecin not equals to UPDATED_SPECIALITE_MEDECIN
        defaultEnfantShouldBeFound("specialiteMedecin.notEquals=" + UPDATED_SPECIALITE_MEDECIN);
    }

    @Test
    @Transactional
    void getAllEnfantsBySpecialiteMedecinIsInShouldWork() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList where specialiteMedecin in DEFAULT_SPECIALITE_MEDECIN or UPDATED_SPECIALITE_MEDECIN
        defaultEnfantShouldBeFound("specialiteMedecin.in=" + DEFAULT_SPECIALITE_MEDECIN + "," + UPDATED_SPECIALITE_MEDECIN);

        // Get all the enfantList where specialiteMedecin equals to UPDATED_SPECIALITE_MEDECIN
        defaultEnfantShouldNotBeFound("specialiteMedecin.in=" + UPDATED_SPECIALITE_MEDECIN);
    }

    @Test
    @Transactional
    void getAllEnfantsBySpecialiteMedecinIsNullOrNotNull() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList where specialiteMedecin is not null
        defaultEnfantShouldBeFound("specialiteMedecin.specified=true");

        // Get all the enfantList where specialiteMedecin is null
        defaultEnfantShouldNotBeFound("specialiteMedecin.specified=false");
    }

    @Test
    @Transactional
    void getAllEnfantsBySpecialiteMedecinContainsSomething() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList where specialiteMedecin contains DEFAULT_SPECIALITE_MEDECIN
        defaultEnfantShouldBeFound("specialiteMedecin.contains=" + DEFAULT_SPECIALITE_MEDECIN);

        // Get all the enfantList where specialiteMedecin contains UPDATED_SPECIALITE_MEDECIN
        defaultEnfantShouldNotBeFound("specialiteMedecin.contains=" + UPDATED_SPECIALITE_MEDECIN);
    }

    @Test
    @Transactional
    void getAllEnfantsBySpecialiteMedecinNotContainsSomething() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfantList where specialiteMedecin does not contain DEFAULT_SPECIALITE_MEDECIN
        defaultEnfantShouldNotBeFound("specialiteMedecin.doesNotContain=" + DEFAULT_SPECIALITE_MEDECIN);

        // Get all the enfantList where specialiteMedecin does not contain UPDATED_SPECIALITE_MEDECIN
        defaultEnfantShouldBeFound("specialiteMedecin.doesNotContain=" + UPDATED_SPECIALITE_MEDECIN);
    }

    @Test
    @Transactional
    void getAllEnfantsByBeneficiaireIsEqualToSomething() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);
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
        enfant.addBeneficiaire(beneficiaire);
        enfantRepository.saveAndFlush(enfant);
        Long beneficiaireId = beneficiaire.getId();

        // Get all the enfantList where beneficiaire equals to beneficiaireId
        defaultEnfantShouldBeFound("beneficiaireId.equals=" + beneficiaireId);

        // Get all the enfantList where beneficiaire equals to (beneficiaireId + 1)
        defaultEnfantShouldNotBeFound("beneficiaireId.equals=" + (beneficiaireId + 1));
    }

    @Test
    @Transactional
    void getAllEnfantsByFamilleIsEqualToSomething() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);
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
        enfant.addFamille(famille);
        enfantRepository.saveAndFlush(enfant);
        Long familleId = famille.getId();

        // Get all the enfantList where famille equals to familleId
        defaultEnfantShouldBeFound("familleId.equals=" + familleId);

        // Get all the enfantList where famille equals to (familleId + 1)
        defaultEnfantShouldNotBeFound("familleId.equals=" + (familleId + 1));
    }

    @Test
    @Transactional
    void getAllEnfantsByProfessionnelIsEqualToSomething() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);
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
        enfant.addProfessionnel(professionnel);
        enfantRepository.saveAndFlush(enfant);
        Long professionnelId = professionnel.getId();

        // Get all the enfantList where professionnel equals to professionnelId
        defaultEnfantShouldBeFound("professionnelId.equals=" + professionnelId);

        // Get all the enfantList where professionnel equals to (professionnelId + 1)
        defaultEnfantShouldNotBeFound("professionnelId.equals=" + (professionnelId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEnfantShouldBeFound(String filter) throws Exception {
        restEnfantMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(enfant.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateDiagnostic").value(hasItem(DEFAULT_DATE_DIAGNOSTIC.toString())))
            .andExpect(jsonPath("$.[*].dateNaissance").value(hasItem(DEFAULT_DATE_NAISSANCE.toString())))
            .andExpect(jsonPath("$.[*].degreAutisme").value(hasItem(DEFAULT_DEGRE_AUTISME)))
            .andExpect(jsonPath("$.[*].mutualiste").value(hasItem(DEFAULT_MUTUALISTE)))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].nomFr").value(hasItem(DEFAULT_NOM_FR)))
            .andExpect(jsonPath("$.[*].nomMedecin").value(hasItem(DEFAULT_NOM_MEDECIN)))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].prenomfr").value(hasItem(DEFAULT_PRENOMFR)))
            .andExpect(jsonPath("$.[*].scolariser").value(hasItem(DEFAULT_SCOLARISER)))
            .andExpect(jsonPath("$.[*].sexe").value(hasItem(DEFAULT_SEXE)))
            .andExpect(jsonPath("$.[*].specialiteMedecin").value(hasItem(DEFAULT_SPECIALITE_MEDECIN)));

        // Check, that the count call also returns 1
        restEnfantMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEnfantShouldNotBeFound(String filter) throws Exception {
        restEnfantMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEnfantMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEnfant() throws Exception {
        // Get the enfant
        restEnfantMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEnfant() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        int databaseSizeBeforeUpdate = enfantRepository.findAll().size();

        // Update the enfant
        Enfant updatedEnfant = enfantRepository.findById(enfant.getId()).get();
        // Disconnect from session so that the updates on updatedEnfant are not directly saved in db
        em.detach(updatedEnfant);
        updatedEnfant
            .dateDiagnostic(UPDATED_DATE_DIAGNOSTIC)
            .dateNaissance(UPDATED_DATE_NAISSANCE)
            .degreAutisme(UPDATED_DEGRE_AUTISME)
            .mutualiste(UPDATED_MUTUALISTE)
            .nom(UPDATED_NOM)
            .nomFr(UPDATED_NOM_FR)
            .nomMedecin(UPDATED_NOM_MEDECIN)
            .prenom(UPDATED_PRENOM)
            .prenomfr(UPDATED_PRENOMFR)
            .scolariser(UPDATED_SCOLARISER)
            .sexe(UPDATED_SEXE)
            .specialiteMedecin(UPDATED_SPECIALITE_MEDECIN);
        EnfantDTO enfantDTO = enfantMapper.toDto(updatedEnfant);

        restEnfantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, enfantDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(enfantDTO))
            )
            .andExpect(status().isOk());

        // Validate the Enfant in the database
        List<Enfant> enfantList = enfantRepository.findAll();
        assertThat(enfantList).hasSize(databaseSizeBeforeUpdate);
        Enfant testEnfant = enfantList.get(enfantList.size() - 1);
        assertThat(testEnfant.getDateDiagnostic()).isEqualTo(UPDATED_DATE_DIAGNOSTIC);
        assertThat(testEnfant.getDateNaissance()).isEqualTo(UPDATED_DATE_NAISSANCE);
        assertThat(testEnfant.getDegreAutisme()).isEqualTo(UPDATED_DEGRE_AUTISME);
        assertThat(testEnfant.getMutualiste()).isEqualTo(UPDATED_MUTUALISTE);
        assertThat(testEnfant.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testEnfant.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testEnfant.getNomMedecin()).isEqualTo(UPDATED_NOM_MEDECIN);
        assertThat(testEnfant.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testEnfant.getPrenomfr()).isEqualTo(UPDATED_PRENOMFR);
        assertThat(testEnfant.getScolariser()).isEqualTo(UPDATED_SCOLARISER);
        assertThat(testEnfant.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testEnfant.getSpecialiteMedecin()).isEqualTo(UPDATED_SPECIALITE_MEDECIN);
    }

    @Test
    @Transactional
    void putNonExistingEnfant() throws Exception {
        int databaseSizeBeforeUpdate = enfantRepository.findAll().size();
        enfant.setId(count.incrementAndGet());

        // Create the Enfant
        EnfantDTO enfantDTO = enfantMapper.toDto(enfant);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEnfantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, enfantDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(enfantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Enfant in the database
        List<Enfant> enfantList = enfantRepository.findAll();
        assertThat(enfantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEnfant() throws Exception {
        int databaseSizeBeforeUpdate = enfantRepository.findAll().size();
        enfant.setId(count.incrementAndGet());

        // Create the Enfant
        EnfantDTO enfantDTO = enfantMapper.toDto(enfant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnfantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(enfantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Enfant in the database
        List<Enfant> enfantList = enfantRepository.findAll();
        assertThat(enfantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEnfant() throws Exception {
        int databaseSizeBeforeUpdate = enfantRepository.findAll().size();
        enfant.setId(count.incrementAndGet());

        // Create the Enfant
        EnfantDTO enfantDTO = enfantMapper.toDto(enfant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnfantMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(enfantDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Enfant in the database
        List<Enfant> enfantList = enfantRepository.findAll();
        assertThat(enfantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEnfantWithPatch() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        int databaseSizeBeforeUpdate = enfantRepository.findAll().size();

        // Update the enfant using partial update
        Enfant partialUpdatedEnfant = new Enfant();
        partialUpdatedEnfant.setId(enfant.getId());

        partialUpdatedEnfant
            .dateDiagnostic(UPDATED_DATE_DIAGNOSTIC)
            .degreAutisme(UPDATED_DEGRE_AUTISME)
            .mutualiste(UPDATED_MUTUALISTE)
            .prenom(UPDATED_PRENOM)
            .scolariser(UPDATED_SCOLARISER)
            .sexe(UPDATED_SEXE);

        restEnfantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEnfant.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEnfant))
            )
            .andExpect(status().isOk());

        // Validate the Enfant in the database
        List<Enfant> enfantList = enfantRepository.findAll();
        assertThat(enfantList).hasSize(databaseSizeBeforeUpdate);
        Enfant testEnfant = enfantList.get(enfantList.size() - 1);
        assertThat(testEnfant.getDateDiagnostic()).isEqualTo(UPDATED_DATE_DIAGNOSTIC);
        assertThat(testEnfant.getDateNaissance()).isEqualTo(DEFAULT_DATE_NAISSANCE);
        assertThat(testEnfant.getDegreAutisme()).isEqualTo(UPDATED_DEGRE_AUTISME);
        assertThat(testEnfant.getMutualiste()).isEqualTo(UPDATED_MUTUALISTE);
        assertThat(testEnfant.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testEnfant.getNomFr()).isEqualTo(DEFAULT_NOM_FR);
        assertThat(testEnfant.getNomMedecin()).isEqualTo(DEFAULT_NOM_MEDECIN);
        assertThat(testEnfant.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testEnfant.getPrenomfr()).isEqualTo(DEFAULT_PRENOMFR);
        assertThat(testEnfant.getScolariser()).isEqualTo(UPDATED_SCOLARISER);
        assertThat(testEnfant.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testEnfant.getSpecialiteMedecin()).isEqualTo(DEFAULT_SPECIALITE_MEDECIN);
    }

    @Test
    @Transactional
    void fullUpdateEnfantWithPatch() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        int databaseSizeBeforeUpdate = enfantRepository.findAll().size();

        // Update the enfant using partial update
        Enfant partialUpdatedEnfant = new Enfant();
        partialUpdatedEnfant.setId(enfant.getId());

        partialUpdatedEnfant
            .dateDiagnostic(UPDATED_DATE_DIAGNOSTIC)
            .dateNaissance(UPDATED_DATE_NAISSANCE)
            .degreAutisme(UPDATED_DEGRE_AUTISME)
            .mutualiste(UPDATED_MUTUALISTE)
            .nom(UPDATED_NOM)
            .nomFr(UPDATED_NOM_FR)
            .nomMedecin(UPDATED_NOM_MEDECIN)
            .prenom(UPDATED_PRENOM)
            .prenomfr(UPDATED_PRENOMFR)
            .scolariser(UPDATED_SCOLARISER)
            .sexe(UPDATED_SEXE)
            .specialiteMedecin(UPDATED_SPECIALITE_MEDECIN);

        restEnfantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEnfant.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEnfant))
            )
            .andExpect(status().isOk());

        // Validate the Enfant in the database
        List<Enfant> enfantList = enfantRepository.findAll();
        assertThat(enfantList).hasSize(databaseSizeBeforeUpdate);
        Enfant testEnfant = enfantList.get(enfantList.size() - 1);
        assertThat(testEnfant.getDateDiagnostic()).isEqualTo(UPDATED_DATE_DIAGNOSTIC);
        assertThat(testEnfant.getDateNaissance()).isEqualTo(UPDATED_DATE_NAISSANCE);
        assertThat(testEnfant.getDegreAutisme()).isEqualTo(UPDATED_DEGRE_AUTISME);
        assertThat(testEnfant.getMutualiste()).isEqualTo(UPDATED_MUTUALISTE);
        assertThat(testEnfant.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testEnfant.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testEnfant.getNomMedecin()).isEqualTo(UPDATED_NOM_MEDECIN);
        assertThat(testEnfant.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testEnfant.getPrenomfr()).isEqualTo(UPDATED_PRENOMFR);
        assertThat(testEnfant.getScolariser()).isEqualTo(UPDATED_SCOLARISER);
        assertThat(testEnfant.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testEnfant.getSpecialiteMedecin()).isEqualTo(UPDATED_SPECIALITE_MEDECIN);
    }

    @Test
    @Transactional
    void patchNonExistingEnfant() throws Exception {
        int databaseSizeBeforeUpdate = enfantRepository.findAll().size();
        enfant.setId(count.incrementAndGet());

        // Create the Enfant
        EnfantDTO enfantDTO = enfantMapper.toDto(enfant);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEnfantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, enfantDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(enfantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Enfant in the database
        List<Enfant> enfantList = enfantRepository.findAll();
        assertThat(enfantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEnfant() throws Exception {
        int databaseSizeBeforeUpdate = enfantRepository.findAll().size();
        enfant.setId(count.incrementAndGet());

        // Create the Enfant
        EnfantDTO enfantDTO = enfantMapper.toDto(enfant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnfantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(enfantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Enfant in the database
        List<Enfant> enfantList = enfantRepository.findAll();
        assertThat(enfantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEnfant() throws Exception {
        int databaseSizeBeforeUpdate = enfantRepository.findAll().size();
        enfant.setId(count.incrementAndGet());

        // Create the Enfant
        EnfantDTO enfantDTO = enfantMapper.toDto(enfant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnfantMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(enfantDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Enfant in the database
        List<Enfant> enfantList = enfantRepository.findAll();
        assertThat(enfantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEnfant() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        int databaseSizeBeforeDelete = enfantRepository.findAll().size();

        // Delete the enfant
        restEnfantMockMvc
            .perform(delete(ENTITY_API_URL_ID, enfant.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Enfant> enfantList = enfantRepository.findAll();
        assertThat(enfantList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
