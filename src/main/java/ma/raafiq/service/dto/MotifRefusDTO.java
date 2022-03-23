package ma.raafiq.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link ma.raafiq.domain.MotifRefus} entity.
 */
public class MotifRefusDTO implements Serializable {

    @NotNull
    private Long id;

    @Size(max = 255)
    private String libeleAr;

    @Size(max = 255)
    private String libeleFr;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibeleAr() {
        return libeleAr;
    }

    public void setLibeleAr(String libeleAr) {
        this.libeleAr = libeleAr;
    }

    public String getLibeleFr() {
        return libeleFr;
    }

    public void setLibeleFr(String libeleFr) {
        this.libeleFr = libeleFr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MotifRefusDTO)) {
            return false;
        }

        MotifRefusDTO motifRefusDTO = (MotifRefusDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, motifRefusDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MotifRefusDTO{" +
            "id=" + getId() +
            ", libeleAr='" + getLibeleAr() + "'" +
            ", libeleFr='" + getLibeleFr() + "'" +
            "}";
    }
}
