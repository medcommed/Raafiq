package ma.raafiq.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link ma.raafiq.domain.Professionnel} entity.
 */
public class ProfessionnelDTO implements Serializable {

    @NotNull
    private Long id;

    @Size(max = 255)
    private String adresse;

    private Integer benef2019;

    private Integer benef2020;

    @NotNull
    @Size(max = 255)
    private String cin;

    private Instant dateCreation;

    private Instant dateModification;

    private LocalDate dateNaissance;

    @NotNull
    @Size(max = 255)
    private String email;

    private Integer etat;

    @Size(max = 255)
    private String explicationRefus;

    @NotNull
    private Integer nbrEnfants;

    private Integer niveauScolarite;

    @Size(max = 255)
    private String nom;

    @Size(max = 255)
    private String nomFr;

    @NotNull
    @Size(max = 255)
    private String numeroDossier;

    @Size(max = 255)
    private String prenom;

    @Size(max = 255)
    private String prenomFr;

    @Size(max = 255)
    private String profession;

    private Integer selectionner;

    private Integer sexe;

    @Size(max = 255)
    private String telephone;

    @Size(max = 255)
    private String lieuTravailProfessionnel;

    private Integer specialiteProfessionnel;

    private AppUserDTO appUser;

    private EnfantDTO enfant;

    private MotifRefusDTO motifRefus;

    private ProvinceDTO province;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public Integer getBenef2019() {
        return benef2019;
    }

    public void setBenef2019(Integer benef2019) {
        this.benef2019 = benef2019;
    }

    public Integer getBenef2020() {
        return benef2020;
    }

    public void setBenef2020(Integer benef2020) {
        this.benef2020 = benef2020;
    }

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public Instant getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Instant dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Instant getDateModification() {
        return dateModification;
    }

    public void setDateModification(Instant dateModification) {
        this.dateModification = dateModification;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getEtat() {
        return etat;
    }

    public void setEtat(Integer etat) {
        this.etat = etat;
    }

    public String getExplicationRefus() {
        return explicationRefus;
    }

    public void setExplicationRefus(String explicationRefus) {
        this.explicationRefus = explicationRefus;
    }

    public Integer getNbrEnfants() {
        return nbrEnfants;
    }

    public void setNbrEnfants(Integer nbrEnfants) {
        this.nbrEnfants = nbrEnfants;
    }

    public Integer getNiveauScolarite() {
        return niveauScolarite;
    }

    public void setNiveauScolarite(Integer niveauScolarite) {
        this.niveauScolarite = niveauScolarite;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getNomFr() {
        return nomFr;
    }

    public void setNomFr(String nomFr) {
        this.nomFr = nomFr;
    }

    public String getNumeroDossier() {
        return numeroDossier;
    }

    public void setNumeroDossier(String numeroDossier) {
        this.numeroDossier = numeroDossier;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getPrenomFr() {
        return prenomFr;
    }

    public void setPrenomFr(String prenomFr) {
        this.prenomFr = prenomFr;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public Integer getSelectionner() {
        return selectionner;
    }

    public void setSelectionner(Integer selectionner) {
        this.selectionner = selectionner;
    }

    public Integer getSexe() {
        return sexe;
    }

    public void setSexe(Integer sexe) {
        this.sexe = sexe;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getLieuTravailProfessionnel() {
        return lieuTravailProfessionnel;
    }

    public void setLieuTravailProfessionnel(String lieuTravailProfessionnel) {
        this.lieuTravailProfessionnel = lieuTravailProfessionnel;
    }

    public Integer getSpecialiteProfessionnel() {
        return specialiteProfessionnel;
    }

    public void setSpecialiteProfessionnel(Integer specialiteProfessionnel) {
        this.specialiteProfessionnel = specialiteProfessionnel;
    }

    public AppUserDTO getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUserDTO appUser) {
        this.appUser = appUser;
    }

    public EnfantDTO getEnfant() {
        return enfant;
    }

    public void setEnfant(EnfantDTO enfant) {
        this.enfant = enfant;
    }

    public MotifRefusDTO getMotifRefus() {
        return motifRefus;
    }

    public void setMotifRefus(MotifRefusDTO motifRefus) {
        this.motifRefus = motifRefus;
    }

    public ProvinceDTO getProvince() {
        return province;
    }

    public void setProvince(ProvinceDTO province) {
        this.province = province;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProfessionnelDTO)) {
            return false;
        }

        ProfessionnelDTO professionnelDTO = (ProfessionnelDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, professionnelDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProfessionnelDTO{" +
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
            ", lieuTravailProfessionnel='" + getLieuTravailProfessionnel() + "'" +
            ", specialiteProfessionnel=" + getSpecialiteProfessionnel() +
            ", appUser=" + getAppUser() +
            ", enfant=" + getEnfant() +
            ", motifRefus=" + getMotifRefus() +
            ", province=" + getProvince() +
            "}";
    }
}
