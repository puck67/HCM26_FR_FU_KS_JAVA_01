import Link from "next/link";
import { getTasks } from "../lib/tasks";

export const dynamic = "force-dynamic";

export default async function Home() {
  const tasks = await getTasks();
  const total = tasks.length;
  const completed = tasks.filter((t) => t.completed).length;
  const pending = total - completed;
  const percentage = total > 0 ? Math.round((completed / total) * 100) : 0;

  // Take the 3 most recent tasks
  const recentTasks = [...tasks].reverse().slice(0, 3);

  return (
    <div className="mx-auto w-full max-w-7xl px-4 py-10 sm:px-6 lg:px-8">
      {/* Hero Welcome */}
      <div className="relative overflow-hidden rounded-3xl bg-gradient-to-tr from-zinc-900 via-zinc-800 to-indigo-950 px-6 py-12 text-white shadow-xl dark:from-zinc-950 dark:via-zinc-900 dark:to-indigo-950/60 sm:px-12 sm:py-16 md:px-16">
        <div className="relative z-10 max-w-2xl">
          <span className="inline-flex items-center rounded-full bg-indigo-500/10 px-3 py-1 text-xs font-medium text-indigo-300 ring-1 ring-inset ring-indigo-500/25">
            Overview Dashboard
          </span>
          <h1 className="mt-4 text-3xl font-extrabold tracking-tight sm:text-5xl">
            Stay organized, <br />
            <span className="bg-gradient-to-r from-indigo-300 to-violet-300 bg-clip-text text-transparent">
              accomplish more.
            </span>
          </h1>
          <p className="mt-4 text-base text-zinc-300 sm:text-lg">
            FlowTask helps you track, manage, and complete your tasks with simple flow controls, instant server updates, and a clean user experience.
          </p>
          <div className="mt-8 flex flex-wrap gap-4">
            <Link
              href="/tasks/new"
              className="inline-flex items-center justify-center rounded-xl bg-white px-5 py-3 text-sm font-semibold text-zinc-900 shadow-sm transition-all duration-200 hover:bg-indigo-50 hover:text-indigo-950 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-white"
            >
              Add New Task
            </Link>
            <Link
              href="/tasks"
              className="inline-flex items-center justify-center rounded-xl border border-zinc-700 bg-zinc-900/30 px-5 py-3 text-sm font-semibold text-zinc-100 backdrop-blur-sm transition-all duration-200 hover:bg-zinc-800/50 hover:text-white"
            >
              View Task List
            </Link>
          </div>
        </div>

        {/* Decorative Grid Lines */}
        <div className="absolute inset-0 opacity-10 [mask-image:radial-gradient(ellipse_at_center,white,transparent)]">
          <svg className="h-full w-full" width="100%" height="100%">
            <defs>
              <pattern
                id="grid"
                width="40"
                height="40"
                patternUnits="userSpaceOnUse"
              >
                <path
                  d="M 40 0 L 0 0 0 40"
                  fill="none"
                  stroke="currentColor"
                  strokeWidth="1"
                />
              </pattern>
            </defs>
            <rect width="100%" height="100%" fill="url(#grid)" />
          </svg>
        </div>
      </div>

      {/* Stats Cards */}
      <div className="mt-10 grid grid-cols-1 gap-6 sm:grid-cols-2 lg:grid-cols-4">
        {/* Total Tasks */}
        <div className="relative overflow-hidden rounded-2xl border border-zinc-200 bg-white p-6 shadow-sm transition-shadow hover:shadow-md dark:border-zinc-800 dark:bg-zinc-900">
          <div className="flex items-center gap-4">
            <div className="flex h-12 w-12 items-center justify-center rounded-xl bg-blue-50 text-blue-600 dark:bg-blue-950/50 dark:text-blue-400">
              <svg
                xmlns="http://www.w3.org/2000/svg"
                fill="none"
                viewBox="0 0 24 24"
                strokeWidth={2}
                stroke="currentColor"
                className="h-6 w-6"
              >
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  d="M9 12h3.75M9 15h3.75M9 18h3.75m3 .75H18a2.25 2.25 0 002.25-2.25V6.108c0-1.135-.845-2.098-1.976-2.192a48.424 48.424 0 00-1.123-.08m-5.801 0c-.065.21-.1.433-.1.664 0 .414.336.75.75.75h4.5a.75.75 0 00.75-.75 2.25 2.25 0 00-.1-.664m-5.8 0A2.251 2.251 0 0113.5 2.25H15c1.03 0 1.9.693 2.166 1.638m-7.377 2.24c-.09-.328-.21-.643-.36-.944m-1.74 1.74a48.455 48.455 0 01-1.123.08m0 0A2.25 2.25 0 003 6.108V17.25A2.25 2.25 0 005.25 19.5h1.382"
                />
              </svg>
            </div>
            <div>
              <p className="text-sm font-medium text-zinc-500 dark:text-zinc-400">
                Total Tasks
              </p>
              <p className="text-2xl font-bold text-zinc-900 dark:text-white">
                {total}
              </p>
            </div>
          </div>
        </div>

        {/* Completed Tasks */}
        <div className="relative overflow-hidden rounded-2xl border border-zinc-200 bg-white p-6 shadow-sm transition-shadow hover:shadow-md dark:border-zinc-800 dark:bg-zinc-900">
          <div className="flex items-center gap-4">
            <div className="flex h-12 w-12 items-center justify-center rounded-xl bg-green-50 text-green-600 dark:bg-green-950/50 dark:text-green-400">
              <svg
                xmlns="http://www.w3.org/2000/svg"
                fill="none"
                viewBox="0 0 24 24"
                strokeWidth={2}
                stroke="currentColor"
                className="h-6 w-6"
              >
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  d="M9 12.75L11.25 15 15 9.75M21 12a9 9 0 11-18 0 9 9 0 0118 0z"
                />
              </svg>
            </div>
            <div>
              <p className="text-sm font-medium text-zinc-500 dark:text-zinc-400">
                Completed
              </p>
              <p className="text-2xl font-bold text-zinc-900 dark:text-white">
                {completed}
              </p>
            </div>
          </div>
        </div>

        {/* Pending Tasks */}
        <div className="relative overflow-hidden rounded-2xl border border-zinc-200 bg-white p-6 shadow-sm transition-shadow hover:shadow-md dark:border-zinc-800 dark:bg-zinc-900">
          <div className="flex items-center gap-4">
            <div className="flex h-12 w-12 items-center justify-center rounded-xl bg-amber-50 text-amber-600 dark:bg-amber-950/50 dark:text-amber-400">
              <svg
                xmlns="http://www.w3.org/2000/svg"
                fill="none"
                viewBox="0 0 24 24"
                strokeWidth={2}
                stroke="currentColor"
                className="h-6 w-6"
              >
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  d="M12 6v6h4.5m4.5 0a9 9 0 11-18 0 9 9 0 0118 0z"
                />
              </svg>
            </div>
            <div>
              <p className="text-sm font-medium text-zinc-500 dark:text-zinc-400">
                Pending
              </p>
              <p className="text-2xl font-bold text-zinc-900 dark:text-white">
                {pending}
              </p>
            </div>
          </div>
        </div>

        {/* Completion Rate */}
        <div className="relative overflow-hidden rounded-2xl border border-zinc-200 bg-white p-6 shadow-sm transition-shadow hover:shadow-md dark:border-zinc-800 dark:bg-zinc-900">
          <div className="flex items-center gap-4">
            <div className="flex h-12 w-12 items-center justify-center rounded-xl bg-purple-50 text-purple-600 dark:bg-purple-950/50 dark:text-purple-400">
              <svg
                xmlns="http://www.w3.org/2000/svg"
                fill="none"
                viewBox="0 0 24 24"
                strokeWidth={2}
                stroke="currentColor"
                className="h-6 w-6"
              >
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  d="M10.5 6a7.5 7.5 0 107.5 7.5h-7.5V6z"
                />
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  d="M13.5 10.5H21A7.5 7.5 0 0013.5 3v7.5z"
                />
              </svg>
            </div>
            <div className="w-full">
              <p className="text-sm font-medium text-zinc-500 dark:text-zinc-400">
                Completion Rate
              </p>
              <div className="flex items-center gap-2">
                <span className="text-2xl font-bold text-zinc-900 dark:text-white">
                  {percentage}%
                </span>
              </div>
              <div className="mt-2 h-1.5 w-full rounded-full bg-zinc-100 dark:bg-zinc-800">
                <div
                  className="h-1.5 rounded-full bg-gradient-to-r from-violet-500 to-indigo-500 transition-all duration-500"
                  style={{ width: `${percentage}%` }}
                />
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* Main Content Layout */}
      <div className="mt-12 grid grid-cols-1 gap-8 lg:grid-cols-3">
        {/* Recent Tasks List */}
        <div className="lg:col-span-2 space-y-6">
          <div className="flex items-center justify-between">
            <h2 className="text-lg font-bold tracking-tight text-zinc-900 dark:text-white">
              Recently Added Tasks
            </h2>
            <Link
              href="/tasks"
              className="text-sm font-semibold text-indigo-600 hover:text-indigo-500 dark:text-indigo-400"
            >
              View all tasks &rarr;
            </Link>
          </div>

          <div className="space-y-4">
            {recentTasks.length === 0 ? (
              <div className="rounded-2xl border border-dashed border-zinc-200 bg-white px-6 py-12 text-center dark:border-zinc-800 dark:bg-zinc-900">
                <svg
                  className="mx-auto h-12 w-12 text-zinc-400"
                  fill="none"
                  viewBox="0 0 24 24"
                  stroke="currentColor"
                  aria-hidden="true"
                >
                  <path
                    vectorEffect="non-scaling-stroke"
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    strokeWidth={2}
                    d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2"
                  />
                </svg>
                <h3 className="mt-2 text-sm font-semibold text-zinc-900 dark:text-white">
                  No tasks found
                </h3>
                <p className="mt-1 text-sm text-zinc-500 dark:text-zinc-400">
                  Get started by creating a new task.
                </p>
                <div className="mt-6">
                  <Link
                    href="/tasks/new"
                    className="inline-flex items-center rounded-xl bg-indigo-600 px-3 py-2 text-sm font-semibold text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600"
                  >
                    Add new task
                  </Link>
                </div>
              </div>
            ) : (
              recentTasks.map((task) => (
                <div
                  key={task.id}
                  className={`group relative flex items-center justify-between rounded-2xl border border-zinc-200 bg-white p-5 shadow-sm transition-all hover:shadow-md dark:border-zinc-800 dark:bg-zinc-900 ${
                    task.completed ? "opacity-75" : ""
                  }`}
                >
                  <div className="min-w-0 flex-1 pr-4">
                    <div className="flex items-center gap-3">
                      <span
                        className={`inline-block h-2.5 w-2.5 rounded-full shrink-0 ${
                          task.completed ? "bg-green-500" : "bg-indigo-500"
                        }`}
                      />
                      <h4
                        className={`text-base font-semibold truncate ${
                          task.completed
                            ? "text-zinc-400 line-through decoration-zinc-400"
                            : "text-zinc-900 dark:text-white"
                        }`}
                      >
                        {task.name}
                      </h4>
                    </div>
                    {task.description && (
                      <p
                        className={`mt-1 text-sm pl-5 truncate ${
                          task.completed
                            ? "text-zinc-400 line-through"
                            : "text-zinc-500 dark:text-zinc-400"
                        }`}
                      >
                        {task.description}
                      </p>
                    )}
                  </div>
                  <div>
                    <Link
                      href={`/tasks/${task.id}`}
                      className="inline-flex items-center justify-center rounded-lg border border-zinc-200 bg-white p-2 text-zinc-500 shadow-sm transition-colors hover:bg-zinc-50 hover:text-zinc-900 dark:border-zinc-800 dark:bg-zinc-900 dark:text-zinc-400 dark:hover:bg-zinc-800 dark:hover:text-white"
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
                  </div>
                </div>
              ))
            )}
          </div>
        </div>

        {/* Tips & Quick Guide */}
        <div className="space-y-6">
          <h2 className="text-lg font-bold tracking-tight text-zinc-900 dark:text-white">
            Quick Actions & Info
          </h2>
          <div className="rounded-2xl border border-zinc-200 bg-white p-6 shadow-sm dark:border-zinc-800 dark:bg-zinc-900">
            <h3 className="text-sm font-semibold text-zinc-900 dark:text-white">
              Technology Stack Used
            </h3>
            <ul className="mt-4 space-y-3 text-sm text-zinc-600 dark:text-zinc-400">
              <li className="flex items-center gap-2">
                <span className="flex h-1.5 w-1.5 rounded-full bg-indigo-500" />
                <span>Next.js App Router</span>
              </li>
              <li className="flex items-center gap-2">
                <span className="flex h-1.5 w-1.5 rounded-full bg-indigo-500" />
                <span>Server Components & Actions</span>
              </li>
              <li className="flex items-center gap-2">
                <span className="flex h-1.5 w-1.5 rounded-full bg-indigo-500" />
                <span>Formik Validation</span>
              </li>
              <li className="flex items-center gap-2">
                <span className="flex h-1.5 w-1.5 rounded-full bg-indigo-500" />
                <span>Tailwind CSS Styling</span>
              </li>
              <li className="flex items-center gap-2">
                <span className="flex h-1.5 w-1.5 rounded-full bg-indigo-500" />
                <span>File-based Database (JSON)</span>
              </li>
            </ul>

            <hr className="my-5 border-zinc-200 dark:border-zinc-800" />

            <h3 className="text-sm font-semibold text-zinc-900 dark:text-white">
              Need to add more?
            </h3>
            <p className="mt-2 text-xs text-zinc-500 dark:text-zinc-400 leading-relaxed">
              Create a new task, fill in a name up to 40 characters and details up to 200. Toggle task completion or delete tasks directly in the All Tasks tab.
            </p>
          </div>
        </div>
      </div>
    </div>
  );
}
