package hr.ericsson.etkca.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link hr.ericsson.etkca.domain.Ticket} entity. This class is used
 * in {@link hr.ericsson.etkca.web.rest.TicketResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /tickets?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TicketCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter rbr;

    private LongFilter attachmentsId;

    private LongFilter statusId;

    private LongFilter categoryId;

    private LongFilter priorityId;

    private LongFilter usersId;

    private LongFilter projectId;

    public TicketCriteria() {
    }

    public TicketCriteria(TicketCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.rbr = other.rbr == null ? null : other.rbr.copy();
        this.attachmentsId = other.attachmentsId == null ? null : other.attachmentsId.copy();
        this.statusId = other.statusId == null ? null : other.statusId.copy();
        this.categoryId = other.categoryId == null ? null : other.categoryId.copy();
        this.priorityId = other.priorityId == null ? null : other.priorityId.copy();
        this.usersId = other.usersId == null ? null : other.usersId.copy();
        this.projectId = other.projectId == null ? null : other.projectId.copy();
    }

    @Override
    public TicketCriteria copy() {
        return new TicketCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getRbr() {
        return rbr;
    }

    public void setRbr(StringFilter rbr) {
        this.rbr = rbr;
    }

    public LongFilter getAttachmentsId() {
        return attachmentsId;
    }

    public void setAttachmentsId(LongFilter attachmentsId) {
        this.attachmentsId = attachmentsId;
    }

    public LongFilter getStatusId() {
        return statusId;
    }

    public void setStatusId(LongFilter statusId) {
        this.statusId = statusId;
    }

    public LongFilter getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(LongFilter categoryId) {
        this.categoryId = categoryId;
    }

    public LongFilter getPriorityId() {
        return priorityId;
    }

    public void setPriorityId(LongFilter priorityId) {
        this.priorityId = priorityId;
    }

    public LongFilter getUsersId() {
        return usersId;
    }

    public void setUsersId(LongFilter usersId) {
        this.usersId = usersId;
    }

    public LongFilter getProjectId() {
        return projectId;
    }

    public void setProjectId(LongFilter projectId) {
        this.projectId = projectId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TicketCriteria that = (TicketCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(rbr, that.rbr) &&
            Objects.equals(attachmentsId, that.attachmentsId) &&
            Objects.equals(statusId, that.statusId) &&
            Objects.equals(categoryId, that.categoryId) &&
            Objects.equals(priorityId, that.priorityId) &&
            Objects.equals(usersId, that.usersId) &&
            Objects.equals(projectId, that.projectId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        rbr,
        attachmentsId,
        statusId,
        categoryId,
        priorityId,
        usersId,
        projectId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TicketCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (rbr != null ? "rbr=" + rbr + ", " : "") +
                (attachmentsId != null ? "attachmentsId=" + attachmentsId + ", " : "") +
                (statusId != null ? "statusId=" + statusId + ", " : "") +
                (categoryId != null ? "categoryId=" + categoryId + ", " : "") +
                (priorityId != null ? "priorityId=" + priorityId + ", " : "") +
                (usersId != null ? "usersId=" + usersId + ", " : "") +
                (projectId != null ? "projectId=" + projectId + ", " : "") +
            "}";
    }

}
