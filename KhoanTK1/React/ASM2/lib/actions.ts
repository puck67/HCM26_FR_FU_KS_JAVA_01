"use server";

import {
    createTodoInStore,
    updateTodoInStore,
    deleteTodoFromStore,
    toggleTodoCompletedInStore,
} from "@/lib/data";
import { revalidatePath } from "next/cache";
import { redirect } from "next/navigation";

// Private helper to validate inputs
function validateTodoInput(name: string, description: string) {
    if (!name || name.length > 40) {
        throw new Error("Invalid todo name");
    }
    if (description.length > 200) {
        throw new Error("Description is too long");
    }
}

export async function createTodo(formData: FormData) {
    const name = (formData.get("name") as string)?.trim();
    const description = (formData.get("description") as string)?.trim() ?? "";

    validateTodoInput(name, description);

    createTodoInStore(name, description);
    revalidatePath("/todos");
    redirect("/todos");
}

export async function updateTodo(id: string, formData: FormData) {
    const name = (formData.get("name") as string)?.trim();
    const description = (formData.get("description") as string)?.trim() ?? "";

    validateTodoInput(name, description);

    updateTodoInStore(id, name, description);
    revalidatePath("/todos");
    revalidatePath(`/todos/${id}`);
    redirect("/todos");
}

export const deleteTodo = async (id: string) => {
    deleteTodoFromStore(id);
    revalidatePath("/todos");
};

export const toggleTodoCompleted = async (id: string) => {
    toggleTodoCompletedInStore(id);
    revalidatePath("/todos");
};
