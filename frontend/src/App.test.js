import { render, screen } from '@testing-library/react';
import App from './App';

test('renders app header', () => {
  render(<App />);
  const headerElements = screen.getAllByText(/ADITYA Bus booking/i);
  expect(headerElements.length).toBeGreaterThan(0);
});
