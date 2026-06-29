import { useState, useCallback } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import { useTask } from '../hooks/useTask';
import { useTaskMutation } from '../hooks/useTaskMutation';
import { Badge } from '../components/Badge';
import { Button } from '../components/Button';
import { Skeleton } from '../components/Skeleton';
import { ConfirmModal } from '../components/ConfirmModal';
import { ROUTES } from '../constants/routes';

const TaskDetailPage = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const taskId = Number(id);

  const { data: task, isLoading, isError } = useTask(taskId);
  const { deleteMutation } = useTaskMutation();
  const [showDeleteModal, setShowDeleteModal] = useState(false);

  const handleDeleteConfirm = useCallback(() => {
    setShowDeleteModal(false);
    deleteMutation.mutate(taskId, {
      onSuccess: () => navigate(ROUTES.TASKS),
    });
  }, [deleteMutation, taskId, navigate]);

  const handleDeleteRequest = useCallback(() => {
    setShowDeleteModal(true);
  }, []);

  const handleCancelDelete = useCallback(() => {
    setShowDeleteModal(false);
  }, []);

  if (isLoading) {
    return (
      <main className="mx-auto max-w-3xl px-4 py-8 sm:px-6 lg:px-8">
        <Skeleton className="h-4 w-24" />
        <div className="mt-6 rounded-xl border border-gray-200 bg-white p-6 shadow-sm space-y-4">
          <div className="flex items-start justify-between gap-4">
            <Skeleton className="h-7 w-64" />
            <Skeleton className="h-5 w-20 rounded-full" />
          </div>
          <Skeleton className="h-4 w-full" />
          <Skeleton className="h-4 w-3/4" />
          <div className="grid grid-cols-2 gap-4 pt-2">
            <Skeleton className="h-12 w-full rounded-lg" />
            <Skeleton className="h-12 w-full rounded-lg" />
          </div>
          <div className="flex gap-3 pt-2">
            <Skeleton className="h-10 w-28 rounded-lg" />
            <Skeleton className="h-10 w-28 rounded-lg" />
          </div>
        </div>
      </main>
    );
  }

  if (isError || !task) {
    return (
      <main className="mx-auto max-w-3xl px-4 py-8 sm:px-6 lg:px-8 text-center">
        <h1 className="text-xl font-semibold text-gray-900">Task not found</h1>
        <p className="mt-2 text-sm text-gray-500">
          This task may have been deleted or the ID is invalid.
        </p>
        <Link
          to={ROUTES.TASKS}
          className="mt-4 inline-block text-sm text-indigo-600 hover:underline"
        >
          ← Back to Tasks
        </Link>
      </main>
    );
  }

  return (
    <main className="mx-auto max-w-3xl px-4 py-8 sm:px-6 lg:px-8">
      <Link
        to={ROUTES.TASKS}
        className="text-sm font-medium text-indigo-600 hover:underline focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-indigo-500 rounded"
      >
        ← Back to Tasks
      </Link>

      <div className="mt-4 rounded-xl border border-gray-200 bg-white p-6 shadow-sm">
        <div className="flex items-start justify-between gap-4">
          <h1 className="text-xl font-bold text-gray-900">{task.name}</h1>
          <Badge completed={task.completed} className="shrink-0" />
        </div>

        {task.description ? (
          <p className="mt-4 text-gray-600 leading-relaxed">{task.description}</p>
        ) : (
          <p className="mt-4 text-sm text-gray-400 italic">No description provided.</p>
        )}

        <dl className="mt-6 grid grid-cols-2 gap-x-4 gap-y-4 rounded-lg bg-gray-50 p-4 text-sm sm:grid-cols-3">
          <div>
            <dt className="font-medium text-gray-500">Task ID</dt>
            <dd className="mt-1 font-mono text-gray-900">#{task.id}</dd>
          </div>
          <div>
            <dt className="font-medium text-gray-500">User ID</dt>
            <dd className="mt-1 text-gray-900">{task.userId}</dd>
          </div>
          <div>
            <dt className="font-medium text-gray-500">Status</dt>
            <dd className="mt-1 text-gray-900">
              {task.completed ? 'Completed' : 'Pending'}
            </dd>
          </div>
        </dl>

        <div className="mt-6 flex gap-3">
          <Button
            variant="secondary"
            onClick={() => navigate(ROUTES.TASK_EDIT(task.id))}
            aria-label={`Edit task: ${task.name}`}
          >
            Edit Task
          </Button>
          <Button
            variant="danger"
            onClick={handleDeleteRequest}
            isLoading={deleteMutation.isPending}
            aria-label={`Delete task: ${task.name}`}
          >
            Delete Task
          </Button>
        </div>
      </div>

      <ConfirmModal
        isOpen={showDeleteModal}
        title="Delete Task"
        message="Are you sure you want to delete this task? This action cannot be undone."
        onConfirm={handleDeleteConfirm}
        onCancel={handleCancelDelete}
      />
    </main>
  );
};

export default TaskDetailPage;
