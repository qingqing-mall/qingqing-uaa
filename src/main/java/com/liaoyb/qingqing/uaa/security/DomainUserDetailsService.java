package com.liaoyb.qingqing.uaa.security;

import com.liaoyb.qingqing.security.Userdetail;
import com.liaoyb.qingqing.uaa.domain.User;
import com.liaoyb.qingqing.uaa.repository.UserRepository;
import com.liaoyb.qingqing.uaa.service.dto.UserWithAuthoritiesDTO;
import com.liaoyb.qingqing.uaa.service.mapper.UserMapper;
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Authenticate a user from the database.
 */
@Component("userDetailsService")
public class DomainUserDetailsService implements UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(DomainUserDetailsService.class);

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public DomainUserDetailsService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String login) {
        log.debug("Authenticating {}", login);

        if (new EmailValidator().isValid(login, null)) {
            return userRepository.findOneWithRolesByEmail(login)
                    .map(user -> createSpringSecurityUser(login, user))
                    .orElseThrow(() -> new UsernameNotFoundException("User with email " + login + " was not found in the database"));
        }

        String lowercaseLogin = login.toLowerCase(Locale.ENGLISH);
        return userRepository.findOneWithRolesByUsername(lowercaseLogin)
                .map(user -> createSpringSecurityUser(lowercaseLogin, user))
                .orElseThrow(() -> new UsernameNotFoundException("User " + lowercaseLogin + " was not found in the database"));

    }

    private org.springframework.security.core.userdetails.User createSpringSecurityUser(String lowercaseLogin, User user) {
        if (!user.activated()) {
            throw new UserNotActivatedException("User " + lowercaseLogin + " was not activated");
        }
        //用户权限
        UserWithAuthoritiesDTO userWithAuthoritiesDTO = userMapper.userToUserWithAuthoritiesDTO(user);
        List<GrantedAuthority> grantedAuthorities = userWithAuthoritiesDTO.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority))
                .collect(Collectors.toList());

        if(grantedAuthorities.isEmpty()){
            throw new UserWithoutAuthorityException("User " + lowercaseLogin + " without authorities");
        }

        return new Userdetail(user.getId(), user.getUsername(),
                user.getPassword(),
                grantedAuthorities);
    }
}
