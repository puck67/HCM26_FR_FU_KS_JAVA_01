import { db } from '../../lib/db';
import { TasksClientView } from './TasksClientView';

// Revalidate this page dynamically if needed, though Server Actions revalidate automatically
export const dynamic = 'force-dynamic';

export default async function TasksPage() {
  const tasks = await db.getTasks();

  return (
    <div>
      <h1 className="text-3xl font-bold text-gray-800 mb-6">Tasks</h1>
      <TasksClientView tasks={tasks} />
    </div>
  );
}
