package com.smartlogix.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ActiveProfiles("test")
@Import(GatewaySmokeTest.TestConfig.class)
class GatewaySmokeTest {

    @TestConfiguration
    static class TestConfig {
        @Bean
        public RouteLocator testRouteLocator(RouteLocatorBuilder builder) {
            return builder.routes()
                    .route("ms-clientes", r -> r
                            .path("/api/clientes/**")
                            .uri("http://localhost:8081"))
                    .route("ms-inventario", r -> r
                            .path("/api/productos/**")
                            .uri("http://localhost:8082"))
                    .route("ms-ventas", r -> r
                            .path("/api/pedidos/**")
                            .uri("http://localhost:8083"))
                    .route("ms-logistica", r -> r
                            .path("/api/despachos/**")
                            .uri("http://localhost:8084"))
                    .build();
        }
    }

    @Autowired
    private RouteLocator routeLocator;

    @Test
    void contextLoads() {
        assertNotNull(routeLocator);
    }

    @Test
    void debeRegistrarCuatroRutas() {
        List<Route> rutas = routeLocator.getRoutes().collectList().block();
        assertNotNull(rutas);
        assertEquals(4, rutas.size());
    }

    @Test
    void rutaClientesDebeExistir() {
        List<Route> rutas = routeLocator.getRoutes().collectList().block();
        assertNotNull(rutas);
        assertTrue(rutas.stream().anyMatch(r -> r.getId().equals("ms-clientes")));
    }

    @Test
    void rutaInventarioDebeExistir() {
        List<Route> rutas = routeLocator.getRoutes().collectList().block();
        assertNotNull(rutas);
        assertTrue(rutas.stream().anyMatch(r -> r.getId().equals("ms-inventario")));
    }

    @Test
    void rutaVentasDebeExistir() {
        List<Route> rutas = routeLocator.getRoutes().collectList().block();
        assertNotNull(rutas);
        assertTrue(rutas.stream().anyMatch(r -> r.getId().equals("ms-ventas")));
    }

    @Test
    void rutaLogisticaDebeExistir() {
        List<Route> rutas = routeLocator.getRoutes().collectList().block();
        assertNotNull(rutas);
        assertTrue(rutas.stream().anyMatch(r -> r.getId().equals("ms-logistica")));
    }
}
