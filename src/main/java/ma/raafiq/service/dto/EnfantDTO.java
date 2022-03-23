package ma.raafiq.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link ma.raafiq.domain.Enfant} entity.
 */
public class EnfantDTO implements Serializable {

    @NotNull
    private Long id;

    private LocalDate dateDiagnostic;

    private LocalDate dateNaissance;

    private Integer degreAutisme;

    private Integer mutualiste;

    @Size(max = 255)
    private String nom;

    @Size(max = 255)
    private String nomFr;

    @Size(max = 255)
    private String nomMedecin;

    @Size(max = 255)
    private String prenom;

    @Size(max = 255)
    private String prenomfr;

    private Integer scolariser;

    private Integer sexe;

    @Size(max = 255)
    private String specialiteMedecin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateDiagnostic() {
        return dateDiagnostic;
    }

    public void setDateDiagnostic(LocalDate dateDiagnostic) {
        this.dateDiagnostic = dateDiagnostic;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public Integer getDegreAutisme() {
        return degreAutisme;
    }

    public void setDegreAutisme(Integer degreAutisme) {
        this.degreAutisme = degreAutisme;
    }

    public Integer getMutualiste() {
        return mutualiste;
    }

    public void setMutualiste(Integer mutualiste) {
        this.mutualiste = mutualiste;
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

    public String getNomMedecin() {
        return nomMedecin;
    }

    public void setNomMedecin(String nomMedecin) {
        this.nomMedecin = nomMedecin;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getPrenomfr() {
        return prenomfr;
    }

    public void setPrenomfr(String prenomfr) {
        this.prenomfr = prenomfr;
    }

    public Integer getScolariser() {
        return scolariser;
    }

    public void setScolariser(Integer scolariser) {
        this.scolariser = scolariser;
    }

    public Integer getSexe() {
        return sexe;
    }

    public void setSexe(Integer sexe) {
        this.sexe = sexe;
    }

    public String getSpecialiteMedecin() {
        return specialiteMedecin;
    }

    public void setSpecialiteMedecin(String specialiteMedecin) {
        this.specialiteMedecin = specialiteMedecin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EnfantDTO)) {
            return false;
        }

        EnfantDTO enfantDTO = (EnfantDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, enfantDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EnfantDTO{" +
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
