package hr.ericsson.etkca.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import hr.ericsson.etkca.web.rest.TestUtil;

public class PriorityDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PriorityDTO.class);
        PriorityDTO priorityDTO1 = new PriorityDTO();
        priorityDTO1.setId(1L);
        PriorityDTO priorityDTO2 = new PriorityDTO();
        assertThat(priorityDTO1).isNotEqualTo(priorityDTO2);
        priorityDTO2.setId(priorityDTO1.getId());
        assertThat(priorityDTO1).isEqualTo(priorityDTO2);
        priorityDTO2.setId(2L);
        assertThat(priorityDTO1).isNotEqualTo(priorityDTO2);
        priorityDTO1.setId(null);
        assertThat(priorityDTO1).isNotEqualTo(priorityDTO2);
    }
}
