package com.liaoyb.qingqing.uaa.service.mapper;

import com.liaoyb.qingqing.uaa.domain.*;
import com.liaoyb.qingqing.uaa.service.dto.PermissionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Permission} and its DTO {@link PermissionDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PermissionMapper extends EntityMapper<PermissionDTO, Permission> {



    default Permission fromId(Long id) {
        if (id == null) {
            return null;
        }
        Permission permission = new Permission();
        permission.setId(id);
        return permission;
    }
}
