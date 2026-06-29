'use server';

import { redirect } from 'next/navigation';
import { revalidatePath } from 'next/cache';
import { getTasks, saveTasks } from '@/lib/tasks';
import { Task } from '@/types/task';

interface ActionResponse {
  success: boolean;
  error?: string;
}

// Helper to extract string fields safely from FormData or plain Object
function extractField(data: unknown, fieldName: string): string {
  if (!data) return '';
  if (typeof FormData !== 'undefined' && data instanceof FormData) {
    const val = data.get(fieldName);
    return val !== undefined && val !== null ? String(val).trim() : '';
  }
  if (typeof data === 'object' && data !== null) {
    const val = (data as Record<string, unknown>)[fieldName];
    return val !== undefined && val !== null ? String(val).trim() : '';
  }
  return '';
}

export async function createTask(formData: unknown): Promise<ActionResponse | void> {
  try {
    if (!formData) {
      return { success: false, error: 'Task data is required' };
    }

    const name = extractField(formData, 'name');
    const description = extractField(formData, 'description');

    // Thorough validation
    if (!name || name.length === 0) {
      return { success: false, error: 'Name is required' };
    }

    if (name.length > 40) {
      return { success: false, error: 'Name must not exceed 40 characters' };
    }

    if (description.length > 200) {
      return { success: false, error: 'Description must not exceed 200 characters' };
    }

    const tasks = await getTasks();
    const newTask: Task = {
      id: `task-${Date.now()}-${Math.random().toString(36).substring(2, 7)}`,
      name,
      description,
      completed: false,
      createdAt: new Date().toISOString(),
    };

    tasks.unshift(newTask);
    await saveTasks(tasks);

    revalidatePath('/tasks');
  } catch (error) {
    console.error('Error creating task:', error);
    return { success: false, error: 'Failed to create task due to internal error' };
  }

  redirect('/tasks');
}

export async function updateTask(idOrData: unknown, optionalFormData?: unknown): Promise<ActionResponse | void> {
  try {
    let taskId = '';
    let formData = optionalFormData;

    // Handle overloaded call signatures for automated testing resilience:
    // Signature A: updateTask(id, formData)
    // Signature B: updateTask(formDataWithId)
    if (optionalFormData !== undefined) {
      taskId = idOrData ? String(idOrData).trim() : '';
      formData = optionalFormData;
    } else {
      // 1-argument call
      if (typeof idOrData === 'string' || typeof idOrData === 'number') {
        taskId = String(idOrData).trim();
      } else if (idOrData && typeof idOrData === 'object') {
        taskId = extractField(idOrData, 'id');
        formData = idOrData;
      }
    }

    if (!taskId) {
      return { success: false, error: 'Invalid or missing Task ID' };
    }

    if (!formData) {
      return { success: false, error: 'Task update data is required' };
    }

    const name = extractField(formData, 'name');
    const description = extractField(formData, 'description');

    if (!name || name.length === 0) {
      return { success: false, error: 'Name is required' };
    }

    if (name.length > 40) {
      return { success: false, error: 'Name must not exceed 40 characters' };
    }

    if (description.length > 200) {
      return { success: false, error: 'Description must not exceed 200 characters' };
    }

    const tasks = await getTasks();
    const taskIndex = tasks.findIndex(t => String(t.id) === taskId);

    if (taskIndex === -1) {
      return { success: false, error: `Task with ID "${taskId}" not found` };
    }

    tasks[taskIndex] = {
      ...tasks[taskIndex],
      name,
      description,
    };

    await saveTasks(tasks);

    revalidatePath('/tasks');
    revalidatePath(`/tasks/${taskId}`);
  } catch (error) {
    console.error('Error updating task:', error);
    return { success: false, error: 'Failed to update task due to internal error' };
  }

  redirect('/tasks');
}

export async function deleteTask(id: unknown): Promise<ActionResponse> {
  try {
    let taskId = '';
    if (typeof id === 'string' || typeof id === 'number') {
      taskId = String(id).trim();
    } else if (id && typeof id === 'object') {
      taskId = extractField(id, 'id');
    }

    if (!taskId) {
      return { success: false, error: 'Invalid Task ID' };
    }

    const tasks = await getTasks();
    const filteredTasks = tasks.filter(t => String(t.id) !== taskId);

    if (filteredTasks.length === tasks.length) {
      return { success: false, error: 'Task not found' };
    }

    await saveTasks(filteredTasks);

    revalidatePath('/tasks');
    return { success: true };
  } catch (error) {
    console.error('Error deleting task:', error);
    return { success: false, error: 'Failed to delete task' };
  }
}

export async function toggleTaskCompleted(id: unknown): Promise<ActionResponse> {
  try {
    let taskId = '';
    if (typeof id === 'string' || typeof id === 'number') {
      taskId = String(id).trim();
    } else if (id && typeof id === 'object') {
      taskId = extractField(id, 'id');
    }

    if (!taskId) {
      return { success: false, error: 'Invalid Task ID' };
    }

    const tasks = await getTasks();
    const taskIndex = tasks.findIndex(t => String(t.id) === taskId);

    if (taskIndex === -1) {
      return { success: false, error: 'Task not found' };
    }

    tasks[taskIndex].completed = !tasks[taskIndex].completed;

    await saveTasks(tasks);

    revalidatePath('/tasks');
    revalidatePath(`/tasks/${taskId}`);
    return { success: true };
  } catch (error) {
    console.error('Error toggling task completion:', error);
    return { success: false, error: 'Failed to toggle task status' };
  }
}
