package hr.ericsson.etkca.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import hr.ericsson.etkca.web.rest.TestUtil;

public class TicketStatusDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TicketStatusDTO.class);
        TicketStatusDTO ticketStatusDTO1 = new TicketStatusDTO();
        ticketStatusDTO1.setId(1L);
        TicketStatusDTO ticketStatusDTO2 = new TicketStatusDTO();
        assertThat(ticketStatusDTO1).isNotEqualTo(ticketStatusDTO2);
        ticketStatusDTO2.setId(ticketStatusDTO1.getId());
        assertThat(ticketStatusDTO1).isEqualTo(ticketStatusDTO2);
        ticketStatusDTO2.setId(2L);
        assertThat(ticketStatusDTO1).isNotEqualTo(ticketStatusDTO2);
        ticketStatusDTO1.setId(null);
        assertThat(ticketStatusDTO1).isNotEqualTo(ticketStatusDTO2);
    }
}
