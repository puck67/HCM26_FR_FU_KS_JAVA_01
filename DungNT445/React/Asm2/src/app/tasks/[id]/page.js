import Link from "next/link";
import { getTasks } from "../../../lib/tasks";
import EditTaskForm from "./EditTaskForm";

export const dynamic = "force-dynamic";

export default async function TaskDetailPage({ params }) {
  const { id } = await params;
  const tasks = await getTasks();
  const task = tasks.find((t) => t.id === id);

  if (!task) {
    return (
      <div className="mx-auto w-full max-w-2xl px-4 py-16 sm:px-6 lg:px-8">
        <div className="rounded-3xl border border-red-200 bg-red-50/50 p-8 text-center dark:border-red-950/30 dark:bg-red-950/10">
          <svg
            className="mx-auto h-12 w-12 text-red-500"
            fill="none"
            viewBox="0 0 24 24"
            stroke="currentColor"
            aria-hidden="true"
          >
            <path
              strokeLinecap="round"
              strokeLinejoin="round"
              strokeWidth={2}
              d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z"
            />
          </svg>
          <h3 className="mt-4 text-lg font-bold text-red-800 dark:text-red-400">
            Task Not Found
          </h3>
          <p className="mt-2 text-sm text-red-700/80 dark:text-red-400/80">
            The task you are trying to edit does not exist or has been deleted.
          </p>
          <div className="mt-6">
            <Link
              href="/tasks"
              className="inline-flex items-center gap-1.5 rounded-xl bg-red-600 px-4 py-2.5 text-sm font-semibold text-white shadow-sm hover:bg-red-500 transition-colors"
            >
              Back to All Tasks
            </Link>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="mx-auto w-full max-w-2xl px-4 py-12 sm:px-6 lg:px-8">
      {/* Back link */}
      <div className="mb-6">
        <Link
          href="/tasks"
          className="inline-flex items-center gap-1.5 text-sm font-semibold text-zinc-500 hover:text-zinc-800 dark:text-zinc-400 dark:hover:text-zinc-100"
        >
          <svg
            xmlns="http://www.w3.org/2000/svg"
            fill="none"
            viewBox="0 0 24 24"
            strokeWidth={2.5}
            stroke="currentColor"
            className="h-4 w-4"
          >
            <path strokeLinecap="round" strokeLinejoin="round" d="M10.5 19.5L3 12m0 0l7.5-7.5M3 12h18" />
          </svg>
          Back to list
        </Link>
      </div>

      {/* Edit Form */}
      <EditTaskForm task={task} />
    </div>
  );
}
