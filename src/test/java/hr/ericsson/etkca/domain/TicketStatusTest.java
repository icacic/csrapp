package hr.ericsson.etkca.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import hr.ericsson.etkca.web.rest.TestUtil;

public class TicketStatusTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TicketStatus.class);
        TicketStatus ticketStatus1 = new TicketStatus();
        ticketStatus1.setId(1L);
        TicketStatus ticketStatus2 = new TicketStatus();
        ticketStatus2.setId(ticketStatus1.getId());
        assertThat(ticketStatus1).isEqualTo(ticketStatus2);
        ticketStatus2.setId(2L);
        assertThat(ticketStatus1).isNotEqualTo(ticketStatus2);
        ticketStatus1.setId(null);
        assertThat(ticketStatus1).isNotEqualTo(ticketStatus2);
    }
}
