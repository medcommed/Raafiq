package ma.raafiq.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ma.raafiq.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AppRoleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AppRole.class);
        AppRole appRole1 = new AppRole();
        appRole1.setId(1L);
        AppRole appRole2 = new AppRole();
        appRole2.setId(appRole1.getId());
        assertThat(appRole1).isEqualTo(appRole2);
        appRole2.setId(2L);
        assertThat(appRole1).isNotEqualTo(appRole2);
        appRole1.setId(null);
        assertThat(appRole1).isNotEqualTo(appRole2);
    }
}
