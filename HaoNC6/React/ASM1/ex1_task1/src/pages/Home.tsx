import React from 'react';
import { Link } from 'react-router-dom';
import { useTasks } from '../context/TaskContext';
import { DashboardStatsSkeleton } from '../components/SkeletonLoader';
import { Alert } from '../components/Alert';
import {
  ListTodo,
  CheckCircle2,
  Clock,
  ArrowRight,
  TrendingUp,
  ShieldCheck,
  Zap,
} from 'lucide-react';

export const Home: React.FC = () => {
  const { state, reloadTasks } = useTasks();
  const { tasks, loading, error } = state;

  // Compute Statistics
  const totalTasks = tasks.length;
  const completedTasks = tasks.filter((t) => t.status === 'completed').length;
  const inProgressTasks = tasks.filter((t) => t.status === 'in_progress').length;

  const completionRate = totalTasks > 0 ? Math.round((completedTasks / totalTasks) * 100) : 0;

  // Get 3 most recent tasks
  const recentTasks = [...tasks]
    .sort((a, b) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime())
    .slice(0, 3);

  return (
    <div className="relative min-h-[calc(100vh-73px)] py-10 px-4 md:px-8 max-w-7xl mx-auto overflow-hidden">
      {/* Decorative Blur Backgrounds */}
      <div className="absolute top-20 left-10 w-72 h-72 bg-violet-600/10 rounded-full blur-3xl -z-10 animate-pulse" />
      <div className="absolute bottom-20 right-10 w-96 h-96 bg-indigo-600/10 rounded-full blur-3xl -z-10" />

      {/* Hero Section */}
      <div className="text-center max-w-3xl mx-auto mb-16">
        <div className="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-violet-500/10 border border-violet-500/20 text-xs font-semibold text-violet-300 mb-6">
          <Zap size={12} className="text-violet-400" />
          <span>Vite + React Hooks + Tailwind v4</span>
        </div>
        <h1 className="text-4xl md:text-6xl font-extrabold tracking-tight mb-6 bg-gradient-to-r from-white via-gray-100 to-gray-400 bg-clip-text text-transparent leading-tight">
          Supercharge Your Workflow with <span className="gradient-text">TaskSphere</span>
        </h1>
        <p className="text-lg text-gray-400 leading-relaxed max-w-2xl mx-auto mb-8">
          A premium glassmorphic dashboard designed to model and manage your tasks. Streamlined CRUD operations, persistent memory, and real-time state synchronization.
        </p>
        <div className="flex flex-wrap justify-center gap-4">
          <Link
            to="/tasks"
            className="glass-button px-6 py-3 rounded-xl text-base font-semibold text-white flex items-center gap-2 transition-all cursor-pointer"
          >
            <span>Launch Dashboard</span>
            <ArrowRight size={18} />
          </Link>
        </div>
      </div>

      {/* Loading & Error States */}
      {loading ? (
        <DashboardStatsSkeleton />
      ) : error ? (
        <div className="mb-12">
          <Alert message={error} onRetry={reloadTasks} />
        </div>
      ) : (
        <>
          {/* Stats Dashboard Grid */}
          <div className="grid grid-cols-1 md:grid-cols-4 gap-6 mb-12">
            {/* Total Tasks */}
            <div className="glass-card p-6 rounded-2xl border border-white/5 flex items-center gap-4 relative overflow-hidden">
              <div className="p-3 bg-blue-500/10 text-blue-400 rounded-xl border border-blue-500/20">
                <ListTodo size={24} />
              </div>
              <div>
                <span className="text-xs font-semibold text-gray-500 uppercase tracking-wider block">Total Tasks</span>
                <span className="text-3xl font-bold text-white block mt-0.5">{totalTasks}</span>
              </div>
            </div>

            {/* In Progress */}
            <div className="glass-card p-6 rounded-2xl border border-white/5 flex items-center gap-4 relative overflow-hidden">
              <div className="p-3 bg-amber-500/10 text-amber-400 rounded-xl border border-amber-500/20">
                <Clock size={24} />
              </div>
              <div>
                <span className="text-xs font-semibold text-gray-500 uppercase tracking-wider block">In Progress</span>
                <span className="text-3xl font-bold text-white block mt-0.5">{inProgressTasks}</span>
              </div>
            </div>

            {/* Completed */}
            <div className="glass-card p-6 rounded-2xl border border-white/5 flex items-center gap-4 relative overflow-hidden">
              <div className="p-3 bg-emerald-500/10 text-emerald-400 rounded-xl border border-emerald-500/20">
                <CheckCircle2 size={24} />
              </div>
              <div>
                <span className="text-xs font-semibold text-gray-500 uppercase tracking-wider block">Completed</span>
                <span className="text-3xl font-bold text-white block mt-0.5">{completedTasks}</span>
              </div>
            </div>

            {/* Progress Percentage */}
            <div className="glass-card p-6 rounded-2xl border border-white/5 flex items-center gap-4 relative overflow-hidden">
              <div className="p-3 bg-violet-500/10 text-violet-400 rounded-xl border border-violet-500/20">
                <TrendingUp size={24} />
              </div>
              <div className="flex-1">
                <span className="text-xs font-semibold text-gray-500 uppercase tracking-wider block">Completion Rate</span>
                <div className="flex items-center gap-3 mt-1">
                  <span className="text-3xl font-bold text-white">{completionRate}%</span>
                  <div className="flex-1 bg-white/5 rounded-full h-2 relative overflow-hidden border border-white/5">
                    <div
                      className="bg-gradient-to-r from-violet-500 to-indigo-500 h-full rounded-full transition-all duration-1000"
                      style={{ width: `${completionRate}%` }}
                    />
                  </div>
                </div>
              </div>
            </div>
          </div>

          {/* Quick Actions & Recent Tasks */}
          <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
            {/* Features Info Card */}
            <div className="glass-card p-8 rounded-3xl border border-white/5 flex flex-col justify-between relative overflow-hidden lg:col-span-1">
              {/* Background gradient element */}
              <div className="absolute -right-20 -bottom-20 w-48 h-48 bg-indigo-500/5 rounded-full blur-2xl pointer-events-none" />

              <div>
                <h3 className="text-xl font-bold text-white mb-6 flex items-center gap-2">
                  <ShieldCheck className="text-violet-400" size={20} />
                  <span>Key Framework Features</span>
                </h3>
                <ul className="space-y-5">
                  <li className="flex gap-3">
                    <div className="h-5 w-5 rounded bg-violet-500/20 border border-violet-500/30 flex items-center justify-center text-xs font-bold text-violet-300 mt-0.5">1</div>
                    <p className="text-sm text-gray-400">
                      State managed using <code className="text-violet-300 bg-violet-500/5 px-1 py-0.5 border border-violet-500/10 rounded">useReducer</code> and Context for unified data flows.
                    </p>
                  </li>
                  <li className="flex gap-3">
                    <div className="h-5 w-5 rounded bg-violet-500/20 border border-violet-500/30 flex items-center justify-center text-xs font-bold text-violet-300 mt-0.5">2</div>
                    <p className="text-sm text-gray-400">
                      Validations powered by <code className="text-violet-300 bg-violet-500/5 px-1 py-0.5 border border-violet-500/10 rounded">Formik</code> for strict inputs safety constraints.
                    </p>
                  </li>
                  <li className="flex gap-3">
                    <div className="h-5 w-5 rounded bg-violet-500/20 border border-violet-500/30 flex items-center justify-center text-xs font-bold text-violet-300 mt-0.5">3</div>
                    <p className="text-sm text-gray-400">
                      Network requests simulated asynchronously with real-time UI loading skeletons and error toggles.
                    </p>
                  </li>
                </ul>
              </div>

              <Link
                to="/tasks"
                className="mt-8 px-4 py-3 rounded-xl border border-white/5 hover:border-violet-500/30 bg-white/3 hover:bg-violet-500/5 text-sm font-semibold text-gray-300 hover:text-violet-300 transition-all flex items-center justify-between group cursor-pointer"
              >
                <span>Navigate to Tasks list</span>
                <ArrowRight size={16} className="group-hover:translate-x-1 transition-transform" />
              </Link>
            </div>

            {/* Recent Tasks */}
            <div className="glass-card p-8 rounded-3xl border border-white/5 lg:col-span-2">
              <div className="flex justify-between items-center mb-6">
                <h3 className="text-xl font-bold text-white">Recent Activities</h3>
                <Link to="/tasks" className="text-sm font-semibold text-violet-400 hover:text-violet-300 flex items-center gap-1">
                  <span>View all</span>
                  <ArrowRight size={14} />
                </Link>
              </div>

              {recentTasks.length === 0 ? (
                <div className="h-48 rounded-2xl border border-dashed border-white/5 flex flex-col items-center justify-center text-center p-6">
                  <p className="text-gray-500 italic mb-2">No tasks available in local storage.</p>
                  <Link to="/tasks" className="text-sm font-semibold text-violet-400 hover:text-violet-300">
                    Create your first task
                  </Link>
                </div>
              ) : (
                <div className="space-y-4">
                  {recentTasks.map((task) => (
                    <div
                      key={task.id}
                      className="p-4 rounded-xl border border-white/5 bg-white/1 hover:bg-white/2 transition-colors flex items-center justify-between gap-4"
                    >
                      <div className="flex-1 min-w-0">
                        <Link to={`/tasks/${task.id}`} className="font-semibold text-white hover:text-violet-400 transition-colors block truncate">
                          {task.name}
                        </Link>
                        <span className="text-xs text-gray-500 block mt-1">
                          Created {new Date(task.createdAt).toLocaleDateString()}
                        </span>
                      </div>
                      <span
                        className={`px-2.5 py-1 rounded-full text-xs font-semibold border ${
                          task.status === 'completed'
                            ? 'bg-emerald-500/10 border-emerald-500/20 text-emerald-400'
                            : task.status === 'in_progress'
                            ? 'bg-amber-500/10 border-amber-500/20 text-amber-400'
                            : 'bg-blue-500/10 border-blue-500/20 text-blue-400'
                        }`}
                      >
                        {task.status === 'completed' ? 'Completed' : task.status === 'in_progress' ? 'In Progress' : 'To Do'}
                      </span>
                    </div>
                  ))}
                </div>
              )}
            </div>
          </div>
        </>
      )}
    </div>
  );
};
