import { useEffect, useState } from 'react';
import { useParams, Link, useNavigate } from 'react-router-dom';
import { Task } from '../types/task';
import { fetchTaskById } from '../services/taskService';
import LoadingSpinner from '../components/LoadingSpinner';
import ErrorDisplay from '../components/ErrorDisplay';

const statusConfig = {
  todo: { label: 'To Do', className: 'bg-gray-100 text-gray-700 border-gray-200' },
  'in-progress': { label: 'In Progress', className: 'bg-yellow-100 text-yellow-700 border-yellow-200' },
  done: { label: 'Done', className: 'bg-green-100 text-green-700 border-green-200' },
};

export default function TaskDetailPage() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [task, setTask] = useState<Task | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!id) return;
    const load = async () => {
      setLoading(true);
      setError(null);
      try {
        const data = await fetchTaskById(Number(id));
        setTask(data);
      } catch (err) {
        setError(err instanceof Error ? err.message : 'Failed to load task');
      } finally {
        setLoading(false);
      }
    };
    load();
  }, [id]);

  if (loading) return <LoadingSpinner message="Loading task details..." />;
  if (error) return <ErrorDisplay message={error} onRetry={() => navigate(0)} />;
  if (!task) return null;

  const status = statusConfig[task.status];

  return (
    <div className="min-h-screen bg-gray-50">
      <div className="max-w-2xl mx-auto px-4 py-8">
        {/* Breadcrumb */}
        <nav className="flex items-center gap-2 text-sm text-gray-500 mb-6">
          <Link to="/tasks" className="hover:text-indigo-600 transition-colors">Tasks</Link>
          <span>/</span>
          <span className="text-gray-800 font-medium">Task #{task.id}</span>
        </nav>

        {/* Card */}
        <div className="bg-white rounded-2xl shadow-sm border border-gray-100 overflow-hidden">
          <div className="p-6 border-b border-gray-50">
            <div className="flex items-start justify-between gap-3">
              <h1 className="text-xl font-bold text-gray-900 leading-tight flex-1">{task.name}</h1>
              <span className={`text-xs px-3 py-1 rounded-full font-medium border ${status.className}`}>
                {status.label}
              </span>
            </div>
          </div>

          <div className="p-6 space-y-4">
            {/* Description */}
            <div>
              <p className="text-xs font-semibold uppercase tracking-widest text-gray-400 mb-1">
                Description
              </p>
              <p className="text-gray-600 text-sm leading-relaxed">
                {task.description || <span className="italic text-gray-400">No description provided.</span>}
              </p>
            </div>

            {/* Meta */}
            <div className="grid grid-cols-2 gap-4 pt-4 border-t border-gray-50">
              <div>
                <p className="text-xs font-semibold uppercase tracking-widest text-gray-400 mb-1">Task ID</p>
                <p className="text-sm font-mono text-gray-700">#{task.id}</p>
              </div>
              <div>
                <p className="text-xs font-semibold uppercase tracking-widest text-gray-400 mb-1">Assigned User</p>
                <p className="text-sm text-gray-700">User #{task.userId}</p>
              </div>
              <div>
                <p className="text-xs font-semibold uppercase tracking-widest text-gray-400 mb-1">Completed</p>
                <p className="text-sm text-gray-700">{task.completed ? '✅ Yes' : '❌ No'}</p>
              </div>
            </div>
          </div>

          {/* Actions */}
          <div className="px-6 py-4 bg-gray-50 flex gap-3">
            <Link
              to={`/tasks/${task.id}/edit`}
              className="px-5 py-2 bg-indigo-600 text-white rounded-lg text-sm font-semibold hover:bg-indigo-700 transition-colors"
            >
              Edit Task
            </Link>
            <Link
              to="/tasks"
              className="px-5 py-2 bg-white border border-gray-200 text-gray-600 rounded-lg text-sm font-medium hover:bg-gray-100 transition-colors"
            >
              Back to List
            </Link>
          </div>
        </div>
      </div>
    </div>
  );
}
