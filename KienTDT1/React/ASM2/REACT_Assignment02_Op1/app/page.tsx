import Link from 'next/link';
import { readTasks } from '@/lib/db';

export const dynamic = 'force-dynamic';

export default async function HomePage() {
  const tasks = readTasks();
  const totalTasks = tasks.length;
  const completedTasks = tasks.filter(t => t.completed).length;
  const pendingTasks = totalTasks - completedTasks;
  const completionRate = totalTasks > 0 ? Math.round((completedTasks / totalTasks) * 100) : 0;

  return (
    <div className="space-y-10 animate-slide-up">
      {/* Hero Section */}
      <div className="relative overflow-hidden rounded-3xl glass-panel p-8 sm:p-12 border border-indigo-500/20 shadow-2xl">
        <div className="absolute top-0 right-0 -z-10 h-72 w-72 bg-indigo-500/10 rounded-full blur-3xl" />
        <div className="absolute bottom-0 left-0 -z-10 h-72 w-72 bg-violet-500/10 rounded-full blur-3xl" />

        <div className="max-w-2xl space-y-6">
          <span className="inline-flex items-center gap-1.5 px-3 py-1 rounded-full text-xs font-semibold bg-indigo-500/10 text-indigo-300 border border-indigo-500/25">
            <span className="h-1.5 w-1.5 rounded-full bg-indigo-400 animate-pulse" />
            Next.js App Router & Server Actions
          </span>
          <h1 className="text-4xl sm:text-5xl font-extrabold tracking-tight text-white leading-none">
            Organize Your Workflow with <span className="bg-gradient-to-r from-indigo-400 via-purple-400 to-pink-400 bg-clip-text text-transparent">AetherTasks</span>
          </h1>
          <p className="text-base sm:text-lg text-slate-400 leading-relaxed">
            Experience the next generation of task management. Built with Next.js 15, Formik form validation, and reactive server actions for instantaneous data updates.
          </p>
          <div className="flex flex-wrap gap-4 pt-2">
            <Link
              href="/tasks"
              className="px-6 py-3 rounded-xl font-semibold bg-gradient-to-r from-indigo-500 to-violet-600 text-white shadow-lg shadow-indigo-500/20 hover:shadow-indigo-500/35 hover:scale-[1.02] active:scale-[0.98] transition-all duration-300"
            >
              Go to Tasks
            </Link>
            <Link
              href="/tasks/new"
              className="px-6 py-3 rounded-xl font-semibold bg-slate-800/80 text-slate-200 border border-slate-700/50 hover:bg-slate-700/60 hover:text-white transition-all duration-300"
            >
              Add New Task
            </Link>
          </div>
        </div>
      </div>

      {/* Stats Section */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
        {/* Stat 1 */}
        <div className="glass-panel rounded-2xl p-6 relative overflow-hidden border border-slate-800/80">
          <div className="absolute top-0 right-0 h-16 w-16 bg-blue-500/5 rounded-full blur-xl" />
          <p className="text-sm font-medium text-slate-400">Total Tasks</p>
          <div className="mt-2 flex items-baseline gap-2">
            <span className="text-4xl font-extrabold text-white">{totalTasks}</span>
            <span className="text-xs text-slate-500">active & done</span>
          </div>
        </div>

        {/* Stat 2 */}
        <div className="glass-panel rounded-2xl p-6 relative overflow-hidden border border-slate-800/80">
          <div className="absolute top-0 right-0 h-16 w-16 bg-emerald-500/5 rounded-full blur-xl" />
          <p className="text-sm font-medium text-slate-400">Completed</p>
          <div className="mt-2 flex items-baseline gap-2">
            <span className="text-4xl font-extrabold text-emerald-400">{completedTasks}</span>
            <span className="text-xs text-slate-500">tasks finished</span>
          </div>
        </div>

        {/* Stat 3 */}
        <div className="glass-panel rounded-2xl p-6 relative overflow-hidden border border-slate-800/80">
          <div className="absolute top-0 right-0 h-16 w-16 bg-amber-500/5 rounded-full blur-xl" />
          <p className="text-sm font-medium text-slate-400">Pending</p>
          <div className="mt-2 flex items-baseline gap-2">
            <span className="text-4xl font-extrabold text-amber-400">{pendingTasks}</span>
            <span className="text-xs text-slate-500">remaining</span>
          </div>
        </div>

        {/* Stat 4 */}
        <div className="glass-panel rounded-2xl p-6 relative overflow-hidden border border-slate-800/80">
          <div className="absolute top-0 right-0 h-16 w-16 bg-indigo-500/5 rounded-full blur-xl" />
          <p className="text-sm font-medium text-slate-400">Completion Rate</p>
          <div className="mt-2 flex items-baseline gap-2">
            <span className="text-4xl font-extrabold bg-gradient-to-r from-indigo-400 to-violet-400 bg-clip-text text-transparent">
              {completionRate}%
            </span>
            <span className="text-xs text-slate-500">efficiency</span>
          </div>
          {/* Progress bar */}
          <div className="mt-4 h-1.5 w-full bg-slate-900 rounded-full overflow-hidden">
            <div
              className="h-full bg-gradient-to-r from-indigo-500 to-violet-500 transition-all duration-500"
              style={{ width: `${completionRate}%` }}
            />
          </div>
        </div>
      </div>
    </div>
  );
}
