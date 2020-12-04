package hr.ericsson.etkca.service;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hr.ericsson.etkca.domain.HDUser;
import hr.ericsson.etkca.domain.User;
import hr.ericsson.etkca.repository.HDUserRepository;
import hr.ericsson.etkca.repository.search.HDUserSearchRepository;
import hr.ericsson.etkca.security.AuthoritiesConstants;
import hr.ericsson.etkca.service.dto.HDUserDTO;
import hr.ericsson.etkca.service.dto.UserDTO;
import hr.ericsson.etkca.service.mapper.HDUserMapper;

/**
 * Service Implementation for managing {@link HDUser}.
 */
@Service
@Transactional
public class HDUserService {

    private final Logger log = LoggerFactory.getLogger(HDUserService.class);

    private final HDUserRepository hDUserRepository;

    private final HDUserMapper hDUserMapper;

    private final HDUserSearchRepository hDUserSearchRepository;
    
    @Autowired
    private UserService userService;

    public HDUserService(HDUserRepository hDUserRepository, HDUserMapper hDUserMapper, HDUserSearchRepository hDUserSearchRepository) {
        this.hDUserRepository = hDUserRepository;
        this.hDUserMapper = hDUserMapper;
        this.hDUserSearchRepository = hDUserSearchRepository;
    }

    /**
     * Save a hDUser.
     *
     * @param hDUserDTO the entity to save.
     * @return the persisted entity.
     */
    public HDUserDTO save(HDUserDTO hDUserDTO) {
        log.debug("Request to save HDUser : {}", hDUserDTO);
        HDUser hDUser = hDUserMapper.toEntity(hDUserDTO);
        hDUser = hDUserRepository.save(hDUser);
        HDUserDTO result = hDUserMapper.toDto(hDUser);
        hDUserSearchRepository.save(hDUser);
        
        UserDTO u = new UserDTO();
        u.setFirstName(hDUserDTO.getFirstName());
        u.setLastName(hDUserDTO.getLastName());
        u.setEmail(hDUserDTO.getEmail());
        u.setLogin(hDUserDTO.getFirstName().toLowerCase().charAt(0) + hDUserDTO.getLastName().toLowerCase());
        //u.setPassword(u.getLogin() + "123");
        u.setActivated(true);
        Set<String> authorites = new HashSet<>();
        authorites.add(AuthoritiesConstants.USER);        
        u.setAuthorities(authorites);
        
        User user = userService.registerUser(u, u.getLogin()+"123");
        
        hDUser.setUser(user);
        hDUser = hDUserRepository.save(hDUser);
        return result;
    }

    /**
     * Get all the hDUsers.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<HDUserDTO> findAll() {
        log.debug("Request to get all HDUsers");
        return hDUserRepository.findAll().stream()
            .map(hDUserMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one hDUser by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<HDUserDTO> findOne(Long id) {
        log.debug("Request to get HDUser : {}", id);
        return hDUserRepository.findById(id)
            .map(hDUserMapper::toDto);
    }

    /**
     * Delete the hDUser by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete HDUser : {}", id);
        hDUserRepository.deleteById(id);
        hDUserSearchRepository.deleteById(id);
    }

    /**
     * Search for the hDUser corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<HDUserDTO> search(String query) {
        log.debug("Request to search HDUsers for query {}", query);
        return StreamSupport
            .stream(hDUserSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(hDUserMapper::toDto)
        .collect(Collectors.toList());
    }
}
