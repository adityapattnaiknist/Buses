import React, { useState, useEffect } from 'react';
import api from '../../api';

const ManageSeats = () => {
    const [trips, setTrips] = useState([]);
    const [selectedTripId, setSelectedTripId] = useState('');
    const [seats, setSeats] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    // Form state
    const [seatNumber, setSeatNumber] = useState('');
    const [available, setAvailable] = useState(true);
    const [editingSeat, setEditingSeat] = useState(null);

    useEffect(() => {
        fetchTrips();
    }, []);

    useEffect(() => {
        if (selectedTripId) {
            fetchSeats(selectedTripId);
        } else {
            setSeats([]);
        }
    }, [selectedTripId]);

    const fetchTrips = async () => {
        try {
            setLoading(true);
            const response = await api.get('/v1/trips');
            setTrips(response.data);
        } catch (err) {
            setError('Failed to fetch trips.');
        } finally {
            setLoading(false);
        }
    };

    const fetchSeats = async (tripId) => {
        try {
            setLoading(true);
            const response = await api.get(`/seats/trip/${tripId}`);
            setSeats(response.data);
        } catch (err) {
            setError('Failed to fetch seats.');
        } finally {
            setLoading(false);
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!selectedTripId) {
            alert('Please select a trip first.');
            return;
        }

        const seatData = {
            seatNumber: parseInt(seatNumber),
            available: available,
        };

        try {
            if (editingSeat) {
                await api.put(`/seats/${editingSeat.id}`, seatData);
            } else {
                seatData.trip = { id: parseInt(selectedTripId) };
                await api.post('/seats', seatData);
            }
            resetForm();
            fetchSeats(selectedTripId);
        } catch (err) {
            alert('Failed to save seat.');
        }
    };

    const handleEdit = (seat) => {
        setEditingSeat(seat);
        setSeatNumber(seat.seatNumber);
        setAvailable(seat.available);
    };

    const handleDelete = async (id) => {
        if (window.confirm('Are you sure you want to delete this seat?')) {
            try {
                await api.delete(`/seats/${id}`);
                fetchSeats(selectedTripId);
            } catch (err) {
                alert('Failed to delete seat.');
            }
        }
    };

    const resetForm = () => {
        setEditingSeat(null);
        setSeatNumber('');
        setAvailable(true);
    };

    if (loading && !trips.length) return <p>Loading...</p>;
    if (error && !trips.length) return <div className="alert alert-danger">{error}</div>;

    return (
        <div className="container mt-4">
            <div className="mb-4">
                <label className="form-label fw-bold">Select Trip:</label>
                <select className="form-select" value={selectedTripId} onChange={e => {
                    setSelectedTripId(e.target.value);
                    resetForm();
                }}>
                    <option value="">-- Select a Trip --</option>
                    {trips.map(trip => (
                        <option key={trip.id} value={trip.id}>
                            {trip.source} to {trip.destination} - {new Date(trip.departureTime).toLocaleString()} ({trip.bus?.busNumber})
                        </option>
                    ))}
                </select>
            </div>

            {selectedTripId && (
                <>
                    <h3>{editingSeat ? 'Edit Seat' : 'Add New Seat'}</h3>
                    <form onSubmit={handleSubmit} className="mb-4 p-3 border rounded">
                        <div className="row align-items-center">
                            <div className="col-md-4 mb-3 mb-md-0">
                                <label className="form-label">Seat Number</label>
                                <input type="number" className="form-control" placeholder="Seat Number" value={seatNumber} onChange={e => setSeatNumber(e.target.value)} required />
                            </div>
                            <div className="col-md-4 mb-3 mb-md-0">
                                <div className="form-check mt-md-4">
                                    <input className="form-check-input" type="checkbox" id="availableCheck" checked={available} onChange={e => setAvailable(e.target.checked)} />
                                    <label className="form-check-label" htmlFor="availableCheck">
                                        Is Available?
                                    </label>
                                </div>
                            </div>
                            <div className="col-md-4 mt-md-4">
                                <button type="submit" className="btn btn-primary me-2">{editingSeat ? 'Update' : 'Add'}</button>
                                {editingSeat && <button type="button" className="btn btn-secondary" onClick={resetForm}>Cancel</button>}
                            </div>
                        </div>
                    </form>

                    <hr/>

                    <h3>Manage Seats for Trip ID: {selectedTripId}</h3>
                    {loading && seats.length === 0 ? (
                        <p>Loading seats...</p>
                    ) : (
                        <table className="table table-striped">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Seat Number</th>
                                    <th>Available</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                {seats.length > 0 ? seats.map(seat => (
                                    <tr key={seat.id}>
                                        <td>{seat.id}</td>
                                        <td>{seat.seatNumber}</td>
                                        <td>{seat.available ? 'Yes' : 'No'}</td>
                                        <td>
                                            <button className="btn btn-sm btn-warning me-2" onClick={() => handleEdit(seat)}>Edit</button>
                                            <button className="btn btn-sm btn-danger" onClick={() => handleDelete(seat.id)}>Delete</button>
                                        </td>
                                    </tr>
                                )) : (
                                    <tr>
                                        <td colSpan="4" className="text-center">No seats found for this trip.</td>
                                    </tr>
                                )}
                            </tbody>
                        </table>
                    )}
                </>
            )}
        </div>
    );
};

export default ManageSeats;
