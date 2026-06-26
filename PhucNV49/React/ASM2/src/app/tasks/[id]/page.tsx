import { notFound } from "next/navigation";
import Link from "next/link";
import { getTask } from "@/lib/data";
import { TaskEditForm } from "./edit-form";

// Server component - loads task data
export default async function TaskDetailPage({ params }: { params: Promise<{ id: string }> }) {
  const { id } = await params;
  const task = getTask(id);

  if (!task) {
    notFound();
  }

  return (
    <div className="max-w-2xl mx-auto">
      {/* Breadcrumb */}
      <nav className="flex items-center gap-2 text-sm text-slate-400 mb-6">
        <Link href="/tasks" className="hover:text-accent transition-colors">Tasks</Link>
        <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round">
          <path d="M9 18l6-6-6-6" />
        </svg>
        <span className="text-slate-600 truncate max-w-[200px]">{task.name}</span>
      </nav>

      {/* Task info card */}
      <article className="bg-white rounded-(--radius-card) border border-slate-200/60 shadow-sm overflow-hidden mb-6">
        <div className="p-6 sm:p-8 space-y-4">
          <div className="flex items-start justify-between gap-4">
            <div className="space-y-2">
              <h1 className={`text-xl sm:text-2xl font-bold tracking-tight ${
                task.completed ? "text-slate-400 line-through" : "text-slate-900"
              }`}>
                {task.name}
              </h1>
              <span className={`inline-flex items-center px-2.5 py-1 rounded-md text-xs font-medium ${
                task.completed
                  ? "bg-emerald-50 text-emerald-700"
                  : "bg-slate-100 text-slate-600"
              }`}>
                {task.completed ? "Completed" : "Pending"}
              </span>
            </div>
          </div>

          <div className="space-y-1.5">
            <h2 className="text-xs font-medium text-slate-400 uppercase tracking-wider">Description</h2>
            <p className={`text-sm leading-relaxed ${
              task.completed ? "text-slate-400 line-through" : "text-slate-600"
            }`}>
              {task.description || "No description provided."}
            </p>
          </div>

          <div className="pt-4 border-t border-slate-100">
            <div className="flex flex-wrap gap-6 text-xs text-slate-400">
              <div>
                <span className="block font-medium text-slate-500 mb-0.5">Created</span>
                {new Date(task.createdAt).toLocaleDateString("en-US", {
                  year: "numeric", month: "short", day: "numeric",
                })}
              </div>
              <div>
                <span className="block font-medium text-slate-500 mb-0.5">ID</span>
                {task.id}
              </div>
            </div>
          </div>
        </div>
      </article>

      {/* Client component: edit form */}
      <TaskEditForm task={task} />
    </div>
  );
}
