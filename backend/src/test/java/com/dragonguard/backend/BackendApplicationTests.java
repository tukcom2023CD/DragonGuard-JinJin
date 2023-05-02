package com.dragonguard.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@EmbeddedKafka(brokerProperties = {"listeners=PLAINTEXT://localhost:9092"}, ports = 9092)
@TestPropertySource(properties = {"spring.config.location=classpath:application-test.yml"})
class BackendApplicationTests {
    @Test
    void contextLoads() {
    }
}
