"use client";

import { useTransition } from "react";
import { useRouter } from "next/navigation";
import Link from "next/link";
import { deleteTask, toggleTaskCompleted } from "@/lib/actions";
import { Task } from "@/types/task";

interface Props {
  task: Task;
}

export default function TaskActions({ task }: Props) {
  const [isPending, startTransition] = useTransition();
  const router = useRouter();

  const handleDelete = () => {
    if (!confirm(`Delete "${task.name}"?`)) return;
    startTransition(async () => {
      await deleteTask(task.id);
      router.refresh();
    });
  };

  const handleToggle = () => {
    startTransition(async () => {
      await toggleTaskCompleted(task.id);
      router.refresh();
    });
  };

  return (
    <div className="flex items-center gap-2 flex-shrink-0 flex-wrap">
      <button
        onClick={handleToggle}
        disabled={isPending}
        className={`text-xs px-3 py-1.5 rounded-md font-medium transition-colors disabled:opacity-50 ${
          task.completed
            ? "bg-gray-100 text-gray-600 hover:bg-gray-200"
            : "bg-green-50 text-green-700 hover:bg-green-100"
        }`}
      >
        {task.completed ? "Undo" : "Complete"}
      </button>

      <Link
        href={`/tasks/${task.id}`}
        className="text-xs px-3 py-1.5 rounded-md bg-amber-50 text-amber-700 hover:bg-amber-100 font-medium transition-colors"
      >
        Edit
      </Link>

      <button
        onClick={handleDelete}
        disabled={isPending}
        className="text-xs px-3 py-1.5 rounded-md bg-red-50 text-red-600 hover:bg-red-100 font-medium transition-colors disabled:opacity-50"
      >
        Delete
      </button>
    </div>
  );
}
