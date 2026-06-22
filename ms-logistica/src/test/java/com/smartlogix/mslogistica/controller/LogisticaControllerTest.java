package com.smartlogix.mslogistica.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartlogix.mslogistica.model.Despacho;
import com.smartlogix.mslogistica.service.DespachoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DespachoController.class)
class LogisticaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DespachoService despachoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void listar_retornaLista() throws Exception {
        Despacho d = new Despacho();
        d.setIdDespacho(1L);
        d.setEstadoDespacho("PENDIENTE");
        when(despachoService.listar()).thenReturn(List.of(d));

        mockMvc.perform(get("/api/despachos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idDespacho").value(1));
    }

    @Test
    void crear_retorna201() throws Exception {
        Despacho d = new Despacho();
        d.setIdDespacho(1L);
        d.setIdPedido(5L);
        d.setEstadoDespacho("PENDIENTE");
        when(despachoService.crear(any(Despacho.class))).thenReturn(d);

        mockMvc.perform(post("/api/despachos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(d)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idDespacho").value(1));
    }

    @Test
    void cambiarEstado_retornaDespachoActualizado() throws Exception {
        Despacho d = new Despacho();
        d.setIdDespacho(1L);
        d.setEstadoDespacho("EN_CAMINO");
        when(despachoService.cambiarEstado(1L, "EN_CAMINO")).thenReturn(d);

        mockMvc.perform(put("/api/despachos/1/estado")
                        .param("estado", "EN_CAMINO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estadoDespacho").value("EN_CAMINO"));
    }
}
