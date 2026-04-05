import React from "react";
import { render, screen } from "@testing-library/react";
import { MemoryRouter, Routes, Route } from "react-router-dom";
import ProtectedRoute from "./ProtectedRoute";
import { AuthContext } from "../context/AuthContext";

// Helper component to test navigation targets
const MockComponent = ({ name }) => <div>{name} Component</div>;

const renderWithAuth = (ui, providerProps, { route = '/' } = {}) => {
  return render(
    <AuthContext.Provider value={providerProps}>
      <MemoryRouter initialEntries={[route]}>
        <Routes>
          <Route path="/" element={<MockComponent name="Home" />} />
          <Route path="/login" element={<MockComponent name="Login" />} />
          <Route path="/protected" element={ui} />
        </Routes>
      </MemoryRouter>
    </AuthContext.Provider>
  );
};

describe("ProtectedRoute Component", () => {
  it("navigates to /login if user is not authenticated", () => {
    const providerProps = { user: null };

    renderWithAuth(
      <ProtectedRoute>
        <MockComponent name="Protected Content" />
      </ProtectedRoute>,
      providerProps,
      { route: '/protected' }
    );

    // Should navigate to /login
    expect(screen.getByText("Login Component")).toBeInTheDocument();
    expect(screen.queryByText("Protected Content Component")).not.toBeInTheDocument();
  });

  it("navigates to / if user has incorrect role", () => {
    const providerProps = { user: { role: 'user' } };

    renderWithAuth(
      <ProtectedRoute role="admin">
        <MockComponent name="Protected Content" />
      </ProtectedRoute>,
      providerProps,
      { route: '/protected' }
    );

    // Should navigate to /
    expect(screen.getByText("Home Component")).toBeInTheDocument();
    expect(screen.queryByText("Protected Content Component")).not.toBeInTheDocument();
  });

  it("renders children if user has correct role", () => {
    const providerProps = { user: { role: 'admin' } };

    renderWithAuth(
      <ProtectedRoute role="admin">
        <MockComponent name="Protected Content" />
      </ProtectedRoute>,
      providerProps,
      { route: '/protected' }
    );

    // Should render children
    expect(screen.getByText("Protected Content Component")).toBeInTheDocument();
  });

  it("renders children if user is authenticated and no role is required", () => {
    const providerProps = { user: { role: 'user' } };

    renderWithAuth(
      <ProtectedRoute>
        <MockComponent name="Protected Content" />
      </ProtectedRoute>,
      providerProps,
      { route: '/protected' }
    );

    // Should render children
    expect(screen.getByText("Protected Content Component")).toBeInTheDocument();
  });
});
