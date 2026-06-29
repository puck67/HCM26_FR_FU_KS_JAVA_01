"use server";

import { revalidatePath } from "next/cache";
import { redirect } from "next/navigation";
import { tasksRepo } from "@/lib/tasksRepo";
import * as yup from "yup";

const taskSchema = yup.object().shape({
  name: yup.string().required("Name is required").max(40, "Name must be at most 40 characters"),
  description: yup.string().max(200, "Description must be at most 200 characters").optional().default(""),
});

export async function createTask(formData: FormData | { name: string; description?: string }) {
  let name = "";
  let description = "";

  if (formData instanceof FormData) {
    name = (formData.get("name") as string) || "";
    description = (formData.get("description") as string) || "";
  } else {
    name = formData.name || "";
    description = formData.description || "";
  }

  await taskSchema.validate({ name, description }, { abortEarly: false });

  await tasksRepo.create({
    name: name.trim(),
    description: description.trim(),
    completed: false,
  });

  revalidatePath("/tasks");
  redirect("/tasks");
}

export async function updateTask(id: string, formData: FormData | { name: string; description?: string }) {
  let name = "";
  let description = "";

  if (formData instanceof FormData) {
    name = (formData.get("name") as string) || "";
    description = (formData.get("description") as string) || "";
  } else {
    name = formData.name || "";
    description = formData.description || "";
  }

  await taskSchema.validate({ name, description }, { abortEarly: false });

  await tasksRepo.update(id, {
    name: name.trim(),
    description: description.trim(),
  });

  revalidatePath("/tasks");
  revalidatePath(`/tasks/${id}`);
  redirect("/tasks");
}

export async function deleteTask(id: string) {
  await tasksRepo.delete(id);
  revalidatePath("/tasks");
}

export async function toggleTaskCompleted(id: string) {
  const task = await tasksRepo.getById(id);
  if (!task) {
    throw new Error("Task not found");
  }
  await tasksRepo.update(id, {
    completed: !task.completed,
  });
  revalidatePath("/tasks");
}
