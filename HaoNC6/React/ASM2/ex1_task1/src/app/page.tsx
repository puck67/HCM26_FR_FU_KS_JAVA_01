import Link from 'next/link';
import { getTasks } from '../lib/tasks';
import { ListTodo, PlusCircle, CheckCircle2, Circle, ArrowRight, LayoutDashboard, Database, Zap } from 'lucide-react';

export const dynamic = 'force-dynamic';

export default async function Home() {
  const tasks = getTasks();

  const totalTasks = tasks.length;
  const completedTasks = tasks.filter((t) => t.completed).length;
  const pendingTasks = totalTasks - completedTasks;
  const completionRate = totalTasks > 0 ? Math.round((completedTasks / totalTasks) * 100) : 0;

  // Get 3 most recently created tasks
  const recentTasks = [...tasks]
    .sort((a, b) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime())
    .slice(0, 3);

  return (
    <div className="space-y-10 animate-fade-in">
      {/* Hero Header */}
      <section className="text-center py-10 space-y-4">
        <div className="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-indigo-500/10 border border-indigo-500/20 text-indigo-300 text-xs font-semibold uppercase tracking-wider mb-2">
          <Zap className="w-3.5 h-3.5" /> Next.js App Router & Server Actions
        </div>
        <h1 className="text-4xl md:text-5xl lg:text-6xl font-extrabold tracking-tight">
          Supercharge Your Workflow with <span className="text-gradient">TaskSphere</span>
        </h1>
        <p className="max-w-2xl mx-auto text-gray-400 text-lg">
          An advanced Next.js task manager utilizing Server Components for speed, Server Actions for mutations, and Formik for real-time validation.
        </p>
        <div className="flex flex-wrap items-center justify-center gap-4 pt-4">
          <Link
            href="/tasks"
            className="flex items-center gap-2 px-6 py-3 rounded-xl bg-indigo-600 hover:bg-indigo-500 text-white font-semibold shadow-lg hover:shadow-indigo-500/20 transition-all duration-300 transform hover:-translate-y-0.5"
          >
            <LayoutDashboard className="w-5 h-5" /> Launch Dashboard
          </Link>
          <Link
            href="/tasks/new"
            className="flex items-center gap-2 px-6 py-3 rounded-xl bg-white/5 hover:bg-white/10 text-white font-semibold border border-white/10 hover:border-white/20 transition-all duration-300"
          >
            <PlusCircle className="w-5 h-5 text-purple-400" /> Create Task
          </Link>
        </div>
      </section>

      {/* Stats Cards */}
      <section className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
        {/* Total Tasks */}
        <div className="glass-panel rounded-2xl p-6 relative overflow-hidden">
          <div className="absolute right-4 top-4 text-white/5">
            <ListTodo className="w-16 h-16" />
          </div>
          <p className="text-gray-400 text-sm font-semibold uppercase tracking-wide">Total Tasks</p>
          <p className="text-4xl font-extrabold mt-2 text-white">{totalTasks}</p>
          <p className="text-xs text-gray-500 mt-2">Active tasks stored in database</p>
        </div>

        {/* Completed Tasks */}
        <div className="glass-panel rounded-2xl p-6 relative overflow-hidden">
          <div className="absolute right-4 top-4 text-emerald-500/5">
            <CheckCircle2 className="w-16 h-16" />
          </div>
          <p className="text-gray-400 text-sm font-semibold uppercase tracking-wide">Completed</p>
          <p className="text-4xl font-extrabold mt-2 text-emerald-400">{completedTasks}</p>
          <p className="text-xs text-gray-500 mt-2">Successfully closed tasks</p>
        </div>

        {/* Pending Tasks */}
        <div className="glass-panel rounded-2xl p-6 relative overflow-hidden">
          <div className="absolute right-4 top-4 text-yellow-500/5">
            <Circle className="w-16 h-16" />
          </div>
          <p className="text-gray-400 text-sm font-semibold uppercase tracking-wide">Pending</p>
          <p className="text-4xl font-extrabold mt-2 text-yellow-400">{pendingTasks}</p>
          <p className="text-xs text-gray-500 mt-2">Remaining tasks to do</p>
        </div>

        {/* Completion Rate */}
        <div className="glass-panel rounded-2xl p-6 relative overflow-hidden">
          <div className="absolute right-4 top-4 text-indigo-500/5">
            <Database className="w-16 h-16" />
          </div>
          <p className="text-gray-400 text-sm font-semibold uppercase tracking-wide">Progress Rate</p>
          <div className="flex items-baseline gap-2 mt-2">
            <p className="text-4xl font-extrabold text-indigo-400">{completionRate}%</p>
            <div className="w-full bg-slate-800 rounded-full h-2.5 max-w-[80px]">
              <div 
                className="bg-indigo-500 h-2.5 rounded-full" 
                style={{ width: `${completionRate}%` }}
              ></div>
            </div>
          </div>
          <p className="text-xs text-gray-500 mt-2">Overall project completion level</p>
        </div>
      </section>

      {/* Grid: Recent Tasks & Technical Features */}
      <section className="grid grid-cols-1 lg:grid-cols-2 gap-8">
        {/* Recent Tasks */}
        <div className="glass-panel rounded-2xl p-6 md:p-8 space-y-6">
          <div className="flex items-center justify-between">
            <h2 className="text-xl font-bold text-white flex items-center gap-2">
              <ListTodo className="w-5 h-5 text-indigo-400" /> Recent Activities
            </h2>
            {totalTasks > 0 && (
              <Link href="/tasks" className="text-xs text-indigo-400 hover:text-indigo-300 font-semibold flex items-center gap-1">
                View all <ArrowRight className="w-3 h-3" />
              </Link>
            )}
          </div>

          <div className="space-y-4">
            {recentTasks.length === 0 ? (
              <div className="text-center py-10 text-gray-500">
                <p>No tasks found. Get started by creating your first task!</p>
                <Link href="/tasks/new" className="text-indigo-400 hover:underline mt-2 inline-block text-sm">
                  Add a task now
                </Link>
              </div>
            ) : (
              recentTasks.map((task) => (
                <div 
                  key={task.id} 
                  className="flex items-start justify-between p-4 rounded-xl bg-white/2 border border-white/5 hover:border-white/10 transition-all"
                >
                  <div className="space-y-1 pr-4">
                    <h3 className={`font-semibold text-sm ${task.completed ? 'line-through text-gray-500' : 'text-white'}`}>
                      {task.name}
                    </h3>
                    <p className="text-xs text-gray-400 line-clamp-1">
                      {task.description || 'No description provided.'}
                    </p>
                  </div>
                  <div className="flex items-center gap-2">
                    <span className={`text-[10px] px-2.5 py-0.5 rounded-full font-medium ${
                      task.completed 
                        ? 'bg-emerald-500/10 text-emerald-400 border border-emerald-500/20' 
                        : 'bg-yellow-500/10 text-yellow-400 border border-yellow-500/20'
                    }`}>
                      {task.completed ? 'Completed' : 'Pending'}
                    </span>
                    <Link 
                      href={`/tasks/${task.id}`}
                      className="p-1 rounded-lg bg-white/5 hover:bg-white/10 text-gray-400 hover:text-white transition-all"
                    >
                      <ArrowRight className="w-4 h-4" />
                    </Link>
                  </div>
                </div>
              ))
            )}
          </div>
        </div>

        {/* Technical Architecture Info */}
        <div className="glass-panel rounded-2xl p-6 md:p-8 space-y-6">
          <h2 className="text-xl font-bold text-white flex items-center gap-2">
            <Database className="w-5 h-5 text-purple-400" /> Technology Highlights
          </h2>
          <div className="space-y-4 text-sm text-gray-400">
            <div className="flex gap-3">
              <div className="p-2 rounded-lg bg-indigo-500/10 text-indigo-400 h-9 w-9 flex items-center justify-center shrink-0">
                1
              </div>
              <div>
                <h4 className="font-semibold text-white">Next.js App Router Routing</h4>
                <p className="text-xs mt-1 text-gray-400">
                  Fully relies on file-based routing with static and dynamic paths (`/`, `/tasks`, `/tasks/new`, `/tasks/[id]`).
                </p>
              </div>
            </div>

            <div className="flex gap-3">
              <div className="p-2 rounded-lg bg-purple-500/10 text-purple-400 h-9 w-9 flex items-center justify-center shrink-0">
                2
              </div>
              <div>
                <h4 className="font-semibold text-white">Hybrid React Architecture</h4>
                <p className="text-xs mt-1 text-gray-400">
                  Combines server-side rendering for SEO and loading speed with client-side interactive forms using React hooks.
                </p>
              </div>
            </div>

            <div className="flex gap-3">
              <div className="p-2 rounded-lg bg-pink-500/10 text-pink-400 h-9 w-9 flex items-center justify-center shrink-0">
                3
              </div>
              <div>
                <h4 className="font-semibold text-white">Secure Server Actions</h4>
                <p className="text-xs mt-1 text-gray-400">
                  All operations (create, update, delete, status toggle) execute on the server, avoiding REST endpoint exposure.
                </p>
              </div>
            </div>
          </div>
        </div>
      </section>
    </div>
  );
}
