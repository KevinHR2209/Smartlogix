import { apiDelete, apiGet, apiPost, apiPut, apiPutParams } from '../api';

describe('api service helpers', () => {
  const originalFetch = global.fetch;

  beforeEach(() => {
    process.env.NEXT_PUBLIC_API_URL = 'http://localhost:8080';
  });

  afterEach(() => {
    global.fetch = originalFetch;
    jest.restoreAllMocks();
  });

  it('apiGet devuelve JSON parseado correctamente', async () => {
    global.fetch = jest.fn().mockResolvedValue({
      ok: true,
      status: 200,
      text: async () => JSON.stringify({ ok: true }),
    }) as any;

    await expect(apiGet('/clientes')).resolves.toEqual({ ok: true });
  });

  it('apiDelete devuelve undefined en respuesta 204', async () => {
    global.fetch = jest.fn().mockResolvedValue({
      ok: true,
      status: 204,
      text: async () => '',
    }) as any;

    await expect(apiDelete('/clientes/1')).resolves.toBeUndefined();
  });

  it('apiPost envia el body como JSON', async () => {
    global.fetch = jest.fn().mockResolvedValue({
      ok: true,
      status: 200,
      text: async () => '{}',
    }) as any;

    await apiPost('/clientes', { nombre: 'Kevin' });

    expect(global.fetch).toHaveBeenCalledWith(
      'http://localhost:8080/clientes',
      expect.objectContaining({
        method: 'POST',
        body: JSON.stringify({ nombre: 'Kevin' }),
      }),
    );
  });

  it('apiPutParams usa metodo PUT sin body', async () => {
    global.fetch = jest.fn().mockResolvedValue({
      ok: true,
      status: 204,
      text: async () => '',
    }) as any;

    await apiPutParams('/pedidos/1/estado?estado=ENTREGADO');

    expect(global.fetch).toHaveBeenCalledWith(
      'http://localhost:8080/pedidos/1/estado?estado=ENTREGADO',
      { method: 'PUT' },
    );
  });

  it('lanza error cuando la respuesta no es ok', async () => {
    global.fetch = jest.fn().mockResolvedValue({
      ok: false,
      status: 500,
      statusText: 'Server Error',
      text: async () => 'fallo backend',
    }) as any;

    await expect(apiPut('/productos/1', { stock: 2 })).rejects.toThrow('fallo backend');
  });
});
