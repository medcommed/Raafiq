package ma.raafiq.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link ma.raafiq.domain.AppUser} entity.
 */
public class AppUserDTO implements Serializable {

    @NotNull
    private Long id;

    @NotNull
    private Boolean active;

    private Instant dateCreation;

    private Instant dateModification;

    @NotNull
    @Size(max = 255)
    private String email;

    @Size(max = 255)
    private String entite;

    @Size(max = 255)
    private String nom;

    @NotNull
    @Size(max = 255)
    private String password;

    @Size(max = 255)
    private String prenom;

    @Size(max = 255)
    private String telephone;

    @NotNull
    @Size(max = 255)
    private String userName;

    private AppRoleDTO appRole;

    private ProvinceDTO province;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Instant getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Instant dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Instant getDateModification() {
        return dateModification;
    }

    public void setDateModification(Instant dateModification) {
        this.dateModification = dateModification;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEntite() {
        return entite;
    }

    public void setEntite(String entite) {
        this.entite = entite;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public AppRoleDTO getAppRole() {
        return appRole;
    }

    public void setAppRole(AppRoleDTO appRole) {
        this.appRole = appRole;
    }

    public ProvinceDTO getProvince() {
        return province;
    }

    public void setProvince(ProvinceDTO province) {
        this.province = province;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppUserDTO)) {
            return false;
        }

        AppUserDTO appUserDTO = (AppUserDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, appUserDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppUserDTO{" +
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
            ", appRole=" + getAppRole() +
            ", province=" + getProvince() +
            "}";
    }
}
