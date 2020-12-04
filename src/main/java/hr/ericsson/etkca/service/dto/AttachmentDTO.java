package hr.ericsson.etkca.service.dto;

import java.io.Serializable;
import javax.persistence.Lob;
import hr.ericsson.etkca.domain.enumeration.Extension;

/**
 * A DTO for the {@link hr.ericsson.etkca.domain.Attachment} entity.
 */
public class AttachmentDTO implements Serializable {
    
    private Long id;

    private String name;

    private Extension extension;

    @Lob
    private byte[] file;

    private String fileContentType;

    private Long ticketId;

    private String ticketDescription;
    
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

    public Extension getExtension() {
        return extension;
    }

    public void setExtension(Extension extension) {
        this.extension = extension;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public String getFileContentType() {
        return fileContentType;
    }

    public void setFileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
    }

    public Long getTicketId() {
        return ticketId;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

    public String getTicketDescription() {
        return ticketDescription;
    }

    public void setTicketDescription(String ticketDescription) {
        this.ticketDescription = ticketDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AttachmentDTO)) {
            return false;
        }

        return id != null && id.equals(((AttachmentDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AttachmentDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", extension='" + getExtension() + "'" +
            ", file='" + getFile() + "'" +
            ", ticketId=" + getTicketId() +
            ", ticketDescription='" + getTicketDescription() + "'" +
            "}";
    }
}
