import React from 'react';
import { Link } from 'react-router-dom';

const Home = () => {
    return (
        <div className="container mt-5">
            <div className="p-5 mb-4 bg-light rounded-3">
                <div className="container-fluid py-5">
                    <h1 className="display-5 fw-bold">Welcome to ADITYA Bus Booking</h1>
                    <p className="col-md-8 fs-4">
                        Your one-stop solution for booking bus tickets online. Fast, reliable, and easy to use.
                    </p>
                    <Link to="/search" className="btn btn-primary btn-lg">
                        Search for a Trip
                    </Link>
                </div>
            </div>
        </div>
    );
};

export default Home;
