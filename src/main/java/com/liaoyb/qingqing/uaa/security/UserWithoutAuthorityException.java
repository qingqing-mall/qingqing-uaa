package com.liaoyb.qingqing.uaa.security;

import org.springframework.security.core.AuthenticationException;

/**
 * This exception is thrown in case of a user without role trying to authenticate.
 */
public class UserWithoutAuthorityException extends AuthenticationException {

    private static final long serialVersionUID = 1L;

    public UserWithoutAuthorityException(String message) {
        super(message);
    }

    public UserWithoutAuthorityException(String message, Throwable t) {
        super(message, t);
    }
}
