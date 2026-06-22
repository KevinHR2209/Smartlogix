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

    @Mock DespachoRepository despachoRepository;
    @Mock TransportistaRepository transportistaRepository;
    @Mock VentasClient ventasClient;
    @InjectMocks DespachoService despachoService;

    @Test
    void crearDespachoValidaPedidoYAsignaFecha() {
        Despacho despacho = Despacho.builder()
                .idPedido(15L)
                .estadoDespacho("PENDIENTE")
                .build();
        when(despachoRepository.save(any(Despacho.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        Despacho creado = despachoService.crear(despacho);

        assertNotNull(creado.getFechaCreacion());
        verify(ventasClient).validarPedido(15L);
        verify(despachoRepository).save(despacho);
    }

    @Test
    void asignarTransportistaGuardaRelacion() {
        Despacho despacho = Despacho.builder().idDespacho(1L).build();
        Transportista transportista = Transportista.builder()
                .idTransportista(9L)
                .nombreCompleto("Juan Perez")
                .build();
        when(despachoRepository.findById(1L)).thenReturn(Optional.of(despacho));
        when(transportistaRepository.findById(9L)).thenReturn(Optional.of(transportista));
        when(despachoRepository.save(any(Despacho.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        Despacho actualizado = despachoService.asignarTransportista(1L, 9L);

        assertEquals(9L, actualizado.getTransportista().getIdTransportista());
        verify(despachoRepository).save(despacho);
    }

    @Test
    void cambiarEstadoDespachoInexistenteLanzaExcepcion() {
        when(despachoRepository.findById(33L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> despachoService.cambiarEstado(33L, "CANCELADO"));
        assertEquals("Despacho no encontrado", ex.getMessage());
    }
}
