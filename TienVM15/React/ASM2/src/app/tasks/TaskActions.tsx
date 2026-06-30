"use client";

import { useTransition } from "react";
import Link from "next/link";
import { deleteTask, toggleTaskCompleted } from "../actions";

interface TaskActionsProps {
  id: string;
  completed: boolean;
}

export default function TaskActions({ id, completed }: TaskActionsProps) {
  const [isPendingDelete, startTransitionDelete] = useTransition();
  const [isPendingToggle, startTransitionToggle] = useTransition();

  const handleToggle = () => {
    startTransitionToggle(async () => {
      await toggleTaskCompleted(id);
    });
  };

  const handleDelete = () => {
    if (confirm("Are you sure you want to delete this task?")) {
      startTransitionDelete(async () => {
        await deleteTask(id);
      });
    }
  };

  return (
    <div className="flex items-center gap-3">
      {/* Toggle Complete Button */}
      <button
        onClick={handleToggle}
        disabled={isPendingToggle}
        className={`inline-flex items-center justify-center rounded-lg px-3.5 py-1.5 text-xs font-semibold shadow-sm transition-all focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 ${
          completed
            ? "bg-zinc-800 text-zinc-400 hover:bg-zinc-700/60 border border-zinc-700/40"
            : "bg-teal-500/10 text-teal-400 hover:bg-teal-500/20 border border-teal-500/30"
        }`}
      >
        {isPendingToggle ? (
          <span className="inline-block animate-spin mr-1.5 h-3 w-3 border-2 border-teal-400 border-t-transparent rounded-full" />
        ) : null}
        {completed ? "Mark Pending" : "Mark Completed"}
      </button>

      {/* Edit Link */}
      <Link
        href={`/tasks/${id}`}
        className="inline-flex items-center justify-center rounded-lg bg-zinc-800 text-zinc-300 hover:text-white hover:bg-zinc-700 border border-zinc-700/50 px-3.5 py-1.5 text-xs font-semibold shadow-sm transition-colors"
      >
        Edit
      </Link>

      {/* Delete Button */}
      <button
        onClick={handleDelete}
        disabled={isPendingDelete}
        className="inline-flex items-center justify-center rounded-lg bg-red-500/10 text-red-400 hover:bg-red-500/20 border border-red-500/30 px-3.5 py-1.5 text-xs font-semibold shadow-sm transition-colors"
      >
        {isPendingDelete ? (
          <span className="inline-block animate-spin mr-1.5 h-3 w-3 border-2 border-red-400 border-t-transparent rounded-full" />
        ) : null}
        Delete
      </button>
    </div>
  );
}
