package hr.ericsson.etkca.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import hr.ericsson.etkca.domain.enumeration.ProjectStatus;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link hr.ericsson.etkca.domain.Project} entity. This class is used
 * in {@link hr.ericsson.etkca.web.rest.ProjectResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /projects?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ProjectCriteria implements Serializable, Criteria {
    /**
     * Class for filtering ProjectStatus
     */
    public static class ProjectStatusFilter extends Filter<ProjectStatus> {

        public ProjectStatusFilter() {
        }

        public ProjectStatusFilter(ProjectStatusFilter filter) {
            super(filter);
        }

        @Override
        public ProjectStatusFilter copy() {
            return new ProjectStatusFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private ProjectStatusFilter status;

    private LongFilter ticketsId;

    private LongFilter usersId;

    private LongFilter organizationId;

    public ProjectCriteria() {
    }

    public ProjectCriteria(ProjectCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.ticketsId = other.ticketsId == null ? null : other.ticketsId.copy();
        this.usersId = other.usersId == null ? null : other.usersId.copy();
        this.organizationId = other.organizationId == null ? null : other.organizationId.copy();
    }

    @Override
    public ProjectCriteria copy() {
        return new ProjectCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public ProjectStatusFilter getStatus() {
        return status;
    }

    public void setStatus(ProjectStatusFilter status) {
        this.status = status;
    }

    public LongFilter getTicketsId() {
        return ticketsId;
    }

    public void setTicketsId(LongFilter ticketsId) {
        this.ticketsId = ticketsId;
    }

    public LongFilter getUsersId() {
        return usersId;
    }

    public void setUsersId(LongFilter usersId) {
        this.usersId = usersId;
    }

    public LongFilter getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(LongFilter organizationId) {
        this.organizationId = organizationId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProjectCriteria that = (ProjectCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(status, that.status) &&
            Objects.equals(ticketsId, that.ticketsId) &&
            Objects.equals(usersId, that.usersId) &&
            Objects.equals(organizationId, that.organizationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        status,
        ticketsId,
        usersId,
        organizationId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProjectCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
                (ticketsId != null ? "ticketsId=" + ticketsId + ", " : "") +
                (usersId != null ? "usersId=" + usersId + ", " : "") +
                (organizationId != null ? "organizationId=" + organizationId + ", " : "") +
            "}";
    }

}
