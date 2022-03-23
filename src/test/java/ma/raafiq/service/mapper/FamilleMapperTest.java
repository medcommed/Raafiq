package ma.raafiq.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FamilleMapperTest {

    private FamilleMapper familleMapper;

    @BeforeEach
    public void setUp() {
        familleMapper = new FamilleMapperImpl();
    }
}
