import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import { AuthProvider } from "./context/AuthContext";
import ProtectedRoute from "./components/ProtectedRoute";

import Login from "./pages/Auth/Login";
import Register from "./pages/Auth/Register";
import SearchTrips from "./pages/Customer/SearchTrips";
import SeatSelection from "./pages/Customer/SeatSelection";
import Checkout from "./pages/Customer/Checkout";
import Ticket from "./pages/Customer/Ticket";
import MyBookings from "./pages/Customer/MyBookings";
import AdminDashboard from "./pages/Admin/AdminDashboard";
import ManageBuses from "./pages/Admin/ManageBuses";
import ManageRoutes from "./pages/Admin/ManageRoutes";
import ManageTrips from "./pages/Admin/ManageTrips";
import Reports from "./pages/Admin/Reports";

function App() {
  return (
    <AuthProvider>
      <Router>
        <Routes>
          {/* Auth */}
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />

          {/* Customer Routes */}
          <Route
            path="/search"
            element={<ProtectedRoute role="customer"><SearchTrips /></ProtectedRoute>}
          />
          <Route
            path="/trips/:id/seats"
            element={<ProtectedRoute role="customer"><SeatSelection /></ProtectedRoute>}
          />
          <Route
            path="/checkout/:bookingId"
            element={<ProtectedRoute role="customer"><Checkout /></ProtectedRoute>}
          />
          <Route
            path="/ticket/:ticketId"
            element={<ProtectedRoute role="customer"><Ticket /></ProtectedRoute>}
          />
          <Route
            path="/my-bookings"
            element={<ProtectedRoute role="customer"><MyBookings /></ProtectedRoute>}
          />

          {/* Admin Routes */}
          <Route
            path="/admin"
            element={<ProtectedRoute role="admin"><AdminDashboard /></ProtectedRoute>}
          >
            <Route path="buses" element={<ManageBuses />} />
            <Route path="routes" element={<ManageRoutes />} />
            <Route path="trips" element={<ManageTrips />} />
            <Route path="reports" element={<Reports />} />
          </Route>
        </Routes>
      </Router>
    </AuthProvider>
  );
}

export default App;
