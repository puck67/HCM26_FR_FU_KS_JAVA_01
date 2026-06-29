import React, { useEffect } from 'react';
import { Link } from 'react-router-dom';
import { useTasks } from '../context/TaskContext';
import { CheckSquare, AlertCircle, Clock, CheckCircle, PlusCircle, ArrowRight } from 'lucide-react';

export const Home: React.FC = () => {
  const { tasks, loading, error, fetchTasks } = useTasks();

  useEffect(() => {
    fetchTasks();
  }, [fetchTasks]);

  const total = tasks.length;
  const pending = tasks.filter(t => t.status === 'pending').length;
  const inProgress = tasks.filter(t => t.status === 'in_progress').length;
  const completed = tasks.filter(t => t.status === 'completed').length;

  const recentTasks = [...tasks]
    .sort((a, b) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime())
    .slice(0, 3);

  return (
    <div className="space-y-8">
      <div>
        <h1 className="text-3xl font-bold bg-gradient-to-r from-slate-100 to-slate-300 bg-clip-text text-transparent mb-2">Welcome to TaskFlow</h1>
        <p className="text-slate-400 text-sm">Stay organized, track your progress, and manage your daily tasks efficiently.</p>
      </div>

      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-5">
        <div className="bg-slate-900 border border-slate-800/80 rounded-2xl p-6 shadow-lg relative overflow-hidden group">
          <div className="absolute right-0 top-0 w-24 h-24 bg-indigo-500/5 rounded-full blur-2xl group-hover:bg-indigo-500/10 transition-all duration-300"></div>
          <div className="w-10 h-10 rounded-xl bg-indigo-500/10 border border-indigo-500/20 flex items-center justify-center mb-4">
            <CheckSquare className="w-5 h-5 text-indigo-400" />
          </div>
          <div className="text-3xl font-bold text-slate-100 mb-1">{total}</div>
          <div className="text-slate-450 text-xs font-medium uppercase tracking-wider">Total Tasks</div>
        </div>

        <div className="bg-slate-900 border border-slate-800/80 rounded-2xl p-6 shadow-lg relative overflow-hidden group">
          <div className="absolute right-0 top-0 w-24 h-24 bg-amber-500/5 rounded-full blur-2xl group-hover:bg-amber-500/10 transition-all duration-300"></div>
          <div className="w-10 h-10 rounded-xl bg-amber-500/10 border border-amber-500/20 flex items-center justify-center mb-4">
            <AlertCircle className="w-5 h-5 text-amber-400" />
          </div>
          <div className="text-3xl font-bold text-slate-100 mb-1">{pending}</div>
          <div className="text-slate-450 text-xs font-medium uppercase tracking-wider">Pending</div>
        </div>

        <div className="bg-slate-900 border border-slate-800/80 rounded-2xl p-6 shadow-lg relative overflow-hidden group">
          <div className="absolute right-0 top-0 w-24 h-24 bg-sky-500/5 rounded-full blur-2xl group-hover:bg-sky-500/10 transition-all duration-300"></div>
          <div className="w-10 h-10 rounded-xl bg-sky-500/10 border border-sky-500/20 flex items-center justify-center mb-4">
            <Clock className="w-5 h-5 text-sky-400" />
          </div>
          <div className="text-3xl font-bold text-slate-100 mb-1">{inProgress}</div>
          <div className="text-slate-450 text-xs font-medium uppercase tracking-wider">In Progress</div>
        </div>

        <div className="bg-slate-900 border border-slate-800/80 rounded-2xl p-6 shadow-lg relative overflow-hidden group">
          <div className="absolute right-0 top-0 w-24 h-24 bg-emerald-500/5 rounded-full blur-2xl group-hover:bg-emerald-500/10 transition-all duration-300"></div>
          <div className="w-10 h-10 rounded-xl bg-emerald-500/10 border border-emerald-500/20 flex items-center justify-center mb-4">
            <CheckCircle className="w-5 h-5 text-emerald-400" />
          </div>
          <div className="text-3xl font-bold text-slate-100 mb-1">{completed}</div>
          <div className="text-slate-450 text-xs font-medium uppercase tracking-wider">Completed</div>
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <div className="lg:col-span-2 bg-slate-900 border border-slate-800/80 rounded-2xl p-6 shadow-lg flex flex-col justify-between">
          <div>
            <div className="flex justify-between items-center mb-6">
              <h2 className="font-bold text-lg text-slate-200">Recent Tasks</h2>
              <Link to="/tasks" className="text-xs text-indigo-400 hover:text-indigo-300 font-semibold flex items-center gap-1 transition-colors">
                View all tasks <ArrowRight className="w-3.5 h-3.5" />
              </Link>
            </div>

            {loading ? (
              <div className="space-y-4">
                {[1, 2].map((i) => (
                  <div key={i} className="h-16 bg-slate-800/50 rounded-xl animate-pulse"></div>
                ))}
              </div>
            ) : error ? (
              <div className="p-4 bg-rose-500/10 border border-rose-500/20 rounded-xl text-rose-400 text-sm">
                {error}
              </div>
            ) : recentTasks.length === 0 ? (
              <div className="text-center py-10 border border-dashed border-slate-800 rounded-xl">
                <p className="text-slate-500 text-sm mb-4">No tasks found. Get started by creating your first task.</p>
                <Link
                  to="/tasks/new"
                  className="inline-flex items-center gap-2 px-4 py-2 bg-indigo-600 hover:bg-indigo-500 text-white text-xs font-semibold rounded-xl transition-all duration-200"
                >
                  <PlusCircle className="w-3.5 h-3.5" /> Create Task
                </Link>
              </div>
            ) : (
              <div className="space-y-3">
                {recentTasks.map((task) => (
                  <Link
                    key={task.id}
                    to={`/tasks/${task.id}`}
                    className="flex justify-between items-center p-4 bg-slate-950/40 hover:bg-slate-950/80 border border-slate-800/60 rounded-xl hover:border-slate-700 transition-all duration-200 group"
                  >
                    <div className="min-w-0 flex-1 pr-4">
                      <h4 className="text-sm font-semibold text-slate-200 group-hover:text-indigo-400 transition-colors truncate">
                        {task.name}
                      </h4>
                      <p className="text-xs text-slate-500 truncate mt-0.5">
                        {task.description || 'No description'}
                      </p>
                    </div>
                    <span className={`px-2.5 py-0.5 rounded-full text-[10px] font-semibold tracking-wide uppercase shrink-0 border ${
                      task.status === 'completed'
                        ? 'bg-emerald-500/10 border-emerald-500/20 text-emerald-400'
                        : task.status === 'in_progress'
                        ? 'bg-sky-500/10 border-sky-500/20 text-sky-400'
                        : 'bg-amber-500/10 border-amber-500/20 text-amber-400'
                    }`}>
                      {task.status.replace('_', ' ')}
                    </span>
                  </Link>
                ))}
              </div>
            )}
          </div>
        </div>

        <div className="bg-slate-900 border border-slate-800/80 rounded-2xl p-6 shadow-lg flex flex-col justify-between">
          <div>
            <h2 className="font-bold text-lg text-slate-200 mb-2">Performance Analytics</h2>
            <p className="text-xs text-slate-400 mb-6">Overview of task status distribution and completion rates.</p>

            <div className="space-y-4">
              <div>
                <div className="flex justify-between text-xs text-slate-400 mb-1.5">
                  <span>Task Completion Progress</span>
                  <span className="font-semibold text-slate-200">
                    {total > 0 ? Math.round((completed / total) * 100) : 0}%
                  </span>
                </div>
                <div className="h-2 w-full bg-slate-950 rounded-full overflow-hidden">
                  <div
                    className="h-full bg-gradient-to-r from-indigo-500 to-emerald-400 rounded-full transition-all duration-500"
                    style={{ width: `${total > 0 ? (completed / total) * 100 : 0}%` }}
                  ></div>
                </div>
              </div>

              <div className="grid grid-cols-3 gap-2 pt-4 border-t border-slate-800/60 text-center">
                <div className="p-2 bg-slate-950/40 rounded-lg">
                  <div className="text-xs text-slate-500 mb-0.5">Pending</div>
                  <div className="text-sm font-semibold text-amber-400">
                    {total > 0 ? Math.round((pending / total) * 100) : 0}%
                  </div>
                </div>
                <div className="p-2 bg-slate-950/40 rounded-lg">
                  <div className="text-xs text-slate-500 mb-0.5">Active</div>
                  <div className="text-sm font-semibold text-sky-400">
                    {total > 0 ? Math.round((inProgress / total) * 100) : 0}%
                  </div>
                </div>
                <div className="p-2 bg-slate-950/40 rounded-lg">
                  <div className="text-xs text-slate-500 mb-0.5">Done</div>
                  <div className="text-sm font-semibold text-emerald-400">
                    {total > 0 ? Math.round((completed / total) * 100) : 0}%
                  </div>
                </div>
              </div>
            </div>
          </div>

          <div className="pt-6 border-t border-slate-800/60 mt-6">
            <Link
              to="/tasks/new"
              className="flex items-center justify-center gap-2 w-full py-3 bg-slate-800 hover:bg-slate-750 text-indigo-400 hover:text-indigo-300 text-sm font-semibold rounded-xl border border-indigo-500/20 hover:border-indigo-500/40 transition-all duration-200"
            >
              <PlusCircle className="w-4 h-4" /> Create New Task
            </Link>
          </div>
        </div>
      </div>
    </div>
  );
};
