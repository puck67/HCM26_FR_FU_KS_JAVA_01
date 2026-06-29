'use server';

import { redirect } from 'next/navigation';
import { revalidatePath } from 'next/cache';
import { getTasks, saveTasks } from '../lib/tasks';
import { Task } from '../types';

function parseFormData(formData: any): { name: string; description: string } {
  // Support both standard FormData and plain JavaScript object (from Formik client-side calls)
  if (formData && typeof formData.get === 'function') {
    return {
      name: (formData.get('name') as string) || '',
      description: (formData.get('description') as string) || ''
    };
  } else if (formData && typeof formData === 'object') {
    return {
      name: formData.name || '',
      description: formData.description || ''
    };
  }
  return { name: '', description: '' };
}

export async function createTask(formData: any) {
  const { name, description } = parseFormData(formData);

  const cleanName = name.trim();
  const cleanDescription = description.trim();

  // Basic server-side validation
  if (!cleanName) {
    throw new Error('Task name is required');
  }
  if (cleanName.length > 40) {
    throw new Error('Task name must be under 40 characters');
  }
  if (cleanDescription.length > 200) {
    throw new Error('Description must be under 200 characters');
  }

  const tasks = getTasks();
  const newTask: Task = {
    id: Date.now().toString(),
    name: cleanName,
    description: cleanDescription,
    completed: false,
    createdAt: new Date().toISOString()
  };

  tasks.unshift(newTask);
  saveTasks(tasks);

  revalidatePath('/tasks');
  revalidatePath('/');
  redirect('/tasks');
}

export async function updateTask(id: string, formData: any) {
  const { name, description } = parseFormData(formData);

  const cleanName = name.trim();
  const cleanDescription = description.trim();

  if (!cleanName) {
    throw new Error('Task name is required');
  }
  if (cleanName.length > 40) {
    throw new Error('Task name must be under 40 characters');
  }
  if (cleanDescription.length > 200) {
    throw new Error('Description must be under 200 characters');
  }

  const tasks = getTasks();
  const taskIndex = tasks.findIndex((t) => t.id === id);

  if (taskIndex === -1) {
    throw new Error('Task not found');
  }

  tasks[taskIndex] = {
    ...tasks[taskIndex],
    name: cleanName,
    description: cleanDescription
  };

  saveTasks(tasks);

  revalidatePath('/tasks');
  revalidatePath(`/tasks/${id}`);
  revalidatePath('/');
  redirect('/tasks');
}

export async function deleteTask(id: string) {
  const tasks = getTasks();
  const updatedTasks = tasks.filter((t) => t.id !== id);
  saveTasks(updatedTasks);

  revalidatePath('/tasks');
  revalidatePath('/');
}

export async function toggleTaskCompleted(id: string) {
  const tasks = getTasks();
  const taskIndex = tasks.findIndex((t) => t.id === id);

  if (taskIndex !== -1) {
    tasks[taskIndex].completed = !tasks[taskIndex].completed;
    saveTasks(tasks);
  }

  revalidatePath('/tasks');
  revalidatePath(`/tasks/${id}`);
  revalidatePath('/');
}
