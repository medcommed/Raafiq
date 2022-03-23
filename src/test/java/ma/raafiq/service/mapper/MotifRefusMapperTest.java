package ma.raafiq.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MotifRefusMapperTest {

    private MotifRefusMapper motifRefusMapper;

    @BeforeEach
    public void setUp() {
        motifRefusMapper = new MotifRefusMapperImpl();
    }
}
