package com.liaoyb.qingqing.uaa.web.rest;

import com.liaoyb.qingqing.uaa.service.UserReceiveAddressService;
import com.liaoyb.qingqing.uaa.web.rest.errors.BadRequestAlertException;
import com.liaoyb.qingqing.uaa.service.dto.UserReceiveAddressDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.liaoyb.qingqing.uaa.domain.UserReceiveAddress}.
 */
@RestController
@RequestMapping("/api")
public class UserReceiveAddressResource {

    private final Logger log = LoggerFactory.getLogger(UserReceiveAddressResource.class);

    private static final String ENTITY_NAME = "userReceiveAddress";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserReceiveAddressService userReceiveAddressService;

    public UserReceiveAddressResource(UserReceiveAddressService userReceiveAddressService) {
        this.userReceiveAddressService = userReceiveAddressService;
    }

    /**
     * {@code POST  /user-receive-addresses} : Create a new userReceiveAddress.
     *
     * @param userReceiveAddressDTO the userReceiveAddressDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userReceiveAddressDTO, or with status {@code 400 (Bad Request)} if the userReceiveAddress has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-receive-addresses")
    public ResponseEntity<UserReceiveAddressDTO> createUserReceiveAddress(@Valid @RequestBody UserReceiveAddressDTO userReceiveAddressDTO) throws URISyntaxException {
        log.debug("REST request to save UserReceiveAddress : {}", userReceiveAddressDTO);
        if (userReceiveAddressDTO.getId() != null) {
            throw new BadRequestAlertException("A new userReceiveAddress cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserReceiveAddressDTO result = userReceiveAddressService.save(userReceiveAddressDTO);
        return ResponseEntity.created(new URI("/api/user-receive-addresses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-receive-addresses} : Updates an existing userReceiveAddress.
     *
     * @param userReceiveAddressDTO the userReceiveAddressDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userReceiveAddressDTO,
     * or with status {@code 400 (Bad Request)} if the userReceiveAddressDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userReceiveAddressDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-receive-addresses")
    public ResponseEntity<UserReceiveAddressDTO> updateUserReceiveAddress(@Valid @RequestBody UserReceiveAddressDTO userReceiveAddressDTO) throws URISyntaxException {
        log.debug("REST request to update UserReceiveAddress : {}", userReceiveAddressDTO);
        if (userReceiveAddressDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        UserReceiveAddressDTO result = userReceiveAddressService.save(userReceiveAddressDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userReceiveAddressDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /user-receive-addresses} : get all the userReceiveAddresses.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userReceiveAddresses in body.
     */
    @GetMapping("/user-receive-addresses")
    public ResponseEntity<List<UserReceiveAddressDTO>> getAllUserReceiveAddresses(Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get a page of UserReceiveAddresses");
        Page<UserReceiveAddressDTO> page = userReceiveAddressService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /user-receive-addresses/:id} : get the "id" userReceiveAddress.
     *
     * @param id the id of the userReceiveAddressDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userReceiveAddressDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-receive-addresses/{id}")
    public ResponseEntity<UserReceiveAddressDTO> getUserReceiveAddress(@PathVariable Long id) {
        log.debug("REST request to get UserReceiveAddress : {}", id);
        Optional<UserReceiveAddressDTO> userReceiveAddressDTO = userReceiveAddressService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userReceiveAddressDTO);
    }

    /**
     * {@code DELETE  /user-receive-addresses/:id} : delete the "id" userReceiveAddress.
     *
     * @param id the id of the userReceiveAddressDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-receive-addresses/{id}")
    public ResponseEntity<Void> deleteUserReceiveAddress(@PathVariable Long id) {
        log.debug("REST request to delete UserReceiveAddress : {}", id);
        userReceiveAddressService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
