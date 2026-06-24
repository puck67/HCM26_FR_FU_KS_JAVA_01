import { BrowserRouter as Router, Routes, Route, NavLink } from "react-router-dom";
import Home from "./pages/Home";
import Tasks from "./pages/Tasks";
import TaskDetail from "./pages/TaskDetail";
import { TaskProvider } from "./TaskContext";
import "./App.css";

export default function App() {
  return (
    <TaskProvider>
      <Router>
        {/* Sidebar điều hướng */}
        <aside className="sidebar">
          <div className="brand">
            <div className="brand-icon">
              <svg viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                <path
                  d="M12 2L2 7l10 5 10-5-10-5zM2 17l10 5 10-5M2 12l10 5 10-5"
                  stroke="#fff"
                  strokeWidth="2"
                  fill="none"
                  strokeLinecap="round"
                  strokeLinejoin="round"
                />
              </svg>
            </div>
            <span className="brand-name">TaskBoard</span>
          </div>

          <nav className="nav-menu">
            <NavLink
              to="/"
              className={({ isActive }) => `nav-item ${isActive ? "active" : ""}`}
            >
              <svg
                fill="none"
                stroke="currentColor"
                strokeWidth="2"
                viewBox="0 0 24 24"
                xmlns="http://www.w3.org/2000/svg"
              >
                <rect x="3" y="3" width="7" height="9" rx="1" />
                <rect x="14" y="3" width="7" height="5" rx="1" />
                <rect x="14" y="12" width="7" height="9" rx="1" />
                <rect x="3" y="16" width="7" height="5" rx="1" />
              </svg>
              Trang chủ
            </NavLink>
            
            <NavLink
              to="/tasks"
              className={({ isActive }) => `nav-item ${isActive ? "active" : ""}`}
            >
              <svg
                fill="none"
                stroke="currentColor"
                strokeWidth="2"
                viewBox="0 0 24 24"
                xmlns="http://www.w3.org/2000/svg"
              >
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2m-6 9l2 2 4-4"
                />
              </svg>
              Bảng công việc
            </NavLink>
          </nav>
        </aside>

        {/* Khung nội dung chính */}
        <main className="main-content">
          <header className="header">
            <div className="header-title">
              <h1>Quản Lý Công Việc</h1>
              <p>Bảng điều khiển tác vụ phần mềm sử dụng React + TypeScript + Reducer.</p>
            </div>
          </header>

          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/tasks" element={<Tasks />} />
            <Route path="/tasks/:id" element={<TaskDetail />} />
          </Routes>
        </main>
      </Router>
    </TaskProvider>
  );
}
