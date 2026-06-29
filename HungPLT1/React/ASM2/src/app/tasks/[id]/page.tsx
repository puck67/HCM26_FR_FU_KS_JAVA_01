import { tasksRepo } from "@/lib/tasksRepo";
import { EditTaskForm } from "./EditTaskForm";
import Link from "next/link";
import { ArrowLeft } from "lucide-react";

export const dynamic = "force-dynamic";

interface PageProps {
  params: Promise<{ id: string }>;
}

export default async function TaskDetailPage({ params }: PageProps) {
  const { id } = await params;
  const task = await tasksRepo.getById(id);

  if (!task) {
    return (
      <div className="flex-1 bg-slate-950 py-12 px-4 sm:px-6 lg:px-8">
        <div className="max-w-md mx-auto space-y-6 text-center">
          <div className="bg-slate-900 border border-slate-800 rounded-2xl p-8 space-y-4">
            <h1 className="text-2xl font-bold text-slate-100">Task Not Found</h1>
            <p className="text-sm text-slate-400">The task you are looking for does not exist or has been deleted.</p>
            <Link
              href="/tasks"
              className="inline-flex items-center gap-2 px-4 py-2 rounded-xl bg-violet-600 hover:bg-violet-700 text-sm font-semibold text-white transition duration-150"
            >
              <ArrowLeft size={16} />
              Return to Tasks
            </Link>
          </div>
        </div>
      </div>
    );
  }

  return <EditTaskForm task={task} />;
}
