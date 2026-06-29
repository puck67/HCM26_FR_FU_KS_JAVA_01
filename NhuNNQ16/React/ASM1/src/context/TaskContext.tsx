import React, { createContext, useEffect, useReducer, useState } from 'react';
import { taskReducer } from '../reducers/taskReducer';
import { Task } from '../types/task';

interface TaskContextType {
  tasks: Task[];
  loading: boolean;
  error: string | null;
  addTask: (name: string, description: string) => void;
  updateTask: (task: Task) => void;
  deleteTask: (id: number) => void;
  toggleComplete: (id: number) => void;
}

export const TaskContext = createContext<TaskContextType | undefined>(undefined);

export function TaskProvider({ children }: { children: React.ReactNode }) {
  const [state, dispatch] = useReducer(taskReducer, { tasks: [] });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchTasks = async () => {
      try {
        setLoading(true);
        setError(null);
        const res = await fetch('https://jsonplaceholder.typicode.com/todos?_limit=10');
        if (!res.ok) throw new Error('Không thể tải dữ liệu từ API. Vui lòng thử lại.');
        const data = await res.json();

        // Chuyển đổi dữ liệu JSONPlaceholder thành định dạng Task
        const tasks: Task[] = data.map((item: { id: number; title: string; completed: boolean }) => ({
          id: item.id,
          name: item.title,
          description: `Đây là mô tả mặc định cho công việc số ${item.id} từ API JSONPlaceholder.`,
          completed: item.completed,
        }));

        dispatch({ type: 'SET_TASKS', payload: tasks });
      } catch (err) {
        setError(err instanceof Error ? err.message : 'Đã xảy ra lỗi');
      } finally {
        setLoading(false);
      }
    };

    fetchTasks();
  }, []);

  const addTask = (name: string, description: string) => {
    const newTask: Task = {
      id: Date.now(),
      name,
      description,
      completed: false,
    };
    dispatch({ type: 'ADD_TASK', payload: newTask });
  };

  const updateTask = (task: Task) => {
    dispatch({ type: 'UPDATE_TASK', payload: task });
  };

  const deleteTask = (id: number) => {
    dispatch({ type: 'DELETE_TASK', payload: id });
  };

  const toggleComplete = (id: number) => {
    dispatch({ type: 'TOGGLE_COMPLETE', payload: id });
  };

  return (
    <TaskContext.Provider
      value={{
        tasks: state.tasks,
        loading,
        error,
        addTask,
        updateTask,
        deleteTask,
        toggleComplete,
      }}
    >
      {children}
    </TaskContext.Provider>
  );
}
