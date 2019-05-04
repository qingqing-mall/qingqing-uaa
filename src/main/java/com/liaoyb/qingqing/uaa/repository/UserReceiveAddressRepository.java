package com.liaoyb.qingqing.uaa.repository;

import com.liaoyb.qingqing.uaa.domain.UserReceiveAddress;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the UserReceiveAddress entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserReceiveAddressRepository extends JpaRepository<UserReceiveAddress, Long> {

}
