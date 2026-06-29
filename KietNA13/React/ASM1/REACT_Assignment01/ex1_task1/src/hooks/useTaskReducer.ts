import { useReducer } from 'react';
import type { Task, TaskAction } from '../types/task';

const taskReducer = (state: Task[], action: TaskAction): Task[] => {
  switch (action.type) {
    case 'SET_TASKS':
      return action.payload;
    case 'ADD_TASK':
      return [...state, action.payload];
    case 'UPDATE_TASK':
      return state.map((t) => (t.id === action.payload.id ? action.payload : t));
    case 'DELETE_TASK':
      return state.filter((t) => t.id !== action.payload);
  }
};

export const useTaskReducer = () => {
  const [tasks, dispatch] = useReducer(taskReducer, []);
  return { tasks, dispatch };
};
