package hr.ericsson.etkca.service;

import hr.ericsson.etkca.domain.Project;
import hr.ericsson.etkca.repository.ProjectRepository;
import hr.ericsson.etkca.repository.search.ProjectSearchRepository;
import hr.ericsson.etkca.service.dto.ProjectDTO;
import hr.ericsson.etkca.service.mapper.ProjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Project}.
 */
@Service
@Transactional
public class ProjectService {

    private final Logger log = LoggerFactory.getLogger(ProjectService.class);

    private final ProjectRepository projectRepository;

    private final ProjectMapper projectMapper;

    private final ProjectSearchRepository projectSearchRepository;

    public ProjectService(ProjectRepository projectRepository, ProjectMapper projectMapper, ProjectSearchRepository projectSearchRepository) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
        this.projectSearchRepository = projectSearchRepository;
    }

    /**
     * Save a project.
     *
     * @param projectDTO the entity to save.
     * @return the persisted entity.
     */
    public ProjectDTO save(ProjectDTO projectDTO) {
        log.debug("Request to save Project : {}", projectDTO);
        Project project = projectMapper.toEntity(projectDTO);
        project = projectRepository.save(project);
        ProjectDTO result = projectMapper.toDto(project);
        projectSearchRepository.save(project);
        return result;
    }

    /**
     * Get all the projects.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ProjectDTO> findAll() {
        log.debug("Request to get all Projects");
        return projectRepository.findAllWithEagerRelationships().stream()
            .map(projectMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get all the projects with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ProjectDTO> findAllWithEagerRelationships(Pageable pageable) {
        return projectRepository.findAllWithEagerRelationships(pageable).map(projectMapper::toDto);
    }

    /**
     * Get one project by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProjectDTO> findOne(Long id) {
        log.debug("Request to get Project : {}", id);
        return projectRepository.findOneWithEagerRelationships(id)
            .map(projectMapper::toDto);
    }

    /**
     * Delete the project by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Project : {}", id);
        projectRepository.deleteById(id);
        projectSearchRepository.deleteById(id);
    }

    /**
     * Search for the project corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ProjectDTO> search(String query) {
        log.debug("Request to search Projects for query {}", query);
        return StreamSupport
            .stream(projectSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(projectMapper::toDto)
        .collect(Collectors.toList());
    }
}
