"use client";

import Link from "next/link";
import { useTransition } from "react";
import type { Task } from "@/lib/data";
import { deleteTask, toggleTaskCompleted } from "@/lib/actions";

export function TaskItem({ task }: { task: Task }) {
  const [isPending, startTransition] = useTransition();

  const handleToggle = () => {
    startTransition(() => {
      toggleTaskCompleted(task.id);
    });
  };

  const handleDelete = () => {
    if (!confirm("Delete this task?")) return;
    startTransition(() => {
      deleteTask(task.id);
    });
  };

  return (
    <li
      className={`group relative bg-white rounded-(--radius-card) border p-4 sm:p-5 transition-all ${
        task.completed
          ? "border-emerald-200/60 bg-emerald-50/30"
          : "border-slate-200/60 hover:border-slate-300 hover:shadow-sm"
      } ${isPending ? "opacity-50 pointer-events-none" : ""}`}
    >
      <div className="flex items-start gap-3">
        {/* Toggle checkbox */}
        <button
          onClick={handleToggle}
          className={`mt-0.5 w-5 h-5 rounded-md border-2 flex items-center justify-center shrink-0 transition-colors ${
            task.completed
              ? "bg-emerald-500 border-emerald-500"
              : "border-slate-300 hover:border-accent"
          }`}
          aria-label={task.completed ? "Mark incomplete" : "Mark completed"}
        >
          {task.completed && (
            <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="white" strokeWidth="3" strokeLinecap="round" strokeLinejoin="round">
              <path d="M5 12l5 5L20 7" />
            </svg>
          )}
        </button>

        {/* Content */}
        <div className="flex-1 min-w-0">
          <Link
            href={`/tasks/${task.id}`}
            className={`text-sm font-semibold transition-colors ${
              task.completed
                ? "text-slate-400 line-through"
                : "text-slate-900 hover:text-accent"
            }`}
          >
            {task.name}
          </Link>
          {task.description && (
            <p className={`text-xs mt-0.5 truncate max-w-md ${
              task.completed ? "text-slate-300 line-through" : "text-slate-400"
            }`}>
              {task.description}
            </p>
          )}
        </div>

        {/* Actions */}
        <div className="flex items-center gap-1 opacity-0 group-hover:opacity-100 transition-opacity">
          <Link
            href={`/tasks/${task.id}`}
            className="p-1.5 rounded-md text-slate-400 hover:text-accent hover:bg-accent-light transition-colors"
            title="Edit"
          >
            <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round">
              <path d="M11 4H4a2 2 0 00-2 2v14a2 2 0 002 2h14a2 2 0 002-2v-7" />
              <path d="M18.5 2.5a2.121 2.121 0 013 3L12 15l-4 1 1-4 9.5-9.5z" />
            </svg>
          </Link>
          <button
            onClick={handleDelete}
            className="p-1.5 rounded-md text-slate-400 hover:text-danger hover:bg-danger-light transition-colors"
            title="Delete"
          >
            <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round">
              <path d="M3 6h18M19 6v14a2 2 0 01-2 2H7a2 2 0 01-2-2V6m3 0V4a2 2 0 012-2h4a2 2 0 012 2v2" />
            </svg>
          </button>
        </div>
      </div>
    </li>
  );
}
