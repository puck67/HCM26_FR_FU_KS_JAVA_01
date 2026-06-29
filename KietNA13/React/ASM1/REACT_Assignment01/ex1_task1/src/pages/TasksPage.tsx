import { useMemo, useCallback } from 'react';
import { Link, useSearchParams } from 'react-router-dom';
import { useTaskContext } from '../context/TaskContext';
import { useTaskMutation } from '../hooks/useTaskMutation';
import { TaskList } from '../components/TaskList';
import { cn } from '../utils/cn';
import { ROUTES } from '../constants/routes';
import type { TaskFilter } from '../types/task';

const FILTER_OPTIONS: { value: TaskFilter; label: string }[] = [
  { value: 'all', label: 'All' },
  { value: 'completed', label: 'Completed' },
  { value: 'pending', label: 'Pending' },
];

const TasksPage = () => {
  const [searchParams, setSearchParams] = useSearchParams();
  const filter = (searchParams.get('filter') ?? 'all') as TaskFilter;

  const { tasks, isLoading } = useTaskContext();
  const { deleteMutation } = useTaskMutation();

  const filteredTasks = useMemo(() => {
    if (filter === 'completed') return tasks.filter((t) => t.completed);
    if (filter === 'pending') return tasks.filter((t) => !t.completed);
    return tasks;
  }, [tasks, filter]);

  const handleFilterChange = useCallback(
    (value: TaskFilter) => {
      setSearchParams(value === 'all' ? {} : { filter: value });
    },
    [setSearchParams],
  );

  const handleDelete = useCallback(
    (id: number) => {
      deleteMutation.mutate(id);
    },
    [deleteMutation],
  );

  const deletingId =
    deleteMutation.isPending ? (deleteMutation.variables ?? null) : null;

  return (
    <main className="mx-auto max-w-7xl px-4 py-8 sm:px-6 lg:px-8">
      <div className="flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">Tasks</h1>
          <p className="mt-1 text-sm text-gray-500">
            Manage and track your tasks
          </p>
        </div>
        <Link
          to={ROUTES.TASK_NEW}
          className="inline-flex items-center gap-1 rounded-lg bg-indigo-600 px-4 py-2 text-sm font-medium text-white hover:bg-indigo-700 focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-indigo-500 focus-visible:ring-offset-2"
        >
          + New Task
        </Link>
      </div>

      <div
        className="mt-6 flex gap-1 rounded-lg border border-gray-200 bg-gray-50 p-1 w-fit"
        role="tablist"
        aria-label="Filter tasks"
      >
        {FILTER_OPTIONS.map(({ value, label }) => (
          <button
            key={value}
            role="tab"
            aria-selected={filter === value}
            onClick={() => handleFilterChange(value)}
            className={cn(
              'rounded-md px-4 py-1.5 text-sm font-medium transition-colors',
              'focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-indigo-500',
              filter === value
                ? 'bg-white text-indigo-600 shadow-sm'
                : 'text-gray-500 hover:text-gray-700',
            )}
          >
            {label}
          </button>
        ))}
      </div>

      <div className="mt-6">
        <TaskList
          tasks={filteredTasks}
          isLoading={isLoading}
          onDelete={handleDelete}
          isDeletingId={deletingId}
          filter={filter}
        />
      </div>
    </main>
  );
};

export default TasksPage;
