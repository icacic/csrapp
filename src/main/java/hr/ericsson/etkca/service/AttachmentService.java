package hr.ericsson.etkca.service;

import hr.ericsson.etkca.domain.Attachment;
import hr.ericsson.etkca.repository.AttachmentRepository;
import hr.ericsson.etkca.repository.search.AttachmentSearchRepository;
import hr.ericsson.etkca.service.dto.AttachmentDTO;
import hr.ericsson.etkca.service.mapper.AttachmentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Attachment}.
 */
@Service
@Transactional
public class AttachmentService {

    private final Logger log = LoggerFactory.getLogger(AttachmentService.class);

    private final AttachmentRepository attachmentRepository;

    private final AttachmentMapper attachmentMapper;

    private final AttachmentSearchRepository attachmentSearchRepository;

    public AttachmentService(AttachmentRepository attachmentRepository, AttachmentMapper attachmentMapper, AttachmentSearchRepository attachmentSearchRepository) {
        this.attachmentRepository = attachmentRepository;
        this.attachmentMapper = attachmentMapper;
        this.attachmentSearchRepository = attachmentSearchRepository;
    }

    /**
     * Save a attachment.
     *
     * @param attachmentDTO the entity to save.
     * @return the persisted entity.
     */
    public AttachmentDTO save(AttachmentDTO attachmentDTO) {
        log.debug("Request to save Attachment : {}", attachmentDTO);
        Attachment attachment = attachmentMapper.toEntity(attachmentDTO);
        attachment = attachmentRepository.save(attachment);
        AttachmentDTO result = attachmentMapper.toDto(attachment);
        attachmentSearchRepository.save(attachment);
        return result;
    }

    /**
     * Get all the attachments.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<AttachmentDTO> findAll() {
        log.debug("Request to get all Attachments");
        return attachmentRepository.findAll().stream()
            .map(attachmentMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one attachment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AttachmentDTO> findOne(Long id) {
        log.debug("Request to get Attachment : {}", id);
        return attachmentRepository.findById(id)
            .map(attachmentMapper::toDto);
    }

    /**
     * Delete the attachment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Attachment : {}", id);
        attachmentRepository.deleteById(id);
        attachmentSearchRepository.deleteById(id);
    }

    /**
     * Search for the attachment corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<AttachmentDTO> search(String query) {
        log.debug("Request to search Attachments for query {}", query);
        return StreamSupport
            .stream(attachmentSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(attachmentMapper::toDto)
        .collect(Collectors.toList());
    }
}
