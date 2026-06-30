import Link from "next/link";
import { getTasks } from "@/data/db";
import EditTaskForm from "./EditTaskForm";

export const revalidate = 0; // Disable static rendering

interface EditTaskPageProps {
  params: Promise<{ id: string }>;
}

export default async function EditTaskPage({ params }: EditTaskPageProps) {
  const { id } = await params;
  const tasks = getTasks();
  const task = tasks.find((t) => t.id === id);

  if (!task) {
    return (
      <div className="max-w-xl mx-auto py-16 px-4 text-center">
        <div className="p-4 bg-zinc-900 border border-zinc-800 rounded-xl max-w-sm mx-auto shadow-md">
          <h2 className="text-lg font-bold text-white">Task not found</h2>
          <p className="mt-2 text-sm text-zinc-400">
            The task you are trying to edit does not exist or has been deleted.
          </p>
          <Link
            href="/tasks"
            className="mt-5 inline-flex items-center justify-center rounded-lg bg-teal-500 px-4 py-2 text-sm font-semibold text-zinc-950 shadow-sm hover:bg-teal-400 transition-colors"
          >
            Go to Tasks List
          </Link>
        </div>
      </div>
    );
  }

  return (
    <div className="max-w-xl mx-auto space-y-6 py-4">
      <div>
        <h1 className="text-2xl font-bold tracking-tight text-white sm:text-3xl">Edit Task</h1>
        <p className="mt-1 text-sm text-zinc-400">
          Modify the fields below to update the task details.
        </p>
      </div>

      <div className="bg-zinc-900 border border-zinc-800 rounded-xl p-6 shadow-md">
        <EditTaskForm task={task} />
      </div>
    </div>
  );
}
