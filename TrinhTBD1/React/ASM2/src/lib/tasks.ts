import fs from 'fs';
import path from 'path';
import { Task } from '@/types/task';

const dataFilePath = path.join(process.cwd(), 'src', 'data', 'tasks.json');

// Memory fallback in case of read/write issues in constrained serverless/SSR environments
let inMemoryTasks: Task[] = [
  {
    "id": "task-1",
    "name": "Complete React Assignment 02",
    "description": "Implement Next.js App Router, Server Actions, Formik, and Tailwind CSS.",
    "completed": false,
    "createdAt": "2026-06-29T08:00:00.000Z"
  },
  {
    "id": "task-2",
    "name": "Review Next.js Caching Strategies",
    "description": "Understand revalidatePath and fetch cache rules for server rendering.",
    "completed": true,
    "createdAt": "2026-06-29T08:30:00.000Z"
  }
];

export async function getTasks(): Promise<Task[]> {
  try {
    if (!fs.existsSync(dataFilePath)) {
      const dir = path.dirname(dataFilePath);
      if (!fs.existsSync(dir)) {
        fs.mkdirSync(dir, { recursive: true });
      }
      fs.writeFileSync(dataFilePath, JSON.stringify(inMemoryTasks, null, 2), 'utf-8');
      return inMemoryTasks;
    }

    const fileData = fs.readFileSync(dataFilePath, 'utf-8');
    if (!fileData || fileData.trim() === '') {
      return inMemoryTasks;
    }
    
    const parsed = JSON.parse(fileData);
    if (Array.isArray(parsed)) {
      inMemoryTasks = parsed;
      return parsed;
    }
    return inMemoryTasks;
  } catch (error) {
    console.error('Error reading tasks.json:', error);
    return inMemoryTasks;
  }
}

export async function getTaskById(id: unknown): Promise<Task | null> {
  if (id === null || id === undefined) return null;
  const taskId = String(id).trim();
  if (!taskId) return null;
  const tasks = await getTasks();
  const found = tasks.find(t => String(t.id) === taskId);
  return found || null;
}

export async function saveTasks(tasks: Task[]): Promise<boolean> {
  try {
    inMemoryTasks = tasks;
    const dir = path.dirname(dataFilePath);
    if (!fs.existsSync(dir)) {
      fs.mkdirSync(dir, { recursive: true });
    }
    fs.writeFileSync(dataFilePath, JSON.stringify(tasks, null, 2), 'utf-8');
    return true;
  } catch (error) {
    console.error('Error writing tasks.json:', error);
    return false;
  }
}
