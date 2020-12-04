package hr.ericsson.etkca.service.mapper;


import hr.ericsson.etkca.domain.*;
import hr.ericsson.etkca.service.dto.TicketStatusDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link TicketStatus} and its DTO {@link TicketStatusDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TicketStatusMapper extends EntityMapper<TicketStatusDTO, TicketStatus> {



    default TicketStatus fromId(Long id) {
        if (id == null) {
            return null;
        }
        TicketStatus ticketStatus = new TicketStatus();
        ticketStatus.setId(id);
        return ticketStatus;
    }
}
