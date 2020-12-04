package hr.ericsson.etkca.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class TicketMapperTest {

    private TicketMapper ticketMapper;

    @BeforeEach
    public void setUp() {
        ticketMapper = new TicketMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(ticketMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(ticketMapper.fromId(null)).isNull();
    }
}
