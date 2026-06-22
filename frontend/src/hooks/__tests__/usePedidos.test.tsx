import { renderHook, waitFor } from '@testing-library/react';
import { usePedidos } from '../usePedidos';
import { pedidosService } from '@/services/pedidosService';

jest.mock('@/services/pedidosService', () => ({
  pedidosService: { getAll: jest.fn() },
}));

describe('usePedidos', () => {
  afterEach(() => jest.clearAllMocks());

  it('carga pedidos correctamente', async () => {
    (pedidosService.getAll as jest.Mock).mockResolvedValue([
      { idPedido: 1, estadoPedido: 'CREADO' },
    ]);

    const { result } = renderHook(() => usePedidos());

    await waitFor(() => expect(result.current.loading).toBe(false));

    expect(result.current.pedidos).toEqual([{ idPedido: 1, estadoPedido: 'CREADO' }]);
    expect(result.current.error).toBeNull();
  });

  it('expone error cuando falla la carga de pedidos', async () => {
    (pedidosService.getAll as jest.Mock).mockRejectedValue(
      new Error('timeout'),
    );

    const { result } = renderHook(() => usePedidos());

    await waitFor(() => expect(result.current.loading).toBe(false));

    expect(result.current.error).toBe('timeout');
    expect(result.current.pedidos).toEqual([]);
  });
});
