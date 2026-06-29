import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { ArrowLeft, Edit2, Trash2, Calendar, Clock, Award, AlertTriangle, CheckSquare, RefreshCw } from 'lucide-react';
import { useTasks } from '../context/TaskContext';
import { api } from '../services/api';
import type { Task } from '../types';
import { Button } from '../components/shared/Button';
import { TaskForm } from '../components/tasks/TaskForm';

export const TaskDetailPage: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const { updateTask, deleteTask, isApiFailing } = useTasks();

  const [task, setTask] = useState<Task | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  
  // Modal states
  const [showEditModal, setShowEditModal] = useState<boolean>(false);
  const [showDeleteModal, setShowDeleteModal] = useState<boolean>(false);
  const [isSubmitting, setIsSubmitting] = useState<boolean>(false);
  const [actionError, setActionError] = useState<string | null>(null);

  const fetchTaskDetails = async () => {
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

  useEffect(() => {
    fetchTaskDetails();
  }, [id, isApiFailing]); // Reload task if API failure toggle changes or id changes

  const handleEditSubmit = async (values: Omit<Task, 'id' | 'createdAt'>) => {
    if (!id || !task) return;
    setIsSubmitting(true);
    setActionError(null);
    try {
      const updated = await updateTask(id, values);
      setTask(updated);
      setShowEditModal(false);
    } catch (err: any) {
      setActionError(err.message || 'Failed to update task.');
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleDeleteConfirm = async () => {
    if (!id) return;
    setIsSubmitting(true);
    setActionError(null);
    try {
      await deleteTask(id);
      setShowDeleteModal(false);
      navigate('/tasks'); // Redirect to task list after deleting
    } catch (err: any) {
      setActionError(err.message || 'Failed to delete task.');
    } finally {
      setIsSubmitting(false);
    }
  };

  const getStatusStyle = (status: Task['status']) => {
    switch (status) {
      case 'Completed':
        return 'bg-cyber-success/10 text-cyber-success border-cyber-success/20';
      case 'In Progress':
        return 'bg-cyber-cyan/10 text-cyber-cyan border-cyber-cyan/20';
      default:
        return 'bg-cyber-warning/10 text-cyber-warning border-cyber-warning/20';
    }
  };

  const getPriorityStyle = (priority: Task['priority']) => {
    switch (priority) {
      case 'High':
        return 'bg-cyber-danger/10 text-cyber-danger border-cyber-danger/20';
      case 'Medium':
        return 'bg-cyber-purple/10 text-cyber-purple border-cyber-purple/20';
      default:
        return 'bg-gray-400/10 text-gray-400 border-gray-400/20';
    }
  };

  const formatDate = (dateStr: string) => {
    const d = new Date(dateStr);
    return d.toLocaleString('en-US', {
      month: 'long',
      day: 'numeric',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
    });
  };

  return (
    <div className="max-w-4xl mx-auto px-4 py-6">
      {/* Back Button */}
      <button
        onClick={() => navigate('/tasks')}
        className="inline-flex items-center gap-2 text-sm text-[#94a3b8] hover:text-white mb-8 transition-colors duration-200"
      >
        <ArrowLeft className="h-4 w-4" />
        <span>Back to Tasks</span>
      </button>

      {loading ? (
        // Loading skeleton
        <div className="glassmorphism rounded-3xl p-8 border border-white/5 animate-pulse space-y-6">
          <div className="flex gap-4">
            <div className="h-6 w-20 bg-white/10 rounded" />
            <div className="h-6 w-24 bg-white/10 rounded" />
          </div>
          <div className="h-10 w-2/3 bg-white/10 rounded" />
          <div className="h-28 w-full bg-white/10 rounded" />
          <div className="h-10 w-1/3 bg-white/10 rounded" />
        </div>
      ) : error ? (
        // Error state
        <div className="glassmorphism rounded-3xl p-8 text-center border border-cyber-danger/20">
          <div className="inline-flex items-center justify-center h-14 w-14 rounded-full bg-cyber-danger/10 border border-cyber-danger/20 text-cyber-danger mb-4">
            <AlertTriangle className="h-6 w-6" />
          </div>
          <h3 className="font-orbitron font-bold text-lg text-white mb-2 uppercase tracking-wide">
            Task Load Failed
          </h3>
          <p className="text-[#94a3b8] text-sm mb-6">
            {error}
          </p>
          <div className="flex justify-center gap-3">
            <Button variant="secondary" onClick={() => navigate('/tasks')}>
              Back to List
            </Button>
            <Button variant="danger" onClick={fetchTaskDetails}>
              <RefreshCw className="h-4 w-4" />
              <span>Retry</span>
            </Button>
          </div>
        </div>
      ) : task ? (
        // Task detail content
        <div className="glassmorphism rounded-3xl p-6 sm:p-8 border border-white/5 relative overflow-hidden shadow-2xl">
          {task.priority === 'High' && (
            <div className="absolute top-0 left-0 w-full h-[3px] bg-gradient-to-r from-cyber-danger via-cyber-pink to-cyber-purple" />
          )}

          {/* Badges and actions bar */}
          <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4 mb-6 pb-6 border-b border-white/5">
            <div className="flex flex-wrap gap-2">
              <span className={`text-xs font-bold uppercase tracking-wider px-3 py-1 rounded-full border ${getStatusStyle(task.status)}`}>
                {task.status}
              </span>
              <span className={`text-xs font-bold uppercase tracking-wider px-3 py-1 rounded-full border ${getPriorityStyle(task.priority)}`}>
                {task.priority} Priority
              </span>
            </div>

            <div className="flex items-center gap-2">
              <Button
                variant="secondary"
                size="sm"
                onClick={() => {
                  setActionError(null);
                  setShowEditModal(true);
                }}
              >
                <Edit2 className="h-4 w-4" />
                <span>Edit Task</span>
              </Button>
              <Button
                variant="danger"
                size="sm"
                onClick={() => {
                  setActionError(null);
                  setShowDeleteModal(true);
                }}
              >
                <Trash2 className="h-4 w-4" />
                <span>Delete</span>
              </Button>
            </div>
          </div>

          {/* Title */}
          <h2 className="font-orbitron font-black text-2xl sm:text-3xl text-white mb-6 leading-tight tracking-wide">
            {task.name}
          </h2>

          {/* Description */}
          <div className="bg-black/20 rounded-xl p-5 border border-white/5 mb-8">
            <h4 className="text-xs font-bold text-gray-500 uppercase tracking-wider mb-2">Description</h4>
            <p className="text-[#cbd5e1] text-sm leading-relaxed whitespace-pre-wrap">
              {task.description || <span className="italic text-gray-600">No description was provided for this task.</span>}
            </p>
          </div>

          {/* Details list */}
          <div className="grid grid-cols-1 sm:grid-cols-2 gap-4 text-sm bg-white/[0.01] rounded-xl p-5 border border-white/5">
            <div className="flex items-center gap-3">
              <div className="h-8 w-8 rounded-lg bg-cyber-purple/10 flex items-center justify-center text-cyber-purple">
                <Calendar className="h-4.5 w-4.5" />
              </div>
              <div>
                <p className="text-[10px] text-gray-500 uppercase tracking-wider font-bold">Created On</p>
                <p className="text-white font-medium mt-0.5">{formatDate(task.createdAt)}</p>
              </div>
            </div>

            <div className="flex items-center gap-3">
              <div className="h-8 w-8 rounded-lg bg-cyber-cyan/10 flex items-center justify-center text-cyber-cyan">
                <Clock className="h-4.5 w-4.5" />
              </div>
              <div>
                <p className="text-[10px] text-gray-500 uppercase tracking-wider font-bold">Task ID</p>
                <p className="text-white font-mono text-xs mt-0.5">{task.id}</p>
              </div>
            </div>
          </div>
        </div>
      ) : null}

      {/* Edit Task Modal */}
      {showEditModal && task && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4">
          <div className="fixed inset-0 bg-black/80 backdrop-blur-md" onClick={() => !isSubmitting && setShowEditModal(false)} />
          <div className="relative glassmorphism rounded-2xl w-full max-w-lg p-6 border border-white/10 shadow-2xl z-10">
            <h3 className="font-orbitron font-bold text-lg text-white mb-6 uppercase tracking-wider">
              Edit Task Details
            </h3>

            {actionError && (
              <div className="bg-cyber-danger/10 border border-cyber-danger/20 text-cyber-danger text-xs font-semibold px-4 py-3 rounded-lg mb-5">
                {actionError}
              </div>
            )}

            <TaskForm
              initialValues={task}
              onSubmit={handleEditSubmit}
              onCancel={() => setShowEditModal(false)}
              submitLabel="Save Details"
            />
          </div>
        </div>
      )}

      {/* Delete Task Modal */}
      {showDeleteModal && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4">
          <div className="fixed inset-0 bg-black/80 backdrop-blur-md" onClick={() => !isSubmitting && setShowDeleteModal(false)} />
          <div className="relative glassmorphism rounded-2xl w-full max-w-md p-6 border border-white/10 shadow-2xl z-10">
            <h3 className="font-orbitron font-bold text-lg text-white mb-4 uppercase tracking-wider">
              Delete Task
            </h3>
            
            <p className="text-sm text-[#94a3b8] mb-6">
              Are you sure you want to delete this task? This operation cannot be reversed.
            </p>

            {actionError && (
              <div className="bg-cyber-danger/10 border border-cyber-danger/20 text-cyber-danger text-xs font-semibold px-4 py-3 rounded-lg mb-5">
                {actionError}
              </div>
            )}

            <div className="flex justify-end gap-3">
              <Button
                variant="secondary"
                onClick={() => setShowDeleteModal(false)}
                disabled={isSubmitting}
              >
                Cancel
              </Button>
              <Button
                variant="danger"
                onClick={handleDeleteConfirm}
                isLoading={isSubmitting}
              >
                Confirm Delete
              </Button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};
