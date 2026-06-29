import React from 'react';
import { NavLink, Outlet } from 'react-router-dom';
import { Layers, LayoutDashboard, ListChecks } from 'lucide-react';

// ─── Nav items ────────────────────────────────────────────────────────────────

interface NavItem {
  to: string;
  label: string;
  icon: React.ReactNode;
  end?: boolean;
}

const NAV_ITEMS: NavItem[] = [
  { to: '/',      label: 'Home',  icon: <LayoutDashboard size={16} />, end: true },
  { to: '/tasks', label: 'Tasks', icon: <ListChecks size={16} /> },
];

// ─── Component ────────────────────────────────────────────────────────────────

/**
 * AppLayout — root shell with sticky header and footer.
 * All page content is injected via <Outlet />.
 */
export const AppLayout: React.FC = () => {
  return (
    <div className="min-h-screen flex flex-col bg-white text-slate-900">
      {/* ── Header ── */}
      <header className="sticky top-0 z-40 bg-white border-b border-slate-200">
        <div className="max-w-5xl mx-auto px-4 h-14 flex items-center justify-between">
          {/* Brand */}
          <div className="flex items-center gap-2.5">
            <div className="p-1.5 bg-blue-50 border border-blue-100 rounded-lg text-blue-600">
              <Layers size={18} />
            </div>
            <span className="font-bold text-base bg-gradient-to-r from-blue-600 via-indigo-600 to-violet-600 bg-clip-text text-transparent">
              TaskFlow
            </span>
          </div>

          {/* Nav */}
          <nav className="flex items-center gap-1">
            {NAV_ITEMS.map((item) => (
              <NavLink
                key={item.to}
                to={item.to}
                end={item.end}
                className={({ isActive }) =>
                  [
                    'flex items-center gap-1.5 px-3 py-1.5 rounded-md text-xs font-semibold transition-all duration-150',
                    isActive
                      ? 'bg-blue-50 text-blue-700 border border-blue-100'
                      : 'text-slate-600 hover:text-slate-900 hover:bg-slate-100',
                  ].join(' ')
                }
              >
                {item.icon}
                {item.label}
              </NavLink>
            ))}
          </nav>
        </div>
      </header>

      {/* ── Main content ── */}
      <main className="flex-1">
        <div className="max-w-5xl mx-auto px-4 py-8">
          <Outlet />
        </div>
      </main>

      {/* ── Footer ── */}
      <footer className="border-t border-slate-200 bg-slate-50">
        <div className="max-w-5xl mx-auto px-4 py-4 text-center text-xs text-slate-400">
          TaskFlow &copy; {new Date().getFullYear()} · React Assignment 02
        </div>
      </footer>
    </div>
  );
};
