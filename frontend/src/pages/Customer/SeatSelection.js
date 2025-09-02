import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import api from '../../api';

const SeatSelection = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const [trip, setTrip] = useState(null);
    const [selectedSeats, setSelectedSeats] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchTripDetails = async () => {
            try {
                const response = await api.get(`/v1/trips/${id}`);
                setTrip(response.data);
            } catch (err) {
                setError('Error fetching trip details.');
            } finally {
                setLoading(false);
            }
        };
        fetchTripDetails();
    }, [id]);

    const handleSeatClick = (seat) => {
        if (seat.isBooked) return;

        setSelectedSeats(prevSelected => {
            if (prevSelected.find(s => s.id === seat.id)) {
                return prevSelected.filter(s => s.id !== seat.id);
            } else {
                return [...prevSelected, seat];
            }
        });
    };

    const handleProceedToCheckout = () => {
        // This is a placeholder. In a real application, you would create a booking
        // here and then navigate to the checkout page with the booking ID.
        // For now, we'll just navigate to a placeholder checkout page.
        if (selectedSeats.length > 0) {
            // The checkout logic will be implemented in the next step.
            // We can pass the selected seats and trip info via state.
            navigate(`/checkout/${trip.id}`, { state: { selectedSeats, trip } });
        } else {
            alert("Please select at least one seat.");
        }
    };

    if (loading) return <p>Loading seat map...</p>;
    if (error) return <div className="alert alert-danger">{error}</div>;
    if (!trip) return <p>Trip not found.</p>;

    return (
        <div className="container mt-4">
            <h2>Select Your Seats</h2>
            <h4>{trip.source} to {trip.destination}</h4>
            <p>Bus: {trip.bus.busNumber}</p>

            <div className="seat-map p-3" style={{ maxWidth: '400px', margin: '0 auto' }}>
                <div className="row g-2">
                    {trip.seats.map(seat => (
                        <div key={seat.id} className="col-3">
                            <button
                                className={`btn w-100 ${
                                    seat.isBooked ? 'btn-danger' :
                                    selectedSeats.find(s => s.id === seat.id) ? 'btn-success' : 'btn-outline-primary'
                                }`}
                                onClick={() => handleSeatClick(seat)}
                                disabled={seat.isBooked}
                            >
                                {seat.seatNumber}
                            </button>
                        </div>
                    ))}
                </div>
            </div>

            <div className="mt-4 text-center">
                <h5>Selected Seats: {selectedSeats.map(s => s.seatNumber).join(', ')}</h5>
                <h5>Total Fare: ${selectedSeats.length * trip.fare}</h5>
                <button
                    className="btn btn-primary mt-3"
                    onClick={handleProceedToCheckout}
                    disabled={selectedSeats.length === 0}
                >
                    Proceed to Checkout
                </button>
            </div>
        </div>
    );
};

export default SeatSelection;
