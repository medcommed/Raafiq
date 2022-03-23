package ma.raafiq.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link ma.raafiq.domain.Enfant} entity. This class is used
 * in {@link ma.raafiq.web.rest.EnfantResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /enfants?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class EnfantCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter dateDiagnostic;

    private LocalDateFilter dateNaissance;

    private IntegerFilter degreAutisme;

    private IntegerFilter mutualiste;

    private StringFilter nom;

    private StringFilter nomFr;

    private StringFilter nomMedecin;

    private StringFilter prenom;

    private StringFilter prenomfr;

    private IntegerFilter scolariser;

    private IntegerFilter sexe;

    private StringFilter specialiteMedecin;

    private LongFilter beneficiaireId;

    private LongFilter familleId;

    private LongFilter professionnelId;

    private Boolean distinct;

    public EnfantCriteria() {}

    public EnfantCriteria(EnfantCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.dateDiagnostic = other.dateDiagnostic == null ? null : other.dateDiagnostic.copy();
        this.dateNaissance = other.dateNaissance == null ? null : other.dateNaissance.copy();
        this.degreAutisme = other.degreAutisme == null ? null : other.degreAutisme.copy();
        this.mutualiste = other.mutualiste == null ? null : other.mutualiste.copy();
        this.nom = other.nom == null ? null : other.nom.copy();
        this.nomFr = other.nomFr == null ? null : other.nomFr.copy();
        this.nomMedecin = other.nomMedecin == null ? null : other.nomMedecin.copy();
        this.prenom = other.prenom == null ? null : other.prenom.copy();
        this.prenomfr = other.prenomfr == null ? null : other.prenomfr.copy();
        this.scolariser = other.scolariser == null ? null : other.scolariser.copy();
        this.sexe = other.sexe == null ? null : other.sexe.copy();
        this.specialiteMedecin = other.specialiteMedecin == null ? null : other.specialiteMedecin.copy();
        this.beneficiaireId = other.beneficiaireId == null ? null : other.beneficiaireId.copy();
        this.familleId = other.familleId == null ? null : other.familleId.copy();
        this.professionnelId = other.professionnelId == null ? null : other.professionnelId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public EnfantCriteria copy() {
        return new EnfantCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LocalDateFilter getDateDiagnostic() {
        return dateDiagnostic;
    }

    public LocalDateFilter dateDiagnostic() {
        if (dateDiagnostic == null) {
            dateDiagnostic = new LocalDateFilter();
        }
        return dateDiagnostic;
    }

    public void setDateDiagnostic(LocalDateFilter dateDiagnostic) {
        this.dateDiagnostic = dateDiagnostic;
    }

    public LocalDateFilter getDateNaissance() {
        return dateNaissance;
    }

    public LocalDateFilter dateNaissance() {
        if (dateNaissance == null) {
            dateNaissance = new LocalDateFilter();
        }
        return dateNaissance;
    }

    public void setDateNaissance(LocalDateFilter dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public IntegerFilter getDegreAutisme() {
        return degreAutisme;
    }

    public IntegerFilter degreAutisme() {
        if (degreAutisme == null) {
            degreAutisme = new IntegerFilter();
        }
        return degreAutisme;
    }

    public void setDegreAutisme(IntegerFilter degreAutisme) {
        this.degreAutisme = degreAutisme;
    }

    public IntegerFilter getMutualiste() {
        return mutualiste;
    }

    public IntegerFilter mutualiste() {
        if (mutualiste == null) {
            mutualiste = new IntegerFilter();
        }
        return mutualiste;
    }

    public void setMutualiste(IntegerFilter mutualiste) {
        this.mutualiste = mutualiste;
    }

    public StringFilter getNom() {
        return nom;
    }

    public StringFilter nom() {
        if (nom == null) {
            nom = new StringFilter();
        }
        return nom;
    }

    public void setNom(StringFilter nom) {
        this.nom = nom;
    }

    public StringFilter getNomFr() {
        return nomFr;
    }

    public StringFilter nomFr() {
        if (nomFr == null) {
            nomFr = new StringFilter();
        }
        return nomFr;
    }

    public void setNomFr(StringFilter nomFr) {
        this.nomFr = nomFr;
    }

    public StringFilter getNomMedecin() {
        return nomMedecin;
    }

    public StringFilter nomMedecin() {
        if (nomMedecin == null) {
            nomMedecin = new StringFilter();
        }
        return nomMedecin;
    }

    public void setNomMedecin(StringFilter nomMedecin) {
        this.nomMedecin = nomMedecin;
    }

    public StringFilter getPrenom() {
        return prenom;
    }

    public StringFilter prenom() {
        if (prenom == null) {
            prenom = new StringFilter();
        }
        return prenom;
    }

    public void setPrenom(StringFilter prenom) {
        this.prenom = prenom;
    }

    public StringFilter getPrenomfr() {
        return prenomfr;
    }

    public StringFilter prenomfr() {
        if (prenomfr == null) {
            prenomfr = new StringFilter();
        }
        return prenomfr;
    }

    public void setPrenomfr(StringFilter prenomfr) {
        this.prenomfr = prenomfr;
    }

    public IntegerFilter getScolariser() {
        return scolariser;
    }

    public IntegerFilter scolariser() {
        if (scolariser == null) {
            scolariser = new IntegerFilter();
        }
        return scolariser;
    }

    public void setScolariser(IntegerFilter scolariser) {
        this.scolariser = scolariser;
    }

    public IntegerFilter getSexe() {
        return sexe;
    }

    public IntegerFilter sexe() {
        if (sexe == null) {
            sexe = new IntegerFilter();
        }
        return sexe;
    }

    public void setSexe(IntegerFilter sexe) {
        this.sexe = sexe;
    }

    public StringFilter getSpecialiteMedecin() {
        return specialiteMedecin;
    }

    public StringFilter specialiteMedecin() {
        if (specialiteMedecin == null) {
            specialiteMedecin = new StringFilter();
        }
        return specialiteMedecin;
    }

    public void setSpecialiteMedecin(StringFilter specialiteMedecin) {
        this.specialiteMedecin = specialiteMedecin;
    }

    public LongFilter getBeneficiaireId() {
        return beneficiaireId;
    }

    public LongFilter beneficiaireId() {
        if (beneficiaireId == null) {
            beneficiaireId = new LongFilter();
        }
        return beneficiaireId;
    }

    public void setBeneficiaireId(LongFilter beneficiaireId) {
        this.beneficiaireId = beneficiaireId;
    }

    public LongFilter getFamilleId() {
        return familleId;
    }

    public LongFilter familleId() {
        if (familleId == null) {
            familleId = new LongFilter();
        }
        return familleId;
    }

    public void setFamilleId(LongFilter familleId) {
        this.familleId = familleId;
    }

    public LongFilter getProfessionnelId() {
        return professionnelId;
    }

    public LongFilter professionnelId() {
        if (professionnelId == null) {
            professionnelId = new LongFilter();
        }
        return professionnelId;
    }

    public void setProfessionnelId(LongFilter professionnelId) {
        this.professionnelId = professionnelId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final EnfantCriteria that = (EnfantCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(dateDiagnostic, that.dateDiagnostic) &&
            Objects.equals(dateNaissance, that.dateNaissance) &&
            Objects.equals(degreAutisme, that.degreAutisme) &&
            Objects.equals(mutualiste, that.mutualiste) &&
            Objects.equals(nom, that.nom) &&
            Objects.equals(nomFr, that.nomFr) &&
            Objects.equals(nomMedecin, that.nomMedecin) &&
            Objects.equals(prenom, that.prenom) &&
            Objects.equals(prenomfr, that.prenomfr) &&
            Objects.equals(scolariser, that.scolariser) &&
            Objects.equals(sexe, that.sexe) &&
            Objects.equals(specialiteMedecin, that.specialiteMedecin) &&
            Objects.equals(beneficiaireId, that.beneficiaireId) &&
            Objects.equals(familleId, that.familleId) &&
            Objects.equals(professionnelId, that.professionnelId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            dateDiagnostic,
            dateNaissance,
            degreAutisme,
            mutualiste,
            nom,
            nomFr,
            nomMedecin,
            prenom,
            prenomfr,
            scolariser,
            sexe,
            specialiteMedecin,
            beneficiaireId,
            familleId,
            professionnelId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EnfantCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (dateDiagnostic != null ? "dateDiagnostic=" + dateDiagnostic + ", " : "") +
            (dateNaissance != null ? "dateNaissance=" + dateNaissance + ", " : "") +
            (degreAutisme != null ? "degreAutisme=" + degreAutisme + ", " : "") +
            (mutualiste != null ? "mutualiste=" + mutualiste + ", " : "") +
            (nom != null ? "nom=" + nom + ", " : "") +
            (nomFr != null ? "nomFr=" + nomFr + ", " : "") +
            (nomMedecin != null ? "nomMedecin=" + nomMedecin + ", " : "") +
            (prenom != null ? "prenom=" + prenom + ", " : "") +
            (prenomfr != null ? "prenomfr=" + prenomfr + ", " : "") +
            (scolariser != null ? "scolariser=" + scolariser + ", " : "") +
            (sexe != null ? "sexe=" + sexe + ", " : "") +
            (specialiteMedecin != null ? "specialiteMedecin=" + specialiteMedecin + ", " : "") +
            (beneficiaireId != null ? "beneficiaireId=" + beneficiaireId + ", " : "") +
            (familleId != null ? "familleId=" + familleId + ", " : "") +
            (professionnelId != null ? "professionnelId=" + professionnelId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
