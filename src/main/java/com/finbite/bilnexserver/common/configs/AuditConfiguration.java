package com.finbite.bilnexserver.common.configs;

import com.finbite.bilnexserver.common.handlers.AuditorAwareHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Configuration for Auditing
 *
 * @author vinodjohn
 * @created 27.02.2025
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class AuditConfiguration {
    @Bean
    public AuditorAware<String> auditorAware() {
        return new AuditorAwareHandler();
    }
}
