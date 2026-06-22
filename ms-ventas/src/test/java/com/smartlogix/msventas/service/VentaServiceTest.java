package com.smartlogix.msventas.service;

import com.smartlogix.msventas.model.Pedido;
import com.smartlogix.msventas.repository.PedidoRepository;
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
class VentaServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @InjectMocks
    private PedidoService pedidoService;

    private Pedido pedido;

    @BeforeEach
    void setUp() {
        pedido = Pedido.builder()
                .idPedido(1L)
                .idCliente(10L)
                .estadoPedido("PENDIENTE")
                .montoTotal(500)
                .fechaCreacion(OffsetDateTime.now())
                .build();
    }

    @Test
    void listar_debeRetornarListaDePedidos() {
        when(pedidoRepository.findAll()).thenReturn(List.of(pedido));

        List<Pedido> resultado = pedidoService.listar();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getIdCliente()).isEqualTo(10L);
        verify(pedidoRepository).findAll();
    }

    @Test
    void buscarPorId_cuandoExiste_debeRetornarPedido() {
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        Pedido resultado = pedidoService.buscarPorId(1L);

        assertThat(resultado.getIdPedido()).isEqualTo(1L);
        assertThat(resultado.getEstadoPedido()).isEqualTo("PENDIENTE");
    }

    @Test
    void buscarPorId_cuandoNoExiste_debeLanzarExcepcion() {
        when(pedidoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> pedidoService.buscarPorId(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Pedido no encontrado");
    }

    @Test
    void crear_debePersistirYRetornarPedido() {
        Pedido nuevo = Pedido.builder()
                .idCliente(10L)
                .montoTotal(300)
                .estadoPedido("PENDIENTE")
                .build();
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);

        Pedido resultado = pedidoService.crear(nuevo);

        assertThat(resultado.getIdPedido()).isEqualTo(1L);
        verify(pedidoRepository).save(nuevo);
    }

    @Test
    void cambiarEstado_debeActualizarEstadoDelPedido() {
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(inv -> inv.getArgument(0));

        Pedido resultado = pedidoService.cambiarEstado(1L, "ENVIADO");

        assertThat(resultado.getEstadoPedido()).isEqualTo("ENVIADO");
        verify(pedidoRepository).save(pedido);
    }
}
