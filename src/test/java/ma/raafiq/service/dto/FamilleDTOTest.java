package ma.raafiq.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ma.raafiq.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FamilleDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FamilleDTO.class);
        FamilleDTO familleDTO1 = new FamilleDTO();
        familleDTO1.setId(1L);
        FamilleDTO familleDTO2 = new FamilleDTO();
        assertThat(familleDTO1).isNotEqualTo(familleDTO2);
        familleDTO2.setId(familleDTO1.getId());
        assertThat(familleDTO1).isEqualTo(familleDTO2);
        familleDTO2.setId(2L);
        assertThat(familleDTO1).isNotEqualTo(familleDTO2);
        familleDTO1.setId(null);
        assertThat(familleDTO1).isNotEqualTo(familleDTO2);
    }
}
