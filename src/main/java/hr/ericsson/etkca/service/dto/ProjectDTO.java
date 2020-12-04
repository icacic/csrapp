package hr.ericsson.etkca.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import hr.ericsson.etkca.domain.enumeration.ProjectStatus;

/**
 * A DTO for the {@link hr.ericsson.etkca.domain.Project} entity.
 */
public class ProjectDTO implements Serializable {
    
    private Long id;

    private String name;

    private ProjectStatus status;

    private Set<HDUserDTO> users = new HashSet<>();

    private Long organizationId;

    private String organizationName;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public void setStatus(ProjectStatus status) {
        this.status = status;
    }

    public Set<HDUserDTO> getUsers() {
        return users;
    }

    public void setUsers(Set<HDUserDTO> hDUsers) {
        this.users = hDUsers;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProjectDTO)) {
            return false;
        }

        return id != null && id.equals(((ProjectDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProjectDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", status='" + getStatus() + "'" +
            ", users='" + getUsers() + "'" +
            ", organizationId=" + getOrganizationId() +
            ", organizationName='" + getOrganizationName() + "'" +
            "}";
    }
}
