import { Task } from '../types/task';

const BASE_URL = 'https://jsonplaceholder.typicode.com/todos';

// Map JSONPlaceholder todos to our Task type
function mapToTask(todo: { id: number; title: string; completed: boolean; userId: number }): Task {
  return {
    id: todo.id,
    name: todo.title,
    description: `Task #${todo.id} assigned to user ${todo.userId}`,
    status: todo.completed ? 'done' : 'todo',
    userId: todo.userId,
    completed: todo.completed,
  };
}

export async function fetchTasks(limit = 20): Promise<Task[]> {
  const response = await fetch(`${BASE_URL}?_limit=${limit}`);
  if (!response.ok) throw new Error('Failed to fetch tasks');
  const data = await response.json();
  return data.map(mapToTask);
}

export async function fetchTaskById(id: number): Promise<Task> {
  const response = await fetch(`${BASE_URL}/${id}`);
  if (!response.ok) throw new Error(`Failed to fetch task with id ${id}`);
  const data = await response.json();
  return mapToTask(data);
}

// For create/update/delete we simulate because JSONPlaceholder doesn't persist
export async function createTask(name: string, description: string): Promise<Task> {
  const response = await fetch(BASE_URL, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ title: name, completed: false, userId: 1 }),
  });
  if (!response.ok) throw new Error('Failed to create task');
  const data = await response.json();
  return {
    id: data.id,
    name,
    description,
    status: 'todo',
    userId: 1,
    completed: false,
  };
}

export async function updateTask(task: Task): Promise<Task> {
  const response = await fetch(`${BASE_URL}/${task.id}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ title: task.name, completed: task.completed, userId: task.userId }),
  });
  if (!response.ok) throw new Error('Failed to update task');
  return task;
}

export async function deleteTask(id: number): Promise<void> {
  const response = await fetch(`${BASE_URL}/${id}`, { method: 'DELETE' });
  if (!response.ok) throw new Error('Failed to delete task');
}
