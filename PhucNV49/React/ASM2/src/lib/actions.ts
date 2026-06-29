"use server";

import { revalidatePath } from "next/cache";
import { redirect } from "next/navigation";
import { addTask, editTask, removeTask, toggleCompleted } from "@/lib/data";

export async function createTask(formData: { name: string; description: string }) {
  addTask({ name: formData.name, description: formData.description });
  revalidatePath("/tasks");
  redirect("/tasks");
}

export async function updateTask(id: string, formData: { name: string; description: string }) {
  editTask(id, { name: formData.name, description: formData.description });
  revalidatePath("/tasks");
  revalidatePath(`/tasks/${id}`);
  redirect("/tasks");
}

export async function deleteTask(id: string) {
  removeTask(id);
  revalidatePath("/tasks");
}

export async function toggleTaskCompleted(id: string) {
  toggleCompleted(id);
  revalidatePath("/tasks");
}
