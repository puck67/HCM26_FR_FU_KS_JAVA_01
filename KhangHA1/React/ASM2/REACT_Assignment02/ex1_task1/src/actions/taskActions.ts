'use server';

import { revalidatePath } from 'next/cache';
import { db } from '../lib/db';

export async function createTask(formData: { name: string; description: string }) {
  await db.createTask(formData);
  revalidatePath('/tasks');
}

export async function updateTask(id: string, formData: { name: string; description: string }) {
  await db.updateTask(id, formData);
  revalidatePath('/tasks');
  revalidatePath(`/tasks/${id}`);
}

export async function deleteTask(id: string) {
  await db.deleteTask(id);
  revalidatePath('/tasks');
}

export async function toggleTaskCompleted(id: string, currentStatus: boolean) {
  await db.updateTask(id, { completed: !currentStatus });
  revalidatePath('/tasks');
}
