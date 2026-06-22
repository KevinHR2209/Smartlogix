package com.smartlogix.msventas.service;

import com.smartlogix.msventas.client.ClienteClient;
import com.smartlogix.msventas.client.InventarioClient;
import com.smartlogix.msventas.model.DetallePedido;
import com.smartlogix.msventas.model.Pedido;
import com.smartlogix.msventas.repository.PedidoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VentaServiceTest {

    @Mock PedidoRepository pedidoRepository;
    @Mock InventarioClient inventarioClient;
    @Mock ClienteClient clienteClient;
    @InjectMocks PedidoService pedidoService;

    @Test
    void buscarPorIdCuandoNoExisteLanzaExcepcion() {
        when(pedidoRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> pedidoService.buscarPorId(99L));
        assertEquals("Pedido no encontrado", ex.getMessage());
    }

    @Test
    void crearValidaClienteDescuentaStockYGuarda() {
        DetallePedido detalle = DetallePedido.builder()
                .idProducto(4L)
                .cantidad(2)
                .precioUnitarioSnapshot(1000)
                .build();
        Pedido pedido = Pedido.builder()
                .idCliente(11L)
                .detalles(List.of(detalle))
                .estadoPedido("CREADO")
                .build();
        when(pedidoRepository.save(any(Pedido.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        Pedido creado = pedidoService.crear(pedido, "Valparaiso");

        assertNotNull(creado.getFechaCreacion());
        assertSame(pedido, detalle.getPedido());
        verify(clienteClient).validarCliente(11L);
        verify(inventarioClient).descontarStock(4L, 2, "Valparaiso");
        verify(pedidoRepository).save(pedido);
    }

    @Test
    void cambiarEstadoActualizaCorrectamente() {
        Pedido pedido = Pedido.builder()
                .idPedido(3L)
                .estadoPedido("PENDIENTE")
                .build();
        when(pedidoRepository.findById(3L)).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(any(Pedido.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        Pedido actualizado = pedidoService.cambiarEstado(3L, "CANCELADO");

        assertEquals("CANCELADO", actualizado.getEstadoPedido());
        verify(pedidoRepository).save(pedido);
    }
}
