package com.smartlogix.mslogistica.service;

import com.smartlogix.mslogistica.client.VentasClient;
import com.smartlogix.mslogistica.model.Despacho;
import com.smartlogix.mslogistica.model.Transportista;
import com.smartlogix.mslogistica.repository.DespachoRepository;
import com.smartlogix.mslogistica.repository.TransportistaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LogisticaServiceTest {

    @Mock
    private DespachoRepository despachoRepository;
    @Mock
    private TransportistaRepository transportistaRepository;
    @Mock
    private VentasClient ventasClient;

    @InjectMocks
    private DespachoService despachoService;

    @Test
    void crear_guardaDespachoCorrectamente() {
        Despacho d = new Despacho();
        d.setIdPedido(1L);
        doNothing().when(ventasClient).validarPedido(1L);
        when(despachoRepository.save(any(Despacho.class))).thenReturn(d);

        Despacho result = despachoService.crear(d);
        assertNotNull(result);
        verify(despachoRepository).save(d);
    }

    @Test
    void asignarTransportista_asignaCorrectamente() {
        Despacho d = new Despacho();
        d.setIdDespacho(1L);
        Transportista t = new Transportista();
        t.setIdTransportista(2L);

        when(despachoRepository.findById(1L)).thenReturn(Optional.of(d));
        when(transportistaRepository.findById(2L)).thenReturn(Optional.of(t));
        when(despachoRepository.save(any(Despacho.class))).thenReturn(d);

        Despacho result = despachoService.asignarTransportista(1L, 2L);
        assertEquals(t, result.getTransportista());
    }

    @Test
    void cambiarEstado_lanzaExcepcionSiDespachoNoExiste() {
        when(despachoRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> despachoService.cambiarEstado(99L, "EN_CAMINO"));
    }
}
