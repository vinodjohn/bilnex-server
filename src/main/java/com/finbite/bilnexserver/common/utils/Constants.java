package com.finbite.bilnexserver.common.utils;

/**
 * Constant values used in this application
 *
 * @author vinodjohn
 * @created 27.02.2025
 */
public class Constants {
    public static class Data {
        public static final String DEFAULT_ITEMS_PER_PAGE = "10";
    }

    public static class Audit {
        public static final String DEFAULT_AUDITOR = "SYSTEM";
        public static final String ROLE_ADMIN = "ADMIN";
        public static final String ROLE_USER = "USER";
    }

    public static class Events {
        public static final String EMAIL_VERIFICATION = "email-verification-topic";
    }
}
