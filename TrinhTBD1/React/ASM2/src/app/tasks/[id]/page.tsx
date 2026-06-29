import React from 'react';
import Link from 'next/link';
import { getTaskById } from '@/lib/tasks';
import { GenericToolbar } from '@/components/generic/GenericToolbar';
import { GenericButton } from '@/components/generic/GenericButton';
import { GenericCard } from '@/components/generic/GenericCard';
import { TaskEditForm } from '@/components/tasks/TaskEditForm';

export const revalidate = 0;
export const dynamic = 'force-dynamic';

interface TaskDetailPageProps {
  params: Promise<{ id: string }>;
}

export default async function TaskDetailPage({ params }: TaskDetailPageProps) {
  const resolvedParams = await params;
  const taskId = resolvedParams?.id;
  const task = taskId ? await getTaskById(taskId) : null;

  if (!task) {
    return (
      <div className="max-w-xl mx-auto py-8">
        <GenericCard className="text-center py-8">
          <h2 className="text-lg font-bold text-zinc-100 mb-2">Task Not Found</h2>
          <p className="text-sm text-zinc-400 mb-5">
            The task with ID &quot;{taskId}&quot; was not found.
          </p>
          <Link href="/tasks">
            <GenericButton variant="primary" label="Back to Tasks" />
          </Link>
        </GenericCard>
      </div>
    );
  }

  return (
    <div className="max-w-2xl mx-auto space-y-4">
      <GenericToolbar
        title="Task Detail"
        extraActions={
          <Link href="/tasks">
            <GenericButton variant="outline" size="md" label="Back to Tasks" />
          </Link>
        }
      />

      <TaskEditForm task={task} />
    </div>
  );
}
