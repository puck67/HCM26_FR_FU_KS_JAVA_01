import React, { createContext, useContext, useReducer, useEffect, useCallback } from 'react';
import { api } from '../services/api';
import type { Task } from '../services/api';

// State interface
interface TaskState {
  tasks: Task[];
  loading: boolean;
  error: string | null;
}

// Actions
type TaskAction =
  | { type: 'FETCH_START' }
  | { type: 'FETCH_SUCCESS'; payload: Task[] }
  | { type: 'FETCH_FAILURE'; payload: string }
  | { type: 'ADD_TASK'; payload: Task }
  | { type: 'UPDATE_TASK'; payload: Task }
  | { type: 'DELETE_TASK'; payload: string };

// Initial State
const initialState: TaskState = {
  tasks: [],
  loading: false,
  error: null,
};

// Reducer function
const taskReducer = (state: TaskState, action: TaskAction): TaskState => {
  switch (action.type) {
    case 'FETCH_START':
      return { ...state, loading: true, error: null };
    case 'FETCH_SUCCESS':
      return { ...state, loading: false, tasks: action.payload, error: null };
    case 'FETCH_FAILURE':
      return { ...state, loading: false, error: action.payload };
    case 'ADD_TASK':
      return { ...state, tasks: [...state.tasks, action.payload] };
    case 'UPDATE_TASK':
      return {
        ...state,
        tasks: state.tasks.map((task) =>
          task.id === action.payload.id ? action.payload : task
        ),
      };
    case 'DELETE_TASK':
      return {
        ...state,
        tasks: state.tasks.filter((task) => task.id !== action.payload),
      };
    default:
      return state;
  }
};

// Context Value Interface
interface TaskContextType {
  state: TaskState;
  fetchTasks: () => Promise<void>;
  addTask: (name: string, description: string) => Promise<void>;
  updateTask: (id: string, fields: Partial<Omit<Task, 'id'>>) => Promise<void>;
  deleteTask: (id: string) => Promise<void>;
  toggleTaskCompletion: (id: string, completed: boolean) => Promise<void>;
}

const TaskContext = createContext<TaskContextType | undefined>(undefined);

export const TaskProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [state, dispatch] = useReducer(taskReducer, initialState);

  // Fetch tasks
  const fetchTasks = useCallback(async () => {
    dispatch({ type: 'FETCH_START' });
    try {
      const data = await api.getTasks();
      dispatch({ type: 'FETCH_SUCCESS', payload: data });
    } catch (err: any) {
      dispatch({ type: 'FETCH_FAILURE', payload: err.message || 'An error occurred while fetching tasks' });
    }
  }, []);

  // Add task
  const addTask = async (name: string, description: string) => {
    try {
      const newTask = await api.createTask({ name, description });
      dispatch({ type: 'ADD_TASK', payload: newTask });
    } catch (err: any) {
      throw new Error(err.message || 'Failed to create task');
    }
  };

  // Update task
  const updateTask = async (id: string, fields: Partial<Omit<Task, 'id'>>) => {
    try {
      const updatedTask = await api.updateTask(id, fields);
      dispatch({ type: 'UPDATE_TASK', payload: updatedTask });
    } catch (err: any) {
      throw new Error(err.message || 'Failed to update task');
    }
  };

  // Delete task
  const deleteTask = async (id: string) => {
    try {
      const deletedId = await api.deleteTask(id);
      dispatch({ type: 'DELETE_TASK', payload: deletedId });
    } catch (err: any) {
      throw new Error(err.message || 'Failed to delete task');
    }
  };

  // Toggle completion helper
  const toggleTaskCompletion = async (id: string, completed: boolean) => {
    await updateTask(id, { completed });
  };

  // Initial load
  useEffect(() => {
    fetchTasks();
  }, [fetchTasks]);

  return (
    <TaskContext.Provider
      value={{
        state,
        fetchTasks,
        addTask,
        updateTask,
        deleteTask,
        toggleTaskCompletion,
      }}
    >
      {children}
    </TaskContext.Provider>
  );
};

export const useTasks = (): TaskContextType => {
  const context = useContext(TaskContext);
  if (!context) {
    throw new Error('useTasks must be used within a TaskProvider');
  }
  return context;
};
