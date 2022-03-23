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
 * Criteria class for the {@link ma.raafiq.domain.Region} entity. This class is used
 * in {@link ma.raafiq.web.rest.RegionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /regions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class RegionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter libeleAr;

    private StringFilter libeleFr;

    private LongFilter provinceId;

    private Boolean distinct;

    public RegionCriteria() {}

    public RegionCriteria(RegionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.libeleAr = other.libeleAr == null ? null : other.libeleAr.copy();
        this.libeleFr = other.libeleFr == null ? null : other.libeleFr.copy();
        this.provinceId = other.provinceId == null ? null : other.provinceId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public RegionCriteria copy() {
        return new RegionCriteria(this);
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
        final RegionCriteria that = (RegionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(libeleAr, that.libeleAr) &&
            Objects.equals(libeleFr, that.libeleFr) &&
            Objects.equals(provinceId, that.provinceId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, libeleAr, libeleFr, provinceId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RegionCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (libeleAr != null ? "libeleAr=" + libeleAr + ", " : "") +
            (libeleFr != null ? "libeleFr=" + libeleFr + ", " : "") +
            (provinceId != null ? "provinceId=" + provinceId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
