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
import ma.raafiq.domain.MotifRefus;
import ma.raafiq.domain.Professionnel;
import ma.raafiq.domain.Province;
import ma.raafiq.repository.ProfessionnelRepository;
import ma.raafiq.service.criteria.ProfessionnelCriteria;
import ma.raafiq.service.dto.ProfessionnelDTO;
import ma.raafiq.service.mapper.ProfessionnelMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ProfessionnelResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProfessionnelResourceIT {

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

    private static final String DEFAULT_LIEU_TRAVAIL_PROFESSIONNEL = "AAAAAAAAAA";
    private static final String UPDATED_LIEU_TRAVAIL_PROFESSIONNEL = "BBBBBBBBBB";

    private static final Integer DEFAULT_SPECIALITE_PROFESSIONNEL = 1;
    private static final Integer UPDATED_SPECIALITE_PROFESSIONNEL = 2;
    private static final Integer SMALLER_SPECIALITE_PROFESSIONNEL = 1 - 1;

    private static final String ENTITY_API_URL = "/api/professionnels";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProfessionnelRepository professionnelRepository;

    @Autowired
    private ProfessionnelMapper professionnelMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProfessionnelMockMvc;

    private Professionnel professionnel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Professionnel createEntity(EntityManager em) {
        Professionnel professionnel = new Professionnel()
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
            .lieuTravailProfessionnel(DEFAULT_LIEU_TRAVAIL_PROFESSIONNEL)
            .specialiteProfessionnel(DEFAULT_SPECIALITE_PROFESSIONNEL);
        return professionnel;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Professionnel createUpdatedEntity(EntityManager em) {
        Professionnel professionnel = new Professionnel()
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
            .lieuTravailProfessionnel(UPDATED_LIEU_TRAVAIL_PROFESSIONNEL)
            .specialiteProfessionnel(UPDATED_SPECIALITE_PROFESSIONNEL);
        return professionnel;
    }

    @BeforeEach
    public void initTest() {
        professionnel = createEntity(em);
    }

    @Test
    @Transactional
    void createProfessionnel() throws Exception {
        int databaseSizeBeforeCreate = professionnelRepository.findAll().size();
        // Create the Professionnel
        ProfessionnelDTO professionnelDTO = professionnelMapper.toDto(professionnel);
        restProfessionnelMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(professionnelDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Professionnel in the database
        List<Professionnel> professionnelList = professionnelRepository.findAll();
        assertThat(professionnelList).hasSize(databaseSizeBeforeCreate + 1);
        Professionnel testProfessionnel = professionnelList.get(professionnelList.size() - 1);
        assertThat(testProfessionnel.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testProfessionnel.getBenef2019()).isEqualTo(DEFAULT_BENEF_2019);
        assertThat(testProfessionnel.getBenef2020()).isEqualTo(DEFAULT_BENEF_2020);
        assertThat(testProfessionnel.getCin()).isEqualTo(DEFAULT_CIN);
        assertThat(testProfessionnel.getDateCreation()).isEqualTo(DEFAULT_DATE_CREATION);
        assertThat(testProfessionnel.getDateModification()).isEqualTo(DEFAULT_DATE_MODIFICATION);
        assertThat(testProfessionnel.getDateNaissance()).isEqualTo(DEFAULT_DATE_NAISSANCE);
        assertThat(testProfessionnel.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testProfessionnel.getEtat()).isEqualTo(DEFAULT_ETAT);
        assertThat(testProfessionnel.getExplicationRefus()).isEqualTo(DEFAULT_EXPLICATION_REFUS);
        assertThat(testProfessionnel.getNbrEnfants()).isEqualTo(DEFAULT_NBR_ENFANTS);
        assertThat(testProfessionnel.getNiveauScolarite()).isEqualTo(DEFAULT_NIVEAU_SCOLARITE);
        assertThat(testProfessionnel.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testProfessionnel.getNomFr()).isEqualTo(DEFAULT_NOM_FR);
        assertThat(testProfessionnel.getNumeroDossier()).isEqualTo(DEFAULT_NUMERO_DOSSIER);
        assertThat(testProfessionnel.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testProfessionnel.getPrenomFr()).isEqualTo(DEFAULT_PRENOM_FR);
        assertThat(testProfessionnel.getProfession()).isEqualTo(DEFAULT_PROFESSION);
        assertThat(testProfessionnel.getSelectionner()).isEqualTo(DEFAULT_SELECTIONNER);
        assertThat(testProfessionnel.getSexe()).isEqualTo(DEFAULT_SEXE);
        assertThat(testProfessionnel.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
        assertThat(testProfessionnel.getLieuTravailProfessionnel()).isEqualTo(DEFAULT_LIEU_TRAVAIL_PROFESSIONNEL);
        assertThat(testProfessionnel.getSpecialiteProfessionnel()).isEqualTo(DEFAULT_SPECIALITE_PROFESSIONNEL);
    }

    @Test
    @Transactional
    void createProfessionnelWithExistingId() throws Exception {
        // Create the Professionnel with an existing ID
        professionnel.setId(1L);
        ProfessionnelDTO professionnelDTO = professionnelMapper.toDto(professionnel);

        int databaseSizeBeforeCreate = professionnelRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProfessionnelMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(professionnelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Professionnel in the database
        List<Professionnel> professionnelList = professionnelRepository.findAll();
        assertThat(professionnelList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCinIsRequired() throws Exception {
        int databaseSizeBeforeTest = professionnelRepository.findAll().size();
        // set the field null
        professionnel.setCin(null);

        // Create the Professionnel, which fails.
        ProfessionnelDTO professionnelDTO = professionnelMapper.toDto(professionnel);

        restProfessionnelMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(professionnelDTO))
            )
            .andExpect(status().isBadRequest());

        List<Professionnel> professionnelList = professionnelRepository.findAll();
        assertThat(professionnelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = professionnelRepository.findAll().size();
        // set the field null
        professionnel.setEmail(null);

        // Create the Professionnel, which fails.
        ProfessionnelDTO professionnelDTO = professionnelMapper.toDto(professionnel);

        restProfessionnelMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(professionnelDTO))
            )
            .andExpect(status().isBadRequest());

        List<Professionnel> professionnelList = professionnelRepository.findAll();
        assertThat(professionnelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNbrEnfantsIsRequired() throws Exception {
        int databaseSizeBeforeTest = professionnelRepository.findAll().size();
        // set the field null
        professionnel.setNbrEnfants(null);

        // Create the Professionnel, which fails.
        ProfessionnelDTO professionnelDTO = professionnelMapper.toDto(professionnel);

        restProfessionnelMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(professionnelDTO))
            )
            .andExpect(status().isBadRequest());

        List<Professionnel> professionnelList = professionnelRepository.findAll();
        assertThat(professionnelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNumeroDossierIsRequired() throws Exception {
        int databaseSizeBeforeTest = professionnelRepository.findAll().size();
        // set the field null
        professionnel.setNumeroDossier(null);

        // Create the Professionnel, which fails.
        ProfessionnelDTO professionnelDTO = professionnelMapper.toDto(professionnel);

        restProfessionnelMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(professionnelDTO))
            )
            .andExpect(status().isBadRequest());

        List<Professionnel> professionnelList = professionnelRepository.findAll();
        assertThat(professionnelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProfessionnels() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList
        restProfessionnelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(professionnel.getId().intValue())))
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
            .andExpect(jsonPath("$.[*].lieuTravailProfessionnel").value(hasItem(DEFAULT_LIEU_TRAVAIL_PROFESSIONNEL)))
            .andExpect(jsonPath("$.[*].specialiteProfessionnel").value(hasItem(DEFAULT_SPECIALITE_PROFESSIONNEL)));
    }

    @Test
    @Transactional
    void getProfessionnel() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get the professionnel
        restProfessionnelMockMvc
            .perform(get(ENTITY_API_URL_ID, professionnel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(professionnel.getId().intValue()))
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
            .andExpect(jsonPath("$.lieuTravailProfessionnel").value(DEFAULT_LIEU_TRAVAIL_PROFESSIONNEL))
            .andExpect(jsonPath("$.specialiteProfessionnel").value(DEFAULT_SPECIALITE_PROFESSIONNEL));
    }

    @Test
    @Transactional
    void getProfessionnelsByIdFiltering() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        Long id = professionnel.getId();

        defaultProfessionnelShouldBeFound("id.equals=" + id);
        defaultProfessionnelShouldNotBeFound("id.notEquals=" + id);

        defaultProfessionnelShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProfessionnelShouldNotBeFound("id.greaterThan=" + id);

        defaultProfessionnelShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProfessionnelShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByAdresseIsEqualToSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where adresse equals to DEFAULT_ADRESSE
        defaultProfessionnelShouldBeFound("adresse.equals=" + DEFAULT_ADRESSE);

        // Get all the professionnelList where adresse equals to UPDATED_ADRESSE
        defaultProfessionnelShouldNotBeFound("adresse.equals=" + UPDATED_ADRESSE);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByAdresseIsNotEqualToSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where adresse not equals to DEFAULT_ADRESSE
        defaultProfessionnelShouldNotBeFound("adresse.notEquals=" + DEFAULT_ADRESSE);

        // Get all the professionnelList where adresse not equals to UPDATED_ADRESSE
        defaultProfessionnelShouldBeFound("adresse.notEquals=" + UPDATED_ADRESSE);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByAdresseIsInShouldWork() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where adresse in DEFAULT_ADRESSE or UPDATED_ADRESSE
        defaultProfessionnelShouldBeFound("adresse.in=" + DEFAULT_ADRESSE + "," + UPDATED_ADRESSE);

        // Get all the professionnelList where adresse equals to UPDATED_ADRESSE
        defaultProfessionnelShouldNotBeFound("adresse.in=" + UPDATED_ADRESSE);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByAdresseIsNullOrNotNull() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where adresse is not null
        defaultProfessionnelShouldBeFound("adresse.specified=true");

        // Get all the professionnelList where adresse is null
        defaultProfessionnelShouldNotBeFound("adresse.specified=false");
    }

    @Test
    @Transactional
    void getAllProfessionnelsByAdresseContainsSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where adresse contains DEFAULT_ADRESSE
        defaultProfessionnelShouldBeFound("adresse.contains=" + DEFAULT_ADRESSE);

        // Get all the professionnelList where adresse contains UPDATED_ADRESSE
        defaultProfessionnelShouldNotBeFound("adresse.contains=" + UPDATED_ADRESSE);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByAdresseNotContainsSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where adresse does not contain DEFAULT_ADRESSE
        defaultProfessionnelShouldNotBeFound("adresse.doesNotContain=" + DEFAULT_ADRESSE);

        // Get all the professionnelList where adresse does not contain UPDATED_ADRESSE
        defaultProfessionnelShouldBeFound("adresse.doesNotContain=" + UPDATED_ADRESSE);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByBenef2019IsEqualToSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where benef2019 equals to DEFAULT_BENEF_2019
        defaultProfessionnelShouldBeFound("benef2019.equals=" + DEFAULT_BENEF_2019);

        // Get all the professionnelList where benef2019 equals to UPDATED_BENEF_2019
        defaultProfessionnelShouldNotBeFound("benef2019.equals=" + UPDATED_BENEF_2019);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByBenef2019IsNotEqualToSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where benef2019 not equals to DEFAULT_BENEF_2019
        defaultProfessionnelShouldNotBeFound("benef2019.notEquals=" + DEFAULT_BENEF_2019);

        // Get all the professionnelList where benef2019 not equals to UPDATED_BENEF_2019
        defaultProfessionnelShouldBeFound("benef2019.notEquals=" + UPDATED_BENEF_2019);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByBenef2019IsInShouldWork() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where benef2019 in DEFAULT_BENEF_2019 or UPDATED_BENEF_2019
        defaultProfessionnelShouldBeFound("benef2019.in=" + DEFAULT_BENEF_2019 + "," + UPDATED_BENEF_2019);

        // Get all the professionnelList where benef2019 equals to UPDATED_BENEF_2019
        defaultProfessionnelShouldNotBeFound("benef2019.in=" + UPDATED_BENEF_2019);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByBenef2019IsNullOrNotNull() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where benef2019 is not null
        defaultProfessionnelShouldBeFound("benef2019.specified=true");

        // Get all the professionnelList where benef2019 is null
        defaultProfessionnelShouldNotBeFound("benef2019.specified=false");
    }

    @Test
    @Transactional
    void getAllProfessionnelsByBenef2019IsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where benef2019 is greater than or equal to DEFAULT_BENEF_2019
        defaultProfessionnelShouldBeFound("benef2019.greaterThanOrEqual=" + DEFAULT_BENEF_2019);

        // Get all the professionnelList where benef2019 is greater than or equal to UPDATED_BENEF_2019
        defaultProfessionnelShouldNotBeFound("benef2019.greaterThanOrEqual=" + UPDATED_BENEF_2019);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByBenef2019IsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where benef2019 is less than or equal to DEFAULT_BENEF_2019
        defaultProfessionnelShouldBeFound("benef2019.lessThanOrEqual=" + DEFAULT_BENEF_2019);

        // Get all the professionnelList where benef2019 is less than or equal to SMALLER_BENEF_2019
        defaultProfessionnelShouldNotBeFound("benef2019.lessThanOrEqual=" + SMALLER_BENEF_2019);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByBenef2019IsLessThanSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where benef2019 is less than DEFAULT_BENEF_2019
        defaultProfessionnelShouldNotBeFound("benef2019.lessThan=" + DEFAULT_BENEF_2019);

        // Get all the professionnelList where benef2019 is less than UPDATED_BENEF_2019
        defaultProfessionnelShouldBeFound("benef2019.lessThan=" + UPDATED_BENEF_2019);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByBenef2019IsGreaterThanSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where benef2019 is greater than DEFAULT_BENEF_2019
        defaultProfessionnelShouldNotBeFound("benef2019.greaterThan=" + DEFAULT_BENEF_2019);

        // Get all the professionnelList where benef2019 is greater than SMALLER_BENEF_2019
        defaultProfessionnelShouldBeFound("benef2019.greaterThan=" + SMALLER_BENEF_2019);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByBenef2020IsEqualToSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where benef2020 equals to DEFAULT_BENEF_2020
        defaultProfessionnelShouldBeFound("benef2020.equals=" + DEFAULT_BENEF_2020);

        // Get all the professionnelList where benef2020 equals to UPDATED_BENEF_2020
        defaultProfessionnelShouldNotBeFound("benef2020.equals=" + UPDATED_BENEF_2020);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByBenef2020IsNotEqualToSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where benef2020 not equals to DEFAULT_BENEF_2020
        defaultProfessionnelShouldNotBeFound("benef2020.notEquals=" + DEFAULT_BENEF_2020);

        // Get all the professionnelList where benef2020 not equals to UPDATED_BENEF_2020
        defaultProfessionnelShouldBeFound("benef2020.notEquals=" + UPDATED_BENEF_2020);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByBenef2020IsInShouldWork() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where benef2020 in DEFAULT_BENEF_2020 or UPDATED_BENEF_2020
        defaultProfessionnelShouldBeFound("benef2020.in=" + DEFAULT_BENEF_2020 + "," + UPDATED_BENEF_2020);

        // Get all the professionnelList where benef2020 equals to UPDATED_BENEF_2020
        defaultProfessionnelShouldNotBeFound("benef2020.in=" + UPDATED_BENEF_2020);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByBenef2020IsNullOrNotNull() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where benef2020 is not null
        defaultProfessionnelShouldBeFound("benef2020.specified=true");

        // Get all the professionnelList where benef2020 is null
        defaultProfessionnelShouldNotBeFound("benef2020.specified=false");
    }

    @Test
    @Transactional
    void getAllProfessionnelsByBenef2020IsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where benef2020 is greater than or equal to DEFAULT_BENEF_2020
        defaultProfessionnelShouldBeFound("benef2020.greaterThanOrEqual=" + DEFAULT_BENEF_2020);

        // Get all the professionnelList where benef2020 is greater than or equal to UPDATED_BENEF_2020
        defaultProfessionnelShouldNotBeFound("benef2020.greaterThanOrEqual=" + UPDATED_BENEF_2020);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByBenef2020IsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where benef2020 is less than or equal to DEFAULT_BENEF_2020
        defaultProfessionnelShouldBeFound("benef2020.lessThanOrEqual=" + DEFAULT_BENEF_2020);

        // Get all the professionnelList where benef2020 is less than or equal to SMALLER_BENEF_2020
        defaultProfessionnelShouldNotBeFound("benef2020.lessThanOrEqual=" + SMALLER_BENEF_2020);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByBenef2020IsLessThanSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where benef2020 is less than DEFAULT_BENEF_2020
        defaultProfessionnelShouldNotBeFound("benef2020.lessThan=" + DEFAULT_BENEF_2020);

        // Get all the professionnelList where benef2020 is less than UPDATED_BENEF_2020
        defaultProfessionnelShouldBeFound("benef2020.lessThan=" + UPDATED_BENEF_2020);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByBenef2020IsGreaterThanSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where benef2020 is greater than DEFAULT_BENEF_2020
        defaultProfessionnelShouldNotBeFound("benef2020.greaterThan=" + DEFAULT_BENEF_2020);

        // Get all the professionnelList where benef2020 is greater than SMALLER_BENEF_2020
        defaultProfessionnelShouldBeFound("benef2020.greaterThan=" + SMALLER_BENEF_2020);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByCinIsEqualToSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where cin equals to DEFAULT_CIN
        defaultProfessionnelShouldBeFound("cin.equals=" + DEFAULT_CIN);

        // Get all the professionnelList where cin equals to UPDATED_CIN
        defaultProfessionnelShouldNotBeFound("cin.equals=" + UPDATED_CIN);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByCinIsNotEqualToSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where cin not equals to DEFAULT_CIN
        defaultProfessionnelShouldNotBeFound("cin.notEquals=" + DEFAULT_CIN);

        // Get all the professionnelList where cin not equals to UPDATED_CIN
        defaultProfessionnelShouldBeFound("cin.notEquals=" + UPDATED_CIN);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByCinIsInShouldWork() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where cin in DEFAULT_CIN or UPDATED_CIN
        defaultProfessionnelShouldBeFound("cin.in=" + DEFAULT_CIN + "," + UPDATED_CIN);

        // Get all the professionnelList where cin equals to UPDATED_CIN
        defaultProfessionnelShouldNotBeFound("cin.in=" + UPDATED_CIN);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByCinIsNullOrNotNull() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where cin is not null
        defaultProfessionnelShouldBeFound("cin.specified=true");

        // Get all the professionnelList where cin is null
        defaultProfessionnelShouldNotBeFound("cin.specified=false");
    }

    @Test
    @Transactional
    void getAllProfessionnelsByCinContainsSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where cin contains DEFAULT_CIN
        defaultProfessionnelShouldBeFound("cin.contains=" + DEFAULT_CIN);

        // Get all the professionnelList where cin contains UPDATED_CIN
        defaultProfessionnelShouldNotBeFound("cin.contains=" + UPDATED_CIN);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByCinNotContainsSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where cin does not contain DEFAULT_CIN
        defaultProfessionnelShouldNotBeFound("cin.doesNotContain=" + DEFAULT_CIN);

        // Get all the professionnelList where cin does not contain UPDATED_CIN
        defaultProfessionnelShouldBeFound("cin.doesNotContain=" + UPDATED_CIN);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByDateCreationIsEqualToSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where dateCreation equals to DEFAULT_DATE_CREATION
        defaultProfessionnelShouldBeFound("dateCreation.equals=" + DEFAULT_DATE_CREATION);

        // Get all the professionnelList where dateCreation equals to UPDATED_DATE_CREATION
        defaultProfessionnelShouldNotBeFound("dateCreation.equals=" + UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByDateCreationIsNotEqualToSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where dateCreation not equals to DEFAULT_DATE_CREATION
        defaultProfessionnelShouldNotBeFound("dateCreation.notEquals=" + DEFAULT_DATE_CREATION);

        // Get all the professionnelList where dateCreation not equals to UPDATED_DATE_CREATION
        defaultProfessionnelShouldBeFound("dateCreation.notEquals=" + UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByDateCreationIsInShouldWork() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where dateCreation in DEFAULT_DATE_CREATION or UPDATED_DATE_CREATION
        defaultProfessionnelShouldBeFound("dateCreation.in=" + DEFAULT_DATE_CREATION + "," + UPDATED_DATE_CREATION);

        // Get all the professionnelList where dateCreation equals to UPDATED_DATE_CREATION
        defaultProfessionnelShouldNotBeFound("dateCreation.in=" + UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByDateCreationIsNullOrNotNull() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where dateCreation is not null
        defaultProfessionnelShouldBeFound("dateCreation.specified=true");

        // Get all the professionnelList where dateCreation is null
        defaultProfessionnelShouldNotBeFound("dateCreation.specified=false");
    }

    @Test
    @Transactional
    void getAllProfessionnelsByDateModificationIsEqualToSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where dateModification equals to DEFAULT_DATE_MODIFICATION
        defaultProfessionnelShouldBeFound("dateModification.equals=" + DEFAULT_DATE_MODIFICATION);

        // Get all the professionnelList where dateModification equals to UPDATED_DATE_MODIFICATION
        defaultProfessionnelShouldNotBeFound("dateModification.equals=" + UPDATED_DATE_MODIFICATION);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByDateModificationIsNotEqualToSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where dateModification not equals to DEFAULT_DATE_MODIFICATION
        defaultProfessionnelShouldNotBeFound("dateModification.notEquals=" + DEFAULT_DATE_MODIFICATION);

        // Get all the professionnelList where dateModification not equals to UPDATED_DATE_MODIFICATION
        defaultProfessionnelShouldBeFound("dateModification.notEquals=" + UPDATED_DATE_MODIFICATION);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByDateModificationIsInShouldWork() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where dateModification in DEFAULT_DATE_MODIFICATION or UPDATED_DATE_MODIFICATION
        defaultProfessionnelShouldBeFound("dateModification.in=" + DEFAULT_DATE_MODIFICATION + "," + UPDATED_DATE_MODIFICATION);

        // Get all the professionnelList where dateModification equals to UPDATED_DATE_MODIFICATION
        defaultProfessionnelShouldNotBeFound("dateModification.in=" + UPDATED_DATE_MODIFICATION);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByDateModificationIsNullOrNotNull() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where dateModification is not null
        defaultProfessionnelShouldBeFound("dateModification.specified=true");

        // Get all the professionnelList where dateModification is null
        defaultProfessionnelShouldNotBeFound("dateModification.specified=false");
    }

    @Test
    @Transactional
    void getAllProfessionnelsByDateNaissanceIsEqualToSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where dateNaissance equals to DEFAULT_DATE_NAISSANCE
        defaultProfessionnelShouldBeFound("dateNaissance.equals=" + DEFAULT_DATE_NAISSANCE);

        // Get all the professionnelList where dateNaissance equals to UPDATED_DATE_NAISSANCE
        defaultProfessionnelShouldNotBeFound("dateNaissance.equals=" + UPDATED_DATE_NAISSANCE);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByDateNaissanceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where dateNaissance not equals to DEFAULT_DATE_NAISSANCE
        defaultProfessionnelShouldNotBeFound("dateNaissance.notEquals=" + DEFAULT_DATE_NAISSANCE);

        // Get all the professionnelList where dateNaissance not equals to UPDATED_DATE_NAISSANCE
        defaultProfessionnelShouldBeFound("dateNaissance.notEquals=" + UPDATED_DATE_NAISSANCE);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByDateNaissanceIsInShouldWork() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where dateNaissance in DEFAULT_DATE_NAISSANCE or UPDATED_DATE_NAISSANCE
        defaultProfessionnelShouldBeFound("dateNaissance.in=" + DEFAULT_DATE_NAISSANCE + "," + UPDATED_DATE_NAISSANCE);

        // Get all the professionnelList where dateNaissance equals to UPDATED_DATE_NAISSANCE
        defaultProfessionnelShouldNotBeFound("dateNaissance.in=" + UPDATED_DATE_NAISSANCE);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByDateNaissanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where dateNaissance is not null
        defaultProfessionnelShouldBeFound("dateNaissance.specified=true");

        // Get all the professionnelList where dateNaissance is null
        defaultProfessionnelShouldNotBeFound("dateNaissance.specified=false");
    }

    @Test
    @Transactional
    void getAllProfessionnelsByDateNaissanceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where dateNaissance is greater than or equal to DEFAULT_DATE_NAISSANCE
        defaultProfessionnelShouldBeFound("dateNaissance.greaterThanOrEqual=" + DEFAULT_DATE_NAISSANCE);

        // Get all the professionnelList where dateNaissance is greater than or equal to UPDATED_DATE_NAISSANCE
        defaultProfessionnelShouldNotBeFound("dateNaissance.greaterThanOrEqual=" + UPDATED_DATE_NAISSANCE);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByDateNaissanceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where dateNaissance is less than or equal to DEFAULT_DATE_NAISSANCE
        defaultProfessionnelShouldBeFound("dateNaissance.lessThanOrEqual=" + DEFAULT_DATE_NAISSANCE);

        // Get all the professionnelList where dateNaissance is less than or equal to SMALLER_DATE_NAISSANCE
        defaultProfessionnelShouldNotBeFound("dateNaissance.lessThanOrEqual=" + SMALLER_DATE_NAISSANCE);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByDateNaissanceIsLessThanSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where dateNaissance is less than DEFAULT_DATE_NAISSANCE
        defaultProfessionnelShouldNotBeFound("dateNaissance.lessThan=" + DEFAULT_DATE_NAISSANCE);

        // Get all the professionnelList where dateNaissance is less than UPDATED_DATE_NAISSANCE
        defaultProfessionnelShouldBeFound("dateNaissance.lessThan=" + UPDATED_DATE_NAISSANCE);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByDateNaissanceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where dateNaissance is greater than DEFAULT_DATE_NAISSANCE
        defaultProfessionnelShouldNotBeFound("dateNaissance.greaterThan=" + DEFAULT_DATE_NAISSANCE);

        // Get all the professionnelList where dateNaissance is greater than SMALLER_DATE_NAISSANCE
        defaultProfessionnelShouldBeFound("dateNaissance.greaterThan=" + SMALLER_DATE_NAISSANCE);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where email equals to DEFAULT_EMAIL
        defaultProfessionnelShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the professionnelList where email equals to UPDATED_EMAIL
        defaultProfessionnelShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where email not equals to DEFAULT_EMAIL
        defaultProfessionnelShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the professionnelList where email not equals to UPDATED_EMAIL
        defaultProfessionnelShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultProfessionnelShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the professionnelList where email equals to UPDATED_EMAIL
        defaultProfessionnelShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where email is not null
        defaultProfessionnelShouldBeFound("email.specified=true");

        // Get all the professionnelList where email is null
        defaultProfessionnelShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllProfessionnelsByEmailContainsSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where email contains DEFAULT_EMAIL
        defaultProfessionnelShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the professionnelList where email contains UPDATED_EMAIL
        defaultProfessionnelShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where email does not contain DEFAULT_EMAIL
        defaultProfessionnelShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the professionnelList where email does not contain UPDATED_EMAIL
        defaultProfessionnelShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByEtatIsEqualToSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where etat equals to DEFAULT_ETAT
        defaultProfessionnelShouldBeFound("etat.equals=" + DEFAULT_ETAT);

        // Get all the professionnelList where etat equals to UPDATED_ETAT
        defaultProfessionnelShouldNotBeFound("etat.equals=" + UPDATED_ETAT);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByEtatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where etat not equals to DEFAULT_ETAT
        defaultProfessionnelShouldNotBeFound("etat.notEquals=" + DEFAULT_ETAT);

        // Get all the professionnelList where etat not equals to UPDATED_ETAT
        defaultProfessionnelShouldBeFound("etat.notEquals=" + UPDATED_ETAT);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByEtatIsInShouldWork() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where etat in DEFAULT_ETAT or UPDATED_ETAT
        defaultProfessionnelShouldBeFound("etat.in=" + DEFAULT_ETAT + "," + UPDATED_ETAT);

        // Get all the professionnelList where etat equals to UPDATED_ETAT
        defaultProfessionnelShouldNotBeFound("etat.in=" + UPDATED_ETAT);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByEtatIsNullOrNotNull() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where etat is not null
        defaultProfessionnelShouldBeFound("etat.specified=true");

        // Get all the professionnelList where etat is null
        defaultProfessionnelShouldNotBeFound("etat.specified=false");
    }

    @Test
    @Transactional
    void getAllProfessionnelsByEtatIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where etat is greater than or equal to DEFAULT_ETAT
        defaultProfessionnelShouldBeFound("etat.greaterThanOrEqual=" + DEFAULT_ETAT);

        // Get all the professionnelList where etat is greater than or equal to UPDATED_ETAT
        defaultProfessionnelShouldNotBeFound("etat.greaterThanOrEqual=" + UPDATED_ETAT);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByEtatIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where etat is less than or equal to DEFAULT_ETAT
        defaultProfessionnelShouldBeFound("etat.lessThanOrEqual=" + DEFAULT_ETAT);

        // Get all the professionnelList where etat is less than or equal to SMALLER_ETAT
        defaultProfessionnelShouldNotBeFound("etat.lessThanOrEqual=" + SMALLER_ETAT);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByEtatIsLessThanSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where etat is less than DEFAULT_ETAT
        defaultProfessionnelShouldNotBeFound("etat.lessThan=" + DEFAULT_ETAT);

        // Get all the professionnelList where etat is less than UPDATED_ETAT
        defaultProfessionnelShouldBeFound("etat.lessThan=" + UPDATED_ETAT);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByEtatIsGreaterThanSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where etat is greater than DEFAULT_ETAT
        defaultProfessionnelShouldNotBeFound("etat.greaterThan=" + DEFAULT_ETAT);

        // Get all the professionnelList where etat is greater than SMALLER_ETAT
        defaultProfessionnelShouldBeFound("etat.greaterThan=" + SMALLER_ETAT);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByExplicationRefusIsEqualToSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where explicationRefus equals to DEFAULT_EXPLICATION_REFUS
        defaultProfessionnelShouldBeFound("explicationRefus.equals=" + DEFAULT_EXPLICATION_REFUS);

        // Get all the professionnelList where explicationRefus equals to UPDATED_EXPLICATION_REFUS
        defaultProfessionnelShouldNotBeFound("explicationRefus.equals=" + UPDATED_EXPLICATION_REFUS);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByExplicationRefusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where explicationRefus not equals to DEFAULT_EXPLICATION_REFUS
        defaultProfessionnelShouldNotBeFound("explicationRefus.notEquals=" + DEFAULT_EXPLICATION_REFUS);

        // Get all the professionnelList where explicationRefus not equals to UPDATED_EXPLICATION_REFUS
        defaultProfessionnelShouldBeFound("explicationRefus.notEquals=" + UPDATED_EXPLICATION_REFUS);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByExplicationRefusIsInShouldWork() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where explicationRefus in DEFAULT_EXPLICATION_REFUS or UPDATED_EXPLICATION_REFUS
        defaultProfessionnelShouldBeFound("explicationRefus.in=" + DEFAULT_EXPLICATION_REFUS + "," + UPDATED_EXPLICATION_REFUS);

        // Get all the professionnelList where explicationRefus equals to UPDATED_EXPLICATION_REFUS
        defaultProfessionnelShouldNotBeFound("explicationRefus.in=" + UPDATED_EXPLICATION_REFUS);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByExplicationRefusIsNullOrNotNull() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where explicationRefus is not null
        defaultProfessionnelShouldBeFound("explicationRefus.specified=true");

        // Get all the professionnelList where explicationRefus is null
        defaultProfessionnelShouldNotBeFound("explicationRefus.specified=false");
    }

    @Test
    @Transactional
    void getAllProfessionnelsByExplicationRefusContainsSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where explicationRefus contains DEFAULT_EXPLICATION_REFUS
        defaultProfessionnelShouldBeFound("explicationRefus.contains=" + DEFAULT_EXPLICATION_REFUS);

        // Get all the professionnelList where explicationRefus contains UPDATED_EXPLICATION_REFUS
        defaultProfessionnelShouldNotBeFound("explicationRefus.contains=" + UPDATED_EXPLICATION_REFUS);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByExplicationRefusNotContainsSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where explicationRefus does not contain DEFAULT_EXPLICATION_REFUS
        defaultProfessionnelShouldNotBeFound("explicationRefus.doesNotContain=" + DEFAULT_EXPLICATION_REFUS);

        // Get all the professionnelList where explicationRefus does not contain UPDATED_EXPLICATION_REFUS
        defaultProfessionnelShouldBeFound("explicationRefus.doesNotContain=" + UPDATED_EXPLICATION_REFUS);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByNbrEnfantsIsEqualToSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where nbrEnfants equals to DEFAULT_NBR_ENFANTS
        defaultProfessionnelShouldBeFound("nbrEnfants.equals=" + DEFAULT_NBR_ENFANTS);

        // Get all the professionnelList where nbrEnfants equals to UPDATED_NBR_ENFANTS
        defaultProfessionnelShouldNotBeFound("nbrEnfants.equals=" + UPDATED_NBR_ENFANTS);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByNbrEnfantsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where nbrEnfants not equals to DEFAULT_NBR_ENFANTS
        defaultProfessionnelShouldNotBeFound("nbrEnfants.notEquals=" + DEFAULT_NBR_ENFANTS);

        // Get all the professionnelList where nbrEnfants not equals to UPDATED_NBR_ENFANTS
        defaultProfessionnelShouldBeFound("nbrEnfants.notEquals=" + UPDATED_NBR_ENFANTS);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByNbrEnfantsIsInShouldWork() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where nbrEnfants in DEFAULT_NBR_ENFANTS or UPDATED_NBR_ENFANTS
        defaultProfessionnelShouldBeFound("nbrEnfants.in=" + DEFAULT_NBR_ENFANTS + "," + UPDATED_NBR_ENFANTS);

        // Get all the professionnelList where nbrEnfants equals to UPDATED_NBR_ENFANTS
        defaultProfessionnelShouldNotBeFound("nbrEnfants.in=" + UPDATED_NBR_ENFANTS);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByNbrEnfantsIsNullOrNotNull() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where nbrEnfants is not null
        defaultProfessionnelShouldBeFound("nbrEnfants.specified=true");

        // Get all the professionnelList where nbrEnfants is null
        defaultProfessionnelShouldNotBeFound("nbrEnfants.specified=false");
    }

    @Test
    @Transactional
    void getAllProfessionnelsByNbrEnfantsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where nbrEnfants is greater than or equal to DEFAULT_NBR_ENFANTS
        defaultProfessionnelShouldBeFound("nbrEnfants.greaterThanOrEqual=" + DEFAULT_NBR_ENFANTS);

        // Get all the professionnelList where nbrEnfants is greater than or equal to UPDATED_NBR_ENFANTS
        defaultProfessionnelShouldNotBeFound("nbrEnfants.greaterThanOrEqual=" + UPDATED_NBR_ENFANTS);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByNbrEnfantsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where nbrEnfants is less than or equal to DEFAULT_NBR_ENFANTS
        defaultProfessionnelShouldBeFound("nbrEnfants.lessThanOrEqual=" + DEFAULT_NBR_ENFANTS);

        // Get all the professionnelList where nbrEnfants is less than or equal to SMALLER_NBR_ENFANTS
        defaultProfessionnelShouldNotBeFound("nbrEnfants.lessThanOrEqual=" + SMALLER_NBR_ENFANTS);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByNbrEnfantsIsLessThanSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where nbrEnfants is less than DEFAULT_NBR_ENFANTS
        defaultProfessionnelShouldNotBeFound("nbrEnfants.lessThan=" + DEFAULT_NBR_ENFANTS);

        // Get all the professionnelList where nbrEnfants is less than UPDATED_NBR_ENFANTS
        defaultProfessionnelShouldBeFound("nbrEnfants.lessThan=" + UPDATED_NBR_ENFANTS);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByNbrEnfantsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where nbrEnfants is greater than DEFAULT_NBR_ENFANTS
        defaultProfessionnelShouldNotBeFound("nbrEnfants.greaterThan=" + DEFAULT_NBR_ENFANTS);

        // Get all the professionnelList where nbrEnfants is greater than SMALLER_NBR_ENFANTS
        defaultProfessionnelShouldBeFound("nbrEnfants.greaterThan=" + SMALLER_NBR_ENFANTS);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByNiveauScolariteIsEqualToSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where niveauScolarite equals to DEFAULT_NIVEAU_SCOLARITE
        defaultProfessionnelShouldBeFound("niveauScolarite.equals=" + DEFAULT_NIVEAU_SCOLARITE);

        // Get all the professionnelList where niveauScolarite equals to UPDATED_NIVEAU_SCOLARITE
        defaultProfessionnelShouldNotBeFound("niveauScolarite.equals=" + UPDATED_NIVEAU_SCOLARITE);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByNiveauScolariteIsNotEqualToSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where niveauScolarite not equals to DEFAULT_NIVEAU_SCOLARITE
        defaultProfessionnelShouldNotBeFound("niveauScolarite.notEquals=" + DEFAULT_NIVEAU_SCOLARITE);

        // Get all the professionnelList where niveauScolarite not equals to UPDATED_NIVEAU_SCOLARITE
        defaultProfessionnelShouldBeFound("niveauScolarite.notEquals=" + UPDATED_NIVEAU_SCOLARITE);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByNiveauScolariteIsInShouldWork() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where niveauScolarite in DEFAULT_NIVEAU_SCOLARITE or UPDATED_NIVEAU_SCOLARITE
        defaultProfessionnelShouldBeFound("niveauScolarite.in=" + DEFAULT_NIVEAU_SCOLARITE + "," + UPDATED_NIVEAU_SCOLARITE);

        // Get all the professionnelList where niveauScolarite equals to UPDATED_NIVEAU_SCOLARITE
        defaultProfessionnelShouldNotBeFound("niveauScolarite.in=" + UPDATED_NIVEAU_SCOLARITE);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByNiveauScolariteIsNullOrNotNull() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where niveauScolarite is not null
        defaultProfessionnelShouldBeFound("niveauScolarite.specified=true");

        // Get all the professionnelList where niveauScolarite is null
        defaultProfessionnelShouldNotBeFound("niveauScolarite.specified=false");
    }

    @Test
    @Transactional
    void getAllProfessionnelsByNiveauScolariteIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where niveauScolarite is greater than or equal to DEFAULT_NIVEAU_SCOLARITE
        defaultProfessionnelShouldBeFound("niveauScolarite.greaterThanOrEqual=" + DEFAULT_NIVEAU_SCOLARITE);

        // Get all the professionnelList where niveauScolarite is greater than or equal to UPDATED_NIVEAU_SCOLARITE
        defaultProfessionnelShouldNotBeFound("niveauScolarite.greaterThanOrEqual=" + UPDATED_NIVEAU_SCOLARITE);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByNiveauScolariteIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where niveauScolarite is less than or equal to DEFAULT_NIVEAU_SCOLARITE
        defaultProfessionnelShouldBeFound("niveauScolarite.lessThanOrEqual=" + DEFAULT_NIVEAU_SCOLARITE);

        // Get all the professionnelList where niveauScolarite is less than or equal to SMALLER_NIVEAU_SCOLARITE
        defaultProfessionnelShouldNotBeFound("niveauScolarite.lessThanOrEqual=" + SMALLER_NIVEAU_SCOLARITE);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByNiveauScolariteIsLessThanSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where niveauScolarite is less than DEFAULT_NIVEAU_SCOLARITE
        defaultProfessionnelShouldNotBeFound("niveauScolarite.lessThan=" + DEFAULT_NIVEAU_SCOLARITE);

        // Get all the professionnelList where niveauScolarite is less than UPDATED_NIVEAU_SCOLARITE
        defaultProfessionnelShouldBeFound("niveauScolarite.lessThan=" + UPDATED_NIVEAU_SCOLARITE);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByNiveauScolariteIsGreaterThanSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where niveauScolarite is greater than DEFAULT_NIVEAU_SCOLARITE
        defaultProfessionnelShouldNotBeFound("niveauScolarite.greaterThan=" + DEFAULT_NIVEAU_SCOLARITE);

        // Get all the professionnelList where niveauScolarite is greater than SMALLER_NIVEAU_SCOLARITE
        defaultProfessionnelShouldBeFound("niveauScolarite.greaterThan=" + SMALLER_NIVEAU_SCOLARITE);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByNomIsEqualToSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where nom equals to DEFAULT_NOM
        defaultProfessionnelShouldBeFound("nom.equals=" + DEFAULT_NOM);

        // Get all the professionnelList where nom equals to UPDATED_NOM
        defaultProfessionnelShouldNotBeFound("nom.equals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByNomIsNotEqualToSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where nom not equals to DEFAULT_NOM
        defaultProfessionnelShouldNotBeFound("nom.notEquals=" + DEFAULT_NOM);

        // Get all the professionnelList where nom not equals to UPDATED_NOM
        defaultProfessionnelShouldBeFound("nom.notEquals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByNomIsInShouldWork() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where nom in DEFAULT_NOM or UPDATED_NOM
        defaultProfessionnelShouldBeFound("nom.in=" + DEFAULT_NOM + "," + UPDATED_NOM);

        // Get all the professionnelList where nom equals to UPDATED_NOM
        defaultProfessionnelShouldNotBeFound("nom.in=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByNomIsNullOrNotNull() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where nom is not null
        defaultProfessionnelShouldBeFound("nom.specified=true");

        // Get all the professionnelList where nom is null
        defaultProfessionnelShouldNotBeFound("nom.specified=false");
    }

    @Test
    @Transactional
    void getAllProfessionnelsByNomContainsSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where nom contains DEFAULT_NOM
        defaultProfessionnelShouldBeFound("nom.contains=" + DEFAULT_NOM);

        // Get all the professionnelList where nom contains UPDATED_NOM
        defaultProfessionnelShouldNotBeFound("nom.contains=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByNomNotContainsSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where nom does not contain DEFAULT_NOM
        defaultProfessionnelShouldNotBeFound("nom.doesNotContain=" + DEFAULT_NOM);

        // Get all the professionnelList where nom does not contain UPDATED_NOM
        defaultProfessionnelShouldBeFound("nom.doesNotContain=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByNomFrIsEqualToSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where nomFr equals to DEFAULT_NOM_FR
        defaultProfessionnelShouldBeFound("nomFr.equals=" + DEFAULT_NOM_FR);

        // Get all the professionnelList where nomFr equals to UPDATED_NOM_FR
        defaultProfessionnelShouldNotBeFound("nomFr.equals=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByNomFrIsNotEqualToSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where nomFr not equals to DEFAULT_NOM_FR
        defaultProfessionnelShouldNotBeFound("nomFr.notEquals=" + DEFAULT_NOM_FR);

        // Get all the professionnelList where nomFr not equals to UPDATED_NOM_FR
        defaultProfessionnelShouldBeFound("nomFr.notEquals=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByNomFrIsInShouldWork() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where nomFr in DEFAULT_NOM_FR or UPDATED_NOM_FR
        defaultProfessionnelShouldBeFound("nomFr.in=" + DEFAULT_NOM_FR + "," + UPDATED_NOM_FR);

        // Get all the professionnelList where nomFr equals to UPDATED_NOM_FR
        defaultProfessionnelShouldNotBeFound("nomFr.in=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByNomFrIsNullOrNotNull() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where nomFr is not null
        defaultProfessionnelShouldBeFound("nomFr.specified=true");

        // Get all the professionnelList where nomFr is null
        defaultProfessionnelShouldNotBeFound("nomFr.specified=false");
    }

    @Test
    @Transactional
    void getAllProfessionnelsByNomFrContainsSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where nomFr contains DEFAULT_NOM_FR
        defaultProfessionnelShouldBeFound("nomFr.contains=" + DEFAULT_NOM_FR);

        // Get all the professionnelList where nomFr contains UPDATED_NOM_FR
        defaultProfessionnelShouldNotBeFound("nomFr.contains=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByNomFrNotContainsSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where nomFr does not contain DEFAULT_NOM_FR
        defaultProfessionnelShouldNotBeFound("nomFr.doesNotContain=" + DEFAULT_NOM_FR);

        // Get all the professionnelList where nomFr does not contain UPDATED_NOM_FR
        defaultProfessionnelShouldBeFound("nomFr.doesNotContain=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByNumeroDossierIsEqualToSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where numeroDossier equals to DEFAULT_NUMERO_DOSSIER
        defaultProfessionnelShouldBeFound("numeroDossier.equals=" + DEFAULT_NUMERO_DOSSIER);

        // Get all the professionnelList where numeroDossier equals to UPDATED_NUMERO_DOSSIER
        defaultProfessionnelShouldNotBeFound("numeroDossier.equals=" + UPDATED_NUMERO_DOSSIER);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByNumeroDossierIsNotEqualToSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where numeroDossier not equals to DEFAULT_NUMERO_DOSSIER
        defaultProfessionnelShouldNotBeFound("numeroDossier.notEquals=" + DEFAULT_NUMERO_DOSSIER);

        // Get all the professionnelList where numeroDossier not equals to UPDATED_NUMERO_DOSSIER
        defaultProfessionnelShouldBeFound("numeroDossier.notEquals=" + UPDATED_NUMERO_DOSSIER);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByNumeroDossierIsInShouldWork() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where numeroDossier in DEFAULT_NUMERO_DOSSIER or UPDATED_NUMERO_DOSSIER
        defaultProfessionnelShouldBeFound("numeroDossier.in=" + DEFAULT_NUMERO_DOSSIER + "," + UPDATED_NUMERO_DOSSIER);

        // Get all the professionnelList where numeroDossier equals to UPDATED_NUMERO_DOSSIER
        defaultProfessionnelShouldNotBeFound("numeroDossier.in=" + UPDATED_NUMERO_DOSSIER);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByNumeroDossierIsNullOrNotNull() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where numeroDossier is not null
        defaultProfessionnelShouldBeFound("numeroDossier.specified=true");

        // Get all the professionnelList where numeroDossier is null
        defaultProfessionnelShouldNotBeFound("numeroDossier.specified=false");
    }

    @Test
    @Transactional
    void getAllProfessionnelsByNumeroDossierContainsSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where numeroDossier contains DEFAULT_NUMERO_DOSSIER
        defaultProfessionnelShouldBeFound("numeroDossier.contains=" + DEFAULT_NUMERO_DOSSIER);

        // Get all the professionnelList where numeroDossier contains UPDATED_NUMERO_DOSSIER
        defaultProfessionnelShouldNotBeFound("numeroDossier.contains=" + UPDATED_NUMERO_DOSSIER);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByNumeroDossierNotContainsSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where numeroDossier does not contain DEFAULT_NUMERO_DOSSIER
        defaultProfessionnelShouldNotBeFound("numeroDossier.doesNotContain=" + DEFAULT_NUMERO_DOSSIER);

        // Get all the professionnelList where numeroDossier does not contain UPDATED_NUMERO_DOSSIER
        defaultProfessionnelShouldBeFound("numeroDossier.doesNotContain=" + UPDATED_NUMERO_DOSSIER);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByPrenomIsEqualToSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where prenom equals to DEFAULT_PRENOM
        defaultProfessionnelShouldBeFound("prenom.equals=" + DEFAULT_PRENOM);

        // Get all the professionnelList where prenom equals to UPDATED_PRENOM
        defaultProfessionnelShouldNotBeFound("prenom.equals=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByPrenomIsNotEqualToSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where prenom not equals to DEFAULT_PRENOM
        defaultProfessionnelShouldNotBeFound("prenom.notEquals=" + DEFAULT_PRENOM);

        // Get all the professionnelList where prenom not equals to UPDATED_PRENOM
        defaultProfessionnelShouldBeFound("prenom.notEquals=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByPrenomIsInShouldWork() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where prenom in DEFAULT_PRENOM or UPDATED_PRENOM
        defaultProfessionnelShouldBeFound("prenom.in=" + DEFAULT_PRENOM + "," + UPDATED_PRENOM);

        // Get all the professionnelList where prenom equals to UPDATED_PRENOM
        defaultProfessionnelShouldNotBeFound("prenom.in=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByPrenomIsNullOrNotNull() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where prenom is not null
        defaultProfessionnelShouldBeFound("prenom.specified=true");

        // Get all the professionnelList where prenom is null
        defaultProfessionnelShouldNotBeFound("prenom.specified=false");
    }

    @Test
    @Transactional
    void getAllProfessionnelsByPrenomContainsSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where prenom contains DEFAULT_PRENOM
        defaultProfessionnelShouldBeFound("prenom.contains=" + DEFAULT_PRENOM);

        // Get all the professionnelList where prenom contains UPDATED_PRENOM
        defaultProfessionnelShouldNotBeFound("prenom.contains=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByPrenomNotContainsSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where prenom does not contain DEFAULT_PRENOM
        defaultProfessionnelShouldNotBeFound("prenom.doesNotContain=" + DEFAULT_PRENOM);

        // Get all the professionnelList where prenom does not contain UPDATED_PRENOM
        defaultProfessionnelShouldBeFound("prenom.doesNotContain=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByPrenomFrIsEqualToSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where prenomFr equals to DEFAULT_PRENOM_FR
        defaultProfessionnelShouldBeFound("prenomFr.equals=" + DEFAULT_PRENOM_FR);

        // Get all the professionnelList where prenomFr equals to UPDATED_PRENOM_FR
        defaultProfessionnelShouldNotBeFound("prenomFr.equals=" + UPDATED_PRENOM_FR);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByPrenomFrIsNotEqualToSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where prenomFr not equals to DEFAULT_PRENOM_FR
        defaultProfessionnelShouldNotBeFound("prenomFr.notEquals=" + DEFAULT_PRENOM_FR);

        // Get all the professionnelList where prenomFr not equals to UPDATED_PRENOM_FR
        defaultProfessionnelShouldBeFound("prenomFr.notEquals=" + UPDATED_PRENOM_FR);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByPrenomFrIsInShouldWork() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where prenomFr in DEFAULT_PRENOM_FR or UPDATED_PRENOM_FR
        defaultProfessionnelShouldBeFound("prenomFr.in=" + DEFAULT_PRENOM_FR + "," + UPDATED_PRENOM_FR);

        // Get all the professionnelList where prenomFr equals to UPDATED_PRENOM_FR
        defaultProfessionnelShouldNotBeFound("prenomFr.in=" + UPDATED_PRENOM_FR);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByPrenomFrIsNullOrNotNull() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where prenomFr is not null
        defaultProfessionnelShouldBeFound("prenomFr.specified=true");

        // Get all the professionnelList where prenomFr is null
        defaultProfessionnelShouldNotBeFound("prenomFr.specified=false");
    }

    @Test
    @Transactional
    void getAllProfessionnelsByPrenomFrContainsSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where prenomFr contains DEFAULT_PRENOM_FR
        defaultProfessionnelShouldBeFound("prenomFr.contains=" + DEFAULT_PRENOM_FR);

        // Get all the professionnelList where prenomFr contains UPDATED_PRENOM_FR
        defaultProfessionnelShouldNotBeFound("prenomFr.contains=" + UPDATED_PRENOM_FR);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByPrenomFrNotContainsSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where prenomFr does not contain DEFAULT_PRENOM_FR
        defaultProfessionnelShouldNotBeFound("prenomFr.doesNotContain=" + DEFAULT_PRENOM_FR);

        // Get all the professionnelList where prenomFr does not contain UPDATED_PRENOM_FR
        defaultProfessionnelShouldBeFound("prenomFr.doesNotContain=" + UPDATED_PRENOM_FR);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByProfessionIsEqualToSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where profession equals to DEFAULT_PROFESSION
        defaultProfessionnelShouldBeFound("profession.equals=" + DEFAULT_PROFESSION);

        // Get all the professionnelList where profession equals to UPDATED_PROFESSION
        defaultProfessionnelShouldNotBeFound("profession.equals=" + UPDATED_PROFESSION);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByProfessionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where profession not equals to DEFAULT_PROFESSION
        defaultProfessionnelShouldNotBeFound("profession.notEquals=" + DEFAULT_PROFESSION);

        // Get all the professionnelList where profession not equals to UPDATED_PROFESSION
        defaultProfessionnelShouldBeFound("profession.notEquals=" + UPDATED_PROFESSION);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByProfessionIsInShouldWork() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where profession in DEFAULT_PROFESSION or UPDATED_PROFESSION
        defaultProfessionnelShouldBeFound("profession.in=" + DEFAULT_PROFESSION + "," + UPDATED_PROFESSION);

        // Get all the professionnelList where profession equals to UPDATED_PROFESSION
        defaultProfessionnelShouldNotBeFound("profession.in=" + UPDATED_PROFESSION);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByProfessionIsNullOrNotNull() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where profession is not null
        defaultProfessionnelShouldBeFound("profession.specified=true");

        // Get all the professionnelList where profession is null
        defaultProfessionnelShouldNotBeFound("profession.specified=false");
    }

    @Test
    @Transactional
    void getAllProfessionnelsByProfessionContainsSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where profession contains DEFAULT_PROFESSION
        defaultProfessionnelShouldBeFound("profession.contains=" + DEFAULT_PROFESSION);

        // Get all the professionnelList where profession contains UPDATED_PROFESSION
        defaultProfessionnelShouldNotBeFound("profession.contains=" + UPDATED_PROFESSION);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByProfessionNotContainsSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where profession does not contain DEFAULT_PROFESSION
        defaultProfessionnelShouldNotBeFound("profession.doesNotContain=" + DEFAULT_PROFESSION);

        // Get all the professionnelList where profession does not contain UPDATED_PROFESSION
        defaultProfessionnelShouldBeFound("profession.doesNotContain=" + UPDATED_PROFESSION);
    }

    @Test
    @Transactional
    void getAllProfessionnelsBySelectionnerIsEqualToSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where selectionner equals to DEFAULT_SELECTIONNER
        defaultProfessionnelShouldBeFound("selectionner.equals=" + DEFAULT_SELECTIONNER);

        // Get all the professionnelList where selectionner equals to UPDATED_SELECTIONNER
        defaultProfessionnelShouldNotBeFound("selectionner.equals=" + UPDATED_SELECTIONNER);
    }

    @Test
    @Transactional
    void getAllProfessionnelsBySelectionnerIsNotEqualToSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where selectionner not equals to DEFAULT_SELECTIONNER
        defaultProfessionnelShouldNotBeFound("selectionner.notEquals=" + DEFAULT_SELECTIONNER);

        // Get all the professionnelList where selectionner not equals to UPDATED_SELECTIONNER
        defaultProfessionnelShouldBeFound("selectionner.notEquals=" + UPDATED_SELECTIONNER);
    }

    @Test
    @Transactional
    void getAllProfessionnelsBySelectionnerIsInShouldWork() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where selectionner in DEFAULT_SELECTIONNER or UPDATED_SELECTIONNER
        defaultProfessionnelShouldBeFound("selectionner.in=" + DEFAULT_SELECTIONNER + "," + UPDATED_SELECTIONNER);

        // Get all the professionnelList where selectionner equals to UPDATED_SELECTIONNER
        defaultProfessionnelShouldNotBeFound("selectionner.in=" + UPDATED_SELECTIONNER);
    }

    @Test
    @Transactional
    void getAllProfessionnelsBySelectionnerIsNullOrNotNull() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where selectionner is not null
        defaultProfessionnelShouldBeFound("selectionner.specified=true");

        // Get all the professionnelList where selectionner is null
        defaultProfessionnelShouldNotBeFound("selectionner.specified=false");
    }

    @Test
    @Transactional
    void getAllProfessionnelsBySelectionnerIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where selectionner is greater than or equal to DEFAULT_SELECTIONNER
        defaultProfessionnelShouldBeFound("selectionner.greaterThanOrEqual=" + DEFAULT_SELECTIONNER);

        // Get all the professionnelList where selectionner is greater than or equal to UPDATED_SELECTIONNER
        defaultProfessionnelShouldNotBeFound("selectionner.greaterThanOrEqual=" + UPDATED_SELECTIONNER);
    }

    @Test
    @Transactional
    void getAllProfessionnelsBySelectionnerIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where selectionner is less than or equal to DEFAULT_SELECTIONNER
        defaultProfessionnelShouldBeFound("selectionner.lessThanOrEqual=" + DEFAULT_SELECTIONNER);

        // Get all the professionnelList where selectionner is less than or equal to SMALLER_SELECTIONNER
        defaultProfessionnelShouldNotBeFound("selectionner.lessThanOrEqual=" + SMALLER_SELECTIONNER);
    }

    @Test
    @Transactional
    void getAllProfessionnelsBySelectionnerIsLessThanSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where selectionner is less than DEFAULT_SELECTIONNER
        defaultProfessionnelShouldNotBeFound("selectionner.lessThan=" + DEFAULT_SELECTIONNER);

        // Get all the professionnelList where selectionner is less than UPDATED_SELECTIONNER
        defaultProfessionnelShouldBeFound("selectionner.lessThan=" + UPDATED_SELECTIONNER);
    }

    @Test
    @Transactional
    void getAllProfessionnelsBySelectionnerIsGreaterThanSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where selectionner is greater than DEFAULT_SELECTIONNER
        defaultProfessionnelShouldNotBeFound("selectionner.greaterThan=" + DEFAULT_SELECTIONNER);

        // Get all the professionnelList where selectionner is greater than SMALLER_SELECTIONNER
        defaultProfessionnelShouldBeFound("selectionner.greaterThan=" + SMALLER_SELECTIONNER);
    }

    @Test
    @Transactional
    void getAllProfessionnelsBySexeIsEqualToSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where sexe equals to DEFAULT_SEXE
        defaultProfessionnelShouldBeFound("sexe.equals=" + DEFAULT_SEXE);

        // Get all the professionnelList where sexe equals to UPDATED_SEXE
        defaultProfessionnelShouldNotBeFound("sexe.equals=" + UPDATED_SEXE);
    }

    @Test
    @Transactional
    void getAllProfessionnelsBySexeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where sexe not equals to DEFAULT_SEXE
        defaultProfessionnelShouldNotBeFound("sexe.notEquals=" + DEFAULT_SEXE);

        // Get all the professionnelList where sexe not equals to UPDATED_SEXE
        defaultProfessionnelShouldBeFound("sexe.notEquals=" + UPDATED_SEXE);
    }

    @Test
    @Transactional
    void getAllProfessionnelsBySexeIsInShouldWork() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where sexe in DEFAULT_SEXE or UPDATED_SEXE
        defaultProfessionnelShouldBeFound("sexe.in=" + DEFAULT_SEXE + "," + UPDATED_SEXE);

        // Get all the professionnelList where sexe equals to UPDATED_SEXE
        defaultProfessionnelShouldNotBeFound("sexe.in=" + UPDATED_SEXE);
    }

    @Test
    @Transactional
    void getAllProfessionnelsBySexeIsNullOrNotNull() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where sexe is not null
        defaultProfessionnelShouldBeFound("sexe.specified=true");

        // Get all the professionnelList where sexe is null
        defaultProfessionnelShouldNotBeFound("sexe.specified=false");
    }

    @Test
    @Transactional
    void getAllProfessionnelsBySexeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where sexe is greater than or equal to DEFAULT_SEXE
        defaultProfessionnelShouldBeFound("sexe.greaterThanOrEqual=" + DEFAULT_SEXE);

        // Get all the professionnelList where sexe is greater than or equal to UPDATED_SEXE
        defaultProfessionnelShouldNotBeFound("sexe.greaterThanOrEqual=" + UPDATED_SEXE);
    }

    @Test
    @Transactional
    void getAllProfessionnelsBySexeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where sexe is less than or equal to DEFAULT_SEXE
        defaultProfessionnelShouldBeFound("sexe.lessThanOrEqual=" + DEFAULT_SEXE);

        // Get all the professionnelList where sexe is less than or equal to SMALLER_SEXE
        defaultProfessionnelShouldNotBeFound("sexe.lessThanOrEqual=" + SMALLER_SEXE);
    }

    @Test
    @Transactional
    void getAllProfessionnelsBySexeIsLessThanSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where sexe is less than DEFAULT_SEXE
        defaultProfessionnelShouldNotBeFound("sexe.lessThan=" + DEFAULT_SEXE);

        // Get all the professionnelList where sexe is less than UPDATED_SEXE
        defaultProfessionnelShouldBeFound("sexe.lessThan=" + UPDATED_SEXE);
    }

    @Test
    @Transactional
    void getAllProfessionnelsBySexeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where sexe is greater than DEFAULT_SEXE
        defaultProfessionnelShouldNotBeFound("sexe.greaterThan=" + DEFAULT_SEXE);

        // Get all the professionnelList where sexe is greater than SMALLER_SEXE
        defaultProfessionnelShouldBeFound("sexe.greaterThan=" + SMALLER_SEXE);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByTelephoneIsEqualToSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where telephone equals to DEFAULT_TELEPHONE
        defaultProfessionnelShouldBeFound("telephone.equals=" + DEFAULT_TELEPHONE);

        // Get all the professionnelList where telephone equals to UPDATED_TELEPHONE
        defaultProfessionnelShouldNotBeFound("telephone.equals=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByTelephoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where telephone not equals to DEFAULT_TELEPHONE
        defaultProfessionnelShouldNotBeFound("telephone.notEquals=" + DEFAULT_TELEPHONE);

        // Get all the professionnelList where telephone not equals to UPDATED_TELEPHONE
        defaultProfessionnelShouldBeFound("telephone.notEquals=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByTelephoneIsInShouldWork() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where telephone in DEFAULT_TELEPHONE or UPDATED_TELEPHONE
        defaultProfessionnelShouldBeFound("telephone.in=" + DEFAULT_TELEPHONE + "," + UPDATED_TELEPHONE);

        // Get all the professionnelList where telephone equals to UPDATED_TELEPHONE
        defaultProfessionnelShouldNotBeFound("telephone.in=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByTelephoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where telephone is not null
        defaultProfessionnelShouldBeFound("telephone.specified=true");

        // Get all the professionnelList where telephone is null
        defaultProfessionnelShouldNotBeFound("telephone.specified=false");
    }

    @Test
    @Transactional
    void getAllProfessionnelsByTelephoneContainsSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where telephone contains DEFAULT_TELEPHONE
        defaultProfessionnelShouldBeFound("telephone.contains=" + DEFAULT_TELEPHONE);

        // Get all the professionnelList where telephone contains UPDATED_TELEPHONE
        defaultProfessionnelShouldNotBeFound("telephone.contains=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByTelephoneNotContainsSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where telephone does not contain DEFAULT_TELEPHONE
        defaultProfessionnelShouldNotBeFound("telephone.doesNotContain=" + DEFAULT_TELEPHONE);

        // Get all the professionnelList where telephone does not contain UPDATED_TELEPHONE
        defaultProfessionnelShouldBeFound("telephone.doesNotContain=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByLieuTravailProfessionnelIsEqualToSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where lieuTravailProfessionnel equals to DEFAULT_LIEU_TRAVAIL_PROFESSIONNEL
        defaultProfessionnelShouldBeFound("lieuTravailProfessionnel.equals=" + DEFAULT_LIEU_TRAVAIL_PROFESSIONNEL);

        // Get all the professionnelList where lieuTravailProfessionnel equals to UPDATED_LIEU_TRAVAIL_PROFESSIONNEL
        defaultProfessionnelShouldNotBeFound("lieuTravailProfessionnel.equals=" + UPDATED_LIEU_TRAVAIL_PROFESSIONNEL);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByLieuTravailProfessionnelIsNotEqualToSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where lieuTravailProfessionnel not equals to DEFAULT_LIEU_TRAVAIL_PROFESSIONNEL
        defaultProfessionnelShouldNotBeFound("lieuTravailProfessionnel.notEquals=" + DEFAULT_LIEU_TRAVAIL_PROFESSIONNEL);

        // Get all the professionnelList where lieuTravailProfessionnel not equals to UPDATED_LIEU_TRAVAIL_PROFESSIONNEL
        defaultProfessionnelShouldBeFound("lieuTravailProfessionnel.notEquals=" + UPDATED_LIEU_TRAVAIL_PROFESSIONNEL);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByLieuTravailProfessionnelIsInShouldWork() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where lieuTravailProfessionnel in DEFAULT_LIEU_TRAVAIL_PROFESSIONNEL or UPDATED_LIEU_TRAVAIL_PROFESSIONNEL
        defaultProfessionnelShouldBeFound(
            "lieuTravailProfessionnel.in=" + DEFAULT_LIEU_TRAVAIL_PROFESSIONNEL + "," + UPDATED_LIEU_TRAVAIL_PROFESSIONNEL
        );

        // Get all the professionnelList where lieuTravailProfessionnel equals to UPDATED_LIEU_TRAVAIL_PROFESSIONNEL
        defaultProfessionnelShouldNotBeFound("lieuTravailProfessionnel.in=" + UPDATED_LIEU_TRAVAIL_PROFESSIONNEL);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByLieuTravailProfessionnelIsNullOrNotNull() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where lieuTravailProfessionnel is not null
        defaultProfessionnelShouldBeFound("lieuTravailProfessionnel.specified=true");

        // Get all the professionnelList where lieuTravailProfessionnel is null
        defaultProfessionnelShouldNotBeFound("lieuTravailProfessionnel.specified=false");
    }

    @Test
    @Transactional
    void getAllProfessionnelsByLieuTravailProfessionnelContainsSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where lieuTravailProfessionnel contains DEFAULT_LIEU_TRAVAIL_PROFESSIONNEL
        defaultProfessionnelShouldBeFound("lieuTravailProfessionnel.contains=" + DEFAULT_LIEU_TRAVAIL_PROFESSIONNEL);

        // Get all the professionnelList where lieuTravailProfessionnel contains UPDATED_LIEU_TRAVAIL_PROFESSIONNEL
        defaultProfessionnelShouldNotBeFound("lieuTravailProfessionnel.contains=" + UPDATED_LIEU_TRAVAIL_PROFESSIONNEL);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByLieuTravailProfessionnelNotContainsSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where lieuTravailProfessionnel does not contain DEFAULT_LIEU_TRAVAIL_PROFESSIONNEL
        defaultProfessionnelShouldNotBeFound("lieuTravailProfessionnel.doesNotContain=" + DEFAULT_LIEU_TRAVAIL_PROFESSIONNEL);

        // Get all the professionnelList where lieuTravailProfessionnel does not contain UPDATED_LIEU_TRAVAIL_PROFESSIONNEL
        defaultProfessionnelShouldBeFound("lieuTravailProfessionnel.doesNotContain=" + UPDATED_LIEU_TRAVAIL_PROFESSIONNEL);
    }

    @Test
    @Transactional
    void getAllProfessionnelsBySpecialiteProfessionnelIsEqualToSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where specialiteProfessionnel equals to DEFAULT_SPECIALITE_PROFESSIONNEL
        defaultProfessionnelShouldBeFound("specialiteProfessionnel.equals=" + DEFAULT_SPECIALITE_PROFESSIONNEL);

        // Get all the professionnelList where specialiteProfessionnel equals to UPDATED_SPECIALITE_PROFESSIONNEL
        defaultProfessionnelShouldNotBeFound("specialiteProfessionnel.equals=" + UPDATED_SPECIALITE_PROFESSIONNEL);
    }

    @Test
    @Transactional
    void getAllProfessionnelsBySpecialiteProfessionnelIsNotEqualToSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where specialiteProfessionnel not equals to DEFAULT_SPECIALITE_PROFESSIONNEL
        defaultProfessionnelShouldNotBeFound("specialiteProfessionnel.notEquals=" + DEFAULT_SPECIALITE_PROFESSIONNEL);

        // Get all the professionnelList where specialiteProfessionnel not equals to UPDATED_SPECIALITE_PROFESSIONNEL
        defaultProfessionnelShouldBeFound("specialiteProfessionnel.notEquals=" + UPDATED_SPECIALITE_PROFESSIONNEL);
    }

    @Test
    @Transactional
    void getAllProfessionnelsBySpecialiteProfessionnelIsInShouldWork() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where specialiteProfessionnel in DEFAULT_SPECIALITE_PROFESSIONNEL or UPDATED_SPECIALITE_PROFESSIONNEL
        defaultProfessionnelShouldBeFound(
            "specialiteProfessionnel.in=" + DEFAULT_SPECIALITE_PROFESSIONNEL + "," + UPDATED_SPECIALITE_PROFESSIONNEL
        );

        // Get all the professionnelList where specialiteProfessionnel equals to UPDATED_SPECIALITE_PROFESSIONNEL
        defaultProfessionnelShouldNotBeFound("specialiteProfessionnel.in=" + UPDATED_SPECIALITE_PROFESSIONNEL);
    }

    @Test
    @Transactional
    void getAllProfessionnelsBySpecialiteProfessionnelIsNullOrNotNull() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where specialiteProfessionnel is not null
        defaultProfessionnelShouldBeFound("specialiteProfessionnel.specified=true");

        // Get all the professionnelList where specialiteProfessionnel is null
        defaultProfessionnelShouldNotBeFound("specialiteProfessionnel.specified=false");
    }

    @Test
    @Transactional
    void getAllProfessionnelsBySpecialiteProfessionnelIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where specialiteProfessionnel is greater than or equal to DEFAULT_SPECIALITE_PROFESSIONNEL
        defaultProfessionnelShouldBeFound("specialiteProfessionnel.greaterThanOrEqual=" + DEFAULT_SPECIALITE_PROFESSIONNEL);

        // Get all the professionnelList where specialiteProfessionnel is greater than or equal to UPDATED_SPECIALITE_PROFESSIONNEL
        defaultProfessionnelShouldNotBeFound("specialiteProfessionnel.greaterThanOrEqual=" + UPDATED_SPECIALITE_PROFESSIONNEL);
    }

    @Test
    @Transactional
    void getAllProfessionnelsBySpecialiteProfessionnelIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where specialiteProfessionnel is less than or equal to DEFAULT_SPECIALITE_PROFESSIONNEL
        defaultProfessionnelShouldBeFound("specialiteProfessionnel.lessThanOrEqual=" + DEFAULT_SPECIALITE_PROFESSIONNEL);

        // Get all the professionnelList where specialiteProfessionnel is less than or equal to SMALLER_SPECIALITE_PROFESSIONNEL
        defaultProfessionnelShouldNotBeFound("specialiteProfessionnel.lessThanOrEqual=" + SMALLER_SPECIALITE_PROFESSIONNEL);
    }

    @Test
    @Transactional
    void getAllProfessionnelsBySpecialiteProfessionnelIsLessThanSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where specialiteProfessionnel is less than DEFAULT_SPECIALITE_PROFESSIONNEL
        defaultProfessionnelShouldNotBeFound("specialiteProfessionnel.lessThan=" + DEFAULT_SPECIALITE_PROFESSIONNEL);

        // Get all the professionnelList where specialiteProfessionnel is less than UPDATED_SPECIALITE_PROFESSIONNEL
        defaultProfessionnelShouldBeFound("specialiteProfessionnel.lessThan=" + UPDATED_SPECIALITE_PROFESSIONNEL);
    }

    @Test
    @Transactional
    void getAllProfessionnelsBySpecialiteProfessionnelIsGreaterThanSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList where specialiteProfessionnel is greater than DEFAULT_SPECIALITE_PROFESSIONNEL
        defaultProfessionnelShouldNotBeFound("specialiteProfessionnel.greaterThan=" + DEFAULT_SPECIALITE_PROFESSIONNEL);

        // Get all the professionnelList where specialiteProfessionnel is greater than SMALLER_SPECIALITE_PROFESSIONNEL
        defaultProfessionnelShouldBeFound("specialiteProfessionnel.greaterThan=" + SMALLER_SPECIALITE_PROFESSIONNEL);
    }

    @Test
    @Transactional
    void getAllProfessionnelsByAppUserIsEqualToSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);
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
        professionnel.setAppUser(appUser);
        professionnelRepository.saveAndFlush(professionnel);
        Long appUserId = appUser.getId();

        // Get all the professionnelList where appUser equals to appUserId
        defaultProfessionnelShouldBeFound("appUserId.equals=" + appUserId);

        // Get all the professionnelList where appUser equals to (appUserId + 1)
        defaultProfessionnelShouldNotBeFound("appUserId.equals=" + (appUserId + 1));
    }

    @Test
    @Transactional
    void getAllProfessionnelsByEnfantIsEqualToSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);
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
        professionnel.setEnfant(enfant);
        professionnelRepository.saveAndFlush(professionnel);
        Long enfantId = enfant.getId();

        // Get all the professionnelList where enfant equals to enfantId
        defaultProfessionnelShouldBeFound("enfantId.equals=" + enfantId);

        // Get all the professionnelList where enfant equals to (enfantId + 1)
        defaultProfessionnelShouldNotBeFound("enfantId.equals=" + (enfantId + 1));
    }

    @Test
    @Transactional
    void getAllProfessionnelsByMotifRefusIsEqualToSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);
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
        professionnel.setMotifRefus(motifRefus);
        professionnelRepository.saveAndFlush(professionnel);
        Long motifRefusId = motifRefus.getId();

        // Get all the professionnelList where motifRefus equals to motifRefusId
        defaultProfessionnelShouldBeFound("motifRefusId.equals=" + motifRefusId);

        // Get all the professionnelList where motifRefus equals to (motifRefusId + 1)
        defaultProfessionnelShouldNotBeFound("motifRefusId.equals=" + (motifRefusId + 1));
    }

    @Test
    @Transactional
    void getAllProfessionnelsByProvinceIsEqualToSomething() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);
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
        professionnel.setProvince(province);
        professionnelRepository.saveAndFlush(professionnel);
        Long provinceId = province.getId();

        // Get all the professionnelList where province equals to provinceId
        defaultProfessionnelShouldBeFound("provinceId.equals=" + provinceId);

        // Get all the professionnelList where province equals to (provinceId + 1)
        defaultProfessionnelShouldNotBeFound("provinceId.equals=" + (provinceId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProfessionnelShouldBeFound(String filter) throws Exception {
        restProfessionnelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(professionnel.getId().intValue())))
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
            .andExpect(jsonPath("$.[*].lieuTravailProfessionnel").value(hasItem(DEFAULT_LIEU_TRAVAIL_PROFESSIONNEL)))
            .andExpect(jsonPath("$.[*].specialiteProfessionnel").value(hasItem(DEFAULT_SPECIALITE_PROFESSIONNEL)));

        // Check, that the count call also returns 1
        restProfessionnelMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProfessionnelShouldNotBeFound(String filter) throws Exception {
        restProfessionnelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProfessionnelMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProfessionnel() throws Exception {
        // Get the professionnel
        restProfessionnelMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProfessionnel() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        int databaseSizeBeforeUpdate = professionnelRepository.findAll().size();

        // Update the professionnel
        Professionnel updatedProfessionnel = professionnelRepository.findById(professionnel.getId()).get();
        // Disconnect from session so that the updates on updatedProfessionnel are not directly saved in db
        em.detach(updatedProfessionnel);
        updatedProfessionnel
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
            .lieuTravailProfessionnel(UPDATED_LIEU_TRAVAIL_PROFESSIONNEL)
            .specialiteProfessionnel(UPDATED_SPECIALITE_PROFESSIONNEL);
        ProfessionnelDTO professionnelDTO = professionnelMapper.toDto(updatedProfessionnel);

        restProfessionnelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, professionnelDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(professionnelDTO))
            )
            .andExpect(status().isOk());

        // Validate the Professionnel in the database
        List<Professionnel> professionnelList = professionnelRepository.findAll();
        assertThat(professionnelList).hasSize(databaseSizeBeforeUpdate);
        Professionnel testProfessionnel = professionnelList.get(professionnelList.size() - 1);
        assertThat(testProfessionnel.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testProfessionnel.getBenef2019()).isEqualTo(UPDATED_BENEF_2019);
        assertThat(testProfessionnel.getBenef2020()).isEqualTo(UPDATED_BENEF_2020);
        assertThat(testProfessionnel.getCin()).isEqualTo(UPDATED_CIN);
        assertThat(testProfessionnel.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
        assertThat(testProfessionnel.getDateModification()).isEqualTo(UPDATED_DATE_MODIFICATION);
        assertThat(testProfessionnel.getDateNaissance()).isEqualTo(UPDATED_DATE_NAISSANCE);
        assertThat(testProfessionnel.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testProfessionnel.getEtat()).isEqualTo(UPDATED_ETAT);
        assertThat(testProfessionnel.getExplicationRefus()).isEqualTo(UPDATED_EXPLICATION_REFUS);
        assertThat(testProfessionnel.getNbrEnfants()).isEqualTo(UPDATED_NBR_ENFANTS);
        assertThat(testProfessionnel.getNiveauScolarite()).isEqualTo(UPDATED_NIVEAU_SCOLARITE);
        assertThat(testProfessionnel.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testProfessionnel.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testProfessionnel.getNumeroDossier()).isEqualTo(UPDATED_NUMERO_DOSSIER);
        assertThat(testProfessionnel.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testProfessionnel.getPrenomFr()).isEqualTo(UPDATED_PRENOM_FR);
        assertThat(testProfessionnel.getProfession()).isEqualTo(UPDATED_PROFESSION);
        assertThat(testProfessionnel.getSelectionner()).isEqualTo(UPDATED_SELECTIONNER);
        assertThat(testProfessionnel.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testProfessionnel.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testProfessionnel.getLieuTravailProfessionnel()).isEqualTo(UPDATED_LIEU_TRAVAIL_PROFESSIONNEL);
        assertThat(testProfessionnel.getSpecialiteProfessionnel()).isEqualTo(UPDATED_SPECIALITE_PROFESSIONNEL);
    }

    @Test
    @Transactional
    void putNonExistingProfessionnel() throws Exception {
        int databaseSizeBeforeUpdate = professionnelRepository.findAll().size();
        professionnel.setId(count.incrementAndGet());

        // Create the Professionnel
        ProfessionnelDTO professionnelDTO = professionnelMapper.toDto(professionnel);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfessionnelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, professionnelDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(professionnelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Professionnel in the database
        List<Professionnel> professionnelList = professionnelRepository.findAll();
        assertThat(professionnelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProfessionnel() throws Exception {
        int databaseSizeBeforeUpdate = professionnelRepository.findAll().size();
        professionnel.setId(count.incrementAndGet());

        // Create the Professionnel
        ProfessionnelDTO professionnelDTO = professionnelMapper.toDto(professionnel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfessionnelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(professionnelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Professionnel in the database
        List<Professionnel> professionnelList = professionnelRepository.findAll();
        assertThat(professionnelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProfessionnel() throws Exception {
        int databaseSizeBeforeUpdate = professionnelRepository.findAll().size();
        professionnel.setId(count.incrementAndGet());

        // Create the Professionnel
        ProfessionnelDTO professionnelDTO = professionnelMapper.toDto(professionnel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfessionnelMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(professionnelDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Professionnel in the database
        List<Professionnel> professionnelList = professionnelRepository.findAll();
        assertThat(professionnelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProfessionnelWithPatch() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        int databaseSizeBeforeUpdate = professionnelRepository.findAll().size();

        // Update the professionnel using partial update
        Professionnel partialUpdatedProfessionnel = new Professionnel();
        partialUpdatedProfessionnel.setId(professionnel.getId());

        partialUpdatedProfessionnel
            .adresse(UPDATED_ADRESSE)
            .benef2019(UPDATED_BENEF_2019)
            .dateModification(UPDATED_DATE_MODIFICATION)
            .dateNaissance(UPDATED_DATE_NAISSANCE)
            .explicationRefus(UPDATED_EXPLICATION_REFUS)
            .niveauScolarite(UPDATED_NIVEAU_SCOLARITE)
            .prenom(UPDATED_PRENOM)
            .profession(UPDATED_PROFESSION)
            .selectionner(UPDATED_SELECTIONNER)
            .telephone(UPDATED_TELEPHONE)
            .lieuTravailProfessionnel(UPDATED_LIEU_TRAVAIL_PROFESSIONNEL)
            .specialiteProfessionnel(UPDATED_SPECIALITE_PROFESSIONNEL);

        restProfessionnelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProfessionnel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProfessionnel))
            )
            .andExpect(status().isOk());

        // Validate the Professionnel in the database
        List<Professionnel> professionnelList = professionnelRepository.findAll();
        assertThat(professionnelList).hasSize(databaseSizeBeforeUpdate);
        Professionnel testProfessionnel = professionnelList.get(professionnelList.size() - 1);
        assertThat(testProfessionnel.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testProfessionnel.getBenef2019()).isEqualTo(UPDATED_BENEF_2019);
        assertThat(testProfessionnel.getBenef2020()).isEqualTo(DEFAULT_BENEF_2020);
        assertThat(testProfessionnel.getCin()).isEqualTo(DEFAULT_CIN);
        assertThat(testProfessionnel.getDateCreation()).isEqualTo(DEFAULT_DATE_CREATION);
        assertThat(testProfessionnel.getDateModification()).isEqualTo(UPDATED_DATE_MODIFICATION);
        assertThat(testProfessionnel.getDateNaissance()).isEqualTo(UPDATED_DATE_NAISSANCE);
        assertThat(testProfessionnel.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testProfessionnel.getEtat()).isEqualTo(DEFAULT_ETAT);
        assertThat(testProfessionnel.getExplicationRefus()).isEqualTo(UPDATED_EXPLICATION_REFUS);
        assertThat(testProfessionnel.getNbrEnfants()).isEqualTo(DEFAULT_NBR_ENFANTS);
        assertThat(testProfessionnel.getNiveauScolarite()).isEqualTo(UPDATED_NIVEAU_SCOLARITE);
        assertThat(testProfessionnel.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testProfessionnel.getNomFr()).isEqualTo(DEFAULT_NOM_FR);
        assertThat(testProfessionnel.getNumeroDossier()).isEqualTo(DEFAULT_NUMERO_DOSSIER);
        assertThat(testProfessionnel.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testProfessionnel.getPrenomFr()).isEqualTo(DEFAULT_PRENOM_FR);
        assertThat(testProfessionnel.getProfession()).isEqualTo(UPDATED_PROFESSION);
        assertThat(testProfessionnel.getSelectionner()).isEqualTo(UPDATED_SELECTIONNER);
        assertThat(testProfessionnel.getSexe()).isEqualTo(DEFAULT_SEXE);
        assertThat(testProfessionnel.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testProfessionnel.getLieuTravailProfessionnel()).isEqualTo(UPDATED_LIEU_TRAVAIL_PROFESSIONNEL);
        assertThat(testProfessionnel.getSpecialiteProfessionnel()).isEqualTo(UPDATED_SPECIALITE_PROFESSIONNEL);
    }

    @Test
    @Transactional
    void fullUpdateProfessionnelWithPatch() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        int databaseSizeBeforeUpdate = professionnelRepository.findAll().size();

        // Update the professionnel using partial update
        Professionnel partialUpdatedProfessionnel = new Professionnel();
        partialUpdatedProfessionnel.setId(professionnel.getId());

        partialUpdatedProfessionnel
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
            .lieuTravailProfessionnel(UPDATED_LIEU_TRAVAIL_PROFESSIONNEL)
            .specialiteProfessionnel(UPDATED_SPECIALITE_PROFESSIONNEL);

        restProfessionnelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProfessionnel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProfessionnel))
            )
            .andExpect(status().isOk());

        // Validate the Professionnel in the database
        List<Professionnel> professionnelList = professionnelRepository.findAll();
        assertThat(professionnelList).hasSize(databaseSizeBeforeUpdate);
        Professionnel testProfessionnel = professionnelList.get(professionnelList.size() - 1);
        assertThat(testProfessionnel.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testProfessionnel.getBenef2019()).isEqualTo(UPDATED_BENEF_2019);
        assertThat(testProfessionnel.getBenef2020()).isEqualTo(UPDATED_BENEF_2020);
        assertThat(testProfessionnel.getCin()).isEqualTo(UPDATED_CIN);
        assertThat(testProfessionnel.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
        assertThat(testProfessionnel.getDateModification()).isEqualTo(UPDATED_DATE_MODIFICATION);
        assertThat(testProfessionnel.getDateNaissance()).isEqualTo(UPDATED_DATE_NAISSANCE);
        assertThat(testProfessionnel.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testProfessionnel.getEtat()).isEqualTo(UPDATED_ETAT);
        assertThat(testProfessionnel.getExplicationRefus()).isEqualTo(UPDATED_EXPLICATION_REFUS);
        assertThat(testProfessionnel.getNbrEnfants()).isEqualTo(UPDATED_NBR_ENFANTS);
        assertThat(testProfessionnel.getNiveauScolarite()).isEqualTo(UPDATED_NIVEAU_SCOLARITE);
        assertThat(testProfessionnel.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testProfessionnel.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testProfessionnel.getNumeroDossier()).isEqualTo(UPDATED_NUMERO_DOSSIER);
        assertThat(testProfessionnel.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testProfessionnel.getPrenomFr()).isEqualTo(UPDATED_PRENOM_FR);
        assertThat(testProfessionnel.getProfession()).isEqualTo(UPDATED_PROFESSION);
        assertThat(testProfessionnel.getSelectionner()).isEqualTo(UPDATED_SELECTIONNER);
        assertThat(testProfessionnel.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testProfessionnel.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testProfessionnel.getLieuTravailProfessionnel()).isEqualTo(UPDATED_LIEU_TRAVAIL_PROFESSIONNEL);
        assertThat(testProfessionnel.getSpecialiteProfessionnel()).isEqualTo(UPDATED_SPECIALITE_PROFESSIONNEL);
    }

    @Test
    @Transactional
    void patchNonExistingProfessionnel() throws Exception {
        int databaseSizeBeforeUpdate = professionnelRepository.findAll().size();
        professionnel.setId(count.incrementAndGet());

        // Create the Professionnel
        ProfessionnelDTO professionnelDTO = professionnelMapper.toDto(professionnel);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfessionnelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, professionnelDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(professionnelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Professionnel in the database
        List<Professionnel> professionnelList = professionnelRepository.findAll();
        assertThat(professionnelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProfessionnel() throws Exception {
        int databaseSizeBeforeUpdate = professionnelRepository.findAll().size();
        professionnel.setId(count.incrementAndGet());

        // Create the Professionnel
        ProfessionnelDTO professionnelDTO = professionnelMapper.toDto(professionnel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfessionnelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(professionnelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Professionnel in the database
        List<Professionnel> professionnelList = professionnelRepository.findAll();
        assertThat(professionnelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProfessionnel() throws Exception {
        int databaseSizeBeforeUpdate = professionnelRepository.findAll().size();
        professionnel.setId(count.incrementAndGet());

        // Create the Professionnel
        ProfessionnelDTO professionnelDTO = professionnelMapper.toDto(professionnel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfessionnelMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(professionnelDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Professionnel in the database
        List<Professionnel> professionnelList = professionnelRepository.findAll();
        assertThat(professionnelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProfessionnel() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        int databaseSizeBeforeDelete = professionnelRepository.findAll().size();

        // Delete the professionnel
        restProfessionnelMockMvc
            .perform(delete(ENTITY_API_URL_ID, professionnel.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Professionnel> professionnelList = professionnelRepository.findAll();
        assertThat(professionnelList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
