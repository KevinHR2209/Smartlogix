package com.smartlogix.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.gateway.config.GatewayAutoConfiguration;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
@EnableAutoConfiguration(exclude = { GatewayAutoConfiguration.class })
class GatewaySmokeTest {

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
