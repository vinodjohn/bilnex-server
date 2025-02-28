package com.finbite.bilnexserver.common.handlers;

import com.finbite.bilnexserver.auth.configs.CustomUserDetails;
import com.finbite.bilnexserver.common.dtos.AuditorDto;
import org.springframework.data.domain.AuditorAware;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static com.finbite.bilnexserver.common.utils.Constants.Audit.DEFAULT_AUDITOR;

/**
 * Custom handler to implement AuditorAware
 *
 * @author vinodjohn
 * @created 27.02.2025
 */
public class AuditorAwareHandler implements AuditorAware<String> {
    @Override
    @NonNull
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals(
                "anonymousUser")) {
            return Optional.of(DEFAULT_AUDITOR);
        } else {
            AuditorDto auditorDto = (AuditorDto) authentication.getPrincipal();
            return Optional.of(auditorDto.getUsername());
        }
    }
}
