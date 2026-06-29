import { getTaskById } from '@/lib/db';
import EditTaskForm from '@/components/EditTaskForm';
import Link from 'next/link';
import { AlertCircle, ArrowLeft } from 'lucide-react';

// Force dynamic rendering to always load fresh task details from JSON file
export const dynamic = 'force-dynamic';

interface TaskDetailPageProps {
  params: Promise<{ id: string }>;
}

export default async function TaskDetailPage({ params }: TaskDetailPageProps) {
  const { id } = await params;
  const task = await getTaskById(id);

  if (!task) {
    return (
      <div className="max-w-xl mx-auto py-12 space-y-6">
        <Link
          href="/tasks"
          className="inline-flex items-center gap-1.5 text-sm text-slate-400 hover:text-slate-200 mb-2 transition-colors group"
        >
          <ArrowLeft className="h-4 w-4 group-hover:-translate-x-0.5 transition-transform" />
          <span>Back to workspace</span>
        </Link>
        <div className="bg-slate-900/40 border border-red-500/20 p-8 rounded-3xl text-center space-y-4">
          <div className="mx-auto h-12 w-12 rounded-full bg-red-950/30 flex items-center justify-center text-red-400">
            <AlertCircle className="h-6 w-6" />
          </div>
          <h2 className="text-xl font-bold text-slate-200">Task Not Found</h2>
          <p className="text-sm text-slate-400 max-w-sm mx-auto">
            The task with ID <code className="text-xs bg-slate-950 px-1.5 py-0.5 rounded font-mono text-slate-300">{id}</code> could not be found or may have been deleted.
          </p>
          <div className="pt-2">
            <Link
              href="/tasks"
              className="inline-flex items-center gap-1.5 rounded-xl bg-slate-800 px-4 py-2 text-xs font-semibold text-slate-300 hover:bg-slate-700 transition-all"
            >
              Go to Workspace
            </Link>
          </div>
        </div>
      </div>
    );
  }

  return <EditTaskForm task={task} />;
}
