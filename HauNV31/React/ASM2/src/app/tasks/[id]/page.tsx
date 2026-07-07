import { getTask } from '@/lib/data';
import { notFound } from 'next/navigation';
import EditTaskForm from './EditTaskForm';

export const dynamic = 'force-dynamic';

interface PageProps {
  params: Promise<{ id: string }>;
}

export default async function TaskDetailPage({ params }: PageProps) {
  const { id } = await params;
  const task = await getTask(id);

  if (!task) {
    notFound();
  }

  return (
    <main className="min-h-screen p-10 bg-gray-100 flex items-center justify-center">
      <div className="bg-white p-8 rounded-xl shadow-lg max-w-md w-full">
        <h1 className="text-2xl font-bold text-gray-800 mb-6">Edit Task</h1>
        <div className="mb-4 text-sm text-gray-500">
          Status: {task.completed ? (
            <span className="text-green-600 font-semibold">Completed</span>
          ) : (
            <span className="text-yellow-600 font-semibold">Pending</span>
          )}
        </div>
        <EditTaskForm task={task} />
      </div>
    </main>
  );
}
