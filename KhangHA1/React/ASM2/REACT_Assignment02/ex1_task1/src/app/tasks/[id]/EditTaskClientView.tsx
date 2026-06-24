'use client';

import { useRouter } from 'next/navigation';
import { TaskForm } from '../../../components/TaskForm';
import { updateTask } from '../../../actions/taskActions';
import type { Task } from '../../../lib/db';

export function EditTaskClientView({ task }: { task: Task }) {
  const router = useRouter();

  const handleSubmit = async (values: { name: string; description: string }) => {
    await updateTask(task.id, values);
    router.push('/tasks');
  };

  const handleCancel = () => {
    router.push('/tasks');
  };

  return (
    <div>
      <h1 className="text-3xl font-bold text-gray-800 mb-6">Edit Task</h1>
      <TaskForm 
        initialValues={{ name: task.name, description: task.description || '' }}
        onSubmit={handleSubmit}
        onCancel={handleCancel}
      />
    </div>
  );
}
