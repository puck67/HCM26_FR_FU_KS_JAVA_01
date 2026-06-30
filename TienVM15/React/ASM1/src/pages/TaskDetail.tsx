import React, { useState } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import { useTasks } from '../context/TaskContext';
import { TaskFormModal } from '../components/TaskFormModal';
import { ArrowLeft, CheckCircle, Clock, Calendar, Edit, Trash2 } from 'lucide-react';
import type { Task } from '../types/task';

export const TaskDetail: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const { tasks, dispatch, loading } = useTasks();

  const [isModalOpen, setIsModalOpen] = useState(false);

  const task = tasks.find((t) => t.id === id);

  if (loading) {
    return (
      <div className="min-h-[300px] flex items-center justify-center">
        <p className="text-slate-400 text-sm">Loading task details...</p>
      </div>
    );
  }

  if (!task) {
    return (
      <div className="bg-slate-900/60 border border-slate-800 rounded-2xl p-8 text-center max-w-md mx-auto space-y-4">
        <h3 className="font-bold text-white text-lg">Task Not Found</h3>
        <p className="text-slate-400 text-sm">The task you are looking for does not exist or has been deleted.</p>
        <Link
          to="/tasks"
          className="inline-flex items-center gap-2 text-violet-400 hover:text-white hover:underline text-sm font-semibold"
        >
          <ArrowLeft className="w-4 h-4" /> Back to tasks list
        </Link>
      </div>
    );
  }

  const handleDelete = () => {
    if (window.confirm('Are you sure you want to delete this task?')) {
      dispatch({ type: 'DELETE_TASK', payload: task.id });
      navigate('/tasks');
    }
  };

  const handleToggleStatus = () => {
    const updated: Task = {
      ...task,
      status: task.status === 'Completed' ? 'Pending' : 'Completed',
    };
    dispatch({ type: 'UPDATE_TASK', payload: updated });
  };

  const handleEditSubmit = async (values: { name: string; description: string; status: 'Pending' | 'Completed' }) => {
    await new Promise((resolve) => setTimeout(resolve, 500));
    const updated: Task = {
      ...task,
      name: values.name,
      description: values.description,
      status: values.status,
    };
    dispatch({ type: 'UPDATE_TASK', payload: updated });
  };

  return (
    <div className="max-w-2xl mx-auto space-y-6">
      {/* Back button */}
      <div>
        <Link
          to="/tasks"
          className="inline-flex items-center gap-2 text-slate-400 hover:text-white transition-colors text-sm font-medium"
        >
          <ArrowLeft className="w-4 h-4" />
          Back to list
        </Link>
      </div>

      {/* Task Details Card */}
      <div className="bg-slate-900/60 border border-slate-800 rounded-2xl overflow-hidden shadow-xl">
        <div className="p-6 sm:p-8 space-y-6">
          {/* Header Status & Action Controls */}
          <div className="flex flex-wrap items-center justify-between gap-4">
            <button
              onClick={handleToggleStatus}
              className={`inline-flex items-center gap-1.5 px-3.5 py-1.5 rounded-full text-xs font-semibold border transition-all ${
                task.status === 'Completed'
                  ? 'bg-emerald-500/10 border-emerald-500/20 text-emerald-400 hover:bg-emerald-500/20'
                  : 'bg-amber-500/10 border-amber-500/20 text-amber-400 hover:bg-amber-500/20'
              }`}
            >
              {task.status === 'Completed' ? (
                <>
                  <CheckCircle className="w-4 h-4" />
                  Completed
                </>
              ) : (
                <>
                  <Clock className="w-4 h-4" />
                  Pending
                </>
              )}
            </button>

            <div className="flex items-center gap-2">
              <button
                onClick={() => setIsModalOpen(true)}
                className="inline-flex items-center gap-1.5 bg-slate-800 hover:bg-slate-700 text-slate-200 hover:text-white text-xs font-semibold px-3 py-1.5 rounded-lg border border-slate-700 transition-all"
              >
                <Edit className="w-3.5 h-3.5" />
                Edit
              </button>
              <button
                onClick={handleDelete}
                className="inline-flex items-center gap-1.5 bg-rose-500/10 hover:bg-rose-500/25 text-rose-400 text-xs font-semibold px-3 py-1.5 rounded-lg border border-rose-500/20 transition-all"
              >
                <Trash2 className="w-3.5 h-3.5" />
                Delete
              </button>
            </div>
          </div>

          {/* Details Content */}
          <div className="space-y-4">
            <h1 className="text-2xl sm:text-3xl font-extrabold text-white leading-tight">
              {task.name}
            </h1>

            <div className="flex items-center gap-2 text-slate-500 text-xs">
              <Calendar className="w-4 h-4" />
              <span>Created on {new Date(task.createdAt).toLocaleString()}</span>
            </div>

            <div className="pt-4 border-t border-slate-800">
              <h3 className="text-xs font-bold text-slate-400 uppercase tracking-wider mb-2">Description</h3>
              <p className="text-slate-300 text-sm leading-relaxed whitespace-pre-wrap">
                {task.description || 'No description was provided for this task.'}
              </p>
            </div>
          </div>
        </div>
      </div>

      {/* Form modal */}
      <TaskFormModal
        isOpen={isModalOpen}
        task={task}
        onClose={() => setIsModalOpen(false)}
        onSubmit={handleEditSubmit}
      />
    </div>
  );
};
