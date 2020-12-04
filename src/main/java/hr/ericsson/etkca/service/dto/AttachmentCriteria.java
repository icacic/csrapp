package hr.ericsson.etkca.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import hr.ericsson.etkca.domain.enumeration.Extension;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link hr.ericsson.etkca.domain.Attachment} entity. This class is used
 * in {@link hr.ericsson.etkca.web.rest.AttachmentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /attachments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AttachmentCriteria implements Serializable, Criteria {
    /**
     * Class for filtering Extension
     */
    public static class ExtensionFilter extends Filter<Extension> {

        public ExtensionFilter() {
        }

        public ExtensionFilter(ExtensionFilter filter) {
            super(filter);
        }

        @Override
        public ExtensionFilter copy() {
            return new ExtensionFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private ExtensionFilter extension;

    private LongFilter ticketId;

    public AttachmentCriteria() {
    }

    public AttachmentCriteria(AttachmentCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.extension = other.extension == null ? null : other.extension.copy();
        this.ticketId = other.ticketId == null ? null : other.ticketId.copy();
    }

    @Override
    public AttachmentCriteria copy() {
        return new AttachmentCriteria(this);
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

    public ExtensionFilter getExtension() {
        return extension;
    }

    public void setExtension(ExtensionFilter extension) {
        this.extension = extension;
    }

    public LongFilter getTicketId() {
        return ticketId;
    }

    public void setTicketId(LongFilter ticketId) {
        this.ticketId = ticketId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AttachmentCriteria that = (AttachmentCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(extension, that.extension) &&
            Objects.equals(ticketId, that.ticketId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        extension,
        ticketId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AttachmentCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (extension != null ? "extension=" + extension + ", " : "") +
                (ticketId != null ? "ticketId=" + ticketId + ", " : "") +
            "}";
    }

}
