import React from 'react';
import { Link } from 'react-router-dom';
import { useTasks } from '../context/TaskContext';
import { CheckCircle2, Clock, ListTodo, Activity } from 'lucide-react';

export const Home: React.FC = () => {
  const { tasks, loading } = useTasks();

  const totalTasks = tasks.length;
  const completedTasks = tasks.filter((t) => t.status === 'Completed').length;
  const pendingTasks = tasks.filter((t) => t.status === 'Pending').length;
  const completionRate = totalTasks > 0 ? Math.round((completedTasks / totalTasks) * 100) : 0;

  return (
    <div className="space-y-8 animate-fade-in">
      {/* Hero Welcome Section */}
      <div className="bg-gradient-to-r from-slate-800 to-slate-900 border border-slate-700/50 rounded-2xl p-6 sm:p-8 relative overflow-hidden shadow-xl">
        <div className="absolute right-0 top-0 w-80 h-80 bg-violet-600/10 rounded-full blur-3xl -z-10" />
        <div className="absolute left-1/3 bottom-0 w-60 h-60 bg-indigo-600/10 rounded-full blur-3xl -z-10" />
        
        <div className="max-w-2xl space-y-4">
          <div className="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-violet-500/10 border border-violet-500/20 text-xs font-semibold text-violet-400">
            <Activity className="w-3.5 h-3.5" /> Workspace Active
          </div>
          <h1 className="text-3xl sm:text-4xl font-extrabold tracking-tight text-white leading-tight">
            Manage your daily project tasks <br className="hidden sm:inline" />
            <span className="bg-gradient-to-r from-violet-400 to-indigo-300 bg-clip-text text-transparent">
              efficiently and beautifully.
            </span>
          </h1>
          <p className="text-slate-400 text-sm sm:text-base">
            Track progress, update task statuses, and stay productive with TaskFlow. Access details, filter by status, and optimize your workflow.
          </p>
          <div className="pt-2 flex flex-wrap gap-3">
            <Link
              to="/tasks"
              className="inline-flex items-center gap-2 bg-gradient-to-r from-violet-600 to-indigo-600 hover:from-violet-500 hover:to-indigo-500 text-white font-semibold text-sm px-5 py-2.5 rounded-xl shadow-lg shadow-violet-500/25 transition-all transform hover:-translate-y-0.5"
            >
              Go to Task List &rarr;
            </Link>
          </div>
        </div>
      </div>

      {/* Analytics Grid */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
        {/* Total Tasks Card */}
        <div className="bg-slate-800/40 border border-slate-800 rounded-xl p-5 flex items-center justify-between hover:border-slate-700/80 transition-all">
          <div className="space-y-1">
            <p className="text-xs font-medium text-slate-400 uppercase tracking-wider">Total Tasks</p>
            <h3 className="text-2xl font-bold text-white">{loading ? '...' : totalTasks}</h3>
          </div>
          <div className="bg-slate-800 p-3 rounded-lg text-slate-300">
            <ListTodo className="w-6 h-6" />
          </div>
        </div>

        {/* Completed Tasks Card */}
        <div className="bg-slate-800/40 border border-slate-800 rounded-xl p-5 flex items-center justify-between hover:border-slate-700/80 transition-all">
          <div className="space-y-1">
            <p className="text-xs font-medium text-slate-400 uppercase tracking-wider">Completed</p>
            <h3 className="text-2xl font-bold text-emerald-400">{loading ? '...' : completedTasks}</h3>
          </div>
          <div className="bg-emerald-500/10 p-3 rounded-lg text-emerald-400 border border-emerald-500/20">
            <CheckCircle2 className="w-6 h-6" />
          </div>
        </div>

        {/* Pending Tasks Card */}
        <div className="bg-slate-800/40 border border-slate-800 rounded-xl p-5 flex items-center justify-between hover:border-slate-700/80 transition-all">
          <div className="space-y-1">
            <p className="text-xs font-medium text-slate-400 uppercase tracking-wider">Pending</p>
            <h3 className="text-2xl font-bold text-amber-400">{loading ? '...' : pendingTasks}</h3>
          </div>
          <div className="bg-amber-500/10 p-3 rounded-lg text-amber-400 border border-amber-500/20">
            <Clock className="w-6 h-6" />
          </div>
        </div>

        {/* Completion Rate Card */}
        <div className="bg-slate-800/40 border border-slate-800 rounded-xl p-5 flex items-center justify-between hover:border-slate-700/80 transition-all">
          <div className="space-y-1">
            <p className="text-xs font-medium text-slate-400 uppercase tracking-wider">Completion Rate</p>
            <h3 className="text-2xl font-bold text-indigo-400">{loading ? '...' : `${completionRate}%`}</h3>
          </div>
          <div className="w-12 h-12 rounded-full border-4 border-slate-700 flex items-center justify-center font-bold text-xs text-white relative">
            <div 
              className="absolute inset-0 rounded-full border-4 border-indigo-500 transition-all duration-500" 
              style={{ clipPath: `polygon(0 0, 100% 0, 100% 100%, 0 100%)`, opacity: completionRate > 0 ? 1 : 0 }}
            />
            {completionRate}%
          </div>
        </div>
      </div>

      {/* Quick Access / Recent Section */}
      <div className="bg-slate-900/60 border border-slate-800 rounded-xl p-6 space-y-4">
        <h2 className="text-lg font-bold text-white flex items-center gap-2">
          <span>Task Overview Guide</span>
        </h2>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4 text-sm text-slate-400">
          <div className="bg-slate-850 p-4 rounded-lg border border-slate-800/40 space-y-2">
            <h4 className="font-semibold text-white">How to view task details:</h4>
            <p>Navigate to the Tasks tab, search or filter for a specific task, and click on "View Details" to view, edit, or delete the task.</p>
          </div>
          <div className="bg-slate-850 p-4 rounded-lg border border-slate-800/40 space-y-2">
            <h4 className="font-semibold text-white">Using the forms:</h4>
            <p>Add new tasks using the Formik form, which validates task names (maximum 40 characters) and description lengths (maximum 200 characters).</p>
          </div>
        </div>
      </div>
    </div>
  );
};
