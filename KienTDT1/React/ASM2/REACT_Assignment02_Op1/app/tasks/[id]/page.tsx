import { notFound } from 'next/navigation';
import { readTasks } from '@/lib/db';
import TaskEditForm from './TaskEditForm';

interface PageProps {
  params: Promise<{ id: string }>;
}

export default async function TaskDetailPage({ params }: PageProps) {
  const { id } = await params;
  const tasks = readTasks();
  const task = tasks.find(t => t.id === id);

  if (!task) {
    notFound();
  }

  return (
    <div className="max-w-xl mx-auto space-y-6 animate-slide-up">
      <TaskEditForm task={task} />
    </div>
  );
}
