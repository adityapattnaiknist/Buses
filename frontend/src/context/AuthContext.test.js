import React from 'react';
import { render } from '@testing-library/react';
import { AuthProvider, AuthContext } from './AuthContext';
import { jwtDecode } from 'jwt-decode';

jest.mock('jwt-decode', () => ({
  jwtDecode: jest.fn()
}));

describe('AuthContext', () => {
  beforeEach(() => {
    jest.spyOn(Storage.prototype, 'getItem');
    jest.spyOn(Storage.prototype, 'removeItem');
    jest.spyOn(console, 'error').mockImplementation(() => {});
  });

  afterEach(() => {
    jest.restoreAllMocks();
  });

  it('removes token from localStorage if jwtDecode throws an error', () => {
    Storage.prototype.getItem.mockReturnValue('invalid_token');
    jwtDecode.mockImplementation(() => {
      throw new Error('Invalid token');
    });

    render(
      <AuthProvider>
        <AuthContext.Consumer>
          {value => <div data-testid="test-consumer"></div>}
        </AuthContext.Consumer>
      </AuthProvider>
    );

    expect(Storage.prototype.getItem).toHaveBeenCalledWith('token');
    expect(jwtDecode).toHaveBeenCalledWith('invalid_token');
    expect(console.error).toHaveBeenCalledWith('Invalid token');
    expect(Storage.prototype.removeItem).toHaveBeenCalledWith('token');
  });
});
