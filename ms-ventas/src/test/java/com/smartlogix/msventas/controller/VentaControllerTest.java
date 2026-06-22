package com.smartlogix.msventas.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartlogix.msventas.model.Pedido;
import com.smartlogix.msventas.service.PedidoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PedidoController.class)
class VentaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PedidoService pedidoService;

    @Test
    void listarRetorna200YListaDePedidos() throws Exception {
        Pedido pedido = Pedido.builder()
                .idPedido(1L)
                .idCliente(7L)
                .estadoPedido("PENDIENTE")
                .build();
        when(pedidoService.listar()).thenReturn(List.of(pedido));

        mockMvc.perform(get("/api/pedidos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idPedido").value(1))
                .andExpect(jsonPath("$[0].estadoPedido").value("PENDIENTE"));
    }

    @Test
    void obtenerPorIdExistenteRetorna200() throws Exception {
        Pedido pedido = Pedido.builder()
                .idPedido(3L)
                .idCliente(9L)
                .estadoPedido("CREADO")
                .build();
        when(pedidoService.buscarPorId(3L)).thenReturn(pedido);

        mockMvc.perform(get("/api/pedidos/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCliente").value(9));
    }

    @Test
    void crearPedidoRetorna201() throws Exception {
        Pedido pedido = Pedido.builder()
                .idPedido(2L)
                .idCliente(5L)
                .fechaCreacion(OffsetDateTime.parse("2026-01-01T10:00:00Z"))
                .estadoPedido("CREADO")
                .build();
        when(pedidoService.crear(any(Pedido.class), eq("Valparaiso"))).thenReturn(pedido);

        mockMvc.perform(post("/api/pedidos")
                        .param("regionDestino", "Valparaiso")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pedido)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idPedido").value(2));
    }

    @Test
    void cambiarEstadoRetorna200() throws Exception {
        Pedido pedido = Pedido.builder()
                .idPedido(6L)
                .estadoPedido("ENTREGADO")
                .build();
        when(pedidoService.cambiarEstado(6L, "ENTREGADO")).thenReturn(pedido);

        mockMvc.perform(put("/api/pedidos/6/estado")
                        .param("estado", "ENTREGADO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estadoPedido").value("ENTREGADO"));
    }
}
