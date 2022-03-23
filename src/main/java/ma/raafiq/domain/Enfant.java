package ma.raafiq.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Enfant.
 */
@Entity
@Table(name = "enfant")
public class Enfant implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "date_diagnostic")
    private LocalDate dateDiagnostic;

    @Column(name = "date_naissance")
    private LocalDate dateNaissance;

    @Column(name = "degre_autisme")
    private Integer degreAutisme;

    @Column(name = "mutualiste")
    private Integer mutualiste;

    @Size(max = 255)
    @Column(name = "nom", length = 255)
    private String nom;

    @Size(max = 255)
    @Column(name = "nom_fr", length = 255)
    private String nomFr;

    @Size(max = 255)
    @Column(name = "nom_medecin", length = 255)
    private String nomMedecin;

    @Size(max = 255)
    @Column(name = "prenom", length = 255)
    private String prenom;

    @Size(max = 255)
    @Column(name = "prenomfr", length = 255)
    private String prenomfr;

    @Column(name = "scolariser")
    private Integer scolariser;

    @Column(name = "sexe")
    private Integer sexe;

    @Size(max = 255)
    @Column(name = "specialite_medecin", length = 255)
    private String specialiteMedecin;

    @OneToMany(mappedBy = "enfant")
    @JsonIgnoreProperties(value = { "appUser", "enfant", "motifRefus", "province" }, allowSetters = true)
    private Set<Beneficiaire> beneficiaires = new HashSet<>();

    @OneToMany(mappedBy = "enfant")
    @JsonIgnoreProperties(value = { "appUser", "enfant", "motifRefus", "province" }, allowSetters = true)
    private Set<Famille> familles = new HashSet<>();

    @OneToMany(mappedBy = "enfant")
    @JsonIgnoreProperties(value = { "appUser", "enfant", "motifRefus", "province" }, allowSetters = true)
    private Set<Professionnel> professionnels = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Enfant id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateDiagnostic() {
        return this.dateDiagnostic;
    }

    public Enfant dateDiagnostic(LocalDate dateDiagnostic) {
        this.setDateDiagnostic(dateDiagnostic);
        return this;
    }

    public void setDateDiagnostic(LocalDate dateDiagnostic) {
        this.dateDiagnostic = dateDiagnostic;
    }

    public LocalDate getDateNaissance() {
        return this.dateNaissance;
    }

    public Enfant dateNaissance(LocalDate dateNaissance) {
        this.setDateNaissance(dateNaissance);
        return this;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public Integer getDegreAutisme() {
        return this.degreAutisme;
    }

    public Enfant degreAutisme(Integer degreAutisme) {
        this.setDegreAutisme(degreAutisme);
        return this;
    }

    public void setDegreAutisme(Integer degreAutisme) {
        this.degreAutisme = degreAutisme;
    }

    public Integer getMutualiste() {
        return this.mutualiste;
    }

    public Enfant mutualiste(Integer mutualiste) {
        this.setMutualiste(mutualiste);
        return this;
    }

    public void setMutualiste(Integer mutualiste) {
        this.mutualiste = mutualiste;
    }

    public String getNom() {
        return this.nom;
    }

    public Enfant nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getNomFr() {
        return this.nomFr;
    }

    public Enfant nomFr(String nomFr) {
        this.setNomFr(nomFr);
        return this;
    }

    public void setNomFr(String nomFr) {
        this.nomFr = nomFr;
    }

    public String getNomMedecin() {
        return this.nomMedecin;
    }

    public Enfant nomMedecin(String nomMedecin) {
        this.setNomMedecin(nomMedecin);
        return this;
    }

    public void setNomMedecin(String nomMedecin) {
        this.nomMedecin = nomMedecin;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public Enfant prenom(String prenom) {
        this.setPrenom(prenom);
        return this;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getPrenomfr() {
        return this.prenomfr;
    }

    public Enfant prenomfr(String prenomfr) {
        this.setPrenomfr(prenomfr);
        return this;
    }

    public void setPrenomfr(String prenomfr) {
        this.prenomfr = prenomfr;
    }

    public Integer getScolariser() {
        return this.scolariser;
    }

    public Enfant scolariser(Integer scolariser) {
        this.setScolariser(scolariser);
        return this;
    }

    public void setScolariser(Integer scolariser) {
        this.scolariser = scolariser;
    }

    public Integer getSexe() {
        return this.sexe;
    }

    public Enfant sexe(Integer sexe) {
        this.setSexe(sexe);
        return this;
    }

    public void setSexe(Integer sexe) {
        this.sexe = sexe;
    }

    public String getSpecialiteMedecin() {
        return this.specialiteMedecin;
    }

    public Enfant specialiteMedecin(String specialiteMedecin) {
        this.setSpecialiteMedecin(specialiteMedecin);
        return this;
    }

    public void setSpecialiteMedecin(String specialiteMedecin) {
        this.specialiteMedecin = specialiteMedecin;
    }

    public Set<Beneficiaire> getBeneficiaires() {
        return this.beneficiaires;
    }

    public void setBeneficiaires(Set<Beneficiaire> beneficiaires) {
        if (this.beneficiaires != null) {
            this.beneficiaires.forEach(i -> i.setEnfant(null));
        }
        if (beneficiaires != null) {
            beneficiaires.forEach(i -> i.setEnfant(this));
        }
        this.beneficiaires = beneficiaires;
    }

    public Enfant beneficiaires(Set<Beneficiaire> beneficiaires) {
        this.setBeneficiaires(beneficiaires);
        return this;
    }

    public Enfant addBeneficiaire(Beneficiaire beneficiaire) {
        this.beneficiaires.add(beneficiaire);
        beneficiaire.setEnfant(this);
        return this;
    }

    public Enfant removeBeneficiaire(Beneficiaire beneficiaire) {
        this.beneficiaires.remove(beneficiaire);
        beneficiaire.setEnfant(null);
        return this;
    }

    public Set<Famille> getFamilles() {
        return this.familles;
    }

    public void setFamilles(Set<Famille> familles) {
        if (this.familles != null) {
            this.familles.forEach(i -> i.setEnfant(null));
        }
        if (familles != null) {
            familles.forEach(i -> i.setEnfant(this));
        }
        this.familles = familles;
    }

    public Enfant familles(Set<Famille> familles) {
        this.setFamilles(familles);
        return this;
    }

    public Enfant addFamille(Famille famille) {
        this.familles.add(famille);
        famille.setEnfant(this);
        return this;
    }

    public Enfant removeFamille(Famille famille) {
        this.familles.remove(famille);
        famille.setEnfant(null);
        return this;
    }

    public Set<Professionnel> getProfessionnels() {
        return this.professionnels;
    }

    public void setProfessionnels(Set<Professionnel> professionnels) {
        if (this.professionnels != null) {
            this.professionnels.forEach(i -> i.setEnfant(null));
        }
        if (professionnels != null) {
            professionnels.forEach(i -> i.setEnfant(this));
        }
        this.professionnels = professionnels;
    }

    public Enfant professionnels(Set<Professionnel> professionnels) {
        this.setProfessionnels(professionnels);
        return this;
    }

    public Enfant addProfessionnel(Professionnel professionnel) {
        this.professionnels.add(professionnel);
        professionnel.setEnfant(this);
        return this;
    }

    public Enfant removeProfessionnel(Professionnel professionnel) {
        this.professionnels.remove(professionnel);
        professionnel.setEnfant(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Enfant)) {
            return false;
        }
        return id != null && id.equals(((Enfant) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Enfant{" +
            "id=" + getId() +
            ", dateDiagnostic='" + getDateDiagnostic() + "'" +
            ", dateNaissance='" + getDateNaissance() + "'" +
            ", degreAutisme=" + getDegreAutisme() +
            ", mutualiste=" + getMutualiste() +
            ", nom='" + getNom() + "'" +
            ", nomFr='" + getNomFr() + "'" +
            ", nomMedecin='" + getNomMedecin() + "'" +
            ", prenom='" + getPrenom() + "'" +
            ", prenomfr='" + getPrenomfr() + "'" +
            ", scolariser=" + getScolariser() +
            ", sexe=" + getSexe() +
            ", specialiteMedecin='" + getSpecialiteMedecin() + "'" +
            "}";
    }
}
