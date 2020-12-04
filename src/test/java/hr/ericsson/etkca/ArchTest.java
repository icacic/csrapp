package hr.ericsson.etkca;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {

        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("hr.ericsson.etkca");

        noClasses()
            .that()
                .resideInAnyPackage("hr.ericsson.etkca.service..")
            .or()
                .resideInAnyPackage("hr.ericsson.etkca.repository..")
            .should().dependOnClassesThat()
                .resideInAnyPackage("..hr.ericsson.etkca.web..")
        .because("Services and repositories should not depend on web layer")
        .check(importedClasses);
    }
}
