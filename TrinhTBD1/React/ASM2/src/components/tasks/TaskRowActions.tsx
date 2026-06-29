'use client';

import React, { useState, useTransition } from 'react';
import Link from 'next/link';
import { Task } from '@/types/task';
import { toggleTaskCompleted, deleteTask } from '@/app/actions/taskActions';
import { GenericButton } from '@/components/generic/GenericButton';
import { GenericModal } from '@/components/generic/GenericModal';

export interface TaskRowActionsProps {
  task: Task;
}

export const TaskRowActions: React.FC<TaskRowActionsProps> = ({ task }) => {
  const [isPending, startTransition] = useTransition();
  const [showDeleteModal, setShowDeleteModal] = useState(false);

  const handleToggle = () => {
    startTransition(async () => {
      await toggleTaskCompleted(task.id);
    });
  };

  const handleConfirmDelete = () => {
    startTransition(async () => {
      await deleteTask(task.id);
      setShowDeleteModal(false);
    });
  };

  return (
    <>
      <div className="flex items-center justify-end gap-2">
        <GenericButton
          variant={task.completed ? 'secondary' : 'primary'}
          size="sm"
          loading={isPending}
          onClick={handleToggle}
          label={task.completed ? 'Mark Pending' : 'Mark Completed'}
        />
        <Link href={`/tasks/${task.id}`}>
          <GenericButton
            variant="outline"
            size="sm"
            label="Edit"
          />
        </Link>
        <GenericButton
          variant="danger"
          size="sm"
          loading={isPending}
          onClick={() => setShowDeleteModal(true)}
          label="Delete"
        />
      </div>

      <GenericModal
        isOpen={showDeleteModal}
        title="Delete Task"
        message={`Are you sure you want to delete task "${task.name}"? This action cannot be undone.`}
        confirmLabel="Delete"
        cancelLabel="Cancel"
        loading={isPending}
        onConfirm={handleConfirmDelete}
        onCancel={() => setShowDeleteModal(false)}
      />
    </>
  );
};
