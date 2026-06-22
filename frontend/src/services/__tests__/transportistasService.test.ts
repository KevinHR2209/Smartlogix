import { transportistasService } from '../transportistasService';
import * as api from '../api';

jest.mock('../api');

describe('transportistasService', () => {
  it('getAll llama a apiGet con /api/transportistas', async () => {
    (api.apiGet as jest.Mock).mockResolvedValue([]);
    await transportistasService.getAll();
    expect(api.apiGet).toHaveBeenCalledWith('/api/transportistas');
  });

  it('create llama a apiPost con el payload correcto', async () => {
    (api.apiPost as jest.Mock).mockResolvedValue({ idTransportista: 1 });
    const payload = { nombreCompleto: 'Juan', patenteVehiculo: 'AB-12', telefonoContacto: '123', estado: 'DISPONIBLE' };
    await transportistasService.create(payload);
    expect(api.apiPost).toHaveBeenCalledWith('/api/transportistas', payload);
  });
});
