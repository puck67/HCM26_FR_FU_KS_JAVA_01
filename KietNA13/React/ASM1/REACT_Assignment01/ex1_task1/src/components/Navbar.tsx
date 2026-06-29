import { NavLink } from 'react-router-dom';
import { cn } from '../utils/cn';
import { ROUTES } from '../constants/routes';

interface NavItem {
  to: string;
  label: string;
  end?: boolean;
}

const NAV_ITEMS: NavItem[] = [
  { to: ROUTES.HOME, label: 'Dashboard', end: true },
  { to: ROUTES.TASKS, label: 'Tasks' },
];

export const Navbar = () => (
  <header className="sticky top-0 z-40 border-b border-gray-200 bg-white/95 shadow-sm backdrop-blur">
    <nav
      className="mx-auto flex max-w-7xl items-center justify-between px-4 py-4 sm:px-6 lg:px-8"
      aria-label="Main navigation"
    >
      <NavLink
        to={ROUTES.HOME}
        className="text-xl font-bold text-indigo-600 focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-indigo-500 focus-visible:ring-offset-2 rounded"
        aria-label="TaskDash home"
      >
        TaskDash
      </NavLink>

      <ul className="flex items-center gap-1" role="list">
        {NAV_ITEMS.map(({ to, label, end }) => (
          <li key={to}>
            <NavLink
              to={to}
              end={end}
              className={({ isActive }) =>
                cn(
                  'rounded-md px-3 py-2 text-sm font-medium transition-colors',
                  'focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-indigo-500',
                  isActive
                    ? 'bg-indigo-50 text-indigo-600'
                    : 'text-gray-600 hover:bg-gray-100 hover:text-gray-900',
                )
              }
            >
              {label}
            </NavLink>
          </li>
        ))}
      </ul>
    </nav>
  </header>
);
