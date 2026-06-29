import Link from 'next/link';
import { store } from '@/lib/store';
import { ROUTES } from '@/constants/routes';
import TaskRow from '@/components/TaskRow';
import EmptyState from '@/components/EmptyState';

// Force dynamic so the page always re-renders from store after mutations
export const dynamic = 'force-dynamic';

export default function TasksPage() {
  const tasks = Array.from(store.values()).sort(
    (a, b) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime(),
  );

  return (
    <div className="flex flex-col gap-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl sm:text-3xl font-bold text-white">All tasks</h1>
          <p className="text-slate-400 text-sm mt-1">
            {tasks.length} task{tasks.length !== 1 ? 's' : ''} total
          </p>
        </div>
        <Link
          href={ROUTES.NEW_TASK}
          className="px-4 py-2 bg-indigo-600 hover:bg-indigo-500 text-white font-semibold rounded-xl text-sm transition-all"
        >
          + New task
        </Link>
      </div>

      {/* Task list */}
      {tasks.length === 0 ? (
        <EmptyState message="No tasks yet" />
      ) : (
        <div className="flex flex-col gap-3">
          {tasks.map((task) => (
            <TaskRow key={task.id} task={task} />
          ))}
        </div>
      )}
    </div>
  );
}
