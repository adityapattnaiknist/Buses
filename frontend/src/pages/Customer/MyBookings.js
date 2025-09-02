import React, { useState, useEffect, useContext } from 'react';
import { Link } from 'react-router-dom';
import { AuthContext } from '../../context/AuthContext';
import api from '../../api';

const MyBookings = () => {
    const [bookings, setBookings] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const { user } = useContext(AuthContext);

    useEffect(() => {
        const fetchBookings = async () => {
            if (!user) {
                setLoading(false);
                return;
            }
            try {
                const response = await api.get('/v1/bookings/my-bookings');
                setBookings(response.data);
            } catch (err) {
                setError('Error fetching bookings.');
            } finally {
                setLoading(false);
            }
        };
        fetchBookings();
    }, [user]);

    const handleCancelBooking = async (bookingId) => {
        if (!window.confirm("Are you sure you want to cancel this booking?")) {
            return;
        }

        try {
            await api.put(`/v1/bookings/${bookingId}/cancel`);
            // Refresh the bookings list
            const response = await api.get('/v1/bookings/my-bookings');
            setBookings(response.data);
        } catch (err) {
            alert('Failed to cancel booking.');
        }
    };

    if (loading) return <p>Loading your bookings...</p>;
    if (error) return <div className="alert alert-danger">{error}</div>;

    return (
        <div className="container mt-4">
            <h2>My Bookings</h2>
            {bookings.length === 0 ? (
                <p>You have no bookings.</p>
            ) : (
                <ul className="list-group">
                    {bookings.map(booking => (
                        <li key={booking.id} className="list-group-item">
                            <h5>Trip: {booking.trip.source} to {booking.trip.destination}</h5>
                            <p>Date: {new Date(booking.trip.departureTime).toLocaleDateString()}</p>
                            <p>Status: <span className={`badge bg-${booking.status === 'CONFIRMED' ? 'success' : 'warning'}`}>{booking.status}</span></p>
                            <p>Amount: ${booking.amount}</p>
                            {booking.status === 'CONFIRMED' && booking.ticket && (
                                <>
                                    <a href={`/api/v1/tickets/${booking.ticket.id}/download`} className="btn btn-info me-2" target="_blank" rel="noopener noreferrer">Download Ticket</a>
                                    <button onClick={() => handleCancelBooking(booking.id)} className="btn btn-danger">Cancel Booking</button>
                                </>
                            )}
                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
};

export default MyBookings;
