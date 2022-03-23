package ma.raafiq.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ma.raafiq.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProfessionnelTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Professionnel.class);
        Professionnel professionnel1 = new Professionnel();
        professionnel1.setId(1L);
        Professionnel professionnel2 = new Professionnel();
        professionnel2.setId(professionnel1.getId());
        assertThat(professionnel1).isEqualTo(professionnel2);
        professionnel2.setId(2L);
        assertThat(professionnel1).isNotEqualTo(professionnel2);
        professionnel1.setId(null);
        assertThat(professionnel1).isNotEqualTo(professionnel2);
    }
}
