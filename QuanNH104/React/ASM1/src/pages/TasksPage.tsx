import React, { useState } from 'react';
import { useTasks } from '../context/TaskContext';
import { TaskCard } from '../components/tasks/TaskCard';
import { TaskForm } from '../components/tasks/TaskForm';
import type { Task } from '../types';
import { Plus, Search, SlidersHorizontal, AlertTriangle, RefreshCw, X } from 'lucide-react';
import { Button } from '../components/shared/Button';

export const TasksPage: React.FC = () => {
  const {
    tasks,
    loading,
    error,
    fetchTasks,
    addTask,
    updateTask,
    deleteTask,
  } = useTasks();

  // Local UI states
  const [searchQuery, setSearchQuery] = useState('');
  const [statusFilter, setStatusFilter] = useState('All');
  const [priorityFilter, setPriorityFilter] = useState('All');
  const [sortBy, setSortBy] = useState('newest'); // 'newest' | 'oldest'

  // Modal States
  const [showModal, setShowModal] = useState<'create' | 'edit' | null>(null);
  const [editingTask, setEditingTask] = useState<Task | null>(null);
  const [deletingTaskId, setDeletingTaskId] = useState<string | null>(null);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [actionError, setActionError] = useState<string | null>(null);

  // Filter and sort tasks
  const filteredTasks = tasks
    .filter(task => {
      const matchesSearch =
        task.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
        task.description.toLowerCase().includes(searchQuery.toLowerCase());
      const matchesStatus = statusFilter === 'All' || task.status === statusFilter;
      const matchesPriority = priorityFilter === 'All' || task.priority === priorityFilter;
      return matchesSearch && matchesStatus && matchesPriority;
    })
    .sort((a, b) => {
      const dateA = new Date(a.createdAt).getTime();
      const dateB = new Date(b.createdAt).getTime();
      return sortBy === 'newest' ? dateB - dateA : dateA - dateB;
    });

  const handleCreateSubmit = async (values: Omit<Task, 'id' | 'createdAt'>) => {
    setIsSubmitting(true);
    setActionError(null);
    try {
      await addTask(values);
      setShowModal(null);
    } catch (err: any) {
      setActionError(err.message || 'Failed to create task.');
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleEditSubmit = async (values: Omit<Task, 'id' | 'createdAt'>) => {
    if (!editingTask) return;
    setIsSubmitting(true);
    setActionError(null);
    try {
      await updateTask(editingTask.id, values);
      setShowModal(null);
      setEditingTask(null);
    } catch (err: any) {
      setActionError(err.message || 'Failed to update task.');
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleDeleteConfirm = async () => {
    if (!deletingTaskId) return;
    setIsSubmitting(true);
    setActionError(null);
    try {
      await deleteTask(deletingTaskId);
      setDeletingTaskId(null);
    } catch (err: any) {
      setActionError(err.message || 'Failed to delete task.');
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-6">
      {/* Header section */}
      <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4 mb-8">
        <div>
          <h2 className="font-orbitron font-black text-2xl sm:text-3xl text-white uppercase tracking-wider">
            Task Management Dashboard
          </h2>
          <p className="text-sm text-gray-500 mt-1">Manage and track your daily tasks</p>
        </div>
        
        <Button
          variant="cyan"
          onClick={() => {
            setActionError(null);
            setShowModal('create');
          }}
          className="font-orbitron tracking-wider text-xs"
        >
          <Plus className="h-4 w-4 stroke-[3]" />
          <span>Create New Task</span>
        </Button>
      </div>

      {/* Filter and search controls */}
      <div className="glassmorphism rounded-2xl p-5 mb-8 border border-white/5 grid grid-cols-1 md:grid-cols-4 gap-4">
        {/* Search */}
        <div className="relative md:col-span-1">
          <Search className="absolute left-3.5 top-3.5 h-4 w-4 text-gray-500" />
          <input
            type="text"
            placeholder="Search by name, desc..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            className="w-full bg-white/5 border border-white/10 rounded-xl pl-10 pr-4 py-2.5 text-white placeholder-gray-500 focus:outline-none focus:border-cyber-cyan focus:ring-1 focus:ring-cyber-cyan/30 transition-all duration-200"
          />
        </div>

        {/* Status filter */}
        <div className="relative">
          <select
            value={statusFilter}
            onChange={(e) => setStatusFilter(e.target.value)}
            className="w-full bg-[#11141c] border border-white/10 rounded-xl px-4 py-2.5 text-white focus:outline-none focus:border-cyber-cyan focus:ring-1 focus:ring-cyber-cyan/30 transition-all duration-200"
          >
            <option value="All">All Statuses</option>
            <option value="Pending">Pending</option>
            <option value="In Progress">In Progress</option>
            <option value="Completed">Completed</option>
          </select>
        </div>

        {/* Priority filter */}
        <div className="relative">
          <select
            value={priorityFilter}
            onChange={(e) => setPriorityFilter(e.target.value)}
            className="w-full bg-[#11141c] border border-white/10 rounded-xl px-4 py-2.5 text-white focus:outline-none focus:border-cyber-cyan focus:ring-1 focus:ring-cyber-cyan/30 transition-all duration-200"
          >
            <option value="All">All Priorities</option>
            <option value="Low">Low Priority</option>
            <option value="Medium">Medium Priority</option>
            <option value="High">High Priority</option>
          </select>
        </div>

        {/* Sort */}
        <div className="relative">
          <select
            value={sortBy}
            onChange={(e) => setSortBy(e.target.value)}
            className="w-full bg-[#11141c] border border-white/10 rounded-xl px-4 py-2.5 text-white focus:outline-none focus:border-cyber-cyan focus:ring-1 focus:ring-cyber-cyan/30 transition-all duration-200"
          >
            <option value="newest">Newest First</option>
            <option value="oldest">Oldest First</option>
          </select>
        </div>
      </div>

      {/* Main content display (loading, error, list) */}
      {loading ? (
        // Loading Skeletons
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {[...Array(6)].map((_, i) => (
            <div
              key={i}
              className="glassmorphism rounded-2xl p-6 h-[200px] border border-white/5 animate-pulse flex flex-col justify-between"
            >
              <div className="space-y-3">
                <div className="flex justify-between">
                  <div className="h-4 w-16 bg-white/10 rounded" />
                  <div className="h-4 w-24 bg-white/10 rounded" />
                </div>
                <div className="h-6 w-3/4 bg-white/10 rounded mt-2" />
                <div className="h-4 w-5/6 bg-white/10 rounded" />
              </div>
              <div className="h-8 bg-white/10 rounded mt-4" />
            </div>
          ))}
        </div>
      ) : error ? (
        // Error State
        <div className="glassmorphism rounded-3xl p-8 max-w-lg mx-auto text-center border border-cyber-danger/20 shadow-lg shadow-cyber-danger/5">
          <div className="inline-flex items-center justify-center h-14 w-14 rounded-full bg-cyber-danger/10 border border-cyber-danger/20 text-cyber-danger mb-4">
            <AlertTriangle className="h-6 w-6" />
          </div>
          <h3 className="font-orbitron font-bold text-lg text-white mb-2 uppercase tracking-wide">
            Data Load Failure
          </h3>
          <p className="text-[#94a3b8] text-sm mb-6 leading-relaxed">
            {error}
          </p>
          <Button
            variant="danger"
            onClick={fetchTasks}
            className="font-orbitron tracking-wider text-xs"
          >
            <RefreshCw className="h-4 w-4" />
            <span>Try Again</span>
          </Button>
        </div>
      ) : filteredTasks.length === 0 ? (
        // Empty State
        <div className="glassmorphism rounded-3xl p-12 text-center border border-white/5">
          <p className="text-gray-500 italic text-sm">No tasks found matching your filters.</p>
          {(searchQuery || statusFilter !== 'All' || priorityFilter !== 'All') && (
            <button
              onClick={() => {
                setSearchQuery('');
                setStatusFilter('All');
                setPriorityFilter('All');
              }}
              className="text-cyber-cyan hover:underline text-xs mt-3 block mx-auto font-medium"
            >
              Clear filters
            </button>
          )}
        </div>
      ) : (
        // Task List
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {filteredTasks.map((task) => (
            <TaskCard
              key={task.id}
              task={task}
              onEdit={(t) => {
                setActionError(null);
                setEditingTask(t);
                setShowModal('edit');
              }}
              onDelete={(id) => {
                setActionError(null);
                setDeletingTaskId(id);
              }}
            />
          ))}
        </div>
      )}

      {/* Modal system (Create / Edit Modal) */}
      {showModal && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4">
          {/* Overlay */}
          <div
            className="fixed inset-0 bg-black/80 backdrop-blur-md"
            onClick={() => {
              if (!isSubmitting) {
                setShowModal(null);
                setEditingTask(null);
              }
            }}
          />
          
          {/* Modal Container */}
          <div className="relative glassmorphism rounded-2xl w-full max-w-lg p-6 overflow-hidden border border-white/10 shadow-2xl animate-float z-10">
            <button
              onClick={() => {
                setShowModal(null);
                setEditingTask(null);
              }}
              disabled={isSubmitting}
              className="absolute top-4 right-4 text-gray-400 hover:text-white"
            >
              <X className="h-5 w-5" />
            </button>

            <h3 className="font-orbitron font-bold text-lg text-white mb-6 uppercase tracking-wider">
              {showModal === 'create' ? 'Create New Task' : 'Edit Task'}
            </h3>

            {actionError && (
              <div className="bg-cyber-danger/10 border border-cyber-danger/20 text-cyber-danger text-xs font-semibold px-4 py-3 rounded-lg mb-5 flex items-center gap-2">
                <AlertTriangle className="h-4 w-4 flex-shrink-0" />
                <span>{actionError}</span>
              </div>
            )}

            <TaskForm
              initialValues={editingTask || undefined}
              onSubmit={showModal === 'create' ? handleCreateSubmit : handleEditSubmit}
              onCancel={() => {
                setShowModal(null);
                setEditingTask(null);
              }}
              submitLabel={showModal === 'create' ? 'Create Task' : 'Save Changes'}
            />
          </div>
        </div>
      )}

      {/* Confirm Delete Modal */}
      {deletingTaskId && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4">
          <div
            className="fixed inset-0 bg-black/80 backdrop-blur-md"
            onClick={() => !isSubmitting && setDeletingTaskId(null)}
          />
          
          <div className="relative glassmorphism rounded-2xl w-full max-w-md p-6 border border-white/10 shadow-2xl z-10">
            <h3 className="font-orbitron font-bold text-lg text-white mb-4 uppercase tracking-wider">
              Delete Task Confirmation
            </h3>
            
            <p className="text-sm text-[#94a3b8] mb-6 leading-relaxed">
              Are you sure you want to delete this task? This action cannot be undone and will permanently remove this task from our systems.
            </p>

            {actionError && (
              <div className="bg-cyber-danger/10 border border-cyber-danger/20 text-cyber-danger text-xs font-semibold px-4 py-3 rounded-lg mb-5">
                {actionError}
              </div>
            )}

            <div className="flex justify-end gap-3">
              <Button
                variant="secondary"
                onClick={() => setDeletingTaskId(null)}
                disabled={isSubmitting}
              >
                Cancel
              </Button>
              <Button
                variant="danger"
                onClick={handleDeleteConfirm}
                isLoading={isSubmitting}
              >
                Delete
              </Button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};
