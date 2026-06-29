import { createTask } from '@/actions/taskActions';
import TaskFormClient from '@/components/TaskFormClient';
import Link from 'next/link';

export default function NewTaskPage() {
  return (
    <div className="max-w-xl mx-auto space-y-6">
      {/* Breadcrumb Navigation */}
      <div className="flex items-center gap-2 text-xs font-semibold uppercase tracking-wider text-slate-500">
        <Link href="/tasks" className="hover:text-indigo-600 transition-colors">
          Tasks Workspace
        </Link>
        <span>/</span>
        <span className="text-slate-700 font-bold">New Task</span>
      </div>

      {/* Main card */}
      <div className="bg-white border border-slate-200 rounded-2xl p-6 md:p-8 shadow-sm">
        <div className="mb-6 pb-4 border-b border-slate-200">
          <h1 className="text-2xl font-extrabold text-slate-900 tracking-tight">Create a New Task</h1>
          <p className="text-slate-500 text-xs mt-1">Add details below to deploy a new item to your workspace.</p>
        </div>

        <TaskFormClient
          action={createTask}
          submitLabel="Create Task"
        />
      </div>
    </div>
  );
}

