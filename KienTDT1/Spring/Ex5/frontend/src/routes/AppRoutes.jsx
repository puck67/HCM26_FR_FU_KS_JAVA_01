import { Routes, Route, Navigate } from "react-router-dom";

import Login from "../pages/Login";
import Dashboard from "../pages/Dashboard";
import Courses from "../pages/Courses";
import CoursesManage from "../pages/CoursesManage";
import Users from "../pages/Users";

import ProtectedRoute from "../components/ProtectedRoute";

export default function AppRoutes() {
    const token = localStorage.getItem("token");

    return (
        <Routes>
            <Route
                path="/"
                element={<Navigate to={token ? "/dashboard" : "/login"} replace />}
            />

            <Route path="/login" element={<Login />} />

            <Route
                path="/dashboard"
                element={
                    <ProtectedRoute>
                        <Dashboard />
                    </ProtectedRoute>
                }
            />

            <Route
                path="/courses"
                element={
                    <ProtectedRoute>
                        <Courses />
                    </ProtectedRoute>
                }
            />

            <Route
                path="/courses/manage"
                element={
                    <ProtectedRoute>
                        <CoursesManage />
                    </ProtectedRoute>
                }
            />

            <Route
                path="/users"
                element={
                    <ProtectedRoute>
                        <Users />
                    </ProtectedRoute>
                }
            />

            <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
    );
}
