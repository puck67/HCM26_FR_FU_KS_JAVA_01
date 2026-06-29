import React, { useState } from 'react';
import { useTasks } from '../context/TaskContext';
import { TaskCard } from '../components/TaskCard';
import { TaskForm } from '../components/TaskForm';
import { TaskCardSkeleton } from '../components/SkeletonLoader';
import { Alert } from '../components/Alert';
import type { Task } from '../types';
import { Search, Plus, Filter, AlertCircle, X, Trash2 } from 'lucide-react';

export const Tasks: React.FC = () => {
  const { state, addTask, updateTask, deleteTask, reloadTasks } = useTasks();
  const { tasks, loading, error } = state;

  // UI state controls
  const [searchQuery, setSearchQuery] = useState('');
  const [statusFilter, setStatusFilter] = useState<'all' | 'todo' | 'in_progress' | 'completed'>('all');
  const [showCreateModal, setShowCreateModal] = useState(false);
  const [editingTask, setEditingTask] = useState<Task | null>(null);
  const [confirmDeleteId, setConfirmDeleteId] = useState<string | null>(null);

  // Filters tasks according to search input and status tabs
  const filteredTasks = tasks.filter((task) => {
    const matchesSearch =
      task.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
      (task.description && task.description.toLowerCase().includes(searchQuery.toLowerCase()));

    const matchesStatus = statusFilter === 'all' || task.status === statusFilter;

    return matchesSearch && matchesStatus;
  });

  const handleCreateSubmit = (values: { name: string; description: string }) => {
    addTask(values.name, values.description);
    setShowCreateModal(false);
  };

  const handleEditSubmit = (values: { name: string; description: string; status?: Task['status'] }) => {
    if (editingTask) {
      updateTask({
        ...editingTask,
        name: values.name,
        description: values.description,
        status: values.status || 'todo',
      });
      setEditingTask(null);
    }
  };

  const handleDeleteConfirm = () => {
    if (confirmDeleteId) {
      deleteTask(confirmDeleteId);
      setConfirmDeleteId(null);
    }
  };

  return (
    <div className="relative min-h-[calc(100vh-73px)] py-10 px-4 md:px-8 max-w-7xl mx-auto">
      {/* Decorative Blur Backgrounds */}
      <div className="absolute top-10 right-10 w-80 h-80 bg-violet-600/5 rounded-full blur-3xl -z-10" />
      <div className="absolute bottom-10 left-10 w-80 h-80 bg-indigo-600/5 rounded-full blur-3xl -z-10" />

      {/* Page Header */}
      <div className="flex flex-col md:flex-row md:items-center justify-between gap-6 mb-10">
        <div>
          <h1 className="text-3xl font-extrabold text-white tracking-tight m-0">Task Dashboard</h1>
          <p className="text-sm text-gray-400 mt-1">
            Manage your daily milestones, project tasks, and status reports.
          </p>
        </div>
        <button
          onClick={() => setShowCreateModal(true)}
          className="glass-button self-start md:self-auto px-5 py-3 rounded-xl text-sm font-semibold text-white flex items-center gap-2 transition-all cursor-pointer"
        >
          <Plus size={18} />
          <span>Create Task</span>
        </button>
      </div>

      {/* Filters and Search Bar */}
      <div className="glass-card p-4 rounded-2xl border border-white/5 flex flex-col lg:flex-row items-center gap-4 mb-8">
        {/* Search */}
        <div className="relative w-full lg:flex-1">
          <Search className="absolute left-4 top-1/2 -translate-y-1/2 text-gray-500" size={18} />
          <input
            type="text"
            placeholder="Search tasks by name or description..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            className="glass-input w-full pl-12 pr-4 py-3 rounded-xl text-sm placeholder-gray-500"
          />
        </div>

        {/* Status Filters */}
        <div className="flex flex-wrap items-center gap-2 w-full lg:w-auto">
          <div className="flex items-center gap-1.5 text-xs font-semibold text-gray-500 uppercase tracking-wider px-2">
            <Filter size={14} />
            <span>Filter</span>
          </div>
          {(['all', 'todo', 'in_progress', 'completed'] as const).map((filter) => (
            <button
              key={filter}
              onClick={() => setStatusFilter(filter)}
              className={`px-4 py-2 rounded-xl text-xs font-semibold transition-all cursor-pointer border ${
                statusFilter === filter
                  ? 'text-violet-400 bg-violet-500/10 border-violet-500/20'
                  : 'text-gray-400 border-transparent hover:bg-white/5 hover:text-gray-200'
              }`}
            >
              {filter === 'all'
                ? 'All Statuses'
                : filter === 'todo'
                ? 'To Do'
                : filter === 'in_progress'
                ? 'In Progress'
                : 'Completed'}
            </button>
          ))}
        </div>
      </div>

      {/* Main Content Area (Loading, Error, and Task List) */}
      {loading ? (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {[1, 2, 3, 4, 5, 6].map((i) => (
            <TaskCardSkeleton key={i} />
          ))}
        </div>
      ) : error ? (
        <div className="py-12">
          <Alert message={error} onRetry={reloadTasks} />
        </div>
      ) : filteredTasks.length === 0 ? (
        <div className="glass-card py-20 px-6 rounded-3xl border border-white/5 flex flex-col items-center justify-center text-center max-w-xl mx-auto">
          <div className="p-4 bg-white/5 text-gray-400 rounded-2xl border border-white/5 mb-4">
            <AlertCircle size={32} />
          </div>
          <h3 className="text-xl font-bold text-white mb-2">No Tasks Found</h3>
          <p className="text-sm text-gray-400 max-w-sm mb-6">
            We couldn't find any tasks matching your criteria. Try adjusting your search keywords or create a new task.
          </p>
          <button
            onClick={() => setShowCreateModal(true)}
            className="glass-button px-5 py-2.5 rounded-xl text-xs font-semibold text-white flex items-center gap-2 cursor-pointer"
          >
            <Plus size={16} />
            <span>Create Task</span>
          </button>
        </div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {filteredTasks.map((task) => (
            <TaskCard
              key={task.id}
              task={task}
              onEdit={setEditingTask}
              onDelete={setConfirmDeleteId}
            />
          ))}
        </div>
      )}

      {/* CREATE MODAL */}
      {showCreateModal && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4">
          {/* Backdrop */}
          <div
            className="fixed inset-0 bg-black/60 backdrop-blur-sm"
            onClick={() => setShowCreateModal(false)}
          />
          {/* Modal Content */}
          <div className="glass-card w-full max-w-lg rounded-3xl border border-white/10 relative z-10 overflow-hidden shadow-2xl p-6 md:p-8 animate-in fade-in zoom-in duration-300">
            <div className="flex items-center justify-between mb-6 pb-4 border-b border-white/5">
              <h2 className="text-xl font-bold text-white m-0">Create New Task</h2>
              <button
                onClick={() => setShowCreateModal(false)}
                className="p-1 text-gray-400 hover:text-white rounded-lg hover:bg-white/5 transition-colors cursor-pointer"
              >
                <X size={20} />
              </button>
            </div>
            <TaskForm
              submitText="Create Task"
              onCancel={() => setShowCreateModal(false)}
              onSubmit={handleCreateSubmit}
            />
          </div>
        </div>
      )}

      {/* EDIT MODAL */}
      {editingTask && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4">
          {/* Backdrop */}
          <div
            className="fixed inset-0 bg-black/60 backdrop-blur-sm"
            onClick={() => setEditingTask(null)}
          />
          {/* Modal Content */}
          <div className="glass-card w-full max-w-lg rounded-3xl border border-white/10 relative z-10 overflow-hidden shadow-2xl p-6 md:p-8 animate-in fade-in zoom-in duration-300">
            <div className="flex items-center justify-between mb-6 pb-4 border-b border-white/5">
              <h2 className="text-xl font-bold text-white m-0">Edit Task</h2>
              <button
                onClick={() => setEditingTask(null)}
                className="p-1 text-gray-400 hover:text-white rounded-lg hover:bg-white/5 transition-colors cursor-pointer"
              >
                <X size={20} />
              </button>
            </div>
            <TaskForm
              isEdit={true}
              initialValues={{
                name: editingTask.name,
                description: editingTask.description,
                status: editingTask.status,
              }}
              submitText="Save Changes"
              onCancel={() => setEditingTask(null)}
              onSubmit={handleEditSubmit}
            />
          </div>
        </div>
      )}

      {/* CONFIRM DELETE MODAL */}
      {confirmDeleteId && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4">
          {/* Backdrop */}
          <div
            className="fixed inset-0 bg-black/60 backdrop-blur-sm"
            onClick={() => setConfirmDeleteId(null)}
          />
          {/* Modal Content */}
          <div className="glass-card w-full max-w-md rounded-2xl border border-rose-500/20 bg-slate-950/90 relative z-10 overflow-hidden shadow-2xl p-6 text-center animate-in fade-in zoom-in duration-300">
            <div className="mx-auto w-12 h-12 bg-rose-500/10 text-rose-400 rounded-xl border border-rose-500/20 flex items-center justify-center mb-4">
              <Trash2 size={24} />
            </div>
            <h3 className="text-lg font-bold text-white mb-2">Delete Task?</h3>
            <p className="text-sm text-gray-400 mb-6 leading-relaxed">
              Are you sure you want to delete this task? This action cannot be undone.
            </p>
            <div className="flex items-center justify-center gap-3">
              <button
                onClick={() => setConfirmDeleteId(null)}
                className="px-4 py-2 rounded-xl text-sm font-medium text-gray-400 hover:text-white hover:bg-white/5 border border-transparent transition-colors cursor-pointer"
              >
                Cancel
              </button>
              <button
                onClick={handleDeleteConfirm}
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
