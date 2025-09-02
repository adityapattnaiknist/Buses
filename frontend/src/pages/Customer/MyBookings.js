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
                <div className="row">
                    {bookings.map(booking => (
                        <div key={booking.id} className="col-md-6 mb-4">
                            <div className="card">
                                <div className="card-header d-flex justify-content-between align-items-center">
                                    <span>Booking ID: {booking.id}</span>
                                    <span className={`badge bg-${booking.status === 'CONFIRMED' ? 'success' : 'danger'}`}>{booking.status}</span>
                                </div>
                                <div className="card-body">
                                    <h5 className="card-title">{booking.trip.source} to {booking.trip.destination}</h5>
                                    <p className="card-text">
                                        <strong>Date:</strong> {new Date(booking.trip.departureTime).toLocaleDateString()}
                                        <br/>
                                        <strong>Bus:</strong> {booking.trip.bus.busNumber}
                                        <br/>
                                        <strong>Amount:</strong> ${booking.amount}
                                    </p>
                                    {booking.status === 'CONFIRMED' && booking.ticket && (
                                        <>
                                            <a href={`/api/v1/tickets/${booking.ticket.id}/download`} className="btn btn-primary me-2" target="_blank" rel="noopener noreferrer">Download Ticket</a>
                                            <button onClick={() => handleCancelBooking(booking.id)} className="btn btn-outline-danger">Cancel Booking</button>
                                        </>
                                    )}
                                </div>
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
};

export default MyBookings;
