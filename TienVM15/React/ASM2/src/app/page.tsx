import Link from "next/link";
import { getTasks } from "../data/db";

export const revalidate = 0; // Disable static rendering for this page to always get fresh stats

export default function Home() {
  const tasks = getTasks();
  const totalTasks = tasks.length;
  const completedTasks = tasks.filter((t) => t.completed).length;
  const pendingTasks = totalTasks - completedTasks;
  const completionRate = totalTasks > 0 ? Math.round((completedTasks / totalTasks) * 100) : 0;

  return (
    <div className="space-y-8 py-4">
      {/* Hero Welcome */}
      <div className="relative rounded-2xl bg-zinc-900 border border-zinc-800 p-8 overflow-hidden shadow-xl">
        <div className="absolute top-0 right-0 -mt-4 -mr-4 w-56 h-56 rounded-full bg-teal-500/10 blur-3xl pointer-events-none"></div>
        <div className="max-w-2xl">
          <h1 className="text-3xl font-extrabold tracking-tight text-white sm:text-4xl">
            Welcome to <span className="text-teal-400">Taskify</span> Dashboard
          </h1>
          <p className="mt-4 text-base text-zinc-400 leading-relaxed">
            Manage your daily tasks, track completion, and stay organized.
            Built with Next.js App Router, Server Actions, TypeScript, Formik, and Tailwind CSS.
          </p>
          <div className="mt-6 flex flex-wrap gap-4">
            <Link
              href="/tasks"
              className="inline-flex items-center justify-center rounded-lg bg-teal-500 px-5 py-2.5 text-sm font-semibold text-zinc-950 shadow-sm hover:bg-teal-400 transition-colors"
            >
              View Tasks List
            </Link>
            <Link
              href="/tasks/new"
              className="inline-flex items-center justify-center rounded-lg border border-zinc-700 bg-zinc-800/40 px-5 py-2.5 text-sm font-semibold text-zinc-300 hover:bg-zinc-800 hover:text-white transition-colors"
            >
              Create New Task
            </Link>
          </div>
        </div>
      </div>

      {/* Stats Section */}
      <div className="grid grid-cols-1 gap-5 sm:grid-cols-2 lg:grid-cols-4">
        {/* Total Tasks */}
        <div className="bg-zinc-900 border border-zinc-800 rounded-xl p-6 shadow-sm">
          <div className="flex items-center justify-between">
            <span className="text-sm font-medium text-zinc-400">Total Tasks</span>
            <div className="p-2 bg-zinc-800 rounded-lg text-zinc-300">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <path d="M9 11l3 3L22 4" />
                <path d="M21 12v7a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h11" />
              </svg>
            </div>
          </div>
          <div className="mt-4">
            <span className="text-3xl font-bold text-white">{totalTasks}</span>
            <p className="text-xs text-zinc-500 mt-1">Active registered tasks</p>
          </div>
        </div>

        {/* Completed */}
        <div className="bg-zinc-900 border border-zinc-800 rounded-xl p-6 shadow-sm">
          <div className="flex items-center justify-between">
            <span className="text-sm font-medium text-teal-400">Completed Tasks</span>
            <div className="p-2 bg-teal-950/40 rounded-lg text-teal-400">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14" />
                <polyline points="22 4 12 14.01 9 11.01" />
              </svg>
            </div>
          </div>
          <div className="mt-4">
            <span className="text-3xl font-bold text-white">{completedTasks}</span>
            <p className="text-xs text-zinc-500 mt-1">Successfully done</p>
          </div>
        </div>

        {/* Pending */}
        <div className="bg-zinc-900 border border-zinc-800 rounded-xl p-6 shadow-sm">
          <div className="flex items-center justify-between">
            <span className="text-sm font-medium text-amber-400">Pending Tasks</span>
            <div className="p-2 bg-amber-950/40 rounded-lg text-amber-400">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <circle cx="12" cy="12" r="10" />
                <polyline points="12 6 12 12 16 14" />
              </svg>
            </div>
          </div>
          <div className="mt-4">
            <span className="text-3xl font-bold text-white">{pendingTasks}</span>
            <p className="text-xs text-zinc-500 mt-1">Awaiting completion</p>
          </div>
        </div>

        {/* Progress Rate */}
        <div className="bg-zinc-900 border border-zinc-800 rounded-xl p-6 shadow-sm">
          <div className="flex items-center justify-between">
            <span className="text-sm font-medium text-indigo-400">Completion Rate</span>
            <div className="p-2 bg-indigo-950/40 rounded-lg text-indigo-400">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <line x1="18" y1="20" x2="18" y2="10" />
                <line x1="12" y1="20" x2="12" y2="4" />
                <line x1="6" y1="20" x2="6" y2="14" />
              </svg>
            </div>
          </div>
          <div className="mt-4">
            <span className="text-3xl font-bold text-white">{completionRate}%</span>
            <div className="w-full bg-zinc-800 rounded-full h-1.5 mt-2">
              <div
                className="bg-indigo-500 h-1.5 rounded-full transition-all duration-500"
                style={{ width: `${completionRate}%` }}
              ></div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
