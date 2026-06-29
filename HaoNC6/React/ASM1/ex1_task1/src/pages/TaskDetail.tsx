import React, { useState, useEffect } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import { useTasks } from '../context/TaskContext';
import { TaskForm } from '../components/TaskForm';
import type { Task } from '../types';
import {
  ArrowLeft,
  Calendar,
  Clock,
  CheckCircle2,
  Circle,
  Edit3,
  Trash2,
  FileText,
  BadgeAlert,
} from 'lucide-react';

export const TaskDetail: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const { state, updateTask, deleteTask } = useTasks();
  const { tasks, loading } = state;

  const [task, setTask] = useState<Task | null>(null);
  const [isEditing, setIsEditing] = useState(false);
  const [showDeleteConfirm, setShowDeleteConfirm] = useState(false);

  // Sync/Lookup task details when tasks array updates or component mounts
  useEffect(() => {
    if (!loading && tasks.length > 0) {
      const foundTask = tasks.find((t) => t.id === id);
      setTask(foundTask || null);
    }
  }, [id, tasks, loading]);

  const handleEditSubmit = (values: { name: string; description: string; status?: Task['status'] }) => {
    if (task) {
      const updated: Task = {
        ...task,
        name: values.name,
        description: values.description,
        status: values.status || 'todo',
      };
      updateTask(updated);
      setIsEditing(false);
    }
  };

  const handleDelete = () => {
    if (task) {
      deleteTask(task.id);
      navigate('/tasks');
    }
  };

  if (loading) {
    return (
      <div className="py-20 px-4 max-w-3xl mx-auto text-center">
        <div className="animate-pulse flex flex-col gap-6">
          <div className="h-6 w-24 bg-slate-800 rounded"></div>
          <div className="glass-card p-8 rounded-3xl border border-white/5 flex flex-col gap-6">
            <div className="h-10 w-2/3 bg-slate-800 rounded"></div>
            <div className="h-5 w-20 bg-slate-800 rounded-full"></div>
            <hr className="border-white/5" />
            <div className="h-4 w-full bg-slate-800 rounded"></div>
            <div className="h-4 w-5/6 bg-slate-800 rounded"></div>
            <div className="h-4 w-4/6 bg-slate-800 rounded"></div>
          </div>
        </div>
      </div>
    );
  }

  if (!task) {
    return (
      <div className="py-20 px-4 max-w-xl mx-auto text-center">
        <div className="glass-card p-10 rounded-3xl border border-rose-500/20 bg-rose-950/5 flex flex-col items-center">
          <div className="p-4 bg-rose-500/10 text-rose-400 rounded-2xl border border-rose-500/20 mb-6">
            <BadgeAlert size={36} />
          </div>
          <h2 className="text-2xl font-bold text-white mb-2">Task Not Found</h2>
          <p className="text-sm text-gray-400 mb-8 max-w-xs">
            The task details you are searching for do not exist or may have been deleted.
          </p>
          <Link
            to="/tasks"
            className="glass-button px-5 py-2.5 rounded-xl text-sm font-semibold text-white flex items-center gap-2 cursor-pointer"
          >
            <ArrowLeft size={16} />
            <span>Return to Tasks</span>
          </Link>
        </div>
      </div>
    );
  }

  const getStatusBadge = (status: Task['status']) => {
    switch (status) {
      case 'completed':
        return (
          <span className="inline-flex items-center gap-1.5 px-3 py-1 rounded-full text-xs font-semibold bg-emerald-500/10 border border-emerald-500/20 text-emerald-400 shadow-[0_0_15px_rgba(16,185,129,0.08)]">
            <CheckCircle2 size={14} />
            <span>Completed</span>
          </span>
        );
      case 'in_progress':
        return (
          <span className="inline-flex items-center gap-1.5 px-3 py-1 rounded-full text-xs font-semibold bg-amber-500/10 border border-amber-500/20 text-amber-400 shadow-[0_0_15px_rgba(245,158,11,0.08)] animate-pulse">
            <Clock size={14} />
            <span>In Progress</span>
          </span>
        );
      default:
        return (
          <span className="inline-flex items-center gap-1.5 px-3 py-1 rounded-full text-xs font-semibold bg-blue-500/10 border border-blue-500/20 text-blue-400 shadow-[0_0_15px_rgba(59,130,246,0.08)]">
            <Circle size={14} />
            <span>To Do</span>
          </span>
        );
    }
  };

  const formattedDate = new Date(task.createdAt).toLocaleString(undefined, {
    month: 'long',
    day: 'numeric',
    year: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
  });

  return (
    <div className="py-10 px-4 md:px-8 max-w-3xl mx-auto">
      {/* Back Button */}
      <Link
        to="/tasks"
        className="inline-flex items-center gap-1.5 text-sm font-semibold text-gray-400 hover:text-white transition-colors mb-8 group"
      >
        <ArrowLeft size={16} className="group-hover:-translate-x-0.5 transition-transform" />
        <span>Back to Task Dashboard</span>
      </Link>

      {isEditing ? (
        <div className="glass-card p-6 md:p-8 rounded-3xl border border-violet-500/20 shadow-2xl relative">
          <div className="flex items-center justify-between mb-6 pb-4 border-b border-white/5">
            <h2 className="text-xl font-bold text-white m-0">Edit Task details</h2>
            <button
              onClick={() => setIsEditing(false)}
              className="text-xs font-semibold text-gray-400 hover:text-white cursor-pointer"
            >
              Cancel Edit
            </button>
          </div>
          <TaskForm
            isEdit={true}
            initialValues={{
              name: task.name,
              description: task.description,
              status: task.status,
            }}
            submitText="Save Changes"
            onCancel={() => setIsEditing(false)}
            onSubmit={handleEditSubmit}
          />
        </div>
      ) : (
        <div className="glass-card p-8 rounded-3xl border border-white/5 relative overflow-hidden shadow-2xl">
          {/* Header */}
          <div className="flex flex-col md:flex-row md:items-start justify-between gap-4 mb-6">
            <div className="space-y-3">
              {getStatusBadge(task.status)}
              <h1 className="text-2xl md:text-3xl font-extrabold text-white tracking-tight leading-tight m-0">
                {task.name}
              </h1>
            </div>

            {/* Actions */}
            <div className="flex items-center gap-2 self-start md:self-auto">
              <button
                onClick={() => setIsEditing(true)}
                className="p-2.5 text-gray-400 hover:text-violet-400 hover:bg-violet-500/10 rounded-xl border border-white/5 hover:border-violet-500/10 transition-all active:scale-95 cursor-pointer flex items-center gap-1.5 text-xs font-semibold"
              >
                <Edit3 size={15} />
                <span className="hidden sm:inline">Edit</span>
              </button>
              <button
                onClick={() => setShowDeleteConfirm(true)}
                className="p-2.5 text-gray-400 hover:text-rose-400 hover:bg-rose-500/10 rounded-xl border border-white/5 hover:border-rose-500/10 transition-all active:scale-95 cursor-pointer flex items-center gap-1.5 text-xs font-semibold"
              >
                <Trash2 size={15} />
                <span className="hidden sm:inline">Delete</span>
              </button>
            </div>
          </div>

          <hr className="border-white/5 my-6" />

          {/* Description */}
          <div className="space-y-3 mb-8">
            <h4 className="text-xs font-bold text-gray-500 uppercase tracking-widest flex items-center gap-1.5">
              <FileText size={14} />
              <span>Description</span>
            </h4>
            <div className="p-5 rounded-2xl bg-white/1 border border-white/5 text-gray-300 text-sm leading-relaxed whitespace-pre-wrap">
              {task.description || <span className="text-gray-600 italic">No description provided for this task.</span>}
            </div>
          </div>

          <hr className="border-white/5 my-6" />

          {/* Date Created */}
          <div className="flex items-center gap-2 text-xs text-gray-500">
            <Calendar size={14} />
            <span>Created on {formattedDate}</span>
          </div>
        </div>
      )}

      {/* CONFIRM DELETE MODAL */}
      {showDeleteConfirm && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4">
          {/* Backdrop */}
          <div
            className="fixed inset-0 bg-black/60 backdrop-blur-sm"
            onClick={() => setShowDeleteConfirm(false)}
          />
          {/* Modal Content */}
          <div className="glass-card w-full max-w-md rounded-2xl border border-rose-500/20 bg-slate-950/90 relative z-10 overflow-hidden shadow-2xl p-6 text-center animate-in fade-in zoom-in duration-300">
            <div className="mx-auto w-12 h-12 bg-rose-500/10 text-rose-400 rounded-xl border border-rose-500/20 flex items-center justify-center mb-4">
              <Trash2 size={24} />
            </div>
            <h3 className="text-lg font-bold text-white mb-2">Delete Task?</h3>
            <p className="text-sm text-gray-400 mb-6 leading-relaxed">
              Are you sure you want to delete this task? This action is permanent and cannot be reverted.
            </p>
            <div className="flex items-center justify-center gap-3">
              <button
                onClick={() => setShowDeleteConfirm(false)}
                className="px-4 py-2 rounded-xl text-sm font-medium text-gray-400 hover:text-white hover:bg-white/5 border border-transparent transition-colors cursor-pointer"
              >
                Cancel
              </button>
              <button
                onClick={handleDelete}
                className="px-5 py-2.5 rounded-xl text-sm font-semibold bg-rose-500/20 hover:bg-rose-500/30 text-rose-300 border border-rose-500/30 transition-all cursor-pointer"
              >
                Delete permanently
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};
