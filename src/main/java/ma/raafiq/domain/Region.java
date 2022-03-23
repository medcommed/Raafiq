package ma.raafiq.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Region.
 */
@Entity
@Table(name = "region")
public class Region implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 255)
    @Column(name = "libele_ar", length = 255)
    private String libeleAr;

    @Size(max = 255)
    @Column(name = "libele_fr", length = 255)
    private String libeleFr;

    @OneToMany(mappedBy = "region")
    @JsonIgnoreProperties(value = { "region", "appUsers", "beneficiaires", "familles", "professionnels" }, allowSetters = true)
    private Set<Province> provinces = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Region id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibeleAr() {
        return this.libeleAr;
    }

    public Region libeleAr(String libeleAr) {
        this.setLibeleAr(libeleAr);
        return this;
    }

    public void setLibeleAr(String libeleAr) {
        this.libeleAr = libeleAr;
    }

    public String getLibeleFr() {
        return this.libeleFr;
    }

    public Region libeleFr(String libeleFr) {
        this.setLibeleFr(libeleFr);
        return this;
    }

    public void setLibeleFr(String libeleFr) {
        this.libeleFr = libeleFr;
    }

    public Set<Province> getProvinces() {
        return this.provinces;
    }

    public void setProvinces(Set<Province> provinces) {
        if (this.provinces != null) {
            this.provinces.forEach(i -> i.setRegion(null));
        }
        if (provinces != null) {
            provinces.forEach(i -> i.setRegion(this));
        }
        this.provinces = provinces;
    }

    public Region provinces(Set<Province> provinces) {
        this.setProvinces(provinces);
        return this;
    }

    public Region addProvince(Province province) {
        this.provinces.add(province);
        province.setRegion(this);
        return this;
    }

    public Region removeProvince(Province province) {
        this.provinces.remove(province);
        province.setRegion(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Region)) {
            return false;
        }
        return id != null && id.equals(((Region) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Region{" +
            "id=" + getId() +
            ", libeleAr='" + getLibeleAr() + "'" +
            ", libeleFr='" + getLibeleFr() + "'" +
            "}";
    }
}
