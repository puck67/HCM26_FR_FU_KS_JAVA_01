import React from 'react';
import { Link, useLocation } from 'react-router-dom';
import { CheckSquare, Home, ListTodo, PlusCircle } from 'lucide-react';

interface NavbarProps {
  onOpenCreateModal?: () => void;
}

export const Navbar: React.FC<NavbarProps> = ({ onOpenCreateModal }) => {
  const location = useLocation();

  const isActive = (path: string) => {
    return location.pathname === path;
  };

  return (
    <nav className="glass sticky top-0 z-50 px-6 py-4 flex items-center justify-between border-b border-white/10 shadow-lg">
      <div className="flex items-center gap-3">
        <div className="bg-indigo-600 p-2 rounded-xl text-white shadow-indigo-500/20 shadow-md">
          <CheckSquare className="w-6 h-6 animate-pulse" />
        </div>
        <div>
          <span className="font-bold text-xl tracking-wider bg-gradient-to-r from-white via-indigo-200 to-indigo-400 bg-clip-text text-transparent">
            ASM1
          </span>
          <span className="text-xs block text-slate-400 font-medium tracking-tight">Task Dashboard</span>
        </div>
      </div>

      <div className="flex items-center gap-6">
        <Link
          to="/"
          className={`flex items-center gap-2 px-4 py-2 rounded-lg font-medium text-sm transition-all duration-200 ${
            isActive('/')
              ? 'bg-indigo-600/10 text-indigo-400 border border-indigo-500/20'
              : 'text-slate-300 hover:text-white hover:bg-white/5'
          }`}
        >
          <Home className="w-4 h-4" />
          <span>Home</span>
        </Link>

        <Link
          to="/tasks"
          className={`flex items-center gap-2 px-4 py-2 rounded-lg font-medium text-sm transition-all duration-200 ${
            isActive('/tasks') || location.pathname.startsWith('/tasks/')
              ? 'bg-indigo-600/10 text-indigo-400 border border-indigo-500/20'
              : 'text-slate-300 hover:text-white hover:bg-white/5'
          }`}
        >
          <ListTodo className="w-4 h-4" />
          <span>Tasks</span>
        </Link>

        {onOpenCreateModal && (
          <button
            onClick={onOpenCreateModal}
            className="flex items-center gap-2 px-4 py-2 bg-indigo-600 hover:bg-indigo-500 active:bg-indigo-700 text-white rounded-lg font-medium text-sm shadow-lg shadow-indigo-600/25 transition-all duration-200"
          >
            <PlusCircle className="w-4 h-4" />
            <span className="hidden sm:inline">New Task</span>
          </button>
        )}
      </div>
    </nav>
  );
};
