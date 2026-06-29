'use client';

import React, { useTransition } from 'react';
import Link from 'next/link';
import { Task } from '../types';
import { deleteTask, toggleTaskCompleted } from '../app/actions';
import { Calendar, CheckCircle2, Circle, Edit3, Trash2 } from 'lucide-react';

interface TaskCardProps {
  task: Task;
}

export default function TaskCard({ task }: TaskCardProps) {
  const [isPendingDelete, startTransitionDelete] = useTransition();
  const [isPendingToggle, startTransitionToggle] = useTransition();

  const handleDelete = () => {
    if (window.confirm(`Are you sure you want to delete the task "${task.name}"?`)) {
      startTransitionDelete(async () => {
        try {
          await deleteTask(task.id);
        } catch (err) {
          alert(err instanceof Error ? err.message : 'Failed to delete task');
        }
      });
    }
  };

  const handleToggle = () => {
    startTransitionToggle(async () => {
      try {
        await toggleTaskCompleted(task.id);
      } catch (err) {
        alert(err instanceof Error ? err.message : 'Failed to toggle task');
      }
    });
  };

  // Format date nicely
  const formatDate = (dateStr: string) => {
    try {
      const d = new Date(dateStr);
      return d.toLocaleDateString('en-US', {
        month: 'short',
        day: 'numeric',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
      });
    } catch {
      return dateStr;
    }
  };

  return (
    <div 
      className={`glass-panel glass-panel-hover rounded-2xl p-5 md:p-6 flex flex-col md:flex-row md:items-center justify-between gap-4 transition-all duration-300 ${
        task.completed ? 'opacity-60 bg-white/[0.01]' : ''
      }`}
    >
      <div className="flex items-start gap-4 flex-1">
        {/* Toggle Button */}
        <button
          onClick={handleToggle}
          disabled={isPendingToggle}
          className="mt-1 transition-transform active:scale-95 disabled:opacity-50 cursor-pointer"
          title={task.completed ? "Mark as Pending" : "Mark as Completed"}
        >
          {task.completed ? (
            <CheckCircle2 className="w-5.5 h-5.5 text-emerald-400 fill-emerald-400/10" />
          ) : (
            <Circle className="w-5.5 h-5.5 text-gray-500 hover:text-indigo-400" />
          )}
        </button>

        {/* Text Details */}
        <div className="space-y-1 flex-1">
          <h3 
            className={`font-semibold text-base transition-all ${
              task.completed 
                ? 'line-through text-gray-500' 
                : 'text-white hover:text-indigo-300'
            }`}
          >
            {task.name}
          </h3>
          <p className={`text-sm ${task.completed ? 'text-gray-600' : 'text-gray-400'}`}>
            {task.description || 'No description provided.'}
          </p>
          <div className="flex items-center gap-1.5 text-xs text-gray-500 pt-1">
            <Calendar className="w-3.5 h-3.5" />
            <span>Created {formatDate(task.createdAt)}</span>
          </div>
        </div>
      </div>

      {/* Action Buttons */}
      <div className="flex items-center gap-2 self-end md:self-center">
        <Link
          href={`/tasks/${task.id}`}
          className="flex items-center gap-1.5 px-3 py-1.5 rounded-lg text-xs font-semibold bg-white/5 hover:bg-white/10 text-gray-300 hover:text-white border border-white/5 hover:border-white/10 transition-all"
        >
          <Edit3 className="w-3.5 h-3.5" /> Edit
        </Link>
        <button
          onClick={handleDelete}
          disabled={isPendingDelete}
          className="flex items-center gap-1.5 px-3 py-1.5 rounded-lg text-xs font-semibold bg-rose-500/10 hover:bg-rose-500/20 text-rose-400 hover:text-rose-300 border border-rose-500/15 hover:border-rose-500/25 transition-all disabled:opacity-50 cursor-pointer"
        >
          <Trash2 className="w-3.5 h-3.5" /> Delete
        </button>
      </div>
    </div>
  );
}
