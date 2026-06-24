'use client';

import { useRouter } from 'next/navigation';
import { TaskForm } from '../../../components/TaskForm';
import { createTask } from '../../../actions/taskActions';

export default function NewTaskPage() {
  const router = useRouter();

  const handleSubmit = async (values: { name: string; description: string }) => {
    await createTask(values);
    router.push('/tasks');
  };

  const handleCancel = () => {
    router.push('/tasks');
  };

  return (
    <div>
      <h1 className="text-3xl font-bold text-gray-800 mb-6">Create New Task</h1>
      <TaskForm 
        initialValues={{ name: '', description: '' }}
        onSubmit={handleSubmit}
        onCancel={handleCancel}
      />
    </div>
  );
}
