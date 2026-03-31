dependencies {
    // RabbitMQ
    implementation("org.springframework.boot:spring-boot-starter-amqp")

    // No JPA - notification service just sends and logs
    // Logs go to ELK via Logstash

    // Testcontainers
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:rabbitmq")
    testImplementation("org.springframework.amqp:spring-rabbit-test")
}
