"use server";

import { revalidatePath } from "next/cache";
import { redirect } from "next/navigation";
import { addTask, updateTaskInStore, deleteTaskFromStore, getTaskById } from "@/lib/store";
import { Task } from "@/types/task";

// Helper to generate unique IDs
function generateId(): string {
  return Date.now().toString(36) + Math.random().toString(36).slice(2);
}

// Create a new task
export async function createTask(formData: FormData): Promise<void> {
  const name = (formData.get("name") as string)?.trim();
  const description = (formData.get("description") as string)?.trim() ?? "";

  if (!name || name.length > 40) {
    throw new Error("Invalid task name");
  }
  if (description.length > 200) {
    throw new Error("Description too long");
  }

  const newTask: Task = {
    id: generateId(),
    name,
    description,
    completed: false,
    createdAt: new Date().toISOString(),
  };

  addTask(newTask);
  revalidatePath("/tasks");
  redirect("/tasks");
}

// Update an existing task
export async function updateTask(id: string, formData: FormData): Promise<void> {
  const name = (formData.get("name") as string)?.trim();
  const description = (formData.get("description") as string)?.trim() ?? "";

  if (!name || name.length > 40) {
    throw new Error("Invalid task name");
  }
  if (description.length > 200) {
    throw new Error("Description too long");
  }

  updateTaskInStore(id, { name, description });
  revalidatePath("/tasks");
  revalidatePath(`/tasks/${id}`);
  redirect("/tasks");
}

// Delete a task
export async function deleteTask(id: string): Promise<void> {
  deleteTaskFromStore(id);
  revalidatePath("/tasks");
}

// Toggle completed status
export async function toggleTaskCompleted(id: string): Promise<void> {
  const task = getTaskById(id);
  if (!task) throw new Error("Task not found");
  updateTaskInStore(id, { completed: !task.completed });
  revalidatePath("/tasks");
}
