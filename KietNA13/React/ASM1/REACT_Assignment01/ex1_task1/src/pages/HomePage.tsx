import { useMemo } from 'react';
import { Link } from 'react-router-dom';
import { useTaskContext } from '../context/TaskContext';
import { Skeleton } from '../components/Skeleton';
import { ROUTES } from '../constants/routes';

interface StatCardProps {
  label: string;
  value: number;
  colorClass: string;
  isLoading: boolean;
}

const StatCard = ({ label, value, colorClass, isLoading }: StatCardProps) => (
  <div className="rounded-xl border border-gray-200 bg-white p-6 shadow-sm transition-shadow hover:shadow-md">
    <p className="text-sm font-medium text-gray-500">{label}</p>
    {isLoading ? (
      <Skeleton className="mt-2 h-9 w-16" />
    ) : (
      <p className={`mt-2 text-3xl font-bold ${colorClass}`}>{value}</p>
    )}
  </div>
);

const HomePage = () => {
  const { tasks, isLoading } = useTaskContext();

  const stats = useMemo(
    () => ({
      total: tasks.length,
      completed: tasks.filter((t) => t.completed).length,
      pending: tasks.filter((t) => !t.completed).length,
    }),
    [tasks],
  );

  return (
    <main className="mx-auto max-w-7xl px-4 py-8 sm:px-6 lg:px-8">
      <div className="flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">Dashboard</h1>
          <p className="mt-1 text-sm text-gray-500">
            Overview of your task management
          </p>
        </div>
        <Link
          to={ROUTES.TASK_NEW}
          className="inline-flex items-center gap-1 rounded-lg bg-indigo-600 px-4 py-2 text-sm font-medium text-white hover:bg-indigo-700 focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-indigo-500 focus-visible:ring-offset-2"
        >
          + New Task
        </Link>
      </div>

      <div className="mt-8 grid gap-4 sm:grid-cols-3">
        <StatCard
          label="Total Tasks"
          value={stats.total}
          colorClass="text-gray-900"
          isLoading={isLoading}
        />
        <StatCard
          label="Completed"
          value={stats.completed}
          colorClass="text-emerald-600"
          isLoading={isLoading}
        />
        <StatCard
          label="Pending"
          value={stats.pending}
          colorClass="text-amber-600"
          isLoading={isLoading}
        />
      </div>

      <div className="mt-10">
        <h2 className="text-lg font-semibold text-gray-900">Quick Actions</h2>
        <div className="mt-4 flex flex-wrap gap-3">
          <Link
            to={ROUTES.TASKS}
            className="rounded-lg border border-gray-300 bg-white px-4 py-2 text-sm font-medium text-gray-700 hover:bg-gray-50 focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-indigo-500"
          >
            View All Tasks
          </Link>
          <Link
            to={`${ROUTES.TASKS}?filter=pending`}
            className="rounded-lg border border-amber-300 bg-amber-50 px-4 py-2 text-sm font-medium text-amber-700 hover:bg-amber-100 focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-amber-500"
          >
            View Pending
          </Link>
          <Link
            to={`${ROUTES.TASKS}?filter=completed`}
            className="rounded-lg border border-emerald-300 bg-emerald-50 px-4 py-2 text-sm font-medium text-emerald-700 hover:bg-emerald-100 focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-emerald-500"
          >
            View Completed
          </Link>
        </div>
      </div>

      <div className="mt-10 rounded-xl border border-indigo-100 bg-indigo-50 p-6">
        <h2 className="font-semibold text-indigo-900">Getting Started</h2>
        <p className="mt-1 text-sm text-indigo-700">
          Create tasks, mark them complete, and filter by status. All data is
          fetched from JSONPlaceholder and managed locally.
        </p>
      </div>
    </main>
  );
};

export default HomePage;
