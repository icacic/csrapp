package hr.ericsson.etkca.service.mapper;


import hr.ericsson.etkca.domain.*;
import hr.ericsson.etkca.service.dto.HDUserDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link HDUser} and its DTO {@link HDUserDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, OrganizationMapper.class})
public interface HDUserMapper extends EntityMapper<HDUserDTO, HDUser> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.email", target = "userEmail")
    @Mapping(source = "organization.id", target = "organizationId")
    @Mapping(source = "organization.name", target = "organizationName")
    HDUserDTO toDto(HDUser hDUser);

    @Mapping(source = "userId", target = "user")
    @Mapping(source = "organizationId", target = "organization")
    @Mapping(target = "projects", ignore = true)
    @Mapping(target = "removeProjects", ignore = true)
    @Mapping(target = "tickets", ignore = true)
    @Mapping(target = "removeTickets", ignore = true)
    HDUser toEntity(HDUserDTO hDUserDTO);

    default HDUser fromId(Long id) {
        if (id == null) {
            return null;
        }
        HDUser hDUser = new HDUser();
        hDUser.setId(id);
        return hDUser;
    }
}
