import React, { useState, useEffect } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import { useTasks } from '../context/TaskContext';
import { TaskForm } from '../components/TaskForm';
import { api } from '../services/api';
import type { Task } from '../services/api';
import { 
  ArrowLeft, 
  Calendar, 
  CheckCircle2, 
  Clock, 
  Trash2, 
  Edit3, 
  AlertTriangle,
  Loader2
} from 'lucide-react';

export const TaskDetail: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const { state, deleteTask, toggleTaskCompletion } = useTasks();
  
  const [task, setTask] = useState<Task | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [isFormOpen, setIsFormOpen] = useState(false);

  // Sync with context tasks array and fetch from API if not found (e.g. direct url load)
  useEffect(() => {
    let active = true;

    const loadTask = async () => {
      if (!id) return;
      
      // First try to look up in context state
      const taskFromContext = state.tasks.find(t => t.id === id);
      if (taskFromContext) {
        if (active) {
          setTask(taskFromContext);
          setLoading(false);
          setError(null);
        }
        return;
      }

      // If not in context (e.g. refreshing on the page), fetch directly from API
      try {
        if (active) setLoading(true);
        const taskData = await api.getTaskById(id);
        if (active) {
          setTask(taskData);
          setError(null);
        }
      } catch (err: any) {
        if (active) {
          setError(err.message || 'Task not found');
        }
      } finally {
        if (active) setLoading(false);
      }
    };

    loadTask();

    return () => {
      active = false;
    };
  }, [id, state.tasks]);

  // Handle completion toggle
  const handleToggle = async () => {
    if (!task) return;
    try {
      await toggleTaskCompletion(task.id, !task.completed);
    } catch (err: any) {
      alert(err.message || 'Failed to update task status');
    }
  };

  // Handle delete
  const handleDelete = async () => {
    if (!task) return;
    if (window.confirm('Are you sure you want to delete this task?')) {
      try {
        await deleteTask(task.id);
        navigate('/tasks');
      } catch (err: any) {
        alert(err.message || 'Failed to delete task');
      }
    }
  };

  const formatDate = (isoString: string) => {
    const date = new Date(isoString);
    return date.toLocaleDateString('en-US', {
      weekday: 'long',
      month: 'long',
      day: 'numeric',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  };

  return (
    <div className="container mx-auto px-6 py-10 max-w-3xl">
      {/* Back button */}
      <Link
        to="/tasks"
        className="inline-flex items-center gap-2 text-slate-400 hover:text-white mb-6 group transition-colors text-sm font-semibold"
      >
        <ArrowLeft className="w-4 h-4 group-hover:-translate-x-1 transition-transform" />
        <span>Back to Tasks</span>
      </Link>

      {/* Loading State */}
      {loading && (
        <div className="glass-card p-12 text-center rounded-3xl flex flex-col items-center">
          <Loader2 className="w-10 h-10 text-indigo-500 animate-spin mb-4" />
          <p className="text-slate-400 font-medium">Retrieving task details...</p>
        </div>
      )}

      {/* Error State */}
      {!loading && error && (
        <div className="glass-card p-10 text-center rounded-3xl border-rose-500/20 bg-rose-950/10">
          <AlertTriangle className="w-12 h-12 text-rose-500 mx-auto mb-4 animate-bounce" />
          <h3 className="text-xl font-bold text-white mb-2">Task Not Found</h3>
          <p className="text-slate-400 text-sm mb-6 max-w-sm mx-auto">{error}</p>
          <Link
            to="/tasks"
            className="px-5 py-2.5 bg-slate-800 hover:bg-slate-700 text-white font-semibold rounded-xl text-sm transition-all"
          >
            Return to list
          </Link>
        </div>
      )}

      {/* Detail Card */}
      {!loading && !error && task && (
        <>
          <div className="glass-card rounded-3xl overflow-hidden shadow-2xl relative">
            {/* Header border status highlight */}
            <div className={`h-2 w-full ${task.completed ? 'bg-emerald-500' : 'bg-indigo-500'}`} />

            <div className="p-8 md:p-10 space-y-6">
              {/* Badge & Meta */}
              <div className="flex flex-wrap items-center justify-between gap-4">
                <div className="flex items-center gap-1.5 text-xs text-slate-400 font-medium">
                  <Calendar className="w-4 h-4 text-indigo-400" />
                  <span>Created on {formatDate(task.createdAt)}</span>
                </div>
                
                <button
                  onClick={handleToggle}
                  className={`inline-flex items-center gap-1.5 px-3 py-1 rounded-full text-xs font-semibold border transition-all ${
                    task.completed
                      ? 'bg-emerald-500/10 text-emerald-400 border-emerald-500/20 hover:bg-emerald-500/20'
                      : 'bg-indigo-500/10 text-indigo-400 border-indigo-500/20 hover:bg-indigo-500/20'
                  }`}
                >
                  {task.completed ? (
                    <>
                      <CheckCircle2 className="w-3.5 h-3.5" />
                      <span>Completed</span>
                    </>
                  ) : (
                    <>
                      <Clock className="w-3.5 h-3.5" />
                      <span>In Progress</span>
                    </>
                  )}
                </button>
              </div>

              {/* Title & Description */}
              <div className="space-y-4">
                <h2 className="text-2xl md:text-3xl font-extrabold text-white leading-tight">
                  {task.name}
                </h2>
                <div className="bg-slate-950/30 p-6 rounded-2xl border border-white/5 min-h-[120px]">
                  <p className="text-slate-300 text-base leading-relaxed whitespace-pre-wrap">
                    {task.description || <span className="italic text-slate-500">No description provided for this task.</span>}
                  </p>
                </div>
              </div>

              {/* Actions Footer */}
              <div className="flex flex-wrap justify-between items-center gap-4 pt-6 border-t border-white/10">
                <button
                  onClick={handleToggle}
                  className={`px-5 py-2.5 rounded-xl font-semibold text-sm transition-all duration-200 border ${
                    task.completed
                      ? 'bg-slate-800 hover:bg-slate-700 text-slate-300 border-white/5'
                      : 'bg-emerald-600 hover:bg-emerald-500 active:bg-emerald-700 text-white border-transparent shadow-lg shadow-emerald-600/25'
                  }`}
                >
                  {task.completed ? 'Mark Active' : 'Mark Completed'}
                </button>

                <div className="flex items-center gap-3">
                  <button
                    onClick={() => setIsFormOpen(true)}
                    className="inline-flex items-center gap-2 px-4 py-2.5 bg-white/5 hover:bg-indigo-600/15 border border-white/10 hover:border-indigo-500/20 text-slate-300 hover:text-indigo-400 font-semibold rounded-xl text-sm transition-all duration-200"
                  >
                    <Edit3 className="w-4 h-4" />
                    <span>Edit</span>
                  </button>
                  <button
                    onClick={handleDelete}
                    className="inline-flex items-center gap-2 px-4 py-2.5 bg-rose-500/10 hover:bg-rose-500 active:bg-rose-700 border border-rose-500/20 hover:border-transparent text-rose-400 hover:text-white font-semibold rounded-xl text-sm transition-all duration-200"
                  >
                    <Trash2 className="w-4 h-4" />
                    <span>Delete</span>
                  </button>
                </div>
              </div>
            </div>
          </div>

          {/* Edit Modal */}
          <TaskForm
            isOpen={isFormOpen}
            onClose={() => setIsFormOpen(false)}
            taskToEdit={task}
          />
        </>
      )}
    </div>
  );
};
