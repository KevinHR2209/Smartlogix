import { renderHook, waitFor } from '@testing-library/react';
import { useProductos } from '../useProductos';
import { productosService } from '@/services/productosService';

jest.mock('@/services/productosService', () => ({
  productosService: { getAll: jest.fn() },
}));

describe('useProductos', () => {
  afterEach(() => jest.clearAllMocks());

  it('carga productos y cambia loading a false', async () => {
    (productosService.getAll as jest.Mock).mockResolvedValue([
      { idProducto: 10, nombre: 'Caja Grande', stock: 50 },
    ]);

    const { result } = renderHook(() => useProductos());

    await waitFor(() => expect(result.current.loading).toBe(false));

    expect(result.current.productos).toHaveLength(1);
    expect(result.current.error).toBeNull();
  });

  it('expone error cuando falla la carga de productos', async () => {
    (productosService.getAll as jest.Mock).mockRejectedValue(
      new Error('red caida'),
    );

    const { result } = renderHook(() => useProductos());

    await waitFor(() => expect(result.current.loading).toBe(false));

    expect(result.current.error).toBe('red caida');
  });
});
