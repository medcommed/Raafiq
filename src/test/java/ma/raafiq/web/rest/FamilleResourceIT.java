package ma.raafiq.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import ma.raafiq.IntegrationTest;
import ma.raafiq.domain.AppUser;
import ma.raafiq.domain.Enfant;
import ma.raafiq.domain.Famille;
import ma.raafiq.domain.MotifRefus;
import ma.raafiq.domain.Province;
import ma.raafiq.repository.FamilleRepository;
import ma.raafiq.service.criteria.FamilleCriteria;
import ma.raafiq.service.dto.FamilleDTO;
import ma.raafiq.service.mapper.FamilleMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link FamilleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FamilleResourceIT {

    private static final String DEFAULT_ADRESSE = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE = "BBBBBBBBBB";

    private static final Integer DEFAULT_BENEF_2019 = 1;
    private static final Integer UPDATED_BENEF_2019 = 2;
    private static final Integer SMALLER_BENEF_2019 = 1 - 1;

    private static final Integer DEFAULT_BENEF_2020 = 1;
    private static final Integer UPDATED_BENEF_2020 = 2;
    private static final Integer SMALLER_BENEF_2020 = 1 - 1;

    private static final String DEFAULT_CIN = "AAAAAAAAAA";
    private static final String UPDATED_CIN = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_CREATION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_CREATION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATE_MODIFICATION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_MODIFICATION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final LocalDate DEFAULT_DATE_NAISSANCE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_NAISSANCE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_NAISSANCE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final Integer DEFAULT_ETAT = 1;
    private static final Integer UPDATED_ETAT = 2;
    private static final Integer SMALLER_ETAT = 1 - 1;

    private static final String DEFAULT_EXPLICATION_REFUS = "AAAAAAAAAA";
    private static final String UPDATED_EXPLICATION_REFUS = "BBBBBBBBBB";

    private static final Integer DEFAULT_NBR_ENFANTS = 1;
    private static final Integer UPDATED_NBR_ENFANTS = 2;
    private static final Integer SMALLER_NBR_ENFANTS = 1 - 1;

    private static final Integer DEFAULT_NIVEAU_SCOLARITE = 1;
    private static final Integer UPDATED_NIVEAU_SCOLARITE = 2;
    private static final Integer SMALLER_NIVEAU_SCOLARITE = 1 - 1;

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_NOM_FR = "AAAAAAAAAA";
    private static final String UPDATED_NOM_FR = "BBBBBBBBBB";

    private static final String DEFAULT_NUMERO_DOSSIER = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_DOSSIER = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM_FR = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM_FR = "BBBBBBBBBB";

    private static final String DEFAULT_PROFESSION = "AAAAAAAAAA";
    private static final String UPDATED_PROFESSION = "BBBBBBBBBB";

    private static final Integer DEFAULT_SELECTIONNER = 1;
    private static final Integer UPDATED_SELECTIONNER = 2;
    private static final Integer SMALLER_SELECTIONNER = 1 - 1;

    private static final Integer DEFAULT_SEXE = 1;
    private static final Integer UPDATED_SEXE = 2;
    private static final Integer SMALLER_SEXE = 1 - 1;

    private static final String DEFAULT_TELEPHONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEPHONE = "BBBBBBBBBB";

    private static final Integer DEFAULT_AUTRE_BENEF_2019 = 1;
    private static final Integer UPDATED_AUTRE_BENEF_2019 = 2;
    private static final Integer SMALLER_AUTRE_BENEF_2019 = 1 - 1;

    private static final Integer DEFAULT_AUTRE_BENEF_2020 = 1;
    private static final Integer UPDATED_AUTRE_BENEF_2020 = 2;
    private static final Integer SMALLER_AUTRE_BENEF_2020 = 1 - 1;

    private static final Integer DEFAULT_RELATION_FAMILIALE = 1;
    private static final Integer UPDATED_RELATION_FAMILIALE = 2;
    private static final Integer SMALLER_RELATION_FAMILIALE = 1 - 1;

    private static final String ENTITY_API_URL = "/api/familles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FamilleRepository familleRepository;

    @Autowired
    private FamilleMapper familleMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFamilleMockMvc;

    private Famille famille;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Famille createEntity(EntityManager em) {
        Famille famille = new Famille()
            .adresse(DEFAULT_ADRESSE)
            .benef2019(DEFAULT_BENEF_2019)
            .benef2020(DEFAULT_BENEF_2020)
            .cin(DEFAULT_CIN)
            .dateCreation(DEFAULT_DATE_CREATION)
            .dateModification(DEFAULT_DATE_MODIFICATION)
            .dateNaissance(DEFAULT_DATE_NAISSANCE)
            .email(DEFAULT_EMAIL)
            .etat(DEFAULT_ETAT)
            .explicationRefus(DEFAULT_EXPLICATION_REFUS)
            .nbrEnfants(DEFAULT_NBR_ENFANTS)
            .niveauScolarite(DEFAULT_NIVEAU_SCOLARITE)
            .nom(DEFAULT_NOM)
            .nomFr(DEFAULT_NOM_FR)
            .numeroDossier(DEFAULT_NUMERO_DOSSIER)
            .prenom(DEFAULT_PRENOM)
            .prenomFr(DEFAULT_PRENOM_FR)
            .profession(DEFAULT_PROFESSION)
            .selectionner(DEFAULT_SELECTIONNER)
            .sexe(DEFAULT_SEXE)
            .telephone(DEFAULT_TELEPHONE)
            .autreBenef2019(DEFAULT_AUTRE_BENEF_2019)
            .autreBenef2020(DEFAULT_AUTRE_BENEF_2020)
            .relationFamiliale(DEFAULT_RELATION_FAMILIALE);
        return famille;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Famille createUpdatedEntity(EntityManager em) {
        Famille famille = new Famille()
            .adresse(UPDATED_ADRESSE)
            .benef2019(UPDATED_BENEF_2019)
            .benef2020(UPDATED_BENEF_2020)
            .cin(UPDATED_CIN)
            .dateCreation(UPDATED_DATE_CREATION)
            .dateModification(UPDATED_DATE_MODIFICATION)
            .dateNaissance(UPDATED_DATE_NAISSANCE)
            .email(UPDATED_EMAIL)
            .etat(UPDATED_ETAT)
            .explicationRefus(UPDATED_EXPLICATION_REFUS)
            .nbrEnfants(UPDATED_NBR_ENFANTS)
            .niveauScolarite(UPDATED_NIVEAU_SCOLARITE)
            .nom(UPDATED_NOM)
            .nomFr(UPDATED_NOM_FR)
            .numeroDossier(UPDATED_NUMERO_DOSSIER)
            .prenom(UPDATED_PRENOM)
            .prenomFr(UPDATED_PRENOM_FR)
            .profession(UPDATED_PROFESSION)
            .selectionner(UPDATED_SELECTIONNER)
            .sexe(UPDATED_SEXE)
            .telephone(UPDATED_TELEPHONE)
            .autreBenef2019(UPDATED_AUTRE_BENEF_2019)
            .autreBenef2020(UPDATED_AUTRE_BENEF_2020)
            .relationFamiliale(UPDATED_RELATION_FAMILIALE);
        return famille;
    }

    @BeforeEach
    public void initTest() {
        famille = createEntity(em);
    }

    @Test
    @Transactional
    void createFamille() throws Exception {
        int databaseSizeBeforeCreate = familleRepository.findAll().size();
        // Create the Famille
        FamilleDTO familleDTO = familleMapper.toDto(famille);
        restFamilleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(familleDTO)))
            .andExpect(status().isCreated());

        // Validate the Famille in the database
        List<Famille> familleList = familleRepository.findAll();
        assertThat(familleList).hasSize(databaseSizeBeforeCreate + 1);
        Famille testFamille = familleList.get(familleList.size() - 1);
        assertThat(testFamille.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testFamille.getBenef2019()).isEqualTo(DEFAULT_BENEF_2019);
        assertThat(testFamille.getBenef2020()).isEqualTo(DEFAULT_BENEF_2020);
        assertThat(testFamille.getCin()).isEqualTo(DEFAULT_CIN);
        assertThat(testFamille.getDateCreation()).isEqualTo(DEFAULT_DATE_CREATION);
        assertThat(testFamille.getDateModification()).isEqualTo(DEFAULT_DATE_MODIFICATION);
        assertThat(testFamille.getDateNaissance()).isEqualTo(DEFAULT_DATE_NAISSANCE);
        assertThat(testFamille.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testFamille.getEtat()).isEqualTo(DEFAULT_ETAT);
        assertThat(testFamille.getExplicationRefus()).isEqualTo(DEFAULT_EXPLICATION_REFUS);
        assertThat(testFamille.getNbrEnfants()).isEqualTo(DEFAULT_NBR_ENFANTS);
        assertThat(testFamille.getNiveauScolarite()).isEqualTo(DEFAULT_NIVEAU_SCOLARITE);
        assertThat(testFamille.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testFamille.getNomFr()).isEqualTo(DEFAULT_NOM_FR);
        assertThat(testFamille.getNumeroDossier()).isEqualTo(DEFAULT_NUMERO_DOSSIER);
        assertThat(testFamille.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testFamille.getPrenomFr()).isEqualTo(DEFAULT_PRENOM_FR);
        assertThat(testFamille.getProfession()).isEqualTo(DEFAULT_PROFESSION);
        assertThat(testFamille.getSelectionner()).isEqualTo(DEFAULT_SELECTIONNER);
        assertThat(testFamille.getSexe()).isEqualTo(DEFAULT_SEXE);
        assertThat(testFamille.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
        assertThat(testFamille.getAutreBenef2019()).isEqualTo(DEFAULT_AUTRE_BENEF_2019);
        assertThat(testFamille.getAutreBenef2020()).isEqualTo(DEFAULT_AUTRE_BENEF_2020);
        assertThat(testFamille.getRelationFamiliale()).isEqualTo(DEFAULT_RELATION_FAMILIALE);
    }

    @Test
    @Transactional
    void createFamilleWithExistingId() throws Exception {
        // Create the Famille with an existing ID
        famille.setId(1L);
        FamilleDTO familleDTO = familleMapper.toDto(famille);

        int databaseSizeBeforeCreate = familleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFamilleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(familleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Famille in the database
        List<Famille> familleList = familleRepository.findAll();
        assertThat(familleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCinIsRequired() throws Exception {
        int databaseSizeBeforeTest = familleRepository.findAll().size();
        // set the field null
        famille.setCin(null);

        // Create the Famille, which fails.
        FamilleDTO familleDTO = familleMapper.toDto(famille);

        restFamilleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(familleDTO)))
            .andExpect(status().isBadRequest());

        List<Famille> familleList = familleRepository.findAll();
        assertThat(familleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = familleRepository.findAll().size();
        // set the field null
        famille.setEmail(null);

        // Create the Famille, which fails.
        FamilleDTO familleDTO = familleMapper.toDto(famille);

        restFamilleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(familleDTO)))
            .andExpect(status().isBadRequest());

        List<Famille> familleList = familleRepository.findAll();
        assertThat(familleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNbrEnfantsIsRequired() throws Exception {
        int databaseSizeBeforeTest = familleRepository.findAll().size();
        // set the field null
        famille.setNbrEnfants(null);

        // Create the Famille, which fails.
        FamilleDTO familleDTO = familleMapper.toDto(famille);

        restFamilleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(familleDTO)))
            .andExpect(status().isBadRequest());

        List<Famille> familleList = familleRepository.findAll();
        assertThat(familleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNumeroDossierIsRequired() throws Exception {
        int databaseSizeBeforeTest = familleRepository.findAll().size();
        // set the field null
        famille.setNumeroDossier(null);

        // Create the Famille, which fails.
        FamilleDTO familleDTO = familleMapper.toDto(famille);

        restFamilleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(familleDTO)))
            .andExpect(status().isBadRequest());

        List<Famille> familleList = familleRepository.findAll();
        assertThat(familleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFamilles() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList
        restFamilleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(famille.getId().intValue())))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE)))
            .andExpect(jsonPath("$.[*].benef2019").value(hasItem(DEFAULT_BENEF_2019)))
            .andExpect(jsonPath("$.[*].benef2020").value(hasItem(DEFAULT_BENEF_2020)))
            .andExpect(jsonPath("$.[*].cin").value(hasItem(DEFAULT_CIN)))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(DEFAULT_DATE_CREATION.toString())))
            .andExpect(jsonPath("$.[*].dateModification").value(hasItem(DEFAULT_DATE_MODIFICATION.toString())))
            .andExpect(jsonPath("$.[*].dateNaissance").value(hasItem(DEFAULT_DATE_NAISSANCE.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].etat").value(hasItem(DEFAULT_ETAT)))
            .andExpect(jsonPath("$.[*].explicationRefus").value(hasItem(DEFAULT_EXPLICATION_REFUS)))
            .andExpect(jsonPath("$.[*].nbrEnfants").value(hasItem(DEFAULT_NBR_ENFANTS)))
            .andExpect(jsonPath("$.[*].niveauScolarite").value(hasItem(DEFAULT_NIVEAU_SCOLARITE)))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].nomFr").value(hasItem(DEFAULT_NOM_FR)))
            .andExpect(jsonPath("$.[*].numeroDossier").value(hasItem(DEFAULT_NUMERO_DOSSIER)))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].prenomFr").value(hasItem(DEFAULT_PRENOM_FR)))
            .andExpect(jsonPath("$.[*].profession").value(hasItem(DEFAULT_PROFESSION)))
            .andExpect(jsonPath("$.[*].selectionner").value(hasItem(DEFAULT_SELECTIONNER)))
            .andExpect(jsonPath("$.[*].sexe").value(hasItem(DEFAULT_SEXE)))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)))
            .andExpect(jsonPath("$.[*].autreBenef2019").value(hasItem(DEFAULT_AUTRE_BENEF_2019)))
            .andExpect(jsonPath("$.[*].autreBenef2020").value(hasItem(DEFAULT_AUTRE_BENEF_2020)))
            .andExpect(jsonPath("$.[*].relationFamiliale").value(hasItem(DEFAULT_RELATION_FAMILIALE)));
    }

    @Test
    @Transactional
    void getFamille() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get the famille
        restFamilleMockMvc
            .perform(get(ENTITY_API_URL_ID, famille.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(famille.getId().intValue()))
            .andExpect(jsonPath("$.adresse").value(DEFAULT_ADRESSE))
            .andExpect(jsonPath("$.benef2019").value(DEFAULT_BENEF_2019))
            .andExpect(jsonPath("$.benef2020").value(DEFAULT_BENEF_2020))
            .andExpect(jsonPath("$.cin").value(DEFAULT_CIN))
            .andExpect(jsonPath("$.dateCreation").value(DEFAULT_DATE_CREATION.toString()))
            .andExpect(jsonPath("$.dateModification").value(DEFAULT_DATE_MODIFICATION.toString()))
            .andExpect(jsonPath("$.dateNaissance").value(DEFAULT_DATE_NAISSANCE.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.etat").value(DEFAULT_ETAT))
            .andExpect(jsonPath("$.explicationRefus").value(DEFAULT_EXPLICATION_REFUS))
            .andExpect(jsonPath("$.nbrEnfants").value(DEFAULT_NBR_ENFANTS))
            .andExpect(jsonPath("$.niveauScolarite").value(DEFAULT_NIVEAU_SCOLARITE))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.nomFr").value(DEFAULT_NOM_FR))
            .andExpect(jsonPath("$.numeroDossier").value(DEFAULT_NUMERO_DOSSIER))
            .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM))
            .andExpect(jsonPath("$.prenomFr").value(DEFAULT_PRENOM_FR))
            .andExpect(jsonPath("$.profession").value(DEFAULT_PROFESSION))
            .andExpect(jsonPath("$.selectionner").value(DEFAULT_SELECTIONNER))
            .andExpect(jsonPath("$.sexe").value(DEFAULT_SEXE))
            .andExpect(jsonPath("$.telephone").value(DEFAULT_TELEPHONE))
            .andExpect(jsonPath("$.autreBenef2019").value(DEFAULT_AUTRE_BENEF_2019))
            .andExpect(jsonPath("$.autreBenef2020").value(DEFAULT_AUTRE_BENEF_2020))
            .andExpect(jsonPath("$.relationFamiliale").value(DEFAULT_RELATION_FAMILIALE));
    }

    @Test
    @Transactional
    void getFamillesByIdFiltering() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        Long id = famille.getId();

        defaultFamilleShouldBeFound("id.equals=" + id);
        defaultFamilleShouldNotBeFound("id.notEquals=" + id);

        defaultFamilleShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFamilleShouldNotBeFound("id.greaterThan=" + id);

        defaultFamilleShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFamilleShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFamillesByAdresseIsEqualToSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where adresse equals to DEFAULT_ADRESSE
        defaultFamilleShouldBeFound("adresse.equals=" + DEFAULT_ADRESSE);

        // Get all the familleList where adresse equals to UPDATED_ADRESSE
        defaultFamilleShouldNotBeFound("adresse.equals=" + UPDATED_ADRESSE);
    }

    @Test
    @Transactional
    void getAllFamillesByAdresseIsNotEqualToSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where adresse not equals to DEFAULT_ADRESSE
        defaultFamilleShouldNotBeFound("adresse.notEquals=" + DEFAULT_ADRESSE);

        // Get all the familleList where adresse not equals to UPDATED_ADRESSE
        defaultFamilleShouldBeFound("adresse.notEquals=" + UPDATED_ADRESSE);
    }

    @Test
    @Transactional
    void getAllFamillesByAdresseIsInShouldWork() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where adresse in DEFAULT_ADRESSE or UPDATED_ADRESSE
        defaultFamilleShouldBeFound("adresse.in=" + DEFAULT_ADRESSE + "," + UPDATED_ADRESSE);

        // Get all the familleList where adresse equals to UPDATED_ADRESSE
        defaultFamilleShouldNotBeFound("adresse.in=" + UPDATED_ADRESSE);
    }

    @Test
    @Transactional
    void getAllFamillesByAdresseIsNullOrNotNull() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where adresse is not null
        defaultFamilleShouldBeFound("adresse.specified=true");

        // Get all the familleList where adresse is null
        defaultFamilleShouldNotBeFound("adresse.specified=false");
    }

    @Test
    @Transactional
    void getAllFamillesByAdresseContainsSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where adresse contains DEFAULT_ADRESSE
        defaultFamilleShouldBeFound("adresse.contains=" + DEFAULT_ADRESSE);

        // Get all the familleList where adresse contains UPDATED_ADRESSE
        defaultFamilleShouldNotBeFound("adresse.contains=" + UPDATED_ADRESSE);
    }

    @Test
    @Transactional
    void getAllFamillesByAdresseNotContainsSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where adresse does not contain DEFAULT_ADRESSE
        defaultFamilleShouldNotBeFound("adresse.doesNotContain=" + DEFAULT_ADRESSE);

        // Get all the familleList where adresse does not contain UPDATED_ADRESSE
        defaultFamilleShouldBeFound("adresse.doesNotContain=" + UPDATED_ADRESSE);
    }

    @Test
    @Transactional
    void getAllFamillesByBenef2019IsEqualToSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where benef2019 equals to DEFAULT_BENEF_2019
        defaultFamilleShouldBeFound("benef2019.equals=" + DEFAULT_BENEF_2019);

        // Get all the familleList where benef2019 equals to UPDATED_BENEF_2019
        defaultFamilleShouldNotBeFound("benef2019.equals=" + UPDATED_BENEF_2019);
    }

    @Test
    @Transactional
    void getAllFamillesByBenef2019IsNotEqualToSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where benef2019 not equals to DEFAULT_BENEF_2019
        defaultFamilleShouldNotBeFound("benef2019.notEquals=" + DEFAULT_BENEF_2019);

        // Get all the familleList where benef2019 not equals to UPDATED_BENEF_2019
        defaultFamilleShouldBeFound("benef2019.notEquals=" + UPDATED_BENEF_2019);
    }

    @Test
    @Transactional
    void getAllFamillesByBenef2019IsInShouldWork() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where benef2019 in DEFAULT_BENEF_2019 or UPDATED_BENEF_2019
        defaultFamilleShouldBeFound("benef2019.in=" + DEFAULT_BENEF_2019 + "," + UPDATED_BENEF_2019);

        // Get all the familleList where benef2019 equals to UPDATED_BENEF_2019
        defaultFamilleShouldNotBeFound("benef2019.in=" + UPDATED_BENEF_2019);
    }

    @Test
    @Transactional
    void getAllFamillesByBenef2019IsNullOrNotNull() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where benef2019 is not null
        defaultFamilleShouldBeFound("benef2019.specified=true");

        // Get all the familleList where benef2019 is null
        defaultFamilleShouldNotBeFound("benef2019.specified=false");
    }

    @Test
    @Transactional
    void getAllFamillesByBenef2019IsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where benef2019 is greater than or equal to DEFAULT_BENEF_2019
        defaultFamilleShouldBeFound("benef2019.greaterThanOrEqual=" + DEFAULT_BENEF_2019);

        // Get all the familleList where benef2019 is greater than or equal to UPDATED_BENEF_2019
        defaultFamilleShouldNotBeFound("benef2019.greaterThanOrEqual=" + UPDATED_BENEF_2019);
    }

    @Test
    @Transactional
    void getAllFamillesByBenef2019IsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where benef2019 is less than or equal to DEFAULT_BENEF_2019
        defaultFamilleShouldBeFound("benef2019.lessThanOrEqual=" + DEFAULT_BENEF_2019);

        // Get all the familleList where benef2019 is less than or equal to SMALLER_BENEF_2019
        defaultFamilleShouldNotBeFound("benef2019.lessThanOrEqual=" + SMALLER_BENEF_2019);
    }

    @Test
    @Transactional
    void getAllFamillesByBenef2019IsLessThanSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where benef2019 is less than DEFAULT_BENEF_2019
        defaultFamilleShouldNotBeFound("benef2019.lessThan=" + DEFAULT_BENEF_2019);

        // Get all the familleList where benef2019 is less than UPDATED_BENEF_2019
        defaultFamilleShouldBeFound("benef2019.lessThan=" + UPDATED_BENEF_2019);
    }

    @Test
    @Transactional
    void getAllFamillesByBenef2019IsGreaterThanSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where benef2019 is greater than DEFAULT_BENEF_2019
        defaultFamilleShouldNotBeFound("benef2019.greaterThan=" + DEFAULT_BENEF_2019);

        // Get all the familleList where benef2019 is greater than SMALLER_BENEF_2019
        defaultFamilleShouldBeFound("benef2019.greaterThan=" + SMALLER_BENEF_2019);
    }

    @Test
    @Transactional
    void getAllFamillesByBenef2020IsEqualToSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where benef2020 equals to DEFAULT_BENEF_2020
        defaultFamilleShouldBeFound("benef2020.equals=" + DEFAULT_BENEF_2020);

        // Get all the familleList where benef2020 equals to UPDATED_BENEF_2020
        defaultFamilleShouldNotBeFound("benef2020.equals=" + UPDATED_BENEF_2020);
    }

    @Test
    @Transactional
    void getAllFamillesByBenef2020IsNotEqualToSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where benef2020 not equals to DEFAULT_BENEF_2020
        defaultFamilleShouldNotBeFound("benef2020.notEquals=" + DEFAULT_BENEF_2020);

        // Get all the familleList where benef2020 not equals to UPDATED_BENEF_2020
        defaultFamilleShouldBeFound("benef2020.notEquals=" + UPDATED_BENEF_2020);
    }

    @Test
    @Transactional
    void getAllFamillesByBenef2020IsInShouldWork() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where benef2020 in DEFAULT_BENEF_2020 or UPDATED_BENEF_2020
        defaultFamilleShouldBeFound("benef2020.in=" + DEFAULT_BENEF_2020 + "," + UPDATED_BENEF_2020);

        // Get all the familleList where benef2020 equals to UPDATED_BENEF_2020
        defaultFamilleShouldNotBeFound("benef2020.in=" + UPDATED_BENEF_2020);
    }

    @Test
    @Transactional
    void getAllFamillesByBenef2020IsNullOrNotNull() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where benef2020 is not null
        defaultFamilleShouldBeFound("benef2020.specified=true");

        // Get all the familleList where benef2020 is null
        defaultFamilleShouldNotBeFound("benef2020.specified=false");
    }

    @Test
    @Transactional
    void getAllFamillesByBenef2020IsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where benef2020 is greater than or equal to DEFAULT_BENEF_2020
        defaultFamilleShouldBeFound("benef2020.greaterThanOrEqual=" + DEFAULT_BENEF_2020);

        // Get all the familleList where benef2020 is greater than or equal to UPDATED_BENEF_2020
        defaultFamilleShouldNotBeFound("benef2020.greaterThanOrEqual=" + UPDATED_BENEF_2020);
    }

    @Test
    @Transactional
    void getAllFamillesByBenef2020IsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where benef2020 is less than or equal to DEFAULT_BENEF_2020
        defaultFamilleShouldBeFound("benef2020.lessThanOrEqual=" + DEFAULT_BENEF_2020);

        // Get all the familleList where benef2020 is less than or equal to SMALLER_BENEF_2020
        defaultFamilleShouldNotBeFound("benef2020.lessThanOrEqual=" + SMALLER_BENEF_2020);
    }

    @Test
    @Transactional
    void getAllFamillesByBenef2020IsLessThanSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where benef2020 is less than DEFAULT_BENEF_2020
        defaultFamilleShouldNotBeFound("benef2020.lessThan=" + DEFAULT_BENEF_2020);

        // Get all the familleList where benef2020 is less than UPDATED_BENEF_2020
        defaultFamilleShouldBeFound("benef2020.lessThan=" + UPDATED_BENEF_2020);
    }

    @Test
    @Transactional
    void getAllFamillesByBenef2020IsGreaterThanSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where benef2020 is greater than DEFAULT_BENEF_2020
        defaultFamilleShouldNotBeFound("benef2020.greaterThan=" + DEFAULT_BENEF_2020);

        // Get all the familleList where benef2020 is greater than SMALLER_BENEF_2020
        defaultFamilleShouldBeFound("benef2020.greaterThan=" + SMALLER_BENEF_2020);
    }

    @Test
    @Transactional
    void getAllFamillesByCinIsEqualToSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where cin equals to DEFAULT_CIN
        defaultFamilleShouldBeFound("cin.equals=" + DEFAULT_CIN);

        // Get all the familleList where cin equals to UPDATED_CIN
        defaultFamilleShouldNotBeFound("cin.equals=" + UPDATED_CIN);
    }

    @Test
    @Transactional
    void getAllFamillesByCinIsNotEqualToSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where cin not equals to DEFAULT_CIN
        defaultFamilleShouldNotBeFound("cin.notEquals=" + DEFAULT_CIN);

        // Get all the familleList where cin not equals to UPDATED_CIN
        defaultFamilleShouldBeFound("cin.notEquals=" + UPDATED_CIN);
    }

    @Test
    @Transactional
    void getAllFamillesByCinIsInShouldWork() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where cin in DEFAULT_CIN or UPDATED_CIN
        defaultFamilleShouldBeFound("cin.in=" + DEFAULT_CIN + "," + UPDATED_CIN);

        // Get all the familleList where cin equals to UPDATED_CIN
        defaultFamilleShouldNotBeFound("cin.in=" + UPDATED_CIN);
    }

    @Test
    @Transactional
    void getAllFamillesByCinIsNullOrNotNull() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where cin is not null
        defaultFamilleShouldBeFound("cin.specified=true");

        // Get all the familleList where cin is null
        defaultFamilleShouldNotBeFound("cin.specified=false");
    }

    @Test
    @Transactional
    void getAllFamillesByCinContainsSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where cin contains DEFAULT_CIN
        defaultFamilleShouldBeFound("cin.contains=" + DEFAULT_CIN);

        // Get all the familleList where cin contains UPDATED_CIN
        defaultFamilleShouldNotBeFound("cin.contains=" + UPDATED_CIN);
    }

    @Test
    @Transactional
    void getAllFamillesByCinNotContainsSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where cin does not contain DEFAULT_CIN
        defaultFamilleShouldNotBeFound("cin.doesNotContain=" + DEFAULT_CIN);

        // Get all the familleList where cin does not contain UPDATED_CIN
        defaultFamilleShouldBeFound("cin.doesNotContain=" + UPDATED_CIN);
    }

    @Test
    @Transactional
    void getAllFamillesByDateCreationIsEqualToSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where dateCreation equals to DEFAULT_DATE_CREATION
        defaultFamilleShouldBeFound("dateCreation.equals=" + DEFAULT_DATE_CREATION);

        // Get all the familleList where dateCreation equals to UPDATED_DATE_CREATION
        defaultFamilleShouldNotBeFound("dateCreation.equals=" + UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    void getAllFamillesByDateCreationIsNotEqualToSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where dateCreation not equals to DEFAULT_DATE_CREATION
        defaultFamilleShouldNotBeFound("dateCreation.notEquals=" + DEFAULT_DATE_CREATION);

        // Get all the familleList where dateCreation not equals to UPDATED_DATE_CREATION
        defaultFamilleShouldBeFound("dateCreation.notEquals=" + UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    void getAllFamillesByDateCreationIsInShouldWork() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where dateCreation in DEFAULT_DATE_CREATION or UPDATED_DATE_CREATION
        defaultFamilleShouldBeFound("dateCreation.in=" + DEFAULT_DATE_CREATION + "," + UPDATED_DATE_CREATION);

        // Get all the familleList where dateCreation equals to UPDATED_DATE_CREATION
        defaultFamilleShouldNotBeFound("dateCreation.in=" + UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    void getAllFamillesByDateCreationIsNullOrNotNull() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where dateCreation is not null
        defaultFamilleShouldBeFound("dateCreation.specified=true");

        // Get all the familleList where dateCreation is null
        defaultFamilleShouldNotBeFound("dateCreation.specified=false");
    }

    @Test
    @Transactional
    void getAllFamillesByDateModificationIsEqualToSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where dateModification equals to DEFAULT_DATE_MODIFICATION
        defaultFamilleShouldBeFound("dateModification.equals=" + DEFAULT_DATE_MODIFICATION);

        // Get all the familleList where dateModification equals to UPDATED_DATE_MODIFICATION
        defaultFamilleShouldNotBeFound("dateModification.equals=" + UPDATED_DATE_MODIFICATION);
    }

    @Test
    @Transactional
    void getAllFamillesByDateModificationIsNotEqualToSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where dateModification not equals to DEFAULT_DATE_MODIFICATION
        defaultFamilleShouldNotBeFound("dateModification.notEquals=" + DEFAULT_DATE_MODIFICATION);

        // Get all the familleList where dateModification not equals to UPDATED_DATE_MODIFICATION
        defaultFamilleShouldBeFound("dateModification.notEquals=" + UPDATED_DATE_MODIFICATION);
    }

    @Test
    @Transactional
    void getAllFamillesByDateModificationIsInShouldWork() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where dateModification in DEFAULT_DATE_MODIFICATION or UPDATED_DATE_MODIFICATION
        defaultFamilleShouldBeFound("dateModification.in=" + DEFAULT_DATE_MODIFICATION + "," + UPDATED_DATE_MODIFICATION);

        // Get all the familleList where dateModification equals to UPDATED_DATE_MODIFICATION
        defaultFamilleShouldNotBeFound("dateModification.in=" + UPDATED_DATE_MODIFICATION);
    }

    @Test
    @Transactional
    void getAllFamillesByDateModificationIsNullOrNotNull() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where dateModification is not null
        defaultFamilleShouldBeFound("dateModification.specified=true");

        // Get all the familleList where dateModification is null
        defaultFamilleShouldNotBeFound("dateModification.specified=false");
    }

    @Test
    @Transactional
    void getAllFamillesByDateNaissanceIsEqualToSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where dateNaissance equals to DEFAULT_DATE_NAISSANCE
        defaultFamilleShouldBeFound("dateNaissance.equals=" + DEFAULT_DATE_NAISSANCE);

        // Get all the familleList where dateNaissance equals to UPDATED_DATE_NAISSANCE
        defaultFamilleShouldNotBeFound("dateNaissance.equals=" + UPDATED_DATE_NAISSANCE);
    }

    @Test
    @Transactional
    void getAllFamillesByDateNaissanceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where dateNaissance not equals to DEFAULT_DATE_NAISSANCE
        defaultFamilleShouldNotBeFound("dateNaissance.notEquals=" + DEFAULT_DATE_NAISSANCE);

        // Get all the familleList where dateNaissance not equals to UPDATED_DATE_NAISSANCE
        defaultFamilleShouldBeFound("dateNaissance.notEquals=" + UPDATED_DATE_NAISSANCE);
    }

    @Test
    @Transactional
    void getAllFamillesByDateNaissanceIsInShouldWork() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where dateNaissance in DEFAULT_DATE_NAISSANCE or UPDATED_DATE_NAISSANCE
        defaultFamilleShouldBeFound("dateNaissance.in=" + DEFAULT_DATE_NAISSANCE + "," + UPDATED_DATE_NAISSANCE);

        // Get all the familleList where dateNaissance equals to UPDATED_DATE_NAISSANCE
        defaultFamilleShouldNotBeFound("dateNaissance.in=" + UPDATED_DATE_NAISSANCE);
    }

    @Test
    @Transactional
    void getAllFamillesByDateNaissanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where dateNaissance is not null
        defaultFamilleShouldBeFound("dateNaissance.specified=true");

        // Get all the familleList where dateNaissance is null
        defaultFamilleShouldNotBeFound("dateNaissance.specified=false");
    }

    @Test
    @Transactional
    void getAllFamillesByDateNaissanceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where dateNaissance is greater than or equal to DEFAULT_DATE_NAISSANCE
        defaultFamilleShouldBeFound("dateNaissance.greaterThanOrEqual=" + DEFAULT_DATE_NAISSANCE);

        // Get all the familleList where dateNaissance is greater than or equal to UPDATED_DATE_NAISSANCE
        defaultFamilleShouldNotBeFound("dateNaissance.greaterThanOrEqual=" + UPDATED_DATE_NAISSANCE);
    }

    @Test
    @Transactional
    void getAllFamillesByDateNaissanceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where dateNaissance is less than or equal to DEFAULT_DATE_NAISSANCE
        defaultFamilleShouldBeFound("dateNaissance.lessThanOrEqual=" + DEFAULT_DATE_NAISSANCE);

        // Get all the familleList where dateNaissance is less than or equal to SMALLER_DATE_NAISSANCE
        defaultFamilleShouldNotBeFound("dateNaissance.lessThanOrEqual=" + SMALLER_DATE_NAISSANCE);
    }

    @Test
    @Transactional
    void getAllFamillesByDateNaissanceIsLessThanSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where dateNaissance is less than DEFAULT_DATE_NAISSANCE
        defaultFamilleShouldNotBeFound("dateNaissance.lessThan=" + DEFAULT_DATE_NAISSANCE);

        // Get all the familleList where dateNaissance is less than UPDATED_DATE_NAISSANCE
        defaultFamilleShouldBeFound("dateNaissance.lessThan=" + UPDATED_DATE_NAISSANCE);
    }

    @Test
    @Transactional
    void getAllFamillesByDateNaissanceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where dateNaissance is greater than DEFAULT_DATE_NAISSANCE
        defaultFamilleShouldNotBeFound("dateNaissance.greaterThan=" + DEFAULT_DATE_NAISSANCE);

        // Get all the familleList where dateNaissance is greater than SMALLER_DATE_NAISSANCE
        defaultFamilleShouldBeFound("dateNaissance.greaterThan=" + SMALLER_DATE_NAISSANCE);
    }

    @Test
    @Transactional
    void getAllFamillesByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where email equals to DEFAULT_EMAIL
        defaultFamilleShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the familleList where email equals to UPDATED_EMAIL
        defaultFamilleShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllFamillesByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where email not equals to DEFAULT_EMAIL
        defaultFamilleShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the familleList where email not equals to UPDATED_EMAIL
        defaultFamilleShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllFamillesByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultFamilleShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the familleList where email equals to UPDATED_EMAIL
        defaultFamilleShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllFamillesByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where email is not null
        defaultFamilleShouldBeFound("email.specified=true");

        // Get all the familleList where email is null
        defaultFamilleShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllFamillesByEmailContainsSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where email contains DEFAULT_EMAIL
        defaultFamilleShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the familleList where email contains UPDATED_EMAIL
        defaultFamilleShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllFamillesByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where email does not contain DEFAULT_EMAIL
        defaultFamilleShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the familleList where email does not contain UPDATED_EMAIL
        defaultFamilleShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllFamillesByEtatIsEqualToSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where etat equals to DEFAULT_ETAT
        defaultFamilleShouldBeFound("etat.equals=" + DEFAULT_ETAT);

        // Get all the familleList where etat equals to UPDATED_ETAT
        defaultFamilleShouldNotBeFound("etat.equals=" + UPDATED_ETAT);
    }

    @Test
    @Transactional
    void getAllFamillesByEtatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where etat not equals to DEFAULT_ETAT
        defaultFamilleShouldNotBeFound("etat.notEquals=" + DEFAULT_ETAT);

        // Get all the familleList where etat not equals to UPDATED_ETAT
        defaultFamilleShouldBeFound("etat.notEquals=" + UPDATED_ETAT);
    }

    @Test
    @Transactional
    void getAllFamillesByEtatIsInShouldWork() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where etat in DEFAULT_ETAT or UPDATED_ETAT
        defaultFamilleShouldBeFound("etat.in=" + DEFAULT_ETAT + "," + UPDATED_ETAT);

        // Get all the familleList where etat equals to UPDATED_ETAT
        defaultFamilleShouldNotBeFound("etat.in=" + UPDATED_ETAT);
    }

    @Test
    @Transactional
    void getAllFamillesByEtatIsNullOrNotNull() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where etat is not null
        defaultFamilleShouldBeFound("etat.specified=true");

        // Get all the familleList where etat is null
        defaultFamilleShouldNotBeFound("etat.specified=false");
    }

    @Test
    @Transactional
    void getAllFamillesByEtatIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where etat is greater than or equal to DEFAULT_ETAT
        defaultFamilleShouldBeFound("etat.greaterThanOrEqual=" + DEFAULT_ETAT);

        // Get all the familleList where etat is greater than or equal to UPDATED_ETAT
        defaultFamilleShouldNotBeFound("etat.greaterThanOrEqual=" + UPDATED_ETAT);
    }

    @Test
    @Transactional
    void getAllFamillesByEtatIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where etat is less than or equal to DEFAULT_ETAT
        defaultFamilleShouldBeFound("etat.lessThanOrEqual=" + DEFAULT_ETAT);

        // Get all the familleList where etat is less than or equal to SMALLER_ETAT
        defaultFamilleShouldNotBeFound("etat.lessThanOrEqual=" + SMALLER_ETAT);
    }

    @Test
    @Transactional
    void getAllFamillesByEtatIsLessThanSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where etat is less than DEFAULT_ETAT
        defaultFamilleShouldNotBeFound("etat.lessThan=" + DEFAULT_ETAT);

        // Get all the familleList where etat is less than UPDATED_ETAT
        defaultFamilleShouldBeFound("etat.lessThan=" + UPDATED_ETAT);
    }

    @Test
    @Transactional
    void getAllFamillesByEtatIsGreaterThanSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where etat is greater than DEFAULT_ETAT
        defaultFamilleShouldNotBeFound("etat.greaterThan=" + DEFAULT_ETAT);

        // Get all the familleList where etat is greater than SMALLER_ETAT
        defaultFamilleShouldBeFound("etat.greaterThan=" + SMALLER_ETAT);
    }

    @Test
    @Transactional
    void getAllFamillesByExplicationRefusIsEqualToSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where explicationRefus equals to DEFAULT_EXPLICATION_REFUS
        defaultFamilleShouldBeFound("explicationRefus.equals=" + DEFAULT_EXPLICATION_REFUS);

        // Get all the familleList where explicationRefus equals to UPDATED_EXPLICATION_REFUS
        defaultFamilleShouldNotBeFound("explicationRefus.equals=" + UPDATED_EXPLICATION_REFUS);
    }

    @Test
    @Transactional
    void getAllFamillesByExplicationRefusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where explicationRefus not equals to DEFAULT_EXPLICATION_REFUS
        defaultFamilleShouldNotBeFound("explicationRefus.notEquals=" + DEFAULT_EXPLICATION_REFUS);

        // Get all the familleList where explicationRefus not equals to UPDATED_EXPLICATION_REFUS
        defaultFamilleShouldBeFound("explicationRefus.notEquals=" + UPDATED_EXPLICATION_REFUS);
    }

    @Test
    @Transactional
    void getAllFamillesByExplicationRefusIsInShouldWork() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where explicationRefus in DEFAULT_EXPLICATION_REFUS or UPDATED_EXPLICATION_REFUS
        defaultFamilleShouldBeFound("explicationRefus.in=" + DEFAULT_EXPLICATION_REFUS + "," + UPDATED_EXPLICATION_REFUS);

        // Get all the familleList where explicationRefus equals to UPDATED_EXPLICATION_REFUS
        defaultFamilleShouldNotBeFound("explicationRefus.in=" + UPDATED_EXPLICATION_REFUS);
    }

    @Test
    @Transactional
    void getAllFamillesByExplicationRefusIsNullOrNotNull() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where explicationRefus is not null
        defaultFamilleShouldBeFound("explicationRefus.specified=true");

        // Get all the familleList where explicationRefus is null
        defaultFamilleShouldNotBeFound("explicationRefus.specified=false");
    }

    @Test
    @Transactional
    void getAllFamillesByExplicationRefusContainsSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where explicationRefus contains DEFAULT_EXPLICATION_REFUS
        defaultFamilleShouldBeFound("explicationRefus.contains=" + DEFAULT_EXPLICATION_REFUS);

        // Get all the familleList where explicationRefus contains UPDATED_EXPLICATION_REFUS
        defaultFamilleShouldNotBeFound("explicationRefus.contains=" + UPDATED_EXPLICATION_REFUS);
    }

    @Test
    @Transactional
    void getAllFamillesByExplicationRefusNotContainsSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where explicationRefus does not contain DEFAULT_EXPLICATION_REFUS
        defaultFamilleShouldNotBeFound("explicationRefus.doesNotContain=" + DEFAULT_EXPLICATION_REFUS);

        // Get all the familleList where explicationRefus does not contain UPDATED_EXPLICATION_REFUS
        defaultFamilleShouldBeFound("explicationRefus.doesNotContain=" + UPDATED_EXPLICATION_REFUS);
    }

    @Test
    @Transactional
    void getAllFamillesByNbrEnfantsIsEqualToSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where nbrEnfants equals to DEFAULT_NBR_ENFANTS
        defaultFamilleShouldBeFound("nbrEnfants.equals=" + DEFAULT_NBR_ENFANTS);

        // Get all the familleList where nbrEnfants equals to UPDATED_NBR_ENFANTS
        defaultFamilleShouldNotBeFound("nbrEnfants.equals=" + UPDATED_NBR_ENFANTS);
    }

    @Test
    @Transactional
    void getAllFamillesByNbrEnfantsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where nbrEnfants not equals to DEFAULT_NBR_ENFANTS
        defaultFamilleShouldNotBeFound("nbrEnfants.notEquals=" + DEFAULT_NBR_ENFANTS);

        // Get all the familleList where nbrEnfants not equals to UPDATED_NBR_ENFANTS
        defaultFamilleShouldBeFound("nbrEnfants.notEquals=" + UPDATED_NBR_ENFANTS);
    }

    @Test
    @Transactional
    void getAllFamillesByNbrEnfantsIsInShouldWork() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where nbrEnfants in DEFAULT_NBR_ENFANTS or UPDATED_NBR_ENFANTS
        defaultFamilleShouldBeFound("nbrEnfants.in=" + DEFAULT_NBR_ENFANTS + "," + UPDATED_NBR_ENFANTS);

        // Get all the familleList where nbrEnfants equals to UPDATED_NBR_ENFANTS
        defaultFamilleShouldNotBeFound("nbrEnfants.in=" + UPDATED_NBR_ENFANTS);
    }

    @Test
    @Transactional
    void getAllFamillesByNbrEnfantsIsNullOrNotNull() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where nbrEnfants is not null
        defaultFamilleShouldBeFound("nbrEnfants.specified=true");

        // Get all the familleList where nbrEnfants is null
        defaultFamilleShouldNotBeFound("nbrEnfants.specified=false");
    }

    @Test
    @Transactional
    void getAllFamillesByNbrEnfantsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where nbrEnfants is greater than or equal to DEFAULT_NBR_ENFANTS
        defaultFamilleShouldBeFound("nbrEnfants.greaterThanOrEqual=" + DEFAULT_NBR_ENFANTS);

        // Get all the familleList where nbrEnfants is greater than or equal to UPDATED_NBR_ENFANTS
        defaultFamilleShouldNotBeFound("nbrEnfants.greaterThanOrEqual=" + UPDATED_NBR_ENFANTS);
    }

    @Test
    @Transactional
    void getAllFamillesByNbrEnfantsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where nbrEnfants is less than or equal to DEFAULT_NBR_ENFANTS
        defaultFamilleShouldBeFound("nbrEnfants.lessThanOrEqual=" + DEFAULT_NBR_ENFANTS);

        // Get all the familleList where nbrEnfants is less than or equal to SMALLER_NBR_ENFANTS
        defaultFamilleShouldNotBeFound("nbrEnfants.lessThanOrEqual=" + SMALLER_NBR_ENFANTS);
    }

    @Test
    @Transactional
    void getAllFamillesByNbrEnfantsIsLessThanSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where nbrEnfants is less than DEFAULT_NBR_ENFANTS
        defaultFamilleShouldNotBeFound("nbrEnfants.lessThan=" + DEFAULT_NBR_ENFANTS);

        // Get all the familleList where nbrEnfants is less than UPDATED_NBR_ENFANTS
        defaultFamilleShouldBeFound("nbrEnfants.lessThan=" + UPDATED_NBR_ENFANTS);
    }

    @Test
    @Transactional
    void getAllFamillesByNbrEnfantsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where nbrEnfants is greater than DEFAULT_NBR_ENFANTS
        defaultFamilleShouldNotBeFound("nbrEnfants.greaterThan=" + DEFAULT_NBR_ENFANTS);

        // Get all the familleList where nbrEnfants is greater than SMALLER_NBR_ENFANTS
        defaultFamilleShouldBeFound("nbrEnfants.greaterThan=" + SMALLER_NBR_ENFANTS);
    }

    @Test
    @Transactional
    void getAllFamillesByNiveauScolariteIsEqualToSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where niveauScolarite equals to DEFAULT_NIVEAU_SCOLARITE
        defaultFamilleShouldBeFound("niveauScolarite.equals=" + DEFAULT_NIVEAU_SCOLARITE);

        // Get all the familleList where niveauScolarite equals to UPDATED_NIVEAU_SCOLARITE
        defaultFamilleShouldNotBeFound("niveauScolarite.equals=" + UPDATED_NIVEAU_SCOLARITE);
    }

    @Test
    @Transactional
    void getAllFamillesByNiveauScolariteIsNotEqualToSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where niveauScolarite not equals to DEFAULT_NIVEAU_SCOLARITE
        defaultFamilleShouldNotBeFound("niveauScolarite.notEquals=" + DEFAULT_NIVEAU_SCOLARITE);

        // Get all the familleList where niveauScolarite not equals to UPDATED_NIVEAU_SCOLARITE
        defaultFamilleShouldBeFound("niveauScolarite.notEquals=" + UPDATED_NIVEAU_SCOLARITE);
    }

    @Test
    @Transactional
    void getAllFamillesByNiveauScolariteIsInShouldWork() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where niveauScolarite in DEFAULT_NIVEAU_SCOLARITE or UPDATED_NIVEAU_SCOLARITE
        defaultFamilleShouldBeFound("niveauScolarite.in=" + DEFAULT_NIVEAU_SCOLARITE + "," + UPDATED_NIVEAU_SCOLARITE);

        // Get all the familleList where niveauScolarite equals to UPDATED_NIVEAU_SCOLARITE
        defaultFamilleShouldNotBeFound("niveauScolarite.in=" + UPDATED_NIVEAU_SCOLARITE);
    }

    @Test
    @Transactional
    void getAllFamillesByNiveauScolariteIsNullOrNotNull() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where niveauScolarite is not null
        defaultFamilleShouldBeFound("niveauScolarite.specified=true");

        // Get all the familleList where niveauScolarite is null
        defaultFamilleShouldNotBeFound("niveauScolarite.specified=false");
    }

    @Test
    @Transactional
    void getAllFamillesByNiveauScolariteIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where niveauScolarite is greater than or equal to DEFAULT_NIVEAU_SCOLARITE
        defaultFamilleShouldBeFound("niveauScolarite.greaterThanOrEqual=" + DEFAULT_NIVEAU_SCOLARITE);

        // Get all the familleList where niveauScolarite is greater than or equal to UPDATED_NIVEAU_SCOLARITE
        defaultFamilleShouldNotBeFound("niveauScolarite.greaterThanOrEqual=" + UPDATED_NIVEAU_SCOLARITE);
    }

    @Test
    @Transactional
    void getAllFamillesByNiveauScolariteIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where niveauScolarite is less than or equal to DEFAULT_NIVEAU_SCOLARITE
        defaultFamilleShouldBeFound("niveauScolarite.lessThanOrEqual=" + DEFAULT_NIVEAU_SCOLARITE);

        // Get all the familleList where niveauScolarite is less than or equal to SMALLER_NIVEAU_SCOLARITE
        defaultFamilleShouldNotBeFound("niveauScolarite.lessThanOrEqual=" + SMALLER_NIVEAU_SCOLARITE);
    }

    @Test
    @Transactional
    void getAllFamillesByNiveauScolariteIsLessThanSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where niveauScolarite is less than DEFAULT_NIVEAU_SCOLARITE
        defaultFamilleShouldNotBeFound("niveauScolarite.lessThan=" + DEFAULT_NIVEAU_SCOLARITE);

        // Get all the familleList where niveauScolarite is less than UPDATED_NIVEAU_SCOLARITE
        defaultFamilleShouldBeFound("niveauScolarite.lessThan=" + UPDATED_NIVEAU_SCOLARITE);
    }

    @Test
    @Transactional
    void getAllFamillesByNiveauScolariteIsGreaterThanSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where niveauScolarite is greater than DEFAULT_NIVEAU_SCOLARITE
        defaultFamilleShouldNotBeFound("niveauScolarite.greaterThan=" + DEFAULT_NIVEAU_SCOLARITE);

        // Get all the familleList where niveauScolarite is greater than SMALLER_NIVEAU_SCOLARITE
        defaultFamilleShouldBeFound("niveauScolarite.greaterThan=" + SMALLER_NIVEAU_SCOLARITE);
    }

    @Test
    @Transactional
    void getAllFamillesByNomIsEqualToSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where nom equals to DEFAULT_NOM
        defaultFamilleShouldBeFound("nom.equals=" + DEFAULT_NOM);

        // Get all the familleList where nom equals to UPDATED_NOM
        defaultFamilleShouldNotBeFound("nom.equals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllFamillesByNomIsNotEqualToSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where nom not equals to DEFAULT_NOM
        defaultFamilleShouldNotBeFound("nom.notEquals=" + DEFAULT_NOM);

        // Get all the familleList where nom not equals to UPDATED_NOM
        defaultFamilleShouldBeFound("nom.notEquals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllFamillesByNomIsInShouldWork() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where nom in DEFAULT_NOM or UPDATED_NOM
        defaultFamilleShouldBeFound("nom.in=" + DEFAULT_NOM + "," + UPDATED_NOM);

        // Get all the familleList where nom equals to UPDATED_NOM
        defaultFamilleShouldNotBeFound("nom.in=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllFamillesByNomIsNullOrNotNull() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where nom is not null
        defaultFamilleShouldBeFound("nom.specified=true");

        // Get all the familleList where nom is null
        defaultFamilleShouldNotBeFound("nom.specified=false");
    }

    @Test
    @Transactional
    void getAllFamillesByNomContainsSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where nom contains DEFAULT_NOM
        defaultFamilleShouldBeFound("nom.contains=" + DEFAULT_NOM);

        // Get all the familleList where nom contains UPDATED_NOM
        defaultFamilleShouldNotBeFound("nom.contains=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllFamillesByNomNotContainsSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where nom does not contain DEFAULT_NOM
        defaultFamilleShouldNotBeFound("nom.doesNotContain=" + DEFAULT_NOM);

        // Get all the familleList where nom does not contain UPDATED_NOM
        defaultFamilleShouldBeFound("nom.doesNotContain=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllFamillesByNomFrIsEqualToSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where nomFr equals to DEFAULT_NOM_FR
        defaultFamilleShouldBeFound("nomFr.equals=" + DEFAULT_NOM_FR);

        // Get all the familleList where nomFr equals to UPDATED_NOM_FR
        defaultFamilleShouldNotBeFound("nomFr.equals=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllFamillesByNomFrIsNotEqualToSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where nomFr not equals to DEFAULT_NOM_FR
        defaultFamilleShouldNotBeFound("nomFr.notEquals=" + DEFAULT_NOM_FR);

        // Get all the familleList where nomFr not equals to UPDATED_NOM_FR
        defaultFamilleShouldBeFound("nomFr.notEquals=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllFamillesByNomFrIsInShouldWork() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where nomFr in DEFAULT_NOM_FR or UPDATED_NOM_FR
        defaultFamilleShouldBeFound("nomFr.in=" + DEFAULT_NOM_FR + "," + UPDATED_NOM_FR);

        // Get all the familleList where nomFr equals to UPDATED_NOM_FR
        defaultFamilleShouldNotBeFound("nomFr.in=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllFamillesByNomFrIsNullOrNotNull() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where nomFr is not null
        defaultFamilleShouldBeFound("nomFr.specified=true");

        // Get all the familleList where nomFr is null
        defaultFamilleShouldNotBeFound("nomFr.specified=false");
    }

    @Test
    @Transactional
    void getAllFamillesByNomFrContainsSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where nomFr contains DEFAULT_NOM_FR
        defaultFamilleShouldBeFound("nomFr.contains=" + DEFAULT_NOM_FR);

        // Get all the familleList where nomFr contains UPDATED_NOM_FR
        defaultFamilleShouldNotBeFound("nomFr.contains=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllFamillesByNomFrNotContainsSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where nomFr does not contain DEFAULT_NOM_FR
        defaultFamilleShouldNotBeFound("nomFr.doesNotContain=" + DEFAULT_NOM_FR);

        // Get all the familleList where nomFr does not contain UPDATED_NOM_FR
        defaultFamilleShouldBeFound("nomFr.doesNotContain=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllFamillesByNumeroDossierIsEqualToSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where numeroDossier equals to DEFAULT_NUMERO_DOSSIER
        defaultFamilleShouldBeFound("numeroDossier.equals=" + DEFAULT_NUMERO_DOSSIER);

        // Get all the familleList where numeroDossier equals to UPDATED_NUMERO_DOSSIER
        defaultFamilleShouldNotBeFound("numeroDossier.equals=" + UPDATED_NUMERO_DOSSIER);
    }

    @Test
    @Transactional
    void getAllFamillesByNumeroDossierIsNotEqualToSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where numeroDossier not equals to DEFAULT_NUMERO_DOSSIER
        defaultFamilleShouldNotBeFound("numeroDossier.notEquals=" + DEFAULT_NUMERO_DOSSIER);

        // Get all the familleList where numeroDossier not equals to UPDATED_NUMERO_DOSSIER
        defaultFamilleShouldBeFound("numeroDossier.notEquals=" + UPDATED_NUMERO_DOSSIER);
    }

    @Test
    @Transactional
    void getAllFamillesByNumeroDossierIsInShouldWork() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where numeroDossier in DEFAULT_NUMERO_DOSSIER or UPDATED_NUMERO_DOSSIER
        defaultFamilleShouldBeFound("numeroDossier.in=" + DEFAULT_NUMERO_DOSSIER + "," + UPDATED_NUMERO_DOSSIER);

        // Get all the familleList where numeroDossier equals to UPDATED_NUMERO_DOSSIER
        defaultFamilleShouldNotBeFound("numeroDossier.in=" + UPDATED_NUMERO_DOSSIER);
    }

    @Test
    @Transactional
    void getAllFamillesByNumeroDossierIsNullOrNotNull() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where numeroDossier is not null
        defaultFamilleShouldBeFound("numeroDossier.specified=true");

        // Get all the familleList where numeroDossier is null
        defaultFamilleShouldNotBeFound("numeroDossier.specified=false");
    }

    @Test
    @Transactional
    void getAllFamillesByNumeroDossierContainsSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where numeroDossier contains DEFAULT_NUMERO_DOSSIER
        defaultFamilleShouldBeFound("numeroDossier.contains=" + DEFAULT_NUMERO_DOSSIER);

        // Get all the familleList where numeroDossier contains UPDATED_NUMERO_DOSSIER
        defaultFamilleShouldNotBeFound("numeroDossier.contains=" + UPDATED_NUMERO_DOSSIER);
    }

    @Test
    @Transactional
    void getAllFamillesByNumeroDossierNotContainsSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where numeroDossier does not contain DEFAULT_NUMERO_DOSSIER
        defaultFamilleShouldNotBeFound("numeroDossier.doesNotContain=" + DEFAULT_NUMERO_DOSSIER);

        // Get all the familleList where numeroDossier does not contain UPDATED_NUMERO_DOSSIER
        defaultFamilleShouldBeFound("numeroDossier.doesNotContain=" + UPDATED_NUMERO_DOSSIER);
    }

    @Test
    @Transactional
    void getAllFamillesByPrenomIsEqualToSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where prenom equals to DEFAULT_PRENOM
        defaultFamilleShouldBeFound("prenom.equals=" + DEFAULT_PRENOM);

        // Get all the familleList where prenom equals to UPDATED_PRENOM
        defaultFamilleShouldNotBeFound("prenom.equals=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    void getAllFamillesByPrenomIsNotEqualToSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where prenom not equals to DEFAULT_PRENOM
        defaultFamilleShouldNotBeFound("prenom.notEquals=" + DEFAULT_PRENOM);

        // Get all the familleList where prenom not equals to UPDATED_PRENOM
        defaultFamilleShouldBeFound("prenom.notEquals=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    void getAllFamillesByPrenomIsInShouldWork() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where prenom in DEFAULT_PRENOM or UPDATED_PRENOM
        defaultFamilleShouldBeFound("prenom.in=" + DEFAULT_PRENOM + "," + UPDATED_PRENOM);

        // Get all the familleList where prenom equals to UPDATED_PRENOM
        defaultFamilleShouldNotBeFound("prenom.in=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    void getAllFamillesByPrenomIsNullOrNotNull() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where prenom is not null
        defaultFamilleShouldBeFound("prenom.specified=true");

        // Get all the familleList where prenom is null
        defaultFamilleShouldNotBeFound("prenom.specified=false");
    }

    @Test
    @Transactional
    void getAllFamillesByPrenomContainsSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where prenom contains DEFAULT_PRENOM
        defaultFamilleShouldBeFound("prenom.contains=" + DEFAULT_PRENOM);

        // Get all the familleList where prenom contains UPDATED_PRENOM
        defaultFamilleShouldNotBeFound("prenom.contains=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    void getAllFamillesByPrenomNotContainsSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where prenom does not contain DEFAULT_PRENOM
        defaultFamilleShouldNotBeFound("prenom.doesNotContain=" + DEFAULT_PRENOM);

        // Get all the familleList where prenom does not contain UPDATED_PRENOM
        defaultFamilleShouldBeFound("prenom.doesNotContain=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    void getAllFamillesByPrenomFrIsEqualToSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where prenomFr equals to DEFAULT_PRENOM_FR
        defaultFamilleShouldBeFound("prenomFr.equals=" + DEFAULT_PRENOM_FR);

        // Get all the familleList where prenomFr equals to UPDATED_PRENOM_FR
        defaultFamilleShouldNotBeFound("prenomFr.equals=" + UPDATED_PRENOM_FR);
    }

    @Test
    @Transactional
    void getAllFamillesByPrenomFrIsNotEqualToSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where prenomFr not equals to DEFAULT_PRENOM_FR
        defaultFamilleShouldNotBeFound("prenomFr.notEquals=" + DEFAULT_PRENOM_FR);

        // Get all the familleList where prenomFr not equals to UPDATED_PRENOM_FR
        defaultFamilleShouldBeFound("prenomFr.notEquals=" + UPDATED_PRENOM_FR);
    }

    @Test
    @Transactional
    void getAllFamillesByPrenomFrIsInShouldWork() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where prenomFr in DEFAULT_PRENOM_FR or UPDATED_PRENOM_FR
        defaultFamilleShouldBeFound("prenomFr.in=" + DEFAULT_PRENOM_FR + "," + UPDATED_PRENOM_FR);

        // Get all the familleList where prenomFr equals to UPDATED_PRENOM_FR
        defaultFamilleShouldNotBeFound("prenomFr.in=" + UPDATED_PRENOM_FR);
    }

    @Test
    @Transactional
    void getAllFamillesByPrenomFrIsNullOrNotNull() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where prenomFr is not null
        defaultFamilleShouldBeFound("prenomFr.specified=true");

        // Get all the familleList where prenomFr is null
        defaultFamilleShouldNotBeFound("prenomFr.specified=false");
    }

    @Test
    @Transactional
    void getAllFamillesByPrenomFrContainsSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where prenomFr contains DEFAULT_PRENOM_FR
        defaultFamilleShouldBeFound("prenomFr.contains=" + DEFAULT_PRENOM_FR);

        // Get all the familleList where prenomFr contains UPDATED_PRENOM_FR
        defaultFamilleShouldNotBeFound("prenomFr.contains=" + UPDATED_PRENOM_FR);
    }

    @Test
    @Transactional
    void getAllFamillesByPrenomFrNotContainsSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where prenomFr does not contain DEFAULT_PRENOM_FR
        defaultFamilleShouldNotBeFound("prenomFr.doesNotContain=" + DEFAULT_PRENOM_FR);

        // Get all the familleList where prenomFr does not contain UPDATED_PRENOM_FR
        defaultFamilleShouldBeFound("prenomFr.doesNotContain=" + UPDATED_PRENOM_FR);
    }

    @Test
    @Transactional
    void getAllFamillesByProfessionIsEqualToSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where profession equals to DEFAULT_PROFESSION
        defaultFamilleShouldBeFound("profession.equals=" + DEFAULT_PROFESSION);

        // Get all the familleList where profession equals to UPDATED_PROFESSION
        defaultFamilleShouldNotBeFound("profession.equals=" + UPDATED_PROFESSION);
    }

    @Test
    @Transactional
    void getAllFamillesByProfessionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where profession not equals to DEFAULT_PROFESSION
        defaultFamilleShouldNotBeFound("profession.notEquals=" + DEFAULT_PROFESSION);

        // Get all the familleList where profession not equals to UPDATED_PROFESSION
        defaultFamilleShouldBeFound("profession.notEquals=" + UPDATED_PROFESSION);
    }

    @Test
    @Transactional
    void getAllFamillesByProfessionIsInShouldWork() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where profession in DEFAULT_PROFESSION or UPDATED_PROFESSION
        defaultFamilleShouldBeFound("profession.in=" + DEFAULT_PROFESSION + "," + UPDATED_PROFESSION);

        // Get all the familleList where profession equals to UPDATED_PROFESSION
        defaultFamilleShouldNotBeFound("profession.in=" + UPDATED_PROFESSION);
    }

    @Test
    @Transactional
    void getAllFamillesByProfessionIsNullOrNotNull() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where profession is not null
        defaultFamilleShouldBeFound("profession.specified=true");

        // Get all the familleList where profession is null
        defaultFamilleShouldNotBeFound("profession.specified=false");
    }

    @Test
    @Transactional
    void getAllFamillesByProfessionContainsSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where profession contains DEFAULT_PROFESSION
        defaultFamilleShouldBeFound("profession.contains=" + DEFAULT_PROFESSION);

        // Get all the familleList where profession contains UPDATED_PROFESSION
        defaultFamilleShouldNotBeFound("profession.contains=" + UPDATED_PROFESSION);
    }

    @Test
    @Transactional
    void getAllFamillesByProfessionNotContainsSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where profession does not contain DEFAULT_PROFESSION
        defaultFamilleShouldNotBeFound("profession.doesNotContain=" + DEFAULT_PROFESSION);

        // Get all the familleList where profession does not contain UPDATED_PROFESSION
        defaultFamilleShouldBeFound("profession.doesNotContain=" + UPDATED_PROFESSION);
    }

    @Test
    @Transactional
    void getAllFamillesBySelectionnerIsEqualToSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where selectionner equals to DEFAULT_SELECTIONNER
        defaultFamilleShouldBeFound("selectionner.equals=" + DEFAULT_SELECTIONNER);

        // Get all the familleList where selectionner equals to UPDATED_SELECTIONNER
        defaultFamilleShouldNotBeFound("selectionner.equals=" + UPDATED_SELECTIONNER);
    }

    @Test
    @Transactional
    void getAllFamillesBySelectionnerIsNotEqualToSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where selectionner not equals to DEFAULT_SELECTIONNER
        defaultFamilleShouldNotBeFound("selectionner.notEquals=" + DEFAULT_SELECTIONNER);

        // Get all the familleList where selectionner not equals to UPDATED_SELECTIONNER
        defaultFamilleShouldBeFound("selectionner.notEquals=" + UPDATED_SELECTIONNER);
    }

    @Test
    @Transactional
    void getAllFamillesBySelectionnerIsInShouldWork() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where selectionner in DEFAULT_SELECTIONNER or UPDATED_SELECTIONNER
        defaultFamilleShouldBeFound("selectionner.in=" + DEFAULT_SELECTIONNER + "," + UPDATED_SELECTIONNER);

        // Get all the familleList where selectionner equals to UPDATED_SELECTIONNER
        defaultFamilleShouldNotBeFound("selectionner.in=" + UPDATED_SELECTIONNER);
    }

    @Test
    @Transactional
    void getAllFamillesBySelectionnerIsNullOrNotNull() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where selectionner is not null
        defaultFamilleShouldBeFound("selectionner.specified=true");

        // Get all the familleList where selectionner is null
        defaultFamilleShouldNotBeFound("selectionner.specified=false");
    }

    @Test
    @Transactional
    void getAllFamillesBySelectionnerIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where selectionner is greater than or equal to DEFAULT_SELECTIONNER
        defaultFamilleShouldBeFound("selectionner.greaterThanOrEqual=" + DEFAULT_SELECTIONNER);

        // Get all the familleList where selectionner is greater than or equal to UPDATED_SELECTIONNER
        defaultFamilleShouldNotBeFound("selectionner.greaterThanOrEqual=" + UPDATED_SELECTIONNER);
    }

    @Test
    @Transactional
    void getAllFamillesBySelectionnerIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where selectionner is less than or equal to DEFAULT_SELECTIONNER
        defaultFamilleShouldBeFound("selectionner.lessThanOrEqual=" + DEFAULT_SELECTIONNER);

        // Get all the familleList where selectionner is less than or equal to SMALLER_SELECTIONNER
        defaultFamilleShouldNotBeFound("selectionner.lessThanOrEqual=" + SMALLER_SELECTIONNER);
    }

    @Test
    @Transactional
    void getAllFamillesBySelectionnerIsLessThanSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where selectionner is less than DEFAULT_SELECTIONNER
        defaultFamilleShouldNotBeFound("selectionner.lessThan=" + DEFAULT_SELECTIONNER);

        // Get all the familleList where selectionner is less than UPDATED_SELECTIONNER
        defaultFamilleShouldBeFound("selectionner.lessThan=" + UPDATED_SELECTIONNER);
    }

    @Test
    @Transactional
    void getAllFamillesBySelectionnerIsGreaterThanSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where selectionner is greater than DEFAULT_SELECTIONNER
        defaultFamilleShouldNotBeFound("selectionner.greaterThan=" + DEFAULT_SELECTIONNER);

        // Get all the familleList where selectionner is greater than SMALLER_SELECTIONNER
        defaultFamilleShouldBeFound("selectionner.greaterThan=" + SMALLER_SELECTIONNER);
    }

    @Test
    @Transactional
    void getAllFamillesBySexeIsEqualToSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where sexe equals to DEFAULT_SEXE
        defaultFamilleShouldBeFound("sexe.equals=" + DEFAULT_SEXE);

        // Get all the familleList where sexe equals to UPDATED_SEXE
        defaultFamilleShouldNotBeFound("sexe.equals=" + UPDATED_SEXE);
    }

    @Test
    @Transactional
    void getAllFamillesBySexeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where sexe not equals to DEFAULT_SEXE
        defaultFamilleShouldNotBeFound("sexe.notEquals=" + DEFAULT_SEXE);

        // Get all the familleList where sexe not equals to UPDATED_SEXE
        defaultFamilleShouldBeFound("sexe.notEquals=" + UPDATED_SEXE);
    }

    @Test
    @Transactional
    void getAllFamillesBySexeIsInShouldWork() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where sexe in DEFAULT_SEXE or UPDATED_SEXE
        defaultFamilleShouldBeFound("sexe.in=" + DEFAULT_SEXE + "," + UPDATED_SEXE);

        // Get all the familleList where sexe equals to UPDATED_SEXE
        defaultFamilleShouldNotBeFound("sexe.in=" + UPDATED_SEXE);
    }

    @Test
    @Transactional
    void getAllFamillesBySexeIsNullOrNotNull() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where sexe is not null
        defaultFamilleShouldBeFound("sexe.specified=true");

        // Get all the familleList where sexe is null
        defaultFamilleShouldNotBeFound("sexe.specified=false");
    }

    @Test
    @Transactional
    void getAllFamillesBySexeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where sexe is greater than or equal to DEFAULT_SEXE
        defaultFamilleShouldBeFound("sexe.greaterThanOrEqual=" + DEFAULT_SEXE);

        // Get all the familleList where sexe is greater than or equal to UPDATED_SEXE
        defaultFamilleShouldNotBeFound("sexe.greaterThanOrEqual=" + UPDATED_SEXE);
    }

    @Test
    @Transactional
    void getAllFamillesBySexeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where sexe is less than or equal to DEFAULT_SEXE
        defaultFamilleShouldBeFound("sexe.lessThanOrEqual=" + DEFAULT_SEXE);

        // Get all the familleList where sexe is less than or equal to SMALLER_SEXE
        defaultFamilleShouldNotBeFound("sexe.lessThanOrEqual=" + SMALLER_SEXE);
    }

    @Test
    @Transactional
    void getAllFamillesBySexeIsLessThanSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where sexe is less than DEFAULT_SEXE
        defaultFamilleShouldNotBeFound("sexe.lessThan=" + DEFAULT_SEXE);

        // Get all the familleList where sexe is less than UPDATED_SEXE
        defaultFamilleShouldBeFound("sexe.lessThan=" + UPDATED_SEXE);
    }

    @Test
    @Transactional
    void getAllFamillesBySexeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where sexe is greater than DEFAULT_SEXE
        defaultFamilleShouldNotBeFound("sexe.greaterThan=" + DEFAULT_SEXE);

        // Get all the familleList where sexe is greater than SMALLER_SEXE
        defaultFamilleShouldBeFound("sexe.greaterThan=" + SMALLER_SEXE);
    }

    @Test
    @Transactional
    void getAllFamillesByTelephoneIsEqualToSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where telephone equals to DEFAULT_TELEPHONE
        defaultFamilleShouldBeFound("telephone.equals=" + DEFAULT_TELEPHONE);

        // Get all the familleList where telephone equals to UPDATED_TELEPHONE
        defaultFamilleShouldNotBeFound("telephone.equals=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllFamillesByTelephoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where telephone not equals to DEFAULT_TELEPHONE
        defaultFamilleShouldNotBeFound("telephone.notEquals=" + DEFAULT_TELEPHONE);

        // Get all the familleList where telephone not equals to UPDATED_TELEPHONE
        defaultFamilleShouldBeFound("telephone.notEquals=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllFamillesByTelephoneIsInShouldWork() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where telephone in DEFAULT_TELEPHONE or UPDATED_TELEPHONE
        defaultFamilleShouldBeFound("telephone.in=" + DEFAULT_TELEPHONE + "," + UPDATED_TELEPHONE);

        // Get all the familleList where telephone equals to UPDATED_TELEPHONE
        defaultFamilleShouldNotBeFound("telephone.in=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllFamillesByTelephoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where telephone is not null
        defaultFamilleShouldBeFound("telephone.specified=true");

        // Get all the familleList where telephone is null
        defaultFamilleShouldNotBeFound("telephone.specified=false");
    }

    @Test
    @Transactional
    void getAllFamillesByTelephoneContainsSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where telephone contains DEFAULT_TELEPHONE
        defaultFamilleShouldBeFound("telephone.contains=" + DEFAULT_TELEPHONE);

        // Get all the familleList where telephone contains UPDATED_TELEPHONE
        defaultFamilleShouldNotBeFound("telephone.contains=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllFamillesByTelephoneNotContainsSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where telephone does not contain DEFAULT_TELEPHONE
        defaultFamilleShouldNotBeFound("telephone.doesNotContain=" + DEFAULT_TELEPHONE);

        // Get all the familleList where telephone does not contain UPDATED_TELEPHONE
        defaultFamilleShouldBeFound("telephone.doesNotContain=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllFamillesByAutreBenef2019IsEqualToSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where autreBenef2019 equals to DEFAULT_AUTRE_BENEF_2019
        defaultFamilleShouldBeFound("autreBenef2019.equals=" + DEFAULT_AUTRE_BENEF_2019);

        // Get all the familleList where autreBenef2019 equals to UPDATED_AUTRE_BENEF_2019
        defaultFamilleShouldNotBeFound("autreBenef2019.equals=" + UPDATED_AUTRE_BENEF_2019);
    }

    @Test
    @Transactional
    void getAllFamillesByAutreBenef2019IsNotEqualToSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where autreBenef2019 not equals to DEFAULT_AUTRE_BENEF_2019
        defaultFamilleShouldNotBeFound("autreBenef2019.notEquals=" + DEFAULT_AUTRE_BENEF_2019);

        // Get all the familleList where autreBenef2019 not equals to UPDATED_AUTRE_BENEF_2019
        defaultFamilleShouldBeFound("autreBenef2019.notEquals=" + UPDATED_AUTRE_BENEF_2019);
    }

    @Test
    @Transactional
    void getAllFamillesByAutreBenef2019IsInShouldWork() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where autreBenef2019 in DEFAULT_AUTRE_BENEF_2019 or UPDATED_AUTRE_BENEF_2019
        defaultFamilleShouldBeFound("autreBenef2019.in=" + DEFAULT_AUTRE_BENEF_2019 + "," + UPDATED_AUTRE_BENEF_2019);

        // Get all the familleList where autreBenef2019 equals to UPDATED_AUTRE_BENEF_2019
        defaultFamilleShouldNotBeFound("autreBenef2019.in=" + UPDATED_AUTRE_BENEF_2019);
    }

    @Test
    @Transactional
    void getAllFamillesByAutreBenef2019IsNullOrNotNull() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where autreBenef2019 is not null
        defaultFamilleShouldBeFound("autreBenef2019.specified=true");

        // Get all the familleList where autreBenef2019 is null
        defaultFamilleShouldNotBeFound("autreBenef2019.specified=false");
    }

    @Test
    @Transactional
    void getAllFamillesByAutreBenef2019IsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where autreBenef2019 is greater than or equal to DEFAULT_AUTRE_BENEF_2019
        defaultFamilleShouldBeFound("autreBenef2019.greaterThanOrEqual=" + DEFAULT_AUTRE_BENEF_2019);

        // Get all the familleList where autreBenef2019 is greater than or equal to UPDATED_AUTRE_BENEF_2019
        defaultFamilleShouldNotBeFound("autreBenef2019.greaterThanOrEqual=" + UPDATED_AUTRE_BENEF_2019);
    }

    @Test
    @Transactional
    void getAllFamillesByAutreBenef2019IsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where autreBenef2019 is less than or equal to DEFAULT_AUTRE_BENEF_2019
        defaultFamilleShouldBeFound("autreBenef2019.lessThanOrEqual=" + DEFAULT_AUTRE_BENEF_2019);

        // Get all the familleList where autreBenef2019 is less than or equal to SMALLER_AUTRE_BENEF_2019
        defaultFamilleShouldNotBeFound("autreBenef2019.lessThanOrEqual=" + SMALLER_AUTRE_BENEF_2019);
    }

    @Test
    @Transactional
    void getAllFamillesByAutreBenef2019IsLessThanSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where autreBenef2019 is less than DEFAULT_AUTRE_BENEF_2019
        defaultFamilleShouldNotBeFound("autreBenef2019.lessThan=" + DEFAULT_AUTRE_BENEF_2019);

        // Get all the familleList where autreBenef2019 is less than UPDATED_AUTRE_BENEF_2019
        defaultFamilleShouldBeFound("autreBenef2019.lessThan=" + UPDATED_AUTRE_BENEF_2019);
    }

    @Test
    @Transactional
    void getAllFamillesByAutreBenef2019IsGreaterThanSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where autreBenef2019 is greater than DEFAULT_AUTRE_BENEF_2019
        defaultFamilleShouldNotBeFound("autreBenef2019.greaterThan=" + DEFAULT_AUTRE_BENEF_2019);

        // Get all the familleList where autreBenef2019 is greater than SMALLER_AUTRE_BENEF_2019
        defaultFamilleShouldBeFound("autreBenef2019.greaterThan=" + SMALLER_AUTRE_BENEF_2019);
    }

    @Test
    @Transactional
    void getAllFamillesByAutreBenef2020IsEqualToSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where autreBenef2020 equals to DEFAULT_AUTRE_BENEF_2020
        defaultFamilleShouldBeFound("autreBenef2020.equals=" + DEFAULT_AUTRE_BENEF_2020);

        // Get all the familleList where autreBenef2020 equals to UPDATED_AUTRE_BENEF_2020
        defaultFamilleShouldNotBeFound("autreBenef2020.equals=" + UPDATED_AUTRE_BENEF_2020);
    }

    @Test
    @Transactional
    void getAllFamillesByAutreBenef2020IsNotEqualToSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where autreBenef2020 not equals to DEFAULT_AUTRE_BENEF_2020
        defaultFamilleShouldNotBeFound("autreBenef2020.notEquals=" + DEFAULT_AUTRE_BENEF_2020);

        // Get all the familleList where autreBenef2020 not equals to UPDATED_AUTRE_BENEF_2020
        defaultFamilleShouldBeFound("autreBenef2020.notEquals=" + UPDATED_AUTRE_BENEF_2020);
    }

    @Test
    @Transactional
    void getAllFamillesByAutreBenef2020IsInShouldWork() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where autreBenef2020 in DEFAULT_AUTRE_BENEF_2020 or UPDATED_AUTRE_BENEF_2020
        defaultFamilleShouldBeFound("autreBenef2020.in=" + DEFAULT_AUTRE_BENEF_2020 + "," + UPDATED_AUTRE_BENEF_2020);

        // Get all the familleList where autreBenef2020 equals to UPDATED_AUTRE_BENEF_2020
        defaultFamilleShouldNotBeFound("autreBenef2020.in=" + UPDATED_AUTRE_BENEF_2020);
    }

    @Test
    @Transactional
    void getAllFamillesByAutreBenef2020IsNullOrNotNull() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where autreBenef2020 is not null
        defaultFamilleShouldBeFound("autreBenef2020.specified=true");

        // Get all the familleList where autreBenef2020 is null
        defaultFamilleShouldNotBeFound("autreBenef2020.specified=false");
    }

    @Test
    @Transactional
    void getAllFamillesByAutreBenef2020IsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where autreBenef2020 is greater than or equal to DEFAULT_AUTRE_BENEF_2020
        defaultFamilleShouldBeFound("autreBenef2020.greaterThanOrEqual=" + DEFAULT_AUTRE_BENEF_2020);

        // Get all the familleList where autreBenef2020 is greater than or equal to UPDATED_AUTRE_BENEF_2020
        defaultFamilleShouldNotBeFound("autreBenef2020.greaterThanOrEqual=" + UPDATED_AUTRE_BENEF_2020);
    }

    @Test
    @Transactional
    void getAllFamillesByAutreBenef2020IsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where autreBenef2020 is less than or equal to DEFAULT_AUTRE_BENEF_2020
        defaultFamilleShouldBeFound("autreBenef2020.lessThanOrEqual=" + DEFAULT_AUTRE_BENEF_2020);

        // Get all the familleList where autreBenef2020 is less than or equal to SMALLER_AUTRE_BENEF_2020
        defaultFamilleShouldNotBeFound("autreBenef2020.lessThanOrEqual=" + SMALLER_AUTRE_BENEF_2020);
    }

    @Test
    @Transactional
    void getAllFamillesByAutreBenef2020IsLessThanSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where autreBenef2020 is less than DEFAULT_AUTRE_BENEF_2020
        defaultFamilleShouldNotBeFound("autreBenef2020.lessThan=" + DEFAULT_AUTRE_BENEF_2020);

        // Get all the familleList where autreBenef2020 is less than UPDATED_AUTRE_BENEF_2020
        defaultFamilleShouldBeFound("autreBenef2020.lessThan=" + UPDATED_AUTRE_BENEF_2020);
    }

    @Test
    @Transactional
    void getAllFamillesByAutreBenef2020IsGreaterThanSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where autreBenef2020 is greater than DEFAULT_AUTRE_BENEF_2020
        defaultFamilleShouldNotBeFound("autreBenef2020.greaterThan=" + DEFAULT_AUTRE_BENEF_2020);

        // Get all the familleList where autreBenef2020 is greater than SMALLER_AUTRE_BENEF_2020
        defaultFamilleShouldBeFound("autreBenef2020.greaterThan=" + SMALLER_AUTRE_BENEF_2020);
    }

    @Test
    @Transactional
    void getAllFamillesByRelationFamilialeIsEqualToSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where relationFamiliale equals to DEFAULT_RELATION_FAMILIALE
        defaultFamilleShouldBeFound("relationFamiliale.equals=" + DEFAULT_RELATION_FAMILIALE);

        // Get all the familleList where relationFamiliale equals to UPDATED_RELATION_FAMILIALE
        defaultFamilleShouldNotBeFound("relationFamiliale.equals=" + UPDATED_RELATION_FAMILIALE);
    }

    @Test
    @Transactional
    void getAllFamillesByRelationFamilialeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where relationFamiliale not equals to DEFAULT_RELATION_FAMILIALE
        defaultFamilleShouldNotBeFound("relationFamiliale.notEquals=" + DEFAULT_RELATION_FAMILIALE);

        // Get all the familleList where relationFamiliale not equals to UPDATED_RELATION_FAMILIALE
        defaultFamilleShouldBeFound("relationFamiliale.notEquals=" + UPDATED_RELATION_FAMILIALE);
    }

    @Test
    @Transactional
    void getAllFamillesByRelationFamilialeIsInShouldWork() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where relationFamiliale in DEFAULT_RELATION_FAMILIALE or UPDATED_RELATION_FAMILIALE
        defaultFamilleShouldBeFound("relationFamiliale.in=" + DEFAULT_RELATION_FAMILIALE + "," + UPDATED_RELATION_FAMILIALE);

        // Get all the familleList where relationFamiliale equals to UPDATED_RELATION_FAMILIALE
        defaultFamilleShouldNotBeFound("relationFamiliale.in=" + UPDATED_RELATION_FAMILIALE);
    }

    @Test
    @Transactional
    void getAllFamillesByRelationFamilialeIsNullOrNotNull() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where relationFamiliale is not null
        defaultFamilleShouldBeFound("relationFamiliale.specified=true");

        // Get all the familleList where relationFamiliale is null
        defaultFamilleShouldNotBeFound("relationFamiliale.specified=false");
    }

    @Test
    @Transactional
    void getAllFamillesByRelationFamilialeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where relationFamiliale is greater than or equal to DEFAULT_RELATION_FAMILIALE
        defaultFamilleShouldBeFound("relationFamiliale.greaterThanOrEqual=" + DEFAULT_RELATION_FAMILIALE);

        // Get all the familleList where relationFamiliale is greater than or equal to UPDATED_RELATION_FAMILIALE
        defaultFamilleShouldNotBeFound("relationFamiliale.greaterThanOrEqual=" + UPDATED_RELATION_FAMILIALE);
    }

    @Test
    @Transactional
    void getAllFamillesByRelationFamilialeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where relationFamiliale is less than or equal to DEFAULT_RELATION_FAMILIALE
        defaultFamilleShouldBeFound("relationFamiliale.lessThanOrEqual=" + DEFAULT_RELATION_FAMILIALE);

        // Get all the familleList where relationFamiliale is less than or equal to SMALLER_RELATION_FAMILIALE
        defaultFamilleShouldNotBeFound("relationFamiliale.lessThanOrEqual=" + SMALLER_RELATION_FAMILIALE);
    }

    @Test
    @Transactional
    void getAllFamillesByRelationFamilialeIsLessThanSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where relationFamiliale is less than DEFAULT_RELATION_FAMILIALE
        defaultFamilleShouldNotBeFound("relationFamiliale.lessThan=" + DEFAULT_RELATION_FAMILIALE);

        // Get all the familleList where relationFamiliale is less than UPDATED_RELATION_FAMILIALE
        defaultFamilleShouldBeFound("relationFamiliale.lessThan=" + UPDATED_RELATION_FAMILIALE);
    }

    @Test
    @Transactional
    void getAllFamillesByRelationFamilialeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where relationFamiliale is greater than DEFAULT_RELATION_FAMILIALE
        defaultFamilleShouldNotBeFound("relationFamiliale.greaterThan=" + DEFAULT_RELATION_FAMILIALE);

        // Get all the familleList where relationFamiliale is greater than SMALLER_RELATION_FAMILIALE
        defaultFamilleShouldBeFound("relationFamiliale.greaterThan=" + SMALLER_RELATION_FAMILIALE);
    }

    @Test
    @Transactional
    void getAllFamillesByAppUserIsEqualToSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);
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
        famille.setAppUser(appUser);
        familleRepository.saveAndFlush(famille);
        Long appUserId = appUser.getId();

        // Get all the familleList where appUser equals to appUserId
        defaultFamilleShouldBeFound("appUserId.equals=" + appUserId);

        // Get all the familleList where appUser equals to (appUserId + 1)
        defaultFamilleShouldNotBeFound("appUserId.equals=" + (appUserId + 1));
    }

    @Test
    @Transactional
    void getAllFamillesByEnfantIsEqualToSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);
        Enfant enfant;
        if (TestUtil.findAll(em, Enfant.class).isEmpty()) {
            enfant = EnfantResourceIT.createEntity(em);
            em.persist(enfant);
            em.flush();
        } else {
            enfant = TestUtil.findAll(em, Enfant.class).get(0);
        }
        em.persist(enfant);
        em.flush();
        famille.setEnfant(enfant);
        familleRepository.saveAndFlush(famille);
        Long enfantId = enfant.getId();

        // Get all the familleList where enfant equals to enfantId
        defaultFamilleShouldBeFound("enfantId.equals=" + enfantId);

        // Get all the familleList where enfant equals to (enfantId + 1)
        defaultFamilleShouldNotBeFound("enfantId.equals=" + (enfantId + 1));
    }

    @Test
    @Transactional
    void getAllFamillesByMotifRefusIsEqualToSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);
        MotifRefus motifRefus;
        if (TestUtil.findAll(em, MotifRefus.class).isEmpty()) {
            motifRefus = MotifRefusResourceIT.createEntity(em);
            em.persist(motifRefus);
            em.flush();
        } else {
            motifRefus = TestUtil.findAll(em, MotifRefus.class).get(0);
        }
        em.persist(motifRefus);
        em.flush();
        famille.setMotifRefus(motifRefus);
        familleRepository.saveAndFlush(famille);
        Long motifRefusId = motifRefus.getId();

        // Get all the familleList where motifRefus equals to motifRefusId
        defaultFamilleShouldBeFound("motifRefusId.equals=" + motifRefusId);

        // Get all the familleList where motifRefus equals to (motifRefusId + 1)
        defaultFamilleShouldNotBeFound("motifRefusId.equals=" + (motifRefusId + 1));
    }

    @Test
    @Transactional
    void getAllFamillesByProvinceIsEqualToSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);
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
        famille.setProvince(province);
        familleRepository.saveAndFlush(famille);
        Long provinceId = province.getId();

        // Get all the familleList where province equals to provinceId
        defaultFamilleShouldBeFound("provinceId.equals=" + provinceId);

        // Get all the familleList where province equals to (provinceId + 1)
        defaultFamilleShouldNotBeFound("provinceId.equals=" + (provinceId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFamilleShouldBeFound(String filter) throws Exception {
        restFamilleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(famille.getId().intValue())))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE)))
            .andExpect(jsonPath("$.[*].benef2019").value(hasItem(DEFAULT_BENEF_2019)))
            .andExpect(jsonPath("$.[*].benef2020").value(hasItem(DEFAULT_BENEF_2020)))
            .andExpect(jsonPath("$.[*].cin").value(hasItem(DEFAULT_CIN)))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(DEFAULT_DATE_CREATION.toString())))
            .andExpect(jsonPath("$.[*].dateModification").value(hasItem(DEFAULT_DATE_MODIFICATION.toString())))
            .andExpect(jsonPath("$.[*].dateNaissance").value(hasItem(DEFAULT_DATE_NAISSANCE.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].etat").value(hasItem(DEFAULT_ETAT)))
            .andExpect(jsonPath("$.[*].explicationRefus").value(hasItem(DEFAULT_EXPLICATION_REFUS)))
            .andExpect(jsonPath("$.[*].nbrEnfants").value(hasItem(DEFAULT_NBR_ENFANTS)))
            .andExpect(jsonPath("$.[*].niveauScolarite").value(hasItem(DEFAULT_NIVEAU_SCOLARITE)))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].nomFr").value(hasItem(DEFAULT_NOM_FR)))
            .andExpect(jsonPath("$.[*].numeroDossier").value(hasItem(DEFAULT_NUMERO_DOSSIER)))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].prenomFr").value(hasItem(DEFAULT_PRENOM_FR)))
            .andExpect(jsonPath("$.[*].profession").value(hasItem(DEFAULT_PROFESSION)))
            .andExpect(jsonPath("$.[*].selectionner").value(hasItem(DEFAULT_SELECTIONNER)))
            .andExpect(jsonPath("$.[*].sexe").value(hasItem(DEFAULT_SEXE)))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)))
            .andExpect(jsonPath("$.[*].autreBenef2019").value(hasItem(DEFAULT_AUTRE_BENEF_2019)))
            .andExpect(jsonPath("$.[*].autreBenef2020").value(hasItem(DEFAULT_AUTRE_BENEF_2020)))
            .andExpect(jsonPath("$.[*].relationFamiliale").value(hasItem(DEFAULT_RELATION_FAMILIALE)));

        // Check, that the count call also returns 1
        restFamilleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFamilleShouldNotBeFound(String filter) throws Exception {
        restFamilleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFamilleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFamille() throws Exception {
        // Get the famille
        restFamilleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFamille() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        int databaseSizeBeforeUpdate = familleRepository.findAll().size();

        // Update the famille
        Famille updatedFamille = familleRepository.findById(famille.getId()).get();
        // Disconnect from session so that the updates on updatedFamille are not directly saved in db
        em.detach(updatedFamille);
        updatedFamille
            .adresse(UPDATED_ADRESSE)
            .benef2019(UPDATED_BENEF_2019)
            .benef2020(UPDATED_BENEF_2020)
            .cin(UPDATED_CIN)
            .dateCreation(UPDATED_DATE_CREATION)
            .dateModification(UPDATED_DATE_MODIFICATION)
            .dateNaissance(UPDATED_DATE_NAISSANCE)
            .email(UPDATED_EMAIL)
            .etat(UPDATED_ETAT)
            .explicationRefus(UPDATED_EXPLICATION_REFUS)
            .nbrEnfants(UPDATED_NBR_ENFANTS)
            .niveauScolarite(UPDATED_NIVEAU_SCOLARITE)
            .nom(UPDATED_NOM)
            .nomFr(UPDATED_NOM_FR)
            .numeroDossier(UPDATED_NUMERO_DOSSIER)
            .prenom(UPDATED_PRENOM)
            .prenomFr(UPDATED_PRENOM_FR)
            .profession(UPDATED_PROFESSION)
            .selectionner(UPDATED_SELECTIONNER)
            .sexe(UPDATED_SEXE)
            .telephone(UPDATED_TELEPHONE)
            .autreBenef2019(UPDATED_AUTRE_BENEF_2019)
            .autreBenef2020(UPDATED_AUTRE_BENEF_2020)
            .relationFamiliale(UPDATED_RELATION_FAMILIALE);
        FamilleDTO familleDTO = familleMapper.toDto(updatedFamille);

        restFamilleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, familleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(familleDTO))
            )
            .andExpect(status().isOk());

        // Validate the Famille in the database
        List<Famille> familleList = familleRepository.findAll();
        assertThat(familleList).hasSize(databaseSizeBeforeUpdate);
        Famille testFamille = familleList.get(familleList.size() - 1);
        assertThat(testFamille.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testFamille.getBenef2019()).isEqualTo(UPDATED_BENEF_2019);
        assertThat(testFamille.getBenef2020()).isEqualTo(UPDATED_BENEF_2020);
        assertThat(testFamille.getCin()).isEqualTo(UPDATED_CIN);
        assertThat(testFamille.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
        assertThat(testFamille.getDateModification()).isEqualTo(UPDATED_DATE_MODIFICATION);
        assertThat(testFamille.getDateNaissance()).isEqualTo(UPDATED_DATE_NAISSANCE);
        assertThat(testFamille.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testFamille.getEtat()).isEqualTo(UPDATED_ETAT);
        assertThat(testFamille.getExplicationRefus()).isEqualTo(UPDATED_EXPLICATION_REFUS);
        assertThat(testFamille.getNbrEnfants()).isEqualTo(UPDATED_NBR_ENFANTS);
        assertThat(testFamille.getNiveauScolarite()).isEqualTo(UPDATED_NIVEAU_SCOLARITE);
        assertThat(testFamille.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testFamille.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testFamille.getNumeroDossier()).isEqualTo(UPDATED_NUMERO_DOSSIER);
        assertThat(testFamille.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testFamille.getPrenomFr()).isEqualTo(UPDATED_PRENOM_FR);
        assertThat(testFamille.getProfession()).isEqualTo(UPDATED_PROFESSION);
        assertThat(testFamille.getSelectionner()).isEqualTo(UPDATED_SELECTIONNER);
        assertThat(testFamille.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testFamille.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testFamille.getAutreBenef2019()).isEqualTo(UPDATED_AUTRE_BENEF_2019);
        assertThat(testFamille.getAutreBenef2020()).isEqualTo(UPDATED_AUTRE_BENEF_2020);
        assertThat(testFamille.getRelationFamiliale()).isEqualTo(UPDATED_RELATION_FAMILIALE);
    }

    @Test
    @Transactional
    void putNonExistingFamille() throws Exception {
        int databaseSizeBeforeUpdate = familleRepository.findAll().size();
        famille.setId(count.incrementAndGet());

        // Create the Famille
        FamilleDTO familleDTO = familleMapper.toDto(famille);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFamilleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, familleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(familleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Famille in the database
        List<Famille> familleList = familleRepository.findAll();
        assertThat(familleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFamille() throws Exception {
        int databaseSizeBeforeUpdate = familleRepository.findAll().size();
        famille.setId(count.incrementAndGet());

        // Create the Famille
        FamilleDTO familleDTO = familleMapper.toDto(famille);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFamilleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(familleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Famille in the database
        List<Famille> familleList = familleRepository.findAll();
        assertThat(familleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFamille() throws Exception {
        int databaseSizeBeforeUpdate = familleRepository.findAll().size();
        famille.setId(count.incrementAndGet());

        // Create the Famille
        FamilleDTO familleDTO = familleMapper.toDto(famille);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFamilleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(familleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Famille in the database
        List<Famille> familleList = familleRepository.findAll();
        assertThat(familleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFamilleWithPatch() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        int databaseSizeBeforeUpdate = familleRepository.findAll().size();

        // Update the famille using partial update
        Famille partialUpdatedFamille = new Famille();
        partialUpdatedFamille.setId(famille.getId());

        partialUpdatedFamille
            .benef2019(UPDATED_BENEF_2019)
            .benef2020(UPDATED_BENEF_2020)
            .cin(UPDATED_CIN)
            .dateCreation(UPDATED_DATE_CREATION)
            .email(UPDATED_EMAIL)
            .explicationRefus(UPDATED_EXPLICATION_REFUS)
            .nbrEnfants(UPDATED_NBR_ENFANTS)
            .nom(UPDATED_NOM)
            .prenomFr(UPDATED_PRENOM_FR)
            .sexe(UPDATED_SEXE)
            .telephone(UPDATED_TELEPHONE)
            .autreBenef2020(UPDATED_AUTRE_BENEF_2020)
            .relationFamiliale(UPDATED_RELATION_FAMILIALE);

        restFamilleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFamille.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFamille))
            )
            .andExpect(status().isOk());

        // Validate the Famille in the database
        List<Famille> familleList = familleRepository.findAll();
        assertThat(familleList).hasSize(databaseSizeBeforeUpdate);
        Famille testFamille = familleList.get(familleList.size() - 1);
        assertThat(testFamille.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testFamille.getBenef2019()).isEqualTo(UPDATED_BENEF_2019);
        assertThat(testFamille.getBenef2020()).isEqualTo(UPDATED_BENEF_2020);
        assertThat(testFamille.getCin()).isEqualTo(UPDATED_CIN);
        assertThat(testFamille.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
        assertThat(testFamille.getDateModification()).isEqualTo(DEFAULT_DATE_MODIFICATION);
        assertThat(testFamille.getDateNaissance()).isEqualTo(DEFAULT_DATE_NAISSANCE);
        assertThat(testFamille.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testFamille.getEtat()).isEqualTo(DEFAULT_ETAT);
        assertThat(testFamille.getExplicationRefus()).isEqualTo(UPDATED_EXPLICATION_REFUS);
        assertThat(testFamille.getNbrEnfants()).isEqualTo(UPDATED_NBR_ENFANTS);
        assertThat(testFamille.getNiveauScolarite()).isEqualTo(DEFAULT_NIVEAU_SCOLARITE);
        assertThat(testFamille.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testFamille.getNomFr()).isEqualTo(DEFAULT_NOM_FR);
        assertThat(testFamille.getNumeroDossier()).isEqualTo(DEFAULT_NUMERO_DOSSIER);
        assertThat(testFamille.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testFamille.getPrenomFr()).isEqualTo(UPDATED_PRENOM_FR);
        assertThat(testFamille.getProfession()).isEqualTo(DEFAULT_PROFESSION);
        assertThat(testFamille.getSelectionner()).isEqualTo(DEFAULT_SELECTIONNER);
        assertThat(testFamille.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testFamille.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testFamille.getAutreBenef2019()).isEqualTo(DEFAULT_AUTRE_BENEF_2019);
        assertThat(testFamille.getAutreBenef2020()).isEqualTo(UPDATED_AUTRE_BENEF_2020);
        assertThat(testFamille.getRelationFamiliale()).isEqualTo(UPDATED_RELATION_FAMILIALE);
    }

    @Test
    @Transactional
    void fullUpdateFamilleWithPatch() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        int databaseSizeBeforeUpdate = familleRepository.findAll().size();

        // Update the famille using partial update
        Famille partialUpdatedFamille = new Famille();
        partialUpdatedFamille.setId(famille.getId());

        partialUpdatedFamille
            .adresse(UPDATED_ADRESSE)
            .benef2019(UPDATED_BENEF_2019)
            .benef2020(UPDATED_BENEF_2020)
            .cin(UPDATED_CIN)
            .dateCreation(UPDATED_DATE_CREATION)
            .dateModification(UPDATED_DATE_MODIFICATION)
            .dateNaissance(UPDATED_DATE_NAISSANCE)
            .email(UPDATED_EMAIL)
            .etat(UPDATED_ETAT)
            .explicationRefus(UPDATED_EXPLICATION_REFUS)
            .nbrEnfants(UPDATED_NBR_ENFANTS)
            .niveauScolarite(UPDATED_NIVEAU_SCOLARITE)
            .nom(UPDATED_NOM)
            .nomFr(UPDATED_NOM_FR)
            .numeroDossier(UPDATED_NUMERO_DOSSIER)
            .prenom(UPDATED_PRENOM)
            .prenomFr(UPDATED_PRENOM_FR)
            .profession(UPDATED_PROFESSION)
            .selectionner(UPDATED_SELECTIONNER)
            .sexe(UPDATED_SEXE)
            .telephone(UPDATED_TELEPHONE)
            .autreBenef2019(UPDATED_AUTRE_BENEF_2019)
            .autreBenef2020(UPDATED_AUTRE_BENEF_2020)
            .relationFamiliale(UPDATED_RELATION_FAMILIALE);

        restFamilleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFamille.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFamille))
            )
            .andExpect(status().isOk());

        // Validate the Famille in the database
        List<Famille> familleList = familleRepository.findAll();
        assertThat(familleList).hasSize(databaseSizeBeforeUpdate);
        Famille testFamille = familleList.get(familleList.size() - 1);
        assertThat(testFamille.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testFamille.getBenef2019()).isEqualTo(UPDATED_BENEF_2019);
        assertThat(testFamille.getBenef2020()).isEqualTo(UPDATED_BENEF_2020);
        assertThat(testFamille.getCin()).isEqualTo(UPDATED_CIN);
        assertThat(testFamille.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
        assertThat(testFamille.getDateModification()).isEqualTo(UPDATED_DATE_MODIFICATION);
        assertThat(testFamille.getDateNaissance()).isEqualTo(UPDATED_DATE_NAISSANCE);
        assertThat(testFamille.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testFamille.getEtat()).isEqualTo(UPDATED_ETAT);
        assertThat(testFamille.getExplicationRefus()).isEqualTo(UPDATED_EXPLICATION_REFUS);
        assertThat(testFamille.getNbrEnfants()).isEqualTo(UPDATED_NBR_ENFANTS);
        assertThat(testFamille.getNiveauScolarite()).isEqualTo(UPDATED_NIVEAU_SCOLARITE);
        assertThat(testFamille.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testFamille.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testFamille.getNumeroDossier()).isEqualTo(UPDATED_NUMERO_DOSSIER);
        assertThat(testFamille.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testFamille.getPrenomFr()).isEqualTo(UPDATED_PRENOM_FR);
        assertThat(testFamille.getProfession()).isEqualTo(UPDATED_PROFESSION);
        assertThat(testFamille.getSelectionner()).isEqualTo(UPDATED_SELECTIONNER);
        assertThat(testFamille.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testFamille.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testFamille.getAutreBenef2019()).isEqualTo(UPDATED_AUTRE_BENEF_2019);
        assertThat(testFamille.getAutreBenef2020()).isEqualTo(UPDATED_AUTRE_BENEF_2020);
        assertThat(testFamille.getRelationFamiliale()).isEqualTo(UPDATED_RELATION_FAMILIALE);
    }

    @Test
    @Transactional
    void patchNonExistingFamille() throws Exception {
        int databaseSizeBeforeUpdate = familleRepository.findAll().size();
        famille.setId(count.incrementAndGet());

        // Create the Famille
        FamilleDTO familleDTO = familleMapper.toDto(famille);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFamilleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, familleDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(familleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Famille in the database
        List<Famille> familleList = familleRepository.findAll();
        assertThat(familleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFamille() throws Exception {
        int databaseSizeBeforeUpdate = familleRepository.findAll().size();
        famille.setId(count.incrementAndGet());

        // Create the Famille
        FamilleDTO familleDTO = familleMapper.toDto(famille);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFamilleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(familleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Famille in the database
        List<Famille> familleList = familleRepository.findAll();
        assertThat(familleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFamille() throws Exception {
        int databaseSizeBeforeUpdate = familleRepository.findAll().size();
        famille.setId(count.incrementAndGet());

        // Create the Famille
        FamilleDTO familleDTO = familleMapper.toDto(famille);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFamilleMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(familleDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Famille in the database
        List<Famille> familleList = familleRepository.findAll();
        assertThat(familleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFamille() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        int databaseSizeBeforeDelete = familleRepository.findAll().size();

        // Delete the famille
        restFamilleMockMvc
            .perform(delete(ENTITY_API_URL_ID, famille.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Famille> familleList = familleRepository.findAll();
        assertThat(familleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
