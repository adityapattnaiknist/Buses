import React, { useState, useEffect } from 'react';
import api from '../../api';

const ManageRoutes = () => {
    const [routes, setRoutes] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    // Form state
    const [source, setSource] = useState('');
    const [destination, setDestination] = useState('');
    const [distance, setDistance] = useState('');
    const [duration, setDuration] = useState('');
    const [editingRoute, setEditingRoute] = useState(null);

    useEffect(() => {
        fetchRoutes();
    }, []);

    const fetchRoutes = async () => {
        try {
            setLoading(true);
            const response = await api.get('/routes');
            setRoutes(response.data);
        } catch (err) {
            setError('Failed to fetch routes.');
        } finally {
            setLoading(false);
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        const routeData = { source, destination, distance: parseFloat(distance), duration };

        try {
            if (editingRoute) {
                await api.put(`/routes/${editingRoute.id}`, routeData);
            } else {
                await api.post('/routes', routeData);
            }
            resetForm();
            fetchRoutes();
        } catch (err) {
            alert('Failed to save route.');
        }
    };

    const handleEdit = (route) => {
        setEditingRoute(route);
        setSource(route.source);
        setDestination(route.destination);
        setDistance(route.distance);
        setDuration(route.duration);
    };

    const handleDelete = async (id) => {
        if (window.confirm('Are you sure you want to delete this route?')) {
            try {
                await api.delete(`/routes/${id}`);
                fetchRoutes();
            } catch (err) {
                alert('Failed to delete route.');
            }
        }
    };

    const resetForm = () => {
        setEditingRoute(null);
        setSource('');
        setDestination('');
        setDistance('');
        setDuration('');
    };

    if (loading) return <p>Loading routes...</p>;
    if (error) return <div className="alert alert-danger">{error}</div>;

    return (
        <div className="container mt-4">
            <h3>{editingRoute ? 'Edit Route' : 'Add New Route'}</h3>
            <form onSubmit={handleSubmit} className="mb-4">
                <div className="row">
                    <div className="col-md-3">
                        <input type="text" className="form-control" placeholder="Source" value={source} onChange={e => setSource(e.target.value)} required />
                    </div>
                    <div className="col-md-3">
                        <input type="text" className="form-control" placeholder="Destination" value={destination} onChange={e => setDestination(e.target.value)} required />
                    </div>
                    <div className="col-md-2">
                        <input type="number" className="form-control" placeholder="Distance (km)" value={distance} onChange={e => setDistance(e.target.value)} required />
                    </div>
                    <div className="col-md-2">
                        <input type="text" className="form-control" placeholder="Duration (e.g., 2h 30m)" value={duration} onChange={e => setDuration(e.target.value)} required />
                    </div>
                    <div className="col-md-2">
                        <button type="submit" className="btn btn-primary me-2">{editingRoute ? 'Update' : 'Add'}</button>
                        {editingRoute && <button type="button" className="btn btn-secondary" onClick={resetForm}>Cancel</button>}
                    </div>
                </div>
            </form>

            <hr/>

            <h3>Manage Routes</h3>
            <table className="table table-striped">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Source</th>
                        <th>Destination</th>
                        <th>Distance</th>
                        <th>Duration</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    {routes.map(route => (
                        <tr key={route.id}>
                            <td>{route.id}</td>
                            <td>{route.source}</td>
                            <td>{route.destination}</td>
                            <td>{route.distance} km</td>
                            <td>{route.duration}</td>
                            <td>
                                <button className="btn btn-sm btn-warning me-2" onClick={() => handleEdit(route)}>Edit</button>
                                <button className="btn btn-sm btn-danger" onClick={() => handleDelete(route.id)}>Delete</button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
};

export default ManageRoutes;
