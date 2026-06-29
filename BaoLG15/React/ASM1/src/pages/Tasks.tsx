import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { useTasks } from '../context/TaskContext';
import { TaskForm } from '../components/TaskForm';
import type { Task } from '../services/api';
import { 
  Search, 
  Plus, 
  Trash2, 
  Edit3, 
  CheckCircle, 
  Circle, 
  AlertCircle, 
  RotateCcw, 
  Eye, 
  Calendar,
  Sparkles
} from 'lucide-react';

export const Tasks: React.FC = () => {
  const { state, fetchTasks, deleteTask, toggleTaskCompletion } = useTasks();
  const { tasks, loading, error } = state;

  // UI state
  const [searchTerm, setSearchTerm] = useState('');
  const [statusFilter, setStatusFilter] = useState<'all' | 'completed' | 'pending'>('all');
  const [isFormOpen, setIsFormOpen] = useState(false);
  const [taskToEdit, setTaskToEdit] = useState<Task | null>(null);

  // Run on mount to make sure data is fresh
  useEffect(() => {
    fetchTasks();
  }, [fetchTasks]);

  // Handle task delete
  const handleDelete = async (id: string, e: React.MouseEvent) => {
    e.preventDefault();
    e.stopPropagation();
    if (window.confirm('Are you sure you want to delete this task?')) {
      try {
        await deleteTask(id);
      } catch (err: any) {
        alert(err.message || 'Failed to delete task');
      }
    }
  };

  // Handle task complete toggle
  const handleToggle = async (task: Task, e: React.MouseEvent) => {
    e.preventDefault();
    e.stopPropagation();
    try {
      await toggleTaskCompletion(task.id, !task.completed);
    } catch (err: any) {
      alert(err.message || 'Failed to update task status');
    }
  };

  // Open edit modal
  const handleEdit = (task: Task, e: React.MouseEvent) => {
    e.preventDefault();
    e.stopPropagation();
    setTaskToEdit(task);
    setIsFormOpen(true);
  };

  // Open create modal
  const handleCreateOpen = () => {
    setTaskToEdit(null);
    setIsFormOpen(true);
  };

  // Filter tasks based on filters and search queries
  const filteredTasks = tasks.filter((task) => {
    const matchesSearch = task.name.toLowerCase().includes(searchTerm.toLowerCase()) || 
                          task.description.toLowerCase().includes(searchTerm.toLowerCase());
    
    if (statusFilter === 'completed') {
      return matchesSearch && task.completed;
    }
    if (statusFilter === 'pending') {
      return matchesSearch && !task.completed;
    }
    return matchesSearch;
  });

  const formatDate = (isoString: string) => {
    const date = new Date(isoString);
    return date.toLocaleDateString('en-US', {
      month: 'short',
      day: 'numeric',
      year: 'numeric',
    });
  };

  return (
    <div className="container mx-auto px-6 py-8 max-w-5xl">
      {/* Header section */}
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4 mb-8">
        <div>
          <h1 className="text-3xl font-extrabold text-white tracking-tight flex items-center gap-2 m-0">
            <Sparkles className="w-6 h-6 text-indigo-400" />
            <span>Workspace Tasks</span>
          </h1>
          <p className="text-slate-400 text-sm mt-1">
            Manage your assignments, track completion, and edit tasks.
          </p>
        </div>
        <button
          onClick={handleCreateOpen}
          className="inline-flex items-center justify-center gap-2 px-5 py-3 bg-indigo-600 hover:bg-indigo-500 active:bg-indigo-700 text-white font-semibold rounded-xl shadow-lg shadow-indigo-600/30 transition-all duration-200"
        >
          <Plus className="w-5 h-5" />
          <span>Add New Task</span>
        </button>
      </div>

      {/* Search and Filters */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-6">
        {/* Search */}
        <div className="relative md:col-span-2">
          <Search className="absolute left-4 top-3.5 w-5 h-5 text-slate-500" />
          <input
            type="text"
            placeholder="Search by name or description..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="w-full pl-12 pr-4 py-3 bg-slate-900/40 border border-white/10 rounded-xl text-white placeholder-slate-500 focus:outline-none focus:ring-2 focus:ring-indigo-500/20 focus:border-indigo-500/50 transition-all text-sm"
          />
        </div>

        {/* Filters */}
        <div className="flex bg-slate-900/40 border border-white/10 p-1 rounded-xl">
          <button
            onClick={() => setStatusFilter('all')}
            className={`flex-1 py-2 text-xs font-semibold rounded-lg transition-all ${
              statusFilter === 'all'
                ? 'bg-indigo-600 text-white shadow-md'
                : 'text-slate-400 hover:text-white'
            }`}
          >
            All
          </button>
          <button
            onClick={() => setStatusFilter('completed')}
            className={`flex-1 py-2 text-xs font-semibold rounded-lg transition-all ${
              statusFilter === 'completed'
                ? 'bg-indigo-600 text-white shadow-md'
                : 'text-slate-400 hover:text-white'
            }`}
          >
            Completed
          </button>
          <button
            onClick={() => setStatusFilter('pending')}
            className={`flex-1 py-2 text-xs font-semibold rounded-lg transition-all ${
              statusFilter === 'pending'
                ? 'bg-indigo-600 text-white shadow-md'
                : 'text-slate-400 hover:text-white'
            }`}
          >
            Pending
          </button>
        </div>
      </div>

      {/* Error state */}
      {error && (
        <div className="glass-card p-6 rounded-2xl border-rose-500/20 bg-rose-950/10 text-center mb-8">
          <AlertCircle className="w-10 h-10 text-rose-500 mx-auto mb-3" />
          <h3 className="text-lg font-bold text-white mb-1">Network Error</h3>
          <p className="text-slate-400 text-sm max-w-md mx-auto mb-4">{error}</p>
          <button
            onClick={fetchTasks}
            className="inline-flex items-center gap-2 px-4 py-2 bg-rose-500/20 hover:bg-rose-500/35 border border-rose-500/30 text-rose-300 font-semibold rounded-xl text-sm transition-all"
          >
            <RotateCcw className="w-4 h-4" />
            <span>Retry Connection</span>
          </button>
        </div>
      )}

      {/* Loading state (initial fetch skeletons) */}
      {loading && tasks.length === 0 ? (
        <div className="space-y-4">
          {[1, 2, 3].map((i) => (
            <div key={i} className="glass-card p-5 rounded-2xl animate-pulse flex items-start gap-4">
              <div className="w-6 h-6 rounded-full bg-white/10 mt-1"></div>
              <div className="flex-1 space-y-2">
                <div className="h-4 bg-white/10 w-1/4 rounded"></div>
                <div className="h-3 bg-white/10 w-2/3 rounded"></div>
              </div>
            </div>
          ))}
        </div>
      ) : (
        <>
          {/* Empty State */}
          {filteredTasks.length === 0 ? (
            <div className="glass-card py-16 text-center rounded-2xl">
              <div className="w-16 h-16 bg-white/5 rounded-2xl flex items-center justify-center mx-auto mb-4 border border-white/5">
                <Search className="w-8 h-8 text-slate-500" />
              </div>
              <h3 className="text-lg font-bold text-white mb-1">No tasks found</h3>
              <p className="text-slate-400 text-sm max-w-xs mx-auto">
                {searchTerm || statusFilter !== 'all'
                  ? "Try refining your search terms or adjustments to filters."
                  : "Get started by adding your first project assignment."}
              </p>
              {!searchTerm && statusFilter === 'all' && (
                <button
                  onClick={handleCreateOpen}
                  className="mt-4 px-4 py-2 bg-indigo-600 hover:bg-indigo-500 text-white font-semibold rounded-xl text-sm transition-all"
                >
                  Create Task
                </button>
              )}
            </div>
          ) : (
            /* Task List Grid */
            <div className="space-y-4">
              {filteredTasks.map((task) => (
                <div
                  key={task.id}
                  className={`glass-card p-5 rounded-2xl flex flex-col md:flex-row md:items-center justify-between gap-4 border-l-4 transition-all ${
                    task.completed 
                      ? 'border-l-emerald-500/80 bg-slate-900/20' 
                      : 'border-l-indigo-500/80'
                  }`}
                >
                  <div className="flex items-start gap-4 flex-1">
                    {/* Completion Toggle checkbox */}
                    <button
                      onClick={(e) => handleToggle(task, e)}
                      className={`mt-1 flex-shrink-0 transition-colors ${
                        task.completed ? 'text-emerald-500' : 'text-slate-500 hover:text-slate-300'
                      }`}
                    >
                      {task.completed ? (
                        <CheckCircle className="w-6 h-6 fill-emerald-500/10" />
                      ) : (
                        <Circle className="w-6 h-6" />
                      )}
                    </button>

                    <div className="space-y-1">
                      <div className="flex flex-wrap items-center gap-2">
                        <Link 
                          to={`/tasks/${task.id}`}
                          className={`font-bold text-base transition-colors hover:text-indigo-400 ${
                            task.completed ? 'text-slate-500 line-through' : 'text-white'
                          }`}
                        >
                          {task.name}
                        </Link>
                        {task.completed ? (
                          <span className="px-2 py-0.5 rounded-full text-[10px] font-semibold bg-emerald-500/10 text-emerald-400 border border-emerald-500/20">
                            Completed
                          </span>
                        ) : (
                          <span className="px-2 py-0.5 rounded-full text-[10px] font-semibold bg-indigo-500/10 text-indigo-400 border border-indigo-500/20">
                            Active
                          </span>
                        )}
                      </div>
                      <p className={`text-sm max-w-xl ${task.completed ? 'text-slate-500' : 'text-slate-300'}`}>
                        {task.description || <span className="italic text-slate-500">No description provided</span>}
                      </p>
                      <div className="flex items-center gap-1.5 text-xs text-slate-500 pt-1.5">
                        <Calendar className="w-3.5 h-3.5" />
                        <span>Created {formatDate(task.createdAt)}</span>
                      </div>
                    </div>
                  </div>

                  {/* Actions buttons */}
                  <div className="flex items-center gap-2 border-t md:border-t-0 border-white/5 pt-3 md:pt-0 self-end md:self-center">
                    <Link
                      to={`/tasks/${task.id}`}
                      className="p-2 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl border border-white/5 transition-all"
                      title="View Details"
                    >
                      <Eye className="w-4 h-4" />
                    </Link>
                    <button
                      onClick={(e) => handleEdit(task, e)}
                      className="p-2 text-slate-400 hover:text-indigo-400 hover:bg-indigo-500/5 rounded-xl border border-white/5 transition-all"
                      title="Edit Task"
                    >
                      <Edit3 className="w-4 h-4" />
                    </button>
                    <button
                      onClick={(e) => handleDelete(task.id, e)}
                      className="p-2 text-slate-400 hover:text-rose-400 hover:bg-rose-500/5 rounded-xl border border-white/5 transition-all"
                      title="Delete Task"
                    >
                      <Trash2 className="w-4 h-4" />
                    </button>
                  </div>
                </div>
              ))}
            </div>
          )}
        </>
      )}

      {/* Task Creation / Editing Form Modal */}
      <TaskForm
        isOpen={isFormOpen}
        onClose={() => setIsFormOpen(false)}
        taskToEdit={taskToEdit}
      />
    </div>
  );
};
