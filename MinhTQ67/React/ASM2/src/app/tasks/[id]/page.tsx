import { notFound } from "next/navigation";
import Link from "next/link";
import { getTaskById } from "@/lib/store";
import EditTaskForm from "@/components/EditTaskForm";

interface Props {
  params: { id: string };
}

// Server Component — loads task data
export default function TaskDetailPage({ params }: Props) {
  const task = getTaskById(params.id);

  if (!task) {
    notFound();
  }

  return (
    <div className="max-w-lg mx-auto">
      {/* Breadcrumb */}
      <nav className="flex items-center gap-2 text-sm text-gray-500 mb-6">
        <Link href="/tasks" className="hover:text-indigo-600 transition-colors">
          Tasks
        </Link>
        <span>/</span>
        <span className="text-gray-800 font-medium truncate max-w-[200px]">{task.name}</span>
      </nav>

      {/* Status badge */}
      <div className="flex items-center gap-2 mb-4">
        <span
          className={`text-xs px-3 py-1 rounded-full font-medium border ${
            task.completed
              ? "bg-green-50 text-green-700 border-green-100"
              : "bg-gray-50 text-gray-600 border-gray-200"
          }`}
        >
          {task.completed ? "✅ Completed" : "⏳ Pending"}
        </span>
        <span className="text-xs text-gray-400">
          Created {new Date(task.createdAt).toLocaleDateString()}
        </span>
      </div>

      {/* Edit Form — Client Component */}
      <EditTaskForm task={task} />
    </div>
  );
}
