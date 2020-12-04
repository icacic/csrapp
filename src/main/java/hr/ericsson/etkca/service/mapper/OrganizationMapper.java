package hr.ericsson.etkca.service.mapper;


import hr.ericsson.etkca.domain.*;
import hr.ericsson.etkca.service.dto.OrganizationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Organization} and its DTO {@link OrganizationDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface OrganizationMapper extends EntityMapper<OrganizationDTO, Organization> {


    @Mapping(target = "projects", ignore = true)
    @Mapping(target = "removeProjects", ignore = true)
    @Mapping(target = "users", ignore = true)
    @Mapping(target = "removeUsers", ignore = true)
    Organization toEntity(OrganizationDTO organizationDTO);

    default Organization fromId(Long id) {
        if (id == null) {
            return null;
        }
        Organization organization = new Organization();
        organization.setId(id);
        return organization;
    }
}
