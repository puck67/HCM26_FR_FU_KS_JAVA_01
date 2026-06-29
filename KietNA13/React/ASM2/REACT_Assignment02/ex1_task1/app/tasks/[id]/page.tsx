import { notFound } from 'next/navigation';
import { store } from '@/lib/store';
import { updateTask } from '@/lib/actions';
import TaskForm from '@/components/TaskForm';
import { ROUTES } from '@/constants/routes';

interface TaskDetailPageProps {
  params: Promise<{ id: string }>;
}

export default async function TaskDetailPage({ params }: TaskDetailPageProps) {
  const { id } = await params;
  const task = store.get(id);

  if (!task) {
    notFound();
  }

  // Partial-apply the task id so TaskForm receives a (values) => Promise<void>
  const boundUpdate = updateTask.bind(null, task.id);

  return <TaskForm task={task} onSubmit={boundUpdate} cancelHref={ROUTES.TASKS} />;
}
