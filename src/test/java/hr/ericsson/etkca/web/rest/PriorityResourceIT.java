package hr.ericsson.etkca.web.rest;

import hr.ericsson.etkca.CsrappApp;
import hr.ericsson.etkca.domain.Priority;
import hr.ericsson.etkca.repository.PriorityRepository;
import hr.ericsson.etkca.repository.search.PrioritySearchRepository;
import hr.ericsson.etkca.service.PriorityService;
import hr.ericsson.etkca.service.dto.PriorityDTO;
import hr.ericsson.etkca.service.mapper.PriorityMapper;
import hr.ericsson.etkca.service.dto.PriorityCriteria;
import hr.ericsson.etkca.service.PriorityQueryService;

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
 * Integration tests for the {@link PriorityResource} REST controller.
 */
@SpringBootTest(classes = CsrappApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class PriorityResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private PriorityRepository priorityRepository;

    @Autowired
    private PriorityMapper priorityMapper;

    @Autowired
    private PriorityService priorityService;

    /**
     * This repository is mocked in the hr.ericsson.etkca.repository.search test package.
     *
     * @see hr.ericsson.etkca.repository.search.PrioritySearchRepositoryMockConfiguration
     */
    @Autowired
    private PrioritySearchRepository mockPrioritySearchRepository;

    @Autowired
    private PriorityQueryService priorityQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPriorityMockMvc;

    private Priority priority;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Priority createEntity(EntityManager em) {
        Priority priority = new Priority()
            .name(DEFAULT_NAME);
        return priority;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Priority createUpdatedEntity(EntityManager em) {
        Priority priority = new Priority()
            .name(UPDATED_NAME);
        return priority;
    }

    @BeforeEach
    public void initTest() {
        priority = createEntity(em);
    }

    @Test
    @Transactional
    public void createPriority() throws Exception {
        int databaseSizeBeforeCreate = priorityRepository.findAll().size();
        // Create the Priority
        PriorityDTO priorityDTO = priorityMapper.toDto(priority);
        restPriorityMockMvc.perform(post("/api/priorities")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(priorityDTO)))
            .andExpect(status().isCreated());

        // Validate the Priority in the database
        List<Priority> priorityList = priorityRepository.findAll();
        assertThat(priorityList).hasSize(databaseSizeBeforeCreate + 1);
        Priority testPriority = priorityList.get(priorityList.size() - 1);
        assertThat(testPriority.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the Priority in Elasticsearch
        verify(mockPrioritySearchRepository, times(1)).save(testPriority);
    }

    @Test
    @Transactional
    public void createPriorityWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = priorityRepository.findAll().size();

        // Create the Priority with an existing ID
        priority.setId(1L);
        PriorityDTO priorityDTO = priorityMapper.toDto(priority);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPriorityMockMvc.perform(post("/api/priorities")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(priorityDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Priority in the database
        List<Priority> priorityList = priorityRepository.findAll();
        assertThat(priorityList).hasSize(databaseSizeBeforeCreate);

        // Validate the Priority in Elasticsearch
        verify(mockPrioritySearchRepository, times(0)).save(priority);
    }


    @Test
    @Transactional
    public void getAllPriorities() throws Exception {
        // Initialize the database
        priorityRepository.saveAndFlush(priority);

        // Get all the priorityList
        restPriorityMockMvc.perform(get("/api/priorities?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(priority.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
    
    @Test
    @Transactional
    public void getPriority() throws Exception {
        // Initialize the database
        priorityRepository.saveAndFlush(priority);

        // Get the priority
        restPriorityMockMvc.perform(get("/api/priorities/{id}", priority.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(priority.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }


    @Test
    @Transactional
    public void getPrioritiesByIdFiltering() throws Exception {
        // Initialize the database
        priorityRepository.saveAndFlush(priority);

        Long id = priority.getId();

        defaultPriorityShouldBeFound("id.equals=" + id);
        defaultPriorityShouldNotBeFound("id.notEquals=" + id);

        defaultPriorityShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPriorityShouldNotBeFound("id.greaterThan=" + id);

        defaultPriorityShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPriorityShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllPrioritiesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        priorityRepository.saveAndFlush(priority);

        // Get all the priorityList where name equals to DEFAULT_NAME
        defaultPriorityShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the priorityList where name equals to UPDATED_NAME
        defaultPriorityShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPrioritiesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        priorityRepository.saveAndFlush(priority);

        // Get all the priorityList where name not equals to DEFAULT_NAME
        defaultPriorityShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the priorityList where name not equals to UPDATED_NAME
        defaultPriorityShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPrioritiesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        priorityRepository.saveAndFlush(priority);

        // Get all the priorityList where name in DEFAULT_NAME or UPDATED_NAME
        defaultPriorityShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the priorityList where name equals to UPDATED_NAME
        defaultPriorityShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPrioritiesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        priorityRepository.saveAndFlush(priority);

        // Get all the priorityList where name is not null
        defaultPriorityShouldBeFound("name.specified=true");

        // Get all the priorityList where name is null
        defaultPriorityShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllPrioritiesByNameContainsSomething() throws Exception {
        // Initialize the database
        priorityRepository.saveAndFlush(priority);

        // Get all the priorityList where name contains DEFAULT_NAME
        defaultPriorityShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the priorityList where name contains UPDATED_NAME
        defaultPriorityShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPrioritiesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        priorityRepository.saveAndFlush(priority);

        // Get all the priorityList where name does not contain DEFAULT_NAME
        defaultPriorityShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the priorityList where name does not contain UPDATED_NAME
        defaultPriorityShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPriorityShouldBeFound(String filter) throws Exception {
        restPriorityMockMvc.perform(get("/api/priorities?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(priority.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restPriorityMockMvc.perform(get("/api/priorities/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPriorityShouldNotBeFound(String filter) throws Exception {
        restPriorityMockMvc.perform(get("/api/priorities?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPriorityMockMvc.perform(get("/api/priorities/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingPriority() throws Exception {
        // Get the priority
        restPriorityMockMvc.perform(get("/api/priorities/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePriority() throws Exception {
        // Initialize the database
        priorityRepository.saveAndFlush(priority);

        int databaseSizeBeforeUpdate = priorityRepository.findAll().size();

        // Update the priority
        Priority updatedPriority = priorityRepository.findById(priority.getId()).get();
        // Disconnect from session so that the updates on updatedPriority are not directly saved in db
        em.detach(updatedPriority);
        updatedPriority
            .name(UPDATED_NAME);
        PriorityDTO priorityDTO = priorityMapper.toDto(updatedPriority);

        restPriorityMockMvc.perform(put("/api/priorities")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(priorityDTO)))
            .andExpect(status().isOk());

        // Validate the Priority in the database
        List<Priority> priorityList = priorityRepository.findAll();
        assertThat(priorityList).hasSize(databaseSizeBeforeUpdate);
        Priority testPriority = priorityList.get(priorityList.size() - 1);
        assertThat(testPriority.getName()).isEqualTo(UPDATED_NAME);

        // Validate the Priority in Elasticsearch
        verify(mockPrioritySearchRepository, times(1)).save(testPriority);
    }

    @Test
    @Transactional
    public void updateNonExistingPriority() throws Exception {
        int databaseSizeBeforeUpdate = priorityRepository.findAll().size();

        // Create the Priority
        PriorityDTO priorityDTO = priorityMapper.toDto(priority);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPriorityMockMvc.perform(put("/api/priorities")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(priorityDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Priority in the database
        List<Priority> priorityList = priorityRepository.findAll();
        assertThat(priorityList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Priority in Elasticsearch
        verify(mockPrioritySearchRepository, times(0)).save(priority);
    }

    @Test
    @Transactional
    public void deletePriority() throws Exception {
        // Initialize the database
        priorityRepository.saveAndFlush(priority);

        int databaseSizeBeforeDelete = priorityRepository.findAll().size();

        // Delete the priority
        restPriorityMockMvc.perform(delete("/api/priorities/{id}", priority.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Priority> priorityList = priorityRepository.findAll();
        assertThat(priorityList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Priority in Elasticsearch
        verify(mockPrioritySearchRepository, times(1)).deleteById(priority.getId());
    }

    @Test
    @Transactional
    public void searchPriority() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        priorityRepository.saveAndFlush(priority);
        when(mockPrioritySearchRepository.search(queryStringQuery("id:" + priority.getId())))
            .thenReturn(Collections.singletonList(priority));

        // Search the priority
        restPriorityMockMvc.perform(get("/api/_search/priorities?query=id:" + priority.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(priority.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
}
