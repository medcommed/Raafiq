package ma.raafiq.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AppRoleMapperTest {

    private AppRoleMapper appRoleMapper;

    @BeforeEach
    public void setUp() {
        appRoleMapper = new AppRoleMapperImpl();
    }
}
