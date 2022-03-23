package ma.raafiq.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A AppRole.
 */
@Entity
@Table(name = "app_role")
public class AppRole implements Serializable {

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

    @OneToMany(mappedBy = "appRole")
    @JsonIgnoreProperties(value = { "appRole", "province", "beneficiaires", "familles", "professionnels" }, allowSetters = true)
    private Set<AppUser> appUsers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AppRole id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibeleAr() {
        return this.libeleAr;
    }

    public AppRole libeleAr(String libeleAr) {
        this.setLibeleAr(libeleAr);
        return this;
    }

    public void setLibeleAr(String libeleAr) {
        this.libeleAr = libeleAr;
    }

    public String getLibeleFr() {
        return this.libeleFr;
    }

    public AppRole libeleFr(String libeleFr) {
        this.setLibeleFr(libeleFr);
        return this;
    }

    public void setLibeleFr(String libeleFr) {
        this.libeleFr = libeleFr;
    }

    public Set<AppUser> getAppUsers() {
        return this.appUsers;
    }

    public void setAppUsers(Set<AppUser> appUsers) {
        if (this.appUsers != null) {
            this.appUsers.forEach(i -> i.setAppRole(null));
        }
        if (appUsers != null) {
            appUsers.forEach(i -> i.setAppRole(this));
        }
        this.appUsers = appUsers;
    }

    public AppRole appUsers(Set<AppUser> appUsers) {
        this.setAppUsers(appUsers);
        return this;
    }

    public AppRole addAppUser(AppUser appUser) {
        this.appUsers.add(appUser);
        appUser.setAppRole(this);
        return this;
    }

    public AppRole removeAppUser(AppUser appUser) {
        this.appUsers.remove(appUser);
        appUser.setAppRole(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppRole)) {
            return false;
        }
        return id != null && id.equals(((AppRole) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppRole{" +
            "id=" + getId() +
            ", libeleAr='" + getLibeleAr() + "'" +
            ", libeleFr='" + getLibeleFr() + "'" +
            "}";
    }
}
