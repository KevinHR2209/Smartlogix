package com.smartlogix.mslogistica.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartlogix.mslogistica.model.Despacho;
import com.smartlogix.mslogistica.service.DespachoService;
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

@WebMvcTest(DespachoController.class)
class LogisticaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DespachoService despachoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void listarDespachos_debeRetornar200ConLista() throws Exception {
        Despacho d = Despacho.builder()
                .idDespacho(1L)
                .idPedido(10L)
                .estadoDespacho("PENDIENTE")
                .direccionEntrega("Av. Siempre Viva 742")
                .comunaEntrega("Santiago")
                .fechaCreacion(OffsetDateTime.now())
                .build();
        when(despachoService.listar()).thenReturn(List.of(d));

        mockMvc.perform(get("/api/despachos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idDespacho").value(1))
                .andExpect(jsonPath("$[0].estadoDespacho").value("PENDIENTE"));
    }

    @Test
    void obtenerDespacho_cuandoExiste_debeRetornar200() throws Exception {
        Despacho d = Despacho.builder()
                .idDespacho(1L)
                .idPedido(10L)
                .estadoDespacho("PENDIENTE")
                .build();
        when(despachoService.buscarPorId(1L)).thenReturn(d);

        mockMvc.perform(get("/api/despachos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idDespacho").value(1));
    }

    @Test
    void crearDespacho_debeRetornar201() throws Exception {
        Despacho nuevo = Despacho.builder()
                .idPedido(10L)
                .estadoDespacho("PENDIENTE")
                .direccionEntrega("Av. Siempre Viva 742")
                .comunaEntrega("Santiago")
                .build();
        Despacho creado = Despacho.builder()
                .idDespacho(2L)
                .idPedido(10L)
                .estadoDespacho("PENDIENTE")
                .fechaCreacion(OffsetDateTime.now())
                .build();
        when(despachoService.crear(any(Despacho.class))).thenReturn(creado);

        mockMvc.perform(post("/api/despachos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idDespacho").value(2));
    }

    @Test
    void cambiarEstado_debeRetornar200() throws Exception {
        Despacho actualizado = Despacho.builder()
                .idDespacho(1L)
                .estadoDespacho("EN_RUTA")
                .build();
        when(despachoService.cambiarEstado(eq(1L), eq("EN_RUTA"))).thenReturn(actualizado);

        mockMvc.perform(put("/api/despachos/1/estado")
                        .param("estado", "EN_RUTA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estadoDespacho").value("EN_RUTA"));
    }

    @Test
    void asignarTransportista_debeRetornar200() throws Exception {
        Despacho actualizado = Despacho.builder()
                .idDespacho(1L)
                .estadoDespacho("PENDIENTE")
                .build();
        when(despachoService.asignarTransportista(eq(1L), eq(5L))).thenReturn(actualizado);

        mockMvc.perform(put("/api/despachos/1/transportista/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idDespacho").value(1));
    }
}
