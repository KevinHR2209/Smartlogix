package com.smartlogix.mslogistica.service;

import com.smartlogix.mslogistica.model.Despacho;
import com.smartlogix.mslogistica.model.Transportista;
import com.smartlogix.mslogistica.repository.DespachoRepository;
import com.smartlogix.mslogistica.repository.TransportistaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LogisticaServiceTest {

    @Mock
    private DespachoRepository despachoRepository;

    @Mock
    private TransportistaRepository transportistaRepository;

    @InjectMocks
    private DespachoService despachoService;

    private Despacho despacho;
    private Transportista transportista;

    @BeforeEach
    void setUp() {
        transportista = new Transportista();
        transportista.setIdTransportista(1L);

        despacho = Despacho.builder()
                .idDespacho(1L)
                .idPedido(10L)
                .estadoDespacho("PENDIENTE")
                .direccionEntrega("Av. Siempre Viva 742")
                .comunaEntrega("Santiago")
                .fechaCreacion(OffsetDateTime.now())
                .build();
    }

    @Test
    void listar_debeRetornarListaDeDespachos() {
        when(despachoRepository.findAll()).thenReturn(List.of(despacho));

        List<Despacho> resultado = despachoService.listar();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getIdPedido()).isEqualTo(10L);
        verify(despachoRepository).findAll();
    }

    @Test
    void buscarPorId_cuandoExiste_debeRetornarDespacho() {
        when(despachoRepository.findById(1L)).thenReturn(Optional.of(despacho));

        Despacho resultado = despachoService.buscarPorId(1L);

        assertThat(resultado.getIdDespacho()).isEqualTo(1L);
        assertThat(resultado.getEstadoDespacho()).isEqualTo("PENDIENTE");
    }

    @Test
    void buscarPorId_cuandoNoExiste_debeLanzarExcepcion() {
        when(despachoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> despachoService.buscarPorId(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Despacho no encontrado");
    }

    @Test
    void crear_debePersistirYRetornarDespacho() {
        Despacho nuevo = Despacho.builder()
                .idPedido(10L)
                .estadoDespacho("PENDIENTE")
                .direccionEntrega("Av. Siempre Viva 742")
                .comunaEntrega("Santiago")
                .build();
        when(despachoRepository.save(any(Despacho.class))).thenReturn(despacho);

        Despacho resultado = despachoService.crear(nuevo);

        assertThat(resultado.getIdDespacho()).isEqualTo(1L);
        verify(despachoRepository).save(nuevo);
    }

    @Test
    void cambiarEstado_debeActualizarEstadoDelDespacho() {
        when(despachoRepository.findById(1L)).thenReturn(Optional.of(despacho));
        when(despachoRepository.save(any(Despacho.class))).thenAnswer(inv -> inv.getArgument(0));

        Despacho resultado = despachoService.cambiarEstado(1L, "EN_RUTA");

        assertThat(resultado.getEstadoDespacho()).isEqualTo("EN_RUTA");
        verify(despachoRepository).save(despacho);
    }

    @Test
    void asignarTransportista_cuandoExisten_debeAsociarCorrectamente() {
        when(despachoRepository.findById(1L)).thenReturn(Optional.of(despacho));
        when(transportistaRepository.findById(1L)).thenReturn(Optional.of(transportista));
        when(despachoRepository.save(any(Despacho.class))).thenAnswer(inv -> inv.getArgument(0));

        Despacho resultado = despachoService.asignarTransportista(1L, 1L);

        assertThat(resultado.getTransportista()).isEqualTo(transportista);
        verify(despachoRepository).save(despacho);
    }

    @Test
    void asignarTransportista_cuandoTransportistaNoExiste_debeLanzarExcepcion() {
        when(despachoRepository.findById(1L)).thenReturn(Optional.of(despacho));
        when(transportistaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> despachoService.asignarTransportista(1L, 99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Transportista no encontrado");
    }
}
