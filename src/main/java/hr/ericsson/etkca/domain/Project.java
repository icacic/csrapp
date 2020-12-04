package hr.ericsson.etkca.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import hr.ericsson.etkca.domain.enumeration.ProjectStatus;

/**
 * A Project.
 */
@Entity
@Table(name = "project")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "project")
public class Project implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ProjectStatus status;

    @OneToMany(mappedBy = "project")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Ticket> tickets = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(name = "project_users",
               joinColumns = @JoinColumn(name = "project_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "users_id", referencedColumnName = "id"))
    private Set<HDUser> users = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = "projects", allowSetters = true)
    private Organization organization;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Project name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public Project status(ProjectStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(ProjectStatus status) {
        this.status = status;
    }

    public Set<Ticket> getTickets() {
        return tickets;
    }

    public Project tickets(Set<Ticket> tickets) {
        this.tickets = tickets;
        return this;
    }

    public Project addTickets(Ticket ticket) {
        this.tickets.add(ticket);
        ticket.setProject(this);
        return this;
    }

    public Project removeTickets(Ticket ticket) {
        this.tickets.remove(ticket);
        ticket.setProject(null);
        return this;
    }

    public void setTickets(Set<Ticket> tickets) {
        this.tickets = tickets;
    }

    public Set<HDUser> getUsers() {
        return users;
    }

    public Project users(Set<HDUser> hDUsers) {
        this.users = hDUsers;
        return this;
    }

    public Project addUsers(HDUser hDUser) {
        this.users.add(hDUser);
        hDUser.getProjects().add(this);
        return this;
    }

    public Project removeUsers(HDUser hDUser) {
        this.users.remove(hDUser);
        hDUser.getProjects().remove(this);
        return this;
    }

    public void setUsers(Set<HDUser> hDUsers) {
        this.users = hDUsers;
    }

    public Organization getOrganization() {
        return organization;
    }

    public Project organization(Organization organization) {
        this.organization = organization;
        return this;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Project)) {
            return false;
        }
        return id != null && id.equals(((Project) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Project{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
