'use server'

import { addTask, removeTask, updateTaskData, getTask } from '@/lib/data';
import { revalidatePath } from 'next/cache';
import { redirect } from 'next/navigation';

export async function createTask(formData: FormData) {
  const name = formData.get('name') as string;
  const description = formData.get('description') as string;
  
  if (!name || name.length > 40) {
    throw new Error('Name is required and must be max 40 characters');
  }
  if (description && description.length > 200) {
    throw new Error('Description must be max 200 characters');
  }

  await addTask({ name, description });
  revalidatePath('/tasks');
  redirect('/tasks');
}

export async function updateTask(id: string, formData: FormData) {
  const name = formData.get('name') as string;
  const description = formData.get('description') as string;
  
  if (!name || name.length > 40) {
    throw new Error('Name is required and must be max 40 characters');
  }
  if (description && description.length > 200) {
    throw new Error('Description must be max 200 characters');
  }

  await updateTaskData(id, { name, description });
  revalidatePath('/tasks');
  revalidatePath(`/tasks/${id}`);
  redirect('/tasks');
}

export async function deleteTask(id: string) {
  await removeTask(id);
  revalidatePath('/tasks');
}

export async function toggleTaskCompleted(id: string) {
  const task = await getTask(id);
  if (task) {
    await updateTaskData(id, { completed: !task.completed });
    revalidatePath('/tasks');
    revalidatePath(`/tasks/${id}`);
  }
}
