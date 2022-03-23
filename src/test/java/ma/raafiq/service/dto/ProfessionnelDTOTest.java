package ma.raafiq.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ma.raafiq.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProfessionnelDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProfessionnelDTO.class);
        ProfessionnelDTO professionnelDTO1 = new ProfessionnelDTO();
        professionnelDTO1.setId(1L);
        ProfessionnelDTO professionnelDTO2 = new ProfessionnelDTO();
        assertThat(professionnelDTO1).isNotEqualTo(professionnelDTO2);
        professionnelDTO2.setId(professionnelDTO1.getId());
        assertThat(professionnelDTO1).isEqualTo(professionnelDTO2);
        professionnelDTO2.setId(2L);
        assertThat(professionnelDTO1).isNotEqualTo(professionnelDTO2);
        professionnelDTO1.setId(null);
        assertThat(professionnelDTO1).isNotEqualTo(professionnelDTO2);
    }
}
