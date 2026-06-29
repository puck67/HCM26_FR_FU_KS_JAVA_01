export interface Task {
  id: string;
  name: string;
  description?: string;
  completed: boolean;
  createdAt: string;
}

export interface TaskFormValues {
  name: string;
  description: string;
}
