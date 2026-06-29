'use server';

import { revalidatePath } from 'next/cache';
import { redirect } from 'next/navigation';
import { store } from './store';
import { Task, TaskFormValues } from '@/types/task';

export async function createTask(values: TaskFormValues): Promise<void> {
  const task: Task = {
    id: crypto.randomUUID(),
    name: values.name.trim(),
    description: values.description?.trim() || undefined,
    completed: false,
    createdAt: new Date().toISOString(),
  };
  store.set(task.id, task);
  revalidatePath('/tasks');
  // redirect() throws internally — must stay outside any try/catch
  redirect('/tasks');
}

export async function updateTask(id: string, values: TaskFormValues): Promise<void> {
  const existing = store.get(id);
  if (!existing) return;

  const updated: Task = {
    ...existing,
    name: values.name.trim(),
    description: values.description?.trim() || undefined,
  };
  store.set(id, updated);
  revalidatePath('/tasks');
  // redirect() throws internally — must stay outside any try/catch
  redirect('/tasks');
}

export async function deleteTask(id: string): Promise<void> {
  store.delete(id);
  revalidatePath('/tasks');
}

export async function toggleTaskCompleted(id: string): Promise<void> {
  const task = store.get(id);
  if (!task) return;

  store.set(id, { ...task, completed: !task.completed });
  revalidatePath('/tasks');
}
