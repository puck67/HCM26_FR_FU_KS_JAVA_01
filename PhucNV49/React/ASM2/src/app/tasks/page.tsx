import Link from "next/link";
import { getTasks } from "@/lib/data";
import { TaskItem } from "./task-item";

// Force dynamic rendering so data is always fresh after mutations
export const dynamic = "force-dynamic";

// Server component - fetches data on server
export default function TasksPage() {
  const tasks = getTasks();

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h1 className="text-2xl font-bold text-slate-900 tracking-tight">Tasks</h1>
          <p className="text-sm text-slate-500 mt-0.5">
            {tasks.length} task{tasks.length !== 1 ? "s" : ""} total
            {tasks.filter((t) => t.completed).length > 0 && (
              <span className="text-emerald-600 ml-2">
                {tasks.filter((t) => t.completed).length} completed
              </span>
            )}
          </p>
        </div>
        <Link
          href="/tasks/new"
          className="inline-flex items-center gap-2 px-4 py-2.5 rounded-(--radius-btn) bg-accent text-white text-sm font-medium hover:bg-accent-dark transition-colors active:scale-[0.98] self-start sm:self-auto"
        >
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round">
            <path d="M12 5v14M5 12h14" />
          </svg>
          New Task
        </Link>
      </div>

      {/* Task list */}
      {tasks.length === 0 ? (
        <div className="text-center py-16">
          <div className="w-12 h-12 rounded-full bg-slate-100 flex items-center justify-center mx-auto mb-3">
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="#94a3b8" strokeWidth="1.5" strokeLinecap="round">
              <path d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2" />
              <rect x="9" y="3" width="6" height="4" rx="1" />
            </svg>
          </div>
          <p className="text-sm text-slate-500 mb-3">No tasks yet</p>
          <Link href="/tasks/new" className="text-sm text-accent hover:underline">
            Create your first task
          </Link>
        </div>
      ) : (
        <ul className="space-y-2">
          {tasks.map((task) => (
            <TaskItem key={task.id} task={task} />
          ))}
        </ul>
      )}
    </div>
  );
}
