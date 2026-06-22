import { renderHook, waitFor } from '@testing-library/react';
import { useTransportistas } from '../useTransportistas';
import { transportistasService } from '@/services/transportistasService';

jest.mock('@/services/transportistasService', () => ({
  transportistasService: { getAll: jest.fn() },
}));

describe('useTransportistas', () => {
  afterEach(() => jest.clearAllMocks());

  it('carga transportistas correctamente', async () => {
    (transportistasService.getAll as jest.Mock).mockResolvedValue([
      { idTransportista: 9, nombreCompleto: 'Juan Perez' },
    ]);

    const { result } = renderHook(() => useTransportistas());

    await waitFor(() => expect(result.current.loading).toBe(false));

    expect(result.current.transportistas).toHaveLength(1);
    expect(result.current.error).toBeNull();
  });

  it('expone error cuando falla la carga de transportistas', async () => {
    (transportistasService.getAll as jest.Mock).mockRejectedValue(
      new Error('gateway down'),
    );

    const { result } = renderHook(() => useTransportistas());

    await waitFor(() => expect(result.current.loading).toBe(false));

    expect(result.current.error).toBe('gateway down');
  });
});
