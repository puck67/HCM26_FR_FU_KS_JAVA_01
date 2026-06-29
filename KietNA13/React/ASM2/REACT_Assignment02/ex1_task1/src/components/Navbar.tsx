import Link from 'next/link';
import { ROUTES } from '@/constants/routes';

export default function Navbar() {
  return (
    <nav className="fixed top-0 left-0 right-0 z-50 bg-slate-900/95 backdrop-blur-sm border-b border-slate-700/50">
      <div className="max-w-5xl mx-auto px-4 sm:px-6 flex items-center justify-between h-16">
        <Link
          href={ROUTES.HOME}
          className="flex items-center gap-2 text-xl font-bold text-white hover:text-indigo-400 transition-colors"
        >
          <span className="text-2xl">✅</span>
          <span>Task manager</span>
        </Link>

        <div className="flex items-center gap-1 sm:gap-2">
          <Link
            href={ROUTES.TASKS}
            className="px-3 py-2 text-sm font-medium text-slate-300 hover:text-white hover:bg-slate-700 rounded-lg transition-all"
          >
            All tasks
          </Link>
          <Link
            href={ROUTES.NEW_TASK}
            className="px-3 py-2 text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-500 rounded-lg transition-all"
          >
            + New task
          </Link>
        </div>
      </div>
    </nav>
  );
}
