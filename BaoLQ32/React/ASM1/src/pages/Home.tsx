import { Link } from "react-router-dom";
import { useTasks } from "../TaskContext";

export default function Home() {
  const { tasks, loading } = useTasks();

  const hoanThanh = tasks.filter((t) => t.status === "Hoàn thành").length;
  const dangThucHien = tasks.filter((t) => t.status === "Đang thực hiện").length;
  const choXuLy = tasks.filter((t) => t.status === "Chờ xử lý").length;

  return (
    <div className="fade-in">
      {/* Banner chào mừng */}
      <div className="mb-10 text-center">
        <h2 className="text-4xl font-bold text-white mb-3 tracking-tight">
          Chào mừng đến với <span className="text-violet-400">TaskBoard</span>
        </h2>
        <p className="text-slate-400 text-lg max-w-xl mx-auto">
          Hệ thống quản lý công việc hiện đại được xây dựng bằng React, TypeScript, Formik và React Router.
        </p>
      </div>

      {/* Lưới thống kê */}
      <div className="grid grid-cols-1 sm:grid-cols-3 gap-5 mb-10">
        <div className="bg-white/5 border border-white/10 rounded-2xl p-6 hover:-translate-y-1 transition-transform">
          <div className="flex items-center justify-between mb-3">
            <span className="text-sm text-slate-400 font-medium uppercase tracking-wide">Hoàn thành</span>
            <span className="w-8 h-8 rounded-full bg-emerald-500/20 flex items-center justify-center text-emerald-400 text-sm font-bold">✓</span>
          </div>
          <div className="text-4xl font-bold text-white">{loading ? "—" : hoanThanh}</div>
          <div className="text-xs text-slate-500 mt-1">Công việc đã hoàn thành</div>
        </div>

        <div className="bg-white/5 border border-white/10 rounded-2xl p-6 hover:-translate-y-1 transition-transform">
          <div className="flex items-center justify-between mb-3">
            <span className="text-sm text-slate-400 font-medium uppercase tracking-wide">Đang thực hiện</span>
            <span className="w-8 h-8 rounded-full bg-blue-500/20 flex items-center justify-center text-blue-400 text-sm font-bold">⚡</span>
          </div>
          <div className="text-4xl font-bold text-white">{loading ? "—" : dangThucHien}</div>
          <div className="text-xs text-slate-500 mt-1">Công việc đang triển khai</div>
        </div>

        <div className="bg-white/5 border border-white/10 rounded-2xl p-6 hover:-translate-y-1 transition-transform">
          <div className="flex items-center justify-between mb-3">
            <span className="text-sm text-slate-400 font-medium uppercase tracking-wide">Chờ xử lý</span>
            <span className="w-8 h-8 rounded-full bg-amber-500/20 flex items-center justify-center text-amber-400 text-sm font-bold">⏱</span>
          </div>
          <div className="text-4xl font-bold text-white">{loading ? "—" : choXuLy}</div>
          <div className="text-xs text-slate-500 mt-1">Công việc chưa bắt đầu</div>
        </div>
      </div>

      {/* Hướng dẫn nhanh */}
      <div className="bg-white/5 border border-white/10 rounded-2xl p-8">
        <h3 className="text-white text-xl font-semibold mb-3">Hướng Dẫn Nhanh</h3>
        <p className="text-slate-400 text-sm leading-relaxed mb-5">
          Dự án này minh hoạ hệ thống component CRUD tái sử dụng trong React + TypeScript, sử dụng{" "}
          <code className="bg-white/10 px-1.5 py-0.5 rounded text-violet-300">useState</code>{" "}
          cho các điều khiển giao diện,{" "}
          <code className="bg-white/10 px-1.5 py-0.5 rounded text-violet-300">useReducer</code>{" "}
          để quản lý danh sách công việc, và{" "}
          <code className="bg-white/10 px-1.5 py-0.5 rounded text-violet-300">useEffect</code>{" "}
          để mô phỏng gọi dữ liệu từ REST API.
        </p>
        <Link
          to="/tasks"
          className="inline-flex items-center gap-2 bg-violet-600 hover:bg-violet-700 text-white font-semibold px-5 py-2.5 rounded-xl transition-colors"
        >
          Đến bảng công việc →
        </Link>
      </div>
    </div>
  );
}
