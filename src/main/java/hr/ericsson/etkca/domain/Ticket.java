package hr.ericsson.etkca.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Ticket.
 */
@Entity
@Table(name = "ticket")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "ticket")
public class Ticket implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "rbr")
    private String rbr;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "ticket")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Attachment> attachments = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = "tickets", allowSetters = true)
    private TicketStatus status;

    @ManyToOne
    @JsonIgnoreProperties(value = "tickets", allowSetters = true)
    private Category category;

    @ManyToOne
    @JsonIgnoreProperties(value = "tickets", allowSetters = true)
    private Priority priority;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(name = "ticket_users",
               joinColumns = @JoinColumn(name = "ticket_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "users_id", referencedColumnName = "id"))
    private Set<HDUser> users = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = "tickets", allowSetters = true)
    private Project project;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRbr() {
        return rbr;
    }

    public Ticket rbr(String rbr) {
        this.rbr = rbr;
        return this;
    }

    public void setRbr(String rbr) {
        this.rbr = rbr;
    }

    public String getDescription() {
        return description;
    }

    public Ticket description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Attachment> getAttachments() {
        return attachments;
    }

    public Ticket attachments(Set<Attachment> attachments) {
        this.attachments = attachments;
        return this;
    }

    public Ticket addAttachments(Attachment attachment) {
        this.attachments.add(attachment);
        attachment.setTicket(this);
        return this;
    }

    public Ticket removeAttachments(Attachment attachment) {
        this.attachments.remove(attachment);
        attachment.setTicket(null);
        return this;
    }

    public void setAttachments(Set<Attachment> attachments) {
        this.attachments = attachments;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public Ticket status(TicketStatus ticketStatus) {
        this.status = ticketStatus;
        return this;
    }

    public void setStatus(TicketStatus ticketStatus) {
        this.status = ticketStatus;
    }

    public Category getCategory() {
        return category;
    }

    public Ticket category(Category category) {
        this.category = category;
        return this;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Priority getPriority() {
        return priority;
    }

    public Ticket priority(Priority priority) {
        this.priority = priority;
        return this;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Set<HDUser> getUsers() {
        return users;
    }

    public Ticket users(Set<HDUser> hDUsers) {
        this.users = hDUsers;
        return this;
    }

    public Ticket addUsers(HDUser hDUser) {
        this.users.add(hDUser);
        hDUser.getTickets().add(this);
        return this;
    }

    public Ticket removeUsers(HDUser hDUser) {
        this.users.remove(hDUser);
        hDUser.getTickets().remove(this);
        return this;
    }

    public void setUsers(Set<HDUser> hDUsers) {
        this.users = hDUsers;
    }

    public Project getProject() {
        return project;
    }

    public Ticket project(Project project) {
        this.project = project;
        return this;
    }

    public void setProject(Project project) {
        this.project = project;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ticket)) {
            return false;
        }
        return id != null && id.equals(((Ticket) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Ticket{" +
            "id=" + getId() +
            ", rbr='" + getRbr() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
