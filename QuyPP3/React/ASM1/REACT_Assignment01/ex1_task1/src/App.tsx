import { BrowserRouter, Routes, Route, Link } from "react-router-dom";
import { TaskProvider } from "./context/TaskContext";
import Home from "./pages/Home";
import Tasks from "./pages/Tasks";
import TaskDetail from "./pages/TaskDetail";

export default function App() {
  return (
    <TaskProvider>
      <BrowserRouter>
        <div className="min-h-screen bg-slate-50 text-slate-900 font-sans">
          {/* Main Navigation Bar */}
          <nav className="sticky top-0 bg-white/80 backdrop-blur-md border-b border-slate-200 z-40 shadow-sm">
            <div className="max-w-6xl mx-auto px-4 h-16 flex items-center justify-between">
              <Link to="/" className="text-md font-black tracking-tight text-slate-900 hover:opacity-80 transition">
                Task<span className="text-blue-600 font-medium">Dashboard</span>
              </Link>
              <div className="flex gap-6 text-sm font-semibold text-slate-600">
                <Link to="/" className="hover:text-blue-600 transition">Home</Link>
                <Link to="/tasks" className="hover:text-blue-600 transition">Workspace</Link>
              </div>
            </div>
          </nav>

          {/* Main Container */}
          <main className="max-w-6xl mx-auto px-4 py-8">
            <Routes>
              <Route path="/" element={<Home />} />
              <Route path="/tasks" element={<Tasks />} />
              <Route path="/tasks/:id" element={<TaskDetail />} />
            </Routes>
          </main>
        </div>
      </BrowserRouter>
    </TaskProvider>
  );
}
