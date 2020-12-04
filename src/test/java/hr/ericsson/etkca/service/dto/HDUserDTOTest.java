package hr.ericsson.etkca.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import hr.ericsson.etkca.web.rest.TestUtil;

public class HDUserDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HDUserDTO.class);
        HDUserDTO hDUserDTO1 = new HDUserDTO();
        hDUserDTO1.setId(1L);
        HDUserDTO hDUserDTO2 = new HDUserDTO();
        assertThat(hDUserDTO1).isNotEqualTo(hDUserDTO2);
        hDUserDTO2.setId(hDUserDTO1.getId());
        assertThat(hDUserDTO1).isEqualTo(hDUserDTO2);
        hDUserDTO2.setId(2L);
        assertThat(hDUserDTO1).isNotEqualTo(hDUserDTO2);
        hDUserDTO1.setId(null);
        assertThat(hDUserDTO1).isNotEqualTo(hDUserDTO2);
    }
}
