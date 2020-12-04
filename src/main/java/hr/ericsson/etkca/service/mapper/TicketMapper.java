package hr.ericsson.etkca.service.mapper;


import hr.ericsson.etkca.domain.*;
import hr.ericsson.etkca.service.dto.TicketDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Ticket} and its DTO {@link TicketDTO}.
 */
@Mapper(componentModel = "spring", uses = {TicketStatusMapper.class, CategoryMapper.class, PriorityMapper.class, HDUserMapper.class, ProjectMapper.class})
public interface TicketMapper extends EntityMapper<TicketDTO, Ticket> {

    @Mapping(source = "status.id", target = "statusId")
    @Mapping(source = "status.name", target = "statusName")
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(source = "priority.id", target = "priorityId")
    @Mapping(source = "priority.name", target = "priorityName")
    @Mapping(source = "project.id", target = "projectId")
    @Mapping(source = "project.name", target = "projectName")
    TicketDTO toDto(Ticket ticket);

    @Mapping(target = "attachments", ignore = true)
    @Mapping(target = "removeAttachments", ignore = true)
    @Mapping(source = "statusId", target = "status")
    @Mapping(source = "categoryId", target = "category")
    @Mapping(source = "priorityId", target = "priority")
    @Mapping(target = "removeUsers", ignore = true)
    @Mapping(source = "projectId", target = "project")
    Ticket toEntity(TicketDTO ticketDTO);

    default Ticket fromId(Long id) {
        if (id == null) {
            return null;
        }
        Ticket ticket = new Ticket();
        ticket.setId(id);
        return ticket;
    }
}
