plugins {
    java
    id("org.springframework.boot") version "4.0.3" apply false
    id("io.spring.dependency-management") version "1.1.7" apply false
}

allprojects {
    group = "com.payflow"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(25))
        }
    }

    configurations {
        compileOnly {
            extendsFrom(configurations.annotationProcessor.get())
        }
    }

    // 공통 모듈 참조
    if (name != "common") {
        dependencies {
            implementation(project(":common"))
        }
    }

    dependencies {
        
        // Spring Boot 공통
        implementation("org.springframework.boot:spring-boot-starter-web")
        implementation("org.springframework.boot:spring-boot-starter-validation")
        implementation("org.springframework.boot:spring-boot-starter-actuator")

        // Lombok
        compileOnly("org.projectlombok:lombok")
        annotationProcessor("org.projectlombok:lombok")

        //JPA
        implementation("org.springframework.boot:spring-boot-starter-data-jpa")

        // Test
        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation(platform("org.testcontainers:testcontainers-bom:1.21.0"))
        testRuntimeOnly("org.junit.platform:junit-platform-launcher")

        // 아키텍쳐 검증
        testImplementation("com.tngtech.archunit:archunit-junit5:1.4.1")
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}
