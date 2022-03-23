package ma.raafiq.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EnfantMapperTest {

    private EnfantMapper enfantMapper;

    @BeforeEach
    public void setUp() {
        enfantMapper = new EnfantMapperImpl();
    }
}
