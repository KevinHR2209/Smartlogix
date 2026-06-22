package com.smartlogix.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ActiveProfiles("test")
class MsGatewayApplicationTests {

    @Test
    void contextLoads() {
        // Verifica que el contexto de Spring Boot levanta correctamente
    }
}
