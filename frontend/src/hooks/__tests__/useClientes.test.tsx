import { renderHook, waitFor } from '@testing-library/react';
import { useClientes } from '../useClientes';
import { clientesService } from '@/services/clientesService';

jest.mock('@/services/clientesService', () => ({
  clientesService: { getAll: jest.fn() },
}));

describe('useClientes', () => {
  afterEach(() => jest.clearAllMocks());

  it('carga clientes correctamente y pone loading en false', async () => {
    (clientesService.getAll as jest.Mock).mockResolvedValue([
      { idCliente: 1, nombre: 'Kevin' },
    ]);

    const { result } = renderHook(() => useClientes());

    await waitFor(() => expect(result.current.loading).toBe(false));

    expect(result.current.clientes).toEqual([{ idCliente: 1, nombre: 'Kevin' }]);
    expect(result.current.error).toBeNull();
  });

  it('expone mensaje de error cuando falla la carga', async () => {
    (clientesService.getAll as jest.Mock).mockRejectedValue(
      new Error('sin conexion'),
    );

    const { result } = renderHook(() => useClientes());

    await waitFor(() => expect(result.current.loading).toBe(false));

    expect(result.current.error).toBe('sin conexion');
    expect(result.current.clientes).toEqual([]);
  });
});
