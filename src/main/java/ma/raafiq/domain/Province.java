package ma.raafiq.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Province.
 */
@Entity
@Table(name = "province")
public class Province implements Serializable {

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

    @ManyToOne
    @JsonIgnoreProperties(value = { "provinces" }, allowSetters = true)
    private Region region;

    @OneToMany(mappedBy = "province")
    @JsonIgnoreProperties(value = { "appRole", "province", "beneficiaires", "familles", "professionnels" }, allowSetters = true)
    private Set<AppUser> appUsers = new HashSet<>();

    @OneToMany(mappedBy = "province")
    @JsonIgnoreProperties(value = { "appUser", "enfant", "motifRefus", "province" }, allowSetters = true)
    private Set<Beneficiaire> beneficiaires = new HashSet<>();

    @OneToMany(mappedBy = "province")
    @JsonIgnoreProperties(value = { "appUser", "enfant", "motifRefus", "province" }, allowSetters = true)
    private Set<Famille> familles = new HashSet<>();

    @OneToMany(mappedBy = "province")
    @JsonIgnoreProperties(value = { "appUser", "enfant", "motifRefus", "province" }, allowSetters = true)
    private Set<Professionnel> professionnels = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Province id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibeleAr() {
        return this.libeleAr;
    }

    public Province libeleAr(String libeleAr) {
        this.setLibeleAr(libeleAr);
        return this;
    }

    public void setLibeleAr(String libeleAr) {
        this.libeleAr = libeleAr;
    }

    public String getLibeleFr() {
        return this.libeleFr;
    }

    public Province libeleFr(String libeleFr) {
        this.setLibeleFr(libeleFr);
        return this;
    }

    public void setLibeleFr(String libeleFr) {
        this.libeleFr = libeleFr;
    }

    public Region getRegion() {
        return this.region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public Province region(Region region) {
        this.setRegion(region);
        return this;
    }

    public Set<AppUser> getAppUsers() {
        return this.appUsers;
    }

    public void setAppUsers(Set<AppUser> appUsers) {
        if (this.appUsers != null) {
            this.appUsers.forEach(i -> i.setProvince(null));
        }
        if (appUsers != null) {
            appUsers.forEach(i -> i.setProvince(this));
        }
        this.appUsers = appUsers;
    }

    public Province appUsers(Set<AppUser> appUsers) {
        this.setAppUsers(appUsers);
        return this;
    }

    public Province addAppUser(AppUser appUser) {
        this.appUsers.add(appUser);
        appUser.setProvince(this);
        return this;
    }

    public Province removeAppUser(AppUser appUser) {
        this.appUsers.remove(appUser);
        appUser.setProvince(null);
        return this;
    }

    public Set<Beneficiaire> getBeneficiaires() {
        return this.beneficiaires;
    }

    public void setBeneficiaires(Set<Beneficiaire> beneficiaires) {
        if (this.beneficiaires != null) {
            this.beneficiaires.forEach(i -> i.setProvince(null));
        }
        if (beneficiaires != null) {
            beneficiaires.forEach(i -> i.setProvince(this));
        }
        this.beneficiaires = beneficiaires;
    }

    public Province beneficiaires(Set<Beneficiaire> beneficiaires) {
        this.setBeneficiaires(beneficiaires);
        return this;
    }

    public Province addBeneficiaire(Beneficiaire beneficiaire) {
        this.beneficiaires.add(beneficiaire);
        beneficiaire.setProvince(this);
        return this;
    }

    public Province removeBeneficiaire(Beneficiaire beneficiaire) {
        this.beneficiaires.remove(beneficiaire);
        beneficiaire.setProvince(null);
        return this;
    }

    public Set<Famille> getFamilles() {
        return this.familles;
    }

    public void setFamilles(Set<Famille> familles) {
        if (this.familles != null) {
            this.familles.forEach(i -> i.setProvince(null));
        }
        if (familles != null) {
            familles.forEach(i -> i.setProvince(this));
        }
        this.familles = familles;
    }

    public Province familles(Set<Famille> familles) {
        this.setFamilles(familles);
        return this;
    }

    public Province addFamille(Famille famille) {
        this.familles.add(famille);
        famille.setProvince(this);
        return this;
    }

    public Province removeFamille(Famille famille) {
        this.familles.remove(famille);
        famille.setProvince(null);
        return this;
    }

    public Set<Professionnel> getProfessionnels() {
        return this.professionnels;
    }

    public void setProfessionnels(Set<Professionnel> professionnels) {
        if (this.professionnels != null) {
            this.professionnels.forEach(i -> i.setProvince(null));
        }
        if (professionnels != null) {
            professionnels.forEach(i -> i.setProvince(this));
        }
        this.professionnels = professionnels;
    }

    public Province professionnels(Set<Professionnel> professionnels) {
        this.setProfessionnels(professionnels);
        return this;
    }

    public Province addProfessionnel(Professionnel professionnel) {
        this.professionnels.add(professionnel);
        professionnel.setProvince(this);
        return this;
    }

    public Province removeProfessionnel(Professionnel professionnel) {
        this.professionnels.remove(professionnel);
        professionnel.setProvince(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Province)) {
            return false;
        }
        return id != null && id.equals(((Province) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Province{" +
            "id=" + getId() +
            ", libeleAr='" + getLibeleAr() + "'" +
            ", libeleFr='" + getLibeleFr() + "'" +
            "}";
    }
}
