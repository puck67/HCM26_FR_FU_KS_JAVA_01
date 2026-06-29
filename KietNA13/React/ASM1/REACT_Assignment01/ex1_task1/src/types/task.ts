export interface Task {
  id: number;
  name: string;
  description?: string;
  completed: boolean;
  userId: number;
}

export interface JsonPlaceholderTodo {
  id: number;
  userId: number;
  title: string;
  completed: boolean;
}

export interface TaskFormValues {
  name: string;
  description: string;
}

export type TaskFilter = 'all' | 'completed' | 'pending';

export type TaskAction =
  | { type: 'ADD_TASK'; payload: Task }
  | { type: 'UPDATE_TASK'; payload: Task }
  | { type: 'DELETE_TASK'; payload: number }
  | { type: 'SET_TASKS'; payload: Task[] };
