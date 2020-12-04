package hr.ericsson.etkca.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import hr.ericsson.etkca.domain.enumeration.OrganizationType;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link hr.ericsson.etkca.domain.Organization} entity. This class is used
 * in {@link hr.ericsson.etkca.web.rest.OrganizationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /organizations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class OrganizationCriteria implements Serializable, Criteria {
    /**
     * Class for filtering OrganizationType
     */
    public static class OrganizationTypeFilter extends Filter<OrganizationType> {

        public OrganizationTypeFilter() {
        }

        public OrganizationTypeFilter(OrganizationTypeFilter filter) {
            super(filter);
        }

        @Override
        public OrganizationTypeFilter copy() {
            return new OrganizationTypeFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter address;

    private OrganizationTypeFilter type;

    private LongFilter projectsId;

    private LongFilter usersId;

    public OrganizationCriteria() {
    }

    public OrganizationCriteria(OrganizationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.address = other.address == null ? null : other.address.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.projectsId = other.projectsId == null ? null : other.projectsId.copy();
        this.usersId = other.usersId == null ? null : other.usersId.copy();
    }

    @Override
    public OrganizationCriteria copy() {
        return new OrganizationCriteria(this);
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

    public StringFilter getAddress() {
        return address;
    }

    public void setAddress(StringFilter address) {
        this.address = address;
    }

    public OrganizationTypeFilter getType() {
        return type;
    }

    public void setType(OrganizationTypeFilter type) {
        this.type = type;
    }

    public LongFilter getProjectsId() {
        return projectsId;
    }

    public void setProjectsId(LongFilter projectsId) {
        this.projectsId = projectsId;
    }

    public LongFilter getUsersId() {
        return usersId;
    }

    public void setUsersId(LongFilter usersId) {
        this.usersId = usersId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final OrganizationCriteria that = (OrganizationCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(address, that.address) &&
            Objects.equals(type, that.type) &&
            Objects.equals(projectsId, that.projectsId) &&
            Objects.equals(usersId, that.usersId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        address,
        type,
        projectsId,
        usersId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrganizationCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (address != null ? "address=" + address + ", " : "") +
                (type != null ? "type=" + type + ", " : "") +
                (projectsId != null ? "projectsId=" + projectsId + ", " : "") +
                (usersId != null ? "usersId=" + usersId + ", " : "") +
            "}";
    }

}
