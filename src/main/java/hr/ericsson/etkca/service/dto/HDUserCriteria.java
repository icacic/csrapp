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
 * Criteria class for the {@link hr.ericsson.etkca.domain.HDUser} entity. This class is used
 * in {@link hr.ericsson.etkca.web.rest.HDUserResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /hd-users?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class HDUserCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter firstName;

    private StringFilter lastName;

    private StringFilter email;

    private StringFilter address;

    private LongFilter userId;

    private LongFilter organizationId;

    private LongFilter projectsId;

    private LongFilter ticketsId;

    public HDUserCriteria() {
    }

    public HDUserCriteria(HDUserCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.firstName = other.firstName == null ? null : other.firstName.copy();
        this.lastName = other.lastName == null ? null : other.lastName.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.address = other.address == null ? null : other.address.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.organizationId = other.organizationId == null ? null : other.organizationId.copy();
        this.projectsId = other.projectsId == null ? null : other.projectsId.copy();
        this.ticketsId = other.ticketsId == null ? null : other.ticketsId.copy();
    }

    @Override
    public HDUserCriteria copy() {
        return new HDUserCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getFirstName() {
        return firstName;
    }

    public void setFirstName(StringFilter firstName) {
        this.firstName = firstName;
    }

    public StringFilter getLastName() {
        return lastName;
    }

    public void setLastName(StringFilter lastName) {
        this.lastName = lastName;
    }

    public StringFilter getEmail() {
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getAddress() {
        return address;
    }

    public void setAddress(StringFilter address) {
        this.address = address;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(LongFilter organizationId) {
        this.organizationId = organizationId;
    }

    public LongFilter getProjectsId() {
        return projectsId;
    }

    public void setProjectsId(LongFilter projectsId) {
        this.projectsId = projectsId;
    }

    public LongFilter getTicketsId() {
        return ticketsId;
    }

    public void setTicketsId(LongFilter ticketsId) {
        this.ticketsId = ticketsId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final HDUserCriteria that = (HDUserCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(firstName, that.firstName) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(email, that.email) &&
            Objects.equals(address, that.address) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(organizationId, that.organizationId) &&
            Objects.equals(projectsId, that.projectsId) &&
            Objects.equals(ticketsId, that.ticketsId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        firstName,
        lastName,
        email,
        address,
        userId,
        organizationId,
        projectsId,
        ticketsId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HDUserCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (firstName != null ? "firstName=" + firstName + ", " : "") +
                (lastName != null ? "lastName=" + lastName + ", " : "") +
                (email != null ? "email=" + email + ", " : "") +
                (address != null ? "address=" + address + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
                (organizationId != null ? "organizationId=" + organizationId + ", " : "") +
                (projectsId != null ? "projectsId=" + projectsId + ", " : "") +
                (ticketsId != null ? "ticketsId=" + ticketsId + ", " : "") +
            "}";
    }

}
