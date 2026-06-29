import fs from 'fs';
import path from 'path';

export interface Task {
  id: string;
  name: string;
  description: string;
  completed: boolean;
  createdAt: string;
}

const DATA_FILE = path.join(process.cwd(), 'data', 'tasks.json');

function ensureFileExists() {
  const dir = path.dirname(DATA_FILE);
  if (!fs.existsSync(dir)) {
    fs.mkdirSync(dir, { recursive: true });
  }
  if (!fs.existsSync(DATA_FILE)) {
    fs.writeFileSync(DATA_FILE, JSON.stringify([], null, 2), 'utf-8');
  }
}

export function readTasks(): Task[] {
  ensureFileExists();
  try {
    const content = fs.readFileSync(DATA_FILE, 'utf-8');
    return JSON.parse(content) as Task[];
  } catch (error) {
    console.error('Failed to read tasks:', error);
    return [];
  }
}

export function writeTasks(tasks: Task[]): void {
  ensureFileExists();
  try {
    fs.writeFileSync(DATA_FILE, JSON.stringify(tasks, null, 2), 'utf-8');
  } catch (error) {
    console.error('Failed to write tasks:', error);
  }
}
