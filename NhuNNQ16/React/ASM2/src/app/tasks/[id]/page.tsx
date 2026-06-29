import { notFound } from 'next/navigation';
import { getTaskById } from '@/lib/store';
import { updateTask } from '@/actions/taskActions';
import TaskFormClient from '@/components/TaskFormClient';
import Link from 'next/link';

interface PageProps {
  params: { id: string };
}

export default function TaskDetailPage({ params }: PageProps) {
  const task = getTaskById(params.id);

  if (!task) {
    notFound();
  }

  const updateTaskWithId = updateTask.bind(null, task.id);

  return (
    <div className="max-w-xl mx-auto space-y-6">
      {/* Breadcrumbs */}
      <div className="flex items-center gap-2 text-xs font-semibold uppercase tracking-wider text-slate-500">
        <Link href="/tasks" className="hover:text-indigo-600 transition-colors">
          Tasks Workspace
        </Link>
        <span>/</span>
        <span className="text-slate-500">Task Detail</span>
        <span>/</span>
        <span className="text-slate-700 font-mono font-bold">#{task.id}</span>
      </div>

      {/* Current Task Status Overview */}
      <div className={`p-6 rounded-2xl border bg-white border-slate-200 shadow-sm flex items-center justify-between ${
        task.completed ? 'border-l-4 border-l-emerald-500' : 'border-l-4 border-l-indigo-600'
      }`}>
        <div className="space-y-1">
          <p className="text-[10px] uppercase font-bold tracking-wider text-slate-400">Current Status</p>
          <h2 className="text-xl font-extrabold text-slate-900">{task.name}</h2>
          <p className="text-xs text-slate-500 font-medium">
            Created: {new Date(task.createdAt).toLocaleString('en-US', {
              month: 'short',
              day: 'numeric',
              year: 'numeric',
              hour: '2-digit',
              minute: '2-digit'
            })}
          </p>
        </div>
        <span
          className={`text-xs px-3 py-1 rounded-full font-bold uppercase tracking-wider ${
            task.completed
              ? 'bg-emerald-50 text-emerald-700 border border-emerald-100'
              : 'bg-indigo-50 text-indigo-700 border border-indigo-100'
          }`}
        >
          {task.completed ? 'Completed' : 'Active'}
        </span>
      </div>

      {/* Edit Form Card */}
      <div className="bg-white border border-slate-200 rounded-2xl p-6 md:p-8 shadow-sm">
        <div className="mb-6 pb-4 border-b border-slate-200">
          <h3 className="text-lg font-extrabold text-slate-900 tracking-tight">Edit Task Details</h3>
          <p className="text-slate-500 text-xs mt-1">Modify task name or description below. Click Save to execute.</p>
        </div>

        <TaskFormClient
          initialValues={{ name: task.name, description: task.description }}
          action={updateTaskWithId}
          submitLabel="Save Changes"
        />
      </div>
    </div>
  );

}
