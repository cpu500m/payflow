package com.payflow.merchant;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.library.Architectures;

/**
 * @description  :
 */
@AnalyzeClasses(packages = "com.payflow.merchant", importOptions = ImportOption.DoNotIncludeTests.class)
public class MerchantArchitectureTest {

	@ArchTest
	void hexagonalArchitecture(JavaClasses classes) {
		Architectures.layeredArchitecture()
			.consideringAllDependencies()
			.layer("domain").definedBy("com.payflow.merchant.domain..")
			.layer("application").definedBy("com.payflow.merchant.application..")
			.layer("adapter").definedBy("com.payflow.merchant.adapter..")
			.whereLayer("domain").mayOnlyBeAccessedByLayers("application", "adapter")
			.whereLayer("application").mayOnlyBeAccessedByLayers("adapter")
			.whereLayer("adapter").mayNotBeAccessedByAnyLayer()
			.check(classes);
	}
}