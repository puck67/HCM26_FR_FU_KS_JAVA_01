import { Link } from "react-router-dom";
import { useTasks } from "../TaskContext";

export default function Home() {
  const { tasks, loading } = useTasks();

  const completedTasks = tasks.filter(t => t.status === "Hoàn thành").length;
  const inProgressTasks = tasks.filter(t => t.status === "Đang thực hiện").length;
  const pendingTasks = tasks.filter(t => t.status === "Đang chờ").length;

  return (
    <div className="home-page fade-in">
      <div className="welcome-banner">
        <h2>Xin chào quản trị viên 👋</h2>
        <p>Hôm nay bạn có những chỉ số công việc cần theo dõi dưới đây.</p>
      </div>

      <section className="stats-grid">
        <div className="stat-card">
          <div className="stat-header">
            <span className="stat-title">Đã hoàn thành</span>
            <div className="stat-icon" style={{ background: "rgba(16, 185, 129, 0.15)", color: "#10b981" }}>✓</div>
          </div>
          <div className="stat-value">{loading ? "..." : completedTasks}</div>
          <div className="stat-footer">Công việc đã kết thúc</div>
        </div>

        <div className="stat-card">
          <div className="stat-header">
            <span className="stat-title">Đang thực hiện</span>
            <div className="stat-icon" style={{ background: "rgba(14, 165, 233, 0.15)", color: "#0ea5e9" }}>⚡</div>
          </div>
          <div className="stat-value">{loading ? "..." : inProgressTasks}</div>
          <div className="stat-footer">Công việc đang triển khai</div>
        </div>

        <div className="stat-card">
          <div className="stat-header">
            <span className="stat-title">Đang chờ xử lý</span>
            <div className="stat-icon" style={{ background: "rgba(245, 158, 11, 0.15)", color: "#f59e0b" }}>⏱</div>
          </div>
          <div className="stat-value">{loading ? "..." : pendingTasks}</div>
          <div className="stat-footer">Công việc chờ thực hiện</div>
        </div>
      </section>

      <section className="crud-container" style={{ marginTop: "24px", gap: "15px" }}>
        <h3 style={{ margin: 0, color: "#fff" }}>Lối tắt & Vận hành nhanh</h3>
        <p style={{ color: "var(--text-secondary)", fontSize: "14px", lineHeight: 1.6 }}>
          Hệ thống React Task Dashboard hỗ trợ quản lý công việc theo mô hình <strong>Vite + React Hooks + Reducer</strong>. 
          Bạn có thể chuyển sang bảng công việc để thêm mới đầu việc bằng <strong>Formik & Yup</strong>, sửa đổi hoặc xem chi tiết từng công việc.
        </p>
        <div style={{ marginTop: "10px" }}>
          <Link to="/tasks" className="btn-submit" style={{ textDecoration: "none", display: "inline-flex", gap: "8px", alignItems: "center" }}>
            Quản lý công việc ngay
            <svg style={{ width: "16px", height: "16px" }} fill="none" stroke="currentColor" strokeWidth="2" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" d="M9 5l7 7-7 7" />
            </svg>
          </Link>
        </div>
      </section>
    </div>
  );
}
