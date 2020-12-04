package hr.ericsson.etkca.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class TicketStatusMapperTest {

    private TicketStatusMapper ticketStatusMapper;

    @BeforeEach
    public void setUp() {
        ticketStatusMapper = new TicketStatusMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(ticketStatusMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(ticketStatusMapper.fromId(null)).isNull();
    }
}
