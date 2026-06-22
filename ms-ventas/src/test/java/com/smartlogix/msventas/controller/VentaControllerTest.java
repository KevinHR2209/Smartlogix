package com.smartlogix.msventas.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartlogix.msventas.model.Pedido;
import com.smartlogix.msventas.service.PedidoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PedidoController.class)
class VentaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PedidoService pedidoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void listar_retornaLista() throws Exception {
        Pedido p = new Pedido();
        p.setIdPedido(1L);
        p.setEstadoPedido("PENDIENTE");
        when(pedidoService.listar()).thenReturn(List.of(p));

        mockMvc.perform(get("/api/pedidos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idPedido").value(1));
    }

    @Test
    void obtener_retornaPedido() throws Exception {
        Pedido p = new Pedido();
        p.setIdPedido(1L);
        p.setEstadoPedido("PENDIENTE");
        when(pedidoService.buscarPorId(1L)).thenReturn(p);

        mockMvc.perform(get("/api/pedidos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idPedido").value(1));
    }

    @Test
    void crear_retorna201() throws Exception {
        Pedido p = new Pedido();
        p.setIdPedido(1L);
        p.setIdCliente(10L);
        p.setEstadoPedido("PENDIENTE");
        when(pedidoService.crear(any(Pedido.class), anyString())).thenReturn(p);

        mockMvc.perform(post("/api/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(p)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idPedido").value(1));
    }

    @Test
    void cambiarEstado_retornaPedidoActualizado() throws Exception {
        Pedido p = new Pedido();
        p.setIdPedido(1L);
        p.setEstadoPedido("ENVIADO");
        when(pedidoService.cambiarEstado(1L, "ENVIADO")).thenReturn(p);

        mockMvc.perform(put("/api/pedidos/1/estado")
                        .param("estado", "ENVIADO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estadoPedido").value("ENVIADO"));
    }
}
