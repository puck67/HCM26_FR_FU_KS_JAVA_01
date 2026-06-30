"use server";

import { revalidatePath } from "next/cache";
import { redirect } from "next/navigation";
import { getTasks, saveTasks, Task } from "@/data/db";

export async function createTask(formData: FormData) {
  const name = formData.get("name") as string;
  const description = formData.get("description") as string;

  if (!name || name.trim() === "") {
    throw new Error("Name is required");
  }

  const tasks = getTasks();
  const newTask: Task = {
    id: Date.now().toString(),
    name: name.trim(),
    description: description ? description.trim() : "",
    completed: false,
    createdAt: new Date().toISOString(),
  };

  tasks.push(newTask);
  saveTasks(tasks);

  revalidatePath("/tasks");
  redirect("/tasks");
}

export async function updateTask(id: string, formData: FormData) {
  const name = formData.get("name") as string;
  const description = formData.get("description") as string;

  if (!name || name.trim() === "") {
    throw new Error("Name is required");
  }

  const tasks = getTasks();
  const taskIndex = tasks.findIndex((t) => t.id === id);

  if (taskIndex > -1) {
    tasks[taskIndex].name = name.trim();
    tasks[taskIndex].description = description ? description.trim() : "";
    saveTasks(tasks);
  }

  revalidatePath("/tasks");
  revalidatePath(`/tasks/${id}`);
  redirect("/tasks");
}

export async function deleteTask(id: string) {
  const tasks = getTasks();
  const filtered = tasks.filter((t) => t.id !== id);
  saveTasks(filtered);
  revalidatePath("/tasks");
}

export async function toggleTaskCompleted(id: string) {
  const tasks = getTasks();
  const taskIndex = tasks.findIndex((t) => t.id === id);
  if (taskIndex > -1) {
    tasks[taskIndex].completed = !tasks[taskIndex].completed;
    saveTasks(tasks);
  }
  revalidatePath("/tasks");
}
