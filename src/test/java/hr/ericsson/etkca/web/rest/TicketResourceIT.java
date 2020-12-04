package hr.ericsson.etkca.web.rest;

import hr.ericsson.etkca.CsrappApp;
import hr.ericsson.etkca.domain.Ticket;
import hr.ericsson.etkca.domain.Attachment;
import hr.ericsson.etkca.domain.TicketStatus;
import hr.ericsson.etkca.domain.Category;
import hr.ericsson.etkca.domain.Priority;
import hr.ericsson.etkca.domain.HDUser;
import hr.ericsson.etkca.domain.Project;
import hr.ericsson.etkca.repository.TicketRepository;
import hr.ericsson.etkca.repository.search.TicketSearchRepository;
import hr.ericsson.etkca.service.TicketService;
import hr.ericsson.etkca.service.dto.TicketDTO;
import hr.ericsson.etkca.service.mapper.TicketMapper;
import hr.ericsson.etkca.service.dto.TicketCriteria;
import hr.ericsson.etkca.service.TicketQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link TicketResource} REST controller.
 */
@SpringBootTest(classes = CsrappApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class TicketResourceIT {

    private static final String DEFAULT_RBR = "AAAAAAAAAA";
    private static final String UPDATED_RBR = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private TicketRepository ticketRepository;

    @Mock
    private TicketRepository ticketRepositoryMock;

    @Autowired
    private TicketMapper ticketMapper;

    @Mock
    private TicketService ticketServiceMock;

    @Autowired
    private TicketService ticketService;

    /**
     * This repository is mocked in the hr.ericsson.etkca.repository.search test package.
     *
     * @see hr.ericsson.etkca.repository.search.TicketSearchRepositoryMockConfiguration
     */
    @Autowired
    private TicketSearchRepository mockTicketSearchRepository;

    @Autowired
    private TicketQueryService ticketQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTicketMockMvc;

    private Ticket ticket;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ticket createEntity(EntityManager em) {
        Ticket ticket = new Ticket()
            .rbr(DEFAULT_RBR)
            .description(DEFAULT_DESCRIPTION);
        return ticket;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ticket createUpdatedEntity(EntityManager em) {
        Ticket ticket = new Ticket()
            .rbr(UPDATED_RBR)
            .description(UPDATED_DESCRIPTION);
        return ticket;
    }

    @BeforeEach
    public void initTest() {
        ticket = createEntity(em);
    }

    @Test
    @Transactional
    public void createTicket() throws Exception {
        int databaseSizeBeforeCreate = ticketRepository.findAll().size();
        // Create the Ticket
        TicketDTO ticketDTO = ticketMapper.toDto(ticket);
        restTicketMockMvc.perform(post("/api/tickets")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(ticketDTO)))
            .andExpect(status().isCreated());

        // Validate the Ticket in the database
        List<Ticket> ticketList = ticketRepository.findAll();
        assertThat(ticketList).hasSize(databaseSizeBeforeCreate + 1);
        Ticket testTicket = ticketList.get(ticketList.size() - 1);
        assertThat(testTicket.getRbr()).isEqualTo(DEFAULT_RBR);
        assertThat(testTicket.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the Ticket in Elasticsearch
        verify(mockTicketSearchRepository, times(1)).save(testTicket);
    }

    @Test
    @Transactional
    public void createTicketWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = ticketRepository.findAll().size();

        // Create the Ticket with an existing ID
        ticket.setId(1L);
        TicketDTO ticketDTO = ticketMapper.toDto(ticket);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTicketMockMvc.perform(post("/api/tickets")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(ticketDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Ticket in the database
        List<Ticket> ticketList = ticketRepository.findAll();
        assertThat(ticketList).hasSize(databaseSizeBeforeCreate);

        // Validate the Ticket in Elasticsearch
        verify(mockTicketSearchRepository, times(0)).save(ticket);
    }


    @Test
    @Transactional
    public void getAllTickets() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList
        restTicketMockMvc.perform(get("/api/tickets?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ticket.getId().intValue())))
            .andExpect(jsonPath("$.[*].rbr").value(hasItem(DEFAULT_RBR)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllTicketsWithEagerRelationshipsIsEnabled() throws Exception {
        when(ticketServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTicketMockMvc.perform(get("/api/tickets?eagerload=true"))
            .andExpect(status().isOk());

        verify(ticketServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllTicketsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(ticketServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTicketMockMvc.perform(get("/api/tickets?eagerload=true"))
            .andExpect(status().isOk());

        verify(ticketServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getTicket() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get the ticket
        restTicketMockMvc.perform(get("/api/tickets/{id}", ticket.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ticket.getId().intValue()))
            .andExpect(jsonPath("$.rbr").value(DEFAULT_RBR))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }


    @Test
    @Transactional
    public void getTicketsByIdFiltering() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        Long id = ticket.getId();

        defaultTicketShouldBeFound("id.equals=" + id);
        defaultTicketShouldNotBeFound("id.notEquals=" + id);

        defaultTicketShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTicketShouldNotBeFound("id.greaterThan=" + id);

        defaultTicketShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTicketShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllTicketsByRbrIsEqualToSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where rbr equals to DEFAULT_RBR
        defaultTicketShouldBeFound("rbr.equals=" + DEFAULT_RBR);

        // Get all the ticketList where rbr equals to UPDATED_RBR
        defaultTicketShouldNotBeFound("rbr.equals=" + UPDATED_RBR);
    }

    @Test
    @Transactional
    public void getAllTicketsByRbrIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where rbr not equals to DEFAULT_RBR
        defaultTicketShouldNotBeFound("rbr.notEquals=" + DEFAULT_RBR);

        // Get all the ticketList where rbr not equals to UPDATED_RBR
        defaultTicketShouldBeFound("rbr.notEquals=" + UPDATED_RBR);
    }

    @Test
    @Transactional
    public void getAllTicketsByRbrIsInShouldWork() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where rbr in DEFAULT_RBR or UPDATED_RBR
        defaultTicketShouldBeFound("rbr.in=" + DEFAULT_RBR + "," + UPDATED_RBR);

        // Get all the ticketList where rbr equals to UPDATED_RBR
        defaultTicketShouldNotBeFound("rbr.in=" + UPDATED_RBR);
    }

    @Test
    @Transactional
    public void getAllTicketsByRbrIsNullOrNotNull() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where rbr is not null
        defaultTicketShouldBeFound("rbr.specified=true");

        // Get all the ticketList where rbr is null
        defaultTicketShouldNotBeFound("rbr.specified=false");
    }
                @Test
    @Transactional
    public void getAllTicketsByRbrContainsSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where rbr contains DEFAULT_RBR
        defaultTicketShouldBeFound("rbr.contains=" + DEFAULT_RBR);

        // Get all the ticketList where rbr contains UPDATED_RBR
        defaultTicketShouldNotBeFound("rbr.contains=" + UPDATED_RBR);
    }

    @Test
    @Transactional
    public void getAllTicketsByRbrNotContainsSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where rbr does not contain DEFAULT_RBR
        defaultTicketShouldNotBeFound("rbr.doesNotContain=" + DEFAULT_RBR);

        // Get all the ticketList where rbr does not contain UPDATED_RBR
        defaultTicketShouldBeFound("rbr.doesNotContain=" + UPDATED_RBR);
    }


    @Test
    @Transactional
    public void getAllTicketsByAttachmentsIsEqualToSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);
        Attachment attachments = AttachmentResourceIT.createEntity(em);
        em.persist(attachments);
        em.flush();
        ticket.addAttachments(attachments);
        ticketRepository.saveAndFlush(ticket);
        Long attachmentsId = attachments.getId();

        // Get all the ticketList where attachments equals to attachmentsId
        defaultTicketShouldBeFound("attachmentsId.equals=" + attachmentsId);

        // Get all the ticketList where attachments equals to attachmentsId + 1
        defaultTicketShouldNotBeFound("attachmentsId.equals=" + (attachmentsId + 1));
    }


    @Test
    @Transactional
    public void getAllTicketsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);
        TicketStatus status = TicketStatusResourceIT.createEntity(em);
        em.persist(status);
        em.flush();
        ticket.setStatus(status);
        ticketRepository.saveAndFlush(ticket);
        Long statusId = status.getId();

        // Get all the ticketList where status equals to statusId
        defaultTicketShouldBeFound("statusId.equals=" + statusId);

        // Get all the ticketList where status equals to statusId + 1
        defaultTicketShouldNotBeFound("statusId.equals=" + (statusId + 1));
    }


    @Test
    @Transactional
    public void getAllTicketsByCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);
        Category category = CategoryResourceIT.createEntity(em);
        em.persist(category);
        em.flush();
        ticket.setCategory(category);
        ticketRepository.saveAndFlush(ticket);
        Long categoryId = category.getId();

        // Get all the ticketList where category equals to categoryId
        defaultTicketShouldBeFound("categoryId.equals=" + categoryId);

        // Get all the ticketList where category equals to categoryId + 1
        defaultTicketShouldNotBeFound("categoryId.equals=" + (categoryId + 1));
    }


    @Test
    @Transactional
    public void getAllTicketsByPriorityIsEqualToSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);
        Priority priority = PriorityResourceIT.createEntity(em);
        em.persist(priority);
        em.flush();
        ticket.setPriority(priority);
        ticketRepository.saveAndFlush(ticket);
        Long priorityId = priority.getId();

        // Get all the ticketList where priority equals to priorityId
        defaultTicketShouldBeFound("priorityId.equals=" + priorityId);

        // Get all the ticketList where priority equals to priorityId + 1
        defaultTicketShouldNotBeFound("priorityId.equals=" + (priorityId + 1));
    }


    @Test
    @Transactional
    public void getAllTicketsByUsersIsEqualToSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);
        HDUser users = HDUserResourceIT.createEntity(em);
        em.persist(users);
        em.flush();
        ticket.addUsers(users);
        ticketRepository.saveAndFlush(ticket);
        Long usersId = users.getId();

        // Get all the ticketList where users equals to usersId
        defaultTicketShouldBeFound("usersId.equals=" + usersId);

        // Get all the ticketList where users equals to usersId + 1
        defaultTicketShouldNotBeFound("usersId.equals=" + (usersId + 1));
    }


    @Test
    @Transactional
    public void getAllTicketsByProjectIsEqualToSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);
        Project project = ProjectResourceIT.createEntity(em);
        em.persist(project);
        em.flush();
        ticket.setProject(project);
        ticketRepository.saveAndFlush(ticket);
        Long projectId = project.getId();

        // Get all the ticketList where project equals to projectId
        defaultTicketShouldBeFound("projectId.equals=" + projectId);

        // Get all the ticketList where project equals to projectId + 1
        defaultTicketShouldNotBeFound("projectId.equals=" + (projectId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTicketShouldBeFound(String filter) throws Exception {
        restTicketMockMvc.perform(get("/api/tickets?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ticket.getId().intValue())))
            .andExpect(jsonPath("$.[*].rbr").value(hasItem(DEFAULT_RBR)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));

        // Check, that the count call also returns 1
        restTicketMockMvc.perform(get("/api/tickets/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTicketShouldNotBeFound(String filter) throws Exception {
        restTicketMockMvc.perform(get("/api/tickets?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTicketMockMvc.perform(get("/api/tickets/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingTicket() throws Exception {
        // Get the ticket
        restTicketMockMvc.perform(get("/api/tickets/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTicket() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        int databaseSizeBeforeUpdate = ticketRepository.findAll().size();

        // Update the ticket
        Ticket updatedTicket = ticketRepository.findById(ticket.getId()).get();
        // Disconnect from session so that the updates on updatedTicket are not directly saved in db
        em.detach(updatedTicket);
        updatedTicket
            .rbr(UPDATED_RBR)
            .description(UPDATED_DESCRIPTION);
        TicketDTO ticketDTO = ticketMapper.toDto(updatedTicket);

        restTicketMockMvc.perform(put("/api/tickets")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(ticketDTO)))
            .andExpect(status().isOk());

        // Validate the Ticket in the database
        List<Ticket> ticketList = ticketRepository.findAll();
        assertThat(ticketList).hasSize(databaseSizeBeforeUpdate);
        Ticket testTicket = ticketList.get(ticketList.size() - 1);
        assertThat(testTicket.getRbr()).isEqualTo(UPDATED_RBR);
        assertThat(testTicket.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the Ticket in Elasticsearch
        verify(mockTicketSearchRepository, times(1)).save(testTicket);
    }

    @Test
    @Transactional
    public void updateNonExistingTicket() throws Exception {
        int databaseSizeBeforeUpdate = ticketRepository.findAll().size();

        // Create the Ticket
        TicketDTO ticketDTO = ticketMapper.toDto(ticket);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTicketMockMvc.perform(put("/api/tickets")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(ticketDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Ticket in the database
        List<Ticket> ticketList = ticketRepository.findAll();
        assertThat(ticketList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Ticket in Elasticsearch
        verify(mockTicketSearchRepository, times(0)).save(ticket);
    }

    @Test
    @Transactional
    public void deleteTicket() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        int databaseSizeBeforeDelete = ticketRepository.findAll().size();

        // Delete the ticket
        restTicketMockMvc.perform(delete("/api/tickets/{id}", ticket.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Ticket> ticketList = ticketRepository.findAll();
        assertThat(ticketList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Ticket in Elasticsearch
        verify(mockTicketSearchRepository, times(1)).deleteById(ticket.getId());
    }

    @Test
    @Transactional
    public void searchTicket() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);
        when(mockTicketSearchRepository.search(queryStringQuery("id:" + ticket.getId())))
            .thenReturn(Collections.singletonList(ticket));

        // Search the ticket
        restTicketMockMvc.perform(get("/api/_search/tickets?query=id:" + ticket.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ticket.getId().intValue())))
            .andExpect(jsonPath("$.[*].rbr").value(hasItem(DEFAULT_RBR)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
}
