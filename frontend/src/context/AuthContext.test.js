import React from 'react';
import { render } from '@testing-library/react';
import { AuthProvider } from './AuthContext';
import { jwtDecode } from 'jwt-decode';

jest.mock('jwt-decode', () => ({
  jwtDecode: jest.fn()
}));

describe('AuthContext', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  it('removes invalid token from localStorage and logs error when jwtDecode throws', () => {
    const fakeToken = 'invalid-token-string';
    const getItemSpy = jest.spyOn(Storage.prototype, 'getItem').mockReturnValue(fakeToken);
    const removeItemSpy = jest.spyOn(Storage.prototype, 'removeItem');
    const consoleErrorSpy = jest.spyOn(console, 'error').mockImplementation(() => {});

    jwtDecode.mockImplementation(() => {
      throw new Error('Invalid token specified');
    });

    render(<AuthProvider><div>Test Child</div></AuthProvider>);

    expect(getItemSpy).toHaveBeenCalledWith('token');
    expect(jwtDecode).toHaveBeenCalledWith(fakeToken);
    expect(consoleErrorSpy).toHaveBeenCalledWith('Invalid token');
    expect(removeItemSpy).toHaveBeenCalledWith('token');

    getItemSpy.mockRestore();
    removeItemSpy.mockRestore();
    consoleErrorSpy.mockRestore();
  });
});
