package com.smartlogix.gateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                // ms-inventario
                .route("inventario_route", r -> r
                        .path("/api/productos", "/api/productos/**")
                        .uri("http://localhost:8081"))
                // ms-clientes
                .route("clientes_route", r -> r
                        .path("/api/clientes", "/api/clientes/**")
                        .uri("http://localhost:8082"))
                // ms-ventas
                .route("ventas_route", r -> r
                        .path("/api/pedidos", "/api/pedidos/**")
                        .uri("http://localhost:8083"))
                // ms-logistica
                .route("logistica_route", r -> r
                        .path("/api/despachos", "/api/despachos/**",
                              "/api/transportistas", "/api/transportistas/**")
                        .uri("http://localhost:8084"))
                .build();
    }
}
