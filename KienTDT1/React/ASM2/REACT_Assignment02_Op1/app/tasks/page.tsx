import Link from 'next/link';
import { readTasks } from '@/lib/db';
import { toggleTaskCompleted, deleteTask } from '@/lib/actions';

export const dynamic = 'force-dynamic';

export default async function TasksPage() {
  const tasks = readTasks();

  return (
    <div className="space-y-8 animate-slide-up">
      {/* Header section */}
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h1 className="text-3xl font-extrabold text-white tracking-tight">Your Tasks</h1>
          <p className="text-slate-400 mt-1">Manage and track your daily priorities.</p>
        </div>
        <Link
          href="/tasks/new"
          className="inline-flex items-center justify-center gap-2 px-5 py-2.5 rounded-xl font-semibold bg-gradient-to-r from-indigo-500 to-violet-600 text-white shadow-lg shadow-indigo-500/10 hover:shadow-indigo-500/25 hover:scale-[1.02] active:scale-[0.98] transition-all duration-300"
        >
          <svg
            className="h-5 w-5"
            fill="none"
            viewBox="0 0 24 24"
            stroke="currentColor"
            strokeWidth="2"
          >
            <path strokeLinecap="round" strokeLinejoin="round" d="M12 4v16m8-8H4" />
          </svg>
          New Task
        </Link>
      </div>

      {/* Task List Grid */}
      {tasks.length === 0 ? (
        <div className="glass-panel rounded-2xl p-12 text-center border border-slate-800/80 flex flex-col items-center justify-center space-y-4">
          <div className="h-16 w-16 rounded-full bg-slate-900 flex items-center justify-center text-slate-500">
            <svg
              className="h-8 w-8"
              fill="none"
              viewBox="0 0 24 24"
              stroke="currentColor"
              strokeWidth="1.5"
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2"
              />
            </svg>
          </div>
          <div className="space-y-1">
            <h3 className="text-lg font-semibold text-slate-200">No tasks found</h3>
            <p className="text-slate-400 text-sm max-w-sm">
              Get started by creating your first task using the button above.
            </p>
          </div>
        </div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          {tasks.map((task) => {
            const toggleAction = toggleTaskCompleted.bind(null, task.id);
            const deleteAction = deleteTask.bind(null, task.id);

            return (
              <div
                key={task.id}
                className={`glass-panel glass-panel-hover rounded-2xl p-6 flex flex-col justify-between border transition-all duration-300 ${
                  task.completed
                    ? 'border-emerald-500/20 bg-emerald-950/5'
                    : 'border-slate-800/80'
                }`}
              >
                <div>
                  <div className="flex items-start justify-between gap-4">
                    {/* Checkbox and Name */}
                    <div className="flex items-start gap-3">
                      <form action={toggleAction} className="mt-1">
                        <button
                          type="submit"
                          className={`h-5 w-5 rounded-md flex items-center justify-center border transition-all duration-300 ${
                            task.completed
                              ? 'bg-emerald-500/20 border-emerald-400 text-emerald-400 shadow-md shadow-emerald-500/10'
                              : 'border-slate-600 hover:border-indigo-400 text-transparent hover:bg-slate-800/50'
                          }`}
                          aria-label={task.completed ? "Mark incomplete" : "Mark completed"}
                        >
                          <svg
                            className="h-3 w-3"
                            fill="none"
                            viewBox="0 0 24 24"
                            stroke="currentColor"
                            strokeWidth="3.5"
                          >
                            <path
                              strokeLinecap="round"
                              strokeLinejoin="round"
                              d="M5 13l4 4L19 7"
                            />
                          </svg>
                        </button>
                      </form>
                      <h2
                        className={`text-lg font-bold tracking-tight transition-all duration-300 ${
                          task.completed
                            ? 'text-slate-500 line-through decoration-slate-600'
                            : 'text-slate-100'
                        }`}
                      >
                        {task.name}
                      </h2>
                    </div>

                    {/* Badge */}
                    <span
                      className={`text-[10px] uppercase tracking-wider font-extrabold px-2 py-0.5 rounded ${
                        task.completed
                          ? 'bg-emerald-500/10 text-emerald-400 border border-emerald-500/20'
                          : 'bg-indigo-500/10 text-indigo-300 border border-indigo-500/20'
                      }`}
                    >
                      {task.completed ? 'Done' : 'Active'}
                    </span>
                  </div>

                  {/* Description */}
                  {task.description && (
                    <p
                      className={`mt-3 text-sm leading-relaxed ${
                        task.completed ? 'text-slate-500' : 'text-slate-400'
                      }`}
                    >
                      {task.description}
                    </p>
                  )}
                </div>

                {/* Actions */}
                <div className="mt-6 pt-4 border-t border-slate-800/40 flex items-center justify-between">
                  <span className="text-[11px] text-slate-500 font-medium">
                    Created: {new Date(task.createdAt).toLocaleDateString()}
                  </span>
                  <div className="flex items-center gap-3">
                    <Link
                      href={`/tasks/${task.id}`}
                      className="px-3.5 py-1.5 rounded-lg text-xs font-semibold bg-slate-800/80 text-slate-300 border border-slate-700/50 hover:bg-slate-700/60 hover:text-white transition-all duration-300"
                    >
                      Edit
                    </Link>
                    <form action={deleteAction}>
                      <button
                        type="submit"
                        className="px-3.5 py-1.5 rounded-lg text-xs font-semibold bg-rose-500/10 text-rose-400 border border-rose-500/20 hover:bg-rose-500/20 hover:text-rose-300 transition-all duration-300"
                      >
                        Delete
                      </button>
                    </form>
                  </div>
                </div>
              </div>
            );
          })}
        </div>
      )}
    </div>
  );
}
