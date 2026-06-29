import React, { useReducer, useState, useEffect, useCallback, type ReactNode } from 'react';
import type { Task, TaskAction, TaskStatus } from '../types/task';
import { fetchTasksApi, saveStoredTasks } from '../api/task';
import { TaskContext } from './TaskContext';
import { Toast } from '../components/common/Toast';

const taskReducer = (state: Task[], action: TaskAction): Task[] => {
  switch (action.type) {
    case 'SET_TASKS':
      return action.payload || [];
    case 'ADD_TASK':
      return [action.payload, ...state];
    case 'UPDATE_TASK':
      return state.map((task) => (task.id === action.payload.id ? action.payload : task));
    case 'DELETE_TASK':
      return state.filter((task) => task.id !== action.payload);
    default:
      return state;
  }
};

export const TaskProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
  const [tasks, dispatch] = useReducer(taskReducer, []);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  const [searchTerm, setSearchTerm] = useState<string>('');
  const [statusFilter, setStatusFilter] = useState<string>('ALL');
  const [isModalOpen, setIsModalOpen] = useState<boolean>(false);
  const [editingTask, setEditingTask] = useState<Task | null>(null);
  const [deletingTaskId, setDeletingTaskId] = useState<string | null>(null);
  const [toastMessage, setToastMessage] = useState<string | null>(null);

  const showToast = useCallback((msg: string) => {
    setToastMessage(msg);
    setTimeout(() => {
      setToastMessage((current) => (current === msg ? null : current));
    }, 3000);
  }, []);

  const clearToast = useCallback(() => {
    setToastMessage(null);
  }, []);

  const loadTasks = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await fetchTasksApi();
      dispatch({ type: 'SET_TASKS', payload: data });
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Lỗi khi tải danh sách công việc');
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    loadTasks();
  }, [loadTasks]);

  useEffect(() => {
    if (!loading && Array.isArray(tasks)) {
      saveStoredTasks(tasks);
    }
  }, [tasks, loading]);

  const addTask = (name: string, description: string, status: TaskStatus = 'Pending') => {
    const cleanName = (name || '').trim();
    const cleanDesc = (description || '').trim();
    if (!cleanName) return;

    const newTask: Task = {
      id: `task-${Date.now()}`,
      name: cleanName,
      description: cleanDesc,
      status,
      createdAt: new Date().toISOString().split('T')[0]
    };
    dispatch({ type: 'ADD_TASK', payload: newTask });
    showToast('Đã tạo công việc thành công!');
  };

  const updateTask = (id: string, name: string, description: string, status?: TaskStatus) => {
    if (!id) return;
    const existing = tasks.find((t) => t.id === id);
    if (!existing) return;

    const updated: Task = {
      ...existing,
      name: (name || '').trim(),
      description: (description || '').trim(),
      status: status || existing.status
    };
    dispatch({ type: 'UPDATE_TASK', payload: updated });
    showToast('Đã cập nhật công việc!');
  };

  const deleteTask = (id: string) => {
    if (!id) return;
    dispatch({ type: 'DELETE_TASK', payload: id });
    showToast('Đã xóa công việc khỏi hệ thống!');
  };

  return (
    <TaskContext.Provider
      value={{
        tasks,
        loading,
        error,
        searchTerm,
        setSearchTerm,
        statusFilter,
        setStatusFilter,
        isModalOpen,
        setIsModalOpen,
        editingTask,
        setEditingTask,
        deletingTaskId,
        setDeletingTaskId,
        toastMessage,
        showToast,
        clearToast,
        addTask,
        updateTask,
        deleteTask,
        reloadTasks: loadTasks
      }}
    >
      {children}
      <Toast message={toastMessage} onClose={clearToast} />
    </TaskContext.Provider>
  );
};

export default TaskProvider;
