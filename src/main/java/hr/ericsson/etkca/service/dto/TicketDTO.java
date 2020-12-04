package hr.ericsson.etkca.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Lob;

/**
 * A DTO for the {@link hr.ericsson.etkca.domain.Ticket} entity.
 */
public class TicketDTO implements Serializable {
    
    private Long id;

    private String rbr;

    @Lob
    private String description;


    private Long statusId;

    private String statusName;

    private Long categoryId;

    private String categoryName;

    private Long priorityId;

    private String priorityName;
    private Set<HDUserDTO> users = new HashSet<>();

    private Long projectId;

    private String projectName;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRbr() {
        return rbr;
    }

    public void setRbr(String rbr) {
        this.rbr = rbr;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getStatusId() {
        return statusId;
    }

    public void setStatusId(Long ticketStatusId) {
        this.statusId = ticketStatusId;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String ticketStatusName) {
        this.statusName = ticketStatusName;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Long getPriorityId() {
        return priorityId;
    }

    public void setPriorityId(Long priorityId) {
        this.priorityId = priorityId;
    }

    public String getPriorityName() {
        return priorityName;
    }

    public void setPriorityName(String priorityName) {
        this.priorityName = priorityName;
    }

    public Set<HDUserDTO> getUsers() {
        return users;
    }

    public void setUsers(Set<HDUserDTO> hDUsers) {
        this.users = hDUsers;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TicketDTO)) {
            return false;
        }

        return id != null && id.equals(((TicketDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TicketDTO{" +
            "id=" + getId() +
            ", rbr='" + getRbr() + "'" +
            ", description='" + getDescription() + "'" +
            ", statusId=" + getStatusId() +
            ", statusName='" + getStatusName() + "'" +
            ", categoryId=" + getCategoryId() +
            ", categoryName='" + getCategoryName() + "'" +
            ", priorityId=" + getPriorityId() +
            ", priorityName='" + getPriorityName() + "'" +
            ", users='" + getUsers() + "'" +
            ", projectId=" + getProjectId() +
            ", projectName='" + getProjectName() + "'" +
            "}";
    }
}
