import React, { useState } from 'react';
import { Link, useLocation } from 'react-router-dom';
import { useTasks } from '../context/TaskContext';
import { 
  CheckSquare, 
  Home, 
  ListTodo, 
  PlusCircle, 
  Menu, 
  X, 
  User, 
  PieChart 
} from 'lucide-react';

interface SidebarProps {
  onOpenCreateModal: () => void;
}

export const Sidebar: React.FC<SidebarProps> = ({ onOpenCreateModal }) => {
  const location = useLocation();
  const { state } = useTasks();
  const { tasks } = state;
  const [isOpen, setIsOpen] = useState(false);

  const isActive = (path: string) => {
    if (path === '/') return location.pathname === '/';
    return location.pathname.startsWith(path);
  };

  const totalTasks = tasks.length;
  const completedTasks = tasks.filter(t => t.completed).length;
  const pendingTasks = totalTasks - completedTasks;

  const toggleMobileMenu = () => setIsOpen(!isOpen);

  const NavItems = () => (
    <div className="space-y-1">
      <Link
        to="/"
        onClick={() => setIsOpen(false)}
        className={`flex items-center gap-3 px-4 py-3 rounded-xl font-medium text-sm transition-all duration-200 ${
          isActive('/')
            ? 'bg-indigo-600 text-white shadow-lg shadow-indigo-600/15'
            : 'text-slate-400 hover:text-white hover:bg-white/5'
        }`}
      >
        <Home className="w-5 h-5" />
        <span>Dashboard</span>
      </Link>

      <Link
        to="/tasks"
        onClick={() => setIsOpen(false)}
        className={`flex items-center gap-3 px-4 py-3 rounded-xl font-medium text-sm transition-all duration-200 ${
          isActive('/tasks')
            ? 'bg-indigo-600 text-white shadow-lg shadow-indigo-600/15'
            : 'text-slate-400 hover:text-white hover:bg-white/5'
        }`}
      >
        <ListTodo className="w-5 h-5" />
        <span>Tasks List</span>
      </Link>
    </div>
  );

  return (
    <>
      {/* Mobile Top Navbar Header */}
      <header className="flex md:hidden items-center justify-between px-6 py-4 bg-[#070b14] border-b border-white/5 sticky top-0 z-40 shadow-lg">
        <div className="flex items-center gap-2.5">
          <div className="bg-indigo-600 p-1.5 rounded-lg text-white">
            <CheckSquare className="w-5 h-5" />
          </div>
          <span className="font-bold text-lg text-white tracking-wide">ASM1</span>
        </div>
        
        <div className="flex items-center gap-3">
          <button
            onClick={onOpenCreateModal}
            className="p-2 bg-indigo-600 hover:bg-indigo-500 text-white rounded-lg transition-colors"
          >
            <PlusCircle className="w-4 h-4" />
          </button>
          <button
            onClick={toggleMobileMenu}
            className="p-2 text-slate-400 hover:text-white rounded-lg hover:bg-white/5 transition-colors"
          >
            {isOpen ? <X className="w-6 h-6" /> : <Menu className="w-6 h-6" />}
          </button>
        </div>
      </header>

      {/* Mobile Sidebar Overlay Drawer */}
      {isOpen && (
        <div className="fixed inset-0 z-50 md:hidden flex">
          {/* Backdrop */}
          <div 
            className="fixed inset-0 bg-slate-950/60 backdrop-blur-sm"
            onClick={toggleMobileMenu}
          />
          
          {/* Menu Panel */}
          <aside className="relative flex flex-col w-72 max-w-xs bg-[#070b14] border-r border-white/10 p-6 z-10 animate-in slide-in-from-left duration-200">
            <div className="flex items-center justify-between mb-8">
              <div className="flex items-center gap-3">
                <div className="bg-indigo-600 p-2 rounded-xl text-white">
                  <CheckSquare className="w-5 h-5" />
                </div>
                <span className="font-bold text-xl text-white tracking-wide">ASM1</span>
              </div>
              <button
                onClick={toggleMobileMenu}
                className="text-slate-400 hover:text-white p-1 rounded-lg hover:bg-white/5"
              >
                <X className="w-5 h-5" />
              </button>
            </div>

            <nav className="flex-1 space-y-6">
              <NavItems />
              
              <button
                onClick={() => {
                  toggleMobileMenu();
                  onOpenCreateModal();
                }}
                className="flex items-center justify-center gap-2 w-full py-3 bg-indigo-600 hover:bg-indigo-500 text-white rounded-xl font-semibold text-sm shadow-lg shadow-indigo-600/25 transition-all"
              >
                <PlusCircle className="w-4 h-4" />
                <span>Create Task</span>
              </button>
            </nav>

            <div className="mt-auto border-t border-white/5 pt-4 flex items-center gap-3">
              <div className="w-9 h-9 rounded-full bg-indigo-600/10 border border-indigo-500/20 flex items-center justify-center text-indigo-400">
                <User className="w-4 h-4" />
              </div>
              <div>
                <p className="text-xs font-semibold text-white">Student Account</p>
                <p className="text-[10px] text-slate-500">REACT_Assignment01</p>
              </div>
            </div>
          </aside>
        </div>
      )}

      {/* Desktop Sidebar (Fixed Left Layout) */}
      <aside className="hidden md:flex md:w-64 md:flex-col md:fixed md:inset-y-0 bg-[#070b14] border-r border-white/5 p-6 shadow-2xl z-30">
        {/* Logo and Brand */}
        <div className="flex items-center gap-3 mb-8">
          <div className="bg-indigo-600 p-2.5 rounded-xl text-white shadow-indigo-500/20 shadow-md">
            <CheckSquare className="w-6 h-6 animate-pulse" />
          </div>
          <div>
            <span className="font-bold text-xl tracking-wider text-white">
              ASM1
            </span>
            <span className="text-[10px] block text-indigo-400 font-semibold tracking-wider uppercase">Dashboard</span>
          </div>
        </div>

        {/* Create Task Button */}
        <button
          onClick={onOpenCreateModal}
          className="flex items-center justify-center gap-2 w-full py-3 bg-indigo-600 hover:bg-indigo-500 text-white rounded-xl font-bold text-sm shadow-lg shadow-indigo-600/25 transition-all duration-200 mb-8 hover:-translate-y-0.5 active:translate-y-0"
        >
          <PlusCircle className="w-4 h-4" />
          <span>New Task</span>
        </button>

        {/* Navigation Items */}
        <nav className="flex-1 space-y-6">
          <div className="space-y-1">
            <span className="px-4 text-[10px] font-bold text-slate-500 uppercase tracking-widest block mb-2">Navigation</span>
            <NavItems />
          </div>

          {/* Quick Mini Stats Widget */}
          <div className="bg-slate-900/40 border border-white/5 p-4 rounded-2xl">
            <span className="text-[10px] font-bold text-slate-500 uppercase tracking-widest block mb-3 flex items-center gap-1.5">
              <PieChart className="w-3.5 h-3.5 text-indigo-400" />
              <span>Backlog Stats</span>
            </span>
            <div className="space-y-2">
              <div className="flex justify-between text-xs font-semibold">
                <span className="text-slate-400">Total:</span>
                <span className="text-white">{totalTasks}</span>
              </div>
              <div className="flex justify-between text-xs font-semibold">
                <span className="text-slate-400">Completed:</span>
                <span className="text-emerald-400">{completedTasks}</span>
              </div>
              <div className="flex justify-between text-xs font-semibold">
                <span className="text-slate-400">Pending:</span>
                <span className="text-amber-400">{pendingTasks}</span>
              </div>
            </div>
          </div>
        </nav>

        {/* Sidebar Footer student info */}
        <div className="border-t border-white/5 pt-4 mt-auto flex items-center gap-3">
          <div className="w-10 h-10 rounded-xl bg-indigo-600/10 border border-indigo-500/20 flex items-center justify-center text-indigo-400">
            <User className="w-5 h-5" />
          </div>
          <div>
            <p className="text-xs font-bold text-white">Student Account</p>
            <p className="text-[10px] text-slate-500 font-medium">REACT_Assignment01</p>
          </div>
        </div>
      </aside>
    </>
  );
};
