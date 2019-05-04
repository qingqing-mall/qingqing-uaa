package com.liaoyb.qingqing.uaa.service.dto;

import lombok.Data;

import java.util.Set;

/**
 * 用户信息包含权限信息
 *
 * @author liaoyb
 */
@Data
public class UserWithAuthoritiesDTO extends UserDTO {
    /**
     * 权限、角色
     */
    private Set<String> authorities;
}
