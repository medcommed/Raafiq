package ma.raafiq.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link ma.raafiq.domain.Professionnel} entity. This class is used
 * in {@link ma.raafiq.web.rest.ProfessionnelResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /professionnels?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class ProfessionnelCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter adresse;

    private IntegerFilter benef2019;

    private IntegerFilter benef2020;

    private StringFilter cin;

    private InstantFilter dateCreation;

    private InstantFilter dateModification;

    private LocalDateFilter dateNaissance;

    private StringFilter email;

    private IntegerFilter etat;

    private StringFilter explicationRefus;

    private IntegerFilter nbrEnfants;

    private IntegerFilter niveauScolarite;

    private StringFilter nom;

    private StringFilter nomFr;

    private StringFilter numeroDossier;

    private StringFilter prenom;

    private StringFilter prenomFr;

    private StringFilter profession;

    private IntegerFilter selectionner;

    private IntegerFilter sexe;

    private StringFilter telephone;

    private StringFilter lieuTravailProfessionnel;

    private IntegerFilter specialiteProfessionnel;

    private LongFilter appUserId;

    private LongFilter enfantId;

    private LongFilter motifRefusId;

    private LongFilter provinceId;

    private Boolean distinct;

    public ProfessionnelCriteria() {}

    public ProfessionnelCriteria(ProfessionnelCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.adresse = other.adresse == null ? null : other.adresse.copy();
        this.benef2019 = other.benef2019 == null ? null : other.benef2019.copy();
        this.benef2020 = other.benef2020 == null ? null : other.benef2020.copy();
        this.cin = other.cin == null ? null : other.cin.copy();
        this.dateCreation = other.dateCreation == null ? null : other.dateCreation.copy();
        this.dateModification = other.dateModification == null ? null : other.dateModification.copy();
        this.dateNaissance = other.dateNaissance == null ? null : other.dateNaissance.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.etat = other.etat == null ? null : other.etat.copy();
        this.explicationRefus = other.explicationRefus == null ? null : other.explicationRefus.copy();
        this.nbrEnfants = other.nbrEnfants == null ? null : other.nbrEnfants.copy();
        this.niveauScolarite = other.niveauScolarite == null ? null : other.niveauScolarite.copy();
        this.nom = other.nom == null ? null : other.nom.copy();
        this.nomFr = other.nomFr == null ? null : other.nomFr.copy();
        this.numeroDossier = other.numeroDossier == null ? null : other.numeroDossier.copy();
        this.prenom = other.prenom == null ? null : other.prenom.copy();
        this.prenomFr = other.prenomFr == null ? null : other.prenomFr.copy();
        this.profession = other.profession == null ? null : other.profession.copy();
        this.selectionner = other.selectionner == null ? null : other.selectionner.copy();
        this.sexe = other.sexe == null ? null : other.sexe.copy();
        this.telephone = other.telephone == null ? null : other.telephone.copy();
        this.lieuTravailProfessionnel = other.lieuTravailProfessionnel == null ? null : other.lieuTravailProfessionnel.copy();
        this.specialiteProfessionnel = other.specialiteProfessionnel == null ? null : other.specialiteProfessionnel.copy();
        this.appUserId = other.appUserId == null ? null : other.appUserId.copy();
        this.enfantId = other.enfantId == null ? null : other.enfantId.copy();
        this.motifRefusId = other.motifRefusId == null ? null : other.motifRefusId.copy();
        this.provinceId = other.provinceId == null ? null : other.provinceId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ProfessionnelCriteria copy() {
        return new ProfessionnelCriteria(this);
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

    public StringFilter getAdresse() {
        return adresse;
    }

    public StringFilter adresse() {
        if (adresse == null) {
            adresse = new StringFilter();
        }
        return adresse;
    }

    public void setAdresse(StringFilter adresse) {
        this.adresse = adresse;
    }

    public IntegerFilter getBenef2019() {
        return benef2019;
    }

    public IntegerFilter benef2019() {
        if (benef2019 == null) {
            benef2019 = new IntegerFilter();
        }
        return benef2019;
    }

    public void setBenef2019(IntegerFilter benef2019) {
        this.benef2019 = benef2019;
    }

    public IntegerFilter getBenef2020() {
        return benef2020;
    }

    public IntegerFilter benef2020() {
        if (benef2020 == null) {
            benef2020 = new IntegerFilter();
        }
        return benef2020;
    }

    public void setBenef2020(IntegerFilter benef2020) {
        this.benef2020 = benef2020;
    }

    public StringFilter getCin() {
        return cin;
    }

    public StringFilter cin() {
        if (cin == null) {
            cin = new StringFilter();
        }
        return cin;
    }

    public void setCin(StringFilter cin) {
        this.cin = cin;
    }

    public InstantFilter getDateCreation() {
        return dateCreation;
    }

    public InstantFilter dateCreation() {
        if (dateCreation == null) {
            dateCreation = new InstantFilter();
        }
        return dateCreation;
    }

    public void setDateCreation(InstantFilter dateCreation) {
        this.dateCreation = dateCreation;
    }

    public InstantFilter getDateModification() {
        return dateModification;
    }

    public InstantFilter dateModification() {
        if (dateModification == null) {
            dateModification = new InstantFilter();
        }
        return dateModification;
    }

    public void setDateModification(InstantFilter dateModification) {
        this.dateModification = dateModification;
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

    public StringFilter getEmail() {
        return email;
    }

    public StringFilter email() {
        if (email == null) {
            email = new StringFilter();
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public IntegerFilter getEtat() {
        return etat;
    }

    public IntegerFilter etat() {
        if (etat == null) {
            etat = new IntegerFilter();
        }
        return etat;
    }

    public void setEtat(IntegerFilter etat) {
        this.etat = etat;
    }

    public StringFilter getExplicationRefus() {
        return explicationRefus;
    }

    public StringFilter explicationRefus() {
        if (explicationRefus == null) {
            explicationRefus = new StringFilter();
        }
        return explicationRefus;
    }

    public void setExplicationRefus(StringFilter explicationRefus) {
        this.explicationRefus = explicationRefus;
    }

    public IntegerFilter getNbrEnfants() {
        return nbrEnfants;
    }

    public IntegerFilter nbrEnfants() {
        if (nbrEnfants == null) {
            nbrEnfants = new IntegerFilter();
        }
        return nbrEnfants;
    }

    public void setNbrEnfants(IntegerFilter nbrEnfants) {
        this.nbrEnfants = nbrEnfants;
    }

    public IntegerFilter getNiveauScolarite() {
        return niveauScolarite;
    }

    public IntegerFilter niveauScolarite() {
        if (niveauScolarite == null) {
            niveauScolarite = new IntegerFilter();
        }
        return niveauScolarite;
    }

    public void setNiveauScolarite(IntegerFilter niveauScolarite) {
        this.niveauScolarite = niveauScolarite;
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

    public StringFilter getNumeroDossier() {
        return numeroDossier;
    }

    public StringFilter numeroDossier() {
        if (numeroDossier == null) {
            numeroDossier = new StringFilter();
        }
        return numeroDossier;
    }

    public void setNumeroDossier(StringFilter numeroDossier) {
        this.numeroDossier = numeroDossier;
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

    public StringFilter getPrenomFr() {
        return prenomFr;
    }

    public StringFilter prenomFr() {
        if (prenomFr == null) {
            prenomFr = new StringFilter();
        }
        return prenomFr;
    }

    public void setPrenomFr(StringFilter prenomFr) {
        this.prenomFr = prenomFr;
    }

    public StringFilter getProfession() {
        return profession;
    }

    public StringFilter profession() {
        if (profession == null) {
            profession = new StringFilter();
        }
        return profession;
    }

    public void setProfession(StringFilter profession) {
        this.profession = profession;
    }

    public IntegerFilter getSelectionner() {
        return selectionner;
    }

    public IntegerFilter selectionner() {
        if (selectionner == null) {
            selectionner = new IntegerFilter();
        }
        return selectionner;
    }

    public void setSelectionner(IntegerFilter selectionner) {
        this.selectionner = selectionner;
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

    public StringFilter getTelephone() {
        return telephone;
    }

    public StringFilter telephone() {
        if (telephone == null) {
            telephone = new StringFilter();
        }
        return telephone;
    }

    public void setTelephone(StringFilter telephone) {
        this.telephone = telephone;
    }

    public StringFilter getLieuTravailProfessionnel() {
        return lieuTravailProfessionnel;
    }

    public StringFilter lieuTravailProfessionnel() {
        if (lieuTravailProfessionnel == null) {
            lieuTravailProfessionnel = new StringFilter();
        }
        return lieuTravailProfessionnel;
    }

    public void setLieuTravailProfessionnel(StringFilter lieuTravailProfessionnel) {
        this.lieuTravailProfessionnel = lieuTravailProfessionnel;
    }

    public IntegerFilter getSpecialiteProfessionnel() {
        return specialiteProfessionnel;
    }

    public IntegerFilter specialiteProfessionnel() {
        if (specialiteProfessionnel == null) {
            specialiteProfessionnel = new IntegerFilter();
        }
        return specialiteProfessionnel;
    }

    public void setSpecialiteProfessionnel(IntegerFilter specialiteProfessionnel) {
        this.specialiteProfessionnel = specialiteProfessionnel;
    }

    public LongFilter getAppUserId() {
        return appUserId;
    }

    public LongFilter appUserId() {
        if (appUserId == null) {
            appUserId = new LongFilter();
        }
        return appUserId;
    }

    public void setAppUserId(LongFilter appUserId) {
        this.appUserId = appUserId;
    }

    public LongFilter getEnfantId() {
        return enfantId;
    }

    public LongFilter enfantId() {
        if (enfantId == null) {
            enfantId = new LongFilter();
        }
        return enfantId;
    }

    public void setEnfantId(LongFilter enfantId) {
        this.enfantId = enfantId;
    }

    public LongFilter getMotifRefusId() {
        return motifRefusId;
    }

    public LongFilter motifRefusId() {
        if (motifRefusId == null) {
            motifRefusId = new LongFilter();
        }
        return motifRefusId;
    }

    public void setMotifRefusId(LongFilter motifRefusId) {
        this.motifRefusId = motifRefusId;
    }

    public LongFilter getProvinceId() {
        return provinceId;
    }

    public LongFilter provinceId() {
        if (provinceId == null) {
            provinceId = new LongFilter();
        }
        return provinceId;
    }

    public void setProvinceId(LongFilter provinceId) {
        this.provinceId = provinceId;
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
        final ProfessionnelCriteria that = (ProfessionnelCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(adresse, that.adresse) &&
            Objects.equals(benef2019, that.benef2019) &&
            Objects.equals(benef2020, that.benef2020) &&
            Objects.equals(cin, that.cin) &&
            Objects.equals(dateCreation, that.dateCreation) &&
            Objects.equals(dateModification, that.dateModification) &&
            Objects.equals(dateNaissance, that.dateNaissance) &&
            Objects.equals(email, that.email) &&
            Objects.equals(etat, that.etat) &&
            Objects.equals(explicationRefus, that.explicationRefus) &&
            Objects.equals(nbrEnfants, that.nbrEnfants) &&
            Objects.equals(niveauScolarite, that.niveauScolarite) &&
            Objects.equals(nom, that.nom) &&
            Objects.equals(nomFr, that.nomFr) &&
            Objects.equals(numeroDossier, that.numeroDossier) &&
            Objects.equals(prenom, that.prenom) &&
            Objects.equals(prenomFr, that.prenomFr) &&
            Objects.equals(profession, that.profession) &&
            Objects.equals(selectionner, that.selectionner) &&
            Objects.equals(sexe, that.sexe) &&
            Objects.equals(telephone, that.telephone) &&
            Objects.equals(lieuTravailProfessionnel, that.lieuTravailProfessionnel) &&
            Objects.equals(specialiteProfessionnel, that.specialiteProfessionnel) &&
            Objects.equals(appUserId, that.appUserId) &&
            Objects.equals(enfantId, that.enfantId) &&
            Objects.equals(motifRefusId, that.motifRefusId) &&
            Objects.equals(provinceId, that.provinceId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            adresse,
            benef2019,
            benef2020,
            cin,
            dateCreation,
            dateModification,
            dateNaissance,
            email,
            etat,
            explicationRefus,
            nbrEnfants,
            niveauScolarite,
            nom,
            nomFr,
            numeroDossier,
            prenom,
            prenomFr,
            profession,
            selectionner,
            sexe,
            telephone,
            lieuTravailProfessionnel,
            specialiteProfessionnel,
            appUserId,
            enfantId,
            motifRefusId,
            provinceId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProfessionnelCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (adresse != null ? "adresse=" + adresse + ", " : "") +
            (benef2019 != null ? "benef2019=" + benef2019 + ", " : "") +
            (benef2020 != null ? "benef2020=" + benef2020 + ", " : "") +
            (cin != null ? "cin=" + cin + ", " : "") +
            (dateCreation != null ? "dateCreation=" + dateCreation + ", " : "") +
            (dateModification != null ? "dateModification=" + dateModification + ", " : "") +
            (dateNaissance != null ? "dateNaissance=" + dateNaissance + ", " : "") +
            (email != null ? "email=" + email + ", " : "") +
            (etat != null ? "etat=" + etat + ", " : "") +
            (explicationRefus != null ? "explicationRefus=" + explicationRefus + ", " : "") +
            (nbrEnfants != null ? "nbrEnfants=" + nbrEnfants + ", " : "") +
            (niveauScolarite != null ? "niveauScolarite=" + niveauScolarite + ", " : "") +
            (nom != null ? "nom=" + nom + ", " : "") +
            (nomFr != null ? "nomFr=" + nomFr + ", " : "") +
            (numeroDossier != null ? "numeroDossier=" + numeroDossier + ", " : "") +
            (prenom != null ? "prenom=" + prenom + ", " : "") +
            (prenomFr != null ? "prenomFr=" + prenomFr + ", " : "") +
            (profession != null ? "profession=" + profession + ", " : "") +
            (selectionner != null ? "selectionner=" + selectionner + ", " : "") +
            (sexe != null ? "sexe=" + sexe + ", " : "") +
            (telephone != null ? "telephone=" + telephone + ", " : "") +
            (lieuTravailProfessionnel != null ? "lieuTravailProfessionnel=" + lieuTravailProfessionnel + ", " : "") +
            (specialiteProfessionnel != null ? "specialiteProfessionnel=" + specialiteProfessionnel + ", " : "") +
            (appUserId != null ? "appUserId=" + appUserId + ", " : "") +
            (enfantId != null ? "enfantId=" + enfantId + ", " : "") +
            (motifRefusId != null ? "motifRefusId=" + motifRefusId + ", " : "") +
            (provinceId != null ? "provinceId=" + provinceId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
