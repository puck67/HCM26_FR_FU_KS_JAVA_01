import { memo, useCallback } from 'react';
import { useNavigate } from 'react-router-dom';
import { Badge } from './Badge';
import { Button } from './Button';
import { ROUTES } from '../constants/routes';
import type { Task } from '../types/task';

const MAX_DESCRIPTION_PREVIEW_LENGTH = 80;

export interface TaskCardProps {
  task: Task;
  onDelete: (id: number) => void;
  isDeleting?: boolean;
}

export const TaskCard = memo(({ task, onDelete, isDeleting = false }: TaskCardProps) => {
  const navigate = useNavigate();

  const handleView = useCallback(() => {
    navigate(ROUTES.TASK_DETAIL(task.id));
  }, [navigate, task.id]);

  const handleEdit = useCallback(() => {
    navigate(ROUTES.TASK_EDIT(task.id));
  }, [navigate, task.id]);

  const handleDelete = useCallback(() => {
    onDelete(task.id);
  }, [onDelete, task.id]);

  const truncatedDescription =
    task.description && task.description.length > MAX_DESCRIPTION_PREVIEW_LENGTH
      ? `${task.description.slice(0, MAX_DESCRIPTION_PREVIEW_LENGTH)}…`
      : task.description;

  return (
    <article
      className="group flex flex-col rounded-xl border border-gray-200 bg-white p-5 shadow-sm transition-shadow hover:shadow-md"
      aria-label={`Task: ${task.name}`}
    >
      <div className="flex items-start justify-between gap-3">
        <div className="min-w-0 flex-1">
          <h3 className="truncate font-semibold text-gray-900">{task.name}</h3>
          {truncatedDescription ? (
            <p className="mt-1 text-sm text-gray-500 line-clamp-2">{truncatedDescription}</p>
          ) : (
            <p className="mt-1 text-sm text-gray-400 italic">No description</p>
          )}
        </div>
        <Badge completed={task.completed} className="shrink-0" />
      </div>

      <div className="mt-4 flex items-center gap-2 pt-2 border-t border-gray-100">
        <Button
          size="sm"
          variant="ghost"
          onClick={handleView}
          aria-label={`View details of task: ${task.name}`}
        >
          View
        </Button>
        <Button
          size="sm"
          variant="secondary"
          onClick={handleEdit}
          aria-label={`Edit task: ${task.name}`}
        >
          Edit
        </Button>
        <Button
          size="sm"
          variant="danger"
          onClick={handleDelete}
          isLoading={isDeleting}
          aria-label={`Delete task: ${task.name}`}
          className="ml-auto"
        >
          Delete
        </Button>
      </div>
    </article>
  );
});

TaskCard.displayName = 'TaskCard';
