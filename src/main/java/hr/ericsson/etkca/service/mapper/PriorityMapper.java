package hr.ericsson.etkca.service.mapper;


import hr.ericsson.etkca.domain.*;
import hr.ericsson.etkca.service.dto.PriorityDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Priority} and its DTO {@link PriorityDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PriorityMapper extends EntityMapper<PriorityDTO, Priority> {



    default Priority fromId(Long id) {
        if (id == null) {
            return null;
        }
        Priority priority = new Priority();
        priority.setId(id);
        return priority;
    }
}
