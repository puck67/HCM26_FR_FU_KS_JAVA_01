"use client";

import TaskForm from "@/components/TaskForm";
import { updateTask } from "@/lib/actions";
import { Task } from "@/types/task";

interface EditTaskFormProps {
  task: Task;
}

export default function EditTaskForm({ task }: EditTaskFormProps) {
  const handleSubmit = async (formData: FormData) => {
    await updateTask(task.id, formData);
  };

  return (
    <TaskForm
      mode="edit"
      initialValues={{ name: task.name, description: task.description }}
      onSubmit={handleSubmit}
    />
  );
}
