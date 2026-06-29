import React from 'react';
import { Link, useLocation } from 'react-router-dom';
import { LayoutDashboard, CheckSquare, Layers } from 'lucide-react';

interface MainLayoutProps {
  children: React.ReactNode;
}

export const MainLayout: React.FC<MainLayoutProps> = ({ children }) => {
  const location = useLocation();

  const navItems = [
    {
      label: 'Home',
      path: '/',
      icon: <LayoutDashboard size={18} />
    },
    {
      label: 'Tasks',
      path: '/tasks',
      icon: <CheckSquare size={18} />
    }
  ];

  return (
    <div className="min-height-100vh flex flex-col bg-white text-black selection:bg-indigo-500/10">
      {/* Top Navbar */}
      <header className="sticky top-0 z-40 w-full border-b border-slate-200 bg-white/90 backdrop-blur-md shadow-sm">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 h-16 flex items-center justify-between">
          <div className="flex items-center gap-3">
            <div className="p-2 bg-indigo-50 border border-indigo-100 text-indigo-600 rounded-lg">
              <Layers size={20} />
            </div>
            <span className="font-bold text-lg bg-gradient-to-r from-blue-600 via-indigo-600 to-violet-600 bg-clip-text text-transparent tracking-wide">
              TaskFlow
            </span>
          </div>

          <nav className="flex items-center gap-1">
            {navItems.map((item) => {
              const isActive =
                item.path === '/'
                  ? location.pathname === '/'
                  : location.pathname.startsWith(item.path);

              return (
                <Link
                  key={item.path}
                  to={item.path}
                  className={`flex items-center gap-2 px-4 py-2 rounded-lg text-sm font-medium transition-all duration-200 ${
                    isActive
                      ? 'bg-indigo-50 text-indigo-600 border border-indigo-100'
                      : 'text-slate-600 hover:text-slate-950 hover:bg-slate-100/50 border border-transparent'
                  }`}
                >
                  {item.icon}
                  {item.label}
                </Link>
              );
            })}
          </nav>
        </div>
      </header>

      {/* Main Content */}
      <main className="flex-1 max-w-7xl w-full mx-auto px-4 sm:px-6 lg:px-8 py-8 animate-fadeIn">
        {children}
      </main>

      {/* Footer */}
      <footer className="w-full border-t border-slate-200 bg-white/60 py-6">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 flex flex-col sm:flex-row items-center justify-between gap-4 text-xs text-slate-500">
          <p>© 2026 TaskFlow. FSOFT React Assignment 1 (Code Quality Standard).</p>
          <div className="flex items-center gap-6">
            <span className="hover:text-slate-700 transition-colors">Vite + React + TS</span>
            <span className="hover:text-slate-700 transition-colors">Tailwind CSS v4</span>
          </div>
        </div>
      </footer>
    </div>
  );
};
