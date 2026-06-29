import Link from 'next/link';
import { Plus, ListTodo } from 'lucide-react';
import { getTasks } from '@/lib/db';
import TaskList from '@/components/TaskList';

// Ensure the page is dynamically rendered and never cached statically
export const dynamic = 'force-dynamic';

export default async function TasksPage() {
  const tasks = await getTasks();

  // Sort tasks: pending first, then newest first
  const sortedTasks = [...tasks].sort((a, b) => {
    if (a.completed !== b.completed) {
      return a.completed ? 1 : -1;
    }
    return new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime();
  });

  return (
    <div className="space-y-8 py-4">
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div className="space-y-1">
          <h1 className="text-3xl font-extrabold tracking-tight text-white flex items-center gap-2.5">
            <ListTodo className="h-8 w-8 text-indigo-400" />
            <span>Tasks Workspace</span>
          </h1>
          <p className="text-sm text-slate-400">
            Create, manage, and track your development milestones in real time.
          </p>
        </div>
        <Link
          href="/tasks/new"
          className="flex items-center justify-center gap-2 rounded-xl bg-indigo-600 px-5 py-3 text-sm font-semibold text-white shadow-md hover:bg-indigo-500 transition-all hover:scale-102 self-start sm:self-center"
        >
          <Plus className="h-4 w-4" />
          <span>New Task</span>
        </Link>
      </div>

      {/* Main Task List Component */}
      <TaskList initialTasks={sortedTasks} />
    </div>
  );
}
