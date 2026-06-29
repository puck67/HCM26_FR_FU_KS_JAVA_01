import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useTasks } from "../TaskContext";
import type { Task } from "../taskReducer";
import TaskFormModal from "./TaskFormModal";

const mauUuTien: Record<string, string> = {
  Cao: "bg-red-500/15 text-red-400",
  "Trung bình": "bg-orange-500/15 text-orange-400",
  Thấp: "bg-slate-500/15 text-slate-400",
};

const mauTrangThai: Record<string, string> = {
  "Hoàn thành": "bg-emerald-500/15 text-emerald-400",
  "Đang thực hiện": "bg-indigo-500/15 text-indigo-400",
  "Chờ xử lý": "bg-amber-500/15 text-amber-400",
};

export default function Tasks() {
  const { tasks, loading, error, dispatch } = useTasks();
  const navigate = useNavigate();

  // useState cho các điều khiển giao diện
  const [tuKhoaTimKiem, setTuKhoaTimKiem] = useState("");
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [modalMode, setModalMode] = useState<"add" | "edit">("add");
  const [congViecChon, setCongViecChon] = useState<Task | undefined>(undefined);

  const handleThem = () => {
    setCongViecChon(undefined);
    setModalMode("add");
    setIsModalOpen(true);
  };

  const handleSua = (task: Task) => {
    setCongViecChon(task);
    setModalMode("edit");
    setIsModalOpen(true);
  };

  const handleXoa = (task: Task) => {
    if (confirm(`Bạn có chắc muốn xóa công việc "${task.name}"?`)) {
      dispatch({ type: "DELETE_TASK", payload: task.id });
    }
  };

  const danhSachLoc = tasks.filter(
    (t) =>
      t.name.toLowerCase().includes(tuKhoaTimKiem.toLowerCase()) ||
      t.description.toLowerCase().includes(tuKhoaTimKiem.toLowerCase()) ||
      t.status.toLowerCase().includes(tuKhoaTimKiem.toLowerCase())
  );

  return (
    <div className="fade-in">
      {/* Tiêu đề trang + Nút thêm */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 mb-6">
        <div>
          <h2 className="text-2xl font-bold text-white">Danh Sách Công Việc</h2>
          <p className="text-sm text-slate-400 mt-1">
            {loading ? "Đang tải..." : `Tìm thấy ${danhSachLoc.length} công việc`}
          </p>
        </div>
        <button
          onClick={handleThem}
          className="inline-flex items-center gap-2 bg-violet-600 hover:bg-violet-700 text-white font-semibold px-4 py-2.5 rounded-xl transition-colors text-sm"
        >
          + Thêm Công Việc
        </button>
      </div>

      {/* Thanh tìm kiếm */}
      <div className="mb-5">
        <input
          type="text"
          placeholder="Tìm kiếm theo tên, trạng thái..."
          value={tuKhoaTimKiem}
          onChange={(e) => setTuKhoaTimKiem(e.target.value)}
          className="w-full sm:w-80 bg-white/5 border border-white/10 rounded-xl px-4 py-2.5 text-white text-sm placeholder-slate-500 outline-none focus:border-violet-500 focus:ring-2 focus:ring-violet-500/20 transition-all"
        />
      </div>

      {/* Trạng thái đang tải */}
      {loading && (
        <div className="flex flex-col items-center justify-center py-16 text-slate-400">
          <div className="w-10 h-10 rounded-full border-2 border-violet-500 border-t-transparent animate-spin mb-4" />
          <p>Đang tải dữ liệu từ hệ thống...</p>
        </div>
      )}

      {/* Trạng thái lỗi */}
      {error && (
        <div className="bg-red-500/10 border border-red-500/30 rounded-xl p-4 text-red-400 text-sm">
          ⚠️ {error}
        </div>
      )}

      {/* Bảng danh sách công việc */}
      {!loading && !error && (
        <div className="bg-white/5 border border-white/10 rounded-2xl overflow-hidden">
          {danhSachLoc.length === 0 ? (
            <div className="py-16 text-center text-slate-400">
              Không tìm thấy công việc nào. Hãy thêm công việc mới!
            </div>
          ) : (
            <table className="w-full">
              <thead>
                <tr className="border-b border-white/10 bg-white/[0.02]">
                  <th className="text-left text-xs uppercase tracking-widest text-slate-400 font-semibold px-6 py-4">Công Việc</th>
                  <th className="text-left text-xs uppercase tracking-widest text-slate-400 font-semibold px-4 py-4 hidden md:table-cell">Ưu Tiên</th>
                  <th className="text-left text-xs uppercase tracking-widest text-slate-400 font-semibold px-4 py-4">Trạng Thái</th>
                  <th className="text-right text-xs uppercase tracking-widest text-slate-400 font-semibold px-6 py-4">Thao Tác</th>
                </tr>
              </thead>
              <tbody>
                {danhSachLoc.map((task) => (
                  <tr
                    key={task.id}
                    className="border-b border-white/5 hover:bg-white/[0.02] transition-colors group"
                  >
                    <td className="px-6 py-4">
                      <button
                        onClick={() => navigate(`/tasks/${task.id}`)}
                        className="text-left group-hover:text-violet-400 transition-colors bg-transparent border-none p-0 cursor-pointer w-full"
                      >
                        <div className="font-medium text-white text-sm group-hover:text-violet-300 transition-colors line-clamp-1">
                          {task.name}
                        </div>
                        <div className="text-slate-500 text-xs mt-0.5 line-clamp-1">
                          {task.description || "Chưa có mô tả"}
                        </div>
                      </button>
                    </td>
                    <td className="px-4 py-4 hidden md:table-cell">
                      <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-semibold ${mauUuTien[task.priority] ?? ""}`}>
                        {task.priority}
                      </span>
                    </td>
                    <td className="px-4 py-4">
                      <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-semibold ${mauTrangThai[task.status] ?? ""}`}>
                        {task.status}
                      </span>
                    </td>
                    <td className="px-6 py-4">
                      <div className="flex gap-2 justify-end">
                        <button
                          onClick={() => navigate(`/tasks/${task.id}`)}
                          className="text-xs px-3 py-1.5 rounded-lg bg-blue-500/15 text-blue-400 hover:bg-blue-500/25 transition-colors font-medium"
                        >
                          Chi tiết
                        </button>
                        <button
                          onClick={() => handleSua(task)}
                          className="text-xs px-3 py-1.5 rounded-lg bg-violet-500/15 text-violet-400 hover:bg-violet-500/25 transition-colors font-medium"
                        >
                          Sửa
                        </button>
                        <button
                          onClick={() => handleXoa(task)}
                          className="text-xs px-3 py-1.5 rounded-lg bg-red-500/15 text-red-400 hover:bg-red-500/25 transition-colors font-medium"
                        >
                          Xóa
                        </button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
      )}

      {/* Modal form công việc */}
      <TaskFormModal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        mode={modalMode}
        initialData={congViecChon}
      />
    </div>
  );
}
