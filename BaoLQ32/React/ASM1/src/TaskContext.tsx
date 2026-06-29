import { createContext, useContext, useReducer, useState, useEffect } from "react";
import type { ReactNode } from "react";
import { taskReducer } from "./taskReducer";
import type { Task, TaskAction } from "./taskReducer";

// Dữ liệu mẫu — mô phỏng dữ liệu từ REST API qua useEffect
const DU_LIEU_MAU: Task[] = [
  {
    id: "cv-001",
    name: "Cài đặt dự án React với Vite",
    description: "Khởi tạo dự án React + TypeScript bằng Vite, cài đặt React Router DOM, Formik và Yup.",
    status: "Hoàn thành",
    priority: "Cao",
  },
  {
    id: "cv-002",
    name: "Xây dựng trang danh sách công việc",
    description: "Hiển thị danh sách công việc lấy từ mock API. Hỗ trợ trạng thái đang tải và lỗi.",
    status: "Đang thực hiện",
    priority: "Cao",
  },
  {
    id: "cv-003",
    name: "Tích hợp Formik với xác thực Yup",
    description: "Tạo form nhập liệu với trường Tên và Mô tả. Xác thực: tên bắt buộc (tối đa 40 ký tự), mô tả tuỳ chọn (tối đa 200 ký tự).",
    status: "Đang thực hiện",
    priority: "Trung bình",
  },
  {
    id: "cv-004",
    name: "Thiết kế giao diện với Tailwind CSS",
    description: "Áp dụng Tailwind CSS cho tất cả các trang. Đảm bảo giao diện responsive trên desktop và di động.",
    status: "Chờ xử lý",
    priority: "Trung bình",
  },
  {
    id: "cv-005",
    name: "Thiết lập điều hướng React Router",
    description: "Cài đặt BrowserRouter với các route cho Trang chủ, Danh sách công việc và Chi tiết công việc.",
    status: "Hoàn thành",
    priority: "Thấp",
  },
];

interface TaskContextType {
  tasks: Task[];
  dispatch: React.Dispatch<TaskAction>;
  loading: boolean;
  error: string | null;
}

const TaskContext = createContext<TaskContextType | undefined>(undefined);

export function TaskProvider({ children }: { children: ReactNode }) {
  const [tasks, dispatch] = useReducer(taskReducer, []);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  // Mô phỏng gọi REST API bằng useEffect (thay bằng fetch() thật khi có backend)
  useEffect(() => {
    const layDanhSachCongViec = async () => {
      try {
        setLoading(true);
        // Giả lập độ trễ mạng 1 giây
        await new Promise<void>((resolve) => setTimeout(resolve, 1000));
        dispatch({ type: "SET_TASKS", payload: DU_LIEU_MAU });
        setError(null);
      } catch {
        setError("Không thể tải danh sách công việc. Vui lòng thử lại.");
      } finally {
        setLoading(false);
      }
    };
    layDanhSachCongViec();
  }, []);

  return (
    <TaskContext.Provider value={{ tasks, dispatch, loading, error }}>
      {children}
    </TaskContext.Provider>
  );
}

export function useTasks() {
  const ctx = useContext(TaskContext);
  if (!ctx) throw new Error("useTasks phải được dùng bên trong <TaskProvider>");
  return ctx;
}
