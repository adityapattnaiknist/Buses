import api from './api';

describe('api interceptor', () => {
    let originalAdapter;
    let mockAdapter;

    beforeAll(() => {
        originalAdapter = api.defaults.adapter;
    });

    afterAll(() => {
        api.defaults.adapter = originalAdapter;
    });

    beforeEach(() => {
        localStorage.clear();
        mockAdapter = jest.fn((config) => {
            return Promise.resolve({
                data: {},
                status: 200,
                statusText: 'OK',
                headers: {},
                config,
                request: {}
            });
        });
        api.defaults.adapter = mockAdapter;
    });

    it('should add Authorization header if token exists in localStorage', async () => {
        localStorage.setItem('token', 'fake-token');

        await api.get('/test');

        expect(mockAdapter).toHaveBeenCalled();
        const config = mockAdapter.mock.calls[0][0];
        // In axios ^1.x headers might be an AxiosHeaders object
        expect(config.headers.get?.('Authorization') || config.headers['Authorization']).toBe('Bearer fake-token');
    });

    it('should not add Authorization header if token does not exist', async () => {
        await api.get('/test');

        expect(mockAdapter).toHaveBeenCalled();
        const config = mockAdapter.mock.calls[0][0];
        expect(config.headers.get?.('Authorization') || config.headers['Authorization']).toBeUndefined();
    });
});
