import React from 'react';
import { NavLink } from 'react-router-dom';
import { Home, CheckSquare, ShieldAlert, ShieldCheck } from 'lucide-react';
import { useTasks } from '../../context/TaskContext';

export const Navbar: React.FC = () => {
  const { isApiFailing, toggleApiError } = useTasks();

  return (
    <nav className="sticky top-0 z-50 glassmorphism border-b border-white/5 py-4 px-6 mb-8">
      <div className="max-w-7xl mx-auto flex flex-col sm:flex-row justify-between items-center gap-4">
        {/* Logo / Brand */}
        <div className="flex items-center gap-2">
          <div className="h-10 w-10 rounded-xl bg-gradient-to-tr from-cyber-pink to-cyber-purple flex items-center justify-center border-glow-purple">
            <CheckSquare className="h-5 w-5 text-white" />
          </div>
          <div>
            <h1 className="font-orbitron font-black text-lg bg-gradient-to-r from-cyber-pink via-cyber-purple to-cyber-cyan bg-clip-text text-transparent">
              CYBER_TASK
            </h1>
          </div>
        </div>

        {/* Nav Links */}
        <div className="flex items-center gap-4 bg-black/40 p-1.5 rounded-xl border border-white/5">
          <NavLink
            to="/"
            className={({ isActive }) =>
              `flex items-center gap-2 px-4 py-2 rounded-lg font-medium text-sm transition-all duration-200 ${
                isActive
                  ? 'bg-gradient-to-r from-cyber-purple to-indigo-600 text-white shadow-lg shadow-cyber-purple/35'
                  : 'text-[#94a3b8] hover:text-white hover:bg-white/5'
              }`
            }
          >
            <Home className="h-4 w-4" />
            <span>Home</span>
          </NavLink>
          
          <NavLink
            to="/tasks"
            className={({ isActive }) =>
              `flex items-center gap-2 px-4 py-2 rounded-lg font-medium text-sm transition-all duration-200 ${
                isActive
                  ? 'bg-gradient-to-r from-cyber-purple to-indigo-600 text-white shadow-lg shadow-cyber-purple/35'
                  : 'text-[#94a3b8] hover:text-white hover:bg-white/5'
              }`
            }
          >
            <CheckSquare className="h-4 w-4" />
            <span>Tasks</span>
          </NavLink>
        </div>

        {/* API Status Switcher */}
        <div className="flex items-center gap-3 bg-[#11141c] px-4 py-2 rounded-xl border border-white/5">
          <span className="text-xs font-semibold text-[#94a3b8] uppercase tracking-wider">
            API Status:
          </span>
          <button
            onClick={() => toggleApiError(!isApiFailing)}
            className={`relative inline-flex h-6 w-11 items-center rounded-full transition-colors duration-300 focus:outline-none ${
              isApiFailing ? 'bg-cyber-danger' : 'bg-cyber-success'
            }`}
          >
            <span
              className={`inline-block h-4 w-4 transform rounded-full bg-white transition-transform duration-300 ${
                isApiFailing ? 'translate-x-6' : 'translate-x-1'
              }`}
            />
          </button>
          <div className="flex items-center gap-1">
            {isApiFailing ? (
              <>
                <ShieldAlert className="h-4 w-4 text-cyber-danger" />
                <span className="text-[10px] font-bold text-cyber-danger uppercase">FAIL (500)</span>
              </>
            ) : (
              <>
                <ShieldCheck className="h-4 w-4 text-cyber-success" />
                <span className="text-[10px] font-bold text-cyber-success uppercase">ONLINE</span>
              </>
            )}
          </div>
        </div>
      </div>
    </nav>
  );
};
