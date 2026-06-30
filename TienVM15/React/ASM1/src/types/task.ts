export interface Task {
  id: string;
  name: string;
  description: string;
  status: 'Pending' | 'Completed';
  createdAt: string;
}

export type TaskAction =
  | { type: 'SET_TASKS'; payload: Task[] }
  | { type: 'ADD_TASK'; payload: Task }
  | { type: 'UPDATE_TASK'; payload: Task }
  | { type: 'DELETE_TASK'; payload: string };

export function tasksReducer(state: Task[], action: TaskAction): Task[] {
  switch (action.type) {
    case 'SET_TASKS':
      return action.payload;
    case 'ADD_TASK':
      return [action.payload, ...state];
    case 'UPDATE_TASK':
      return state.map((task) =>
        task.id === action.payload.id ? action.payload : task
      );
    case 'DELETE_TASK':
      return state.filter((task) => task.id !== action.payload);
    default:
      return state;
  }
}
