package com.smartlogix.msventas.service;

import com.smartlogix.msventas.client.ClienteClient;
import com.smartlogix.msventas.client.InventarioClient;
import com.smartlogix.msventas.model.Pedido;
import com.smartlogix.msventas.repository.PedidoRepository;
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
class VentaServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;
    @Mock
    private InventarioClient inventarioClient;
    @Mock
    private ClienteClient clienteClient;

    @InjectMocks
    private PedidoService pedidoService;

    @Test
    void buscarPorId_lanzaExcepcionSiNoExiste() {
        when(pedidoRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> pedidoService.buscarPorId(99L));
    }

    @Test
    void crear_guardaPedidoCorrectamente() {
        Pedido pedido = new Pedido();
        pedido.setIdCliente(1L);
        doNothing().when(clienteClient).validarCliente(1L);
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);

        Pedido result = pedidoService.crear(pedido, "Metropolitana");
        assertNotNull(result);
        verify(pedidoRepository).save(pedido);
    }

    @Test
    void cambiarEstado_actualizaEstado() {
        Pedido pedido = new Pedido();
        pedido.setIdPedido(1L);
        pedido.setEstadoPedido("PENDIENTE");
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);

        Pedido result = pedidoService.cambiarEstado(1L, "ENVIADO");
        assertEquals("ENVIADO", result.getEstadoPedido());
    }
}
