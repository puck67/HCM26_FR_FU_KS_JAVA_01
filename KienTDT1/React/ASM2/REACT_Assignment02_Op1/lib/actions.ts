"use server";

import { redirect } from 'next/navigation';
import { revalidatePath } from 'next/cache';
import { readTasks, writeTasks, Task } from './db';

export async function createTask(formData: FormData) {
  const name = formData.get('name') as string;
  const description = (formData.get('description') as string) || '';

  if (!name || name.trim() === '') {
    throw new Error('Name is required');
  }
  if (name.length > 40) {
    throw new Error('Name must be less than 40 characters');
  }
  if (description.length > 200) {
    throw new Error('Description must be less than 200 characters');
  }

  const tasks = readTasks();
  const newTask: Task = {
    id: Math.random().toString(36).substring(2, 9),
    name: name.trim(),
    description: description.trim(),
    completed: false,
    createdAt: new Date().toISOString(),
  };

  tasks.unshift(newTask);
  writeTasks(tasks);

  revalidatePath('/tasks');
  redirect('/tasks');
}

export async function updateTask(id: string, formData: FormData) {
  const name = formData.get('name') as string;
  const description = (formData.get('description') as string) || '';

  if (!name || name.trim() === '') {
    throw new Error('Name is required');
  }
  if (name.length > 40) {
    throw new Error('Name must be less than 40 characters');
  }
  if (description.length > 200) {
    throw new Error('Description must be less than 200 characters');
  }

  const tasks = readTasks();
  const taskIndex = tasks.findIndex(t => t.id === id);

  if (taskIndex === -1) {
    throw new Error('Task not found');
  }

  tasks[taskIndex] = {
    ...tasks[taskIndex],
    name: name.trim(),
    description: description.trim(),
  };

  writeTasks(tasks);

  revalidatePath('/tasks');
  revalidatePath(`/tasks/${id}`);
  redirect('/tasks');
}

export async function deleteTask(id: string) {
  const tasks = readTasks();
  const filteredTasks = tasks.filter(t => t.id !== id);
  writeTasks(filteredTasks);

  revalidatePath('/tasks');
}

export async function toggleTaskCompleted(id: string) {
  const tasks = readTasks();
  const taskIndex = tasks.findIndex(t => t.id === id);
  if (taskIndex !== -1) {
    tasks[taskIndex].completed = !tasks[taskIndex].completed;
    writeTasks(tasks);
  }

  revalidatePath('/tasks');
}
