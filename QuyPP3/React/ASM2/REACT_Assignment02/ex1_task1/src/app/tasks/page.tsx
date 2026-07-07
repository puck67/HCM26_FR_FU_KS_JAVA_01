import { dataStore } from "@/lib/dataStore";
import { deleteTask, toggleTaskCompleted } from "@/app/actions/taskActions";
import Link from "next/link";

export const dynamic = "force-dynamic"; // Equivalent to cache: 'no-store' pipeline

export default async function TasksPage() {
  const tasks = await dataStore.getAll();

  return (
    <div className="space-y-6 max-w-4xl mx-auto">
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4 border-b border-slate-200 pb-5">
        <div>
          <h1 className="text-2xl font-bold text-slate-900">Task Workspace</h1>
          <p className="text-sm text-slate-500">Rendered serverside with fast state execution bindings.</p>
        </div>
        <Link
          href="/tasks/new"
          className="px-4 py-2 bg-blue-600 hover:bg-blue-700 text-white text-sm font-semibold rounded-xl shadow-sm transition"
        >
          Add Task
        </Link>
      </div>

      <div className="divide-y divide-slate-200 border border-slate-200 rounded-2xl bg-white shadow-sm overflow-hidden">
        {tasks.length === 0 ? (
          <div className="p-8 text-center text-slate-400 font-medium">No system tasks configured.</div>
        ) : (
          tasks.map((task) => (
            <div key={task.id} className="p-5 flex items-center justify-between gap-4 hover:bg-slate-50/50 transition">
              <div className="space-y-1 min-w-0">
                <h3 className={`font-semibold text-slate-900 text-base transition-all ${task.completed ? "line-through text-slate-400 opacity-60" : ""}`}>
                  {task.name}
                </h3>
                <p className={`text-sm text-slate-500 truncate max-w-md ${task.completed ? "line-through text-slate-300" : ""}`}>
                  {task.description || "—"}
                </p>
              </div>

              <div className="flex items-center gap-2 shrink-0">
                {/* Checkbox Trigger mapping straight to a Server Action */}
                <form action={toggleTaskCompleted.bind(null, task.id)}>
                  <button
                    type="submit"
                    className={`px-3 py-1.5 text-xs font-bold rounded-lg transition ${
                      task.completed 
                        ? "bg-green-50 text-green-700 border border-green-200" 
                        : "bg-slate-100 text-slate-600 border border-slate-200 hover:bg-slate-200"
                    }`}
                  >
                    {task.completed ? "Completed" : "Mark Done"}
                  </button>
                </form>

                <Link
                  href={`/tasks/${task.id}`}
                  className="px-3 py-1.5 bg-amber-50 hover:bg-amber-100 text-amber-700 text-xs font-bold rounded-lg transition"
                >
                  Edit
                </Link>

                <form action={deleteTask.bind(null, task.id)}>
                  <button
                    type="submit"
                    className="px-3 py-1.5 bg-rose-50 hover:bg-rose-100 text-rose-600 text-xs font-bold rounded-lg transition"
                  >
                    Delete
                  </button>
                </form>
              </div>
            </div>
          ))
        )}
      </div>
    </div>
  );
}
