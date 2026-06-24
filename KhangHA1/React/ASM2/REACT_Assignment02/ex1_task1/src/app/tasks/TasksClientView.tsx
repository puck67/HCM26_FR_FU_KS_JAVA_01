'use client';

import { useRouter } from 'next/navigation';
import { useTransition } from 'react';
import { CrudTable } from '../../components/common/CrudTable';
import { CrudActionBar } from '../../components/common/CrudActionBar';
import { deleteTask, toggleTaskCompleted } from '../../actions/taskActions';
import type { Task } from '../../lib/db';

export function TasksClientView({ tasks }: { tasks: Task[] }) {
  const router = useRouter();
  const [isPending, startTransition] = useTransition();

  const handleAddClick = () => {
    router.push('/tasks/new');
  };

  const handleEditClick = (task: Task) => {
    router.push(`/tasks/${task.id}`);
  };

  const handleDeleteClick = (task: Task) => {
    if (window.confirm(`Are you sure you want to delete "${task.name}"?`)) {
      startTransition(async () => {
        await deleteTask(task.id);
      });
    }
  };

  const handleToggleComplete = (task: Task) => {
    startTransition(async () => {
      await toggleTaskCompleted(task.id, task.completed);
    });
  };

  const handleRowClick = (task: Task) => {
    router.push(`/tasks/${task.id}`);
  };

  const rowStyle = (task: Task) => {
    if (task.completed) {
      return 'bg-green-50 text-gray-500 line-through';
    }
    return 'bg-white text-gray-900';
  };

  return (
    <div>
      <div className="mb-6">
        <CrudActionBar onAdd={handleAddClick} />
      </div>
      
      {isPending && <div className="text-sm text-blue-600 mb-2">Updating...</div>}

      <CrudTable
        data={tasks}
        onEdit={handleEditClick}
        onDelete={handleDeleteClick}
        onRowClick={handleRowClick}
        rowStyle={rowStyle}
        extraActions={(task) => [
          {
            title: task.completed ? "Mark Pending" : "Mark Completed",
            action: () => handleToggleComplete(task),
            style: task.completed ? "bg-yellow-100 text-yellow-800 hover:bg-yellow-200" : "bg-green-100 text-green-800 hover:bg-green-200"
          }
        ]}
      />
    </div>
  );
}
