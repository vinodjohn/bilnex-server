package com.finbite.bilnexserver.notification.models;

import com.finbite.bilnexserver.common.models.Auditable;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Email Verification model
 *
 * @author vinodjohn
 * @created 28.02.2025
 */
@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class EmailVerification extends Auditable<String> {
    @Id
    @Column(updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String email;
    private String verificationCode;
    private LocalDateTime expirationTime;
}
