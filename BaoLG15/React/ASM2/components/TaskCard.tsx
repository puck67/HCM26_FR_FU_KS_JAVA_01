"use client";

import { deleteTask, toggleTaskCompleted } from "@/lib/actions";
import { Task } from "@/types/task";
import Link from "next/link";
import { useState, useTransition } from "react";

interface TaskCardProps {
  task: Task;
}

export default function TaskCard({ task }: TaskCardProps) {
  const [isPending, startTransition] = useTransition();
  const [deletePending, setDeletePending] = useState(false);

  const handleToggle = () => {
    startTransition(async () => {
      await toggleTaskCompleted(task.id);
    });
  };

  const handleDelete = () => {
    setDeletePending(true);
    startTransition(async () => {
      await deleteTask(task.id);
      setDeletePending(false);
    });
  };

  return (
    <div
      className={`glass-card rounded-2xl p-5 transition-all duration-300 hover:border-gray-600/60 hover:-translate-y-0.5 group ${
        task.completed ? "opacity-70" : ""
      } ${isPending || deletePending ? "opacity-50 pointer-events-none" : ""}`}
    >
      {/* Header */}
      <div className="flex items-start justify-between gap-3 mb-3">
        <div className="flex items-start gap-3 flex-1 min-w-0">
          {/* Completed indicator */}
          <button
            id={`toggle-${task.id}`}
            onClick={handleToggle}
            disabled={isPending}
            className={`mt-0.5 flex-shrink-0 w-5 h-5 rounded-full border-2 flex items-center justify-center transition-all duration-200 ${
              task.completed
                ? "bg-emerald-500 border-emerald-500"
                : "border-gray-600 hover:border-violet-400"
            }`}
            title={task.completed ? "Mark incomplete" : "Mark complete"}
          >
            {task.completed && (
              <svg className="w-3 h-3 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={3} d="M5 13l4 4L19 7" />
              </svg>
            )}
          </button>

          <div className="flex-1 min-w-0">
            <h3
              className={`font-semibold text-base leading-snug transition-all duration-200 ${
                task.completed
                  ? "line-through text-gray-500"
                  : "text-white group-hover:text-violet-200"
              }`}
            >
              {task.name}
            </h3>
            {task.description && (
              <p
                className={`text-sm mt-1 leading-relaxed line-clamp-2 ${
                  task.completed ? "text-gray-600 line-through" : "text-gray-400"
                }`}
              >
                {task.description}
              </p>
            )}
          </div>
        </div>

        {/* Status badge */}
        <span
          className={`flex-shrink-0 px-2 py-0.5 rounded-full text-xs font-medium ${
            task.completed
              ? "bg-emerald-900/40 text-emerald-400 border border-emerald-800/50"
              : "bg-amber-900/40 text-amber-400 border border-amber-800/50"
          }`}
        >
          {task.completed ? "Done" : "Pending"}
        </span>
      </div>

      {/* Footer */}
      <div className="flex items-center justify-between pt-3 border-t border-gray-800/60">
        <span className="text-xs text-gray-600">
          {new Date(task.createdAt).toLocaleDateString("en-US", {
            month: "short",
            day: "numeric",
            year: "numeric",
          })}
        </span>

        <div className="flex items-center gap-1.5">
          {/* Toggle button */}
          <button
            id={`toggle-label-${task.id}`}
            onClick={handleToggle}
            disabled={isPending}
            className={`px-2.5 py-1 rounded-lg text-xs font-medium transition-all duration-200 ${
              task.completed
                ? "bg-gray-800/60 text-gray-400 hover:bg-gray-700/60 hover:text-gray-200"
                : "bg-emerald-900/30 text-emerald-400 hover:bg-emerald-900/50 border border-emerald-800/40"
            }`}
          >
            {task.completed ? "Undo" : "Complete"}
          </button>

          {/* Edit button */}
          <Link
            href={`/tasks/${task.id}`}
            id={`edit-${task.id}`}
            className="px-2.5 py-1 rounded-lg text-xs font-medium bg-indigo-900/30 text-indigo-400 hover:bg-indigo-900/50 border border-indigo-800/40 transition-all duration-200"
          >
            Edit
          </Link>

          {/* Delete button */}
          <button
            id={`delete-${task.id}`}
            onClick={handleDelete}
            disabled={deletePending}
            className="px-2.5 py-1 rounded-lg text-xs font-medium bg-rose-900/30 text-rose-400 hover:bg-rose-900/50 border border-rose-800/40 transition-all duration-200"
          >
            {deletePending ? "..." : "Delete"}
          </button>
        </div>
      </div>
    </div>
  );
}
