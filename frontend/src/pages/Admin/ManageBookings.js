import React, { useState, useEffect } from 'react';
import api from '../../api';

const ManageBookings = () => {
    const [bookings, setBookings] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        fetchBookings();
    }, []);

    const fetchBookings = async () => {
        try {
            setLoading(true);
            const response = await api.get('/v1/bookings');
            setBookings(response.data);
            setError(null);
        } catch (err) {
            setError('Failed to fetch bookings.');
        } finally {
            setLoading(false);
        }
    };

    const handleUpdateStatus = async (id, newStatus) => {
        try {
            await api.put(`/v1/bookings/${id}/status?status=${newStatus}`);
            fetchBookings(); // Refresh the list
        } catch (err) {
            alert('Failed to update booking status.');
        }
    };

    if (loading) return <p>Loading bookings...</p>;
    if (error) return <div className="alert alert-danger">{error}</div>;

    return (
        <div className="container mt-4">
            <h2>Manage Bookings</h2>
            <table className="table table-striped">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>User</th>
                        <th>Trip Route</th>
                        <th>Amount</th>
                        <th>Date</th>
                        <th>Status</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    {bookings.map(booking => (
                        <tr key={booking.id}>
                            <td>{booking.id}</td>
                            <td>{booking.user ? booking.user.name : 'N/A'}</td>
                            <td>
                                {booking.trip ? `${booking.trip.source} to ${booking.trip.destination}` : 'N/A'}
                            </td>
                            <td>${booking.amount ? booking.amount.toFixed(2) : '0.00'}</td>
                            <td>{booking.createdAt ? new Date(booking.createdAt).toLocaleString() : 'N/A'}</td>
                            <td>
                                <span className={`badge bg-${
                                    booking.status === 'CONFIRMED' ? 'success' :
                                    booking.status === 'CANCELLED' ? 'danger' :
                                    booking.status === 'HOLD' ? 'warning' : 'secondary'
                                }`}>
                                    {booking.status}
                                </span>
                            </td>
                            <td>
                                <select
                                    className="form-select form-select-sm"
                                    value={booking.status}
                                    onChange={(e) => handleUpdateStatus(booking.id, e.target.value)}
                                >
                                    <option value="PENDING">PENDING</option>
                                    <option value="HOLD">HOLD</option>
                                    <option value="CONFIRMED">CONFIRMED</option>
                                    <option value="CANCELLED">CANCELLED</option>
                                </select>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
};

export default ManageBookings;
