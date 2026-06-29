import { useEffect, useState } from 'react';
import { useParams, Link, useNavigate } from 'react-router-dom';
import { useTasks } from '../hooks/useTasks';
import { fetchTaskById } from '../services/taskService';
import { Task, TaskFormValues } from '../types/task';
import TaskForm from '../components/TaskForm';
import LoadingSpinner from '../components/LoadingSpinner';
import ErrorDisplay from '../components/ErrorDisplay';

export default function EditTaskPage() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const { editTask } = useTasks();
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

  const handleSubmit = async (values: TaskFormValues) => {
    if (!task) return;
    await editTask(task, values);
    navigate(`/tasks/${task.id}`);
  };

  if (loading) return <LoadingSpinner message="Loading task..." />;
  if (error) return <ErrorDisplay message={error} onRetry={() => navigate(0)} />;
  if (!task) return null;

  const initialValues: TaskFormValues = {
    name: task.name,
    description: task.description,
  };

  return (
    <div className="min-h-screen bg-gray-50">
      <div className="max-w-lg mx-auto px-4 py-8">
        {/* Breadcrumb */}
        <nav className="flex items-center gap-2 text-sm text-gray-500 mb-6">
          <Link to="/tasks" className="hover:text-indigo-600 transition-colors">Tasks</Link>
          <span>/</span>
          <Link to={`/tasks/${task.id}`} className="hover:text-indigo-600 transition-colors">
            Task #{task.id}
          </Link>
          <span>/</span>
          <span className="text-gray-800 font-medium">Edit</span>
        </nav>

        <div className="bg-white rounded-2xl shadow-sm border border-gray-100 p-6">
          <div className="mb-6">
            <h1 className="text-xl font-bold text-gray-900">Edit Task</h1>
            <p className="text-gray-500 text-sm mt-1">Update the task details below.</p>
          </div>

          <TaskForm
            initialValues={initialValues}
            onSubmit={handleSubmit}
            submitLabel="Save Changes"
          />

          <div className="mt-3">
            <Link
              to={`/tasks/${task.id}`}
              className="block w-full text-center py-2.5 border border-gray-200 text-gray-600 rounded-lg text-sm font-medium hover:bg-gray-50 transition-colors"
            >
              Cancel
            </Link>
          </div>
        </div>
      </div>
    </div>
  );
}
