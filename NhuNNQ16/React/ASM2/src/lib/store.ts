import { Task } from '@/types/task';

// -----------------------------------------------------
// In-memory data store (persisted in globalThis to survive Next.js dev reloads)
// -----------------------------------------------------

const initialTasks: Task[] = [
  {
    id: '1',
    name: 'Học React Router',
    description: 'Tìm hiểu về file-based routing trong Next.js App Router',
    completed: true,
    createdAt: new Date('2024-01-01').toISOString(),
  },
  {
    id: '2',
    name: 'Viết Server Actions',
    description: 'Thực hành createTask, updateTask, deleteTask với Next.js',
    completed: false,
    createdAt: new Date('2024-01-02').toISOString(),
  },
  {
    id: '3',
    name: 'Cấu hình Tailwind CSS',
    description: '',
    completed: false,
    createdAt: new Date('2024-01-03').toISOString(),
  },
];

const globalForTasks = globalThis as unknown as {
  tasks: Task[];
};

if (!globalForTasks.tasks) {
  globalForTasks.tasks = initialTasks;
}

// Lấy tất cả tasks
export function getTasks(): Task[] {
  return globalForTasks.tasks;
}

// Lấy một task theo id
export function getTaskById(id: string): Task | undefined {
  return globalForTasks.tasks.find((t) => t.id === id);
}

// Thêm task mới
export function addTask(task: Task): void {
  globalForTasks.tasks.push(task);
}

// Cập nhật task
export function editTask(id: string, data: Partial<Task>): void {
  globalForTasks.tasks = globalForTasks.tasks.map((t) => (t.id === id ? { ...t, ...data } : t));
}

// Xóa task
export function removeTask(id: string): void {
  globalForTasks.tasks = globalForTasks.tasks.filter((t) => t.id !== id);
}

