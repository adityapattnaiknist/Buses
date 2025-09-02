import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import api from '../../api';

const SearchTrips = () => {
    const [source, setSource] = useState('');
    const [destination, setDestination] = useState('');
    const [date, setDate] = useState('');
    const [trips, setTrips] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [searched, setSearched] = useState(false);

    const handleSearch = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError(null);
        setSearched(true);
        try {
            const response = await api.get('/v1/trips/search', {
                params: { source, destination, date }
            });
            setTrips(response.data);
        } catch (err) {
            setError('Error fetching trips. Please try again.');
            setTrips([]);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="container mt-4">
            <h2>Search for Trips</h2>
            <form onSubmit={handleSearch} className="row g-3 align-items-end">
                <div className="col-md-3">
                    <label htmlFor="source" className="form-label">Source</label>
                    <input
                        type="text"
                        id="source"
                        className="form-control"
                        value={source}
                        onChange={(e) => setSource(e.target.value)}
                        required
                    />
                </div>
                <div className="col-md-3">
                    <label htmlFor="destination" className="form-label">Destination</label>
                    <input
                        type="text"
                        id="destination"
                        className="form-control"
                        value={destination}
                        onChange={(e) => setDestination(e.target.value)}
                        required
                    />
                </div>
                <div className="col-md-3">
                    <label htmlFor="date" className="form-label">Date</label>
                    <input
                        type="date"
                        id="date"
                        className="form-control"
                        value={date}
                        onChange={(e) => setDate(e.target.value)}
                        required
                    />
                </div>
                <div className="col-md-3">
                    <button type="submit" className="btn btn-primary w-100">Search</button>
                </div>
            </form>

            <hr />

            {loading && <p>Loading...</p>}
            {error && <div className="alert alert-danger">{error}</div>}

            {searched && !loading && trips.length === 0 && (
                <p>No trips found for the selected criteria.</p>
            )}

            {trips.length > 0 && (
                <div>
                    <h3>Available Trips</h3>
                    <ul className="list-group">
                        {trips.map(trip => (
                            <li key={trip.id} className="list-group-item d-flex justify-content-between align-items-center">
                                <div>
                                    <h5>{trip.bus.busNumber} - {trip.source} to {trip.destination}</h5>
                                    <p>Departure: {new Date(trip.departureTime).toLocaleString()}</p>
                                    <p>Arrival: {new Date(trip.arrivalTime).toLocaleString()}</p>
                                    <p>Fare: ${trip.fare}</p>
                                </div>
                                <Link to={`/trips/${trip.id}/seats`} className="btn btn-success">View Seats</Link>
                            </li>
                        ))}
                    </ul>
                </div>
            )}
        </div>
    );
};

export default SearchTrips;
