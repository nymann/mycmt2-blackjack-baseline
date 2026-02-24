package com.jitterted.ebp.blackjack;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("architecture")
class ArchitectureTest {

    private static final JavaClasses classes = new ClassFileImporter()
            .withImportOption(new ImportOption.DoNotIncludeTests())
            .importPackages("com.jitterted.ebp.blackjack");

    @Test
    void no_direct_random_usage() {
        noClasses()
                .should()
                .dependOnClassesThat()
                .haveFullyQualifiedName("java.util.Random")
                .check(classes);
    }

    @Test
    void no_system_exit_calls() {
        noClasses()
                .should()
                .callMethod(System.class, "exit", int.class)
                .because("System.exit() should never be called in production code")
                .check(classes);
    }

    @Test
    void all_classes_must_reside_in_hexagonal_packages() {
        classes()
                .that()
                .haveNameNotMatching(".*Game.*")
                .should()
                .resideInAnyPackage("..domain..", "..application..", "..adapter..")
                .because(
                        "all production classes (except the entrypoint) must be organized into domain, application, or adapter packages")
                .check(classes);
    }

    @Test
    void domain_must_not_depend_on_adapters_or_application() {
        noClasses()
                .that()
                .resideInAPackage("..domain..")
                .should()
                .dependOnClassesThat()
                .resideInAnyPackage("..adapter..", "..application..")
                .allowEmptyShould(true)
                .because("domain must not depend on adapters or application")
                .check(classes);
    }

    @Test
    void application_must_not_depend_on_adapters() {
        noClasses()
                .that()
                .resideInAPackage("..application..")
                .should()
                .dependOnClassesThat()
                .resideInAPackage("..adapter..")
                .allowEmptyShould(true)
                .because("application must not depend on adapters")
                .check(classes);
    }
}
