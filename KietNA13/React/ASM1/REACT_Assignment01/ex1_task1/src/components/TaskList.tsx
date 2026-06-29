import { useState, useCallback, useMemo } from 'react';
import { TaskCard } from './TaskCard';
import { Skeleton } from './Skeleton';
import { EmptyState } from './EmptyState';
import { ConfirmModal } from './ConfirmModal';
import type { Task } from '../types/task';

const SKELETON_COUNT = 5;

export interface TaskListProps {
  tasks: Task[];
  isLoading: boolean;
  onDelete: (id: number) => void;
  isDeletingId?: number | null;
  filter?: string;
}

export const TaskList = ({
  tasks,
  isLoading,
  onDelete,
  isDeletingId,
  filter,
}: TaskListProps) => {
  const [pendingDeleteId, setPendingDeleteId] = useState<number | null>(null);

  const handleDeleteRequest = useCallback((id: number) => {
    setPendingDeleteId(id);
  }, []);

  const handleConfirmDelete = useCallback(() => {
    if (pendingDeleteId !== null) {
      onDelete(pendingDeleteId);
      setPendingDeleteId(null);
    }
  }, [pendingDeleteId, onDelete]);

  const handleCancelDelete = useCallback(() => {
    setPendingDeleteId(null);
  }, []);

  const skeletonKeys = useMemo(
    () => Array.from({ length: SKELETON_COUNT }, (_, i) => i),
    [],
  );

  if (isLoading) {
    return (
      <div
        className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3"
        aria-busy="true"
        aria-label="Loading tasks"
      >
        {skeletonKeys.map((i) => (
          <div
            key={i}
            className="rounded-xl border border-gray-200 bg-white p-5 shadow-sm"
          >
            <div className="flex items-start justify-between gap-3">
              <div className="flex-1 space-y-2">
                <Skeleton className="h-5 w-3/4" />
                <Skeleton className="h-4 w-full" />
                <Skeleton className="h-4 w-1/2" />
              </div>
              <Skeleton className="h-5 w-20 shrink-0 rounded-full" />
            </div>
            <div className="mt-4 flex gap-2 border-t border-gray-100 pt-2">
              <Skeleton className="h-8 w-14 rounded-lg" />
              <Skeleton className="h-8 w-14 rounded-lg" />
              <Skeleton className="ml-auto h-8 w-16 rounded-lg" />
            </div>
          </div>
        ))}
      </div>
    );
  }

  if (tasks.length === 0) {
    return <EmptyState filter={filter} />;
  }

  return (
    <>
      <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
        {tasks.map((task) => (
          <TaskCard
            key={task.id}
            task={task}
            onDelete={handleDeleteRequest}
            isDeleting={isDeletingId === task.id}
          />
        ))}
      </div>

      <ConfirmModal
        isOpen={pendingDeleteId !== null}
        title="Delete Task"
        message="Are you sure you want to delete this task? This action cannot be undone."
        onConfirm={handleConfirmDelete}
        onCancel={handleCancelDelete}
      />
    </>
  );
};
