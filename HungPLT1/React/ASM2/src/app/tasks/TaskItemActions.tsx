"use client";

import { useTransition } from "react";
import Link from "next/link";
import { CheckSquare, Square, Edit, Trash2 } from "lucide-react";

interface TaskItemActionsProps {
  taskId: string;
  isCompleted: boolean;
  toggleAction: (id: string) => Promise<void>;
  deleteAction: (id: string) => Promise<void>;
}

export function TaskItemActions({
  taskId,
  isCompleted,
  toggleAction,
  deleteAction,
}: TaskItemActionsProps) {
  const [isPendingToggle, startToggleTransition] = useTransition();
  const [isPendingDelete, startDeleteTransition] = useTransition();

  return (
    <div className="flex items-center gap-2 self-end sm:self-center">
      <button
        onClick={() => startToggleTransition(() => toggleAction(taskId))}
        disabled={isPendingToggle || isPendingDelete}
        className={`inline-flex items-center gap-1.5 px-3 py-1.5 rounded-xl border text-xs font-bold transition duration-150 ${
          isCompleted
            ? "border-slate-800 bg-slate-800/50 hover:bg-slate-800 text-slate-400 hover:text-slate-200"
            : "border-violet-500/20 bg-violet-500/5 hover:bg-violet-500/10 text-violet-400 hover:text-violet-300"
        } disabled:opacity-50`}
      >
        {isCompleted ? <CheckSquare size={14} /> : <Square size={14} />}
        {isCompleted ? "Mark Active" : "Mark Completed"}
      </button>

      <Link
        href={`/tasks/${taskId}`}
        className="inline-flex items-center justify-center p-1.5 rounded-xl border border-slate-800 bg-slate-800/20 hover:bg-slate-800 hover:text-slate-100 text-slate-400 transition duration-150"
      >
        <Edit size={16} />
      </Link>

      <button
        onClick={() => {
          if (confirm("Are you sure you want to delete this task?")) {
            startDeleteTransition(() => deleteAction(taskId));
          }
        }}
        disabled={isPendingToggle || isPendingDelete}
        className="inline-flex items-center justify-center p-1.5 rounded-xl border border-red-500/10 bg-red-500/5 hover:bg-red-500/10 hover:text-red-400 text-red-500/60 transition duration-150 disabled:opacity-50"
      >
        <Trash2 size={16} />
      </button>
    </div>
  );
}
