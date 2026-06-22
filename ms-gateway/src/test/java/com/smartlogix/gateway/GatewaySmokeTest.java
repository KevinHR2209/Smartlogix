package com.smartlogix.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.gateway.route.RouteLocator;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class GatewaySmokeTest {

    @Autowired
    private RouteLocator routeLocator;

    @Test
    void contextLoads() {
        // El contexto de Spring arranca correctamente
    }

    @Test
    void debeRegistrarCuatroRutas() {
        long totalRutas = routeLocator.getRoutes().count().block();
        assertThat(totalRutas).isEqualTo(4);
    }

    @Test
    void rutaInventarioDebeExistir() {
        boolean existe = routeLocator.getRoutes()
                .any(r -> r.getId().equals("inventario_route"))
                .block();
        assertThat(existe).isTrue();
    }

    @Test
    void rutaClientesDebeExistir() {
        boolean existe = routeLocator.getRoutes()
                .any(r -> r.getId().equals("clientes_route"))
                .block();
        assertThat(existe).isTrue();
    }

    @Test
    void rutaVentasDebeExistir() {
        boolean existe = routeLocator.getRoutes()
                .any(r -> r.getId().equals("ventas_route"))
                .block();
        assertThat(existe).isTrue();
    }

    @Test
    void rutaLogisticaDebeExistir() {
        boolean existe = routeLocator.getRoutes()
                .any(r -> r.getId().equals("logistica_route"))
                .block();
        assertThat(existe).isTrue();
    }
}
