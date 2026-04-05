import { render, screen } from '@testing-library/react';
import App from './App';

test('renders ADITYA Bus booking link', () => {
  render(<App />);
  const linkElements = screen.getAllByText(/ADITYA Bus booking/i);
  expect(linkElements.length).toBeGreaterThan(0);
});
