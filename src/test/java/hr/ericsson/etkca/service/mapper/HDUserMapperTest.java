package hr.ericsson.etkca.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class HDUserMapperTest {

    private HDUserMapper hDUserMapper;

    @BeforeEach
    public void setUp() {
        hDUserMapper = new HDUserMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(hDUserMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(hDUserMapper.fromId(null)).isNull();
    }
}
