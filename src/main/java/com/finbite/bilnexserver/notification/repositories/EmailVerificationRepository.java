package com.finbite.bilnexserver.notification.repositories;

import com.finbite.bilnexserver.notification.models.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Repository to handle Email Verification related queries
 *
 * @author vinodjohn
 * @created 28.02.2025
 */
@Repository
public interface EmailVerificationRepository extends PagingAndSortingRepository<EmailVerification, UUID>,
        JpaRepository<EmailVerification, UUID> {
}
