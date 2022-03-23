package ma.raafiq.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ma.raafiq.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MotifRefusTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MotifRefus.class);
        MotifRefus motifRefus1 = new MotifRefus();
        motifRefus1.setId(1L);
        MotifRefus motifRefus2 = new MotifRefus();
        motifRefus2.setId(motifRefus1.getId());
        assertThat(motifRefus1).isEqualTo(motifRefus2);
        motifRefus2.setId(2L);
        assertThat(motifRefus1).isNotEqualTo(motifRefus2);
        motifRefus1.setId(null);
        assertThat(motifRefus1).isNotEqualTo(motifRefus2);
    }
}
