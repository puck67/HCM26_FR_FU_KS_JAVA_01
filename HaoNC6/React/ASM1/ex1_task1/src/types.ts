export interface Task {
  id: string;
  name: string;
  description: string;
  status: 'todo' | 'in_progress' | 'completed';
  createdAt: string;
}

export interface TaskState {
  tasks: Task[];
  loading: boolean;
  error: string | null;
}

export type TaskAction =
  | { type: 'FETCH_START' }
  | { type: 'FETCH_SUCCESS'; payload: Task[] }
  | { type: 'FETCH_ERROR'; payload: string }
  | { type: 'ADD_TASK'; payload: Task }
  | { type: 'UPDATE_TASK'; payload: Task }
  | { type: 'DELETE_TASK'; payload: string };
