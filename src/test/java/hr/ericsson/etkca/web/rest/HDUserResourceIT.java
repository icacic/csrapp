package hr.ericsson.etkca.web.rest;

import hr.ericsson.etkca.CsrappApp;
import hr.ericsson.etkca.domain.HDUser;
import hr.ericsson.etkca.domain.User;
import hr.ericsson.etkca.domain.Organization;
import hr.ericsson.etkca.domain.Project;
import hr.ericsson.etkca.domain.Ticket;
import hr.ericsson.etkca.repository.HDUserRepository;
import hr.ericsson.etkca.repository.search.HDUserSearchRepository;
import hr.ericsson.etkca.service.HDUserService;
import hr.ericsson.etkca.service.dto.HDUserDTO;
import hr.ericsson.etkca.service.mapper.HDUserMapper;
import hr.ericsson.etkca.service.dto.HDUserCriteria;
import hr.ericsson.etkca.service.HDUserQueryService;

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

/**
 * Integration tests for the {@link HDUserResource} REST controller.
 */
@SpringBootTest(classes = CsrappApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class HDUserResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    @Autowired
    private HDUserRepository hDUserRepository;

    @Autowired
    private HDUserMapper hDUserMapper;

    @Autowired
    private HDUserService hDUserService;

    /**
     * This repository is mocked in the hr.ericsson.etkca.repository.search test package.
     *
     * @see hr.ericsson.etkca.repository.search.HDUserSearchRepositoryMockConfiguration
     */
    @Autowired
    private HDUserSearchRepository mockHDUserSearchRepository;

    @Autowired
    private HDUserQueryService hDUserQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHDUserMockMvc;

    private HDUser hDUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HDUser createEntity(EntityManager em) {
        HDUser hDUser = new HDUser()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .email(DEFAULT_EMAIL)
            .address(DEFAULT_ADDRESS);
        return hDUser;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HDUser createUpdatedEntity(EntityManager em) {
        HDUser hDUser = new HDUser()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .address(UPDATED_ADDRESS);
        return hDUser;
    }

    @BeforeEach
    public void initTest() {
        hDUser = createEntity(em);
    }

    @Test
    @Transactional
    public void createHDUser() throws Exception {
        int databaseSizeBeforeCreate = hDUserRepository.findAll().size();
        // Create the HDUser
        HDUserDTO hDUserDTO = hDUserMapper.toDto(hDUser);
        restHDUserMockMvc.perform(post("/api/hd-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(hDUserDTO)))
            .andExpect(status().isCreated());

        // Validate the HDUser in the database
        List<HDUser> hDUserList = hDUserRepository.findAll();
        assertThat(hDUserList).hasSize(databaseSizeBeforeCreate + 1);
        HDUser testHDUser = hDUserList.get(hDUserList.size() - 1);
        assertThat(testHDUser.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testHDUser.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testHDUser.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testHDUser.getAddress()).isEqualTo(DEFAULT_ADDRESS);

        // Validate the HDUser in Elasticsearch
        verify(mockHDUserSearchRepository, times(1)).save(testHDUser);
    }

    @Test
    @Transactional
    public void createHDUserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = hDUserRepository.findAll().size();

        // Create the HDUser with an existing ID
        hDUser.setId(1L);
        HDUserDTO hDUserDTO = hDUserMapper.toDto(hDUser);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHDUserMockMvc.perform(post("/api/hd-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(hDUserDTO)))
            .andExpect(status().isBadRequest());

        // Validate the HDUser in the database
        List<HDUser> hDUserList = hDUserRepository.findAll();
        assertThat(hDUserList).hasSize(databaseSizeBeforeCreate);

        // Validate the HDUser in Elasticsearch
        verify(mockHDUserSearchRepository, times(0)).save(hDUser);
    }


    @Test
    @Transactional
    public void getAllHDUsers() throws Exception {
        // Initialize the database
        hDUserRepository.saveAndFlush(hDUser);

        // Get all the hDUserList
        restHDUserMockMvc.perform(get("/api/hd-users?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hDUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)));
    }
    
    @Test
    @Transactional
    public void getHDUser() throws Exception {
        // Initialize the database
        hDUserRepository.saveAndFlush(hDUser);

        // Get the hDUser
        restHDUserMockMvc.perform(get("/api/hd-users/{id}", hDUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(hDUser.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS));
    }


    @Test
    @Transactional
    public void getHDUsersByIdFiltering() throws Exception {
        // Initialize the database
        hDUserRepository.saveAndFlush(hDUser);

        Long id = hDUser.getId();

        defaultHDUserShouldBeFound("id.equals=" + id);
        defaultHDUserShouldNotBeFound("id.notEquals=" + id);

        defaultHDUserShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultHDUserShouldNotBeFound("id.greaterThan=" + id);

        defaultHDUserShouldBeFound("id.lessThanOrEqual=" + id);
        defaultHDUserShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllHDUsersByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        hDUserRepository.saveAndFlush(hDUser);

        // Get all the hDUserList where firstName equals to DEFAULT_FIRST_NAME
        defaultHDUserShouldBeFound("firstName.equals=" + DEFAULT_FIRST_NAME);

        // Get all the hDUserList where firstName equals to UPDATED_FIRST_NAME
        defaultHDUserShouldNotBeFound("firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllHDUsersByFirstNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        hDUserRepository.saveAndFlush(hDUser);

        // Get all the hDUserList where firstName not equals to DEFAULT_FIRST_NAME
        defaultHDUserShouldNotBeFound("firstName.notEquals=" + DEFAULT_FIRST_NAME);

        // Get all the hDUserList where firstName not equals to UPDATED_FIRST_NAME
        defaultHDUserShouldBeFound("firstName.notEquals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllHDUsersByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        hDUserRepository.saveAndFlush(hDUser);

        // Get all the hDUserList where firstName in DEFAULT_FIRST_NAME or UPDATED_FIRST_NAME
        defaultHDUserShouldBeFound("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME);

        // Get all the hDUserList where firstName equals to UPDATED_FIRST_NAME
        defaultHDUserShouldNotBeFound("firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllHDUsersByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        hDUserRepository.saveAndFlush(hDUser);

        // Get all the hDUserList where firstName is not null
        defaultHDUserShouldBeFound("firstName.specified=true");

        // Get all the hDUserList where firstName is null
        defaultHDUserShouldNotBeFound("firstName.specified=false");
    }
                @Test
    @Transactional
    public void getAllHDUsersByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        hDUserRepository.saveAndFlush(hDUser);

        // Get all the hDUserList where firstName contains DEFAULT_FIRST_NAME
        defaultHDUserShouldBeFound("firstName.contains=" + DEFAULT_FIRST_NAME);

        // Get all the hDUserList where firstName contains UPDATED_FIRST_NAME
        defaultHDUserShouldNotBeFound("firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllHDUsersByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        hDUserRepository.saveAndFlush(hDUser);

        // Get all the hDUserList where firstName does not contain DEFAULT_FIRST_NAME
        defaultHDUserShouldNotBeFound("firstName.doesNotContain=" + DEFAULT_FIRST_NAME);

        // Get all the hDUserList where firstName does not contain UPDATED_FIRST_NAME
        defaultHDUserShouldBeFound("firstName.doesNotContain=" + UPDATED_FIRST_NAME);
    }


    @Test
    @Transactional
    public void getAllHDUsersByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        hDUserRepository.saveAndFlush(hDUser);

        // Get all the hDUserList where lastName equals to DEFAULT_LAST_NAME
        defaultHDUserShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the hDUserList where lastName equals to UPDATED_LAST_NAME
        defaultHDUserShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllHDUsersByLastNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        hDUserRepository.saveAndFlush(hDUser);

        // Get all the hDUserList where lastName not equals to DEFAULT_LAST_NAME
        defaultHDUserShouldNotBeFound("lastName.notEquals=" + DEFAULT_LAST_NAME);

        // Get all the hDUserList where lastName not equals to UPDATED_LAST_NAME
        defaultHDUserShouldBeFound("lastName.notEquals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllHDUsersByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        hDUserRepository.saveAndFlush(hDUser);

        // Get all the hDUserList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultHDUserShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the hDUserList where lastName equals to UPDATED_LAST_NAME
        defaultHDUserShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllHDUsersByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        hDUserRepository.saveAndFlush(hDUser);

        // Get all the hDUserList where lastName is not null
        defaultHDUserShouldBeFound("lastName.specified=true");

        // Get all the hDUserList where lastName is null
        defaultHDUserShouldNotBeFound("lastName.specified=false");
    }
                @Test
    @Transactional
    public void getAllHDUsersByLastNameContainsSomething() throws Exception {
        // Initialize the database
        hDUserRepository.saveAndFlush(hDUser);

        // Get all the hDUserList where lastName contains DEFAULT_LAST_NAME
        defaultHDUserShouldBeFound("lastName.contains=" + DEFAULT_LAST_NAME);

        // Get all the hDUserList where lastName contains UPDATED_LAST_NAME
        defaultHDUserShouldNotBeFound("lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllHDUsersByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        hDUserRepository.saveAndFlush(hDUser);

        // Get all the hDUserList where lastName does not contain DEFAULT_LAST_NAME
        defaultHDUserShouldNotBeFound("lastName.doesNotContain=" + DEFAULT_LAST_NAME);

        // Get all the hDUserList where lastName does not contain UPDATED_LAST_NAME
        defaultHDUserShouldBeFound("lastName.doesNotContain=" + UPDATED_LAST_NAME);
    }


    @Test
    @Transactional
    public void getAllHDUsersByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        hDUserRepository.saveAndFlush(hDUser);

        // Get all the hDUserList where email equals to DEFAULT_EMAIL
        defaultHDUserShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the hDUserList where email equals to UPDATED_EMAIL
        defaultHDUserShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllHDUsersByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        hDUserRepository.saveAndFlush(hDUser);

        // Get all the hDUserList where email not equals to DEFAULT_EMAIL
        defaultHDUserShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the hDUserList where email not equals to UPDATED_EMAIL
        defaultHDUserShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllHDUsersByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        hDUserRepository.saveAndFlush(hDUser);

        // Get all the hDUserList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultHDUserShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the hDUserList where email equals to UPDATED_EMAIL
        defaultHDUserShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllHDUsersByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        hDUserRepository.saveAndFlush(hDUser);

        // Get all the hDUserList where email is not null
        defaultHDUserShouldBeFound("email.specified=true");

        // Get all the hDUserList where email is null
        defaultHDUserShouldNotBeFound("email.specified=false");
    }
                @Test
    @Transactional
    public void getAllHDUsersByEmailContainsSomething() throws Exception {
        // Initialize the database
        hDUserRepository.saveAndFlush(hDUser);

        // Get all the hDUserList where email contains DEFAULT_EMAIL
        defaultHDUserShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the hDUserList where email contains UPDATED_EMAIL
        defaultHDUserShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllHDUsersByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        hDUserRepository.saveAndFlush(hDUser);

        // Get all the hDUserList where email does not contain DEFAULT_EMAIL
        defaultHDUserShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the hDUserList where email does not contain UPDATED_EMAIL
        defaultHDUserShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }


    @Test
    @Transactional
    public void getAllHDUsersByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        hDUserRepository.saveAndFlush(hDUser);

        // Get all the hDUserList where address equals to DEFAULT_ADDRESS
        defaultHDUserShouldBeFound("address.equals=" + DEFAULT_ADDRESS);

        // Get all the hDUserList where address equals to UPDATED_ADDRESS
        defaultHDUserShouldNotBeFound("address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllHDUsersByAddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        hDUserRepository.saveAndFlush(hDUser);

        // Get all the hDUserList where address not equals to DEFAULT_ADDRESS
        defaultHDUserShouldNotBeFound("address.notEquals=" + DEFAULT_ADDRESS);

        // Get all the hDUserList where address not equals to UPDATED_ADDRESS
        defaultHDUserShouldBeFound("address.notEquals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllHDUsersByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        hDUserRepository.saveAndFlush(hDUser);

        // Get all the hDUserList where address in DEFAULT_ADDRESS or UPDATED_ADDRESS
        defaultHDUserShouldBeFound("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS);

        // Get all the hDUserList where address equals to UPDATED_ADDRESS
        defaultHDUserShouldNotBeFound("address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllHDUsersByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        hDUserRepository.saveAndFlush(hDUser);

        // Get all the hDUserList where address is not null
        defaultHDUserShouldBeFound("address.specified=true");

        // Get all the hDUserList where address is null
        defaultHDUserShouldNotBeFound("address.specified=false");
    }
                @Test
    @Transactional
    public void getAllHDUsersByAddressContainsSomething() throws Exception {
        // Initialize the database
        hDUserRepository.saveAndFlush(hDUser);

        // Get all the hDUserList where address contains DEFAULT_ADDRESS
        defaultHDUserShouldBeFound("address.contains=" + DEFAULT_ADDRESS);

        // Get all the hDUserList where address contains UPDATED_ADDRESS
        defaultHDUserShouldNotBeFound("address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllHDUsersByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        hDUserRepository.saveAndFlush(hDUser);

        // Get all the hDUserList where address does not contain DEFAULT_ADDRESS
        defaultHDUserShouldNotBeFound("address.doesNotContain=" + DEFAULT_ADDRESS);

        // Get all the hDUserList where address does not contain UPDATED_ADDRESS
        defaultHDUserShouldBeFound("address.doesNotContain=" + UPDATED_ADDRESS);
    }


    @Test
    @Transactional
    public void getAllHDUsersByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        hDUserRepository.saveAndFlush(hDUser);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        hDUser.setUser(user);
        hDUserRepository.saveAndFlush(hDUser);
        Long userId = user.getId();

        // Get all the hDUserList where user equals to userId
        defaultHDUserShouldBeFound("userId.equals=" + userId);

        // Get all the hDUserList where user equals to userId + 1
        defaultHDUserShouldNotBeFound("userId.equals=" + (userId + 1));
    }


    @Test
    @Transactional
    public void getAllHDUsersByOrganizationIsEqualToSomething() throws Exception {
        // Initialize the database
        hDUserRepository.saveAndFlush(hDUser);
        Organization organization = OrganizationResourceIT.createEntity(em);
        em.persist(organization);
        em.flush();
        hDUser.setOrganization(organization);
        hDUserRepository.saveAndFlush(hDUser);
        Long organizationId = organization.getId();

        // Get all the hDUserList where organization equals to organizationId
        defaultHDUserShouldBeFound("organizationId.equals=" + organizationId);

        // Get all the hDUserList where organization equals to organizationId + 1
        defaultHDUserShouldNotBeFound("organizationId.equals=" + (organizationId + 1));
    }


    @Test
    @Transactional
    public void getAllHDUsersByProjectsIsEqualToSomething() throws Exception {
        // Initialize the database
        hDUserRepository.saveAndFlush(hDUser);
        Project projects = ProjectResourceIT.createEntity(em);
        em.persist(projects);
        em.flush();
        hDUser.addProjects(projects);
        hDUserRepository.saveAndFlush(hDUser);
        Long projectsId = projects.getId();

        // Get all the hDUserList where projects equals to projectsId
        defaultHDUserShouldBeFound("projectsId.equals=" + projectsId);

        // Get all the hDUserList where projects equals to projectsId + 1
        defaultHDUserShouldNotBeFound("projectsId.equals=" + (projectsId + 1));
    }


    @Test
    @Transactional
    public void getAllHDUsersByTicketsIsEqualToSomething() throws Exception {
        // Initialize the database
        hDUserRepository.saveAndFlush(hDUser);
        Ticket tickets = TicketResourceIT.createEntity(em);
        em.persist(tickets);
        em.flush();
        hDUser.addTickets(tickets);
        hDUserRepository.saveAndFlush(hDUser);
        Long ticketsId = tickets.getId();

        // Get all the hDUserList where tickets equals to ticketsId
        defaultHDUserShouldBeFound("ticketsId.equals=" + ticketsId);

        // Get all the hDUserList where tickets equals to ticketsId + 1
        defaultHDUserShouldNotBeFound("ticketsId.equals=" + (ticketsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultHDUserShouldBeFound(String filter) throws Exception {
        restHDUserMockMvc.perform(get("/api/hd-users?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hDUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)));

        // Check, that the count call also returns 1
        restHDUserMockMvc.perform(get("/api/hd-users/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultHDUserShouldNotBeFound(String filter) throws Exception {
        restHDUserMockMvc.perform(get("/api/hd-users?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restHDUserMockMvc.perform(get("/api/hd-users/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingHDUser() throws Exception {
        // Get the hDUser
        restHDUserMockMvc.perform(get("/api/hd-users/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHDUser() throws Exception {
        // Initialize the database
        hDUserRepository.saveAndFlush(hDUser);

        int databaseSizeBeforeUpdate = hDUserRepository.findAll().size();

        // Update the hDUser
        HDUser updatedHDUser = hDUserRepository.findById(hDUser.getId()).get();
        // Disconnect from session so that the updates on updatedHDUser are not directly saved in db
        em.detach(updatedHDUser);
        updatedHDUser
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .address(UPDATED_ADDRESS);
        HDUserDTO hDUserDTO = hDUserMapper.toDto(updatedHDUser);

        restHDUserMockMvc.perform(put("/api/hd-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(hDUserDTO)))
            .andExpect(status().isOk());

        // Validate the HDUser in the database
        List<HDUser> hDUserList = hDUserRepository.findAll();
        assertThat(hDUserList).hasSize(databaseSizeBeforeUpdate);
        HDUser testHDUser = hDUserList.get(hDUserList.size() - 1);
        assertThat(testHDUser.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testHDUser.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testHDUser.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testHDUser.getAddress()).isEqualTo(UPDATED_ADDRESS);

        // Validate the HDUser in Elasticsearch
        verify(mockHDUserSearchRepository, times(1)).save(testHDUser);
    }

    @Test
    @Transactional
    public void updateNonExistingHDUser() throws Exception {
        int databaseSizeBeforeUpdate = hDUserRepository.findAll().size();

        // Create the HDUser
        HDUserDTO hDUserDTO = hDUserMapper.toDto(hDUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHDUserMockMvc.perform(put("/api/hd-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(hDUserDTO)))
            .andExpect(status().isBadRequest());

        // Validate the HDUser in the database
        List<HDUser> hDUserList = hDUserRepository.findAll();
        assertThat(hDUserList).hasSize(databaseSizeBeforeUpdate);

        // Validate the HDUser in Elasticsearch
        verify(mockHDUserSearchRepository, times(0)).save(hDUser);
    }

    @Test
    @Transactional
    public void deleteHDUser() throws Exception {
        // Initialize the database
        hDUserRepository.saveAndFlush(hDUser);

        int databaseSizeBeforeDelete = hDUserRepository.findAll().size();

        // Delete the hDUser
        restHDUserMockMvc.perform(delete("/api/hd-users/{id}", hDUser.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<HDUser> hDUserList = hDUserRepository.findAll();
        assertThat(hDUserList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the HDUser in Elasticsearch
        verify(mockHDUserSearchRepository, times(1)).deleteById(hDUser.getId());
    }

    @Test
    @Transactional
    public void searchHDUser() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        hDUserRepository.saveAndFlush(hDUser);
        when(mockHDUserSearchRepository.search(queryStringQuery("id:" + hDUser.getId())))
            .thenReturn(Collections.singletonList(hDUser));

        // Search the hDUser
        restHDUserMockMvc.perform(get("/api/_search/hd-users?query=id:" + hDUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hDUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)));
    }
}
