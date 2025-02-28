package com.finbite.bilnexserver.common.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * Token Verification Request Event
 *
 * @author vinodjohn
 * @created 28.02.2025
 */
@Getter
public class EmailVerificationRequest extends ApplicationEvent {
    private final String email;
    private final String code;

    public EmailVerificationRequest(Object source, String email, String code) {
        super(source);
        this.email = email;
        this.code = code;
    }
}
