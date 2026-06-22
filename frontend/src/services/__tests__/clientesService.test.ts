import { clientesService } from '../clientesService';
import * as api from '../api';

jest.mock('../api');

describe('clientesService', () => {
  it('getAll llama a apiGet con /api/clientes', async () => {
    (api.apiGet as jest.Mock).mockResolvedValue([]);
    await clientesService.getAll();
    expect(api.apiGet).toHaveBeenCalledWith('/api/clientes');
  });

  it('create llama a apiPost con el payload correcto', async () => {
    (api.apiPost as jest.Mock).mockResolvedValue({ idCliente: 1 });
    const payload = { nombre: 'Kevin', correoElectronico: 'k@k.com', telefono: '123', direccion: 'Calle 1' };
    await clientesService.create(payload);
    expect(api.apiPost).toHaveBeenCalledWith('/api/clientes', payload);
  });

  it('update llama a apiPut con id y datos', async () => {
    (api.apiPut as jest.Mock).mockResolvedValue({});
    const cliente = { idCliente: 5, nombre: 'Chris', correoElectronico: 'c@c.com', telefono: '999', direccion: 'Av 2' };
    await clientesService.update(5, cliente);
    expect(api.apiPut).toHaveBeenCalledWith('/api/clientes/5', cliente);
  });

  it('remove llama a apiDelete con el id', async () => {
    (api.apiDelete as jest.Mock).mockResolvedValue(undefined);
    await clientesService.remove(3);
    expect(api.apiDelete).toHaveBeenCalledWith('/api/clientes/3');
  });
});
