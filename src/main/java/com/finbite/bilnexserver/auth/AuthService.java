package com.finbite.bilnexserver.auth;

import java.util.Map;

/**
 * Service to handle Google Authentication
 *
 * @author vinodjohn
 * @created 04.03.2025
 */
public interface AuthService {
    /**
     * To handle Google login
     *
     * @param idToken token
     * @return Map of object
     */
    Map<String, Object> handleGoogleLogin(String idToken);

    /**
     * To get a common google password for Google users
     *
     * @return Password
     */
    String getGooglePassword();
}
