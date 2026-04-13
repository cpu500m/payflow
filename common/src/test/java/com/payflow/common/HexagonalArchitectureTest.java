package com.payflow.common;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.library.Architectures;

/**
 * @description  : hexagonal architecture Test base (예시)
 */
public abstract class HexagonalArchitectureTest {

	protected abstract String basePackage();

	@ArchTest
	void hexagonalArchitecture(JavaClasses classes) {
		Architectures.layeredArchitecture()
			.consideringAllDependencies()
			.layer("domain").definedBy(basePackage() + ".domain..")
			.layer("application").definedBy(basePackage() + ".application..")
			.layer("adapter").definedBy(basePackage() + ".adapter..")
			.whereLayer("domain").mayOnlyBeAccessedByLayers("application", "adapter")
			.whereLayer("application").mayOnlyBeAccessedByLayers("adapter")
			.whereLayer("adapter").mayNotBeAccessedByAnyLayer()
			.check(classes);
	}
}
