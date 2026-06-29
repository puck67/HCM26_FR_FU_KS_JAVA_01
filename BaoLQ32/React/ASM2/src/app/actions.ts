'use server';

import { revalidatePath } from 'next/cache';
import { getTasks, saveTasks, Task } from '@/lib/db';

// Helper to parse name and description from either FormData or plain object
function parseTaskForm(formData: FormData | { name: string; description?: string }) {
  let name = '';
  let description = '';

  if (formData instanceof FormData) {
    name = (formData.get('name') as string) || '';
    description = (formData.get('description') as string) || '';
  } else if (formData && typeof formData === 'object') {
    name = formData.name || '';
    description = formData.description || '';
  }

  return {
    name: name.trim(),
    description: description.trim(),
  };
}

export async function createTask(formData: FormData | { name: string; description?: string }) {
  const { name, description } = parseTaskForm(formData);

  if (!name) {
    throw new Error('Task name is required');
  }
  if (name.length > 40) {
    throw new Error('Task name cannot exceed 40 characters');
  }
  if (description.length > 200) {
    throw new Error('Task description cannot exceed 200 characters');
  }

  const tasks = await getTasks();
  const newTask: Task = {
    id: `task-${Date.now()}-${Math.random().toString(36).substr(2, 9)}`,
    name,
    description,
    completed: false,
    createdAt: new Date().toISOString(),
  };

  tasks.push(newTask);
  await saveTasks(tasks);
  
  revalidatePath('/tasks');
  return newTask;
}

export async function updateTask(id: string, formData: FormData | { name: string; description?: string }) {
  const { name, description } = parseTaskForm(formData);

  if (!name) {
    throw new Error('Task name is required');
  }
  if (name.length > 40) {
    throw new Error('Task name cannot exceed 40 characters');
  }
  if (description.length > 200) {
    throw new Error('Task description cannot exceed 200 characters');
  }

  const tasks = await getTasks();
  const index = tasks.findIndex(t => t.id === id);
  if (index === -1) {
    throw new Error('Task not found');
  }

  tasks[index] = {
    ...tasks[index],
    name,
    description,
  };

  await saveTasks(tasks);
  
  revalidatePath('/tasks');
  revalidatePath(`/tasks/${id}`);
  return tasks[index];
}

export async function deleteTask(id: string) {
  const tasks = await getTasks();
  const filtered = tasks.filter(t => t.id !== id);
  await saveTasks(filtered);
  
  revalidatePath('/tasks');
  return { success: true };
}

export async function toggleTaskCompleted(id: string) {
  const tasks = await getTasks();
  const index = tasks.findIndex(t => t.id === id);
  if (index === -1) {
    throw new Error('Task not found');
  }

  tasks[index] = {
    ...tasks[index],
    completed: !tasks[index].completed,
  };

  await saveTasks(tasks);
  
  revalidatePath('/tasks');
  return tasks[index];
}
