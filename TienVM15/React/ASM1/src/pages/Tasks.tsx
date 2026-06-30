import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import { useTasks } from '../context/TaskContext';
import type { Task } from '../types/task';
import { TaskFormModal } from '../components/TaskFormModal';
import { Plus, Search, Filter, CheckCircle, Clock, Eye, Edit2, Trash2, Loader2, AlertCircle } from 'lucide-react';

export const Tasks: React.FC = () => {
  const { tasks, loading, error, dispatch } = useTasks();

  const [searchTerm, setSearchTerm] = useState('');
  const [statusFilter, setStatusFilter] = useState<'All' | 'Pending' | 'Completed'>('All');
  
  // Modal state
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingTask, setEditingTask] = useState<Task | null>(null);

  const handleOpenCreateModal = () => {
    setEditingTask(null);
    setIsModalOpen(true);
  };

  const handleOpenEditModal = (task: Task) => {
    setEditingTask(task);
    setIsModalOpen(true);
  };

  const handleFormSubmit = async (values: { name: string; description: string; status: 'Pending' | 'Completed' }) => {
    await new Promise((resolve) => setTimeout(resolve, 600));

    if (editingTask) {
      const updated: Task = {
        ...editingTask,
        name: values.name,
        description: values.description,
        status: values.status,
      };
      dispatch({ type: 'UPDATE_TASK', payload: updated });
    } else {
      const newTask: Task = {
        id: Math.random().toString(36).substr(2, 9),
        name: values.name,
        description: values.description,
        status: values.status,
        createdAt: new Date().toISOString(),
      };
      dispatch({ type: 'ADD_TASK', payload: newTask });
    }
  };

  const handleDeleteTask = async (id: string) => {
    if (window.confirm('Are you sure you want to delete this task?')) {
      await new Promise((resolve) => setTimeout(resolve, 400));
      dispatch({ type: 'DELETE_TASK', payload: id });
    }
  };

  const handleToggleStatus = (task: Task) => {
    const updated: Task = {
      ...task,
      status: task.status === 'Completed' ? 'Pending' : 'Completed',
    };
    dispatch({ type: 'UPDATE_TASK', payload: updated });
  };

  // Filter tasks
  const filteredTasks = tasks.filter((task) => {
    const matchesSearch = task.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
                          task.description.toLowerCase().includes(searchTerm.toLowerCase());
    
    const matchesStatus = statusFilter === 'All' || task.status === statusFilter;

    return matchesSearch && matchesStatus;
  });

  return (
    <div className="space-y-6">
      {/* Top Header Actions */}
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h1 className="text-2xl sm:text-3xl font-extrabold text-white tracking-tight">Project Tasks</h1>
          <p className="text-slate-400 text-sm">Create, filter, and track statuses of all tasks.</p>
        </div>
        <button
          onClick={handleOpenCreateModal}
          className="inline-flex items-center justify-center gap-2 bg-gradient-to-r from-violet-600 to-indigo-600 hover:from-violet-500 hover:to-indigo-500 text-white font-semibold text-sm px-4.5 py-2.5 rounded-xl shadow-lg shadow-violet-500/20 transition-all transform hover:-translate-y-0.5"
        >
          <Plus className="w-4 h-4" />
          Add Task
        </button>
      </div>

      {/* Filter and Search Bar */}
      <div className="flex flex-col md:flex-row gap-4 bg-slate-900/40 p-4 border border-slate-800 rounded-xl">
        {/* Search */}
        <div className="flex-1 relative">
          <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-slate-500" />
          <input
            type="text"
            placeholder="Search tasks by name or description..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="w-full bg-slate-950 border border-slate-800 rounded-xl pl-10 pr-4 py-2.5 text-sm text-slate-200 placeholder-slate-500 focus:outline-none focus:border-violet-500/50 focus:ring-2 focus:ring-violet-500/25 transition-all"
          />
        </div>

        {/* Status Filters */}
        <div className="flex items-center gap-2 self-start md:self-auto">
          <Filter className="w-4 h-4 text-slate-500 hidden sm:inline" />
          <div className="flex bg-slate-950 p-1 rounded-lg border border-slate-800">
            {(['All', 'Pending', 'Completed'] as const).map((filter) => (
              <button
                key={filter}
                onClick={() => setStatusFilter(filter)}
                className={`px-3 py-1.5 rounded-md font-semibold text-xs transition-all ${
                  statusFilter === filter
                    ? 'bg-slate-800 text-white shadow-inner'
                    : 'text-slate-400 hover:text-white'
                }`}
              >
                {filter}
              </button>
            ))}
          </div>
        </div>
      </div>

      {/* Loading and Error states */}
      {loading ? (
        <div className="min-h-[300px] flex flex-col items-center justify-center gap-3">
          <Loader2 className="w-8 h-8 text-violet-500 animate-spin" />
          <p className="text-slate-400 text-sm">Fetching tasks from REST API...</p>
        </div>
      ) : error ? (
        <div className="bg-rose-500/10 border border-rose-500/20 rounded-xl p-6 text-center max-w-lg mx-auto space-y-3">
          <AlertCircle className="w-10 h-10 text-rose-400 mx-auto" />
          <h3 className="font-bold text-white text-lg">Failed to load tasks</h3>
          <p className="text-slate-400 text-sm">{error}</p>
        </div>
      ) : filteredTasks.length === 0 ? (
        <div className="bg-slate-900/20 border border-slate-800 border-dashed rounded-2xl p-12 text-center max-w-md mx-auto space-y-3">
          <div className="w-12 h-12 rounded-full bg-slate-800 flex items-center justify-center mx-auto text-slate-400">
            <Search className="w-6 h-6" />
          </div>
          <h3 className="font-bold text-white text-base">No tasks found</h3>
          <p className="text-slate-500 text-xs">Try adjusting your filters or create a new task to get started.</p>
        </div>
      ) : (
        /* Task Cards Grid */
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          {filteredTasks.map((task) => (
            <div
              key={task.id}
              className="bg-slate-900/60 border border-slate-800 rounded-2xl p-5 hover:border-slate-700/80 transition-all flex flex-col justify-between group shadow-lg"
            >
              <div className="space-y-3">
                {/* Header status badge & actions */}
                <div className="flex items-center justify-between">
                  <button
                    onClick={() => handleToggleStatus(task)}
                    className={`inline-flex items-center gap-1.5 px-3 py-1 rounded-full text-xs font-semibold border transition-all ${
                      task.status === 'Completed'
                        ? 'bg-emerald-500/10 border-emerald-500/20 text-emerald-400 hover:bg-emerald-500/20'
                        : 'bg-amber-500/10 border-amber-500/20 text-amber-400 hover:bg-amber-500/20'
                    }`}
                  >
                    {task.status === 'Completed' ? (
                      <>
                        <CheckCircle className="w-3.5 h-3.5" />
                        Completed
                      </>
                    ) : (
                      <>
                        <Clock className="w-3.5 h-3.5" />
                        Pending
                      </>
                    )}
                  </button>

                  <div className="opacity-0 group-hover:opacity-100 transition-opacity flex items-center gap-1">
                    <Link
                      to={`/tasks/${task.id}`}
                      title="View Details"
                      className="p-1.5 rounded-lg text-slate-400 hover:text-white hover:bg-slate-800 transition-colors"
                    >
                      <Eye className="w-4 h-4" />
                    </Link>
                    <button
                      onClick={() => handleOpenEditModal(task)}
                      title="Edit Task"
                      className="p-1.5 rounded-lg text-slate-400 hover:text-white hover:bg-slate-800 transition-colors"
                    >
                      <Edit2 className="w-4 h-4" />
                    </button>
                    <button
                      onClick={() => handleDeleteTask(task.id)}
                      title="Delete Task"
                      className="p-1.5 rounded-lg text-slate-400 hover:text-rose-400 hover:bg-slate-800 transition-colors"
                    >
                      <Trash2 className="w-4 h-4" />
                    </button>
                  </div>
                </div>

                {/* Task Title & description */}
                <div className="space-y-1">
                  <h3 className="font-bold text-white group-hover:text-violet-400 transition-colors">
                    {task.name}
                  </h3>
                  <p className="text-slate-400 text-xs line-clamp-2">
                    {task.description || 'No description provided.'}
                  </p>
                </div>
              </div>

              {/* Card Footer date */}
              <div className="pt-4 mt-4 border-t border-slate-850 flex items-center justify-between text-[11px] text-slate-500">
                <span>Created {new Date(task.createdAt).toLocaleDateString()}</span>
                <Link
                  to={`/tasks/${task.id}`}
                  className="text-violet-400 hover:underline font-semibold flex items-center gap-0.5"
                >
                  Details &rarr;
                </Link>
              </div>
            </div>
          ))}
        </div>
      )}

      {/* Task Creation & Modification modal */}
      <TaskFormModal
        isOpen={isModalOpen}
        task={editingTask}
        onClose={() => setIsModalOpen(false)}
        onSubmit={handleFormSubmit}
      />
    </div>
  );
};
