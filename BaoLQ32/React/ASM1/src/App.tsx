import { BrowserRouter, Routes, Route, NavLink } from "react-router-dom";
import { TaskProvider } from "./TaskContext";
import Home from "./pages/Home";
import Tasks from "./pages/Tasks";
import TaskDetail from "./pages/TaskDetail";

export default function App() {
  return (
    <TaskProvider>
      <BrowserRouter>
        <div className="flex min-h-screen bg-[#0a0b12] text-white font-sans">

          {/* ─── Thanh điều hướng bên trái ─── */}
          <aside className="w-64 flex-shrink-0 bg-[#0e0f1c] border-r border-white/[0.06] flex flex-col gap-8 px-6 py-10">
            {/* Thương hiệu */}
            <div className="flex items-center gap-3">
              <div className="w-9 h-9 rounded-xl bg-gradient-to-br from-violet-500 to-blue-500 flex items-center justify-center shadow-lg shadow-violet-500/30">
                <svg viewBox="0 0 24 24" fill="none" stroke="white" strokeWidth="2" className="w-5 h-5">
                  <path
                    d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2m-6 9l2 2 4-4"
                    strokeLinecap="round"
                    strokeLinejoin="round"
                  />
                </svg>
              </div>
              <span className="font-bold text-lg bg-gradient-to-r from-white via-violet-200 to-blue-300 bg-clip-text text-transparent">
                TaskBoard
              </span>
            </div>

            {/* Điều hướng */}
            <nav className="flex flex-col gap-1">
              <NavLink
                to="/"
                end
                className={({ isActive }) =>
                  `flex items-center gap-3 px-4 py-2.5 rounded-xl text-sm font-medium transition-all ${
                    isActive
                      ? "bg-violet-500/20 text-white border border-violet-500/30"
                      : "text-slate-400 hover:text-white hover:bg-white/5"
                  }`
                }
              >
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" className="w-4 h-4">
                  <rect x="3" y="3" width="7" height="7" rx="1" />
                  <rect x="14" y="3" width="7" height="7" rx="1" />
                  <rect x="3" y="14" width="7" height="7" rx="1" />
                  <rect x="14" y="14" width="7" height="7" rx="1" />
                </svg>
                Trang Chủ
              </NavLink>

              <NavLink
                to="/tasks"
                className={({ isActive }) =>
                  `flex items-center gap-3 px-4 py-2.5 rounded-xl text-sm font-medium transition-all ${
                    isActive
                      ? "bg-violet-500/20 text-white border border-violet-500/30"
                      : "text-slate-400 hover:text-white hover:bg-white/5"
                  }`
                }
              >
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" className="w-4 h-4">
                  <path
                    d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2m-6 9l2 2 4-4"
                    strokeLinecap="round"
                    strokeLinejoin="round"
                  />
                </svg>
                Bảng Công Việc
              </NavLink>
            </nav>

            {/* Thông tin dưới cùng */}
            <div className="mt-auto text-xs text-slate-600 leading-relaxed">
              React + TypeScript + Vite<br />
              Formik · Yup · React Router
            </div>
          </aside>

          {/* ─── Nội dung chính ─── */}
          <main className="flex-1 flex flex-col overflow-y-auto">
            <header className="px-10 py-6 border-b border-white/[0.06] flex items-center justify-between">
              <div>
                <h1 className="text-2xl font-bold text-white tracking-tight">Bảng Điều Khiển Công Việc</h1>
                <p className="text-sm text-slate-400 mt-0.5">
                  REACT_Assignment01 — Vite + Hooks + Reducer
                </p>
              </div>
            </header>

            <div className="flex-1 px-10 py-8">
              <Routes>
                <Route path="/" element={<Home />} />
                <Route path="/tasks" element={<Tasks />} />
                <Route path="/tasks/:id" element={<TaskDetail />} />
              </Routes>
            </div>
          </main>
        </div>
      </BrowserRouter>
    </TaskProvider>
  );
}
