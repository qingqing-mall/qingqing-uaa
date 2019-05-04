package com.liaoyb.qingqing.uaa.service.mapper;

import com.liaoyb.qingqing.uaa.domain.*;
import com.liaoyb.qingqing.uaa.service.dto.UserReceiveAddressDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserReceiveAddress} and its DTO {@link UserReceiveAddressDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface UserReceiveAddressMapper extends EntityMapper<UserReceiveAddressDTO, UserReceiveAddress> {



    default UserReceiveAddress fromId(Long id) {
        if (id == null) {
            return null;
        }
        UserReceiveAddress userReceiveAddress = new UserReceiveAddress();
        userReceiveAddress.setId(id);
        return userReceiveAddress;
    }
}
