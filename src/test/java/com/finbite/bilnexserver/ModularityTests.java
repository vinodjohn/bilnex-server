package com.finbite.bilnexserver;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

/**
 * Modularity Tests
 *
 * @author vinodjohn
 * @created 27.02.2025
 */
public class ModularityTests {
    static ApplicationModules modules = ApplicationModules.of(BilnexServerApplication.class);

    @Test
    void verifiesModularStructure() {
        modules.verify();
    }

    @Test
    void createModuleDocumentation() {
        new Documenter(modules).writeDocumentation();
    }
}
