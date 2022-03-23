package ma.raafiq.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A MotifRefus.
 */
@Entity
@Table(name = "motif_refus")
public class MotifRefus implements Serializable {

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

    @OneToMany(mappedBy = "motifRefus")
    @JsonIgnoreProperties(value = { "appUser", "enfant", "motifRefus", "province" }, allowSetters = true)
    private Set<Beneficiaire> beneficiaires = new HashSet<>();

    @OneToMany(mappedBy = "motifRefus")
    @JsonIgnoreProperties(value = { "appUser", "enfant", "motifRefus", "province" }, allowSetters = true)
    private Set<Famille> familles = new HashSet<>();

    @OneToMany(mappedBy = "motifRefus")
    @JsonIgnoreProperties(value = { "appUser", "enfant", "motifRefus", "province" }, allowSetters = true)
    private Set<Professionnel> professionnels = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MotifRefus id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibeleAr() {
        return this.libeleAr;
    }

    public MotifRefus libeleAr(String libeleAr) {
        this.setLibeleAr(libeleAr);
        return this;
    }

    public void setLibeleAr(String libeleAr) {
        this.libeleAr = libeleAr;
    }

    public String getLibeleFr() {
        return this.libeleFr;
    }

    public MotifRefus libeleFr(String libeleFr) {
        this.setLibeleFr(libeleFr);
        return this;
    }

    public void setLibeleFr(String libeleFr) {
        this.libeleFr = libeleFr;
    }

    public Set<Beneficiaire> getBeneficiaires() {
        return this.beneficiaires;
    }

    public void setBeneficiaires(Set<Beneficiaire> beneficiaires) {
        if (this.beneficiaires != null) {
            this.beneficiaires.forEach(i -> i.setMotifRefus(null));
        }
        if (beneficiaires != null) {
            beneficiaires.forEach(i -> i.setMotifRefus(this));
        }
        this.beneficiaires = beneficiaires;
    }

    public MotifRefus beneficiaires(Set<Beneficiaire> beneficiaires) {
        this.setBeneficiaires(beneficiaires);
        return this;
    }

    public MotifRefus addBeneficiaire(Beneficiaire beneficiaire) {
        this.beneficiaires.add(beneficiaire);
        beneficiaire.setMotifRefus(this);
        return this;
    }

    public MotifRefus removeBeneficiaire(Beneficiaire beneficiaire) {
        this.beneficiaires.remove(beneficiaire);
        beneficiaire.setMotifRefus(null);
        return this;
    }

    public Set<Famille> getFamilles() {
        return this.familles;
    }

    public void setFamilles(Set<Famille> familles) {
        if (this.familles != null) {
            this.familles.forEach(i -> i.setMotifRefus(null));
        }
        if (familles != null) {
            familles.forEach(i -> i.setMotifRefus(this));
        }
        this.familles = familles;
    }

    public MotifRefus familles(Set<Famille> familles) {
        this.setFamilles(familles);
        return this;
    }

    public MotifRefus addFamille(Famille famille) {
        this.familles.add(famille);
        famille.setMotifRefus(this);
        return this;
    }

    public MotifRefus removeFamille(Famille famille) {
        this.familles.remove(famille);
        famille.setMotifRefus(null);
        return this;
    }

    public Set<Professionnel> getProfessionnels() {
        return this.professionnels;
    }

    public void setProfessionnels(Set<Professionnel> professionnels) {
        if (this.professionnels != null) {
            this.professionnels.forEach(i -> i.setMotifRefus(null));
        }
        if (professionnels != null) {
            professionnels.forEach(i -> i.setMotifRefus(this));
        }
        this.professionnels = professionnels;
    }

    public MotifRefus professionnels(Set<Professionnel> professionnels) {
        this.setProfessionnels(professionnels);
        return this;
    }

    public MotifRefus addProfessionnel(Professionnel professionnel) {
        this.professionnels.add(professionnel);
        professionnel.setMotifRefus(this);
        return this;
    }

    public MotifRefus removeProfessionnel(Professionnel professionnel) {
        this.professionnels.remove(professionnel);
        professionnel.setMotifRefus(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MotifRefus)) {
            return false;
        }
        return id != null && id.equals(((MotifRefus) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MotifRefus{" +
            "id=" + getId() +
            ", libeleAr='" + getLibeleAr() + "'" +
            ", libeleFr='" + getLibeleFr() + "'" +
            "}";
    }
}
