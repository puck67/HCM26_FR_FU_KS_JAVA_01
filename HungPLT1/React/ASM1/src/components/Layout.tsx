import React from 'react';
import { NavLink, Link, Outlet } from 'react-router-dom';
import { LayoutDashboard, CheckSquare, PlusCircle } from 'lucide-react';
import { useTasks } from '../context/TaskContext';

export const Layout: React.FC = () => {
  const { tasks } = useTasks();

  const total = tasks.length;
  const pending = tasks.filter(t => t.status === 'pending').length;
  const inProgress = tasks.filter(t => t.status === 'in_progress').length;
  const completed = tasks.filter(t => t.status === 'completed').length;

  return (
    <div className="min-h-screen bg-slate-950 text-slate-100 flex flex-col md:flex-row font-sans selection:bg-indigo-500/30 selection:text-indigo-200">
      <aside className="w-full md:w-64 bg-slate-900 border-b md:border-b-0 md:border-r border-slate-800/80 flex flex-col md:sticky md:top-0 md:h-screen transition-all duration-300">
        <div className="p-6 border-b border-slate-800/80">
          <Link to="/" className="flex items-center gap-3 group">
            <div className="w-9 h-9 rounded-xl bg-gradient-to-tr from-indigo-600 to-violet-500 flex items-center justify-center shadow-lg shadow-indigo-500/20 group-hover:scale-105 transition-transform duration-300">
              <CheckSquare className="w-5 h-5 text-white" />
            </div>
            <div>
              <span className="font-bold text-lg bg-gradient-to-r from-indigo-200 to-slate-200 bg-clip-text text-transparent">TaskFlow</span>
              <span className="block text-[10px] text-indigo-400 font-semibold tracking-wider uppercase">Dashboard</span>
            </div>
          </Link>
        </div>

        <nav className="flex-1 p-4 space-y-1.5">
          <NavLink
            to="/"
            end
            className={({ isActive }) =>
              `flex items-center gap-3 px-4 py-3 rounded-xl text-sm font-medium transition-all duration-200 ${
                isActive
                  ? 'bg-indigo-600 text-white shadow-lg shadow-indigo-600/10'
                  : 'text-slate-400 hover:bg-slate-800/60 hover:text-slate-200'
              }`
            }
          >
            <LayoutDashboard className="w-4 h-4" />
            <span>Dashboard Home</span>
          </NavLink>

          <NavLink
            to="/tasks"
            className={({ isActive }) =>
              `flex items-center gap-3 px-4 py-3 rounded-xl text-sm font-medium transition-all duration-200 ${
                isActive
                  ? 'bg-indigo-600 text-white shadow-lg shadow-indigo-600/10'
                  : 'text-slate-400 hover:bg-slate-800/60 hover:text-slate-200'
              }`
            }
          >
            <CheckSquare className="w-4 h-4" />
            <span>Tasks Manager</span>
          </NavLink>
        </nav>

        <div className="p-4 border-t border-slate-800/80">
          <Link
            to="/tasks/new"
            className="flex items-center justify-center gap-2 w-full py-3 bg-gradient-to-r from-indigo-600 to-indigo-700 hover:from-indigo-500 hover:to-indigo-600 active:from-indigo-700 active:to-indigo-800 text-white text-sm font-semibold rounded-xl shadow-lg shadow-indigo-950/40 hover:shadow-indigo-500/10 transition-all duration-300"
          >
            <PlusCircle className="w-4 h-4" />
            <span>Create Task</span>
          </Link>
        </div>
      </aside>

      <main className="flex-1 flex flex-col min-w-0">
        <header className="sticky top-0 z-10 bg-slate-950/80 backdrop-blur-md border-b border-slate-800/50 px-6 py-4">
          <div className="flex flex-col sm:flex-row sm:items-center sm:justify-end gap-4">
            <div className="flex items-center gap-6 overflow-x-auto py-1 scrollbar-none">
              <div className="flex items-center gap-2 shrink-0">
                <div className="w-2 h-2 rounded-full bg-slate-400"></div>
                <span className="text-xs text-slate-400 font-medium">Total: <strong className="text-slate-200 font-semibold">{total}</strong></span>
              </div>
              <div className="flex items-center gap-2 shrink-0">
                <div className="w-2 h-2 rounded-full bg-amber-400"></div>
                <span className="text-xs text-slate-400 font-medium">Pending: <strong className="text-slate-200 font-semibold">{pending}</strong></span>
              </div>
              <div className="flex items-center gap-2 shrink-0">
                <div className="w-2 h-2 rounded-full bg-sky-400"></div>
                <span className="text-xs text-slate-400 font-medium">In Progress: <strong className="text-slate-200 font-semibold">{inProgress}</strong></span>
              </div>
              <div className="flex items-center gap-2 shrink-0">
                <div className="w-2 h-2 rounded-full bg-emerald-400"></div>
                <span className="text-xs text-slate-400 font-medium">Completed: <strong className="text-slate-200 font-semibold">{completed}</strong></span>
              </div>
            </div>
          </div>
        </header>

        <div className="flex-1 p-6 md:p-8 max-w-7xl mx-auto w-full">
          <Outlet />
        </div>
      </main>
    </div>
  );
};
