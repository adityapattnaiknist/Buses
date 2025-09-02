import React, { useState } from 'react';
import api from '../../api';

const Reports = () => {
    const [bookingReport, setBookingReport] = useState(null);
    const [ticketReport, setTicketReport] = useState(null);
    const [salesReport, setSalesReport] = useState(null);
    const [occupancyReport, setOccupancyReport] = useState(null);
    const [routePerformanceReport, setRoutePerformanceReport] = useState(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const fetchBookingReport = async () => {
        setLoading(true);
        setError(null);
        try {
            const response = await api.get('/reports/bookings');
            setBookingReport(response.data);
        } catch (error) {
            setError('Error fetching booking report');
        } finally {
            setLoading(false);
        }
    };

    const fetchTicketReport = async () => {
        setLoading(true);
        setError(null);
        try {
            const response = await api.get('/reports/tickets');
            setTicketReport(response.data);
        } catch (error) {
            setError('Error fetching ticket report');
        } finally {
            setLoading(false);
        }
    };

    const fetchSalesReport = async () => {
        setLoading(true);
        setError(null);
        try {
            const response = await api.get('/reports/sales');
            setSalesReport(response.data);
        } catch (error) {
            setError('Error fetching sales report');
        } finally {
            setLoading(false);
        }
    };

    const fetchOccupancyReport = async () => {
        setLoading(true);
        setError(null);
        try {
            const response = await api.get('/reports/occupancy');
            setOccupancyReport(response.data);
        } catch (error) {
            setError('Error fetching occupancy report');
        } finally {
            setLoading(false);
        }
    };

    const fetchRoutePerformanceReport = async () => {
        setLoading(true);
        setError(null);
        try {
            const response = await api.get('/reports/route-performance');
            setRoutePerformanceReport(response.data);
        } catch (error) {
            setError('Error fetching route performance report');
        } finally {
            setLoading(false);
        }
    };

    const renderTable = (data) => {
        if (!data || data.length === 0) {
            return <p>No data to display.</p>;
        }

        const headers = Object.keys(data[0]);

        return (
            <table className="table table-striped">
                <thead>
                    <tr>
                        {headers.map(header => <th key={header}>{header}</th>)}
                    </tr>
                </thead>
                <tbody>
                    {data.map((row, index) => (
                        <tr key={index}>
                            {headers.map(header => <td key={header}>{row[header]}</td>)}
                        </tr>
                    ))}
                </tbody>
            </table>
        );
    };

    return (
        <div className="container mt-4">
            <h2>Reports</h2>
            {error && <div className="alert alert-danger">{error}</div>}
            {loading && <p>Loading...</p>}

            <div className="mb-4">
                <button className="btn btn-primary" onClick={fetchBookingReport}>Generate Booking Report</button>
                {bookingReport && renderTable(bookingReport)}
            </div>

            <div className="mb-4">
                <button className="btn btn-primary" onClick={fetchTicketReport}>Generate Ticket Report</button>
                {ticketReport && renderTable(ticketReport)}
            </div>

            <div className="mb-4">
                <button className="btn btn-primary" onClick={fetchSalesReport}>Generate Sales Report</button>
                {salesReport && renderTable(salesReport)}
            </div>

            <div className="mb-4">
                <button className="btn btn-primary" onClick={fetchOccupancyReport}>Generate Occupancy Report</button>
                {occupancyReport && renderTable(occupancyReport)}
            </div>

            <div className="mb-4">
                <button className="btn btn-primary" onClick={fetchRoutePerformanceReport}>Generate Route Performance Report</button>
                {routePerformanceReport && renderTable(routePerformanceReport)}
            </div>
        </div>
    );
};

export default Reports;
