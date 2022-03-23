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
 * Criteria class for the {@link ma.raafiq.domain.AppRole} entity. This class is used
 * in {@link ma.raafiq.web.rest.AppRoleResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /app-roles?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class AppRoleCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter libeleAr;

    private StringFilter libeleFr;

    private LongFilter appUserId;

    private Boolean distinct;

    public AppRoleCriteria() {}

    public AppRoleCriteria(AppRoleCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.libeleAr = other.libeleAr == null ? null : other.libeleAr.copy();
        this.libeleFr = other.libeleFr == null ? null : other.libeleFr.copy();
        this.appUserId = other.appUserId == null ? null : other.appUserId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public AppRoleCriteria copy() {
        return new AppRoleCriteria(this);
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
        final AppRoleCriteria that = (AppRoleCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(libeleAr, that.libeleAr) &&
            Objects.equals(libeleFr, that.libeleFr) &&
            Objects.equals(appUserId, that.appUserId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, libeleAr, libeleFr, appUserId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppRoleCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (libeleAr != null ? "libeleAr=" + libeleAr + ", " : "") +
            (libeleFr != null ? "libeleFr=" + libeleFr + ", " : "") +
            (appUserId != null ? "appUserId=" + appUserId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
