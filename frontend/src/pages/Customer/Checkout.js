import React, { useState, useContext } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { AuthContext } from '../../context/AuthContext';
import api from '../../api';

const Checkout = () => {
    const { state } = useLocation();
    const navigate = useNavigate();
    const { user } = useContext(AuthContext);
    const { selectedSeats, trip } = state || {};

    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    // Mock payment details state
    const [cardDetails, setCardDetails] = useState({ number: '', expiry: '', cvv: '' });

    const handlePayment = async () => {
        if (!selectedSeats || selectedSeats.length === 0 || !trip) {
            setError("Booking details are missing.");
            return;
        }
        if (!user) {
            setError("You must be logged in to make a booking.");
            navigate('/login');
            return;
        }

        setLoading(true);
        setError(null);

        try {
            // 1. Create a booking on hold
            const holdRequest = {
                userId: user.id,
                tripId: trip.id,
                seatIds: selectedSeats.map(s => s.id),
                amount: selectedSeats.length * trip.fare,
            };
            const bookingResponse = await api.post('/v1/bookings/hold', holdRequest);
            const booking = bookingResponse.data;

            // 2. Process payment (mock)
            // In a real app, you'd use a payment gateway here.
            // We'll simulate a successful payment and then confirm the booking.
            await api.post(`/payments/checkout?bookingId=${booking.id}&amount=${booking.amount}`);

            // 3. Confirm the booking
            const confirmResponse = await api.put(`/v1/bookings/${booking.id}/confirm`);
            const confirmedBooking = confirmResponse.data;

            // 4. Navigate to the ticket page
            navigate(`/ticket/${confirmedBooking.ticket.id}`);

        } catch (err) {
            setError('An error occurred during checkout. Please try again.');
        } finally {
            setLoading(false);
        }
    };

    if (!selectedSeats || !trip) {
        return (
            <div className="container mt-4">
                <h2>Invalid Checkout</h2>
                <p>No booking details found. Please select a trip and seats first.</p>
            </div>
        );
    }

    // This is a simplified checkout form for demonstration
    return (
        <div className="container mt-4">
            <h2>Checkout</h2>
            <div className="row">
                <div className="col-md-6">
                    <h4>Booking Summary</h4>
                    <p><strong>Trip:</strong> {trip.source} to {trip.destination}</p>
                    <p><strong>Bus:</strong> {trip.bus.busNumber}</p>
                    <p><strong>Seats:</strong> {selectedSeats.map(s => s.seatNumber).join(', ')}</p>
                    <p><strong>Total Fare:</strong> ${selectedSeats.length * trip.fare}</p>
                </div>
                <div className="col-md-6">
                    <h4>Payment Information</h4>
                    <form>
                        <div className="mb-3">
                            <label className="form-label">Card Number</label>
                            <input type="text" className="form-control" onChange={e => setCardDetails({...cardDetails, number: e.target.value})} />
                        </div>
                        <div className="mb-3">
                            <label className="form-label">Expiry Date</label>
                            <input type="text" className="form-control" placeholder="MM/YY" onChange={e => setCardDetails({...cardDetails, expiry: e.target.value})} />
                        </div>
                        <div className="mb-3">
                            <label className="form-label">CVV</label>
                            <input type="text" className="form-control" onChange={e => setCardDetails({...cardDetails, cvv: e.target.value})} />
                        </div>
                    </form>
                </div>
            </div>

            {error && <div className="alert alert-danger mt-3">{error}</div>}

            <button
                className="btn btn-primary mt-3"
                onClick={handlePayment}
                disabled={loading}
            >
                {loading ? 'Processing...' : `Pay $${selectedSeats.length * trip.fare}`}
            </button>
        </div>
    );
};

export default Checkout;
