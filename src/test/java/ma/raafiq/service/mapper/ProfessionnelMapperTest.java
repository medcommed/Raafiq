package ma.raafiq.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProfessionnelMapperTest {

    private ProfessionnelMapper professionnelMapper;

    @BeforeEach
    public void setUp() {
        professionnelMapper = new ProfessionnelMapperImpl();
    }
}
