import React from 'react';
import { useParams, Link, useNavigate } from 'react-router-dom';
import { useTasks } from '../context/TaskContext';
import { ArrowLeft, Calendar, Edit3, Trash2, CheckCircle2, Clock, ShieldAlert } from 'lucide-react';

export const TaskDetail = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const { tasks, toggleTaskStatus, deleteTask } = useTasks();

  const task = tasks.find((t) => t.id === id);

  if (!task) {
    return (
      <div className="max-w-md mx-auto my-16 p-8 bg-white border border-slate-200 rounded-2xl shadow-sm text-center">
        <div className="inline-flex p-3.5 bg-slate-50 border border-slate-100 rounded-full text-slate-400 mb-4">
          <ShieldAlert className="w-8 h-8" />
        </div>
        <h3 className="text-xl font-bold text-slate-800 mb-2">Task Not Found</h3>
        <p className="text-sm text-slate-500 mb-6">
          The task you are trying to view does not exist or has been deleted.
        </p>
        <Link
          to="/tasks"
          className="inline-flex items-center gap-2 px-4 py-2 bg-indigo-600 hover:bg-indigo-700 text-white rounded-lg text-sm font-semibold transition-colors shadow-sm"
        >
          <ArrowLeft className="w-4 h-4" />
          <span>Back to Tasks</span>
        </Link>
      </div>
    );
  }

  const getPriorityBadge = (priority) => {
    switch (priority) {
      case 'high':
        return 'bg-rose-50 text-rose-700 border-rose-200';
      case 'medium':
        return 'bg-amber-50 text-amber-700 border-amber-200';
      case 'low':
      default:
        return 'bg-slate-50 text-slate-700 border-slate-200';
    }
  };

  const handleToggle = () => {
    toggleTaskStatus(task.id);
  };

  const handleDelete = () => {
    if (window.confirm(`Are you sure you want to delete "${task.name}"?`)) {
      deleteTask(task.id);
      navigate('/tasks');
    }
  };

  return (
    <div className="max-w-3xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      {/* Back link */}
      <Link
        to="/tasks"
        className="inline-flex items-center gap-2 text-sm font-semibold text-slate-500 hover:text-indigo-600 transition-colors mb-6 group"
      >
        <ArrowLeft className="w-4 h-4 transition-transform group-hover:-translate-x-1" />
        <span>Back to Task Dashboard</span>
      </Link>

      {/* Main card */}
      <div className="bg-white border border-slate-200 rounded-3xl p-6 md:p-8 shadow-sm">
        {/* Badges and actions bar */}
        <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 pb-6 border-b border-slate-100 mb-6">
          <div className="flex flex-wrap gap-2">
            <span className={`text-xs font-bold px-3 py-1 rounded-full border uppercase tracking-wider ${getPriorityBadge(task.priority)}`}>
              {task.priority} Priority
            </span>
            <span className={`text-xs font-bold px-3 py-1 rounded-full border uppercase tracking-wider ${task.completed ? 'bg-emerald-50 text-emerald-700 border-emerald-200' : 'bg-blue-50 text-blue-700 border-blue-200'}`}>
              {task.completed ? 'completed' : 'pending'}
            </span>
          </div>

          <div className="flex gap-2">
            <button
              onClick={handleToggle}
              className={`flex items-center gap-1.5 px-3 py-1.5 rounded-lg text-xs font-semibold border transition-all cursor-pointer ${
                task.completed
                  ? 'bg-slate-50 hover:bg-slate-100 text-slate-600 border-slate-200'
                  : 'bg-emerald-50 hover:bg-emerald-100 text-emerald-700 border-emerald-200'
              }`}
            >
              {task.completed ? <Clock className="w-3.5 h-3.5" /> : <CheckCircle2 className="w-3.5 h-3.5" />}
              <span>{task.completed ? 'Mark Pending' : 'Mark Completed'}</span>
            </button>
            <Link
              to={`/tasks/edit/${task.id}`}
              className="flex items-center gap-1.5 px-3 py-1.5 bg-indigo-50 hover:bg-indigo-100 text-indigo-700 rounded-lg text-xs font-semibold border border-indigo-100 transition-all"
            >
              <Edit3 className="w-3.5 h-3.5" />
              <span>Edit Details</span>
            </Link>
            <button
              onClick={handleDelete}
              className="flex items-center gap-1.5 px-3 py-1.5 bg-rose-50 hover:bg-rose-100 text-rose-700 rounded-lg text-xs font-semibold border border-rose-100 transition-all cursor-pointer"
            >
              <Trash2 className="w-3.5 h-3.5" />
              <span>Delete</span>
            </button>
          </div>
        </div>

        {/* Content */}
        <div>
          <h2 className="text-2xl md:text-3xl font-extrabold text-slate-900 leading-tight mb-4">
            {task.name}
          </h2>

          <div className="flex items-center gap-2 text-slate-400 text-sm mb-6">
            <Calendar className="w-4 h-4 text-slate-400" />
            <span>Created on {new Date(task.createdAt).toLocaleString(undefined, {
              dateStyle: 'long',
              timeStyle: 'short'
            })}</span>
          </div>

          <div className="prose prose-slate max-w-none">
            <h4 className="text-xs font-bold text-slate-400 uppercase tracking-widest mb-2">Description</h4>
            {task.description ? (
              <p className="text-slate-600 bg-slate-50 border border-slate-100 rounded-2xl p-5 leading-relaxed text-sm md:text-base whitespace-pre-wrap">
                {task.description}
              </p>
            ) : (
              <p className="text-slate-400 italic bg-slate-50 border border-slate-100 rounded-2xl p-5 text-sm">
                No description provided for this task. Click Edit to add details.
              </p>
            )}
          </div>
        </div>

        {/* Metadata info */}
        <div className="mt-8 pt-6 border-t border-slate-100 grid grid-cols-2 md:grid-cols-3 gap-4 text-xs text-slate-400">
          <div>
            <span className="block font-medium text-slate-300 uppercase tracking-wider mb-0.5">Task ID</span>
            <code className="text-[11px] bg-slate-50 border border-slate-100 rounded px-1.5 py-0.5 font-mono text-slate-600">
              {task.id}
            </code>
          </div>
          <div>
            <span className="block font-medium text-slate-300 uppercase tracking-wider mb-0.5">Status state</span>
            <span className={task.completed ? 'text-emerald-500 font-semibold' : 'text-blue-500 font-semibold'}>
              {task.completed ? 'Finished' : 'In Progress'}
            </span>
          </div>
          <div className="col-span-2 md:col-span-1">
            <span className="block font-medium text-slate-300 uppercase tracking-wider mb-0.5">Priority level</span>
            <span className={`font-semibold capitalize ${
              task.priority === 'high' ? 'text-rose-500' : task.priority === 'medium' ? 'text-amber-500' : 'text-slate-500'
            }`}>
              {task.priority}
            </span>
          </div>
        </div>
      </div>
    </div>
  );
};
