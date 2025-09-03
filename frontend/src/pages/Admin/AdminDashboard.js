import React from 'react';
import { Link, Outlet } from 'react-router-dom';

const AdminDashboard = () => {
    return (
        <div style={{ display: 'flex' }}>
            <nav style={{ width: '200px', borderRight: '1px solid #ccc', padding: '20px' }}>
                <h2>Admin Menu</h2>
                <ul>
                    <li><Link to="buses">Manage Buses</Link></li>
                    <li><Link to="routes">Manage Routes</Link></li>
                    <li><Link to="trips">Manage Trips</Link></li>
                    <li><Link to="seats">Manage Seats</Link></li>
                    <li><Link to="bookings">Manage Bookings</Link></li>
                    <li><Link to="reports">View Reports</Link></li>
                    <li><Link to="notifications">Send Notification</Link></li>
                </ul>
            </nav>
            <main style={{ flex: 1, padding: '20px' }}>
                <Outlet />
            </main>
        </div>
    );
};

export default AdminDashboard;
