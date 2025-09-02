import React, { useState, useEffect } from 'react';
import api from '../../api';

const ManageBuses = () => {
    const [buses, setBuses] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    // Form state
    const [busNumber, setBusNumber] = useState('');
    const [type, setType] = useState('');
    const [capacity, setCapacity] = useState('');
    const [editingBus, setEditingBus] = useState(null);

    useEffect(() => {
        fetchBuses();
    }, []);

    const fetchBuses = async () => {
        try {
            setLoading(true);
            const response = await api.get('/v1/buses');
            setBuses(response.data);
        } catch (err) {
            setError('Failed to fetch buses.');
        } finally {
            setLoading(false);
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        const busData = { busNumber, type, capacity: parseInt(capacity) };

        try {
            if (editingBus) {
                await api.put(`/v1/buses/${editingBus.id}`, busData);
            } else {
                await api.post('/v1/buses', busData);
            }
            resetForm();
            fetchBuses();
        } catch (err) {
            alert('Failed to save bus.');
        }
    };

    const handleEdit = (bus) => {
        setEditingBus(bus);
        setBusNumber(bus.busNumber);
        setType(bus.type);
        setCapacity(bus.capacity);
    };

    const handleDelete = async (id) => {
        if (window.confirm('Are you sure you want to delete this bus?')) {
            try {
                await api.delete(`/v1/buses/${id}`);
                fetchBuses();
            } catch (err) {
                alert('Failed to delete bus.');
            }
        }
    };

    const resetForm = () => {
        setEditingBus(null);
        setBusNumber('');
        setType('');
        setCapacity('');
    };

    if (loading) return <p>Loading buses...</p>;
    if (error) return <div className="alert alert-danger">{error}</div>;

    return (
        <div className="container mt-4">
            <h3>{editingBus ? 'Edit Bus' : 'Add New Bus'}</h3>
            <form onSubmit={handleSubmit} className="mb-4">
                <div className="row">
                    <div className="col-md-3">
                        <input type="text" className="form-control" placeholder="Bus Number" value={busNumber} onChange={e => setBusNumber(e.target.value)} required />
                    </div>
                    <div className="col-md-3">
                        <input type="text" className="form-control" placeholder="Type (e.g., AC, Non-AC)" value={type} onChange={e => setType(e.target.value)} required />
                    </div>
                    <div className="col-md-3">
                        <input type="number" className="form-control" placeholder="Capacity" value={capacity} onChange={e => setCapacity(e.target.value)} required />
                    </div>
                    <div className="col-md-3">
                        <button type="submit" className="btn btn-primary me-2">{editingBus ? 'Update' : 'Add'}</button>
                        {editingBus && <button type="button" className="btn btn-secondary" onClick={resetForm}>Cancel</button>}
                    </div>
                </div>
            </form>

            <hr/>

            <h3>Manage Buses</h3>
            <table className="table table-striped">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Bus Number</th>
                        <th>Type</th>
                        <th>Capacity</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    {buses.map(bus => (
                        <tr key={bus.id}>
                            <td>{bus.id}</td>
                            <td>{bus.busNumber}</td>
                            <td>{bus.type}</td>
                            <td>{bus.capacity}</td>
                            <td>
                                <button className="btn btn-sm btn-warning me-2" onClick={() => handleEdit(bus)}>Edit</button>
                                <button className="btn btn-sm btn-danger" onClick={() => handleDelete(bus.id)}>Delete</button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
};

export default ManageBuses;
