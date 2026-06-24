import { useReducer } from 'react';
import type { Task } from '../api/mockApi';

export type TaskState = {
  tasks: Task[];
  loading: boolean;
  error: string | null;
};

type TaskAction =
  | { type: 'FETCH_INIT' }
  | { type: 'FETCH_SUCCESS'; payload: Task[] }
  | { type: 'FETCH_FAILURE'; payload: string }
  | { type: 'CREATE'; payload: Task }
  | { type: 'UPDATE'; payload: Task }
  | { type: 'DELETE'; payload: string };

const taskReducer = (state: TaskState, action: TaskAction): TaskState => {
  switch (action.type) {
    case 'FETCH_INIT':
      return { ...state, loading: true, error: null };
    case 'FETCH_SUCCESS':
      return { ...state, loading: false, tasks: action.payload };
    case 'FETCH_FAILURE':
      return { ...state, loading: false, error: action.payload };
    case 'CREATE':
      return { ...state, tasks: [...state.tasks, action.payload] };
    case 'UPDATE':
      return {
        ...state,
        tasks: state.tasks.map(t => (t.id === action.payload.id ? action.payload : t)),
      };
    case 'DELETE':
      return {
        ...state,
        tasks: state.tasks.filter(t => t.id !== action.payload),
      };
    default:
      return state;
  }
};

export const useTaskReducer = () => {
  const [state, dispatch] = useReducer(taskReducer, {
    tasks: [],
    loading: false,
    error: null,
  });

  return { state, dispatch };
};
