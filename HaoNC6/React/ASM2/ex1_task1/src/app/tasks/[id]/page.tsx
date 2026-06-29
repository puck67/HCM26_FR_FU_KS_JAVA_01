import Link from 'next/link';
import { getTasks } from '../../../lib/tasks';
import TaskEditForm from '../../../components/TaskEditForm';
import { ArrowLeft, AlertTriangle } from 'lucide-react';

export const dynamic = 'force-dynamic';

interface PageProps {
  params: Promise<{
    id: string;
  }>;
}

export default async function TaskDetailPage({ params }: PageProps) {
  const { id } = await params;

  // Retrieve tasks from storage
  const tasks = getTasks();
  const task = tasks.find((t) => t.id === id);

  // Handle task not found case (404)
  if (!task) {
    return (
      <div className="max-w-xl mx-auto space-y-6">
        <Link
          href="/tasks"
          className="inline-flex items-center gap-2 text-xs font-semibold text-gray-400 hover:text-white transition-all"
        >
          <ArrowLeft className="w-3.5 h-3.5" /> Back to Dashboard
        </Link>

        <div className="glass-panel rounded-3xl p-8 text-center space-y-4">
          <AlertTriangle className="w-12 h-12 text-rose-500 mx-auto" />
          <h1 className="text-xl font-bold text-white">Task Not Found</h1>
          <p className="text-sm text-gray-400 max-w-sm mx-auto">
            The task with ID &ldquo;{id}&rdquo; could not be found or has been deleted.
          </p>
          <div className="pt-2">
            <Link
              href="/tasks"
              className="inline-flex items-center justify-center px-5 py-2.5 rounded-xl bg-indigo-600 hover:bg-indigo-500 text-white font-semibold shadow-lg hover:shadow-indigo-500/20 transition-all text-sm"
            >
              Return to Dashboard
            </Link>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="max-w-xl mx-auto space-y-6">
      {/* Back Link */}
      <Link
        href="/tasks"
        className="inline-flex items-center gap-2 text-xs font-semibold text-gray-400 hover:text-white transition-all"
      >
        <ArrowLeft className="w-3.5 h-3.5" /> Back to Dashboard
      </Link>

      {/* Form Container */}
      <TaskEditForm task={task} />
    </div>
  );
}
