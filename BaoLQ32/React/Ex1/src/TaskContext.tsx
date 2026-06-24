import React, { useReducer, useState, useEffect, createContext, useContext } from "react";
import { taskReducer, type Task, type TaskAction } from "./taskReducer";

interface TaskContextType {
  tasks: Task[];
  dispatch: React.Dispatch<TaskAction>;
  loading: boolean;
  error: string | null;
  setTasks: (tasks: Task[]) => void;
  setError: (err: string | null) => void;
  setLoading: (loading: boolean) => void;
}

const TaskContext = createContext<TaskContextType | undefined>(undefined);

const initialTasks: Task[] = [
  { id: "1", name: "Triển khai hệ thống xác thực JWT", description: "Viết API đăng ký, đăng nhập và bảo mật các router React với JWT token.", status: "Đang thực hiện", priority: "Cao" },
  { id: "2", name: "Tối ưu hóa chỉ mục Postgres CSDL", description: "Phân tích EXPLAIN ANALYZE các câu truy vấn chậm và thêm index phù hợp.", status: "Đang chờ", priority: "Trung bình" },
  { id: "3", name: "Viết tài liệu Swagger / OpenAPI", description: "Hoàn tất tài liệu API Swagger cho các endpoint của dịch vụ lưu trú.", status: "Hoàn thành", priority: "Thấp" },
  { id: "4", name: "Thiết kế giao diện Dashboard", description: "Dựng khung layout Sidebar, Header và các biểu đồ bằng Chart.js.", status: "Hoàn thành", priority: "Cao" }
];

export function TaskProvider({ children }: { children: React.ReactNode }) {
  const [tasks, dispatch] = useReducer(taskReducer, []);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  // Giả lập gọi REST API dùng useEffect
  useEffect(() => {
    const fetchTasks = async () => {
      try {
        setLoading(true);
        // Giả lập thời gian phản hồi mạng 1.2s
        await new Promise((resolve) => setTimeout(resolve, 1200));
        
        // Nạp dữ liệu thành công
        dispatch({ type: "SET_TASKS", payload: initialTasks });
        setError(null);
      } catch (err) {
        setError("Lỗi tải danh sách công việc từ máy chủ.");
      } finally {
        setLoading(false);
      }
    };
    fetchTasks();
  }, []);

  const setTasks = (payload: Task[]) => {
    dispatch({ type: "SET_TASKS", payload });
  };

  return (
    <TaskContext.Provider value={{ tasks, dispatch, loading, error, setTasks, setError, setLoading }}>
      {children}
    </TaskContext.Provider>
  );
}

export function useTasks() {
  const context = useContext(TaskContext);
  if (!context) {
    throw new Error("useTasks phải được đặt bên trong TaskProvider");
  }
  return context;
}
