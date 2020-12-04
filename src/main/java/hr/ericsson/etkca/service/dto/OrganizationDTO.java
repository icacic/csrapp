package hr.ericsson.etkca.service.dto;

import java.io.Serializable;
import hr.ericsson.etkca.domain.enumeration.OrganizationType;

/**
 * A DTO for the {@link hr.ericsson.etkca.domain.Organization} entity.
 */
public class OrganizationDTO implements Serializable {
    
    private Long id;

    private String name;

    private String address;

    private OrganizationType type;

    
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public OrganizationType getType() {
        return type;
    }

    public void setType(OrganizationType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrganizationDTO)) {
            return false;
        }

        return id != null && id.equals(((OrganizationDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrganizationDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", address='" + getAddress() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
