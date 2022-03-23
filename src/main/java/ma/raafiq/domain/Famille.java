package ma.raafiq.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Famille.
 */
@Entity
@Table(name = "famille")
public class Famille implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 255)
    @Column(name = "adresse", length = 255)
    private String adresse;

    @Column(name = "benef_2019")
    private Integer benef2019;

    @Column(name = "benef_2020")
    private Integer benef2020;

    @NotNull
    @Size(max = 255)
    @Column(name = "cin", length = 255, nullable = false, unique = true)
    private String cin;

    @Column(name = "date_creation")
    private Instant dateCreation;

    @Column(name = "date_modification")
    private Instant dateModification;

    @Column(name = "date_naissance")
    private LocalDate dateNaissance;

    @NotNull
    @Size(max = 255)
    @Column(name = "email", length = 255, nullable = false, unique = true)
    private String email;

    @Column(name = "etat")
    private Integer etat;

    @Size(max = 255)
    @Column(name = "explication_refus", length = 255)
    private String explicationRefus;

    @NotNull
    @Column(name = "nbr_enfants", nullable = false)
    private Integer nbrEnfants;

    @Column(name = "niveau_scolarite")
    private Integer niveauScolarite;

    @Size(max = 255)
    @Column(name = "nom", length = 255)
    private String nom;

    @Size(max = 255)
    @Column(name = "nom_fr", length = 255)
    private String nomFr;

    @NotNull
    @Size(max = 255)
    @Column(name = "numero_dossier", length = 255, nullable = false, unique = true)
    private String numeroDossier;

    @Size(max = 255)
    @Column(name = "prenom", length = 255)
    private String prenom;

    @Size(max = 255)
    @Column(name = "prenom_fr", length = 255)
    private String prenomFr;

    @Size(max = 255)
    @Column(name = "profession", length = 255)
    private String profession;

    @Column(name = "selectionner")
    private Integer selectionner;

    @Column(name = "sexe")
    private Integer sexe;

    @Size(max = 255)
    @Column(name = "telephone", length = 255)
    private String telephone;

    @Column(name = "autre_benef_2019")
    private Integer autreBenef2019;

    @Column(name = "autre_benef_2020")
    private Integer autreBenef2020;

    @Column(name = "relation_familiale")
    private Integer relationFamiliale;

    @ManyToOne
    @JsonIgnoreProperties(value = { "appRole", "province", "beneficiaires", "familles", "professionnels" }, allowSetters = true)
    private AppUser appUser;

    @ManyToOne
    @JsonIgnoreProperties(value = { "beneficiaires", "familles", "professionnels" }, allowSetters = true)
    private Enfant enfant;

    @ManyToOne
    @JsonIgnoreProperties(value = { "beneficiaires", "familles", "professionnels" }, allowSetters = true)
    private MotifRefus motifRefus;

    @ManyToOne
    @JsonIgnoreProperties(value = { "region", "appUsers", "beneficiaires", "familles", "professionnels" }, allowSetters = true)
    private Province province;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Famille id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAdresse() {
        return this.adresse;
    }

    public Famille adresse(String adresse) {
        this.setAdresse(adresse);
        return this;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public Integer getBenef2019() {
        return this.benef2019;
    }

    public Famille benef2019(Integer benef2019) {
        this.setBenef2019(benef2019);
        return this;
    }

    public void setBenef2019(Integer benef2019) {
        this.benef2019 = benef2019;
    }

    public Integer getBenef2020() {
        return this.benef2020;
    }

    public Famille benef2020(Integer benef2020) {
        this.setBenef2020(benef2020);
        return this;
    }

    public void setBenef2020(Integer benef2020) {
        this.benef2020 = benef2020;
    }

    public String getCin() {
        return this.cin;
    }

    public Famille cin(String cin) {
        this.setCin(cin);
        return this;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public Instant getDateCreation() {
        return this.dateCreation;
    }

    public Famille dateCreation(Instant dateCreation) {
        this.setDateCreation(dateCreation);
        return this;
    }

    public void setDateCreation(Instant dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Instant getDateModification() {
        return this.dateModification;
    }

    public Famille dateModification(Instant dateModification) {
        this.setDateModification(dateModification);
        return this;
    }

    public void setDateModification(Instant dateModification) {
        this.dateModification = dateModification;
    }

    public LocalDate getDateNaissance() {
        return this.dateNaissance;
    }

    public Famille dateNaissance(LocalDate dateNaissance) {
        this.setDateNaissance(dateNaissance);
        return this;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getEmail() {
        return this.email;
    }

    public Famille email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getEtat() {
        return this.etat;
    }

    public Famille etat(Integer etat) {
        this.setEtat(etat);
        return this;
    }

    public void setEtat(Integer etat) {
        this.etat = etat;
    }

    public String getExplicationRefus() {
        return this.explicationRefus;
    }

    public Famille explicationRefus(String explicationRefus) {
        this.setExplicationRefus(explicationRefus);
        return this;
    }

    public void setExplicationRefus(String explicationRefus) {
        this.explicationRefus = explicationRefus;
    }

    public Integer getNbrEnfants() {
        return this.nbrEnfants;
    }

    public Famille nbrEnfants(Integer nbrEnfants) {
        this.setNbrEnfants(nbrEnfants);
        return this;
    }

    public void setNbrEnfants(Integer nbrEnfants) {
        this.nbrEnfants = nbrEnfants;
    }

    public Integer getNiveauScolarite() {
        return this.niveauScolarite;
    }

    public Famille niveauScolarite(Integer niveauScolarite) {
        this.setNiveauScolarite(niveauScolarite);
        return this;
    }

    public void setNiveauScolarite(Integer niveauScolarite) {
        this.niveauScolarite = niveauScolarite;
    }

    public String getNom() {
        return this.nom;
    }

    public Famille nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getNomFr() {
        return this.nomFr;
    }

    public Famille nomFr(String nomFr) {
        this.setNomFr(nomFr);
        return this;
    }

    public void setNomFr(String nomFr) {
        this.nomFr = nomFr;
    }

    public String getNumeroDossier() {
        return this.numeroDossier;
    }

    public Famille numeroDossier(String numeroDossier) {
        this.setNumeroDossier(numeroDossier);
        return this;
    }

    public void setNumeroDossier(String numeroDossier) {
        this.numeroDossier = numeroDossier;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public Famille prenom(String prenom) {
        this.setPrenom(prenom);
        return this;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getPrenomFr() {
        return this.prenomFr;
    }

    public Famille prenomFr(String prenomFr) {
        this.setPrenomFr(prenomFr);
        return this;
    }

    public void setPrenomFr(String prenomFr) {
        this.prenomFr = prenomFr;
    }

    public String getProfession() {
        return this.profession;
    }

    public Famille profession(String profession) {
        this.setProfession(profession);
        return this;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public Integer getSelectionner() {
        return this.selectionner;
    }

    public Famille selectionner(Integer selectionner) {
        this.setSelectionner(selectionner);
        return this;
    }

    public void setSelectionner(Integer selectionner) {
        this.selectionner = selectionner;
    }

    public Integer getSexe() {
        return this.sexe;
    }

    public Famille sexe(Integer sexe) {
        this.setSexe(sexe);
        return this;
    }

    public void setSexe(Integer sexe) {
        this.sexe = sexe;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public Famille telephone(String telephone) {
        this.setTelephone(telephone);
        return this;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Integer getAutreBenef2019() {
        return this.autreBenef2019;
    }

    public Famille autreBenef2019(Integer autreBenef2019) {
        this.setAutreBenef2019(autreBenef2019);
        return this;
    }

    public void setAutreBenef2019(Integer autreBenef2019) {
        this.autreBenef2019 = autreBenef2019;
    }

    public Integer getAutreBenef2020() {
        return this.autreBenef2020;
    }

    public Famille autreBenef2020(Integer autreBenef2020) {
        this.setAutreBenef2020(autreBenef2020);
        return this;
    }

    public void setAutreBenef2020(Integer autreBenef2020) {
        this.autreBenef2020 = autreBenef2020;
    }

    public Integer getRelationFamiliale() {
        return this.relationFamiliale;
    }

    public Famille relationFamiliale(Integer relationFamiliale) {
        this.setRelationFamiliale(relationFamiliale);
        return this;
    }

    public void setRelationFamiliale(Integer relationFamiliale) {
        this.relationFamiliale = relationFamiliale;
    }

    public AppUser getAppUser() {
        return this.appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public Famille appUser(AppUser appUser) {
        this.setAppUser(appUser);
        return this;
    }

    public Enfant getEnfant() {
        return this.enfant;
    }

    public void setEnfant(Enfant enfant) {
        this.enfant = enfant;
    }

    public Famille enfant(Enfant enfant) {
        this.setEnfant(enfant);
        return this;
    }

    public MotifRefus getMotifRefus() {
        return this.motifRefus;
    }

    public void setMotifRefus(MotifRefus motifRefus) {
        this.motifRefus = motifRefus;
    }

    public Famille motifRefus(MotifRefus motifRefus) {
        this.setMotifRefus(motifRefus);
        return this;
    }

    public Province getProvince() {
        return this.province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    public Famille province(Province province) {
        this.setProvince(province);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Famille)) {
            return false;
        }
        return id != null && id.equals(((Famille) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Famille{" +
            "id=" + getId() +
            ", adresse='" + getAdresse() + "'" +
            ", benef2019=" + getBenef2019() +
            ", benef2020=" + getBenef2020() +
            ", cin='" + getCin() + "'" +
            ", dateCreation='" + getDateCreation() + "'" +
            ", dateModification='" + getDateModification() + "'" +
            ", dateNaissance='" + getDateNaissance() + "'" +
            ", email='" + getEmail() + "'" +
            ", etat=" + getEtat() +
            ", explicationRefus='" + getExplicationRefus() + "'" +
            ", nbrEnfants=" + getNbrEnfants() +
            ", niveauScolarite=" + getNiveauScolarite() +
            ", nom='" + getNom() + "'" +
            ", nomFr='" + getNomFr() + "'" +
            ", numeroDossier='" + getNumeroDossier() + "'" +
            ", prenom='" + getPrenom() + "'" +
            ", prenomFr='" + getPrenomFr() + "'" +
            ", profession='" + getProfession() + "'" +
            ", selectionner=" + getSelectionner() +
            ", sexe=" + getSexe() +
            ", telephone='" + getTelephone() + "'" +
            ", autreBenef2019=" + getAutreBenef2019() +
            ", autreBenef2020=" + getAutreBenef2020() +
            ", relationFamiliale=" + getRelationFamiliale() +
            "}";
    }
}