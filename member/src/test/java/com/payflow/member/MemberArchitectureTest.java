package com.payflow.member;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.library.Architectures;

/**
 * @description    :
 */
@AnalyzeClasses(packages = "com.payflow.member", importOptions = ImportOption.DoNotIncludeTests.class)
public class MemberArchitectureTest {

	@ArchTest
	void hexagonalArchitecture(JavaClasses classes) {
		Architectures.layeredArchitecture()
			.consideringAllDependencies()
			.layer("domain").definedBy("com.payflow.member.domain..")
			.layer("application").definedBy("com.payflow.member.application..")
			.layer("adapter").definedBy("com.payflow.member.adapter..")
			.whereLayer("domain").mayOnlyBeAccessedByLayers("application", "adapter")
			.whereLayer("application").mayOnlyBeAccessedByLayers("adapter")
			.whereLayer("adapter").mayNotBeAccessedByAnyLayer()
			.check(classes);
	}
}