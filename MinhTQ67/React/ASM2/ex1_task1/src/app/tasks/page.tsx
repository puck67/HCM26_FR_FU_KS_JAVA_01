import Link from "next/link";
import { getTasks } from "@/lib/store";
import TaskActions from "@/components/TaskActions";

// No caching — always fetch fresh data
export const dynamic = "force-dynamic";

export default function TasksPage() {
  const tasks = getTasks();

  return (
    <div>
      {/* Header */}
      <div className="flex items-center justify-between mb-6">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">Tasks</h1>
          <p className="text-gray-500 text-sm mt-0.5">
            {tasks.length} task{tasks.length !== 1 ? "s" : ""} total
          </p>
        </div>
        <Link
          href="/tasks/new"
          className="px-5 py-2.5 bg-indigo-600 text-white rounded-xl text-sm font-semibold hover:bg-indigo-700 transition-colors shadow-sm"
        >
          + New Task
        </Link>
      </div>

      {/* Task List */}
      {tasks.length === 0 ? (
        <div className="text-center py-20">
          <div className="text-5xl mb-4">📭</div>
          <p className="text-gray-500 mb-2">No tasks yet.</p>
          <Link href="/tasks/new" className="text-indigo-600 text-sm font-medium">
            Create your first task →
          </Link>
        </div>
      ) : (
        <div className="space-y-3">
          {tasks.map((task) => (
            <div
              key={task.id}
              className={`bg-white rounded-xl border p-5 flex flex-col sm:flex-row sm:items-center gap-4 transition-all ${
                task.completed
                  ? "border-green-100 bg-green-50 opacity-80"
                  : "border-gray-100 hover:shadow-sm"
              }`}
            >
              {/* Info */}
              <div className="flex-1 min-w-0">
                <div className="flex items-center gap-2 flex-wrap">
                  <h2
                    className={`font-semibold text-sm ${
                      task.completed ? "line-through text-gray-400" : "text-gray-800"
                    }`}
                  >
                    {task.name}
                  </h2>
                  {task.completed && (
                    <span className="text-xs px-2 py-0.5 rounded-full bg-green-100 text-green-700 font-medium">
                      Completed
                    </span>
                  )}
                </div>
                {task.description && (
                  <p
                    className={`text-xs mt-1 line-clamp-2 ${
                      task.completed ? "text-gray-400 line-through" : "text-gray-500"
                    }`}
                  >
                    {task.description}
                  </p>
                )}
                <p className="text-xs text-gray-300 mt-1">
                  {new Date(task.createdAt).toLocaleDateString()}
                </p>
              </div>

              {/* Actions (Client Component) */}
              <TaskActions task={task} />
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
