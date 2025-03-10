package com.finbite.bilnexserver.auth.repositories;

import com.finbite.bilnexserver.auth.models.TokenRefresh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository to handle Refresh Token related data queries
 *
 * @author vinodjohn
 * @created 27.02.2025
 */
@Repository
public interface TokenRefreshRepository extends PagingAndSortingRepository<TokenRefresh, UUID>,
        JpaRepository<TokenRefresh, UUID> {
    Optional<TokenRefresh> findByToken(String token);

    @Query(value = "SELECT * FROM token_refresh tr WHERE tr.person_id = :personId AND tr.is_active = true ORDER BY tr" +
            ".created_date DESC LIMIT 1", nativeQuery = true)
    Optional<TokenRefresh> findLatestActiveTokenByPerson(@Param("personId") UUID personId);
}

