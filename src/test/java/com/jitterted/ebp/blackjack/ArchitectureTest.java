package com.jitterted.ebp.blackjack;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Tag;

@Tag("architecture")
@AnalyzeClasses(packages = "com.jitterted.ebp.blackjack", importOptions = ImportOption.DoNotIncludeTests.class)
class ArchitectureTest {

    @ArchTest
    static final ArchRule no_direct_random_usage =
            noClasses().should().dependOnClassesThat().haveFullyQualifiedName("java.util.Random");

    @ArchTest
    static final ArchRule no_system_exit_calls = noClasses()
            .should()
            .callMethod(System.class, "exit", int.class)
            .because("System.exit() should never be called in production code");

    @ArchTest
    static final ArchRule all_classes_must_reside_in_hexagonal_packages = classes()
            .that()
            .doNotHaveFullyQualifiedName("com.jitterted.ebp.blackjack.Game")
            .should()
            .resideInAnyPackage(
                    "..domain..", "..application..", "..adapter..")
            .because("all production classes (except the entrypoint) must be organized into domain, application, or adapter packages");

    @ArchTest
    static final ArchRule domain_must_not_depend_on_adapters_or_application = noClasses()
            .that()
            .resideInAPackage("..domain..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..adapter..", "..application..")
            .allowEmptyShould(true)
            .because("domain must not depend on adapters or application");

    @ArchTest
    static final ArchRule application_must_not_depend_on_adapters = noClasses()
            .that()
            .resideInAPackage("..application..")
            .should()
            .dependOnClassesThat()
            .resideInAPackage("..adapter..")
            .allowEmptyShould(true)
            .because("application must not depend on adapters");
}
