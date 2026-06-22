import { pedidosService } from '../pedidosService';
import * as api from '../api';

jest.mock('../api');

describe('pedidosService', () => {
  it('getAll llama a apiGet con /api/pedidos', async () => {
    (api.apiGet as jest.Mock).mockResolvedValue([]);
    await pedidosService.getAll();
    expect(api.apiGet).toHaveBeenCalledWith('/api/pedidos');
  });

  it('create llama a apiPost con regionDestino como query param', async () => {
    (api.apiPost as jest.Mock).mockResolvedValue({ idPedido: 1 });
    const payload = { idCliente: 1, estadoPedido: 'CREADO', montoTotal: 5000, detalles: [] };
    await pedidosService.create(payload, 'Valparaiso');
    expect(api.apiPost).toHaveBeenCalledWith('/api/pedidos?regionDestino=Valparaiso', payload);
  });

  it('cambiarEstado llama a apiPutParams con estado en query string', async () => {
    (api.apiPutParams as jest.Mock).mockResolvedValue(undefined);
    await pedidosService.cambiarEstado(6, 'ENTREGADO');
    expect(api.apiPutParams).toHaveBeenCalledWith('/api/pedidos/6/estado?estado=ENTREGADO');
  });
});
