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
import ma.raafiq.domain.Beneficiaire;
import ma.raafiq.domain.Enfant;
import ma.raafiq.domain.MotifRefus;
import ma.raafiq.domain.Province;
import ma.raafiq.repository.BeneficiaireRepository;
import ma.raafiq.service.criteria.BeneficiaireCriteria;
import ma.raafiq.service.dto.BeneficiaireDTO;
import ma.raafiq.service.mapper.BeneficiaireMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link BeneficiaireResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BeneficiaireResourceIT {

    private static final String DEFAULT_TYPE_BENEFICIARE = "AAA";
    private static final String UPDATED_TYPE_BENEFICIARE = "BBB";

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

    private static final String DEFAULT_LIEU_TRAVAIL_PROFESSIONNEL = "AAAAAAAAAA";
    private static final String UPDATED_LIEU_TRAVAIL_PROFESSIONNEL = "BBBBBBBBBB";

    private static final Integer DEFAULT_SPECIALITE_PROFESSIONNEL = 1;
    private static final Integer UPDATED_SPECIALITE_PROFESSIONNEL = 2;
    private static final Integer SMALLER_SPECIALITE_PROFESSIONNEL = 1 - 1;

    private static final String ENTITY_API_URL = "/api/beneficiaires";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BeneficiaireRepository beneficiaireRepository;

    @Autowired
    private BeneficiaireMapper beneficiaireMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBeneficiaireMockMvc;

    private Beneficiaire beneficiaire;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Beneficiaire createEntity(EntityManager em) {
        Beneficiaire beneficiaire = new Beneficiaire()
            .typeBeneficiare(DEFAULT_TYPE_BENEFICIARE)
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
            .relationFamiliale(DEFAULT_RELATION_FAMILIALE)
            .lieuTravailProfessionnel(DEFAULT_LIEU_TRAVAIL_PROFESSIONNEL)
            .specialiteProfessionnel(DEFAULT_SPECIALITE_PROFESSIONNEL);
        return beneficiaire;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Beneficiaire createUpdatedEntity(EntityManager em) {
        Beneficiaire beneficiaire = new Beneficiaire()
            .typeBeneficiare(UPDATED_TYPE_BENEFICIARE)
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
            .relationFamiliale(UPDATED_RELATION_FAMILIALE)
            .lieuTravailProfessionnel(UPDATED_LIEU_TRAVAIL_PROFESSIONNEL)
            .specialiteProfessionnel(UPDATED_SPECIALITE_PROFESSIONNEL);
        return beneficiaire;
    }

    @BeforeEach
    public void initTest() {
        beneficiaire = createEntity(em);
    }

    @Test
    @Transactional
    void createBeneficiaire() throws Exception {
        int databaseSizeBeforeCreate = beneficiaireRepository.findAll().size();
        // Create the Beneficiaire
        BeneficiaireDTO beneficiaireDTO = beneficiaireMapper.toDto(beneficiaire);
        restBeneficiaireMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(beneficiaireDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Beneficiaire in the database
        List<Beneficiaire> beneficiaireList = beneficiaireRepository.findAll();
        assertThat(beneficiaireList).hasSize(databaseSizeBeforeCreate + 1);
        Beneficiaire testBeneficiaire = beneficiaireList.get(beneficiaireList.size() - 1);
        assertThat(testBeneficiaire.getTypeBeneficiare()).isEqualTo(DEFAULT_TYPE_BENEFICIARE);
        assertThat(testBeneficiaire.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testBeneficiaire.getBenef2019()).isEqualTo(DEFAULT_BENEF_2019);
        assertThat(testBeneficiaire.getBenef2020()).isEqualTo(DEFAULT_BENEF_2020);
        assertThat(testBeneficiaire.getCin()).isEqualTo(DEFAULT_CIN);
        assertThat(testBeneficiaire.getDateCreation()).isEqualTo(DEFAULT_DATE_CREATION);
        assertThat(testBeneficiaire.getDateModification()).isEqualTo(DEFAULT_DATE_MODIFICATION);
        assertThat(testBeneficiaire.getDateNaissance()).isEqualTo(DEFAULT_DATE_NAISSANCE);
        assertThat(testBeneficiaire.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testBeneficiaire.getEtat()).isEqualTo(DEFAULT_ETAT);
        assertThat(testBeneficiaire.getExplicationRefus()).isEqualTo(DEFAULT_EXPLICATION_REFUS);
        assertThat(testBeneficiaire.getNbrEnfants()).isEqualTo(DEFAULT_NBR_ENFANTS);
        assertThat(testBeneficiaire.getNiveauScolarite()).isEqualTo(DEFAULT_NIVEAU_SCOLARITE);
        assertThat(testBeneficiaire.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testBeneficiaire.getNomFr()).isEqualTo(DEFAULT_NOM_FR);
        assertThat(testBeneficiaire.getNumeroDossier()).isEqualTo(DEFAULT_NUMERO_DOSSIER);
        assertThat(testBeneficiaire.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testBeneficiaire.getPrenomFr()).isEqualTo(DEFAULT_PRENOM_FR);
        assertThat(testBeneficiaire.getProfession()).isEqualTo(DEFAULT_PROFESSION);
        assertThat(testBeneficiaire.getSelectionner()).isEqualTo(DEFAULT_SELECTIONNER);
        assertThat(testBeneficiaire.getSexe()).isEqualTo(DEFAULT_SEXE);
        assertThat(testBeneficiaire.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
        assertThat(testBeneficiaire.getAutreBenef2019()).isEqualTo(DEFAULT_AUTRE_BENEF_2019);
        assertThat(testBeneficiaire.getAutreBenef2020()).isEqualTo(DEFAULT_AUTRE_BENEF_2020);
        assertThat(testBeneficiaire.getRelationFamiliale()).isEqualTo(DEFAULT_RELATION_FAMILIALE);
        assertThat(testBeneficiaire.getLieuTravailProfessionnel()).isEqualTo(DEFAULT_LIEU_TRAVAIL_PROFESSIONNEL);
        assertThat(testBeneficiaire.getSpecialiteProfessionnel()).isEqualTo(DEFAULT_SPECIALITE_PROFESSIONNEL);
    }

    @Test
    @Transactional
    void createBeneficiaireWithExistingId() throws Exception {
        // Create the Beneficiaire with an existing ID
        beneficiaire.setId(1L);
        BeneficiaireDTO beneficiaireDTO = beneficiaireMapper.toDto(beneficiaire);

        int databaseSizeBeforeCreate = beneficiaireRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBeneficiaireMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(beneficiaireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Beneficiaire in the database
        List<Beneficiaire> beneficiaireList = beneficiaireRepository.findAll();
        assertThat(beneficiaireList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTypeBeneficiareIsRequired() throws Exception {
        int databaseSizeBeforeTest = beneficiaireRepository.findAll().size();
        // set the field null
        beneficiaire.setTypeBeneficiare(null);

        // Create the Beneficiaire, which fails.
        BeneficiaireDTO beneficiaireDTO = beneficiaireMapper.toDto(beneficiaire);

        restBeneficiaireMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(beneficiaireDTO))
            )
            .andExpect(status().isBadRequest());

        List<Beneficiaire> beneficiaireList = beneficiaireRepository.findAll();
        assertThat(beneficiaireList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCinIsRequired() throws Exception {
        int databaseSizeBeforeTest = beneficiaireRepository.findAll().size();
        // set the field null
        beneficiaire.setCin(null);

        // Create the Beneficiaire, which fails.
        BeneficiaireDTO beneficiaireDTO = beneficiaireMapper.toDto(beneficiaire);

        restBeneficiaireMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(beneficiaireDTO))
            )
            .andExpect(status().isBadRequest());

        List<Beneficiaire> beneficiaireList = beneficiaireRepository.findAll();
        assertThat(beneficiaireList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = beneficiaireRepository.findAll().size();
        // set the field null
        beneficiaire.setEmail(null);

        // Create the Beneficiaire, which fails.
        BeneficiaireDTO beneficiaireDTO = beneficiaireMapper.toDto(beneficiaire);

        restBeneficiaireMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(beneficiaireDTO))
            )
            .andExpect(status().isBadRequest());

        List<Beneficiaire> beneficiaireList = beneficiaireRepository.findAll();
        assertThat(beneficiaireList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNbrEnfantsIsRequired() throws Exception {
        int databaseSizeBeforeTest = beneficiaireRepository.findAll().size();
        // set the field null
        beneficiaire.setNbrEnfants(null);

        // Create the Beneficiaire, which fails.
        BeneficiaireDTO beneficiaireDTO = beneficiaireMapper.toDto(beneficiaire);

        restBeneficiaireMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(beneficiaireDTO))
            )
            .andExpect(status().isBadRequest());

        List<Beneficiaire> beneficiaireList = beneficiaireRepository.findAll();
        assertThat(beneficiaireList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNumeroDossierIsRequired() throws Exception {
        int databaseSizeBeforeTest = beneficiaireRepository.findAll().size();
        // set the field null
        beneficiaire.setNumeroDossier(null);

        // Create the Beneficiaire, which fails.
        BeneficiaireDTO beneficiaireDTO = beneficiaireMapper.toDto(beneficiaire);

        restBeneficiaireMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(beneficiaireDTO))
            )
            .andExpect(status().isBadRequest());

        List<Beneficiaire> beneficiaireList = beneficiaireRepository.findAll();
        assertThat(beneficiaireList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBeneficiaires() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList
        restBeneficiaireMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(beneficiaire.getId().intValue())))
            .andExpect(jsonPath("$.[*].typeBeneficiare").value(hasItem(DEFAULT_TYPE_BENEFICIARE)))
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
            .andExpect(jsonPath("$.[*].relationFamiliale").value(hasItem(DEFAULT_RELATION_FAMILIALE)))
            .andExpect(jsonPath("$.[*].lieuTravailProfessionnel").value(hasItem(DEFAULT_LIEU_TRAVAIL_PROFESSIONNEL)))
            .andExpect(jsonPath("$.[*].specialiteProfessionnel").value(hasItem(DEFAULT_SPECIALITE_PROFESSIONNEL)));
    }

    @Test
    @Transactional
    void getBeneficiaire() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get the beneficiaire
        restBeneficiaireMockMvc
            .perform(get(ENTITY_API_URL_ID, beneficiaire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(beneficiaire.getId().intValue()))
            .andExpect(jsonPath("$.typeBeneficiare").value(DEFAULT_TYPE_BENEFICIARE))
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
            .andExpect(jsonPath("$.relationFamiliale").value(DEFAULT_RELATION_FAMILIALE))
            .andExpect(jsonPath("$.lieuTravailProfessionnel").value(DEFAULT_LIEU_TRAVAIL_PROFESSIONNEL))
            .andExpect(jsonPath("$.specialiteProfessionnel").value(DEFAULT_SPECIALITE_PROFESSIONNEL));
    }

    @Test
    @Transactional
    void getBeneficiairesByIdFiltering() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        Long id = beneficiaire.getId();

        defaultBeneficiaireShouldBeFound("id.equals=" + id);
        defaultBeneficiaireShouldNotBeFound("id.notEquals=" + id);

        defaultBeneficiaireShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultBeneficiaireShouldNotBeFound("id.greaterThan=" + id);

        defaultBeneficiaireShouldBeFound("id.lessThanOrEqual=" + id);
        defaultBeneficiaireShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByTypeBeneficiareIsEqualToSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where typeBeneficiare equals to DEFAULT_TYPE_BENEFICIARE
        defaultBeneficiaireShouldBeFound("typeBeneficiare.equals=" + DEFAULT_TYPE_BENEFICIARE);

        // Get all the beneficiaireList where typeBeneficiare equals to UPDATED_TYPE_BENEFICIARE
        defaultBeneficiaireShouldNotBeFound("typeBeneficiare.equals=" + UPDATED_TYPE_BENEFICIARE);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByTypeBeneficiareIsNotEqualToSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where typeBeneficiare not equals to DEFAULT_TYPE_BENEFICIARE
        defaultBeneficiaireShouldNotBeFound("typeBeneficiare.notEquals=" + DEFAULT_TYPE_BENEFICIARE);

        // Get all the beneficiaireList where typeBeneficiare not equals to UPDATED_TYPE_BENEFICIARE
        defaultBeneficiaireShouldBeFound("typeBeneficiare.notEquals=" + UPDATED_TYPE_BENEFICIARE);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByTypeBeneficiareIsInShouldWork() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where typeBeneficiare in DEFAULT_TYPE_BENEFICIARE or UPDATED_TYPE_BENEFICIARE
        defaultBeneficiaireShouldBeFound("typeBeneficiare.in=" + DEFAULT_TYPE_BENEFICIARE + "," + UPDATED_TYPE_BENEFICIARE);

        // Get all the beneficiaireList where typeBeneficiare equals to UPDATED_TYPE_BENEFICIARE
        defaultBeneficiaireShouldNotBeFound("typeBeneficiare.in=" + UPDATED_TYPE_BENEFICIARE);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByTypeBeneficiareIsNullOrNotNull() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where typeBeneficiare is not null
        defaultBeneficiaireShouldBeFound("typeBeneficiare.specified=true");

        // Get all the beneficiaireList where typeBeneficiare is null
        defaultBeneficiaireShouldNotBeFound("typeBeneficiare.specified=false");
    }

    @Test
    @Transactional
    void getAllBeneficiairesByTypeBeneficiareContainsSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where typeBeneficiare contains DEFAULT_TYPE_BENEFICIARE
        defaultBeneficiaireShouldBeFound("typeBeneficiare.contains=" + DEFAULT_TYPE_BENEFICIARE);

        // Get all the beneficiaireList where typeBeneficiare contains UPDATED_TYPE_BENEFICIARE
        defaultBeneficiaireShouldNotBeFound("typeBeneficiare.contains=" + UPDATED_TYPE_BENEFICIARE);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByTypeBeneficiareNotContainsSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where typeBeneficiare does not contain DEFAULT_TYPE_BENEFICIARE
        defaultBeneficiaireShouldNotBeFound("typeBeneficiare.doesNotContain=" + DEFAULT_TYPE_BENEFICIARE);

        // Get all the beneficiaireList where typeBeneficiare does not contain UPDATED_TYPE_BENEFICIARE
        defaultBeneficiaireShouldBeFound("typeBeneficiare.doesNotContain=" + UPDATED_TYPE_BENEFICIARE);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByAdresseIsEqualToSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where adresse equals to DEFAULT_ADRESSE
        defaultBeneficiaireShouldBeFound("adresse.equals=" + DEFAULT_ADRESSE);

        // Get all the beneficiaireList where adresse equals to UPDATED_ADRESSE
        defaultBeneficiaireShouldNotBeFound("adresse.equals=" + UPDATED_ADRESSE);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByAdresseIsNotEqualToSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where adresse not equals to DEFAULT_ADRESSE
        defaultBeneficiaireShouldNotBeFound("adresse.notEquals=" + DEFAULT_ADRESSE);

        // Get all the beneficiaireList where adresse not equals to UPDATED_ADRESSE
        defaultBeneficiaireShouldBeFound("adresse.notEquals=" + UPDATED_ADRESSE);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByAdresseIsInShouldWork() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where adresse in DEFAULT_ADRESSE or UPDATED_ADRESSE
        defaultBeneficiaireShouldBeFound("adresse.in=" + DEFAULT_ADRESSE + "," + UPDATED_ADRESSE);

        // Get all the beneficiaireList where adresse equals to UPDATED_ADRESSE
        defaultBeneficiaireShouldNotBeFound("adresse.in=" + UPDATED_ADRESSE);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByAdresseIsNullOrNotNull() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where adresse is not null
        defaultBeneficiaireShouldBeFound("adresse.specified=true");

        // Get all the beneficiaireList where adresse is null
        defaultBeneficiaireShouldNotBeFound("adresse.specified=false");
    }

    @Test
    @Transactional
    void getAllBeneficiairesByAdresseContainsSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where adresse contains DEFAULT_ADRESSE
        defaultBeneficiaireShouldBeFound("adresse.contains=" + DEFAULT_ADRESSE);

        // Get all the beneficiaireList where adresse contains UPDATED_ADRESSE
        defaultBeneficiaireShouldNotBeFound("adresse.contains=" + UPDATED_ADRESSE);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByAdresseNotContainsSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where adresse does not contain DEFAULT_ADRESSE
        defaultBeneficiaireShouldNotBeFound("adresse.doesNotContain=" + DEFAULT_ADRESSE);

        // Get all the beneficiaireList where adresse does not contain UPDATED_ADRESSE
        defaultBeneficiaireShouldBeFound("adresse.doesNotContain=" + UPDATED_ADRESSE);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByBenef2019IsEqualToSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where benef2019 equals to DEFAULT_BENEF_2019
        defaultBeneficiaireShouldBeFound("benef2019.equals=" + DEFAULT_BENEF_2019);

        // Get all the beneficiaireList where benef2019 equals to UPDATED_BENEF_2019
        defaultBeneficiaireShouldNotBeFound("benef2019.equals=" + UPDATED_BENEF_2019);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByBenef2019IsNotEqualToSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where benef2019 not equals to DEFAULT_BENEF_2019
        defaultBeneficiaireShouldNotBeFound("benef2019.notEquals=" + DEFAULT_BENEF_2019);

        // Get all the beneficiaireList where benef2019 not equals to UPDATED_BENEF_2019
        defaultBeneficiaireShouldBeFound("benef2019.notEquals=" + UPDATED_BENEF_2019);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByBenef2019IsInShouldWork() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where benef2019 in DEFAULT_BENEF_2019 or UPDATED_BENEF_2019
        defaultBeneficiaireShouldBeFound("benef2019.in=" + DEFAULT_BENEF_2019 + "," + UPDATED_BENEF_2019);

        // Get all the beneficiaireList where benef2019 equals to UPDATED_BENEF_2019
        defaultBeneficiaireShouldNotBeFound("benef2019.in=" + UPDATED_BENEF_2019);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByBenef2019IsNullOrNotNull() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where benef2019 is not null
        defaultBeneficiaireShouldBeFound("benef2019.specified=true");

        // Get all the beneficiaireList where benef2019 is null
        defaultBeneficiaireShouldNotBeFound("benef2019.specified=false");
    }

    @Test
    @Transactional
    void getAllBeneficiairesByBenef2019IsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where benef2019 is greater than or equal to DEFAULT_BENEF_2019
        defaultBeneficiaireShouldBeFound("benef2019.greaterThanOrEqual=" + DEFAULT_BENEF_2019);

        // Get all the beneficiaireList where benef2019 is greater than or equal to UPDATED_BENEF_2019
        defaultBeneficiaireShouldNotBeFound("benef2019.greaterThanOrEqual=" + UPDATED_BENEF_2019);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByBenef2019IsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where benef2019 is less than or equal to DEFAULT_BENEF_2019
        defaultBeneficiaireShouldBeFound("benef2019.lessThanOrEqual=" + DEFAULT_BENEF_2019);

        // Get all the beneficiaireList where benef2019 is less than or equal to SMALLER_BENEF_2019
        defaultBeneficiaireShouldNotBeFound("benef2019.lessThanOrEqual=" + SMALLER_BENEF_2019);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByBenef2019IsLessThanSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where benef2019 is less than DEFAULT_BENEF_2019
        defaultBeneficiaireShouldNotBeFound("benef2019.lessThan=" + DEFAULT_BENEF_2019);

        // Get all the beneficiaireList where benef2019 is less than UPDATED_BENEF_2019
        defaultBeneficiaireShouldBeFound("benef2019.lessThan=" + UPDATED_BENEF_2019);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByBenef2019IsGreaterThanSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where benef2019 is greater than DEFAULT_BENEF_2019
        defaultBeneficiaireShouldNotBeFound("benef2019.greaterThan=" + DEFAULT_BENEF_2019);

        // Get all the beneficiaireList where benef2019 is greater than SMALLER_BENEF_2019
        defaultBeneficiaireShouldBeFound("benef2019.greaterThan=" + SMALLER_BENEF_2019);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByBenef2020IsEqualToSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where benef2020 equals to DEFAULT_BENEF_2020
        defaultBeneficiaireShouldBeFound("benef2020.equals=" + DEFAULT_BENEF_2020);

        // Get all the beneficiaireList where benef2020 equals to UPDATED_BENEF_2020
        defaultBeneficiaireShouldNotBeFound("benef2020.equals=" + UPDATED_BENEF_2020);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByBenef2020IsNotEqualToSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where benef2020 not equals to DEFAULT_BENEF_2020
        defaultBeneficiaireShouldNotBeFound("benef2020.notEquals=" + DEFAULT_BENEF_2020);

        // Get all the beneficiaireList where benef2020 not equals to UPDATED_BENEF_2020
        defaultBeneficiaireShouldBeFound("benef2020.notEquals=" + UPDATED_BENEF_2020);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByBenef2020IsInShouldWork() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where benef2020 in DEFAULT_BENEF_2020 or UPDATED_BENEF_2020
        defaultBeneficiaireShouldBeFound("benef2020.in=" + DEFAULT_BENEF_2020 + "," + UPDATED_BENEF_2020);

        // Get all the beneficiaireList where benef2020 equals to UPDATED_BENEF_2020
        defaultBeneficiaireShouldNotBeFound("benef2020.in=" + UPDATED_BENEF_2020);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByBenef2020IsNullOrNotNull() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where benef2020 is not null
        defaultBeneficiaireShouldBeFound("benef2020.specified=true");

        // Get all the beneficiaireList where benef2020 is null
        defaultBeneficiaireShouldNotBeFound("benef2020.specified=false");
    }

    @Test
    @Transactional
    void getAllBeneficiairesByBenef2020IsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where benef2020 is greater than or equal to DEFAULT_BENEF_2020
        defaultBeneficiaireShouldBeFound("benef2020.greaterThanOrEqual=" + DEFAULT_BENEF_2020);

        // Get all the beneficiaireList where benef2020 is greater than or equal to UPDATED_BENEF_2020
        defaultBeneficiaireShouldNotBeFound("benef2020.greaterThanOrEqual=" + UPDATED_BENEF_2020);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByBenef2020IsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where benef2020 is less than or equal to DEFAULT_BENEF_2020
        defaultBeneficiaireShouldBeFound("benef2020.lessThanOrEqual=" + DEFAULT_BENEF_2020);

        // Get all the beneficiaireList where benef2020 is less than or equal to SMALLER_BENEF_2020
        defaultBeneficiaireShouldNotBeFound("benef2020.lessThanOrEqual=" + SMALLER_BENEF_2020);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByBenef2020IsLessThanSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where benef2020 is less than DEFAULT_BENEF_2020
        defaultBeneficiaireShouldNotBeFound("benef2020.lessThan=" + DEFAULT_BENEF_2020);

        // Get all the beneficiaireList where benef2020 is less than UPDATED_BENEF_2020
        defaultBeneficiaireShouldBeFound("benef2020.lessThan=" + UPDATED_BENEF_2020);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByBenef2020IsGreaterThanSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where benef2020 is greater than DEFAULT_BENEF_2020
        defaultBeneficiaireShouldNotBeFound("benef2020.greaterThan=" + DEFAULT_BENEF_2020);

        // Get all the beneficiaireList where benef2020 is greater than SMALLER_BENEF_2020
        defaultBeneficiaireShouldBeFound("benef2020.greaterThan=" + SMALLER_BENEF_2020);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByCinIsEqualToSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where cin equals to DEFAULT_CIN
        defaultBeneficiaireShouldBeFound("cin.equals=" + DEFAULT_CIN);

        // Get all the beneficiaireList where cin equals to UPDATED_CIN
        defaultBeneficiaireShouldNotBeFound("cin.equals=" + UPDATED_CIN);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByCinIsNotEqualToSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where cin not equals to DEFAULT_CIN
        defaultBeneficiaireShouldNotBeFound("cin.notEquals=" + DEFAULT_CIN);

        // Get all the beneficiaireList where cin not equals to UPDATED_CIN
        defaultBeneficiaireShouldBeFound("cin.notEquals=" + UPDATED_CIN);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByCinIsInShouldWork() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where cin in DEFAULT_CIN or UPDATED_CIN
        defaultBeneficiaireShouldBeFound("cin.in=" + DEFAULT_CIN + "," + UPDATED_CIN);

        // Get all the beneficiaireList where cin equals to UPDATED_CIN
        defaultBeneficiaireShouldNotBeFound("cin.in=" + UPDATED_CIN);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByCinIsNullOrNotNull() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where cin is not null
        defaultBeneficiaireShouldBeFound("cin.specified=true");

        // Get all the beneficiaireList where cin is null
        defaultBeneficiaireShouldNotBeFound("cin.specified=false");
    }

    @Test
    @Transactional
    void getAllBeneficiairesByCinContainsSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where cin contains DEFAULT_CIN
        defaultBeneficiaireShouldBeFound("cin.contains=" + DEFAULT_CIN);

        // Get all the beneficiaireList where cin contains UPDATED_CIN
        defaultBeneficiaireShouldNotBeFound("cin.contains=" + UPDATED_CIN);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByCinNotContainsSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where cin does not contain DEFAULT_CIN
        defaultBeneficiaireShouldNotBeFound("cin.doesNotContain=" + DEFAULT_CIN);

        // Get all the beneficiaireList where cin does not contain UPDATED_CIN
        defaultBeneficiaireShouldBeFound("cin.doesNotContain=" + UPDATED_CIN);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByDateCreationIsEqualToSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where dateCreation equals to DEFAULT_DATE_CREATION
        defaultBeneficiaireShouldBeFound("dateCreation.equals=" + DEFAULT_DATE_CREATION);

        // Get all the beneficiaireList where dateCreation equals to UPDATED_DATE_CREATION
        defaultBeneficiaireShouldNotBeFound("dateCreation.equals=" + UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByDateCreationIsNotEqualToSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where dateCreation not equals to DEFAULT_DATE_CREATION
        defaultBeneficiaireShouldNotBeFound("dateCreation.notEquals=" + DEFAULT_DATE_CREATION);

        // Get all the beneficiaireList where dateCreation not equals to UPDATED_DATE_CREATION
        defaultBeneficiaireShouldBeFound("dateCreation.notEquals=" + UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByDateCreationIsInShouldWork() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where dateCreation in DEFAULT_DATE_CREATION or UPDATED_DATE_CREATION
        defaultBeneficiaireShouldBeFound("dateCreation.in=" + DEFAULT_DATE_CREATION + "," + UPDATED_DATE_CREATION);

        // Get all the beneficiaireList where dateCreation equals to UPDATED_DATE_CREATION
        defaultBeneficiaireShouldNotBeFound("dateCreation.in=" + UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByDateCreationIsNullOrNotNull() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where dateCreation is not null
        defaultBeneficiaireShouldBeFound("dateCreation.specified=true");

        // Get all the beneficiaireList where dateCreation is null
        defaultBeneficiaireShouldNotBeFound("dateCreation.specified=false");
    }

    @Test
    @Transactional
    void getAllBeneficiairesByDateModificationIsEqualToSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where dateModification equals to DEFAULT_DATE_MODIFICATION
        defaultBeneficiaireShouldBeFound("dateModification.equals=" + DEFAULT_DATE_MODIFICATION);

        // Get all the beneficiaireList where dateModification equals to UPDATED_DATE_MODIFICATION
        defaultBeneficiaireShouldNotBeFound("dateModification.equals=" + UPDATED_DATE_MODIFICATION);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByDateModificationIsNotEqualToSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where dateModification not equals to DEFAULT_DATE_MODIFICATION
        defaultBeneficiaireShouldNotBeFound("dateModification.notEquals=" + DEFAULT_DATE_MODIFICATION);

        // Get all the beneficiaireList where dateModification not equals to UPDATED_DATE_MODIFICATION
        defaultBeneficiaireShouldBeFound("dateModification.notEquals=" + UPDATED_DATE_MODIFICATION);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByDateModificationIsInShouldWork() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where dateModification in DEFAULT_DATE_MODIFICATION or UPDATED_DATE_MODIFICATION
        defaultBeneficiaireShouldBeFound("dateModification.in=" + DEFAULT_DATE_MODIFICATION + "," + UPDATED_DATE_MODIFICATION);

        // Get all the beneficiaireList where dateModification equals to UPDATED_DATE_MODIFICATION
        defaultBeneficiaireShouldNotBeFound("dateModification.in=" + UPDATED_DATE_MODIFICATION);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByDateModificationIsNullOrNotNull() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where dateModification is not null
        defaultBeneficiaireShouldBeFound("dateModification.specified=true");

        // Get all the beneficiaireList where dateModification is null
        defaultBeneficiaireShouldNotBeFound("dateModification.specified=false");
    }

    @Test
    @Transactional
    void getAllBeneficiairesByDateNaissanceIsEqualToSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where dateNaissance equals to DEFAULT_DATE_NAISSANCE
        defaultBeneficiaireShouldBeFound("dateNaissance.equals=" + DEFAULT_DATE_NAISSANCE);

        // Get all the beneficiaireList where dateNaissance equals to UPDATED_DATE_NAISSANCE
        defaultBeneficiaireShouldNotBeFound("dateNaissance.equals=" + UPDATED_DATE_NAISSANCE);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByDateNaissanceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where dateNaissance not equals to DEFAULT_DATE_NAISSANCE
        defaultBeneficiaireShouldNotBeFound("dateNaissance.notEquals=" + DEFAULT_DATE_NAISSANCE);

        // Get all the beneficiaireList where dateNaissance not equals to UPDATED_DATE_NAISSANCE
        defaultBeneficiaireShouldBeFound("dateNaissance.notEquals=" + UPDATED_DATE_NAISSANCE);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByDateNaissanceIsInShouldWork() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where dateNaissance in DEFAULT_DATE_NAISSANCE or UPDATED_DATE_NAISSANCE
        defaultBeneficiaireShouldBeFound("dateNaissance.in=" + DEFAULT_DATE_NAISSANCE + "," + UPDATED_DATE_NAISSANCE);

        // Get all the beneficiaireList where dateNaissance equals to UPDATED_DATE_NAISSANCE
        defaultBeneficiaireShouldNotBeFound("dateNaissance.in=" + UPDATED_DATE_NAISSANCE);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByDateNaissanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where dateNaissance is not null
        defaultBeneficiaireShouldBeFound("dateNaissance.specified=true");

        // Get all the beneficiaireList where dateNaissance is null
        defaultBeneficiaireShouldNotBeFound("dateNaissance.specified=false");
    }

    @Test
    @Transactional
    void getAllBeneficiairesByDateNaissanceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where dateNaissance is greater than or equal to DEFAULT_DATE_NAISSANCE
        defaultBeneficiaireShouldBeFound("dateNaissance.greaterThanOrEqual=" + DEFAULT_DATE_NAISSANCE);

        // Get all the beneficiaireList where dateNaissance is greater than or equal to UPDATED_DATE_NAISSANCE
        defaultBeneficiaireShouldNotBeFound("dateNaissance.greaterThanOrEqual=" + UPDATED_DATE_NAISSANCE);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByDateNaissanceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where dateNaissance is less than or equal to DEFAULT_DATE_NAISSANCE
        defaultBeneficiaireShouldBeFound("dateNaissance.lessThanOrEqual=" + DEFAULT_DATE_NAISSANCE);

        // Get all the beneficiaireList where dateNaissance is less than or equal to SMALLER_DATE_NAISSANCE
        defaultBeneficiaireShouldNotBeFound("dateNaissance.lessThanOrEqual=" + SMALLER_DATE_NAISSANCE);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByDateNaissanceIsLessThanSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where dateNaissance is less than DEFAULT_DATE_NAISSANCE
        defaultBeneficiaireShouldNotBeFound("dateNaissance.lessThan=" + DEFAULT_DATE_NAISSANCE);

        // Get all the beneficiaireList where dateNaissance is less than UPDATED_DATE_NAISSANCE
        defaultBeneficiaireShouldBeFound("dateNaissance.lessThan=" + UPDATED_DATE_NAISSANCE);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByDateNaissanceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where dateNaissance is greater than DEFAULT_DATE_NAISSANCE
        defaultBeneficiaireShouldNotBeFound("dateNaissance.greaterThan=" + DEFAULT_DATE_NAISSANCE);

        // Get all the beneficiaireList where dateNaissance is greater than SMALLER_DATE_NAISSANCE
        defaultBeneficiaireShouldBeFound("dateNaissance.greaterThan=" + SMALLER_DATE_NAISSANCE);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where email equals to DEFAULT_EMAIL
        defaultBeneficiaireShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the beneficiaireList where email equals to UPDATED_EMAIL
        defaultBeneficiaireShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where email not equals to DEFAULT_EMAIL
        defaultBeneficiaireShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the beneficiaireList where email not equals to UPDATED_EMAIL
        defaultBeneficiaireShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultBeneficiaireShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the beneficiaireList where email equals to UPDATED_EMAIL
        defaultBeneficiaireShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where email is not null
        defaultBeneficiaireShouldBeFound("email.specified=true");

        // Get all the beneficiaireList where email is null
        defaultBeneficiaireShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllBeneficiairesByEmailContainsSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where email contains DEFAULT_EMAIL
        defaultBeneficiaireShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the beneficiaireList where email contains UPDATED_EMAIL
        defaultBeneficiaireShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where email does not contain DEFAULT_EMAIL
        defaultBeneficiaireShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the beneficiaireList where email does not contain UPDATED_EMAIL
        defaultBeneficiaireShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByEtatIsEqualToSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where etat equals to DEFAULT_ETAT
        defaultBeneficiaireShouldBeFound("etat.equals=" + DEFAULT_ETAT);

        // Get all the beneficiaireList where etat equals to UPDATED_ETAT
        defaultBeneficiaireShouldNotBeFound("etat.equals=" + UPDATED_ETAT);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByEtatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where etat not equals to DEFAULT_ETAT
        defaultBeneficiaireShouldNotBeFound("etat.notEquals=" + DEFAULT_ETAT);

        // Get all the beneficiaireList where etat not equals to UPDATED_ETAT
        defaultBeneficiaireShouldBeFound("etat.notEquals=" + UPDATED_ETAT);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByEtatIsInShouldWork() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where etat in DEFAULT_ETAT or UPDATED_ETAT
        defaultBeneficiaireShouldBeFound("etat.in=" + DEFAULT_ETAT + "," + UPDATED_ETAT);

        // Get all the beneficiaireList where etat equals to UPDATED_ETAT
        defaultBeneficiaireShouldNotBeFound("etat.in=" + UPDATED_ETAT);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByEtatIsNullOrNotNull() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where etat is not null
        defaultBeneficiaireShouldBeFound("etat.specified=true");

        // Get all the beneficiaireList where etat is null
        defaultBeneficiaireShouldNotBeFound("etat.specified=false");
    }

    @Test
    @Transactional
    void getAllBeneficiairesByEtatIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where etat is greater than or equal to DEFAULT_ETAT
        defaultBeneficiaireShouldBeFound("etat.greaterThanOrEqual=" + DEFAULT_ETAT);

        // Get all the beneficiaireList where etat is greater than or equal to UPDATED_ETAT
        defaultBeneficiaireShouldNotBeFound("etat.greaterThanOrEqual=" + UPDATED_ETAT);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByEtatIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where etat is less than or equal to DEFAULT_ETAT
        defaultBeneficiaireShouldBeFound("etat.lessThanOrEqual=" + DEFAULT_ETAT);

        // Get all the beneficiaireList where etat is less than or equal to SMALLER_ETAT
        defaultBeneficiaireShouldNotBeFound("etat.lessThanOrEqual=" + SMALLER_ETAT);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByEtatIsLessThanSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where etat is less than DEFAULT_ETAT
        defaultBeneficiaireShouldNotBeFound("etat.lessThan=" + DEFAULT_ETAT);

        // Get all the beneficiaireList where etat is less than UPDATED_ETAT
        defaultBeneficiaireShouldBeFound("etat.lessThan=" + UPDATED_ETAT);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByEtatIsGreaterThanSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where etat is greater than DEFAULT_ETAT
        defaultBeneficiaireShouldNotBeFound("etat.greaterThan=" + DEFAULT_ETAT);

        // Get all the beneficiaireList where etat is greater than SMALLER_ETAT
        defaultBeneficiaireShouldBeFound("etat.greaterThan=" + SMALLER_ETAT);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByExplicationRefusIsEqualToSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where explicationRefus equals to DEFAULT_EXPLICATION_REFUS
        defaultBeneficiaireShouldBeFound("explicationRefus.equals=" + DEFAULT_EXPLICATION_REFUS);

        // Get all the beneficiaireList where explicationRefus equals to UPDATED_EXPLICATION_REFUS
        defaultBeneficiaireShouldNotBeFound("explicationRefus.equals=" + UPDATED_EXPLICATION_REFUS);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByExplicationRefusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where explicationRefus not equals to DEFAULT_EXPLICATION_REFUS
        defaultBeneficiaireShouldNotBeFound("explicationRefus.notEquals=" + DEFAULT_EXPLICATION_REFUS);

        // Get all the beneficiaireList where explicationRefus not equals to UPDATED_EXPLICATION_REFUS
        defaultBeneficiaireShouldBeFound("explicationRefus.notEquals=" + UPDATED_EXPLICATION_REFUS);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByExplicationRefusIsInShouldWork() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where explicationRefus in DEFAULT_EXPLICATION_REFUS or UPDATED_EXPLICATION_REFUS
        defaultBeneficiaireShouldBeFound("explicationRefus.in=" + DEFAULT_EXPLICATION_REFUS + "," + UPDATED_EXPLICATION_REFUS);

        // Get all the beneficiaireList where explicationRefus equals to UPDATED_EXPLICATION_REFUS
        defaultBeneficiaireShouldNotBeFound("explicationRefus.in=" + UPDATED_EXPLICATION_REFUS);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByExplicationRefusIsNullOrNotNull() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where explicationRefus is not null
        defaultBeneficiaireShouldBeFound("explicationRefus.specified=true");

        // Get all the beneficiaireList where explicationRefus is null
        defaultBeneficiaireShouldNotBeFound("explicationRefus.specified=false");
    }

    @Test
    @Transactional
    void getAllBeneficiairesByExplicationRefusContainsSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where explicationRefus contains DEFAULT_EXPLICATION_REFUS
        defaultBeneficiaireShouldBeFound("explicationRefus.contains=" + DEFAULT_EXPLICATION_REFUS);

        // Get all the beneficiaireList where explicationRefus contains UPDATED_EXPLICATION_REFUS
        defaultBeneficiaireShouldNotBeFound("explicationRefus.contains=" + UPDATED_EXPLICATION_REFUS);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByExplicationRefusNotContainsSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where explicationRefus does not contain DEFAULT_EXPLICATION_REFUS
        defaultBeneficiaireShouldNotBeFound("explicationRefus.doesNotContain=" + DEFAULT_EXPLICATION_REFUS);

        // Get all the beneficiaireList where explicationRefus does not contain UPDATED_EXPLICATION_REFUS
        defaultBeneficiaireShouldBeFound("explicationRefus.doesNotContain=" + UPDATED_EXPLICATION_REFUS);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByNbrEnfantsIsEqualToSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where nbrEnfants equals to DEFAULT_NBR_ENFANTS
        defaultBeneficiaireShouldBeFound("nbrEnfants.equals=" + DEFAULT_NBR_ENFANTS);

        // Get all the beneficiaireList where nbrEnfants equals to UPDATED_NBR_ENFANTS
        defaultBeneficiaireShouldNotBeFound("nbrEnfants.equals=" + UPDATED_NBR_ENFANTS);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByNbrEnfantsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where nbrEnfants not equals to DEFAULT_NBR_ENFANTS
        defaultBeneficiaireShouldNotBeFound("nbrEnfants.notEquals=" + DEFAULT_NBR_ENFANTS);

        // Get all the beneficiaireList where nbrEnfants not equals to UPDATED_NBR_ENFANTS
        defaultBeneficiaireShouldBeFound("nbrEnfants.notEquals=" + UPDATED_NBR_ENFANTS);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByNbrEnfantsIsInShouldWork() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where nbrEnfants in DEFAULT_NBR_ENFANTS or UPDATED_NBR_ENFANTS
        defaultBeneficiaireShouldBeFound("nbrEnfants.in=" + DEFAULT_NBR_ENFANTS + "," + UPDATED_NBR_ENFANTS);

        // Get all the beneficiaireList where nbrEnfants equals to UPDATED_NBR_ENFANTS
        defaultBeneficiaireShouldNotBeFound("nbrEnfants.in=" + UPDATED_NBR_ENFANTS);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByNbrEnfantsIsNullOrNotNull() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where nbrEnfants is not null
        defaultBeneficiaireShouldBeFound("nbrEnfants.specified=true");

        // Get all the beneficiaireList where nbrEnfants is null
        defaultBeneficiaireShouldNotBeFound("nbrEnfants.specified=false");
    }

    @Test
    @Transactional
    void getAllBeneficiairesByNbrEnfantsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where nbrEnfants is greater than or equal to DEFAULT_NBR_ENFANTS
        defaultBeneficiaireShouldBeFound("nbrEnfants.greaterThanOrEqual=" + DEFAULT_NBR_ENFANTS);

        // Get all the beneficiaireList where nbrEnfants is greater than or equal to UPDATED_NBR_ENFANTS
        defaultBeneficiaireShouldNotBeFound("nbrEnfants.greaterThanOrEqual=" + UPDATED_NBR_ENFANTS);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByNbrEnfantsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where nbrEnfants is less than or equal to DEFAULT_NBR_ENFANTS
        defaultBeneficiaireShouldBeFound("nbrEnfants.lessThanOrEqual=" + DEFAULT_NBR_ENFANTS);

        // Get all the beneficiaireList where nbrEnfants is less than or equal to SMALLER_NBR_ENFANTS
        defaultBeneficiaireShouldNotBeFound("nbrEnfants.lessThanOrEqual=" + SMALLER_NBR_ENFANTS);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByNbrEnfantsIsLessThanSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where nbrEnfants is less than DEFAULT_NBR_ENFANTS
        defaultBeneficiaireShouldNotBeFound("nbrEnfants.lessThan=" + DEFAULT_NBR_ENFANTS);

        // Get all the beneficiaireList where nbrEnfants is less than UPDATED_NBR_ENFANTS
        defaultBeneficiaireShouldBeFound("nbrEnfants.lessThan=" + UPDATED_NBR_ENFANTS);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByNbrEnfantsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where nbrEnfants is greater than DEFAULT_NBR_ENFANTS
        defaultBeneficiaireShouldNotBeFound("nbrEnfants.greaterThan=" + DEFAULT_NBR_ENFANTS);

        // Get all the beneficiaireList where nbrEnfants is greater than SMALLER_NBR_ENFANTS
        defaultBeneficiaireShouldBeFound("nbrEnfants.greaterThan=" + SMALLER_NBR_ENFANTS);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByNiveauScolariteIsEqualToSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where niveauScolarite equals to DEFAULT_NIVEAU_SCOLARITE
        defaultBeneficiaireShouldBeFound("niveauScolarite.equals=" + DEFAULT_NIVEAU_SCOLARITE);

        // Get all the beneficiaireList where niveauScolarite equals to UPDATED_NIVEAU_SCOLARITE
        defaultBeneficiaireShouldNotBeFound("niveauScolarite.equals=" + UPDATED_NIVEAU_SCOLARITE);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByNiveauScolariteIsNotEqualToSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where niveauScolarite not equals to DEFAULT_NIVEAU_SCOLARITE
        defaultBeneficiaireShouldNotBeFound("niveauScolarite.notEquals=" + DEFAULT_NIVEAU_SCOLARITE);

        // Get all the beneficiaireList where niveauScolarite not equals to UPDATED_NIVEAU_SCOLARITE
        defaultBeneficiaireShouldBeFound("niveauScolarite.notEquals=" + UPDATED_NIVEAU_SCOLARITE);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByNiveauScolariteIsInShouldWork() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where niveauScolarite in DEFAULT_NIVEAU_SCOLARITE or UPDATED_NIVEAU_SCOLARITE
        defaultBeneficiaireShouldBeFound("niveauScolarite.in=" + DEFAULT_NIVEAU_SCOLARITE + "," + UPDATED_NIVEAU_SCOLARITE);

        // Get all the beneficiaireList where niveauScolarite equals to UPDATED_NIVEAU_SCOLARITE
        defaultBeneficiaireShouldNotBeFound("niveauScolarite.in=" + UPDATED_NIVEAU_SCOLARITE);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByNiveauScolariteIsNullOrNotNull() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where niveauScolarite is not null
        defaultBeneficiaireShouldBeFound("niveauScolarite.specified=true");

        // Get all the beneficiaireList where niveauScolarite is null
        defaultBeneficiaireShouldNotBeFound("niveauScolarite.specified=false");
    }

    @Test
    @Transactional
    void getAllBeneficiairesByNiveauScolariteIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where niveauScolarite is greater than or equal to DEFAULT_NIVEAU_SCOLARITE
        defaultBeneficiaireShouldBeFound("niveauScolarite.greaterThanOrEqual=" + DEFAULT_NIVEAU_SCOLARITE);

        // Get all the beneficiaireList where niveauScolarite is greater than or equal to UPDATED_NIVEAU_SCOLARITE
        defaultBeneficiaireShouldNotBeFound("niveauScolarite.greaterThanOrEqual=" + UPDATED_NIVEAU_SCOLARITE);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByNiveauScolariteIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where niveauScolarite is less than or equal to DEFAULT_NIVEAU_SCOLARITE
        defaultBeneficiaireShouldBeFound("niveauScolarite.lessThanOrEqual=" + DEFAULT_NIVEAU_SCOLARITE);

        // Get all the beneficiaireList where niveauScolarite is less than or equal to SMALLER_NIVEAU_SCOLARITE
        defaultBeneficiaireShouldNotBeFound("niveauScolarite.lessThanOrEqual=" + SMALLER_NIVEAU_SCOLARITE);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByNiveauScolariteIsLessThanSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where niveauScolarite is less than DEFAULT_NIVEAU_SCOLARITE
        defaultBeneficiaireShouldNotBeFound("niveauScolarite.lessThan=" + DEFAULT_NIVEAU_SCOLARITE);

        // Get all the beneficiaireList where niveauScolarite is less than UPDATED_NIVEAU_SCOLARITE
        defaultBeneficiaireShouldBeFound("niveauScolarite.lessThan=" + UPDATED_NIVEAU_SCOLARITE);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByNiveauScolariteIsGreaterThanSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where niveauScolarite is greater than DEFAULT_NIVEAU_SCOLARITE
        defaultBeneficiaireShouldNotBeFound("niveauScolarite.greaterThan=" + DEFAULT_NIVEAU_SCOLARITE);

        // Get all the beneficiaireList where niveauScolarite is greater than SMALLER_NIVEAU_SCOLARITE
        defaultBeneficiaireShouldBeFound("niveauScolarite.greaterThan=" + SMALLER_NIVEAU_SCOLARITE);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByNomIsEqualToSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where nom equals to DEFAULT_NOM
        defaultBeneficiaireShouldBeFound("nom.equals=" + DEFAULT_NOM);

        // Get all the beneficiaireList where nom equals to UPDATED_NOM
        defaultBeneficiaireShouldNotBeFound("nom.equals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByNomIsNotEqualToSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where nom not equals to DEFAULT_NOM
        defaultBeneficiaireShouldNotBeFound("nom.notEquals=" + DEFAULT_NOM);

        // Get all the beneficiaireList where nom not equals to UPDATED_NOM
        defaultBeneficiaireShouldBeFound("nom.notEquals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByNomIsInShouldWork() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where nom in DEFAULT_NOM or UPDATED_NOM
        defaultBeneficiaireShouldBeFound("nom.in=" + DEFAULT_NOM + "," + UPDATED_NOM);

        // Get all the beneficiaireList where nom equals to UPDATED_NOM
        defaultBeneficiaireShouldNotBeFound("nom.in=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByNomIsNullOrNotNull() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where nom is not null
        defaultBeneficiaireShouldBeFound("nom.specified=true");

        // Get all the beneficiaireList where nom is null
        defaultBeneficiaireShouldNotBeFound("nom.specified=false");
    }

    @Test
    @Transactional
    void getAllBeneficiairesByNomContainsSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where nom contains DEFAULT_NOM
        defaultBeneficiaireShouldBeFound("nom.contains=" + DEFAULT_NOM);

        // Get all the beneficiaireList where nom contains UPDATED_NOM
        defaultBeneficiaireShouldNotBeFound("nom.contains=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByNomNotContainsSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where nom does not contain DEFAULT_NOM
        defaultBeneficiaireShouldNotBeFound("nom.doesNotContain=" + DEFAULT_NOM);

        // Get all the beneficiaireList where nom does not contain UPDATED_NOM
        defaultBeneficiaireShouldBeFound("nom.doesNotContain=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByNomFrIsEqualToSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where nomFr equals to DEFAULT_NOM_FR
        defaultBeneficiaireShouldBeFound("nomFr.equals=" + DEFAULT_NOM_FR);

        // Get all the beneficiaireList where nomFr equals to UPDATED_NOM_FR
        defaultBeneficiaireShouldNotBeFound("nomFr.equals=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByNomFrIsNotEqualToSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where nomFr not equals to DEFAULT_NOM_FR
        defaultBeneficiaireShouldNotBeFound("nomFr.notEquals=" + DEFAULT_NOM_FR);

        // Get all the beneficiaireList where nomFr not equals to UPDATED_NOM_FR
        defaultBeneficiaireShouldBeFound("nomFr.notEquals=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByNomFrIsInShouldWork() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where nomFr in DEFAULT_NOM_FR or UPDATED_NOM_FR
        defaultBeneficiaireShouldBeFound("nomFr.in=" + DEFAULT_NOM_FR + "," + UPDATED_NOM_FR);

        // Get all the beneficiaireList where nomFr equals to UPDATED_NOM_FR
        defaultBeneficiaireShouldNotBeFound("nomFr.in=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByNomFrIsNullOrNotNull() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where nomFr is not null
        defaultBeneficiaireShouldBeFound("nomFr.specified=true");

        // Get all the beneficiaireList where nomFr is null
        defaultBeneficiaireShouldNotBeFound("nomFr.specified=false");
    }

    @Test
    @Transactional
    void getAllBeneficiairesByNomFrContainsSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where nomFr contains DEFAULT_NOM_FR
        defaultBeneficiaireShouldBeFound("nomFr.contains=" + DEFAULT_NOM_FR);

        // Get all the beneficiaireList where nomFr contains UPDATED_NOM_FR
        defaultBeneficiaireShouldNotBeFound("nomFr.contains=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByNomFrNotContainsSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where nomFr does not contain DEFAULT_NOM_FR
        defaultBeneficiaireShouldNotBeFound("nomFr.doesNotContain=" + DEFAULT_NOM_FR);

        // Get all the beneficiaireList where nomFr does not contain UPDATED_NOM_FR
        defaultBeneficiaireShouldBeFound("nomFr.doesNotContain=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByNumeroDossierIsEqualToSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where numeroDossier equals to DEFAULT_NUMERO_DOSSIER
        defaultBeneficiaireShouldBeFound("numeroDossier.equals=" + DEFAULT_NUMERO_DOSSIER);

        // Get all the beneficiaireList where numeroDossier equals to UPDATED_NUMERO_DOSSIER
        defaultBeneficiaireShouldNotBeFound("numeroDossier.equals=" + UPDATED_NUMERO_DOSSIER);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByNumeroDossierIsNotEqualToSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where numeroDossier not equals to DEFAULT_NUMERO_DOSSIER
        defaultBeneficiaireShouldNotBeFound("numeroDossier.notEquals=" + DEFAULT_NUMERO_DOSSIER);

        // Get all the beneficiaireList where numeroDossier not equals to UPDATED_NUMERO_DOSSIER
        defaultBeneficiaireShouldBeFound("numeroDossier.notEquals=" + UPDATED_NUMERO_DOSSIER);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByNumeroDossierIsInShouldWork() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where numeroDossier in DEFAULT_NUMERO_DOSSIER or UPDATED_NUMERO_DOSSIER
        defaultBeneficiaireShouldBeFound("numeroDossier.in=" + DEFAULT_NUMERO_DOSSIER + "," + UPDATED_NUMERO_DOSSIER);

        // Get all the beneficiaireList where numeroDossier equals to UPDATED_NUMERO_DOSSIER
        defaultBeneficiaireShouldNotBeFound("numeroDossier.in=" + UPDATED_NUMERO_DOSSIER);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByNumeroDossierIsNullOrNotNull() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where numeroDossier is not null
        defaultBeneficiaireShouldBeFound("numeroDossier.specified=true");

        // Get all the beneficiaireList where numeroDossier is null
        defaultBeneficiaireShouldNotBeFound("numeroDossier.specified=false");
    }

    @Test
    @Transactional
    void getAllBeneficiairesByNumeroDossierContainsSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where numeroDossier contains DEFAULT_NUMERO_DOSSIER
        defaultBeneficiaireShouldBeFound("numeroDossier.contains=" + DEFAULT_NUMERO_DOSSIER);

        // Get all the beneficiaireList where numeroDossier contains UPDATED_NUMERO_DOSSIER
        defaultBeneficiaireShouldNotBeFound("numeroDossier.contains=" + UPDATED_NUMERO_DOSSIER);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByNumeroDossierNotContainsSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where numeroDossier does not contain DEFAULT_NUMERO_DOSSIER
        defaultBeneficiaireShouldNotBeFound("numeroDossier.doesNotContain=" + DEFAULT_NUMERO_DOSSIER);

        // Get all the beneficiaireList where numeroDossier does not contain UPDATED_NUMERO_DOSSIER
        defaultBeneficiaireShouldBeFound("numeroDossier.doesNotContain=" + UPDATED_NUMERO_DOSSIER);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByPrenomIsEqualToSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where prenom equals to DEFAULT_PRENOM
        defaultBeneficiaireShouldBeFound("prenom.equals=" + DEFAULT_PRENOM);

        // Get all the beneficiaireList where prenom equals to UPDATED_PRENOM
        defaultBeneficiaireShouldNotBeFound("prenom.equals=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByPrenomIsNotEqualToSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where prenom not equals to DEFAULT_PRENOM
        defaultBeneficiaireShouldNotBeFound("prenom.notEquals=" + DEFAULT_PRENOM);

        // Get all the beneficiaireList where prenom not equals to UPDATED_PRENOM
        defaultBeneficiaireShouldBeFound("prenom.notEquals=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByPrenomIsInShouldWork() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where prenom in DEFAULT_PRENOM or UPDATED_PRENOM
        defaultBeneficiaireShouldBeFound("prenom.in=" + DEFAULT_PRENOM + "," + UPDATED_PRENOM);

        // Get all the beneficiaireList where prenom equals to UPDATED_PRENOM
        defaultBeneficiaireShouldNotBeFound("prenom.in=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByPrenomIsNullOrNotNull() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where prenom is not null
        defaultBeneficiaireShouldBeFound("prenom.specified=true");

        // Get all the beneficiaireList where prenom is null
        defaultBeneficiaireShouldNotBeFound("prenom.specified=false");
    }

    @Test
    @Transactional
    void getAllBeneficiairesByPrenomContainsSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where prenom contains DEFAULT_PRENOM
        defaultBeneficiaireShouldBeFound("prenom.contains=" + DEFAULT_PRENOM);

        // Get all the beneficiaireList where prenom contains UPDATED_PRENOM
        defaultBeneficiaireShouldNotBeFound("prenom.contains=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByPrenomNotContainsSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where prenom does not contain DEFAULT_PRENOM
        defaultBeneficiaireShouldNotBeFound("prenom.doesNotContain=" + DEFAULT_PRENOM);

        // Get all the beneficiaireList where prenom does not contain UPDATED_PRENOM
        defaultBeneficiaireShouldBeFound("prenom.doesNotContain=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByPrenomFrIsEqualToSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where prenomFr equals to DEFAULT_PRENOM_FR
        defaultBeneficiaireShouldBeFound("prenomFr.equals=" + DEFAULT_PRENOM_FR);

        // Get all the beneficiaireList where prenomFr equals to UPDATED_PRENOM_FR
        defaultBeneficiaireShouldNotBeFound("prenomFr.equals=" + UPDATED_PRENOM_FR);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByPrenomFrIsNotEqualToSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where prenomFr not equals to DEFAULT_PRENOM_FR
        defaultBeneficiaireShouldNotBeFound("prenomFr.notEquals=" + DEFAULT_PRENOM_FR);

        // Get all the beneficiaireList where prenomFr not equals to UPDATED_PRENOM_FR
        defaultBeneficiaireShouldBeFound("prenomFr.notEquals=" + UPDATED_PRENOM_FR);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByPrenomFrIsInShouldWork() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where prenomFr in DEFAULT_PRENOM_FR or UPDATED_PRENOM_FR
        defaultBeneficiaireShouldBeFound("prenomFr.in=" + DEFAULT_PRENOM_FR + "," + UPDATED_PRENOM_FR);

        // Get all the beneficiaireList where prenomFr equals to UPDATED_PRENOM_FR
        defaultBeneficiaireShouldNotBeFound("prenomFr.in=" + UPDATED_PRENOM_FR);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByPrenomFrIsNullOrNotNull() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where prenomFr is not null
        defaultBeneficiaireShouldBeFound("prenomFr.specified=true");

        // Get all the beneficiaireList where prenomFr is null
        defaultBeneficiaireShouldNotBeFound("prenomFr.specified=false");
    }

    @Test
    @Transactional
    void getAllBeneficiairesByPrenomFrContainsSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where prenomFr contains DEFAULT_PRENOM_FR
        defaultBeneficiaireShouldBeFound("prenomFr.contains=" + DEFAULT_PRENOM_FR);

        // Get all the beneficiaireList where prenomFr contains UPDATED_PRENOM_FR
        defaultBeneficiaireShouldNotBeFound("prenomFr.contains=" + UPDATED_PRENOM_FR);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByPrenomFrNotContainsSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where prenomFr does not contain DEFAULT_PRENOM_FR
        defaultBeneficiaireShouldNotBeFound("prenomFr.doesNotContain=" + DEFAULT_PRENOM_FR);

        // Get all the beneficiaireList where prenomFr does not contain UPDATED_PRENOM_FR
        defaultBeneficiaireShouldBeFound("prenomFr.doesNotContain=" + UPDATED_PRENOM_FR);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByProfessionIsEqualToSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where profession equals to DEFAULT_PROFESSION
        defaultBeneficiaireShouldBeFound("profession.equals=" + DEFAULT_PROFESSION);

        // Get all the beneficiaireList where profession equals to UPDATED_PROFESSION
        defaultBeneficiaireShouldNotBeFound("profession.equals=" + UPDATED_PROFESSION);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByProfessionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where profession not equals to DEFAULT_PROFESSION
        defaultBeneficiaireShouldNotBeFound("profession.notEquals=" + DEFAULT_PROFESSION);

        // Get all the beneficiaireList where profession not equals to UPDATED_PROFESSION
        defaultBeneficiaireShouldBeFound("profession.notEquals=" + UPDATED_PROFESSION);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByProfessionIsInShouldWork() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where profession in DEFAULT_PROFESSION or UPDATED_PROFESSION
        defaultBeneficiaireShouldBeFound("profession.in=" + DEFAULT_PROFESSION + "," + UPDATED_PROFESSION);

        // Get all the beneficiaireList where profession equals to UPDATED_PROFESSION
        defaultBeneficiaireShouldNotBeFound("profession.in=" + UPDATED_PROFESSION);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByProfessionIsNullOrNotNull() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where profession is not null
        defaultBeneficiaireShouldBeFound("profession.specified=true");

        // Get all the beneficiaireList where profession is null
        defaultBeneficiaireShouldNotBeFound("profession.specified=false");
    }

    @Test
    @Transactional
    void getAllBeneficiairesByProfessionContainsSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where profession contains DEFAULT_PROFESSION
        defaultBeneficiaireShouldBeFound("profession.contains=" + DEFAULT_PROFESSION);

        // Get all the beneficiaireList where profession contains UPDATED_PROFESSION
        defaultBeneficiaireShouldNotBeFound("profession.contains=" + UPDATED_PROFESSION);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByProfessionNotContainsSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where profession does not contain DEFAULT_PROFESSION
        defaultBeneficiaireShouldNotBeFound("profession.doesNotContain=" + DEFAULT_PROFESSION);

        // Get all the beneficiaireList where profession does not contain UPDATED_PROFESSION
        defaultBeneficiaireShouldBeFound("profession.doesNotContain=" + UPDATED_PROFESSION);
    }

    @Test
    @Transactional
    void getAllBeneficiairesBySelectionnerIsEqualToSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where selectionner equals to DEFAULT_SELECTIONNER
        defaultBeneficiaireShouldBeFound("selectionner.equals=" + DEFAULT_SELECTIONNER);

        // Get all the beneficiaireList where selectionner equals to UPDATED_SELECTIONNER
        defaultBeneficiaireShouldNotBeFound("selectionner.equals=" + UPDATED_SELECTIONNER);
    }

    @Test
    @Transactional
    void getAllBeneficiairesBySelectionnerIsNotEqualToSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where selectionner not equals to DEFAULT_SELECTIONNER
        defaultBeneficiaireShouldNotBeFound("selectionner.notEquals=" + DEFAULT_SELECTIONNER);

        // Get all the beneficiaireList where selectionner not equals to UPDATED_SELECTIONNER
        defaultBeneficiaireShouldBeFound("selectionner.notEquals=" + UPDATED_SELECTIONNER);
    }

    @Test
    @Transactional
    void getAllBeneficiairesBySelectionnerIsInShouldWork() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where selectionner in DEFAULT_SELECTIONNER or UPDATED_SELECTIONNER
        defaultBeneficiaireShouldBeFound("selectionner.in=" + DEFAULT_SELECTIONNER + "," + UPDATED_SELECTIONNER);

        // Get all the beneficiaireList where selectionner equals to UPDATED_SELECTIONNER
        defaultBeneficiaireShouldNotBeFound("selectionner.in=" + UPDATED_SELECTIONNER);
    }

    @Test
    @Transactional
    void getAllBeneficiairesBySelectionnerIsNullOrNotNull() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where selectionner is not null
        defaultBeneficiaireShouldBeFound("selectionner.specified=true");

        // Get all the beneficiaireList where selectionner is null
        defaultBeneficiaireShouldNotBeFound("selectionner.specified=false");
    }

    @Test
    @Transactional
    void getAllBeneficiairesBySelectionnerIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where selectionner is greater than or equal to DEFAULT_SELECTIONNER
        defaultBeneficiaireShouldBeFound("selectionner.greaterThanOrEqual=" + DEFAULT_SELECTIONNER);

        // Get all the beneficiaireList where selectionner is greater than or equal to UPDATED_SELECTIONNER
        defaultBeneficiaireShouldNotBeFound("selectionner.greaterThanOrEqual=" + UPDATED_SELECTIONNER);
    }

    @Test
    @Transactional
    void getAllBeneficiairesBySelectionnerIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where selectionner is less than or equal to DEFAULT_SELECTIONNER
        defaultBeneficiaireShouldBeFound("selectionner.lessThanOrEqual=" + DEFAULT_SELECTIONNER);

        // Get all the beneficiaireList where selectionner is less than or equal to SMALLER_SELECTIONNER
        defaultBeneficiaireShouldNotBeFound("selectionner.lessThanOrEqual=" + SMALLER_SELECTIONNER);
    }

    @Test
    @Transactional
    void getAllBeneficiairesBySelectionnerIsLessThanSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where selectionner is less than DEFAULT_SELECTIONNER
        defaultBeneficiaireShouldNotBeFound("selectionner.lessThan=" + DEFAULT_SELECTIONNER);

        // Get all the beneficiaireList where selectionner is less than UPDATED_SELECTIONNER
        defaultBeneficiaireShouldBeFound("selectionner.lessThan=" + UPDATED_SELECTIONNER);
    }

    @Test
    @Transactional
    void getAllBeneficiairesBySelectionnerIsGreaterThanSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where selectionner is greater than DEFAULT_SELECTIONNER
        defaultBeneficiaireShouldNotBeFound("selectionner.greaterThan=" + DEFAULT_SELECTIONNER);

        // Get all the beneficiaireList where selectionner is greater than SMALLER_SELECTIONNER
        defaultBeneficiaireShouldBeFound("selectionner.greaterThan=" + SMALLER_SELECTIONNER);
    }

    @Test
    @Transactional
    void getAllBeneficiairesBySexeIsEqualToSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where sexe equals to DEFAULT_SEXE
        defaultBeneficiaireShouldBeFound("sexe.equals=" + DEFAULT_SEXE);

        // Get all the beneficiaireList where sexe equals to UPDATED_SEXE
        defaultBeneficiaireShouldNotBeFound("sexe.equals=" + UPDATED_SEXE);
    }

    @Test
    @Transactional
    void getAllBeneficiairesBySexeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where sexe not equals to DEFAULT_SEXE
        defaultBeneficiaireShouldNotBeFound("sexe.notEquals=" + DEFAULT_SEXE);

        // Get all the beneficiaireList where sexe not equals to UPDATED_SEXE
        defaultBeneficiaireShouldBeFound("sexe.notEquals=" + UPDATED_SEXE);
    }

    @Test
    @Transactional
    void getAllBeneficiairesBySexeIsInShouldWork() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where sexe in DEFAULT_SEXE or UPDATED_SEXE
        defaultBeneficiaireShouldBeFound("sexe.in=" + DEFAULT_SEXE + "," + UPDATED_SEXE);

        // Get all the beneficiaireList where sexe equals to UPDATED_SEXE
        defaultBeneficiaireShouldNotBeFound("sexe.in=" + UPDATED_SEXE);
    }

    @Test
    @Transactional
    void getAllBeneficiairesBySexeIsNullOrNotNull() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where sexe is not null
        defaultBeneficiaireShouldBeFound("sexe.specified=true");

        // Get all the beneficiaireList where sexe is null
        defaultBeneficiaireShouldNotBeFound("sexe.specified=false");
    }

    @Test
    @Transactional
    void getAllBeneficiairesBySexeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where sexe is greater than or equal to DEFAULT_SEXE
        defaultBeneficiaireShouldBeFound("sexe.greaterThanOrEqual=" + DEFAULT_SEXE);

        // Get all the beneficiaireList where sexe is greater than or equal to UPDATED_SEXE
        defaultBeneficiaireShouldNotBeFound("sexe.greaterThanOrEqual=" + UPDATED_SEXE);
    }

    @Test
    @Transactional
    void getAllBeneficiairesBySexeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where sexe is less than or equal to DEFAULT_SEXE
        defaultBeneficiaireShouldBeFound("sexe.lessThanOrEqual=" + DEFAULT_SEXE);

        // Get all the beneficiaireList where sexe is less than or equal to SMALLER_SEXE
        defaultBeneficiaireShouldNotBeFound("sexe.lessThanOrEqual=" + SMALLER_SEXE);
    }

    @Test
    @Transactional
    void getAllBeneficiairesBySexeIsLessThanSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where sexe is less than DEFAULT_SEXE
        defaultBeneficiaireShouldNotBeFound("sexe.lessThan=" + DEFAULT_SEXE);

        // Get all the beneficiaireList where sexe is less than UPDATED_SEXE
        defaultBeneficiaireShouldBeFound("sexe.lessThan=" + UPDATED_SEXE);
    }

    @Test
    @Transactional
    void getAllBeneficiairesBySexeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where sexe is greater than DEFAULT_SEXE
        defaultBeneficiaireShouldNotBeFound("sexe.greaterThan=" + DEFAULT_SEXE);

        // Get all the beneficiaireList where sexe is greater than SMALLER_SEXE
        defaultBeneficiaireShouldBeFound("sexe.greaterThan=" + SMALLER_SEXE);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByTelephoneIsEqualToSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where telephone equals to DEFAULT_TELEPHONE
        defaultBeneficiaireShouldBeFound("telephone.equals=" + DEFAULT_TELEPHONE);

        // Get all the beneficiaireList where telephone equals to UPDATED_TELEPHONE
        defaultBeneficiaireShouldNotBeFound("telephone.equals=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByTelephoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where telephone not equals to DEFAULT_TELEPHONE
        defaultBeneficiaireShouldNotBeFound("telephone.notEquals=" + DEFAULT_TELEPHONE);

        // Get all the beneficiaireList where telephone not equals to UPDATED_TELEPHONE
        defaultBeneficiaireShouldBeFound("telephone.notEquals=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByTelephoneIsInShouldWork() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where telephone in DEFAULT_TELEPHONE or UPDATED_TELEPHONE
        defaultBeneficiaireShouldBeFound("telephone.in=" + DEFAULT_TELEPHONE + "," + UPDATED_TELEPHONE);

        // Get all the beneficiaireList where telephone equals to UPDATED_TELEPHONE
        defaultBeneficiaireShouldNotBeFound("telephone.in=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByTelephoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where telephone is not null
        defaultBeneficiaireShouldBeFound("telephone.specified=true");

        // Get all the beneficiaireList where telephone is null
        defaultBeneficiaireShouldNotBeFound("telephone.specified=false");
    }

    @Test
    @Transactional
    void getAllBeneficiairesByTelephoneContainsSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where telephone contains DEFAULT_TELEPHONE
        defaultBeneficiaireShouldBeFound("telephone.contains=" + DEFAULT_TELEPHONE);

        // Get all the beneficiaireList where telephone contains UPDATED_TELEPHONE
        defaultBeneficiaireShouldNotBeFound("telephone.contains=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByTelephoneNotContainsSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where telephone does not contain DEFAULT_TELEPHONE
        defaultBeneficiaireShouldNotBeFound("telephone.doesNotContain=" + DEFAULT_TELEPHONE);

        // Get all the beneficiaireList where telephone does not contain UPDATED_TELEPHONE
        defaultBeneficiaireShouldBeFound("telephone.doesNotContain=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByAutreBenef2019IsEqualToSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where autreBenef2019 equals to DEFAULT_AUTRE_BENEF_2019
        defaultBeneficiaireShouldBeFound("autreBenef2019.equals=" + DEFAULT_AUTRE_BENEF_2019);

        // Get all the beneficiaireList where autreBenef2019 equals to UPDATED_AUTRE_BENEF_2019
        defaultBeneficiaireShouldNotBeFound("autreBenef2019.equals=" + UPDATED_AUTRE_BENEF_2019);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByAutreBenef2019IsNotEqualToSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where autreBenef2019 not equals to DEFAULT_AUTRE_BENEF_2019
        defaultBeneficiaireShouldNotBeFound("autreBenef2019.notEquals=" + DEFAULT_AUTRE_BENEF_2019);

        // Get all the beneficiaireList where autreBenef2019 not equals to UPDATED_AUTRE_BENEF_2019
        defaultBeneficiaireShouldBeFound("autreBenef2019.notEquals=" + UPDATED_AUTRE_BENEF_2019);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByAutreBenef2019IsInShouldWork() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where autreBenef2019 in DEFAULT_AUTRE_BENEF_2019 or UPDATED_AUTRE_BENEF_2019
        defaultBeneficiaireShouldBeFound("autreBenef2019.in=" + DEFAULT_AUTRE_BENEF_2019 + "," + UPDATED_AUTRE_BENEF_2019);

        // Get all the beneficiaireList where autreBenef2019 equals to UPDATED_AUTRE_BENEF_2019
        defaultBeneficiaireShouldNotBeFound("autreBenef2019.in=" + UPDATED_AUTRE_BENEF_2019);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByAutreBenef2019IsNullOrNotNull() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where autreBenef2019 is not null
        defaultBeneficiaireShouldBeFound("autreBenef2019.specified=true");

        // Get all the beneficiaireList where autreBenef2019 is null
        defaultBeneficiaireShouldNotBeFound("autreBenef2019.specified=false");
    }

    @Test
    @Transactional
    void getAllBeneficiairesByAutreBenef2019IsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where autreBenef2019 is greater than or equal to DEFAULT_AUTRE_BENEF_2019
        defaultBeneficiaireShouldBeFound("autreBenef2019.greaterThanOrEqual=" + DEFAULT_AUTRE_BENEF_2019);

        // Get all the beneficiaireList where autreBenef2019 is greater than or equal to UPDATED_AUTRE_BENEF_2019
        defaultBeneficiaireShouldNotBeFound("autreBenef2019.greaterThanOrEqual=" + UPDATED_AUTRE_BENEF_2019);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByAutreBenef2019IsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where autreBenef2019 is less than or equal to DEFAULT_AUTRE_BENEF_2019
        defaultBeneficiaireShouldBeFound("autreBenef2019.lessThanOrEqual=" + DEFAULT_AUTRE_BENEF_2019);

        // Get all the beneficiaireList where autreBenef2019 is less than or equal to SMALLER_AUTRE_BENEF_2019
        defaultBeneficiaireShouldNotBeFound("autreBenef2019.lessThanOrEqual=" + SMALLER_AUTRE_BENEF_2019);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByAutreBenef2019IsLessThanSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where autreBenef2019 is less than DEFAULT_AUTRE_BENEF_2019
        defaultBeneficiaireShouldNotBeFound("autreBenef2019.lessThan=" + DEFAULT_AUTRE_BENEF_2019);

        // Get all the beneficiaireList where autreBenef2019 is less than UPDATED_AUTRE_BENEF_2019
        defaultBeneficiaireShouldBeFound("autreBenef2019.lessThan=" + UPDATED_AUTRE_BENEF_2019);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByAutreBenef2019IsGreaterThanSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where autreBenef2019 is greater than DEFAULT_AUTRE_BENEF_2019
        defaultBeneficiaireShouldNotBeFound("autreBenef2019.greaterThan=" + DEFAULT_AUTRE_BENEF_2019);

        // Get all the beneficiaireList where autreBenef2019 is greater than SMALLER_AUTRE_BENEF_2019
        defaultBeneficiaireShouldBeFound("autreBenef2019.greaterThan=" + SMALLER_AUTRE_BENEF_2019);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByAutreBenef2020IsEqualToSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where autreBenef2020 equals to DEFAULT_AUTRE_BENEF_2020
        defaultBeneficiaireShouldBeFound("autreBenef2020.equals=" + DEFAULT_AUTRE_BENEF_2020);

        // Get all the beneficiaireList where autreBenef2020 equals to UPDATED_AUTRE_BENEF_2020
        defaultBeneficiaireShouldNotBeFound("autreBenef2020.equals=" + UPDATED_AUTRE_BENEF_2020);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByAutreBenef2020IsNotEqualToSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where autreBenef2020 not equals to DEFAULT_AUTRE_BENEF_2020
        defaultBeneficiaireShouldNotBeFound("autreBenef2020.notEquals=" + DEFAULT_AUTRE_BENEF_2020);

        // Get all the beneficiaireList where autreBenef2020 not equals to UPDATED_AUTRE_BENEF_2020
        defaultBeneficiaireShouldBeFound("autreBenef2020.notEquals=" + UPDATED_AUTRE_BENEF_2020);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByAutreBenef2020IsInShouldWork() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where autreBenef2020 in DEFAULT_AUTRE_BENEF_2020 or UPDATED_AUTRE_BENEF_2020
        defaultBeneficiaireShouldBeFound("autreBenef2020.in=" + DEFAULT_AUTRE_BENEF_2020 + "," + UPDATED_AUTRE_BENEF_2020);

        // Get all the beneficiaireList where autreBenef2020 equals to UPDATED_AUTRE_BENEF_2020
        defaultBeneficiaireShouldNotBeFound("autreBenef2020.in=" + UPDATED_AUTRE_BENEF_2020);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByAutreBenef2020IsNullOrNotNull() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where autreBenef2020 is not null
        defaultBeneficiaireShouldBeFound("autreBenef2020.specified=true");

        // Get all the beneficiaireList where autreBenef2020 is null
        defaultBeneficiaireShouldNotBeFound("autreBenef2020.specified=false");
    }

    @Test
    @Transactional
    void getAllBeneficiairesByAutreBenef2020IsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where autreBenef2020 is greater than or equal to DEFAULT_AUTRE_BENEF_2020
        defaultBeneficiaireShouldBeFound("autreBenef2020.greaterThanOrEqual=" + DEFAULT_AUTRE_BENEF_2020);

        // Get all the beneficiaireList where autreBenef2020 is greater than or equal to UPDATED_AUTRE_BENEF_2020
        defaultBeneficiaireShouldNotBeFound("autreBenef2020.greaterThanOrEqual=" + UPDATED_AUTRE_BENEF_2020);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByAutreBenef2020IsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where autreBenef2020 is less than or equal to DEFAULT_AUTRE_BENEF_2020
        defaultBeneficiaireShouldBeFound("autreBenef2020.lessThanOrEqual=" + DEFAULT_AUTRE_BENEF_2020);

        // Get all the beneficiaireList where autreBenef2020 is less than or equal to SMALLER_AUTRE_BENEF_2020
        defaultBeneficiaireShouldNotBeFound("autreBenef2020.lessThanOrEqual=" + SMALLER_AUTRE_BENEF_2020);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByAutreBenef2020IsLessThanSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where autreBenef2020 is less than DEFAULT_AUTRE_BENEF_2020
        defaultBeneficiaireShouldNotBeFound("autreBenef2020.lessThan=" + DEFAULT_AUTRE_BENEF_2020);

        // Get all the beneficiaireList where autreBenef2020 is less than UPDATED_AUTRE_BENEF_2020
        defaultBeneficiaireShouldBeFound("autreBenef2020.lessThan=" + UPDATED_AUTRE_BENEF_2020);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByAutreBenef2020IsGreaterThanSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where autreBenef2020 is greater than DEFAULT_AUTRE_BENEF_2020
        defaultBeneficiaireShouldNotBeFound("autreBenef2020.greaterThan=" + DEFAULT_AUTRE_BENEF_2020);

        // Get all the beneficiaireList where autreBenef2020 is greater than SMALLER_AUTRE_BENEF_2020
        defaultBeneficiaireShouldBeFound("autreBenef2020.greaterThan=" + SMALLER_AUTRE_BENEF_2020);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByRelationFamilialeIsEqualToSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where relationFamiliale equals to DEFAULT_RELATION_FAMILIALE
        defaultBeneficiaireShouldBeFound("relationFamiliale.equals=" + DEFAULT_RELATION_FAMILIALE);

        // Get all the beneficiaireList where relationFamiliale equals to UPDATED_RELATION_FAMILIALE
        defaultBeneficiaireShouldNotBeFound("relationFamiliale.equals=" + UPDATED_RELATION_FAMILIALE);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByRelationFamilialeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where relationFamiliale not equals to DEFAULT_RELATION_FAMILIALE
        defaultBeneficiaireShouldNotBeFound("relationFamiliale.notEquals=" + DEFAULT_RELATION_FAMILIALE);

        // Get all the beneficiaireList where relationFamiliale not equals to UPDATED_RELATION_FAMILIALE
        defaultBeneficiaireShouldBeFound("relationFamiliale.notEquals=" + UPDATED_RELATION_FAMILIALE);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByRelationFamilialeIsInShouldWork() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where relationFamiliale in DEFAULT_RELATION_FAMILIALE or UPDATED_RELATION_FAMILIALE
        defaultBeneficiaireShouldBeFound("relationFamiliale.in=" + DEFAULT_RELATION_FAMILIALE + "," + UPDATED_RELATION_FAMILIALE);

        // Get all the beneficiaireList where relationFamiliale equals to UPDATED_RELATION_FAMILIALE
        defaultBeneficiaireShouldNotBeFound("relationFamiliale.in=" + UPDATED_RELATION_FAMILIALE);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByRelationFamilialeIsNullOrNotNull() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where relationFamiliale is not null
        defaultBeneficiaireShouldBeFound("relationFamiliale.specified=true");

        // Get all the beneficiaireList where relationFamiliale is null
        defaultBeneficiaireShouldNotBeFound("relationFamiliale.specified=false");
    }

    @Test
    @Transactional
    void getAllBeneficiairesByRelationFamilialeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where relationFamiliale is greater than or equal to DEFAULT_RELATION_FAMILIALE
        defaultBeneficiaireShouldBeFound("relationFamiliale.greaterThanOrEqual=" + DEFAULT_RELATION_FAMILIALE);

        // Get all the beneficiaireList where relationFamiliale is greater than or equal to UPDATED_RELATION_FAMILIALE
        defaultBeneficiaireShouldNotBeFound("relationFamiliale.greaterThanOrEqual=" + UPDATED_RELATION_FAMILIALE);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByRelationFamilialeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where relationFamiliale is less than or equal to DEFAULT_RELATION_FAMILIALE
        defaultBeneficiaireShouldBeFound("relationFamiliale.lessThanOrEqual=" + DEFAULT_RELATION_FAMILIALE);

        // Get all the beneficiaireList where relationFamiliale is less than or equal to SMALLER_RELATION_FAMILIALE
        defaultBeneficiaireShouldNotBeFound("relationFamiliale.lessThanOrEqual=" + SMALLER_RELATION_FAMILIALE);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByRelationFamilialeIsLessThanSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where relationFamiliale is less than DEFAULT_RELATION_FAMILIALE
        defaultBeneficiaireShouldNotBeFound("relationFamiliale.lessThan=" + DEFAULT_RELATION_FAMILIALE);

        // Get all the beneficiaireList where relationFamiliale is less than UPDATED_RELATION_FAMILIALE
        defaultBeneficiaireShouldBeFound("relationFamiliale.lessThan=" + UPDATED_RELATION_FAMILIALE);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByRelationFamilialeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where relationFamiliale is greater than DEFAULT_RELATION_FAMILIALE
        defaultBeneficiaireShouldNotBeFound("relationFamiliale.greaterThan=" + DEFAULT_RELATION_FAMILIALE);

        // Get all the beneficiaireList where relationFamiliale is greater than SMALLER_RELATION_FAMILIALE
        defaultBeneficiaireShouldBeFound("relationFamiliale.greaterThan=" + SMALLER_RELATION_FAMILIALE);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByLieuTravailProfessionnelIsEqualToSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where lieuTravailProfessionnel equals to DEFAULT_LIEU_TRAVAIL_PROFESSIONNEL
        defaultBeneficiaireShouldBeFound("lieuTravailProfessionnel.equals=" + DEFAULT_LIEU_TRAVAIL_PROFESSIONNEL);

        // Get all the beneficiaireList where lieuTravailProfessionnel equals to UPDATED_LIEU_TRAVAIL_PROFESSIONNEL
        defaultBeneficiaireShouldNotBeFound("lieuTravailProfessionnel.equals=" + UPDATED_LIEU_TRAVAIL_PROFESSIONNEL);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByLieuTravailProfessionnelIsNotEqualToSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where lieuTravailProfessionnel not equals to DEFAULT_LIEU_TRAVAIL_PROFESSIONNEL
        defaultBeneficiaireShouldNotBeFound("lieuTravailProfessionnel.notEquals=" + DEFAULT_LIEU_TRAVAIL_PROFESSIONNEL);

        // Get all the beneficiaireList where lieuTravailProfessionnel not equals to UPDATED_LIEU_TRAVAIL_PROFESSIONNEL
        defaultBeneficiaireShouldBeFound("lieuTravailProfessionnel.notEquals=" + UPDATED_LIEU_TRAVAIL_PROFESSIONNEL);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByLieuTravailProfessionnelIsInShouldWork() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where lieuTravailProfessionnel in DEFAULT_LIEU_TRAVAIL_PROFESSIONNEL or UPDATED_LIEU_TRAVAIL_PROFESSIONNEL
        defaultBeneficiaireShouldBeFound(
            "lieuTravailProfessionnel.in=" + DEFAULT_LIEU_TRAVAIL_PROFESSIONNEL + "," + UPDATED_LIEU_TRAVAIL_PROFESSIONNEL
        );

        // Get all the beneficiaireList where lieuTravailProfessionnel equals to UPDATED_LIEU_TRAVAIL_PROFESSIONNEL
        defaultBeneficiaireShouldNotBeFound("lieuTravailProfessionnel.in=" + UPDATED_LIEU_TRAVAIL_PROFESSIONNEL);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByLieuTravailProfessionnelIsNullOrNotNull() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where lieuTravailProfessionnel is not null
        defaultBeneficiaireShouldBeFound("lieuTravailProfessionnel.specified=true");

        // Get all the beneficiaireList where lieuTravailProfessionnel is null
        defaultBeneficiaireShouldNotBeFound("lieuTravailProfessionnel.specified=false");
    }

    @Test
    @Transactional
    void getAllBeneficiairesByLieuTravailProfessionnelContainsSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where lieuTravailProfessionnel contains DEFAULT_LIEU_TRAVAIL_PROFESSIONNEL
        defaultBeneficiaireShouldBeFound("lieuTravailProfessionnel.contains=" + DEFAULT_LIEU_TRAVAIL_PROFESSIONNEL);

        // Get all the beneficiaireList where lieuTravailProfessionnel contains UPDATED_LIEU_TRAVAIL_PROFESSIONNEL
        defaultBeneficiaireShouldNotBeFound("lieuTravailProfessionnel.contains=" + UPDATED_LIEU_TRAVAIL_PROFESSIONNEL);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByLieuTravailProfessionnelNotContainsSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where lieuTravailProfessionnel does not contain DEFAULT_LIEU_TRAVAIL_PROFESSIONNEL
        defaultBeneficiaireShouldNotBeFound("lieuTravailProfessionnel.doesNotContain=" + DEFAULT_LIEU_TRAVAIL_PROFESSIONNEL);

        // Get all the beneficiaireList where lieuTravailProfessionnel does not contain UPDATED_LIEU_TRAVAIL_PROFESSIONNEL
        defaultBeneficiaireShouldBeFound("lieuTravailProfessionnel.doesNotContain=" + UPDATED_LIEU_TRAVAIL_PROFESSIONNEL);
    }

    @Test
    @Transactional
    void getAllBeneficiairesBySpecialiteProfessionnelIsEqualToSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where specialiteProfessionnel equals to DEFAULT_SPECIALITE_PROFESSIONNEL
        defaultBeneficiaireShouldBeFound("specialiteProfessionnel.equals=" + DEFAULT_SPECIALITE_PROFESSIONNEL);

        // Get all the beneficiaireList where specialiteProfessionnel equals to UPDATED_SPECIALITE_PROFESSIONNEL
        defaultBeneficiaireShouldNotBeFound("specialiteProfessionnel.equals=" + UPDATED_SPECIALITE_PROFESSIONNEL);
    }

    @Test
    @Transactional
    void getAllBeneficiairesBySpecialiteProfessionnelIsNotEqualToSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where specialiteProfessionnel not equals to DEFAULT_SPECIALITE_PROFESSIONNEL
        defaultBeneficiaireShouldNotBeFound("specialiteProfessionnel.notEquals=" + DEFAULT_SPECIALITE_PROFESSIONNEL);

        // Get all the beneficiaireList where specialiteProfessionnel not equals to UPDATED_SPECIALITE_PROFESSIONNEL
        defaultBeneficiaireShouldBeFound("specialiteProfessionnel.notEquals=" + UPDATED_SPECIALITE_PROFESSIONNEL);
    }

    @Test
    @Transactional
    void getAllBeneficiairesBySpecialiteProfessionnelIsInShouldWork() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where specialiteProfessionnel in DEFAULT_SPECIALITE_PROFESSIONNEL or UPDATED_SPECIALITE_PROFESSIONNEL
        defaultBeneficiaireShouldBeFound(
            "specialiteProfessionnel.in=" + DEFAULT_SPECIALITE_PROFESSIONNEL + "," + UPDATED_SPECIALITE_PROFESSIONNEL
        );

        // Get all the beneficiaireList where specialiteProfessionnel equals to UPDATED_SPECIALITE_PROFESSIONNEL
        defaultBeneficiaireShouldNotBeFound("specialiteProfessionnel.in=" + UPDATED_SPECIALITE_PROFESSIONNEL);
    }

    @Test
    @Transactional
    void getAllBeneficiairesBySpecialiteProfessionnelIsNullOrNotNull() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where specialiteProfessionnel is not null
        defaultBeneficiaireShouldBeFound("specialiteProfessionnel.specified=true");

        // Get all the beneficiaireList where specialiteProfessionnel is null
        defaultBeneficiaireShouldNotBeFound("specialiteProfessionnel.specified=false");
    }

    @Test
    @Transactional
    void getAllBeneficiairesBySpecialiteProfessionnelIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where specialiteProfessionnel is greater than or equal to DEFAULT_SPECIALITE_PROFESSIONNEL
        defaultBeneficiaireShouldBeFound("specialiteProfessionnel.greaterThanOrEqual=" + DEFAULT_SPECIALITE_PROFESSIONNEL);

        // Get all the beneficiaireList where specialiteProfessionnel is greater than or equal to UPDATED_SPECIALITE_PROFESSIONNEL
        defaultBeneficiaireShouldNotBeFound("specialiteProfessionnel.greaterThanOrEqual=" + UPDATED_SPECIALITE_PROFESSIONNEL);
    }

    @Test
    @Transactional
    void getAllBeneficiairesBySpecialiteProfessionnelIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where specialiteProfessionnel is less than or equal to DEFAULT_SPECIALITE_PROFESSIONNEL
        defaultBeneficiaireShouldBeFound("specialiteProfessionnel.lessThanOrEqual=" + DEFAULT_SPECIALITE_PROFESSIONNEL);

        // Get all the beneficiaireList where specialiteProfessionnel is less than or equal to SMALLER_SPECIALITE_PROFESSIONNEL
        defaultBeneficiaireShouldNotBeFound("specialiteProfessionnel.lessThanOrEqual=" + SMALLER_SPECIALITE_PROFESSIONNEL);
    }

    @Test
    @Transactional
    void getAllBeneficiairesBySpecialiteProfessionnelIsLessThanSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where specialiteProfessionnel is less than DEFAULT_SPECIALITE_PROFESSIONNEL
        defaultBeneficiaireShouldNotBeFound("specialiteProfessionnel.lessThan=" + DEFAULT_SPECIALITE_PROFESSIONNEL);

        // Get all the beneficiaireList where specialiteProfessionnel is less than UPDATED_SPECIALITE_PROFESSIONNEL
        defaultBeneficiaireShouldBeFound("specialiteProfessionnel.lessThan=" + UPDATED_SPECIALITE_PROFESSIONNEL);
    }

    @Test
    @Transactional
    void getAllBeneficiairesBySpecialiteProfessionnelIsGreaterThanSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList where specialiteProfessionnel is greater than DEFAULT_SPECIALITE_PROFESSIONNEL
        defaultBeneficiaireShouldNotBeFound("specialiteProfessionnel.greaterThan=" + DEFAULT_SPECIALITE_PROFESSIONNEL);

        // Get all the beneficiaireList where specialiteProfessionnel is greater than SMALLER_SPECIALITE_PROFESSIONNEL
        defaultBeneficiaireShouldBeFound("specialiteProfessionnel.greaterThan=" + SMALLER_SPECIALITE_PROFESSIONNEL);
    }

    @Test
    @Transactional
    void getAllBeneficiairesByAppUserIsEqualToSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);
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
        beneficiaire.setAppUser(appUser);
        beneficiaireRepository.saveAndFlush(beneficiaire);
        Long appUserId = appUser.getId();

        // Get all the beneficiaireList where appUser equals to appUserId
        defaultBeneficiaireShouldBeFound("appUserId.equals=" + appUserId);

        // Get all the beneficiaireList where appUser equals to (appUserId + 1)
        defaultBeneficiaireShouldNotBeFound("appUserId.equals=" + (appUserId + 1));
    }

    @Test
    @Transactional
    void getAllBeneficiairesByEnfantIsEqualToSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);
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
        beneficiaire.setEnfant(enfant);
        beneficiaireRepository.saveAndFlush(beneficiaire);
        Long enfantId = enfant.getId();

        // Get all the beneficiaireList where enfant equals to enfantId
        defaultBeneficiaireShouldBeFound("enfantId.equals=" + enfantId);

        // Get all the beneficiaireList where enfant equals to (enfantId + 1)
        defaultBeneficiaireShouldNotBeFound("enfantId.equals=" + (enfantId + 1));
    }

    @Test
    @Transactional
    void getAllBeneficiairesByMotifRefusIsEqualToSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);
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
        beneficiaire.setMotifRefus(motifRefus);
        beneficiaireRepository.saveAndFlush(beneficiaire);
        Long motifRefusId = motifRefus.getId();

        // Get all the beneficiaireList where motifRefus equals to motifRefusId
        defaultBeneficiaireShouldBeFound("motifRefusId.equals=" + motifRefusId);

        // Get all the beneficiaireList where motifRefus equals to (motifRefusId + 1)
        defaultBeneficiaireShouldNotBeFound("motifRefusId.equals=" + (motifRefusId + 1));
    }

    @Test
    @Transactional
    void getAllBeneficiairesByProvinceIsEqualToSomething() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);
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
        beneficiaire.setProvince(province);
        beneficiaireRepository.saveAndFlush(beneficiaire);
        Long provinceId = province.getId();

        // Get all the beneficiaireList where province equals to provinceId
        defaultBeneficiaireShouldBeFound("provinceId.equals=" + provinceId);

        // Get all the beneficiaireList where province equals to (provinceId + 1)
        defaultBeneficiaireShouldNotBeFound("provinceId.equals=" + (provinceId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBeneficiaireShouldBeFound(String filter) throws Exception {
        restBeneficiaireMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(beneficiaire.getId().intValue())))
            .andExpect(jsonPath("$.[*].typeBeneficiare").value(hasItem(DEFAULT_TYPE_BENEFICIARE)))
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
            .andExpect(jsonPath("$.[*].relationFamiliale").value(hasItem(DEFAULT_RELATION_FAMILIALE)))
            .andExpect(jsonPath("$.[*].lieuTravailProfessionnel").value(hasItem(DEFAULT_LIEU_TRAVAIL_PROFESSIONNEL)))
            .andExpect(jsonPath("$.[*].specialiteProfessionnel").value(hasItem(DEFAULT_SPECIALITE_PROFESSIONNEL)));

        // Check, that the count call also returns 1
        restBeneficiaireMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBeneficiaireShouldNotBeFound(String filter) throws Exception {
        restBeneficiaireMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBeneficiaireMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBeneficiaire() throws Exception {
        // Get the beneficiaire
        restBeneficiaireMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBeneficiaire() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        int databaseSizeBeforeUpdate = beneficiaireRepository.findAll().size();

        // Update the beneficiaire
        Beneficiaire updatedBeneficiaire = beneficiaireRepository.findById(beneficiaire.getId()).get();
        // Disconnect from session so that the updates on updatedBeneficiaire are not directly saved in db
        em.detach(updatedBeneficiaire);
        updatedBeneficiaire
            .typeBeneficiare(UPDATED_TYPE_BENEFICIARE)
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
            .relationFamiliale(UPDATED_RELATION_FAMILIALE)
            .lieuTravailProfessionnel(UPDATED_LIEU_TRAVAIL_PROFESSIONNEL)
            .specialiteProfessionnel(UPDATED_SPECIALITE_PROFESSIONNEL);
        BeneficiaireDTO beneficiaireDTO = beneficiaireMapper.toDto(updatedBeneficiaire);

        restBeneficiaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, beneficiaireDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(beneficiaireDTO))
            )
            .andExpect(status().isOk());

        // Validate the Beneficiaire in the database
        List<Beneficiaire> beneficiaireList = beneficiaireRepository.findAll();
        assertThat(beneficiaireList).hasSize(databaseSizeBeforeUpdate);
        Beneficiaire testBeneficiaire = beneficiaireList.get(beneficiaireList.size() - 1);
        assertThat(testBeneficiaire.getTypeBeneficiare()).isEqualTo(UPDATED_TYPE_BENEFICIARE);
        assertThat(testBeneficiaire.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testBeneficiaire.getBenef2019()).isEqualTo(UPDATED_BENEF_2019);
        assertThat(testBeneficiaire.getBenef2020()).isEqualTo(UPDATED_BENEF_2020);
        assertThat(testBeneficiaire.getCin()).isEqualTo(UPDATED_CIN);
        assertThat(testBeneficiaire.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
        assertThat(testBeneficiaire.getDateModification()).isEqualTo(UPDATED_DATE_MODIFICATION);
        assertThat(testBeneficiaire.getDateNaissance()).isEqualTo(UPDATED_DATE_NAISSANCE);
        assertThat(testBeneficiaire.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testBeneficiaire.getEtat()).isEqualTo(UPDATED_ETAT);
        assertThat(testBeneficiaire.getExplicationRefus()).isEqualTo(UPDATED_EXPLICATION_REFUS);
        assertThat(testBeneficiaire.getNbrEnfants()).isEqualTo(UPDATED_NBR_ENFANTS);
        assertThat(testBeneficiaire.getNiveauScolarite()).isEqualTo(UPDATED_NIVEAU_SCOLARITE);
        assertThat(testBeneficiaire.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testBeneficiaire.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testBeneficiaire.getNumeroDossier()).isEqualTo(UPDATED_NUMERO_DOSSIER);
        assertThat(testBeneficiaire.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testBeneficiaire.getPrenomFr()).isEqualTo(UPDATED_PRENOM_FR);
        assertThat(testBeneficiaire.getProfession()).isEqualTo(UPDATED_PROFESSION);
        assertThat(testBeneficiaire.getSelectionner()).isEqualTo(UPDATED_SELECTIONNER);
        assertThat(testBeneficiaire.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testBeneficiaire.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testBeneficiaire.getAutreBenef2019()).isEqualTo(UPDATED_AUTRE_BENEF_2019);
        assertThat(testBeneficiaire.getAutreBenef2020()).isEqualTo(UPDATED_AUTRE_BENEF_2020);
        assertThat(testBeneficiaire.getRelationFamiliale()).isEqualTo(UPDATED_RELATION_FAMILIALE);
        assertThat(testBeneficiaire.getLieuTravailProfessionnel()).isEqualTo(UPDATED_LIEU_TRAVAIL_PROFESSIONNEL);
        assertThat(testBeneficiaire.getSpecialiteProfessionnel()).isEqualTo(UPDATED_SPECIALITE_PROFESSIONNEL);
    }

    @Test
    @Transactional
    void putNonExistingBeneficiaire() throws Exception {
        int databaseSizeBeforeUpdate = beneficiaireRepository.findAll().size();
        beneficiaire.setId(count.incrementAndGet());

        // Create the Beneficiaire
        BeneficiaireDTO beneficiaireDTO = beneficiaireMapper.toDto(beneficiaire);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBeneficiaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, beneficiaireDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(beneficiaireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Beneficiaire in the database
        List<Beneficiaire> beneficiaireList = beneficiaireRepository.findAll();
        assertThat(beneficiaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBeneficiaire() throws Exception {
        int databaseSizeBeforeUpdate = beneficiaireRepository.findAll().size();
        beneficiaire.setId(count.incrementAndGet());

        // Create the Beneficiaire
        BeneficiaireDTO beneficiaireDTO = beneficiaireMapper.toDto(beneficiaire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBeneficiaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(beneficiaireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Beneficiaire in the database
        List<Beneficiaire> beneficiaireList = beneficiaireRepository.findAll();
        assertThat(beneficiaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBeneficiaire() throws Exception {
        int databaseSizeBeforeUpdate = beneficiaireRepository.findAll().size();
        beneficiaire.setId(count.incrementAndGet());

        // Create the Beneficiaire
        BeneficiaireDTO beneficiaireDTO = beneficiaireMapper.toDto(beneficiaire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBeneficiaireMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(beneficiaireDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Beneficiaire in the database
        List<Beneficiaire> beneficiaireList = beneficiaireRepository.findAll();
        assertThat(beneficiaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBeneficiaireWithPatch() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        int databaseSizeBeforeUpdate = beneficiaireRepository.findAll().size();

        // Update the beneficiaire using partial update
        Beneficiaire partialUpdatedBeneficiaire = new Beneficiaire();
        partialUpdatedBeneficiaire.setId(beneficiaire.getId());

        partialUpdatedBeneficiaire
            .typeBeneficiare(UPDATED_TYPE_BENEFICIARE)
            .cin(UPDATED_CIN)
            .dateModification(UPDATED_DATE_MODIFICATION)
            .dateNaissance(UPDATED_DATE_NAISSANCE)
            .email(UPDATED_EMAIL)
            .etat(UPDATED_ETAT)
            .explicationRefus(UPDATED_EXPLICATION_REFUS)
            .niveauScolarite(UPDATED_NIVEAU_SCOLARITE)
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .prenomFr(UPDATED_PRENOM_FR)
            .profession(UPDATED_PROFESSION)
            .sexe(UPDATED_SEXE)
            .telephone(UPDATED_TELEPHONE)
            .autreBenef2019(UPDATED_AUTRE_BENEF_2019)
            .relationFamiliale(UPDATED_RELATION_FAMILIALE)
            .lieuTravailProfessionnel(UPDATED_LIEU_TRAVAIL_PROFESSIONNEL);

        restBeneficiaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBeneficiaire.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBeneficiaire))
            )
            .andExpect(status().isOk());

        // Validate the Beneficiaire in the database
        List<Beneficiaire> beneficiaireList = beneficiaireRepository.findAll();
        assertThat(beneficiaireList).hasSize(databaseSizeBeforeUpdate);
        Beneficiaire testBeneficiaire = beneficiaireList.get(beneficiaireList.size() - 1);
        assertThat(testBeneficiaire.getTypeBeneficiare()).isEqualTo(UPDATED_TYPE_BENEFICIARE);
        assertThat(testBeneficiaire.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testBeneficiaire.getBenef2019()).isEqualTo(DEFAULT_BENEF_2019);
        assertThat(testBeneficiaire.getBenef2020()).isEqualTo(DEFAULT_BENEF_2020);
        assertThat(testBeneficiaire.getCin()).isEqualTo(UPDATED_CIN);
        assertThat(testBeneficiaire.getDateCreation()).isEqualTo(DEFAULT_DATE_CREATION);
        assertThat(testBeneficiaire.getDateModification()).isEqualTo(UPDATED_DATE_MODIFICATION);
        assertThat(testBeneficiaire.getDateNaissance()).isEqualTo(UPDATED_DATE_NAISSANCE);
        assertThat(testBeneficiaire.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testBeneficiaire.getEtat()).isEqualTo(UPDATED_ETAT);
        assertThat(testBeneficiaire.getExplicationRefus()).isEqualTo(UPDATED_EXPLICATION_REFUS);
        assertThat(testBeneficiaire.getNbrEnfants()).isEqualTo(DEFAULT_NBR_ENFANTS);
        assertThat(testBeneficiaire.getNiveauScolarite()).isEqualTo(UPDATED_NIVEAU_SCOLARITE);
        assertThat(testBeneficiaire.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testBeneficiaire.getNomFr()).isEqualTo(DEFAULT_NOM_FR);
        assertThat(testBeneficiaire.getNumeroDossier()).isEqualTo(DEFAULT_NUMERO_DOSSIER);
        assertThat(testBeneficiaire.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testBeneficiaire.getPrenomFr()).isEqualTo(UPDATED_PRENOM_FR);
        assertThat(testBeneficiaire.getProfession()).isEqualTo(UPDATED_PROFESSION);
        assertThat(testBeneficiaire.getSelectionner()).isEqualTo(DEFAULT_SELECTIONNER);
        assertThat(testBeneficiaire.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testBeneficiaire.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testBeneficiaire.getAutreBenef2019()).isEqualTo(UPDATED_AUTRE_BENEF_2019);
        assertThat(testBeneficiaire.getAutreBenef2020()).isEqualTo(DEFAULT_AUTRE_BENEF_2020);
        assertThat(testBeneficiaire.getRelationFamiliale()).isEqualTo(UPDATED_RELATION_FAMILIALE);
        assertThat(testBeneficiaire.getLieuTravailProfessionnel()).isEqualTo(UPDATED_LIEU_TRAVAIL_PROFESSIONNEL);
        assertThat(testBeneficiaire.getSpecialiteProfessionnel()).isEqualTo(DEFAULT_SPECIALITE_PROFESSIONNEL);
    }

    @Test
    @Transactional
    void fullUpdateBeneficiaireWithPatch() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        int databaseSizeBeforeUpdate = beneficiaireRepository.findAll().size();

        // Update the beneficiaire using partial update
        Beneficiaire partialUpdatedBeneficiaire = new Beneficiaire();
        partialUpdatedBeneficiaire.setId(beneficiaire.getId());

        partialUpdatedBeneficiaire
            .typeBeneficiare(UPDATED_TYPE_BENEFICIARE)
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
            .relationFamiliale(UPDATED_RELATION_FAMILIALE)
            .lieuTravailProfessionnel(UPDATED_LIEU_TRAVAIL_PROFESSIONNEL)
            .specialiteProfessionnel(UPDATED_SPECIALITE_PROFESSIONNEL);

        restBeneficiaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBeneficiaire.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBeneficiaire))
            )
            .andExpect(status().isOk());

        // Validate the Beneficiaire in the database
        List<Beneficiaire> beneficiaireList = beneficiaireRepository.findAll();
        assertThat(beneficiaireList).hasSize(databaseSizeBeforeUpdate);
        Beneficiaire testBeneficiaire = beneficiaireList.get(beneficiaireList.size() - 1);
        assertThat(testBeneficiaire.getTypeBeneficiare()).isEqualTo(UPDATED_TYPE_BENEFICIARE);
        assertThat(testBeneficiaire.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testBeneficiaire.getBenef2019()).isEqualTo(UPDATED_BENEF_2019);
        assertThat(testBeneficiaire.getBenef2020()).isEqualTo(UPDATED_BENEF_2020);
        assertThat(testBeneficiaire.getCin()).isEqualTo(UPDATED_CIN);
        assertThat(testBeneficiaire.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
        assertThat(testBeneficiaire.getDateModification()).isEqualTo(UPDATED_DATE_MODIFICATION);
        assertThat(testBeneficiaire.getDateNaissance()).isEqualTo(UPDATED_DATE_NAISSANCE);
        assertThat(testBeneficiaire.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testBeneficiaire.getEtat()).isEqualTo(UPDATED_ETAT);
        assertThat(testBeneficiaire.getExplicationRefus()).isEqualTo(UPDATED_EXPLICATION_REFUS);
        assertThat(testBeneficiaire.getNbrEnfants()).isEqualTo(UPDATED_NBR_ENFANTS);
        assertThat(testBeneficiaire.getNiveauScolarite()).isEqualTo(UPDATED_NIVEAU_SCOLARITE);
        assertThat(testBeneficiaire.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testBeneficiaire.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testBeneficiaire.getNumeroDossier()).isEqualTo(UPDATED_NUMERO_DOSSIER);
        assertThat(testBeneficiaire.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testBeneficiaire.getPrenomFr()).isEqualTo(UPDATED_PRENOM_FR);
        assertThat(testBeneficiaire.getProfession()).isEqualTo(UPDATED_PROFESSION);
        assertThat(testBeneficiaire.getSelectionner()).isEqualTo(UPDATED_SELECTIONNER);
        assertThat(testBeneficiaire.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testBeneficiaire.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testBeneficiaire.getAutreBenef2019()).isEqualTo(UPDATED_AUTRE_BENEF_2019);
        assertThat(testBeneficiaire.getAutreBenef2020()).isEqualTo(UPDATED_AUTRE_BENEF_2020);
        assertThat(testBeneficiaire.getRelationFamiliale()).isEqualTo(UPDATED_RELATION_FAMILIALE);
        assertThat(testBeneficiaire.getLieuTravailProfessionnel()).isEqualTo(UPDATED_LIEU_TRAVAIL_PROFESSIONNEL);
        assertThat(testBeneficiaire.getSpecialiteProfessionnel()).isEqualTo(UPDATED_SPECIALITE_PROFESSIONNEL);
    }

    @Test
    @Transactional
    void patchNonExistingBeneficiaire() throws Exception {
        int databaseSizeBeforeUpdate = beneficiaireRepository.findAll().size();
        beneficiaire.setId(count.incrementAndGet());

        // Create the Beneficiaire
        BeneficiaireDTO beneficiaireDTO = beneficiaireMapper.toDto(beneficiaire);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBeneficiaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, beneficiaireDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(beneficiaireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Beneficiaire in the database
        List<Beneficiaire> beneficiaireList = beneficiaireRepository.findAll();
        assertThat(beneficiaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBeneficiaire() throws Exception {
        int databaseSizeBeforeUpdate = beneficiaireRepository.findAll().size();
        beneficiaire.setId(count.incrementAndGet());

        // Create the Beneficiaire
        BeneficiaireDTO beneficiaireDTO = beneficiaireMapper.toDto(beneficiaire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBeneficiaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(beneficiaireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Beneficiaire in the database
        List<Beneficiaire> beneficiaireList = beneficiaireRepository.findAll();
        assertThat(beneficiaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBeneficiaire() throws Exception {
        int databaseSizeBeforeUpdate = beneficiaireRepository.findAll().size();
        beneficiaire.setId(count.incrementAndGet());

        // Create the Beneficiaire
        BeneficiaireDTO beneficiaireDTO = beneficiaireMapper.toDto(beneficiaire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBeneficiaireMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(beneficiaireDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Beneficiaire in the database
        List<Beneficiaire> beneficiaireList = beneficiaireRepository.findAll();
        assertThat(beneficiaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBeneficiaire() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        int databaseSizeBeforeDelete = beneficiaireRepository.findAll().size();

        // Delete the beneficiaire
        restBeneficiaireMockMvc
            .perform(delete(ENTITY_API_URL_ID, beneficiaire.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Beneficiaire> beneficiaireList = beneficiaireRepository.findAll();
        assertThat(beneficiaireList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
