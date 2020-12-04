package hr.ericsson.etkca.web.rest;

import hr.ericsson.etkca.CsrappApp;
import hr.ericsson.etkca.domain.Organization;
import hr.ericsson.etkca.domain.Project;
import hr.ericsson.etkca.domain.HDUser;
import hr.ericsson.etkca.repository.OrganizationRepository;
import hr.ericsson.etkca.repository.search.OrganizationSearchRepository;
import hr.ericsson.etkca.service.OrganizationService;
import hr.ericsson.etkca.service.dto.OrganizationDTO;
import hr.ericsson.etkca.service.mapper.OrganizationMapper;
import hr.ericsson.etkca.service.dto.OrganizationCriteria;
import hr.ericsson.etkca.service.OrganizationQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import hr.ericsson.etkca.domain.enumeration.OrganizationType;
/**
 * Integration tests for the {@link OrganizationResource} REST controller.
 */
@SpringBootTest(classes = CsrappApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class OrganizationResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final OrganizationType DEFAULT_TYPE = OrganizationType.BUSINESS;
    private static final OrganizationType UPDATED_TYPE = OrganizationType.GOVERNMENT;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private OrganizationMapper organizationMapper;

    @Autowired
    private OrganizationService organizationService;

    /**
     * This repository is mocked in the hr.ericsson.etkca.repository.search test package.
     *
     * @see hr.ericsson.etkca.repository.search.OrganizationSearchRepositoryMockConfiguration
     */
    @Autowired
    private OrganizationSearchRepository mockOrganizationSearchRepository;

    @Autowired
    private OrganizationQueryService organizationQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrganizationMockMvc;

    private Organization organization;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Organization createEntity(EntityManager em) {
        Organization organization = new Organization()
            .name(DEFAULT_NAME)
            .address(DEFAULT_ADDRESS)
            .type(DEFAULT_TYPE);
        return organization;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Organization createUpdatedEntity(EntityManager em) {
        Organization organization = new Organization()
            .name(UPDATED_NAME)
            .address(UPDATED_ADDRESS)
            .type(UPDATED_TYPE);
        return organization;
    }

    @BeforeEach
    public void initTest() {
        organization = createEntity(em);
    }

    @Test
    @Transactional
    public void createOrganization() throws Exception {
        int databaseSizeBeforeCreate = organizationRepository.findAll().size();
        // Create the Organization
        OrganizationDTO organizationDTO = organizationMapper.toDto(organization);
        restOrganizationMockMvc.perform(post("/api/organizations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(organizationDTO)))
            .andExpect(status().isCreated());

        // Validate the Organization in the database
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeCreate + 1);
        Organization testOrganization = organizationList.get(organizationList.size() - 1);
        assertThat(testOrganization.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOrganization.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testOrganization.getType()).isEqualTo(DEFAULT_TYPE);

        // Validate the Organization in Elasticsearch
        verify(mockOrganizationSearchRepository, times(1)).save(testOrganization);
    }

    @Test
    @Transactional
    public void createOrganizationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = organizationRepository.findAll().size();

        // Create the Organization with an existing ID
        organization.setId(1L);
        OrganizationDTO organizationDTO = organizationMapper.toDto(organization);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrganizationMockMvc.perform(post("/api/organizations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(organizationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Organization in the database
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeCreate);

        // Validate the Organization in Elasticsearch
        verify(mockOrganizationSearchRepository, times(0)).save(organization);
    }


    @Test
    @Transactional
    public void getAllOrganizations() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList
        restOrganizationMockMvc.perform(get("/api/organizations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(organization.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }
    
    @Test
    @Transactional
    public void getOrganization() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get the organization
        restOrganizationMockMvc.perform(get("/api/organizations/{id}", organization.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(organization.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }


    @Test
    @Transactional
    public void getOrganizationsByIdFiltering() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        Long id = organization.getId();

        defaultOrganizationShouldBeFound("id.equals=" + id);
        defaultOrganizationShouldNotBeFound("id.notEquals=" + id);

        defaultOrganizationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultOrganizationShouldNotBeFound("id.greaterThan=" + id);

        defaultOrganizationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultOrganizationShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllOrganizationsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where name equals to DEFAULT_NAME
        defaultOrganizationShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the organizationList where name equals to UPDATED_NAME
        defaultOrganizationShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllOrganizationsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where name not equals to DEFAULT_NAME
        defaultOrganizationShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the organizationList where name not equals to UPDATED_NAME
        defaultOrganizationShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllOrganizationsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where name in DEFAULT_NAME or UPDATED_NAME
        defaultOrganizationShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the organizationList where name equals to UPDATED_NAME
        defaultOrganizationShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllOrganizationsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where name is not null
        defaultOrganizationShouldBeFound("name.specified=true");

        // Get all the organizationList where name is null
        defaultOrganizationShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllOrganizationsByNameContainsSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where name contains DEFAULT_NAME
        defaultOrganizationShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the organizationList where name contains UPDATED_NAME
        defaultOrganizationShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllOrganizationsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where name does not contain DEFAULT_NAME
        defaultOrganizationShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the organizationList where name does not contain UPDATED_NAME
        defaultOrganizationShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllOrganizationsByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where address equals to DEFAULT_ADDRESS
        defaultOrganizationShouldBeFound("address.equals=" + DEFAULT_ADDRESS);

        // Get all the organizationList where address equals to UPDATED_ADDRESS
        defaultOrganizationShouldNotBeFound("address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllOrganizationsByAddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where address not equals to DEFAULT_ADDRESS
        defaultOrganizationShouldNotBeFound("address.notEquals=" + DEFAULT_ADDRESS);

        // Get all the organizationList where address not equals to UPDATED_ADDRESS
        defaultOrganizationShouldBeFound("address.notEquals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllOrganizationsByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where address in DEFAULT_ADDRESS or UPDATED_ADDRESS
        defaultOrganizationShouldBeFound("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS);

        // Get all the organizationList where address equals to UPDATED_ADDRESS
        defaultOrganizationShouldNotBeFound("address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllOrganizationsByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where address is not null
        defaultOrganizationShouldBeFound("address.specified=true");

        // Get all the organizationList where address is null
        defaultOrganizationShouldNotBeFound("address.specified=false");
    }
                @Test
    @Transactional
    public void getAllOrganizationsByAddressContainsSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where address contains DEFAULT_ADDRESS
        defaultOrganizationShouldBeFound("address.contains=" + DEFAULT_ADDRESS);

        // Get all the organizationList where address contains UPDATED_ADDRESS
        defaultOrganizationShouldNotBeFound("address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllOrganizationsByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where address does not contain DEFAULT_ADDRESS
        defaultOrganizationShouldNotBeFound("address.doesNotContain=" + DEFAULT_ADDRESS);

        // Get all the organizationList where address does not contain UPDATED_ADDRESS
        defaultOrganizationShouldBeFound("address.doesNotContain=" + UPDATED_ADDRESS);
    }


    @Test
    @Transactional
    public void getAllOrganizationsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where type equals to DEFAULT_TYPE
        defaultOrganizationShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the organizationList where type equals to UPDATED_TYPE
        defaultOrganizationShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllOrganizationsByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where type not equals to DEFAULT_TYPE
        defaultOrganizationShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the organizationList where type not equals to UPDATED_TYPE
        defaultOrganizationShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllOrganizationsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultOrganizationShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the organizationList where type equals to UPDATED_TYPE
        defaultOrganizationShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllOrganizationsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where type is not null
        defaultOrganizationShouldBeFound("type.specified=true");

        // Get all the organizationList where type is null
        defaultOrganizationShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrganizationsByProjectsIsEqualToSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);
        Project projects = ProjectResourceIT.createEntity(em);
        em.persist(projects);
        em.flush();
        organization.addProjects(projects);
        organizationRepository.saveAndFlush(organization);
        Long projectsId = projects.getId();

        // Get all the organizationList where projects equals to projectsId
        defaultOrganizationShouldBeFound("projectsId.equals=" + projectsId);

        // Get all the organizationList where projects equals to projectsId + 1
        defaultOrganizationShouldNotBeFound("projectsId.equals=" + (projectsId + 1));
    }


    @Test
    @Transactional
    public void getAllOrganizationsByUsersIsEqualToSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);
        HDUser users = HDUserResourceIT.createEntity(em);
        em.persist(users);
        em.flush();
        organization.addUsers(users);
        organizationRepository.saveAndFlush(organization);
        Long usersId = users.getId();

        // Get all the organizationList where users equals to usersId
        defaultOrganizationShouldBeFound("usersId.equals=" + usersId);

        // Get all the organizationList where users equals to usersId + 1
        defaultOrganizationShouldNotBeFound("usersId.equals=" + (usersId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOrganizationShouldBeFound(String filter) throws Exception {
        restOrganizationMockMvc.perform(get("/api/organizations?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(organization.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));

        // Check, that the count call also returns 1
        restOrganizationMockMvc.perform(get("/api/organizations/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOrganizationShouldNotBeFound(String filter) throws Exception {
        restOrganizationMockMvc.perform(get("/api/organizations?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOrganizationMockMvc.perform(get("/api/organizations/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingOrganization() throws Exception {
        // Get the organization
        restOrganizationMockMvc.perform(get("/api/organizations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrganization() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        int databaseSizeBeforeUpdate = organizationRepository.findAll().size();

        // Update the organization
        Organization updatedOrganization = organizationRepository.findById(organization.getId()).get();
        // Disconnect from session so that the updates on updatedOrganization are not directly saved in db
        em.detach(updatedOrganization);
        updatedOrganization
            .name(UPDATED_NAME)
            .address(UPDATED_ADDRESS)
            .type(UPDATED_TYPE);
        OrganizationDTO organizationDTO = organizationMapper.toDto(updatedOrganization);

        restOrganizationMockMvc.perform(put("/api/organizations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(organizationDTO)))
            .andExpect(status().isOk());

        // Validate the Organization in the database
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeUpdate);
        Organization testOrganization = organizationList.get(organizationList.size() - 1);
        assertThat(testOrganization.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOrganization.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testOrganization.getType()).isEqualTo(UPDATED_TYPE);

        // Validate the Organization in Elasticsearch
        verify(mockOrganizationSearchRepository, times(1)).save(testOrganization);
    }

    @Test
    @Transactional
    public void updateNonExistingOrganization() throws Exception {
        int databaseSizeBeforeUpdate = organizationRepository.findAll().size();

        // Create the Organization
        OrganizationDTO organizationDTO = organizationMapper.toDto(organization);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrganizationMockMvc.perform(put("/api/organizations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(organizationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Organization in the database
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Organization in Elasticsearch
        verify(mockOrganizationSearchRepository, times(0)).save(organization);
    }

    @Test
    @Transactional
    public void deleteOrganization() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        int databaseSizeBeforeDelete = organizationRepository.findAll().size();

        // Delete the organization
        restOrganizationMockMvc.perform(delete("/api/organizations/{id}", organization.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Organization in Elasticsearch
        verify(mockOrganizationSearchRepository, times(1)).deleteById(organization.getId());
    }

    @Test
    @Transactional
    public void searchOrganization() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        organizationRepository.saveAndFlush(organization);
        when(mockOrganizationSearchRepository.search(queryStringQuery("id:" + organization.getId())))
            .thenReturn(Collections.singletonList(organization));

        // Search the organization
        restOrganizationMockMvc.perform(get("/api/_search/organizations?query=id:" + organization.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(organization.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }
}
