'use client';

import { useTransition } from 'react';
import Link from 'next/link';
import { Task } from '@/types/task';
import { ROUTES } from '@/constants/routes';
import { deleteTask, toggleTaskCompleted } from '@/lib/actions';

interface TaskRowProps {
  task: Task;
}

export default function TaskRow({ task }: TaskRowProps) {
  const [isPendingDelete, startDeleteTransition] = useTransition();
  const [isPendingToggle, startToggleTransition] = useTransition();

  function handleDelete() {
    startDeleteTransition(() => {
      deleteTask(task.id);
    });
  }

  function handleToggle() {
    startToggleTransition(() => {
      toggleTaskCompleted(task.id);
    });
  }

  return (
    <div className="group flex flex-col sm:flex-row sm:items-center gap-3 p-4 bg-slate-800 hover:bg-slate-750 border border-slate-700 hover:border-slate-600 rounded-xl transition-all duration-200">
      {/* Status dot */}
      <div className="flex-shrink-0">
        <span
          className={`inline-block w-3 h-3 rounded-full ${
            task.completed ? 'bg-emerald-500' : 'bg-amber-400'
          }`}
        />
      </div>

      {/* Task info */}
      <div className="flex-1 min-w-0">
        <p
          className={`font-semibold truncate text-sm sm:text-base ${
            task.completed ? 'line-through text-gray-400' : 'text-slate-100'
          }`}
        >
          {task.name}
        </p>
        {task.description && (
          <p
            className={`text-xs sm:text-sm mt-0.5 truncate ${
              task.completed ? 'line-through text-gray-500' : 'text-slate-400'
            }`}
          >
            {task.description}
          </p>
        )}
        <p className="text-xs text-slate-600 mt-1">
          {new Date(task.createdAt).toLocaleDateString('en-US', {
            year: 'numeric',
            month: 'short',
            day: 'numeric',
          })}
        </p>
      </div>

      {/* Actions */}
      <div className="flex items-center gap-2 flex-wrap sm:flex-nowrap">
        <button
          onClick={handleToggle}
          disabled={isPendingToggle}
          aria-label={
            task.completed
              ? `Mark task as pending: ${task.name}`
              : `Mark task as complete: ${task.name}`
          }
          className={`px-3 py-1.5 text-xs font-medium rounded-lg border transition-all disabled:opacity-50 disabled:cursor-not-allowed ${
            task.completed
              ? 'border-amber-500/50 text-amber-400 hover:bg-amber-500/10'
              : 'border-emerald-500/50 text-emerald-400 hover:bg-emerald-500/10'
          }`}
        >
          {isPendingToggle
            ? 'Updating…'
            : task.completed
            ? 'Mark pending'
            : 'Mark complete'}
        </button>

        <Link
          href={ROUTES.TASK_DETAIL(task.id)}
          aria-label={`Edit task: ${task.name}`}
          className="px-3 py-1.5 text-xs font-medium rounded-lg border border-indigo-500/50 text-indigo-400 hover:bg-indigo-500/10 transition-all"
        >
          Edit
        </Link>

        <button
          onClick={handleDelete}
          disabled={isPendingDelete}
          aria-label={`Delete task: ${task.name}`}
          className="px-3 py-1.5 text-xs font-medium rounded-lg border border-red-500/50 text-red-400 hover:bg-red-500/10 transition-all disabled:opacity-50 disabled:cursor-not-allowed"
        >
          {isPendingDelete ? 'Deleting…' : 'Delete'}
        </button>
      </div>
    </div>
  );
}
