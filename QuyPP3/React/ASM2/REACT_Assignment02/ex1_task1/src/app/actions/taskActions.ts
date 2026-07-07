"use server";

import { dataStore } from "@/lib/dataStore";
import { revalidatePath } from "next/cache";
import { redirect } from "next/navigation";

export async function createTask(formData: { name: string; description: string }) {
  await dataStore.add(formData);
  revalidatePath("/tasks");
  redirect("/tasks");
}

export async function updateTask(id: string, formData: { name: string; description: string }) {
  await dataStore.update(id, formData);
  revalidatePath("/tasks");
  revalidatePath(`/tasks/${id}`);
  redirect("/tasks");
}

export async function deleteTask(id: string) {
  await dataStore.delete(id);
  revalidatePath("/tasks");
}

export async function toggleTaskCompleted(id: string) {
  const task = await dataStore.getById(id);
  if (task) {
    await dataStore.update(id, { completed: !task.completed });
    revalidatePath("/tasks");
    revalidatePath(`/tasks/${id}`);
  }
}
