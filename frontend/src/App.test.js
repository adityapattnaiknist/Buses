import { render, screen } from '@testing-library/react';
import App from './App';

test('renders Welcome to ADITYA Bus Booking', () => {
  render(<App />);
  const linkElement = screen.getByText(/Welcome to ADITYA Bus Booking/i);
  expect(linkElement).toBeInTheDocument();
});
