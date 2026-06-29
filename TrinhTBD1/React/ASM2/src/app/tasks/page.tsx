import React from 'react';
import { getTasks } from '@/lib/tasks';
import { Task } from '@/types/task';
import { GenericToolbar } from '@/components/generic/GenericToolbar';
import { GenericTable, Column } from '@/components/generic/GenericTable';
import { GenericBadge } from '@/components/generic/GenericBadge';
import { TaskRowActions } from '@/components/tasks/TaskRowActions';

export const revalidate = 0;
export const dynamic = 'force-dynamic';

export default async function TaskListPage() {
  const tasks = await getTasks();

  const columns: Column<Task>[] = [
    {
      header: 'Task Name',
      accessor: (task: Task) => (
        <div>
          <div className={`font-medium ${task.completed ? 'line-through text-zinc-500' : 'text-zinc-100'}`}>
            {task.name}
          </div>
          {task.description && (
            <div className={`text-xs mt-0.5 max-w-md truncate ${task.completed ? 'text-zinc-600' : 'text-zinc-400'}`}>
              {task.description}
            </div>
          )}
        </div>
      )
    },
    {
      header: 'Status',
      accessor: (task: Task) => (
        <GenericBadge
          label={task.completed ? 'Completed' : 'Pending'}
          variant={task.completed ? 'completed' : 'pending'}
        />
      ),
      className: 'whitespace-nowrap'
    },
    {
      header: 'Created',
      accessor: (task: Task) => (
        <span className="text-xs text-zinc-400 font-mono">
          {new Date(task.createdAt).toLocaleDateString()}
        </span>
      ),
      className: 'whitespace-nowrap'
    }
  ];

  return (
    <div className="space-y-4">
      <GenericToolbar
        title="Tasks"
        entityName="Task"
        createUrl="/tasks/new"
        createLabel="New Task"
      />

      <GenericTable<Task>
        columns={columns}
        data={tasks}
        keyExtractor={(item) => item.id}
        emptyMessage="No tasks found."
        rowClassName={(item) => item.completed ? 'bg-zinc-950/40 opacity-75' : ''}
        renderActions={(item) => <TaskRowActions task={item} />}
      />
    </div>
  );
}
