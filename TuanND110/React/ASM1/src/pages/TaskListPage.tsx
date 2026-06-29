import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { PlusCircle, Edit, Trash2, Search } from 'lucide-react';
import { useTasks } from '../context/TaskContext.tsx';
import type { Task } from '../types/task.ts';
import { Card } from '../components/generic/Card.tsx';
import { Button } from '../components/generic/Button.tsx';
import { Badge, statusToBadgeProps } from '../components/generic/Badge.tsx';
import { ListView } from '../components/generic/ListView.tsx';
import { ConfirmDialog } from '../components/generic/ConfirmDialog.tsx';
import { Toast } from '../components/generic/Toast.tsx';

// ─── Toast state type ─────────────────────────────────────────────────────────

interface ToastState {
  message: string;
  variant: 'success' | 'error' | 'info';
}

// ─── Component ────────────────────────────────────────────────────────────────

/**
 * TaskListPage — displays all tasks with search filter and status tabs.
 * Supports: Edit, Delete (with confirmation), Mark Completed / Undo.
 * Route: /tasks
 */
export const TaskListPage: React.FC = () => {
  const { tasks, loading, error, fetchTasks, deleteTask, updateTask } = useTasks();
  const navigate = useNavigate();

  // ── Search & filter ──────────────────────────────────────────────────────────
  const [searchQuery, setSearchQuery]     = useState('');
  const [statusFilter, setStatusFilter]   = useState<string>('all');

  // ── Action loading ───────────────────────────────────────────────────────────
  const [busyTaskId, setBusyTaskId]       = useState<string | null>(null);

  // ── Confirm dialog ───────────────────────────────────────────────────────────
  const [deleteTargetId, setDeleteTargetId] = useState<string | null>(null);
  const [isDeleting, setIsDeleting]         = useState(false);

  // ── Toast ────────────────────────────────────────────────────────────────────
  const [toast, setToast] = useState<ToastState | null>(null);

  useEffect(() => { fetchTasks(); }, [fetchTasks]);

  // ── Derived list ─────────────────────────────────────────────────────────────
  const filteredTasks = tasks.filter((t) => {
    const matchesSearch =
      t.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
      t.description.toLowerCase().includes(searchQuery.toLowerCase());
    const matchesStatus = statusFilter === 'all' || t.status === statusFilter;
    return matchesSearch && matchesStatus;
  });

  // ── Handlers ─────────────────────────────────────────────────────────────────
  const handleRequestDelete = (e: React.MouseEvent, id: string) => {
    e.stopPropagation();
    setDeleteTargetId(id);
  };

  const handleConfirmDelete = async () => {
    if (!deleteTargetId) return;
    setIsDeleting(true);
    try {
      await deleteTask(deleteTargetId);
      setToast({ message: 'Task deleted successfully.', variant: 'success' });
    } catch {
      setToast({ message: 'Failed to delete task.', variant: 'error' });
    } finally {
      setIsDeleting(false);
      setDeleteTargetId(null);
    }
  };

  const handleStatusChange = async (task: Task, newStatus: Task['status'], e: React.ChangeEvent<HTMLSelectElement>) => {
    e.stopPropagation();
    setBusyTaskId(task.id);
    try {
      await updateTask(task.id, { status: newStatus });
      setToast({
        message: `Task "${task.name}" status updated to "${newStatus.replace('_', ' ')}".`,
        variant: 'success',
      });
    } catch {
      setToast({ message: 'Failed to update task status.', variant: 'error' });
    } finally {
      setBusyTaskId(null);
    }
  };

  // ── Status tab labels ─────────────────────────────────────────────────────────
  const STATUS_TABS = [
    { key: 'all',         label: 'All' },
    { key: 'todo',        label: 'To Do' },
    { key: 'in_progress', label: 'In Progress' },
    { key: 'completed',   label: 'Completed' },
  ];

  return (
    <div className="space-y-6">
      {/* Page header */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h1 className="text-2xl font-extrabold text-slate-900 tracking-tight">Tasks</h1>
          <p className="text-sm text-slate-500 mt-1">
            {tasks.length} task{tasks.length !== 1 ? 's' : ''} total
          </p>
        </div>
        <Button
          variant="primary"
          leadingIcon={<PlusCircle size={16} />}
          onClick={() => navigate('/tasks/new')}
        >
          New Task
        </Button>
      </div>

      {/* Filters row */}
      <div className="flex flex-col sm:flex-row gap-3">
        {/* Search */}
        <div className="relative flex-1">
          <Search size={15} className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" />
          <input
            type="text"
            placeholder="Search tasks…"
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            className="w-full pl-9 pr-4 py-2 text-sm rounded-md border border-slate-300 bg-white text-slate-900 placeholder-slate-400 focus:outline-none focus:border-blue-500 focus:ring-1 focus:ring-blue-500 transition-all"
          />
        </div>

        {/* Status tabs */}
        <div className="flex items-center gap-1 border border-slate-200 p-1 rounded-lg bg-slate-50 overflow-x-auto">
          {STATUS_TABS.map((tab) => (
            <button
              key={tab.key}
              onClick={() => setStatusFilter(tab.key)}
              className={[
                'px-3 py-1.5 rounded-md text-xs font-semibold uppercase tracking-wider whitespace-nowrap transition-all duration-200',
                statusFilter === tab.key
                  ? 'bg-blue-600 text-white shadow-sm'
                  : 'text-slate-600 hover:text-slate-900',
              ].join(' ')}
            >
              {tab.label}
            </button>
          ))}
        </div>
      </div>

      {/* Error banner */}
      {error && (
        <p className="text-sm text-red-600 font-medium">{error}</p>
      )}

      {/* List */}
      <ListView<Task>
        items={filteredTasks}
        isLoading={loading}
        keyExtractor={(t) => t.id}
        emptyMessage="No tasks match your filter."
        renderItem={(task) => (
          <Card
            onClick={() => navigate(`/tasks/${task.id}`)}
            noPadding
            className={[
              'transition-all duration-200',
              task.status === 'completed'
                ? 'bg-emerald-50/50 border-emerald-200'
                : '',
              busyTaskId === task.id ? 'opacity-60 pointer-events-none' : '',
            ].join(' ')}
          >
            <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 p-4">
              {/* Task info */}
              <div className="flex-1 min-w-0 space-y-1">
                <div className="flex flex-wrap items-center gap-2">
                  <h3
                    className={[
                      'text-sm font-bold',
                      task.status === 'completed'
                        ? 'line-through text-slate-400'
                        : 'text-slate-900 hover:text-blue-600 transition-colors',
                    ].join(' ')}
                  >
                    {task.name}
                  </h3>
                  <Badge {...statusToBadgeProps(task.status)} />
                </div>
                {task.description && (
                  <p className="text-xs text-slate-500 line-clamp-1">
                    {task.description}
                  </p>
                )}
                <p className="text-[10px] text-slate-400">
                  {new Date(task.createdAt).toLocaleDateString('en-US', {
                    year: 'numeric', month: 'short', day: 'numeric',
                  })}
                </p>
              </div>

              {/* Action buttons */}
              <div
                className="flex items-center gap-2 shrink-0"
                onClick={(e) => e.stopPropagation()}
              >
                {/* Status Dropdown */}
                <select
                  value={task.status}
                  disabled={busyTaskId === task.id}
                  onChange={(e) => handleStatusChange(task, e.target.value as Task['status'], e)}
                  className="bg-white border border-slate-300 text-xs text-slate-700 rounded-md px-2 py-1.5 focus:outline-none focus:border-blue-500 focus:ring-1 focus:ring-blue-500 transition-all font-semibold cursor-pointer"
                >
                  <option value="todo">To Do</option>
                  <option value="in_progress">In Progress</option>
                  <option value="completed">Completed</option>
                </select>

                {/* Edit */}
                <Button
                  variant="secondary"
                  size="sm"
                  leadingIcon={<Edit size={13} />}
                  onClick={(e) => { e.stopPropagation(); navigate(`/tasks/${task.id}`); }}
                  aria-label="Edit task"
                >
                  Edit
                </Button>

                {/* Delete */}
                <Button
                  variant="danger"
                  size="sm"
                  leadingIcon={<Trash2 size={13} />}
                  onClick={(e) => handleRequestDelete(e, task.id)}
                  aria-label="Delete task"
                >
                  Delete
                </Button>
              </div>
            </div>
          </Card>
        )}
      />

      {/* Confirm delete dialog */}
      <ConfirmDialog
        isOpen={deleteTargetId !== null}
        title="Delete Task"
        message="Are you sure you want to permanently delete this task? This action cannot be undone."
        confirmLabel="Delete"
        confirmVariant="danger"
        isConfirmLoading={isDeleting}
        onConfirm={handleConfirmDelete}
        onCancel={() => setDeleteTargetId(null)}
      />

      {/* Toast notifications */}
      {toast && (
        <Toast
          message={toast.message}
          variant={toast.variant}
          onDismiss={() => setToast(null)}
        />
      )}
    </div>
  );
};
