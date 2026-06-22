package com.smartlogix.mslogistica.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartlogix.mslogistica.model.Despacho;
import com.smartlogix.mslogistica.service.DespachoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DespachoController.class)
class LogisticaControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @MockBean DespachoService despachoService;

    @Test
    void listarRetornaListaDeDespachos() throws Exception {
        when(despachoService.listar()).thenReturn(
                List.of(Despacho.builder().idDespacho(1L).estadoDespacho("PENDIENTE").build()));

        mockMvc.perform(get("/api/despachos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].estadoDespacho").value("PENDIENTE"));
    }

    @Test
    void crearDespachoRetorna201() throws Exception {
        Despacho despacho = Despacho.builder()
                .idDespacho(2L)
                .idPedido(8L)
                .estadoDespacho("PENDIENTE")
                .build();
        when(despachoService.crear(any(Despacho.class))).thenReturn(despacho);

        mockMvc.perform(post("/api/despachos")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(despacho)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idPedido").value(8));
    }

    @Test
    void cambiarEstadoDespachoRetorna200() throws Exception {
        when(despachoService.cambiarEstado(2L, "EN_RUTA"))
                .thenReturn(Despacho.builder().idDespacho(2L).estadoDespacho("EN_RUTA").build());

        mockMvc.perform(put("/api/despachos/2/estado")
                        .param("estado", "EN_RUTA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estadoDespacho").value("EN_RUTA"));
    }
}
