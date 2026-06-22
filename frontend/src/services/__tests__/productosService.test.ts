import { productosService } from '../productosService';
import * as api from '../api';

jest.mock('../api');

describe('productosService', () => {
  it('getAll llama a apiGet con /api/productos', async () => {
    (api.apiGet as jest.Mock).mockResolvedValue([]);
    await productosService.getAll();
    expect(api.apiGet).toHaveBeenCalledWith('/api/productos');
  });

  it('create llama a apiPost con el payload correcto', async () => {
    (api.apiPost as jest.Mock).mockResolvedValue({ idProducto: 1 });
    const payload = { nombre: 'Caja', descripcion: 'Grande', precio: 100, stock: 10 };
    await productosService.create(payload);
    expect(api.apiPost).toHaveBeenCalledWith('/api/productos', payload);
  });

  it('remove llama a apiDelete con el id', async () => {
    (api.apiDelete as jest.Mock).mockResolvedValue(undefined);
    await productosService.remove(7);
    expect(api.apiDelete).toHaveBeenCalledWith('/api/productos/7');
  });
});
