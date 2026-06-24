import { useState } from "react";
import { DashboardPage } from "./DashboardPage";
import TaskPage, { tasks as initialTasks } from "./TaskPage";
import type { Task } from "./TaskPage";
import StudentPage, { students as initialStudents } from "./StudentPage";
import type { Student } from "./StudentPage";

export function AdminPage() {
  // Navigation States
  const [activeTab, setActiveTab] = useState<"dashboard" | "tasks" | "students">("dashboard");
  const [isMobileMenuOpen, setIsMobileMenuOpen] = useState(false);

  // Shared Data States (so Dashboard can calculate combined metrics in real time)
  const [tasks, setTasks] = useState<Task[]>(initialTasks);
  const [students, setStudents] = useState<Student[]>(initialStudents);

  return (
    <div className="flex min-h-screen bg-brand-bg text-brand-text">
      {/* Sidebar Navigation - Desktop */}
      <aside className="hidden md:flex flex-col w-64 bg-slate-900/80 border-r border-brand-border shrink-0 p-5 space-y-6 backdrop-blur-md">
        <div className="flex items-center gap-2.5 pb-4 border-b border-brand-border">
          <span className="p-2 rounded-xl bg-indigo-500/10 border border-indigo-500/20 text-indigo-400">
            <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2.5" d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z" />
            </svg>
          </span>
          <div>
            <h1 className="font-extrabold tracking-tight text-brand-text text-lg">Admin Control</h1>
            <p className="text-2xs text-brand-text-muted">Enterprise Portal</p>
          </div>
        </div>

        <nav className="flex-1 space-y-1.5">
          <button
            onClick={() => setActiveTab("dashboard")}
            className={`w-full flex items-center gap-3 px-4 py-3 rounded-xl text-sm font-semibold transition cursor-pointer ${
              activeTab === "dashboard"
                ? "bg-indigo-600 text-white shadow-lg shadow-indigo-600/20"
                : "text-brand-text-muted hover:text-brand-text hover:bg-slate-800/50"
            }`}
          >
            <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M4 6a2 2 0 012-2h2a2 2 0 012 2v4a2 2 0 01-2 2H6a2 2 0 01-2-2V6zM14 6a2 2 0 012-2h2a2 2 0 012 2v4a2 2 0 01-2 2h-2a2 2 0 01-2-2V6zM4 16a2 2 0 012-2h2a2 2 0 012 2v4a2 2 0 01-2 2H6a2 2 0 01-2-2v-4zM14 16a2 2 0 012-2h2a2 2 0 012 2v4a2 2 0 01-2 2h-2a2 2 0 01-2-2v-4z" />
            </svg>
            Dashboard
          </button>
          
          <button
            onClick={() => setActiveTab("tasks")}
            className={`w-full flex items-center gap-3 px-4 py-3 rounded-xl text-sm font-semibold transition cursor-pointer ${
              activeTab === "tasks"
                ? "bg-indigo-600 text-white shadow-lg shadow-indigo-600/20"
                : "text-brand-text-muted hover:text-brand-text hover:bg-slate-800/50"
            }`}
          >
            <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" />
            </svg>
            Tasks Manager
          </button>

          <button
            onClick={() => setActiveTab("students")}
            className={`w-full flex items-center gap-3 px-4 py-3 rounded-xl text-sm font-semibold transition cursor-pointer ${
              activeTab === "students"
                ? "bg-indigo-600 text-white shadow-lg shadow-indigo-600/20"
                : "text-brand-text-muted hover:text-brand-text hover:bg-slate-800/50"
            }`}
          >
            <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M12 14l9-5-9-5-9 5 9 5zm0 0l6.16-3.422a12.083 12.083 0 01.665 6.479A11.952 11.952 0 0012 20.055a11.952 11.952 0 00-6.824-2.998 12.078 12.078 0 01.665-6.479L12 14zm-4 6v-7.5l4-2.222" />
            </svg>
            Students Manager
          </button>
        </nav>
        
        <div className="pt-4 border-t border-brand-border text-center text-xs text-brand-text-muted">
          v1.2.0 • Light Theme Enabled
        </div>
      </aside>

      {/* Sidebar Navigation - Mobile Drawer Backdrop */}
      {isMobileMenuOpen && (
        <div 
          className="fixed inset-0 z-40 bg-slate-950/70 backdrop-blur-sm md:hidden"
          onClick={() => setIsMobileMenuOpen(false)}
        />
      )}

      {/* Sidebar Navigation - Mobile Drawer Content */}
      <aside className={`fixed inset-y-0 left-0 z-50 w-64 bg-slate-900 border-r border-brand-border p-5 flex flex-col space-y-6 transition-transform duration-300 md:hidden ${
        isMobileMenuOpen ? "translate-x-0" : "-translate-x-0 -translate-x-full"
      }`}>
        <div className="flex items-center justify-between pb-4 border-b border-brand-border">
          <div className="flex items-center gap-2">
            <span className="p-1.5 rounded-lg bg-indigo-500/10 border border-indigo-500/20 text-indigo-400">
              <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z" />
              </svg>
            </span>
            <span className="font-extrabold text-brand-text">Admin Control</span>
          </div>
          <button 
            onClick={() => setIsMobileMenuOpen(false)}
            className="text-brand-text-muted hover:text-brand-text p-1.5 rounded-lg bg-slate-800"
          >
            <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        </div>

        <nav className="flex-1 space-y-1.5">
          <button
            onClick={() => { setActiveTab("dashboard"); setIsMobileMenuOpen(false); }}
            className={`w-full flex items-center gap-3 px-4 py-3 rounded-xl text-sm font-semibold transition cursor-pointer ${
              activeTab === "dashboard"
                ? "bg-indigo-600 text-white"
                : "text-brand-text-muted hover:text-brand-text hover:bg-slate-800/50"
            }`}
          >
            Dashboard
          </button>
          
          <button
            onClick={() => { setActiveTab("tasks"); setIsMobileMenuOpen(false); }}
            className={`w-full flex items-center gap-3 px-4 py-3 rounded-xl text-sm font-semibold transition cursor-pointer ${
              activeTab === "tasks"
                ? "bg-indigo-600 text-white"
                : "text-brand-text-muted hover:text-brand-text hover:bg-slate-800/50"
            }`}
          >
            Tasks Manager
          </button>

          <button
            onClick={() => { setActiveTab("students"); setIsMobileMenuOpen(false); }}
            className={`w-full flex items-center gap-3 px-4 py-3 rounded-xl text-sm font-semibold transition cursor-pointer ${
              activeTab === "students"
                ? "bg-indigo-600 text-white"
                : "text-brand-text-muted hover:text-brand-text hover:bg-slate-800/50"
            }`}
          >
            Students Manager
          </button>
        </nav>
      </aside>

      {/* Main Content Area */}
      <div className="flex-1 flex flex-col min-w-0">
        {/* Mobile Header Bar */}
        <header className="md:hidden flex items-center justify-between bg-slate-900/60 border-b border-brand-border p-4 backdrop-blur-md">
          <div className="flex items-center gap-2">
            <span className="p-1.5 rounded-lg bg-indigo-500/10 text-indigo-400">
              <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M4 6a2 2 0 012-2h2a2 2 0 012 2v4a2 2 0 01-2 2H6a2 2 0 01-2-2V6z" />
              </svg>
            </span>
            <span className="font-extrabold text-brand-text capitalize">{activeTab} Panel</span>
          </div>
          <button 
            onClick={() => setIsMobileMenuOpen(true)}
            className="p-2 rounded-lg bg-slate-800 text-brand-text hover:bg-slate-700"
          >
            <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M4 6h16M4 12h16M4 18h16" />
            </svg>
          </button>
        </header>

        {/* Scrollable Container */}
        <main className="flex-1 overflow-y-auto px-4 py-8 md:p-8 space-y-6 max-w-6xl w-full mx-auto">
          {activeTab === "dashboard" && (
            <DashboardPage
              tasks={tasks}
              students={students}
              setActiveTab={setActiveTab}
              onQuickAddTask={() => setActiveTab("tasks")}
              onQuickAddStudent={() => setActiveTab("students")}
            />
          )}

          {activeTab === "tasks" && (
            <TaskPage tasks={tasks} setTasks={setTasks} />
          )}

          {activeTab === "students" && (
            <StudentPage students={students} setStudents={setStudents} />
          )}
        </main>
      </div>
    </div>
  );
}
