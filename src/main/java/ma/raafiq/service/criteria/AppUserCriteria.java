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
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link ma.raafiq.domain.AppUser} entity. This class is used
 * in {@link ma.raafiq.web.rest.AppUserResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /app-users?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class AppUserCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BooleanFilter active;

    private InstantFilter dateCreation;

    private InstantFilter dateModification;

    private StringFilter email;

    private StringFilter entite;

    private StringFilter nom;

    private StringFilter password;

    private StringFilter prenom;

    private StringFilter telephone;

    private StringFilter userName;

    private LongFilter appRoleId;

    private LongFilter provinceId;

    private LongFilter beneficiaireId;

    private LongFilter familleId;

    private LongFilter professionnelId;

    private Boolean distinct;

    public AppUserCriteria() {}

    public AppUserCriteria(AppUserCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.active = other.active == null ? null : other.active.copy();
        this.dateCreation = other.dateCreation == null ? null : other.dateCreation.copy();
        this.dateModification = other.dateModification == null ? null : other.dateModification.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.entite = other.entite == null ? null : other.entite.copy();
        this.nom = other.nom == null ? null : other.nom.copy();
        this.password = other.password == null ? null : other.password.copy();
        this.prenom = other.prenom == null ? null : other.prenom.copy();
        this.telephone = other.telephone == null ? null : other.telephone.copy();
        this.userName = other.userName == null ? null : other.userName.copy();
        this.appRoleId = other.appRoleId == null ? null : other.appRoleId.copy();
        this.provinceId = other.provinceId == null ? null : other.provinceId.copy();
        this.beneficiaireId = other.beneficiaireId == null ? null : other.beneficiaireId.copy();
        this.familleId = other.familleId == null ? null : other.familleId.copy();
        this.professionnelId = other.professionnelId == null ? null : other.professionnelId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public AppUserCriteria copy() {
        return new AppUserCriteria(this);
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

    public BooleanFilter getActive() {
        return active;
    }

    public BooleanFilter active() {
        if (active == null) {
            active = new BooleanFilter();
        }
        return active;
    }

    public void setActive(BooleanFilter active) {
        this.active = active;
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

    public StringFilter getEntite() {
        return entite;
    }

    public StringFilter entite() {
        if (entite == null) {
            entite = new StringFilter();
        }
        return entite;
    }

    public void setEntite(StringFilter entite) {
        this.entite = entite;
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

    public StringFilter getPassword() {
        return password;
    }

    public StringFilter password() {
        if (password == null) {
            password = new StringFilter();
        }
        return password;
    }

    public void setPassword(StringFilter password) {
        this.password = password;
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

    public StringFilter getUserName() {
        return userName;
    }

    public StringFilter userName() {
        if (userName == null) {
            userName = new StringFilter();
        }
        return userName;
    }

    public void setUserName(StringFilter userName) {
        this.userName = userName;
    }

    public LongFilter getAppRoleId() {
        return appRoleId;
    }

    public LongFilter appRoleId() {
        if (appRoleId == null) {
            appRoleId = new LongFilter();
        }
        return appRoleId;
    }

    public void setAppRoleId(LongFilter appRoleId) {
        this.appRoleId = appRoleId;
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
        final AppUserCriteria that = (AppUserCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(active, that.active) &&
            Objects.equals(dateCreation, that.dateCreation) &&
            Objects.equals(dateModification, that.dateModification) &&
            Objects.equals(email, that.email) &&
            Objects.equals(entite, that.entite) &&
            Objects.equals(nom, that.nom) &&
            Objects.equals(password, that.password) &&
            Objects.equals(prenom, that.prenom) &&
            Objects.equals(telephone, that.telephone) &&
            Objects.equals(userName, that.userName) &&
            Objects.equals(appRoleId, that.appRoleId) &&
            Objects.equals(provinceId, that.provinceId) &&
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
            active,
            dateCreation,
            dateModification,
            email,
            entite,
            nom,
            password,
            prenom,
            telephone,
            userName,
            appRoleId,
            provinceId,
            beneficiaireId,
            familleId,
            professionnelId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppUserCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (active != null ? "active=" + active + ", " : "") +
            (dateCreation != null ? "dateCreation=" + dateCreation + ", " : "") +
            (dateModification != null ? "dateModification=" + dateModification + ", " : "") +
            (email != null ? "email=" + email + ", " : "") +
            (entite != null ? "entite=" + entite + ", " : "") +
            (nom != null ? "nom=" + nom + ", " : "") +
            (password != null ? "password=" + password + ", " : "") +
            (prenom != null ? "prenom=" + prenom + ", " : "") +
            (telephone != null ? "telephone=" + telephone + ", " : "") +
            (userName != null ? "userName=" + userName + ", " : "") +
            (appRoleId != null ? "appRoleId=" + appRoleId + ", " : "") +
            (provinceId != null ? "provinceId=" + provinceId + ", " : "") +
            (beneficiaireId != null ? "beneficiaireId=" + beneficiaireId + ", " : "") +
            (familleId != null ? "familleId=" + familleId + ", " : "") +
            (professionnelId != null ? "professionnelId=" + professionnelId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
