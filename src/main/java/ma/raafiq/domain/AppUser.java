package ma.raafiq.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A AppUser.
 */
@Entity
@Table(name = "app_user")
public class AppUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active;

    @Column(name = "date_creation")
    private Instant dateCreation;

    @Column(name = "date_modification")
    private Instant dateModification;

    @NotNull
    @Size(max = 255)
    @Column(name = "email", length = 255, nullable = false, unique = true)
    private String email;

    @Size(max = 255)
    @Column(name = "entite", length = 255)
    private String entite;

    @Size(max = 255)
    @Column(name = "nom", length = 255)
    private String nom;

    @NotNull
    @Size(max = 255)
    @Column(name = "password", length = 255, nullable = false)
    private String password;

    @Size(max = 255)
    @Column(name = "prenom", length = 255)
    private String prenom;

    @Size(max = 255)
    @Column(name = "telephone", length = 255)
    private String telephone;

    @NotNull
    @Size(max = 255)
    @Column(name = "user_name", length = 255, nullable = false, unique = true)
    private String userName;

    @ManyToOne
    @JsonIgnoreProperties(value = { "appUsers" }, allowSetters = true)
    private AppRole appRole;

    @ManyToOne
    @JsonIgnoreProperties(value = { "region", "appUsers", "beneficiaires", "familles", "professionnels" }, allowSetters = true)
    private Province province;

    @OneToMany(mappedBy = "appUser")
    @JsonIgnoreProperties(value = { "appUser", "enfant", "motifRefus", "province" }, allowSetters = true)
    private Set<Beneficiaire> beneficiaires = new HashSet<>();

    @OneToMany(mappedBy = "appUser")
    @JsonIgnoreProperties(value = { "appUser", "enfant", "motifRefus", "province" }, allowSetters = true)
    private Set<Famille> familles = new HashSet<>();

    @OneToMany(mappedBy = "appUser")
    @JsonIgnoreProperties(value = { "appUser", "enfant", "motifRefus", "province" }, allowSetters = true)
    private Set<Professionnel> professionnels = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AppUser id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getActive() {
        return this.active;
    }

    public AppUser active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Instant getDateCreation() {
        return this.dateCreation;
    }

    public AppUser dateCreation(Instant dateCreation) {
        this.setDateCreation(dateCreation);
        return this;
    }

    public void setDateCreation(Instant dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Instant getDateModification() {
        return this.dateModification;
    }

    public AppUser dateModification(Instant dateModification) {
        this.setDateModification(dateModification);
        return this;
    }

    public void setDateModification(Instant dateModification) {
        this.dateModification = dateModification;
    }

    public String getEmail() {
        return this.email;
    }

    public AppUser email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEntite() {
        return this.entite;
    }

    public AppUser entite(String entite) {
        this.setEntite(entite);
        return this;
    }

    public void setEntite(String entite) {
        this.entite = entite;
    }

    public String getNom() {
        return this.nom;
    }

    public AppUser nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPassword() {
        return this.password;
    }

    public AppUser password(String password) {
        this.setPassword(password);
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public AppUser prenom(String prenom) {
        this.setPrenom(prenom);
        return this;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public AppUser telephone(String telephone) {
        this.setTelephone(telephone);
        return this;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getUserName() {
        return this.userName;
    }

    public AppUser userName(String userName) {
        this.setUserName(userName);
        return this;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public AppRole getAppRole() {
        return this.appRole;
    }

    public void setAppRole(AppRole appRole) {
        this.appRole = appRole;
    }

    public AppUser appRole(AppRole appRole) {
        this.setAppRole(appRole);
        return this;
    }

    public Province getProvince() {
        return this.province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    public AppUser province(Province province) {
        this.setProvince(province);
        return this;
    }

    public Set<Beneficiaire> getBeneficiaires() {
        return this.beneficiaires;
    }

    public void setBeneficiaires(Set<Beneficiaire> beneficiaires) {
        if (this.beneficiaires != null) {
            this.beneficiaires.forEach(i -> i.setAppUser(null));
        }
        if (beneficiaires != null) {
            beneficiaires.forEach(i -> i.setAppUser(this));
        }
        this.beneficiaires = beneficiaires;
    }

    public AppUser beneficiaires(Set<Beneficiaire> beneficiaires) {
        this.setBeneficiaires(beneficiaires);
        return this;
    }

    public AppUser addBeneficiaire(Beneficiaire beneficiaire) {
        this.beneficiaires.add(beneficiaire);
        beneficiaire.setAppUser(this);
        return this;
    }

    public AppUser removeBeneficiaire(Beneficiaire beneficiaire) {
        this.beneficiaires.remove(beneficiaire);
        beneficiaire.setAppUser(null);
        return this;
    }

    public Set<Famille> getFamilles() {
        return this.familles;
    }

    public void setFamilles(Set<Famille> familles) {
        if (this.familles != null) {
            this.familles.forEach(i -> i.setAppUser(null));
        }
        if (familles != null) {
            familles.forEach(i -> i.setAppUser(this));
        }
        this.familles = familles;
    }

    public AppUser familles(Set<Famille> familles) {
        this.setFamilles(familles);
        return this;
    }

    public AppUser addFamille(Famille famille) {
        this.familles.add(famille);
        famille.setAppUser(this);
        return this;
    }

    public AppUser removeFamille(Famille famille) {
        this.familles.remove(famille);
        famille.setAppUser(null);
        return this;
    }

    public Set<Professionnel> getProfessionnels() {
        return this.professionnels;
    }

    public void setProfessionnels(Set<Professionnel> professionnels) {
        if (this.professionnels != null) {
            this.professionnels.forEach(i -> i.setAppUser(null));
        }
        if (professionnels != null) {
            professionnels.forEach(i -> i.setAppUser(this));
        }
        this.professionnels = professionnels;
    }

    public AppUser professionnels(Set<Professionnel> professionnels) {
        this.setProfessionnels(professionnels);
        return this;
    }

    public AppUser addProfessionnel(Professionnel professionnel) {
        this.professionnels.add(professionnel);
        professionnel.setAppUser(this);
        return this;
    }

    public AppUser removeProfessionnel(Professionnel professionnel) {
        this.professionnels.remove(professionnel);
        professionnel.setAppUser(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppUser)) {
            return false;
        }
        return id != null && id.equals(((AppUser) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppUser{" +
            "id=" + getId() +
            ", active='" + getActive() + "'" +
            ", dateCreation='" + getDateCreation() + "'" +
            ", dateModification='" + getDateModification() + "'" +
            ", email='" + getEmail() + "'" +
            ", entite='" + getEntite() + "'" +
            ", nom='" + getNom() + "'" +
            ", password='" + getPassword() + "'" +
            ", prenom='" + getPrenom() + "'" +
            ", telephone='" + getTelephone() + "'" +
            ", userName='" + getUserName() + "'" +
            "}";
    }
}
