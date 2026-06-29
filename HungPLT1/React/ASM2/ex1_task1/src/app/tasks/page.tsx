import Link from "next/link";
import { tasksRepo } from "@/lib/tasksRepo";
import { Plus, CheckSquare } from "lucide-react";
import { deleteTask, toggleTaskCompleted } from "@/app/actions";
import { TaskItemActions } from "./TaskItemActions";

export const dynamic = "force-dynamic";

export default async function TasksPage() {
  const tasks = await tasksRepo.getAll();

  return (
    <div className="flex-1 bg-slate-950 py-12 px-4 sm:px-6 lg:px-8">
      <div className="max-w-4xl mx-auto space-y-8">
        <div className="flex items-center justify-between">
          <div className="space-y-1">
            <h1 className="text-3xl font-extrabold tracking-tight text-slate-100">Tasks</h1>
            <p className="text-sm text-slate-400">Manage and track your active and completed tasks.</p>
          </div>
          <Link
            href="/tasks/new"
            className="inline-flex items-center gap-2 px-4 py-2 rounded-xl bg-violet-600 hover:bg-violet-700 text-sm font-semibold text-white shadow-lg shadow-violet-600/25 transition duration-150"
          >
            <Plus size={18} />
            Add Task
          </Link>
        </div>

        {tasks.length === 0 ? (
          <div className="bg-slate-900 border border-slate-800 rounded-2xl p-12 text-center space-y-4">
            <div className="w-12 h-12 rounded-full bg-slate-800 flex items-center justify-center mx-auto text-slate-400">
              <CheckSquare size={24} />
            </div>
            <div className="space-y-1">
              <p className="text-base font-semibold text-slate-200">No tasks found</p>
              <p className="text-sm text-slate-500">Get started by creating your first task.</p>
            </div>
            <Link
              href="/tasks/new"
              className="inline-flex items-center gap-2 px-4 py-2 rounded-xl bg-violet-600/10 hover:bg-violet-600/25 text-violet-400 text-sm font-semibold transition duration-150"
            >
              Create Task
            </Link>
          </div>
        ) : (
          <div className="grid grid-cols-1 gap-4">
            {tasks.map((task) => (
              <div
                key={task.id}
                className={`bg-slate-900 border rounded-2xl p-5 shadow-md flex flex-col sm:flex-row sm:items-center justify-between gap-4 transition duration-150 hover:border-slate-700 ${
                  task.completed ? "border-slate-800 bg-slate-900/40 opacity-75" : "border-slate-800"
                }`}
              >
                <div className="space-y-2 flex-grow">
                  <div className="flex items-center gap-2">
                    <span
                      className={`text-lg font-bold tracking-tight ${
                        task.completed ? "text-slate-500 line-through decoration-slate-600" : "text-slate-100"
                      }`}
                    >
                      {task.name}
                    </span>
                    {task.completed ? (
                      <span className="inline-flex items-center px-2 py-0.5 rounded-full text-xs font-semibold bg-emerald-500/10 text-emerald-400 border border-emerald-500/20">
                        Completed
                      </span>
                    ) : (
                      <span className="inline-flex items-center px-2 py-0.5 rounded-full text-xs font-semibold bg-violet-500/10 text-violet-400 border border-violet-500/20">
                        Active
                      </span>
                    )}
                  </div>
                  {task.description && (
                    <p className={`text-sm ${task.completed ? "text-slate-600 line-through" : "text-slate-400"}`}>
                      {task.description}
                    </p>
                  )}
                  <div className="text-slate-600 text-xs font-medium">
                    Created on {new Date(task.createdAt).toLocaleDateString()}
                  </div>
                </div>

                <TaskItemActions
                  taskId={task.id}
                  isCompleted={task.completed}
                  toggleAction={toggleTaskCompleted}
                  deleteAction={deleteTask}
                />
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}
