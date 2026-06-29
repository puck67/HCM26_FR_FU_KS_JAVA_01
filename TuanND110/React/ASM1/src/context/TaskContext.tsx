import React, {
  createContext,
  useContext,
  useReducer,
  useCallback,
} from 'react';
import type { Task } from '../types/task';
import { taskApi } from '../services/api';

// ── State ────────────────────────────────────────────────────
interface TaskState {
  tasks: Task[];
  loading: boolean;
  error: string | null;
}

type TaskAction =
  | { type: 'FETCH_START' }
  | { type: 'FETCH_SUCCESS'; payload: Task[] }
  | { type: 'FETCH_ERROR'; payload: string }
  | { type: 'ADD_TASK'; payload: Task }
  | { type: 'UPDATE_TASK'; payload: Task }
  | { type: 'DELETE_TASK'; payload: string };

function reducer(state: TaskState, action: TaskAction): TaskState {
  switch (action.type) {
    case 'FETCH_START':
      return { ...state, loading: true, error: null };
    case 'FETCH_SUCCESS':
      return { tasks: action.payload, loading: false, error: null };
    case 'FETCH_ERROR':
      return { ...state, loading: false, error: action.payload };
    case 'ADD_TASK':
      return { ...state, tasks: [action.payload, ...state.tasks] };
    case 'UPDATE_TASK':
      return {
        ...state,
        tasks: state.tasks.map((t) =>
          t.id === action.payload.id ? action.payload : t
        ),
      };
    case 'DELETE_TASK':
      return {
        ...state,
        tasks: state.tasks.filter((t) => t.id !== action.payload),
      };
    default:
      return state;
  }
}

// ── Context ──────────────────────────────────────────────────
interface TaskContextValue {
  tasks: Task[];
  loading: boolean;
  error: string | null;
  fetchTasks: () => Promise<void>;
  createTask: (name: string, description: string) => Promise<Task>;
  updateTask: (id: string, data: Partial<Pick<Task, 'name' | 'description' | 'status'>>) => Promise<Task>;
  deleteTask: (id: string) => Promise<void>;
  toggleTaskCompleted: (id: string) => Promise<Task>;
}

const TaskContext = createContext<TaskContextValue | null>(null);

export const TaskProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [state, dispatch] = useReducer(reducer, {
    tasks: [],
    loading: false,
    error: null,
  });

  const fetchTasks = useCallback(async () => {
    dispatch({ type: 'FETCH_START' });
    try {
      const tasks = await taskApi.getTasks();
      dispatch({ type: 'FETCH_SUCCESS', payload: tasks });
    } catch (e: unknown) {
      dispatch({ type: 'FETCH_ERROR', payload: (e as Error).message });
    }
  }, []);

  const createTask = useCallback(async (name: string, description: string): Promise<Task> => {
    const task = await taskApi.createTask(name, description);
    dispatch({ type: 'ADD_TASK', payload: task });
    return task;
  }, []);

  const updateTask = useCallback(
    async (id: string, data: Partial<Pick<Task, 'name' | 'description' | 'status'>>): Promise<Task> => {
      const updated = await taskApi.updateTask(id, data);
      dispatch({ type: 'UPDATE_TASK', payload: updated });
      return updated;
    },
    []
  );

  const deleteTask = useCallback(async (id: string): Promise<void> => {
    await taskApi.deleteTask(id);
    dispatch({ type: 'DELETE_TASK', payload: id });
  }, []);

  const toggleTaskCompleted = useCallback(async (id: string): Promise<Task> => {
    const updated = await taskApi.toggleTaskCompleted(id);
    dispatch({ type: 'UPDATE_TASK', payload: updated });
    return updated;
  }, []);

  return (
    <TaskContext.Provider
      value={{ ...state, fetchTasks, createTask, updateTask, deleteTask, toggleTaskCompleted }}
    >
      {children}
    </TaskContext.Provider>
  );
};

export const useTasks = (): TaskContextValue => {
  const ctx = useContext(TaskContext);
  if (!ctx) throw new Error('useTasks must be used inside <TaskProvider>');
  return ctx;
};
