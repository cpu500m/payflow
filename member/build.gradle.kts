dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    // security
    implementation("org.springframework.boot:spring-boot-starter-security")

    runtimeOnly("com.mysql:mysql-connector-j")

    // test
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:mysql")
}
