package ma.raafiq.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ma.raafiq.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EnfantDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EnfantDTO.class);
        EnfantDTO enfantDTO1 = new EnfantDTO();
        enfantDTO1.setId(1L);
        EnfantDTO enfantDTO2 = new EnfantDTO();
        assertThat(enfantDTO1).isNotEqualTo(enfantDTO2);
        enfantDTO2.setId(enfantDTO1.getId());
        assertThat(enfantDTO1).isEqualTo(enfantDTO2);
        enfantDTO2.setId(2L);
        assertThat(enfantDTO1).isNotEqualTo(enfantDTO2);
        enfantDTO1.setId(null);
        assertThat(enfantDTO1).isNotEqualTo(enfantDTO2);
    }
}
