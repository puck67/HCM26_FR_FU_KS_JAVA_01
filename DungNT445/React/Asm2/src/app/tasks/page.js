import Link from "next/link";
import { getTasks } from "../../lib/tasks";
import { deleteTask, toggleTaskCompleted } from "../actions";

export const dynamic = "force-dynamic";

export default async function TasksPage() {
  const tasks = await getTasks();

  return (
    <div className="mx-auto w-full max-w-7xl px-4 py-10 sm:px-6 lg:px-8">
      {/* Header section */}
      <div className="flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between border-b border-zinc-200 pb-6 dark:border-zinc-800">
        <div>
          <h1 className="text-3xl font-extrabold tracking-tight text-zinc-900 dark:text-white sm:text-4xl">
            All Tasks
          </h1>
          <p className="mt-2 text-sm text-zinc-500 dark:text-zinc-400">
            Keep track of your projects, edit entries, and mark completion progress.
          </p>
        </div>
        <div className="flex shrink-0 gap-3">
          <Link
            href="/tasks/new"
            className="inline-flex items-center gap-2 rounded-xl bg-indigo-600 px-4 py-2.5 text-sm font-semibold text-white shadow-sm hover:bg-indigo-500 transition-colors"
          >
            <svg
              xmlns="http://www.w3.org/2000/svg"
              fill="none"
              viewBox="0 0 24 24"
              strokeWidth={2}
              stroke="currentColor"
              className="h-4 w-4"
            >
              <path strokeLinecap="round" strokeLinejoin="round" d="M12 4.5v15m7.5-7.5h-15" />
            </svg>
            Add New Task
          </Link>
        </div>
      </div>

      {/* Tasks Grid */}
      <div className="mt-8">
        {tasks.length === 0 ? (
          <div className="rounded-3xl border-2 border-dashed border-zinc-200 bg-white px-6 py-16 text-center dark:border-zinc-800 dark:bg-zinc-900">
            <svg
              className="mx-auto h-16 w-16 text-zinc-400"
              fill="none"
              viewBox="0 0 24 24"
              stroke="currentColor"
              aria-hidden="true"
            >
              <path
                vectorEffect="non-scaling-stroke"
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth={1.5}
                d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2"
              />
            </svg>
            <h3 className="mt-4 text-lg font-semibold text-zinc-900 dark:text-white">
              No tasks created yet
            </h3>
            <p className="mt-2 text-sm text-zinc-500 dark:text-zinc-400">
              Get started by adding your first task. It only takes a minute.
            </p>
            <div className="mt-6">
              <Link
                href="/tasks/new"
                className="inline-flex items-center rounded-xl bg-indigo-600 px-4 py-2.5 text-sm font-semibold text-white shadow-sm hover:bg-indigo-500 transition-colors"
              >
                Create your first task
              </Link>
            </div>
          </div>
        ) : (
          <div className="grid grid-cols-1 gap-6 sm:grid-cols-2 lg:grid-cols-3">
            {tasks.map((task) => {
              const toggleAction = toggleTaskCompleted.bind(null, task.id);
              const deleteAction = deleteTask.bind(null, task.id);

              return (
                <div
                  key={task.id}
                  className={`group relative flex flex-col justify-between rounded-2xl border p-6 shadow-sm transition-all duration-300 hover:-translate-y-0.5 hover:shadow-md ${
                    task.completed
                      ? "bg-zinc-50/60 border-zinc-200 dark:bg-zinc-900/30 dark:border-zinc-800/80"
                      : "bg-white border-zinc-200 dark:bg-zinc-900 dark:border-zinc-800"
                  }`}
                >
                  <div>
                    {/* Status Badge */}
                    <div className="flex items-center justify-between">
                      <span
                        className={`inline-flex items-center rounded-full px-2.5 py-0.5 text-xs font-semibold ${
                          task.completed
                            ? "bg-green-50 text-green-700 dark:bg-green-950/30 dark:text-green-400"
                            : "bg-amber-50 text-amber-700 dark:bg-amber-950/30 dark:text-amber-400"
                        }`}
                      >
                        {task.completed ? (
                          <>
                            <svg className="mr-1 h-3 w-3 text-green-500" fill="currentColor" viewBox="0 0 8 8">
                              <circle cx="4" cy="4" r="3" />
                            </svg>
                            Completed
                          </>
                        ) : (
                          <>
                            <svg className="mr-1 h-3 w-3 text-amber-500" fill="currentColor" viewBox="0 0 8 8">
                              <circle cx="4" cy="4" r="3" />
                            </svg>
                            Pending
                          </>
                        )}
                      </span>
                    </div>

                    {/* Task Title & Desc */}
                    <div className="mt-4">
                      <h3
                        className={`text-lg font-bold leading-6 ${
                          task.completed
                            ? "text-zinc-400 line-through decoration-zinc-400/80 decoration-2"
                            : "text-zinc-900 dark:text-white"
                        }`}
                      >
                        {task.name}
                      </h3>
                      {task.description ? (
                        <p
                          className={`mt-2 text-sm leading-relaxed ${
                            task.completed
                              ? "text-zinc-400 line-through decoration-zinc-400/50"
                              : "text-zinc-600 dark:text-zinc-400"
                          }`}
                        >
                          {task.description}
                        </p>
                      ) : (
                        <p className="mt-2 text-sm italic text-zinc-400 dark:text-zinc-500">
                          No description provided.
                        </p>
                      )}
                    </div>
                  </div>

                  {/* Actions buttons container */}
                  <div className="mt-6 flex items-center justify-between border-t border-zinc-100 pt-4 dark:border-zinc-800">
                    <form action={toggleAction}>
                      <button
                        type="submit"
                        className={`inline-flex items-center gap-1.5 rounded-lg px-2.5 py-1.5 text-xs font-semibold transition-colors ${
                          task.completed
                            ? "bg-zinc-100 text-zinc-700 hover:bg-zinc-200 dark:bg-zinc-800 dark:text-zinc-300 dark:hover:bg-zinc-700"
                            : "bg-green-600 text-white hover:bg-green-500 dark:bg-green-700 dark:hover:bg-green-600"
                        }`}
                      >
                        {task.completed ? (
                          <>
                            <svg
                              xmlns="http://www.w3.org/2000/svg"
                              fill="none"
                              viewBox="0 0 24 24"
                              strokeWidth={2.5}
                              stroke="currentColor"
                              className="h-3.5 w-3.5"
                            >
                              <path
                                strokeLinecap="round"
                                strokeLinejoin="round"
                                d="M9 15L3 9m0 0l6-6M3 9h12a6 6 0 010 12h-3"
                              />
                            </svg>
                            Mark Incomplete
                          </>
                        ) : (
                          <>
                            <svg
                              xmlns="http://www.w3.org/2000/svg"
                              fill="none"
                              viewBox="0 0 24 24"
                              strokeWidth={2.5}
                              stroke="currentColor"
                              className="h-3.5 w-3.5"
                            >
                              <path strokeLinecap="round" strokeLinejoin="round" d="M4.5 12.75l6 6 9-13.5" />
                            </svg>
                            Mark Complete
                          </>
                        )}
                      </button>
                    </form>

                    <div className="flex gap-2">
                      {/* Edit Button */}
                      <Link
                        href={`/tasks/${task.id}`}
                        className="inline-flex items-center gap-1 rounded-lg border border-zinc-200 bg-white p-2 text-zinc-500 shadow-sm transition-colors hover:bg-zinc-50 hover:text-zinc-950 dark:border-zinc-800 dark:bg-zinc-900 dark:text-zinc-400 dark:hover:bg-zinc-800 dark:hover:text-white"
                        title="Edit Task"
                      >
                        <svg
                          xmlns="http://www.w3.org/2000/svg"
                          fill="none"
                          viewBox="0 0 24 24"
                          strokeWidth={2}
                          stroke="currentColor"
                          className="h-4 w-4"
                        >
                          <path
                            strokeLinecap="round"
                            strokeLinejoin="round"
                            d="M16.862 4.487l1.687-1.688a1.875 1.875 0 112.652 2.652L6.832 19.82a4.5 4.5 0 01-1.897 1.13l-2.685.8.8-2.685a4.5 4.5 0 011.13-1.897L16.863 4.487zm0 0L19.5 7.125"
                          />
                        </svg>
                      </Link>

                      {/* Delete Button */}
                      <form action={deleteAction}>
                        <button
                          type="submit"
                          className="inline-flex items-center gap-1 rounded-lg border border-zinc-200 bg-white p-2 text-red-500 shadow-sm transition-colors hover:border-red-100 hover:bg-red-50 hover:text-red-600 dark:border-zinc-800 dark:bg-zinc-900 dark:hover:border-red-950/40 dark:hover:bg-red-950/20"
                          title="Delete Task"
                          onClick={(e) => {
                            if (!confirm("Are you sure you want to delete this task?")) {
                              e.preventDefault();
                            }
                          }}
                        >
                          <svg
                            xmlns="http://www.w3.org/2000/svg"
                            fill="none"
                            viewBox="0 0 24 24"
                            strokeWidth={2}
                            stroke="currentColor"
                            className="h-4 w-4"
                          >
                            <path
                              strokeLinecap="round"
                              strokeLinejoin="round"
                              d="M14.74 9l-.346 9m-4.788 0L9.26 9m9.968-3.21c.342.052.682.107 1.022.166m-1.022-.165L18.16 19.673a2.25 2.25 0 01-2.244 2.077H8.084a2.25 2.25 0 01-2.244-2.077L4.772 5.79m14.456 0a48.108 48.108 0 00-3.478-.397m-12 .562c.34-.059.68-.114 1.022-.165m0 0a48.11 48.11 0 013.478-.397m7.5 0v-.916c0-1.18-.91-2.164-2.09-2.201a51.964 51.964 0 00-3.32 0c-1.18.037-2.09 1.022-2.09 2.201v.916m7.5 0a48.667 48.667 0 00-7.5 0"
                            />
                          </svg>
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
    </div>
  );
}
