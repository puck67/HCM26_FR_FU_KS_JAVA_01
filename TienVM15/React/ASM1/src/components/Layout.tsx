import React from 'react';
import { Link, NavLink, Outlet } from 'react-router-dom';
import { LayoutDashboard, CheckSquare, Sparkles } from 'lucide-react';

export const Layout: React.FC = () => {
  return (
    <div className="min-h-screen bg-slate-900 text-slate-100 flex flex-col font-sans">
      {/* Top Header navbar */}
      <header className="border-b border-slate-800 bg-slate-950/80 backdrop-blur-md sticky top-0 z-50">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 h-16 flex items-center justify-between">
          <Link to="/" className="flex items-center gap-3 hover:opacity-90 transition-opacity">
            <div className="bg-gradient-to-tr from-violet-600 to-indigo-600 p-2 rounded-xl text-white shadow-lg shadow-indigo-500/20">
              <Sparkles className="w-5 h-5" />
            </div>
            <span className="font-extrabold text-xl tracking-tight bg-gradient-to-r from-violet-400 to-indigo-200 bg-clip-text text-transparent">
              TaskFlow
            </span>
          </Link>

          <nav className="flex items-center gap-2">
            <NavLink
              to="/"
              className={({ isActive }) =>
                `flex items-center gap-2 px-4 py-2 rounded-lg font-medium text-sm transition-all ${
                  isActive
                    ? 'bg-slate-800 text-white shadow-inner'
                    : 'text-slate-400 hover:text-white hover:bg-slate-800/50'
                }`
              }
            >
              <LayoutDashboard className="w-4 h-4" />
              Dashboard
            </NavLink>
            <NavLink
              to="/tasks"
              className={({ isActive }) =>
                `flex items-center gap-2 px-4 py-2 rounded-lg font-medium text-sm transition-all ${
                  isActive
                    ? 'bg-slate-800 text-white shadow-inner'
                    : 'text-slate-400 hover:text-white hover:bg-slate-800/50'
                }`
              }
            >
              <CheckSquare className="w-4 h-4" />
              Tasks
            </NavLink>
          </nav>
        </div>
      </header>

      {/* Main content page area */}
      <main className="flex-1 max-w-7xl w-full mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <Outlet />
      </main>

      {/* Modern footer */}
      <footer className="border-t border-slate-800/50 bg-slate-950/20 py-6 text-center text-xs text-slate-500">
        <p>&copy; {new Date().getFullYear()} TaskFlow Dashboard. Built with React + TypeScript + Tailwind CSS.</p>
      </footer>
    </div>
  );
};
