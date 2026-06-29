import React from 'react';
import { Link } from 'react-router-dom';
import { useTasks } from '../context/TaskContext';
import { CheckSquare, ListTodo, Plus, ClipboardList, AlertOctagon, RotateCcw } from 'lucide-react';

export const Home = () => {
  const { tasks, loading, error, toggleTaskStatus, refetchTasks } = useTasks();

  const totalTasks = tasks.length;
  const completedTasks = tasks.filter((t) => t.completed).length;
  const pendingTasks = totalTasks - completedTasks;
  const completionRate = totalTasks > 0 ? Math.round((completedTasks / totalTasks) * 100) : 0;

  const highPriorityTasks = tasks.filter((t) => t.priority === 'high' && !t.completed).length;

  const recentTasks = [...tasks]
    .sort((a, b) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime())
    .slice(0, 3);

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      {/* Header Section */}
      <div className="flex flex-col md:flex-row md:items-center justify-between gap-4 mb-8">
        <div>
          <h1 className="text-3xl font-extrabold text-slate-900 tracking-tight my-0">
            Welcome to TaskFlow
          </h1>
          <p className="mt-2 text-slate-500">
            Manage, organize, and track your daily tasks in one unified space.
          </p>
        </div>
        <div className="flex gap-3">
          <Link
            to="/tasks/new"
            className="flex items-center gap-2 px-4 py-2.5 bg-indigo-600 hover:bg-indigo-700 text-white rounded-xl text-sm font-semibold shadow-md transition-all hover:scale-[1.02]"
          >
            <Plus className="w-4 h-4" />
            <span>Create Task</span>
          </Link>
          <button
            onClick={refetchTasks}
            className="flex items-center gap-2 px-4 py-2.5 bg-white border border-slate-200 text-slate-700 hover:bg-slate-50 rounded-xl text-sm font-semibold shadow-sm transition-all cursor-pointer"
            title="Reset to API Initial Seed"
          >
            <RotateCcw className="w-4 h-4" />
            <span>Reset Demo Data</span>
          </button>
        </div>
      </div>

      {loading ? (
        <div className="py-20 flex justify-center">
          <div className="animate-pulse flex space-x-4">
            <div className="text-slate-400">Loading Dashboard Statistics...</div>
          </div>
        </div>
      ) : error ? (
        <div className="p-6 border border-rose-100 bg-rose-50/50 rounded-2xl text-rose-700 mb-8">
          <p>Failed to load dashboard metrics: {error}</p>
        </div>
      ) : (
        <>
          {/* Statistics Grid */}
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
            {/* Total Tasks */}
            <div className="bg-white p-6 rounded-2xl border border-slate-100 shadow-sm flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-slate-400 uppercase tracking-wider">Total Tasks</p>
                <h3 className="text-3xl font-extrabold text-slate-800 mt-1">{totalTasks}</h3>
              </div>
              <div className="p-3.5 bg-indigo-50 rounded-xl text-indigo-600">
                <ClipboardList className="w-6 h-6" />
              </div>
            </div>

            {/* Completed Tasks */}
            <div className="bg-white p-6 rounded-2xl border border-slate-100 shadow-sm flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-slate-400 uppercase tracking-wider">Completed</p>
                <h3 className="text-3xl font-extrabold text-emerald-600 mt-1">{completedTasks}</h3>
              </div>
              <div className="p-3.5 bg-emerald-50 rounded-xl text-emerald-600">
                <CheckSquare className="w-6 h-6" />
              </div>
            </div>

            {/* Pending Tasks */}
            <div className="bg-white p-6 rounded-2xl border border-slate-100 shadow-sm flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-slate-400 uppercase tracking-wider">Pending</p>
                <h3 className="text-3xl font-extrabold text-amber-600 mt-1">{pendingTasks}</h3>
              </div>
              <div className="p-3.5 bg-amber-50 rounded-xl text-amber-600">
                <ListTodo className="w-6 h-6" />
              </div>
            </div>

            {/* High Priority Urgent */}
            <div className="bg-white p-6 rounded-2xl border border-slate-100 shadow-sm flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-slate-400 uppercase tracking-wider">Urgent Pending</p>
                <h3 className="text-3xl font-extrabold text-rose-600 mt-1">{highPriorityTasks}</h3>
              </div>
              <div className="p-3.5 bg-rose-50 rounded-xl text-rose-600">
                <AlertOctagon className="w-6 h-6" />
              </div>
            </div>
          </div>

          {/* Main Dashboard Layout */}
          <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
            {/* Completion Progress Chart card */}
            <div className="bg-white p-6 rounded-2xl border border-slate-100 shadow-sm lg:col-span-1 flex flex-col justify-between">
              <div>
                <h3 className="text-lg font-bold text-slate-800 mb-2">Overall Progress</h3>
                <p className="text-sm text-slate-400 mb-6">Percentage of all tasks finished.</p>
              </div>
              <div className="flex flex-col items-center justify-center my-4">
                {/* Radial progress simulator */}
                <div className="relative w-36 h-36 flex items-center justify-center">
                  <svg className="w-full h-full transform -rotate-90" viewBox="0 0 36 36">
                    <path
                      className="text-slate-100"
                      strokeWidth="3.5"
                      stroke="currentColor"
                      fill="none"
                      d="M18 2.0845
                        a 15.9155 15.9155 0 0 1 0 31.831
                        a 15.9155 15.9155 0 0 1 0 -31.831"
                    />
                    <path
                      className="text-indigo-600 transition-all duration-1000 ease-out"
                      strokeDasharray={`${completionRate}, 100`}
                      strokeWidth="3.5"
                      strokeLinecap="round"
                      stroke="currentColor"
                      fill="none"
                      d="M18 2.0845
                        a 15.9155 15.9155 0 0 1 0 31.831
                        a 15.9155 15.9155 0 0 1 0 -31.831"
                    />
                  </svg>
                  <div className="absolute flex flex-col items-center">
                    <span className="text-3xl font-extrabold text-slate-800">{completionRate}%</span>
                    <span className="text-[10px] uppercase font-bold text-slate-400 tracking-wider">done</span>
                  </div>
                </div>
              </div>
              <div className="mt-4 pt-4 border-t border-slate-100 flex items-center justify-between text-xs text-slate-500">
                <span className="flex items-center gap-1.5">
                  <span className="w-2.5 h-2.5 rounded-full bg-indigo-600 inline-block"></span>
                  Completed ({completedTasks})
                </span>
                <span className="flex items-center gap-1.5">
                  <span className="w-2.5 h-2.5 rounded-full bg-slate-200 inline-block"></span>
                  Pending ({pendingTasks})
                </span>
              </div>
            </div>

            {/* Recent Tasks List */}
            <div className="bg-white p-6 rounded-2xl border border-slate-100 shadow-sm lg:col-span-2">
              <div className="flex items-center justify-between mb-6">
                <div>
                  <h3 className="text-lg font-bold text-slate-800">Recent Tasks</h3>
                  <p className="text-sm text-slate-400">Quickly toggle your latest additions.</p>
                </div>
                <Link
                  to="/tasks"
                  className="text-sm font-semibold text-indigo-600 hover:text-indigo-700 hover:underline"
                >
                  View All Tasks
                </Link>
              </div>

              {recentTasks.length === 0 ? (
                <div className="py-12 text-center text-slate-400">
                  <p className="mb-4">No tasks found. Create your first task to get started!</p>
                  <Link
                    to="/tasks/new"
                    className="inline-flex items-center gap-2 px-4 py-2 bg-indigo-50 hover:bg-indigo-100 text-indigo-600 rounded-lg text-sm font-semibold transition-colors"
                  >
                    <Plus className="w-4 h-4" />
                    <span>Create Task</span>
                  </Link>
                </div>
              ) : (
                <div className="space-y-4">
                  {recentTasks.map((task) => (
                    <div
                      key={task.id}
                      className={`flex items-center justify-between p-4 border rounded-xl transition-all ${
                        task.completed
                          ? 'bg-emerald-50/20 border-emerald-100 text-slate-400'
                          : 'bg-white border-slate-100 hover:border-slate-200 text-slate-700'
                      }`}
                    >
                      <div className="flex items-center gap-3 mr-4 overflow-hidden">
                        <button
                          onClick={() => toggleTaskStatus(task.id)}
                          className="text-slate-400 hover:text-indigo-600 cursor-pointer flex-shrink-0"
                        >
                          {task.completed ? (
                            <CheckSquare className="w-5 h-5 text-emerald-500" />
                          ) : (
                            <div className="w-5 h-5 rounded border-2 border-slate-300 hover:border-indigo-500"></div>
                          )}
                        </button>
                        <div className="truncate">
                          <p className={`font-semibold truncate ${task.completed ? 'line-through' : ''}`}>
                            {task.name}
                          </p>
                          <p className="text-xs text-slate-400 truncate">
                            {task.description || 'No description'}
                          </p>
                        </div>
                      </div>
                      <Link
                        to={`/tasks/${task.id}`}
                        className="text-xs font-semibold px-2.5 py-1 bg-slate-100 hover:bg-slate-200 text-slate-600 rounded-lg transition-colors flex-shrink-0"
                      >
                        Detail
                      </Link>
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
