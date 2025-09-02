import React, { useState, useEffect } from 'react';
import api from '../../api';

const ManageTrips = () => {
    const [trips, setTrips] = useState([]);
    const [buses, setBuses] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    // Form state
    const [formState, setFormState] = useState({
        source: '',
        destination: '',
        departureTime: '',
        arrivalTime: '',
        fare: '',
        busId: ''
    });
    const [editingTrip, setEditingTrip] = useState(null);

    useEffect(() => {
        fetchTripsAndBuses();
    }, []);

    const fetchTripsAndBuses = async () => {
        try {
            setLoading(true);
            const [tripsResponse, busesResponse] = await Promise.all([
                api.get('/v1/trips'),
                api.get('/v1/buses')
            ]);
            setTrips(tripsResponse.data);
            setBuses(busesResponse.data);
        } catch (err) {
            setError('Failed to fetch data.');
        } finally {
            setLoading(false);
        }
    };

    const handleInputChange = (e) => {
        setFormState({ ...formState, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        const tripData = {
            ...formState,
            fare: parseFloat(formState.fare),
            bus: { id: parseInt(formState.busId) }
        };

        try {
            if (editingTrip) {
                await api.put(`/v1/trips/${editingTrip.id}`, tripData);
            } else {
                await api.post('/v1/trips', tripData);
            }
            resetForm();
            fetchTripsAndBuses();
        } catch (err) {
            alert('Failed to save trip.');
        }
    };

    const handleEdit = (trip) => {
        setEditingTrip(trip);
        setFormState({
            source: trip.source,
            destination: trip.destination,
            departureTime: trip.departureTime.substring(0, 16),
            arrivalTime: trip.arrivalTime.substring(0, 16),
            fare: trip.fare,
            busId: trip.bus.id
        });
    };

    const handleDelete = async (id) => {
        if (window.confirm('Are you sure you want to delete this trip?')) {
            try {
                await api.delete(`/v1/trips/${id}`);
                fetchTripsAndBuses();
            } catch (err) {
                alert('Failed to delete trip.');
            }
        }
    };

    const resetForm = () => {
        setEditingTrip(null);
        setFormState({ source: '', destination: '', departureTime: '', arrivalTime: '', fare: '', busId: '' });
    };

    if (loading) return <p>Loading trips...</p>;
    if (error) return <div className="alert alert-danger">{error}</div>;

    return (
        <div className="container mt-4">
            <h3>{editingTrip ? 'Edit Trip' : 'Add New Trip'}</h3>
            <form onSubmit={handleSubmit} className="mb-4 p-3 border rounded">
                <div className="row">
                    <div className="col-md-4 mb-3">
                        <input type="text" name="source" className="form-control" placeholder="Source" value={formState.source} onChange={handleInputChange} required />
                    </div>
                    <div className="col-md-4 mb-3">
                        <input type="text" name="destination" className="form-control" placeholder="Destination" value={formState.destination} onChange={handleInputChange} required />
                    </div>
                    <div className="col-md-4 mb-3">
                        <input type="datetime-local" name="departureTime" className="form-control" value={formState.departureTime} onChange={handleInputChange} required />
                    </div>
                    <div className="col-md-4 mb-3">
                        <input type="datetime-local" name="arrivalTime" className="form-control" value={formState.arrivalTime} onChange={handleInputChange} required />
                    </div>
                    <div className="col-md-4 mb-3">
                        <input type="number" name="fare" className="form-control" placeholder="Fare" value={formState.fare} onChange={handleInputChange} required />
                    </div>
                    <div className="col-md-4 mb-3">
                        <select name="busId" className="form-select" value={formState.busId} onChange={handleInputChange} required>
                            <option value="">Select a Bus</option>
                            {buses.map(bus => <option key={bus.id} value={bus.id}>{bus.busNumber} - {bus.type}</option>)}
                        </select>
                    </div>
                </div>
                <button type="submit" className="btn btn-primary me-2">{editingTrip ? 'Update' : 'Add'}</button>
                {editingTrip && <button type="button" className="btn btn-secondary" onClick={resetForm}>Cancel</button>}
            </form>

            <hr/>

            <h3>Manage Trips</h3>
            <table className="table table-striped">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Source-Destination</th>
                        <th>Departure</th>
                        <th>Bus</th>
                        <th>Fare</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    {trips.map(trip => (
                        <tr key={trip.id}>
                            <td>{trip.id}</td>
                            <td>{trip.source} to {trip.destination}</td>
                            <td>{new Date(trip.departureTime).toLocaleString()}</td>
                            <td>{trip.bus.busNumber}</td>
                            <td>${trip.fare}</td>
                            <td>
                                <button className="btn btn-sm btn-warning me-2" onClick={() => handleEdit(trip)}>Edit</button>
                                <button className="btn btn-sm btn-danger" onClick={() => handleDelete(trip.id)}>Delete</button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
};

export default ManageTrips;
