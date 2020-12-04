package hr.ericsson.etkca.web.rest;

import hr.ericsson.etkca.CsrappApp;
import hr.ericsson.etkca.domain.TicketStatus;
import hr.ericsson.etkca.repository.TicketStatusRepository;
import hr.ericsson.etkca.repository.search.TicketStatusSearchRepository;
import hr.ericsson.etkca.service.TicketStatusService;
import hr.ericsson.etkca.service.dto.TicketStatusDTO;
import hr.ericsson.etkca.service.mapper.TicketStatusMapper;
import hr.ericsson.etkca.service.dto.TicketStatusCriteria;
import hr.ericsson.etkca.service.TicketStatusQueryService;

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
 * Integration tests for the {@link TicketStatusResource} REST controller.
 */
@SpringBootTest(classes = CsrappApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class TicketStatusResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private TicketStatusRepository ticketStatusRepository;

    @Autowired
    private TicketStatusMapper ticketStatusMapper;

    @Autowired
    private TicketStatusService ticketStatusService;

    /**
     * This repository is mocked in the hr.ericsson.etkca.repository.search test package.
     *
     * @see hr.ericsson.etkca.repository.search.TicketStatusSearchRepositoryMockConfiguration
     */
    @Autowired
    private TicketStatusSearchRepository mockTicketStatusSearchRepository;

    @Autowired
    private TicketStatusQueryService ticketStatusQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTicketStatusMockMvc;

    private TicketStatus ticketStatus;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TicketStatus createEntity(EntityManager em) {
        TicketStatus ticketStatus = new TicketStatus()
            .name(DEFAULT_NAME);
        return ticketStatus;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TicketStatus createUpdatedEntity(EntityManager em) {
        TicketStatus ticketStatus = new TicketStatus()
            .name(UPDATED_NAME);
        return ticketStatus;
    }

    @BeforeEach
    public void initTest() {
        ticketStatus = createEntity(em);
    }

    @Test
    @Transactional
    public void createTicketStatus() throws Exception {
        int databaseSizeBeforeCreate = ticketStatusRepository.findAll().size();
        // Create the TicketStatus
        TicketStatusDTO ticketStatusDTO = ticketStatusMapper.toDto(ticketStatus);
        restTicketStatusMockMvc.perform(post("/api/ticket-statuses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(ticketStatusDTO)))
            .andExpect(status().isCreated());

        // Validate the TicketStatus in the database
        List<TicketStatus> ticketStatusList = ticketStatusRepository.findAll();
        assertThat(ticketStatusList).hasSize(databaseSizeBeforeCreate + 1);
        TicketStatus testTicketStatus = ticketStatusList.get(ticketStatusList.size() - 1);
        assertThat(testTicketStatus.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the TicketStatus in Elasticsearch
        verify(mockTicketStatusSearchRepository, times(1)).save(testTicketStatus);
    }

    @Test
    @Transactional
    public void createTicketStatusWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = ticketStatusRepository.findAll().size();

        // Create the TicketStatus with an existing ID
        ticketStatus.setId(1L);
        TicketStatusDTO ticketStatusDTO = ticketStatusMapper.toDto(ticketStatus);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTicketStatusMockMvc.perform(post("/api/ticket-statuses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(ticketStatusDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TicketStatus in the database
        List<TicketStatus> ticketStatusList = ticketStatusRepository.findAll();
        assertThat(ticketStatusList).hasSize(databaseSizeBeforeCreate);

        // Validate the TicketStatus in Elasticsearch
        verify(mockTicketStatusSearchRepository, times(0)).save(ticketStatus);
    }


    @Test
    @Transactional
    public void getAllTicketStatuses() throws Exception {
        // Initialize the database
        ticketStatusRepository.saveAndFlush(ticketStatus);

        // Get all the ticketStatusList
        restTicketStatusMockMvc.perform(get("/api/ticket-statuses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ticketStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
    
    @Test
    @Transactional
    public void getTicketStatus() throws Exception {
        // Initialize the database
        ticketStatusRepository.saveAndFlush(ticketStatus);

        // Get the ticketStatus
        restTicketStatusMockMvc.perform(get("/api/ticket-statuses/{id}", ticketStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ticketStatus.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }


    @Test
    @Transactional
    public void getTicketStatusesByIdFiltering() throws Exception {
        // Initialize the database
        ticketStatusRepository.saveAndFlush(ticketStatus);

        Long id = ticketStatus.getId();

        defaultTicketStatusShouldBeFound("id.equals=" + id);
        defaultTicketStatusShouldNotBeFound("id.notEquals=" + id);

        defaultTicketStatusShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTicketStatusShouldNotBeFound("id.greaterThan=" + id);

        defaultTicketStatusShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTicketStatusShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllTicketStatusesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        ticketStatusRepository.saveAndFlush(ticketStatus);

        // Get all the ticketStatusList where name equals to DEFAULT_NAME
        defaultTicketStatusShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the ticketStatusList where name equals to UPDATED_NAME
        defaultTicketStatusShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllTicketStatusesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ticketStatusRepository.saveAndFlush(ticketStatus);

        // Get all the ticketStatusList where name not equals to DEFAULT_NAME
        defaultTicketStatusShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the ticketStatusList where name not equals to UPDATED_NAME
        defaultTicketStatusShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllTicketStatusesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        ticketStatusRepository.saveAndFlush(ticketStatus);

        // Get all the ticketStatusList where name in DEFAULT_NAME or UPDATED_NAME
        defaultTicketStatusShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the ticketStatusList where name equals to UPDATED_NAME
        defaultTicketStatusShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllTicketStatusesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        ticketStatusRepository.saveAndFlush(ticketStatus);

        // Get all the ticketStatusList where name is not null
        defaultTicketStatusShouldBeFound("name.specified=true");

        // Get all the ticketStatusList where name is null
        defaultTicketStatusShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllTicketStatusesByNameContainsSomething() throws Exception {
        // Initialize the database
        ticketStatusRepository.saveAndFlush(ticketStatus);

        // Get all the ticketStatusList where name contains DEFAULT_NAME
        defaultTicketStatusShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the ticketStatusList where name contains UPDATED_NAME
        defaultTicketStatusShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllTicketStatusesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        ticketStatusRepository.saveAndFlush(ticketStatus);

        // Get all the ticketStatusList where name does not contain DEFAULT_NAME
        defaultTicketStatusShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the ticketStatusList where name does not contain UPDATED_NAME
        defaultTicketStatusShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTicketStatusShouldBeFound(String filter) throws Exception {
        restTicketStatusMockMvc.perform(get("/api/ticket-statuses?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ticketStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restTicketStatusMockMvc.perform(get("/api/ticket-statuses/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTicketStatusShouldNotBeFound(String filter) throws Exception {
        restTicketStatusMockMvc.perform(get("/api/ticket-statuses?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTicketStatusMockMvc.perform(get("/api/ticket-statuses/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingTicketStatus() throws Exception {
        // Get the ticketStatus
        restTicketStatusMockMvc.perform(get("/api/ticket-statuses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTicketStatus() throws Exception {
        // Initialize the database
        ticketStatusRepository.saveAndFlush(ticketStatus);

        int databaseSizeBeforeUpdate = ticketStatusRepository.findAll().size();

        // Update the ticketStatus
        TicketStatus updatedTicketStatus = ticketStatusRepository.findById(ticketStatus.getId()).get();
        // Disconnect from session so that the updates on updatedTicketStatus are not directly saved in db
        em.detach(updatedTicketStatus);
        updatedTicketStatus
            .name(UPDATED_NAME);
        TicketStatusDTO ticketStatusDTO = ticketStatusMapper.toDto(updatedTicketStatus);

        restTicketStatusMockMvc.perform(put("/api/ticket-statuses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(ticketStatusDTO)))
            .andExpect(status().isOk());

        // Validate the TicketStatus in the database
        List<TicketStatus> ticketStatusList = ticketStatusRepository.findAll();
        assertThat(ticketStatusList).hasSize(databaseSizeBeforeUpdate);
        TicketStatus testTicketStatus = ticketStatusList.get(ticketStatusList.size() - 1);
        assertThat(testTicketStatus.getName()).isEqualTo(UPDATED_NAME);

        // Validate the TicketStatus in Elasticsearch
        verify(mockTicketStatusSearchRepository, times(1)).save(testTicketStatus);
    }

    @Test
    @Transactional
    public void updateNonExistingTicketStatus() throws Exception {
        int databaseSizeBeforeUpdate = ticketStatusRepository.findAll().size();

        // Create the TicketStatus
        TicketStatusDTO ticketStatusDTO = ticketStatusMapper.toDto(ticketStatus);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTicketStatusMockMvc.perform(put("/api/ticket-statuses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(ticketStatusDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TicketStatus in the database
        List<TicketStatus> ticketStatusList = ticketStatusRepository.findAll();
        assertThat(ticketStatusList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TicketStatus in Elasticsearch
        verify(mockTicketStatusSearchRepository, times(0)).save(ticketStatus);
    }

    @Test
    @Transactional
    public void deleteTicketStatus() throws Exception {
        // Initialize the database
        ticketStatusRepository.saveAndFlush(ticketStatus);

        int databaseSizeBeforeDelete = ticketStatusRepository.findAll().size();

        // Delete the ticketStatus
        restTicketStatusMockMvc.perform(delete("/api/ticket-statuses/{id}", ticketStatus.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TicketStatus> ticketStatusList = ticketStatusRepository.findAll();
        assertThat(ticketStatusList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the TicketStatus in Elasticsearch
        verify(mockTicketStatusSearchRepository, times(1)).deleteById(ticketStatus.getId());
    }

    @Test
    @Transactional
    public void searchTicketStatus() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        ticketStatusRepository.saveAndFlush(ticketStatus);
        when(mockTicketStatusSearchRepository.search(queryStringQuery("id:" + ticketStatus.getId())))
            .thenReturn(Collections.singletonList(ticketStatus));

        // Search the ticketStatus
        restTicketStatusMockMvc.perform(get("/api/_search/ticket-statuses?query=id:" + ticketStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ticketStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
}
