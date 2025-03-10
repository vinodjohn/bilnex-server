package com.finbite.bilnexserver.auth.implementations;

import com.finbite.bilnexserver.auth.PersonService;
import com.finbite.bilnexserver.auth.TokenRefreshService;
import com.finbite.bilnexserver.auth.exceptions.PersonNotFoundException;
import com.finbite.bilnexserver.auth.exceptions.TokenRefreshException;
import com.finbite.bilnexserver.auth.models.TokenRefresh;
import com.finbite.bilnexserver.auth.repositories.TokenRefreshRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of TokenRefreshService
 *
 * @author vinodjohn
 * @created 27.02.2025
 */
@Service
@Transactional
@RequiredArgsConstructor
public class TokenRefreshServiceImpl implements TokenRefreshService {
    private final TokenRefreshRepository tokenRefreshRepository;

    private final PersonService personService;

    @Value("${bilnex.app.jwtRefreshExpirationSec}")
    private int tokenRefreshDuration;

    @Override
    public TokenRefresh createRefreshToken(UUID personId) throws PersonNotFoundException {
        TokenRefresh tokenRefresh = new TokenRefresh();
        tokenRefresh.setToken(UUID.randomUUID().toString());
        tokenRefresh.setEndTime(LocalDateTime.now().plusSeconds(tokenRefreshDuration));
        tokenRefresh.setPerson(personService.findPersonById(personId));
        tokenRefresh.setActive(true);

        return tokenRefreshRepository.saveAndFlush(tokenRefresh);
    }

    @Override
    public TokenRefresh findTokenRefreshById(UUID id) {
        Optional<TokenRefresh> tokenRefresh = tokenRefreshRepository.findById(id);

        if (tokenRefresh.isEmpty()) {
            throw new TokenRefreshException(id);
        }

        return tokenRefresh.get();
    }

    @Override
    public TokenRefresh findTokenRefreshByToken(String token) {
        Optional<TokenRefresh> tokenRefresh = tokenRefreshRepository.findByToken(token);

        if (tokenRefresh.isEmpty()) {
            throw new TokenRefreshException(token);
        }

        return tokenRefresh.get();
    }

    @Override
    public TokenRefresh verifyTokenExpiry(TokenRefresh tokenRefresh) {
        if (tokenRefresh.getEndTime().isBefore(LocalDateTime.now())) {
            deleteTokenRefreshById(tokenRefresh.getId());
            throw new TokenRefreshException(tokenRefresh.getToken(), "Refresh token was expired. Please sign in again");
        }

        return tokenRefresh;
    }

    @Override
    public void deleteTokenRefreshById(UUID id) throws TokenRefreshException {
        TokenRefresh tokenRefresh = findTokenRefreshById(id);
        tokenRefresh.setActive(false);
        tokenRefreshRepository.saveAndFlush(tokenRefresh);
    }

    @Override
    public void deleteTokenRefreshByPersonId(UUID personId) {
        Optional<TokenRefresh> tokenRefresh = tokenRefreshRepository.findLatestActiveTokenByPerson(personId);

        if (tokenRefresh.isEmpty()) {
            throw new RuntimeException(MessageFormat.format("No active token refresh found for person id {0}",
                    personId));
        }

        TokenRefresh tokenRefreshToDelete = tokenRefresh.get();
        tokenRefreshToDelete.setActive(false);
        tokenRefreshRepository.saveAndFlush(tokenRefreshToDelete);
    }
}
