import { useContext } from 'react';
import { TaskContext } from '../context/TaskContext';

// Hook để truy cập dữ liệu tasks dùng chung từ TaskContext
export function useTasks() {
  const context = useContext(TaskContext);
  if (!context) {
    throw new Error('useTasks phải được sử dụng bên trong TaskProvider');
  }
  return context;
}
