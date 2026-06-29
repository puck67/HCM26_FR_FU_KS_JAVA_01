import { useParams, useNavigate, Link } from "react-router-dom";
import { useTasks } from "../TaskContext";

const mauUuTien: Record<string, string> = {
  Cao: "bg-red-500/15 text-red-400 border border-red-500/20",
  "Trung bình": "bg-orange-500/15 text-orange-400 border border-orange-500/20",
  Thấp: "bg-slate-500/15 text-slate-400 border border-slate-500/20",
};

const mauTrangThai: Record<string, string> = {
  "Hoàn thành": "bg-emerald-500/15 text-emerald-400 border border-emerald-500/20",
  "Đang thực hiện": "bg-indigo-500/15 text-indigo-400 border border-indigo-500/20",
  "Chờ xử lý": "bg-amber-500/15 text-amber-400 border border-amber-500/20",
};

export default function TaskDetail() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const { tasks, loading, dispatch } = useTasks();

  const congViec = tasks.find((t) => t.id === id);

  if (loading) {
    return (
      <div className="flex flex-col items-center justify-center py-20 text-slate-400">
        <div className="w-10 h-10 rounded-full border-2 border-violet-500 border-t-transparent animate-spin mb-4" />
        <p>Đang tải thông tin chi tiết...</p>
      </div>
    );
  }

  if (!congViec) {
    return (
      <div className="text-center py-20">
        <h3 className="text-2xl font-bold text-red-400 mb-3">Không Tìm Thấy Công Việc!</h3>
        <p className="text-slate-400 mb-6">Công việc này có thể đã bị xóa hoặc không tồn tại.</p>
        <Link
          to="/tasks"
          className="inline-flex items-center gap-2 border border-white/10 text-slate-300 hover:bg-white/5 px-5 py-2.5 rounded-xl text-sm font-medium transition-colors"
        >
          ← Quay lại danh sách
        </Link>
      </div>
    );
  }

  const doiTrangThai = () => {
    const tiepTheo =
      congViec.status === "Chờ xử lý"
        ? "Đang thực hiện"
        : congViec.status === "Đang thực hiện"
        ? "Hoàn thành"
        : "Chờ xử lý";
    dispatch({ type: "UPDATE_TASK", payload: { ...congViec, status: tiepTheo } });
  };

  const trangThaiTiepTheo =
    congViec.status === "Chờ xử lý"
      ? "Đang thực hiện"
      : congViec.status === "Đang thực hiện"
      ? "Hoàn thành"
      : "Chờ xử lý";

  return (
    <div className="max-w-2xl mx-auto fade-in">
      {/* Nút quay lại */}
      <div className="mb-6">
        <Link
          to="/tasks"
          className="inline-flex items-center gap-2 text-slate-400 hover:text-white text-sm transition-colors"
        >
          ← Quay lại danh sách công việc
        </Link>
      </div>

      {/* Card chi tiết */}
      <div className="bg-white/5 border border-white/10 rounded-2xl p-8">
        {/* Header */}
        <div className="flex items-start justify-between pb-5 mb-5 border-b border-white/10">
          <div>
            <p className="text-xs text-slate-500 font-mono mb-1">MÃ SỐ: {congViec.id}</p>
            <h2 className="text-2xl font-bold text-white">{congViec.name}</h2>
          </div>
          <div className="flex gap-2 ml-4 flex-shrink-0">
            <span className={`inline-flex items-center px-3 py-1 rounded-full text-xs font-semibold ${mauUuTien[congViec.priority] ?? ""}`}>
              {congViec.priority}
            </span>
            <span className={`inline-flex items-center px-3 py-1 rounded-full text-xs font-semibold ${mauTrangThai[congViec.status] ?? ""}`}>
              {congViec.status}
            </span>
          </div>
        </div>

        {/* Mô tả */}
        <div className="mb-6">
          <h4 className="text-xs uppercase tracking-widest text-slate-400 font-semibold mb-2">Mô Tả Chi Tiết</h4>
          <p className="text-slate-300 text-sm leading-relaxed bg-white/[0.02] border border-white/10 rounded-xl p-4">
            {congViec.description || "Chưa có mô tả cho công việc này."}
          </p>
        </div>

        {/* Nút thao tác */}
        <div className="flex flex-col sm:flex-row gap-3 pt-4 border-t border-white/10">
          <button
            onClick={doiTrangThai}
            className="flex-1 px-4 py-2.5 rounded-xl bg-white/5 border border-white/10 text-white text-sm font-medium hover:bg-white/10 transition-colors"
          >
            Chuyển sang:{" "}
            <span className="text-violet-400">{trangThaiTiepTheo}</span>
          </button>
          <button
            onClick={() => navigate("/tasks")}
            className="flex-1 px-4 py-2.5 rounded-xl bg-violet-600 hover:bg-violet-700 text-white text-sm font-semibold transition-colors"
          >
            Xác Nhận & Quay Lại
          </button>
        </div>
      </div>
    </div>
  );
}
