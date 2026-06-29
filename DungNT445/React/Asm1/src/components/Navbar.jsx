import React from 'react';
import { Link, useLocation } from 'react-router-dom';
import { ListTodo, CheckSquare, Home, PlusCircle } from 'lucide-react';

export const Navbar = () => {
  const location = useLocation();

  const isActive = (path) => {
    return location.pathname === path
      ? 'bg-indigo-600 text-white shadow-md'
      : 'text-slate-600 hover:bg-slate-100 hover:text-slate-900';
  };

  return (
    <nav className="sticky top-0 z-50 bg-white/80 backdrop-blur-md border-b border-slate-200">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex items-center justify-between h-16">
          <div className="flex items-center">
            <Link to="/" className="flex items-center gap-2 text-indigo-600 font-bold text-xl transition-all hover:scale-105">
              <CheckSquare className="w-8 h-8 text-indigo-600" />
              <span className="bg-gradient-to-r from-indigo-600 to-violet-600 bg-clip-text text-transparent">
                TaskFlow
              </span>
            </Link>
          </div>
          
          <div className="flex gap-2">
            <Link
              to="/"
              className={`flex items-center gap-2 px-4 py-2 rounded-lg text-sm font-medium transition-all ${isActive('/')}`}
            >
              <Home className="w-4 h-4" />
              <span className="hidden sm:inline">Dashboard</span>
            </Link>
            
            <Link
              to="/tasks"
              className={`flex items-center gap-2 px-4 py-2 rounded-lg text-sm font-medium transition-all ${isActive('/tasks')}`}
            >
              <ListTodo className="w-4 h-4" />
              <span className="hidden sm:inline">Tasks</span>
            </Link>
            
            <Link
              to="/tasks/new"
              className="flex items-center gap-2 px-4 py-2 rounded-lg text-sm font-medium bg-gradient-to-r from-indigo-500 to-violet-600 text-white shadow-sm hover:from-indigo-600 hover:to-violet-700 transition-all hover:scale-105"
            >
              <PlusCircle className="w-4 h-4" />
              <span>New Task</span>
            </Link>
          </div>
        </div>
      </div>
    </nav>
  );
};
