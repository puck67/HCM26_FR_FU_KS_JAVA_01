export type TaskStatus = 'todo' | 'in-progress' | 'done';

export interface Task {
  id: number;
  name: string;
  description: string;
  status: TaskStatus;
  userId: number;
  completed: boolean;
}

export interface TaskFormValues {
  name: string;
  description: string;
}
