package ma.raafiq.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ma.raafiq.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MotifRefusDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MotifRefusDTO.class);
        MotifRefusDTO motifRefusDTO1 = new MotifRefusDTO();
        motifRefusDTO1.setId(1L);
        MotifRefusDTO motifRefusDTO2 = new MotifRefusDTO();
        assertThat(motifRefusDTO1).isNotEqualTo(motifRefusDTO2);
        motifRefusDTO2.setId(motifRefusDTO1.getId());
        assertThat(motifRefusDTO1).isEqualTo(motifRefusDTO2);
        motifRefusDTO2.setId(2L);
        assertThat(motifRefusDTO1).isNotEqualTo(motifRefusDTO2);
        motifRefusDTO1.setId(null);
        assertThat(motifRefusDTO1).isNotEqualTo(motifRefusDTO2);
    }
}
