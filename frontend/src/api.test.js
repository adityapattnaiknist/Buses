import api from './api';

describe('API Interceptor', () => {
    let originalAdapter;

    beforeEach(() => {
        originalAdapter = api.defaults.adapter;
        api.defaults.adapter = jest.fn().mockResolvedValue({
            data: {},
            status: 200,
            statusText: 'OK',
            headers: {},
            config: {}
        });
        localStorage.clear();
    });

    afterEach(() => {
        api.defaults.adapter = originalAdapter;
        jest.clearAllMocks();
    });

    it('should add Authorization header if token exists in localStorage', async () => {
        localStorage.setItem('token', 'test-token');

        await api.get('/test');

        expect(api.defaults.adapter).toHaveBeenCalled();
        const config = api.defaults.adapter.mock.calls[0][0];
        // Axios config.headers is an AxiosHeaders object in v1+, we can check it
        expect(config.headers.get('Authorization')).toBe('Bearer test-token');
    });

    it('should not add Authorization header if token does not exist', async () => {
        await api.get('/test');

        expect(api.defaults.adapter).toHaveBeenCalled();
        const config = api.defaults.adapter.mock.calls[0][0];
        expect(config.headers.has('Authorization')).toBe(false);
    });
});
