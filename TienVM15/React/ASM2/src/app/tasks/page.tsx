import Link from "next/link";
import { getTasks } from "@/data/db";
import TaskActions from "./TaskActions";

export const revalidate = 0; // Disable static rendering to ensure new tasks are fetched

export default function TasksPage() {
  const tasks = getTasks();

  return (
    <div className="space-y-6 py-4">
      {/* Header section */}
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h1 className="text-2xl font-bold tracking-tight text-white sm:text-3xl">Tasks List</h1>
          <p className="mt-1 text-sm text-zinc-400">
            View, filter, edit, or delete tasks in your system.
          </p>
        </div>
        <Link
          href="/tasks/new"
          className="inline-flex items-center justify-center rounded-lg bg-teal-500 px-4.5 py-2.5 text-sm font-semibold text-zinc-950 shadow-sm hover:bg-teal-400 transition-colors"
        >
          Create Task
        </Link>
      </div>

      {/* Task Table/List */}
      <div className="bg-zinc-900 border border-zinc-800 rounded-xl overflow-hidden shadow-md">
        {tasks.length === 0 ? (
          <div className="flex flex-col items-center justify-center py-16 px-4 text-center">
            <div className="p-4 bg-zinc-800/50 rounded-full text-zinc-500 mb-4 border border-zinc-700/30">
              <svg className="h-8 w-8" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth="1.5">
                <path strokeLinecap="round" strokeLinejoin="round" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" />
              </svg>
            </div>
            <h3 className="text-base font-semibold text-zinc-200">No tasks found</h3>
            <p className="mt-1 text-sm text-zinc-500 max-w-xs">
              Your task list is empty. Get started by creating a new task.
            </p>
            <Link
              href="/tasks/new"
              className="mt-4.5 inline-flex items-center justify-center rounded-lg bg-teal-500 px-3.5 py-2 text-xs font-semibold text-zinc-950 shadow-sm hover:bg-teal-400 transition-colors"
            >
              Add first task
            </Link>
          </div>
        ) : (
          <div className="overflow-x-auto">
            <table className="w-full text-left border-collapse">
              <thead>
                <tr className="border-b border-zinc-800 bg-zinc-900/80">
                  <th className="py-4 px-6 text-sm font-semibold text-zinc-400">Name</th>
                  <th className="py-4 px-6 text-sm font-semibold text-zinc-400">Description</th>
                  <th className="py-4 px-6 text-sm font-semibold text-zinc-400">Status</th>
                  <th className="py-4 px-6 text-sm font-semibold text-zinc-400 text-right">Actions</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-zinc-800/60">
                {tasks.map((task) => (
                  <tr key={task.id} className="hover:bg-zinc-900/40 transition-colors">
                    <td className="py-4 px-6">
                      <div className={`font-semibold text-sm ${task.completed ? "line-through text-zinc-500" : "text-white"}`}>
                        {task.name}
                      </div>
                    </td>
                    <td className="py-4 px-6 max-w-xs truncate">
                      <div className={`text-sm ${task.completed ? "line-through text-zinc-500" : "text-zinc-300"}`}>
                        {task.description || <span className="text-zinc-600 italic">No description</span>}
                      </div>
                    </td>
                    <td className="py-4 px-6">
                      <span
                        className={`inline-flex items-center rounded-md px-2 py-1 text-xs font-medium ring-1 ring-inset ${
                          task.completed
                            ? "bg-teal-500/10 text-teal-400 ring-teal-500/20"
                            : "bg-amber-500/10 text-amber-400 ring-amber-500/20"
                        }`}
                      >
                        {task.completed ? "Completed" : "Pending"}
                      </span>
                    </td>
                    <td className="py-4 px-6 text-right">
                      <TaskActions id={task.id} completed={task.completed} />
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  );
}
