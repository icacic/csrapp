package hr.ericsson.etkca.service.mapper;


import hr.ericsson.etkca.domain.*;
import hr.ericsson.etkca.service.dto.AttachmentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Attachment} and its DTO {@link AttachmentDTO}.
 */
@Mapper(componentModel = "spring", uses = {TicketMapper.class})
public interface AttachmentMapper extends EntityMapper<AttachmentDTO, Attachment> {

    @Mapping(source = "ticket.id", target = "ticketId")
    @Mapping(source = "ticket.description", target = "ticketDescription")
    AttachmentDTO toDto(Attachment attachment);

    @Mapping(source = "ticketId", target = "ticket")
    Attachment toEntity(AttachmentDTO attachmentDTO);

    default Attachment fromId(Long id) {
        if (id == null) {
            return null;
        }
        Attachment attachment = new Attachment();
        attachment.setId(id);
        return attachment;
    }

    default String map(byte[] value) {
        return new String(value);
    }
}
