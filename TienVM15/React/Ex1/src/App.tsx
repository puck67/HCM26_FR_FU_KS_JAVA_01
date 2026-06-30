import { useState } from "react";
import TaskPage from "./pages/TaskPage";
import StudentPage from "./pages/StudentPage";
import TeacherPage from "./pages/TeacherPage";
import { ErrorBoundary } from "./components";
import "./App.css";

type Page = "tasks" | "students" | "teachers";

const navItems: { id: Page; label: string; icon: React.ReactNode }[] = [
  {
    id: "tasks",
    label: "Tasks",
    icon: (
      <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
        <path d="M9 11l3 3L22 4" />
        <path d="M21 12v7a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h11" />
      </svg>
    ),
  },
  {
    id: "students",
    label: "Students",
    icon: (
      <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
        <path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2" />
        <circle cx="9" cy="7" r="4" />
        <path d="M23 21v-2a4 4 0 0 0-3-3.87M16 3.13a4 4 0 0 1 0 7.75" />
      </svg>
    ),
  },
  {
    id: "teachers",
    label: "Teachers",
    icon: (
      <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
        <rect x="2" y="3" width="20" height="14" rx="2" />
        <path d="M8 21h8M12 17v4" />
      </svg>
    ),
  },
];

export default function App() {
  const [activePage, setActivePage] = useState<Page>("tasks");

  const renderPage = () => {
    switch (activePage) {
      case "tasks":    return <TaskPage />;
      case "students": return <StudentPage />;
      case "teachers": return <TeacherPage />;
    }
  };

  return (
    <div className="app-layout">
      {/* Sidebar */}
      <aside className="sidebar">
        <div className="sidebar__brand">
          <div className="sidebar__brand-icon">
            <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5">
              <polyline points="22 12 18 12 15 21 9 3 6 12 2 12" />
            </svg>
          </div>
          <span className="sidebar__brand-name">CRUDify</span>
        </div>

        <nav className="sidebar__nav">
          {navItems.map((item) => (
            <button
              key={item.id}
              className={`sidebar__nav-item ${activePage === item.id ? "sidebar__nav-item--active" : ""}`}
              onClick={() => setActivePage(item.id)}
            >
              <span className="sidebar__nav-icon">{item.icon}</span>
              <span className="sidebar__nav-label">{item.label}</span>
              {activePage === item.id && <span className="sidebar__nav-indicator" />}
            </button>
          ))}
        </nav>

        <div className="sidebar__footer">
          <span>Generic CRUD System</span>
          <span>v1.0</span>
        </div>
      </aside>

      {/* Main Content */}
      <main className="main-content">
        <div className="page-transition">
          <ErrorBoundary>
            {renderPage()}
          </ErrorBoundary>
        </div>
      </main>
    </div>
  );
}
