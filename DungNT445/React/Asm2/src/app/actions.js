"use server";

import { revalidatePath } from "next/cache";
import { getTasks, saveTasks } from "../lib/tasks";

export async function createTask(formData) {
  const name = formData.get("name");
  const description = formData.get("description");

  if (!name || name.trim() === "") {
    throw new Error("Name is required");
  }

  const tasks = await getTasks();
  const newTask = {
    id: Date.now().toString(),
    name: name.substring(0, 40), // enforce max 40 chars on server
    description: description ? description.substring(0, 200) : "", // enforce max 200 chars on server
    completed: false,
  };

  tasks.push(newTask);
  await saveTasks(tasks);

  revalidatePath("/tasks");
}

export async function updateTask(id, formData) {
  const name = formData.get("name");
  const description = formData.get("description");

  if (!name || name.trim() === "") {
    throw new Error("Name is required");
  }

  const tasks = await getTasks();
  const updatedTasks = tasks.map((task) => {
    if (task.id === id) {
      return {
        ...task,
        name: name.substring(0, 40),
        description: description ? description.substring(0, 200) : "",
      };
    }
    return task;
  });

  await saveTasks(updatedTasks);

  revalidatePath("/tasks");
  revalidatePath(`/tasks/${id}`);
}

export async function deleteTask(id) {
  const tasks = await getTasks();
  const filteredTasks = tasks.filter((task) => task.id !== id);
  await saveTasks(filteredTasks);

  revalidatePath("/tasks");
}

export async function toggleTaskCompleted(id) {
  const tasks = await getTasks();
  const updatedTasks = tasks.map((task) => {
    if (task.id === id) {
      return {
        ...task,
        completed: !task.completed,
      };
    }
    return task;
  });

  await saveTasks(updatedTasks);

  revalidatePath("/tasks");
}
