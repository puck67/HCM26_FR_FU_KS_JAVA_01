import Link from 'next/link';
import { getTasks } from '@/lib/db';
import { ListTodo, Plus, CheckCircle, Clock, LayoutGrid, ArrowRight } from 'lucide-react';

export default async function Home() {
  const tasks = await getTasks();
  const totalTasks = tasks.length;
  const completedTasks = tasks.filter(t => t.completed).length;
  const pendingTasks = totalTasks - completedTasks;

  return (
    <div className="space-y-12 py-6">
      {/* Hero Section */}
      <div className="relative overflow-hidden rounded-3xl bg-gradient-to-r from-slate-900 via-indigo-950 to-slate-900 border border-slate-800 p-8 md:p-12 shadow-2xl">
        <div className="absolute inset-0 bg-[radial-gradient(circle_at_top_right,rgba(99,102,241,0.15),transparent_40%)]" />
        <div className="relative z-10 max-w-2xl space-y-4">
          <span className="inline-flex items-center gap-1.5 rounded-full bg-indigo-500/10 px-3 py-1 text-xs font-semibold text-indigo-400 border border-indigo-500/20">
            Next.js App Router & Server Actions
          </span>
          <h1 className="text-4xl font-extrabold tracking-tight sm:text-5xl bg-gradient-to-r from-white via-slate-100 to-slate-400 bg-clip-text text-transparent">
            Manage Tasks with Elegant Simplicity
          </h1>
          <p className="text-lg text-slate-400 leading-relaxed">
            AetherTask combines cutting-edge Next.js architecture with instant server-state mutations and robust client-side validation to bring you a responsive task environment.
          </p>
          <div className="flex flex-wrap gap-4 pt-4">
            <Link
              href="/tasks"
              className="flex items-center gap-2 rounded-xl bg-indigo-600 px-5 py-3 text-sm font-semibold text-white shadow-md hover:bg-indigo-500 transition-all hover:translate-x-1"
            >
              <span>View Task List</span>
              <ArrowRight className="h-4 w-4" />
            </Link>
            <Link
              href="/tasks/new"
              className="flex items-center gap-2 rounded-xl bg-slate-800 px-5 py-3 text-sm font-semibold text-slate-300 hover:bg-slate-700 transition-all"
            >
              <Plus className="h-4 w-4" />
              <span>Create Task</span>
            </Link>
          </div>
        </div>
      </div>

      {/* Stats Section */}
      <div className="grid grid-cols-1 gap-6 sm:grid-cols-3">
        {/* Total Tasks */}
        <div className="relative overflow-hidden rounded-2xl border border-slate-800 bg-slate-900/50 p-6 shadow-lg backdrop-blur-sm transition-all hover:border-slate-700">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm font-medium text-slate-400">Total Tasks</p>
              <p className="mt-2 text-3xl font-bold text-white">{totalTasks}</p>
            </div>
            <div className="rounded-xl bg-slate-800 p-3 text-slate-300">
              <LayoutGrid className="h-6 w-6 text-indigo-400" />
            </div>
          </div>
        </div>

        {/* Completed Tasks */}
        <div className="relative overflow-hidden rounded-2xl border border-slate-800 bg-slate-900/50 p-6 shadow-lg backdrop-blur-sm transition-all hover:border-slate-700">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm font-medium text-slate-400">Completed</p>
              <p className="mt-2 text-3xl font-bold text-emerald-400">{completedTasks}</p>
            </div>
            <div className="rounded-xl bg-slate-800 p-3 text-emerald-400/20">
              <CheckCircle className="h-6 w-6 text-emerald-400" />
            </div>
          </div>
        </div>

        {/* Pending Tasks */}
        <div className="relative overflow-hidden rounded-2xl border border-slate-800 bg-slate-900/50 p-6 shadow-lg backdrop-blur-sm transition-all hover:border-slate-700">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm font-medium text-slate-400">Pending</p>
              <p className="mt-2 text-3xl font-bold text-amber-400">{pendingTasks}</p>
            </div>
            <div className="rounded-xl bg-slate-800 p-3 text-amber-400/20">
              <Clock className="h-6 w-6 text-amber-400" />
            </div>
          </div>
        </div>
      </div>

      {/* Feature Section / Overview */}
      <div className="border border-slate-800 bg-slate-950/40 rounded-3xl p-8 space-y-6">
        <h2 className="text-xl font-bold text-slate-100 flex items-center gap-2">
          <ListTodo className="h-5 w-5 text-indigo-400" />
          <span>Technical Features Implemented</span>
        </h2>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6 text-sm text-slate-400">
          <div className="space-y-4">
            <div className="flex gap-3">
              <div className="h-5 w-5 rounded-full bg-indigo-500/10 text-indigo-400 flex items-center justify-center font-bold text-xs mt-0.5">1</div>
              <div>
                <h4 className="font-semibold text-slate-200">Server & Client Components</h4>
                <p className="mt-1">Correctly balances Server Components for data fetching with Client Components for dynamic forms & actions.</p>
              </div>
            </div>
            <div className="flex gap-3">
              <div className="h-5 w-5 rounded-full bg-indigo-500/10 text-indigo-400 flex items-center justify-center font-bold text-xs mt-0.5">2</div>
              <div>
                <h4 className="font-semibold text-slate-200">Next.js Server Actions</h4>
                <p className="mt-1">Mutations like creating, updating, toggling, and deleting tasks are processed via Server Actions, with instant cache revalidation.</p>
              </div>
            </div>
          </div>
          <div className="space-y-4">
            <div className="flex gap-3">
              <div className="h-5 w-5 rounded-full bg-indigo-500/10 text-indigo-400 flex items-center justify-center font-bold text-xs mt-0.5">3</div>
              <div>
                <h4 className="font-semibold text-slate-200">Formik Form Validation</h4>
                <p className="mt-1">Task creation and updates are validated client-side with Formik. Constraints ensure correct data length and formatting.</p>
              </div>
            </div>
            <div className="flex gap-3">
              <div className="h-5 w-5 rounded-full bg-indigo-500/10 text-indigo-400 flex items-center justify-center font-bold text-xs mt-0.5">4</div>
              <div>
                <h4 className="font-semibold text-slate-200">Tailwind CSS v4 Layout</h4>
                <p className="mt-1">Fully responsive, clean interface tailored for modern dark mode aesthetics, incorporating micro-animations and status badges.</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
