package com.liaoyb.qingqing.uaa.service;

import com.liaoyb.qingqing.uaa.domain.UserReceiveAddress;
import com.liaoyb.qingqing.uaa.repository.UserReceiveAddressRepository;
import com.liaoyb.qingqing.uaa.service.dto.UserReceiveAddressDTO;
import com.liaoyb.qingqing.uaa.service.mapper.UserReceiveAddressMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link UserReceiveAddress}.
 */
@Service
@Transactional
public class UserReceiveAddressService {

    private final Logger log = LoggerFactory.getLogger(UserReceiveAddressService.class);

    private final UserReceiveAddressRepository userReceiveAddressRepository;

    private final UserReceiveAddressMapper userReceiveAddressMapper;

    public UserReceiveAddressService(UserReceiveAddressRepository userReceiveAddressRepository, UserReceiveAddressMapper userReceiveAddressMapper) {
        this.userReceiveAddressRepository = userReceiveAddressRepository;
        this.userReceiveAddressMapper = userReceiveAddressMapper;
    }

    /**
     * Save a userReceiveAddress.
     *
     * @param userReceiveAddressDTO the entity to save.
     * @return the persisted entity.
     */
    public UserReceiveAddressDTO save(UserReceiveAddressDTO userReceiveAddressDTO) {
        log.debug("Request to save UserReceiveAddress : {}", userReceiveAddressDTO);
        UserReceiveAddress userReceiveAddress = userReceiveAddressMapper.toEntity(userReceiveAddressDTO);
        userReceiveAddress = userReceiveAddressRepository.save(userReceiveAddress);
        return userReceiveAddressMapper.toDto(userReceiveAddress);
    }

    /**
     * Get all the userReceiveAddresses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<UserReceiveAddressDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserReceiveAddresses");
        return userReceiveAddressRepository.findAll(pageable)
            .map(userReceiveAddressMapper::toDto);
    }


    /**
     * Get one userReceiveAddress by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UserReceiveAddressDTO> findOne(Long id) {
        log.debug("Request to get UserReceiveAddress : {}", id);
        return userReceiveAddressRepository.findById(id)
            .map(userReceiveAddressMapper::toDto);
    }

    /**
     * Delete the userReceiveAddress by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete UserReceiveAddress : {}", id);
        userReceiveAddressRepository.deleteById(id);
    }
}
