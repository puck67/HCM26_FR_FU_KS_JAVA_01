'use client';

import { useTransition } from 'react';
import { deleteTask, toggleTaskCompleted } from '@/actions/taskActions';
import Link from 'next/link';

interface TaskActionsProps {
  id: string;
  completed: boolean;
}

export default function TaskActions({ id, completed }: TaskActionsProps) {
  const [isPending, startTransition] = useTransition();

  const handleToggle = () => {
    startTransition(async () => {
      await toggleTaskCompleted(id);
    });
  };

  const handleDelete = () => {
    if (confirm('Are you sure you want to delete this task?')) {
      startTransition(async () => {
        await deleteTask(id);
      });
    }
  };

  return (
    <div className={`flex items-center gap-2 flex-shrink-0 transition-opacity duration-200 ${isPending ? 'opacity-50 pointer-events-none' : ''}`}>
      {/* Complete/Uncomplete button */}
      <button
        onClick={handleToggle}
        className={`p-2 rounded-xl border text-xs font-semibold flex items-center gap-1.5 transition-all active:scale-95 ${
          completed
            ? 'bg-amber-50 text-amber-800 border-amber-200 hover:bg-amber-100/70'
            : 'bg-emerald-50 text-emerald-700 border-emerald-200 hover:bg-emerald-100/70'
        }`}
        title={completed ? 'Mark as Active' : 'Mark as Completed'}
        disabled={isPending}
      >
        {completed ? (
          <>
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={2.5} stroke="currentColor" className="w-4 h-4">
              <path strokeLinecap="round" strokeLinejoin="round" d="M9 15L3 9m0 0l6-6M3 9h12a6 6 0 010 12h-3" />
            </svg>
            <span className="hidden sm:inline">Reactivate</span>
          </>
        ) : (
          <>
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={2.5} stroke="currentColor" className="w-4 h-4">
              <path strokeLinecap="round" strokeLinejoin="round" d="M4.5 12.75l6 6 9-13.5" />
            </svg>
            <span className="hidden sm:inline">Complete</span>
          </>
        )}
      </button>

      {/* Edit button */}
      <Link
        href={`/tasks/${id}`}
        className="p-2 rounded-xl bg-slate-50 border border-slate-200 hover:border-slate-300 text-slate-700 hover:text-indigo-600 transition-all hover:shadow-sm active:scale-95 text-xs font-semibold flex items-center gap-1.5"
        title="Edit Task"
      >
        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={2} stroke="currentColor" className="w-4 h-4">
          <path strokeLinecap="round" strokeLinejoin="round" d="M16.862 4.487l1.687-1.688a1.875 1.875 0 112.652 2.652L6.83 17.59a4.5 4.5 0 01-1.897 1.13L6 18l.8-2.685a4.5 4.5 0 011.13-1.897l8.932-8.931zm0 0L19.5 7.125M18 14v4.75A2.25 2.25 0 0115.75 21H5.25A2.25 2.25 0 013 18.75V8.25A2.25 2.25 0 015.25 6H10" />
        </svg>
        <span className="hidden sm:inline">Edit</span>
      </Link>

      {/* Delete button */}
      <button
        onClick={handleDelete}
        className="p-2 rounded-xl bg-rose-50 border border-rose-200 hover:bg-rose-100 text-rose-700 hover:text-rose-800 transition-all hover:shadow-sm active:scale-95 text-xs font-semibold flex items-center gap-1.5"
        title="Delete Task"
        disabled={isPending}
      >
        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={2} stroke="currentColor" className="w-4 h-4">
          <path strokeLinecap="round" strokeLinejoin="round" d="M14.74 9l-.346 9m-4.788 0L9.26 9m9.968-3.21c.342.052.682.107 1.022.166m-1.022-.165L18.16 19.673a2.25 2.25 0 01-2.244 2.077H8.084a2.25 2.25 0 01-2.244-2.077L4.772 5.79m14.456 0a48.108 48.108 0 00-3.478-.397m-12 .562c.34-.059.68-.114 1.022-.165m0 0a48.11 48.11 0 013.478-.397m7.5 0v-.916c0-1.18-.91-2.164-2.09-2.201a51.964 51.964 0 00-3.32 0c-1.18.037-2.09 1.022-2.09 2.201v.916m7.5 0a48.667 48.667 0 00-7.5 0" />
        </svg>
        <span className="hidden sm:inline">Delete</span>
      </button>
    </div>

  );
}
