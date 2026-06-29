import { Link } from 'react-router-dom';
import { Task } from '../types/task';

interface TaskCardProps {
  task: Task;
  onDelete: (id: number) => void;
}

const statusConfig = {
  todo: { label: 'To Do', className: 'bg-gray-100 text-gray-700' },
  'in-progress': { label: 'In Progress', className: 'bg-yellow-100 text-yellow-700' },
  done: { label: 'Done', className: 'bg-green-100 text-green-700' },
};

export default function TaskCard({ task, onDelete }: TaskCardProps) {
  const status = statusConfig[task.status];

  return (
    <div className="bg-white rounded-xl shadow-sm border border-gray-100 p-5 flex flex-col gap-3 hover:shadow-md transition-shadow">
      <div className="flex items-start justify-between gap-2">
        <h3 className="font-semibold text-gray-800 text-sm leading-tight line-clamp-2 flex-1">
          {task.name}
        </h3>
        <span className={`text-xs px-2 py-1 rounded-full font-medium whitespace-nowrap ${status.className}`}>
          {status.label}
        </span>
      </div>

      {task.description && (
        <p className="text-gray-500 text-xs line-clamp-2">{task.description}</p>
      )}

      <div className="flex items-center justify-between pt-1 border-t border-gray-50">
        <span className="text-xs text-gray-400">ID: #{task.id}</span>
        <div className="flex gap-2">
          <Link
            to={`/tasks/${task.id}`}
            className="text-xs px-3 py-1.5 rounded-md bg-indigo-50 text-indigo-600 hover:bg-indigo-100 font-medium transition-colors"
          >
            View
          </Link>
          <Link
            to={`/tasks/${task.id}/edit`}
            className="text-xs px-3 py-1.5 rounded-md bg-amber-50 text-amber-600 hover:bg-amber-100 font-medium transition-colors"
          >
            Edit
          </Link>
          <button
            onClick={() => onDelete(task.id)}
            className="text-xs px-3 py-1.5 rounded-md bg-red-50 text-red-600 hover:bg-red-100 font-medium transition-colors"
          >
            Delete
          </button>
        </div>
      </div>
    </div>
  );
}
