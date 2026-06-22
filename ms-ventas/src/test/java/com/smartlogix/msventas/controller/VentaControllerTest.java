package com.smartlogix.msventas.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartlogix.msventas.model.Pedido;
import com.smartlogix.msventas.service.PedidoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PedidoController.class)
class VentaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PedidoService pedidoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void listarPedidos_debeRetornar200ConLista() throws Exception {
        Pedido p = Pedido.builder()
                .idPedido(1L)
                .idCliente(10L)
                .estadoPedido("PENDIENTE")
                .montoTotal(500)
                .fechaCreacion(OffsetDateTime.now())
                .build();
        when(pedidoService.listar()).thenReturn(List.of(p));

        mockMvc.perform(get("/api/pedidos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idPedido").value(1))
                .andExpect(jsonPath("$[0].estadoPedido").value("PENDIENTE"));
    }

    @Test
    void obtenerPedido_cuandoExiste_debeRetornar200() throws Exception {
        Pedido p = Pedido.builder()
                .idPedido(1L)
                .idCliente(10L)
                .estadoPedido("PENDIENTE")
                .montoTotal(500)
                .build();
        when(pedidoService.buscarPorId(1L)).thenReturn(p);

        mockMvc.perform(get("/api/pedidos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idPedido").value(1));
    }

    @Test
    void crearPedido_debeRetornar201() throws Exception {
        Pedido nuevo = Pedido.builder()
                .idCliente(10L)
                .montoTotal(300)
                .estadoPedido("PENDIENTE")
                .build();
        Pedido guardado = Pedido.builder()
                .idPedido(2L)
                .idCliente(10L)
                .montoTotal(300)
                .estadoPedido("PENDIENTE")
                .fechaCreacion(OffsetDateTime.now())
                .build();
        when(pedidoService.crear(any(Pedido.class))).thenReturn(guardado);

        mockMvc.perform(post("/api/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idPedido").value(2));
    }

    @Test
    void cambiarEstado_debeRetornar200() throws Exception {
        Pedido actualizado = Pedido.builder()
                .idPedido(1L)
                .estadoPedido("ENVIADO")
                .build();
        when(pedidoService.cambiarEstado(eq(1L), eq("ENVIADO"))).thenReturn(actualizado);

        mockMvc.perform(put("/api/pedidos/1/estado")
                        .param("estado", "ENVIADO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estadoPedido").value("ENVIADO"));
    }
}
