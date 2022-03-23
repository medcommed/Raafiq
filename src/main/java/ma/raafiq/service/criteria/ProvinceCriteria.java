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
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link ma.raafiq.domain.Province} entity. This class is used
 * in {@link ma.raafiq.web.rest.ProvinceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /provinces?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class ProvinceCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter libeleAr;

    private StringFilter libeleFr;

    private LongFilter regionId;

    private LongFilter appUserId;

    private LongFilter beneficiaireId;

    private LongFilter familleId;

    private LongFilter professionnelId;

    private Boolean distinct;

    public ProvinceCriteria() {}

    public ProvinceCriteria(ProvinceCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.libeleAr = other.libeleAr == null ? null : other.libeleAr.copy();
        this.libeleFr = other.libeleFr == null ? null : other.libeleFr.copy();
        this.regionId = other.regionId == null ? null : other.regionId.copy();
        this.appUserId = other.appUserId == null ? null : other.appUserId.copy();
        this.beneficiaireId = other.beneficiaireId == null ? null : other.beneficiaireId.copy();
        this.familleId = other.familleId == null ? null : other.familleId.copy();
        this.professionnelId = other.professionnelId == null ? null : other.professionnelId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ProvinceCriteria copy() {
        return new ProvinceCriteria(this);
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

    public StringFilter getLibeleAr() {
        return libeleAr;
    }

    public StringFilter libeleAr() {
        if (libeleAr == null) {
            libeleAr = new StringFilter();
        }
        return libeleAr;
    }

    public void setLibeleAr(StringFilter libeleAr) {
        this.libeleAr = libeleAr;
    }

    public StringFilter getLibeleFr() {
        return libeleFr;
    }

    public StringFilter libeleFr() {
        if (libeleFr == null) {
            libeleFr = new StringFilter();
        }
        return libeleFr;
    }

    public void setLibeleFr(StringFilter libeleFr) {
        this.libeleFr = libeleFr;
    }

    public LongFilter getRegionId() {
        return regionId;
    }

    public LongFilter regionId() {
        if (regionId == null) {
            regionId = new LongFilter();
        }
        return regionId;
    }

    public void setRegionId(LongFilter regionId) {
        this.regionId = regionId;
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
        final ProvinceCriteria that = (ProvinceCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(libeleAr, that.libeleAr) &&
            Objects.equals(libeleFr, that.libeleFr) &&
            Objects.equals(regionId, that.regionId) &&
            Objects.equals(appUserId, that.appUserId) &&
            Objects.equals(beneficiaireId, that.beneficiaireId) &&
            Objects.equals(familleId, that.familleId) &&
            Objects.equals(professionnelId, that.professionnelId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, libeleAr, libeleFr, regionId, appUserId, beneficiaireId, familleId, professionnelId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProvinceCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (libeleAr != null ? "libeleAr=" + libeleAr + ", " : "") +
            (libeleFr != null ? "libeleFr=" + libeleFr + ", " : "") +
            (regionId != null ? "regionId=" + regionId + ", " : "") +
            (appUserId != null ? "appUserId=" + appUserId + ", " : "") +
            (beneficiaireId != null ? "beneficiaireId=" + beneficiaireId + ", " : "") +
            (familleId != null ? "familleId=" + familleId + ", " : "") +
            (professionnelId != null ? "professionnelId=" + professionnelId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
