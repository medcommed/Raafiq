package ma.raafiq.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ma.raafiq.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AppRoleDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AppRoleDTO.class);
        AppRoleDTO appRoleDTO1 = new AppRoleDTO();
        appRoleDTO1.setId(1L);
        AppRoleDTO appRoleDTO2 = new AppRoleDTO();
        assertThat(appRoleDTO1).isNotEqualTo(appRoleDTO2);
        appRoleDTO2.setId(appRoleDTO1.getId());
        assertThat(appRoleDTO1).isEqualTo(appRoleDTO2);
        appRoleDTO2.setId(2L);
        assertThat(appRoleDTO1).isNotEqualTo(appRoleDTO2);
        appRoleDTO1.setId(null);
        assertThat(appRoleDTO1).isNotEqualTo(appRoleDTO2);
    }
}
