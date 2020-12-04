package hr.ericsson.etkca.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import hr.ericsson.etkca.domain.enumeration.OrganizationType;

/**
 * A Organization.
 */
@Entity
@Table(name = "organization")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "organization")
public class Organization implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private OrganizationType type;

    @OneToMany(mappedBy = "organization")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Project> projects = new HashSet<>();

    @OneToMany(mappedBy = "organization")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<HDUser> users = new HashSet<>();

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

    public Organization name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public Organization address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public OrganizationType getType() {
        return type;
    }

    public Organization type(OrganizationType type) {
        this.type = type;
        return this;
    }

    public void setType(OrganizationType type) {
        this.type = type;
    }

    public Set<Project> getProjects() {
        return projects;
    }

    public Organization projects(Set<Project> projects) {
        this.projects = projects;
        return this;
    }

    public Organization addProjects(Project project) {
        this.projects.add(project);
        project.setOrganization(this);
        return this;
    }

    public Organization removeProjects(Project project) {
        this.projects.remove(project);
        project.setOrganization(null);
        return this;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }

    public Set<HDUser> getUsers() {
        return users;
    }

    public Organization users(Set<HDUser> hDUsers) {
        this.users = hDUsers;
        return this;
    }

    public Organization addUsers(HDUser hDUser) {
        this.users.add(hDUser);
        hDUser.setOrganization(this);
        return this;
    }

    public Organization removeUsers(HDUser hDUser) {
        this.users.remove(hDUser);
        hDUser.setOrganization(null);
        return this;
    }

    public void setUsers(Set<HDUser> hDUsers) {
        this.users = hDUsers;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Organization)) {
            return false;
        }
        return id != null && id.equals(((Organization) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Organization{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", address='" + getAddress() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
