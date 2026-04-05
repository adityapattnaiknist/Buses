import React, { useState, useEffect } from 'react';
import api from '../../api';

const SendNotification = () => {
    const [users, setUsers] = useState([]);
    const [loadingUsers, setLoadingUsers] = useState(true);
    const [error, setError] = useState(null);
    const [successMessage, setSuccessMessage] = useState(null);

    // Form state
    const [userId, setUserId] = useState('');
    const [type, setType] = useState('GENERAL');
    const [message, setMessage] = useState('');
    const [sending, setSending] = useState(false);

    useEffect(() => {
        fetchUsers();
    }, []);

    const fetchUsers = async () => {
        try {
            setLoadingUsers(true);
            const response = await api.get('/users');
            setUsers(response.data);
        } catch (err) {
            setError('Failed to fetch users.');
        } finally {
            setLoadingUsers(false);
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setSending(true);
        setError(null);
        setSuccessMessage(null);

        const notificationData = {
            message,
            type,
            user: { id: parseInt(userId) }
        };

        try {
            await api.post('/notifications', notificationData);
            setSuccessMessage('Notification sent successfully!');
            resetForm();
        } catch (err) {
            setError('Failed to send notification.');
        } finally {
            setSending(false);
        }
    };

    const resetForm = () => {
        setUserId('');
        setType('GENERAL');
        setMessage('');
    };

    return (
        <div className="container mt-4">
            <h2>Send Notification</h2>

            {error && <div className="alert alert-danger">{error}</div>}
            {successMessage && <div className="alert alert-success">{successMessage}</div>}

            <form onSubmit={handleSubmit} className="mb-4" style={{ maxWidth: '600px' }}>
                <div className="mb-3">
                    <label htmlFor="userSelect" className="form-label">Select User</label>
                    <select
                        id="userSelect"
                        className="form-select"
                        value={userId}
                        onChange={e => setUserId(e.target.value)}
                        required
                        disabled={loadingUsers}
                    >
                        <option value="">{loadingUsers ? 'Loading users...' : '-- Select User --'}</option>
                        {users.map(user => (
                            <option key={user.id} value={user.id}>
                                {user.username || user.email || `User ${user.id}`}
                            </option>
                        ))}
                    </select>
                </div>

                <div className="mb-3">
                    <label htmlFor="typeSelect" className="form-label">Notification Type</label>
                    <select
                        id="typeSelect"
                        className="form-select"
                        value={type}
                        onChange={e => setType(e.target.value)}
                        required
                    >
                        <option value="GENERAL">General</option>
                        <option value="BOOKING_CONFIRMATION">Booking Confirmation</option>
                        <option value="BOOKING_CANCELLATION">Booking Cancellation</option>
                    </select>
                </div>

                <div className="mb-3">
                    <label htmlFor="messageTextarea" className="form-label">Message</label>
                    <textarea
                        id="messageTextarea"
                        className="form-control"
                        rows="4"
                        placeholder="Enter notification message here..."
                        value={message}
                        onChange={e => setMessage(e.target.value)}
                        required
                    ></textarea>
                </div>

                <button type="submit" className="btn btn-primary" disabled={sending || loadingUsers}>
                    {sending ? 'Sending...' : 'Send Notification'}
                </button>
            </form>
        </div>
    );
};

export default SendNotification;
