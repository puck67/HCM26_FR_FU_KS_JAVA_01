import Link from 'next/link';
import { store } from '@/lib/store';
import { ROUTES } from '@/constants/routes';
import StatCard from '@/components/StatCard';

export default function HomePage() {
  const tasks = Array.from(store.values());
  const totalTasks = tasks.length;
  const completedTasks = tasks.filter((t) => t.completed).length;
  const pendingTasks = totalTasks - completedTasks;

  const progressPercent =
    totalTasks > 0 ? Math.round((completedTasks / totalTasks) * 100) : 0;

  return (
    <div className="flex flex-col gap-10">
      {/* Hero */}
      <div className="text-center space-y-3">
        <h1 className="text-4xl sm:text-5xl font-extrabold text-white tracking-tight">
          Task manager
        </h1>
        <p className="text-slate-400 text-base sm:text-lg max-w-md mx-auto">
          Stay on top of your work with a fast, server-rendered task tracker.
        </p>
      </div>

      {/* Summary cards */}
      <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
        <StatCard
          label="Total tasks"
          value={totalTasks}
          colorClass="from-indigo-600/20 to-violet-600/20 border-indigo-500/30"
          icon="📋"
        />
        <StatCard
          label="Completed"
          value={completedTasks}
          colorClass="from-emerald-600/20 to-teal-600/20 border-emerald-500/30"
          icon="✅"
        />
        <StatCard
          label="Pending"
          value={pendingTasks}
          colorClass="from-amber-600/20 to-orange-600/20 border-amber-500/30"
          icon="⏳"
        />
      </div>

      {/* Progress bar
          CSS custom property used here because Tailwind cannot express
          runtime percentage values without JIT arbitrary values that would
          require unsafe dynamic class generation. */}
      {totalTasks > 0 && (
        <div className="space-y-2">
          <div className="flex justify-between text-sm text-slate-400">
            <span>Overall progress</span>
            <span>{progressPercent}%</span>
          </div>
          <div className="w-full bg-slate-800 rounded-full h-2.5 overflow-hidden">
            <div
              className="h-full bg-gradient-to-r from-indigo-500 to-emerald-500 rounded-full transition-all duration-500"
              style={
                { '--progress': `${progressPercent}%`, width: 'var(--progress)' } as React.CSSProperties
              }
            />
          </div>
        </div>
      )}

      {/* CTA */}
      <div className="flex flex-col sm:flex-row gap-3 justify-center">
        <Link
          href={ROUTES.TASKS}
          className="inline-flex items-center justify-center gap-2 px-6 py-3 bg-indigo-600 hover:bg-indigo-500 text-white font-semibold rounded-xl transition-all text-sm sm:text-base"
        >
          View all tasks →
        </Link>
        <Link
          href={ROUTES.NEW_TASK}
          className="inline-flex items-center justify-center gap-2 px-6 py-3 bg-slate-800 hover:bg-slate-700 border border-slate-700 text-slate-200 font-semibold rounded-xl transition-all text-sm sm:text-base"
        >
          + Create task
        </Link>
      </div>
    </div>
  );
}
