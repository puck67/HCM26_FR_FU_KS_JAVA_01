"use server";

import { revalidatePath } from "next/cache";
import { redirect } from "next/navigation";
import { getTasks, saveTasks, Task } from "./tasksStore";

export async function createTask(formData: { name: string; description?: string }) {
  const name = (formData.name || "").trim();
  const description = (formData.description || "").trim();

  if (!name) {
    throw new Error("Name is required");
  }
  if (name.length > 40) {
    throw new Error("Name must be less than 40 characters");
  }
  if (description.length > 200) {
    throw new Error("Description must be less than 200 characters");
  }

  const tasks = getTasks();
  const newTask: Task = {
    id: Math.random().toString(36).substring(2, 9),
    name,
    description,
    completed: false,
    createdAt: new Date().toISOString(),
  };

  tasks.push(newTask);
  saveTasks(tasks);

  revalidatePath("/tasks");
  redirect("/tasks");
}

export async function updateTask(id: string, formData: { name: string; description?: string }) {
  const name = (formData.name || "").trim();
  const description = (formData.description || "").trim();

  if (!name) {
    throw new Error("Name is required");
  }
  if (name.length > 40) {
    throw new Error("Name must be less than 40 characters");
  }
  if (description.length > 200) {
    throw new Error("Description must be less than 200 characters");
  }

  const tasks = getTasks();
  const taskIndex = tasks.findIndex((t) => t.id === id);

  if (taskIndex === -1) {
    throw new Error("Task not found");
  }

  tasks[taskIndex] = {
    ...tasks[taskIndex],
    name,
    description,
  };

  saveTasks(tasks);

  revalidatePath("/tasks");
  revalidatePath(`/tasks/${id}`);
  redirect("/tasks");
}

export async function deleteTask(id: string) {
  const tasks = getTasks();
  const filteredTasks = tasks.filter((t) => t.id !== id);
  saveTasks(filteredTasks);

  revalidatePath("/tasks");
}

export async function toggleTaskCompleted(id: string) {
  const tasks = getTasks();
  const taskIndex = tasks.findIndex((t) => t.id === id);

  if (taskIndex !== -1) {
    tasks[taskIndex].completed = !tasks[taskIndex].completed;
    saveTasks(tasks);
  }

  revalidatePath("/tasks");
  revalidatePath(`/tasks/${id}`);
}
