package com.liaoyb.qingqing.uaa.service;

import com.liaoyb.qingqing.uaa.config.Constants;
import com.liaoyb.qingqing.uaa.domain.Role;
import com.liaoyb.qingqing.uaa.domain.User;
import com.liaoyb.qingqing.uaa.enums.UserActivationStatus;
import com.liaoyb.qingqing.uaa.enums.UserActiveStatus;
import com.liaoyb.qingqing.uaa.repository.RoleRepository;
import com.liaoyb.qingqing.uaa.repository.UserRepository;
import com.liaoyb.qingqing.uaa.security.AuthoritiesConstants;
import com.liaoyb.qingqing.uaa.security.SecurityUtils;
import com.liaoyb.qingqing.uaa.service.dto.UserDTO;
import com.liaoyb.qingqing.uaa.service.mapper.UserMapper;
import com.liaoyb.qingqing.uaa.service.util.RandomUtil;
import com.liaoyb.qingqing.uaa.web.rest.errors.EmailAlreadyUsedException;
import com.liaoyb.qingqing.uaa.web.rest.errors.InvalidPasswordException;
import com.liaoyb.qingqing.uaa.web.rest.errors.UsernameAlreadyUsedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;

    private final CacheManager cacheManager;

    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository, CacheManager cacheManager, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.cacheManager = cacheManager;
        this.userMapper = userMapper;
    }

    public Optional<User> activateRegistration(String key) {
        log.debug("Activating user for activation key {}", key);
        return userRepository.findOneByActivationKey(key)
                .map(user -> {
                    // activate given user for the registration key.
                    user.setActivationStatus(UserActivationStatus.ACTIVATED.getValue());
                    user.setActivationKey(null);
                    this.clearUserCaches(user);
                    log.debug("Activated user: {}", user);
                    return user;
                });
    }

    public Optional<User> completePasswordReset(String newPassword, String key) {
        log.debug("Reset user password for reset key {}", key);
        return userRepository.findOneByResetKey(key)
                .filter(user -> user.getResetDate().isAfter(Instant.now().minusSeconds(86400)))
                .map(user -> {
                    user.setPassword(passwordEncoder.encode(newPassword));
                    user.setResetKey(null);
                    user.setResetDate(null);
                    this.clearUserCaches(user);
                    return user;
                });
    }

    public Optional<User> requestPasswordReset(String mail) {
        return userRepository.findOneByEmailIgnoreCase(mail)
                .filter(User::activated)
                .map(user -> {
                    user.setResetKey(RandomUtil.generateResetKey());
                    user.setResetDate(Instant.now());
                    this.clearUserCaches(user);
                    return user;
                });
    }

    public User registerUser(UserDTO userDTO, String password) {
        userRepository.findOneByUsername(userDTO.getUsername().toLowerCase()).ifPresent(existingUser -> {
            boolean removed = removeNonActivatedUser(existingUser);
            if (!removed) {
                throw new UsernameAlreadyUsedException();
            }
        });
        userRepository.findOneByEmailIgnoreCase(userDTO.getEmail()).ifPresent(existingUser -> {
            boolean removed = removeNonActivatedUser(existingUser);
            if (!removed) {
                throw new EmailAlreadyUsedException();
            }
        });
        User newUser = new User();
        newUser.setUsername(userDTO.getUsername().toLowerCase());
        newUser.setNickname(userDTO.getNickname());
        newUser.setEmail(userDTO.getEmail().toLowerCase());
        // new user gets initially a generated password
        String encryptedPassword = passwordEncoder.encode(RandomUtil.generatePassword());
        newUser.setPassword(encryptedPassword);
        newUser.setPhone(userDTO.getPhone());
        newUser.setImageUrl(userDTO.getImageUrl());
        newUser.setGender(userDTO.getGender());
        newUser.setBirthday(userDTO.getBirthday());
        newUser.setCity(userDTO.getCity());
        newUser.setLangKey(userDTO.getLangKey());

        newUser.setActiveStatus(UserActiveStatus.ENABLED.getValue());
        // new user is not active
        newUser.setActivationStatus(UserActivationStatus.NOT_ACTIVATED.getValue());
        // new user gets registration key
        newUser.setActivationKey(RandomUtil.generateActivationKey());
        Set<Role> roles = new HashSet<>();
        roleRepository.findByName(AuthoritiesConstants.USER).ifPresent(roles::add);
        newUser.setRoles(roles);
        userRepository.save(newUser);
        this.clearUserCaches(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    private boolean removeNonActivatedUser(User existingUser) {
        if (existingUser.activated()) {
            return false;
        }
        userRepository.delete(existingUser);
        userRepository.flush();
        this.clearUserCaches(existingUser);
        return true;
    }

    public User createUser(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername().toLowerCase());
        user.setNickname(userDTO.getNickname());
        user.setEmail(userDTO.getEmail().toLowerCase());
        String encryptedPassword = passwordEncoder.encode(RandomUtil.generatePassword());
        user.setPassword(encryptedPassword);
        user.setPhone(userDTO.getPhone());
        user.setImageUrl(userDTO.getImageUrl());
        user.setGender(userDTO.getGender());
        user.setBirthday(userDTO.getBirthday());
        user.setCity(userDTO.getCity());

        if (userDTO.getLangKey() == null) {
            user.setLangKey(Constants.DEFAULT_LANGUAGE); // default language
        } else {
            user.setLangKey(userDTO.getLangKey());
        }
        user.setActiveStatus(UserActiveStatus.ENABLED.getValue());
        //未激活
        user.setActivationStatus(UserActivationStatus.NOT_ACTIVATED.getValue());

        user.setResetKey(RandomUtil.generateResetKey());
        user.setResetDate(Instant.now());
        if (userDTO.getRoles() != null) {
            Set<Role> roles = userDTO.getRoles().stream()
                    .map(roleRepository::findByName)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toSet());
            user.setRoles(roles);
        }
        userRepository.save(user);
        this.clearUserCaches(user);
        log.debug("Created Information for User: {}", user);
        return user;
    }

    /**
     * Update basic information (first name, last name, email, language) for the current user.
     *
     * @param nickname first name of user
     * @param email    email id of user
     * @param phone    phone
     * @param imageUrl image URL of user
     * @param gender   gender of user
     * @param birthday birthday of user
     */
    public void updateUser(String nickname, String email, String phone, String imageUrl, Integer gender, Instant birthday, String city, String langKey) {
        SecurityUtils.getCurrentUserId()
                .flatMap(userRepository::findById)
                .ifPresent(user -> {
                    user.setNickname(nickname);
                    user.setEmail(email.toLowerCase());
                    user.setPhone(phone);
                    user.setImageUrl(imageUrl);
                    user.setGender(gender);
                    user.setBirthday(birthday);
                    user.setCity(city);
                    user.setLangKey(langKey);
                    this.clearUserCaches(user);
                    log.debug("Changed Information for User: {}", user);
                });
    }

    /**
     * Update all information for a specific user, and return the modified user.
     *
     * @param userDTO user to update
     * @return updated user
     */
    public Optional<UserDTO> updateUser(UserDTO userDTO) {
        return Optional.of(userRepository
                .findById(userDTO.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(user -> {
                    this.clearUserCaches(user);
                    user.setUsername(userDTO.getUsername().toLowerCase());
                    user.setNickname(userDTO.getNickname());
                    user.setEmail(userDTO.getEmail().toLowerCase());
                    user.setPhone(userDTO.getPhone());
                    user.setImageUrl(userDTO.getImageUrl());
                    user.setGender(userDTO.getGender());
                    user.setBirthday(userDTO.getBirthday());
                    user.setCity(userDTO.getCity());
                    Set<Role> managedRoles = user.getRoles();
                    managedRoles.clear();
                    userDTO.getRoles().stream()
                            .map(roleRepository::findByName)
                            .filter(Optional::isPresent)
                            .map(Optional::get)
                            .forEach(managedRoles::add);
                    this.clearUserCaches(user);
                    log.debug("Changed Information for User: {}", user);
                    return user;
                })
                .map(userMapper::userToUserDTO);
    }

    public void deleteUser(long userId) {
        userRepository.findById(userId).ifPresent(user -> {
            userRepository.delete(user);
            this.clearUserCaches(user);
            log.debug("Deleted User: {}", user);
        });
    }

    public void changePassword(String currentClearTextPassword, String newPassword) {
        SecurityUtils.getCurrentUserId()
                .flatMap(userRepository::findById)
                .ifPresent(user -> {
                    String currentEncryptedPassword = user.getPassword();
                    if (!passwordEncoder.matches(currentClearTextPassword, currentEncryptedPassword)) {
                        throw new InvalidPasswordException();
                    }
                    String encryptedPassword = passwordEncoder.encode(newPassword);
                    user.setPassword(encryptedPassword);
                    this.clearUserCaches(user);
                    log.debug("Changed password for User: {}", user);
                });
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> getAllManagedUsers(Pageable pageable) {
        return userRepository.findAllByUsernameNot(pageable, Constants.ANONYMOUS_USER).map(UserDTO::new);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthoritiesByLogin(String login) {
        return userRepository.findOneWithRolesByUsername(login);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthoritiesByUserId(long userId) {
        return userRepository.findOneWithRolesById(userId);
    }

    @Transactional(readOnly = true)
    public Optional<User> getCurrentUserWithRoles() {
        return SecurityUtils.getCurrentUserId().flatMap(userRepository::findOneWithRolesById);
    }

    /**
     * Not activated users should be automatically deleted after 3 days.
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void removeNotActivatedUsers() {
        userRepository
                .findAllByActivationStatusAndCreatedDateBefore(UserActivationStatus.NOT_ACTIVATED.getValue(), Instant.now().minus(3, ChronoUnit.DAYS))
                .forEach(user -> {
                    log.debug("Deleting not activated user {}", user.getUsername());
                    userRepository.delete(user);
                    this.clearUserCaches(user);
                });
    }

    /**
     * Gets a list of all the authorities.
     *
     * @return a list of all the authorities.
     */
    public List<String> getAuthorities() {
        //todo
        return roleRepository.findAll().stream().map(Role::getName).collect(Collectors.toList());
    }

    private void clearUserCaches(User user) {
        Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_USERNAME_CACHE)).evict(user.getUsername());
        Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE)).evict(user.getEmail());
    }
}
