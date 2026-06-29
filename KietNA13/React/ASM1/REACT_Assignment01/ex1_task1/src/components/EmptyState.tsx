import { Link } from 'react-router-dom';
import { ROUTES } from '../constants/routes';

export interface EmptyStateProps {
  filter?: string;
}

export const EmptyState = ({ filter }: EmptyStateProps) => {
  const isFiltered = filter !== undefined && filter !== 'all';

  return (
    <div className="flex flex-col items-center justify-center py-20 text-center">
      <svg
        className="h-24 w-24 text-gray-300"
        fill="none"
        viewBox="0 0 24 24"
        stroke="currentColor"
        aria-hidden="true"
      >
        <path
          strokeLinecap="round"
          strokeLinejoin="round"
          strokeWidth={1}
          d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2m-6 9l2 2 4-4"
        />
      </svg>
      <h2 className="mt-4 text-lg font-semibold text-gray-900">
        {isFiltered ? `No ${filter} tasks` : 'No tasks yet'}
      </h2>
      <p className="mt-1 text-sm text-gray-500">
        {isFiltered
          ? `Switch to "All" to see everything, or create a new task.`
          : 'Get started by creating your first task.'}
      </p>
      {!isFiltered && (
        <Link
          to={ROUTES.TASK_NEW}
          className="mt-6 inline-flex items-center rounded-lg bg-indigo-600 px-4 py-2 text-sm font-medium text-white hover:bg-indigo-700 focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-indigo-500 focus-visible:ring-offset-2"
        >
          Create Task
        </Link>
      )}
    </div>
  );
};
