package com.smartlogix.gateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.uri;

@Configuration
public class GatewayConfig {

    @Bean
    public RouterFunction<ServerResponse> inventarioRoute() {
        return route("inventario_route")
                // Se define qué rutas atrapar y se llama a http() vacío
                .route(RequestPredicates.path("/api/productos").or(RequestPredicates.path("/api/productos/**")), http())
                // Se define hacia dónde se redirige usando el filtro uri()
                .before(uri("http://localhost:8081"))
                .build();
    }
}