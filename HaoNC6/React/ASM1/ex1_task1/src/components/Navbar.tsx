import React from 'react';
import { NavLink, Link } from 'react-router-dom';
import { CheckSquare, Home, ListTodo } from 'lucide-react';

export const Navbar: React.FC = () => {
  return (
    <nav className="glass-nav sticky top-0 z-50 px-4 md:px-8 py-4 flex items-center justify-between">
      <Link to="/" className="flex items-center gap-2 group">
        <div className="p-2 bg-violet-600/20 text-violet-400 rounded-xl border border-violet-500/30 group-hover:border-violet-500/60 transition-colors duration-300">
          <CheckSquare size={24} />
        </div>
        <span className="font-bold text-xl tracking-tight bg-gradient-to-r from-white to-gray-300 bg-clip-text text-transparent group-hover:from-violet-300 group-hover:to-indigo-300 transition-all duration-300">
          Task<span className="text-violet-400">Sphere</span>
        </span>
      </Link>

      <div className="flex items-center gap-6">
        <NavLink
          to="/"
          className={({ isActive }) =>
            `flex items-center gap-1.5 px-3 py-1.5 rounded-lg text-sm font-medium transition-all duration-300 ${
              isActive
                ? 'text-violet-400 bg-violet-500/10 border border-violet-500/20'
                : 'text-gray-400 hover:text-gray-200 hover:bg-white/5 border border-transparent'
            }`
          }
        >
          <Home size={16} />
          <span>Home</span>
        </NavLink>

        <NavLink
          to="/tasks"
          className={({ isActive }) =>
            `flex items-center gap-1.5 px-3 py-1.5 rounded-lg text-sm font-medium transition-all duration-300 ${
              isActive
                ? 'text-violet-400 bg-violet-500/10 border border-violet-500/20'
                : 'text-gray-400 hover:text-gray-200 hover:bg-white/5 border border-transparent'
            }`
          }
        >
          <ListTodo size={16} />
          <span>Tasks</span>
        </NavLink>
      </div>
    </nav>
  );
};
