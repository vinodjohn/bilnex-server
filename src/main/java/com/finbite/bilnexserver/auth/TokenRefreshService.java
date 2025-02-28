package com.finbite.bilnexserver.auth;

import com.finbite.bilnexserver.auth.exceptions.PersonNotFoundException;
import com.finbite.bilnexserver.auth.exceptions.TokenRefreshException;
import com.finbite.bilnexserver.auth.models.TokenRefresh;

import java.util.UUID;

/**
 * Service to handle Token Refresh related operations
 *
 * @author vinodjohn
 * @created 27.02.2025
 */
public interface TokenRefreshService {
    /**
     * To create a new Token Refresh
     *
     * @param personId ID of the person
     * @return Token Refresh obj
     */
    TokenRefresh createRefreshToken(UUID personId) throws PersonNotFoundException;

    /**
     * To find Token Refresh by ID
     *
     * @param id ID of Token Refresh
     * @return Token Refresh Object
     */
    TokenRefresh findTokenRefreshById(UUID id);

    /**
     * To find Token Refresh by Token
     *
     * @param token Token
     * @return Token Refresh
     */
    TokenRefresh findTokenRefreshByToken(String token);

    /**
     * To verify the expiration of the token
     *
     * @param tokenRefresh Token Refersh obj
     * @return TokenRefresh obj
     */
    TokenRefresh verifyTokenExpiry(TokenRefresh tokenRefresh);


    /**
     * To delete a token refresh by ID
     *
     * @param id Token Refresh ID
     */
    void deleteTokenRefreshById(UUID id) throws TokenRefreshException;

    /**
     * To delete a token refresh by Person
     *
     * @param personId Person's ID
     */
    void deleteTokenRefreshByPersonId(UUID personId);

}
