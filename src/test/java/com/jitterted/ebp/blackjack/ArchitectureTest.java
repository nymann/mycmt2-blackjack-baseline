package com.jitterted.ebp.blackjack;

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
}
