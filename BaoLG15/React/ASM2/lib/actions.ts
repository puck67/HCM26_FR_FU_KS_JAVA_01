"use server";

import {
  createTaskInStore,
  updateTaskInStore,
  deleteTaskFromStore,
  toggleTaskCompletedInStore,
} from "@/lib/data";
import { revalidatePath } from "next/cache";
import { redirect } from "next/navigation";

export async function createTask(formData: FormData) {
  const name = (formData.get("name") as string)?.trim();
  const description = (formData.get("description") as string)?.trim() ?? "";

  if (!name || name.length > 40) {
    throw new Error("Invalid task name");
  }
  if (description.length > 200) {
    throw new Error("Description too long");
  }

  createTaskInStore(name, description);
  revalidatePath("/tasks");
  redirect("/tasks");
}

export async function updateTask(id: string, formData: FormData) {
  const name = (formData.get("name") as string)?.trim();
  const description = (formData.get("description") as string)?.trim() ?? "";

  if (!name || name.length > 40) {
    throw new Error("Invalid task name");
  }
  if (description.length > 200) {
    throw new Error("Description too long");
  }

  updateTaskInStore(id, name, description);
  revalidatePath("/tasks");
  revalidatePath(`/tasks/${id}`);
  redirect("/tasks");
}

export async function deleteTask(id: string) {
  deleteTaskFromStore(id);
  revalidatePath("/tasks");
}

export async function toggleTaskCompleted(id: string) {
  toggleTaskCompletedInStore(id);
  revalidatePath("/tasks");
}
