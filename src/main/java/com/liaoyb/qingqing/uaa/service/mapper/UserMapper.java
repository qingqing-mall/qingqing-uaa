package com.liaoyb.qingqing.uaa.service.mapper;

import com.google.common.collect.Sets;
import com.liaoyb.qingqing.uaa.domain.Permission;
import com.liaoyb.qingqing.uaa.domain.Role;
import com.liaoyb.qingqing.uaa.domain.User;
import com.liaoyb.qingqing.uaa.repository.PermissionRepository;
import com.liaoyb.qingqing.uaa.repository.RoleRepository;
import com.liaoyb.qingqing.uaa.service.dto.UserDTO;
import com.liaoyb.qingqing.uaa.service.dto.UserWithAuthoritiesDTO;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mapper for the entity {@link User} and its DTO called {@link UserDTO}.
 * <p>
 * Normal mappers are generated using MapStruct, this one is hand-coded as MapStruct
 * support is still in beta, and requires a manual step with an IDE.
 */
@Service
public class UserMapper {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public UserMapper(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    public List<UserDTO> usersToUserDTOs(List<User> users) {
        return users.stream()
                .filter(Objects::nonNull)
                .map(this::userToUserDTO)
                .collect(Collectors.toList());
    }

    public UserDTO userToUserDTO(User user) {
        return new UserDTO(user);
    }

    public UserWithAuthoritiesDTO userToUserWithAuthoritiesDTO(User user) {
        UserWithAuthoritiesDTO userDTO = new UserWithAuthoritiesDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setNickname(user.getNickname());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhone(user.getPhone());
        userDTO.setImageUrl(user.getImageUrl());
        userDTO.setGender(user.getGender());
        userDTO.setBirthday(user.getBirthday());
        userDTO.setCity(user.getCity());
        userDTO.setLangKey(user.getLangKey());
        userDTO.setActiveStatus(user.getActiveStatus());
        userDTO.setActivationStatus(user.getActivationStatus());
        userDTO.setActivationKey(user.getActivationKey());
        userDTO.setResetKey(user.getResetKey());
        userDTO.setResetDate(user.getResetDate());

        userDTO.setCreatedBy(user.getCreatedBy());
        userDTO.setCreatedDate(user.getCreatedDate());
        userDTO.setLastModifiedBy(user.getLastModifiedBy());
        userDTO.setLastModifiedDate(user.getLastModifiedDate());

        Set<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
        userDTO.setRoles(roles);

        Set<Long> roleIds = user.getRoles().stream()
                .map(Role::getId)
                .collect(Collectors.toSet());

        //根据角色获取权限
        List<Permission> permissionList = roleIds.isEmpty() ? Collections.emptyList() : permissionRepository.findWithRelationships(roleIds);
        Set<String> permissions = permissionList.stream()
                .map(Permission::getName)
                .collect(Collectors.toSet());

        //角色、权限 转换为权限字符串
        Set<String> authorities = Sets.newHashSet(roles);
        authorities.addAll(permissions);
        userDTO.setAuthorities(authorities);

        return userDTO;
    }

    public List<User> userDTOsToUsers(List<UserDTO> userDTOs) {
        return userDTOs.stream()
                .filter(Objects::nonNull)
                .map(this::userDTOToUser)
                .collect(Collectors.toList());
    }

    public User userDTOToUser(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        } else {
            User user = new User();
            user.setId(userDTO.getId());
            user.setUsername(userDTO.getUsername());
            user.setNickname(userDTO.getNickname());
            user.setEmail(userDTO.getEmail());
            user.setPhone(userDTO.getPhone());
            user.setImageUrl(userDTO.getImageUrl());
            user.setGender(userDTO.getGender());
            user.setBirthday(userDTO.getBirthday());
            user.setCity(userDTO.getCity());
            user.setLangKey(userDTO.getLangKey());
            user.setActiveStatus(userDTO.getActiveStatus());
            user.setActivationStatus(userDTO.getActivationStatus());
            user.setActivationKey(userDTO.getActivationKey());
            user.setResetKey(userDTO.getResetKey());
            user.setResetDate(userDTO.getResetDate());

            user.setCreatedBy(userDTO.getCreatedBy());
            user.setCreatedDate(userDTO.getCreatedDate());
            user.setLastModifiedBy(userDTO.getLastModifiedBy());
            user.setLastModifiedDate(userDTO.getLastModifiedDate());

            Set<Role> roles = this.rolesFromStrings(userDTO.getRoles());
            user.setRoles(roles);
            return user;
        }
    }


    private Set<Role> rolesFromStrings(Set<String> rolesAsString) {
        Set<Role> roles = new HashSet<>();

        if (rolesAsString != null) {
            roles = rolesAsString.stream()
                    .map(roleRepository::findByName)
                    .filter(Optional::isPresent)
                    .map(Optional::get).collect(Collectors.toSet());
        }

        return roles;
    }

    public User userFromId(Long id) {
        if (id == null) {
            return null;
        }
        User user = new User();
        user.setId(id);
        return user;
    }
}
