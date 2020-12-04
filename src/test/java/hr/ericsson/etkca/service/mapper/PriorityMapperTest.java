package hr.ericsson.etkca.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class PriorityMapperTest {

    private PriorityMapper priorityMapper;

    @BeforeEach
    public void setUp() {
        priorityMapper = new PriorityMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(priorityMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(priorityMapper.fromId(null)).isNull();
    }
}
