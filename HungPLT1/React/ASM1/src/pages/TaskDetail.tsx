import React, { useEffect, useState } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import { useTasks } from '../context/TaskContext';
import type { Task } from '../types';
import { api } from '../utils/api';
import { TaskForm } from '../components/TaskForm';
import { Calendar, ArrowLeft, Edit2, Trash2, AlertTriangle, CheckCircle, Clock } from 'lucide-react';

export const TaskDetail: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const { editTask, deleteTask } = useTasks();

  const [task, setTask] = useState<Task | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [isEditing, setIsEditing] = useState(false);
  const [showDeleteConfirm, setShowDeleteConfirm] = useState(false);

  useEffect(() => {
    const fetchSingleTask = async () => {
      if (!id) return;
      setLoading(true);
      setError(null);
      try {
        const data = await api.getTaskById(id);
        setTask(data);
      } catch (err: any) {
        setError(err.message || 'Failed to load task details.');
      } finally {
        setLoading(false);
      }
    };

    fetchSingleTask();
  }, [id]);

  const handleEditSubmit = async (values: { name: string; description: string; status: any }) => {
    if (!id || !task) return;
    try {
      await editTask(id, values.name, values.description, values.status);
      setTask({
        ...task,
        name: values.name,
        description: values.description,
        status: values.status,
      });
      setIsEditing(false);
    } catch (err: any) {
      setError(err.message || 'Failed to update task.');
    }
  };

  const handleDeleteConfirm = async () => {
    if (!id) return;
    try {
      await deleteTask(id);
      navigate('/tasks');
    } catch (err: any) {
      setError(err.message || 'Failed to delete task.');
      setShowDeleteConfirm(false);
    }
  };

  if (loading) {
    return (
      <div className="flex flex-col items-center justify-center py-20 space-y-4">
        <div className="w-10 h-10 border-4 border-indigo-500 border-t-transparent rounded-full animate-spin"></div>
        <p className="text-slate-400 text-sm">Loading task details...</p>
      </div>
    );
  }

  if (error || !task) {
    return (
      <div className="max-w-lg mx-auto p-8 bg-slate-900 border border-slate-800 rounded-2xl text-center space-y-5">
        <div className="w-12 h-12 bg-rose-500/10 border border-rose-500/20 rounded-full flex items-center justify-center mx-auto">
          <AlertTriangle className="w-6 h-6 text-rose-500" />
        </div>
        <div className="space-y-1">
          <h2 className="text-lg font-semibold text-slate-200">Error Loading Task</h2>
          <p className="text-slate-400 text-sm">{error || 'Task not found.'}</p>
        </div>
        <Link
          to="/tasks"
          className="inline-flex items-center gap-2 text-xs font-semibold text-indigo-400 hover:text-indigo-300 transition-colors"
        >
          <ArrowLeft className="w-3.5 h-3.5" /> Back to Tasks
        </Link>
      </div>
    );
  }

  const getStatusStyles = (status: string) => {
    switch (status) {
      case 'completed':
        return 'bg-emerald-500/10 border-emerald-500/25 text-emerald-400';
      case 'in_progress':
        return 'bg-sky-500/10 border-sky-500/25 text-sky-400';
      default:
        return 'bg-amber-500/10 border-amber-500/25 text-amber-400';
    }
  };

  const formattedDate = new Date(task.createdAt).toLocaleDateString('en-US', {
    weekday: 'long',
    month: 'long',
    day: 'numeric',
    year: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
  });

  return (
    <div className="max-w-2xl mx-auto space-y-6">
      <div className="flex items-center justify-between">
        <Link
          to="/tasks"
          className="inline-flex items-center gap-2 text-xs text-slate-400 hover:text-slate-200 font-semibold transition-colors"
        >
          <ArrowLeft className="w-4 h-4" /> Back to list
        </Link>
      </div>

      <div className="bg-slate-900 border border-slate-800 rounded-2xl p-6 md:p-8 shadow-xl">
        {isEditing ? (
          <div className="space-y-6">
            <div>
              <h2 className="text-xl font-bold text-slate-100 mb-1">Edit Task</h2>
              <p className="text-slate-450 text-xs">Update your task properties and save changes.</p>
            </div>
            <TaskForm
              initialValues={{
                name: task.name,
                description: task.description,
                status: task.status,
              }}
              onSubmit={handleEditSubmit}
              onCancel={() => setIsEditing(false)}
              submitButtonText="Save Changes"
            />
          </div>
        ) : showDeleteConfirm ? (
          <div className="text-center py-4 space-y-6">
            <div className="w-14 h-14 bg-rose-500/10 border border-rose-500/20 rounded-full flex items-center justify-center mx-auto">
              <AlertTriangle className="w-8 h-8 text-rose-400" />
            </div>
            <div className="space-y-2">
              <h3 className="text-lg font-bold text-slate-100">Delete Task</h3>
              <p className="text-slate-400 text-sm max-w-sm mx-auto">
                Are you sure you want to delete <strong className="text-slate-200">"{task.name}"</strong>? This action cannot be undone.
              </p>
            </div>
            <div className="flex justify-center gap-4 max-w-xs mx-auto">
              <button
                type="button"
                onClick={() => setShowDeleteConfirm(false)}
                className="flex-1 py-2.5 bg-slate-800 hover:bg-slate-700 text-slate-350 text-sm font-semibold rounded-xl transition-colors cursor-pointer"
              >
                Cancel
              </button>
              <button
                type="button"
                onClick={handleDeleteConfirm}
                className="flex-1 py-2.5 bg-rose-600 hover:bg-rose-500 text-white text-sm font-semibold rounded-xl shadow-lg shadow-rose-950/20 transition-colors cursor-pointer"
              >
                Delete
              </button>
            </div>
          </div>
        ) : (
          <div className="space-y-6">
            <div className="flex flex-wrap justify-between items-start gap-4">
              <span className={`inline-flex items-center gap-1.5 px-3 py-1 rounded-full text-xs font-semibold border ${getStatusStyles(task.status)}`}>
                {task.status === 'completed' && <CheckCircle className="w-3.5 h-3.5" />}
                {task.status === 'in_progress' && <Clock className="w-3.5 h-3.5" />}
                {task.status === 'pending' && <AlertTriangle className="w-3.5 h-3.5" />}
                {task.status.replace('_', ' ')}
              </span>

              <div className="flex items-center gap-2">
                <button
                  onClick={() => setIsEditing(true)}
                  className="p-2 bg-slate-950/60 border border-slate-800 hover:border-indigo-500/50 hover:bg-slate-800 text-slate-300 hover:text-indigo-400 rounded-xl transition-all cursor-pointer"
                  title="Edit task"
                >
                  <Edit2 className="w-4 h-4" />
                </button>
                <button
                  onClick={() => setShowDeleteConfirm(true)}
                  className="p-2 bg-slate-950/60 border border-slate-800 hover:border-rose-500/50 hover:bg-slate-800 text-slate-300 hover:text-rose-400 rounded-xl transition-all cursor-pointer"
                  title="Delete task"
                >
                  <Trash2 className="w-4 h-4" />
                </button>
              </div>
            </div>

            <div className="space-y-2">
              <h1 className="text-2xl font-bold text-slate-100 leading-snug">{task.name}</h1>
              <div className="flex items-center gap-1.5 text-slate-500 text-xs">
                <Calendar className="w-3.5 h-3.5" />
                <span>Created on {formattedDate}</span>
              </div>
            </div>

            <div className="pt-6 border-t border-slate-800/60">
              <h3 className="text-xs font-bold text-slate-450 uppercase tracking-wider mb-2">Description</h3>
              <p className="text-slate-300 text-sm leading-relaxed whitespace-pre-wrap">
                {task.description || <em className="text-slate-500">No description provided for this task.</em>}
              </p>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};
