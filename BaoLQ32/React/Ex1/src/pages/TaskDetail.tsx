import { useParams, useNavigate, Link } from "react-router-dom";
import { useTasks } from "../TaskContext";

export default function TaskDetail() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const { tasks, loading, dispatch } = useTasks();

  const task = tasks.find((t) => t.id === id);

  if (loading) {
    return (
      <div className="crud-container" style={{ padding: "40px", textAlign: "center" }}>
        <div className="spinner" style={{ margin: "0 auto 20px" }}></div>
        <p>Đang truy xuất thông tin chi tiết...</p>
      </div>
    );
  }

  if (!task) {
    return (
      <div className="crud-container" style={{ padding: "40px", textAlign: "center" }}>
        <h3 style={{ color: "var(--color-rose)" }}>Không tìm thấy công việc!</h3>
        <p style={{ color: "var(--text-secondary)", margin: "15px 0" }}>Công việc này không tồn tại hoặc đã bị xóa khỏi hệ thống.</p>
        <Link to="/tasks" className="btn-cancel" style={{ textDecoration: "none", display: "inline-block" }}>
          Quay lại danh sách
        </Link>
      </div>
    );
  }

  const getPriorityClass = (priority: string) => {
    if (priority === "Cao") return "badge-danger";
    if (priority === "Trung bình") return "badge-amber";
    return "badge-muted";
  };

  const getStatusClass = (status: string) => {
    if (status === "Hoàn thành") return "badge-success";
    if (status === "Đang thực hiện") return "badge-warning";
    return "badge-info";
  };

  const handleToggleStatus = () => {
    const nextStatus = 
      task.status === "Đang chờ" 
        ? "Đang thực hiện" 
        : task.status === "Đang thực hiện" 
        ? "Hoàn thành" 
        : "Đang chờ";
        
    dispatch({
      type: "UPDATE_TASK",
      payload: { ...task, status: nextStatus }
    });
  };

  return (
    <div className="task-detail-page fade-in" style={{ maxWidth: "800px", margin: "0 auto" }}>
      <div style={{ marginBottom: "20px" }}>
        <Link to="/tasks" className="btn-cancel" style={{ textDecoration: "none", display: "inline-flex", alignItems: "center", gap: "8px", fontSize: "14px" }}>
          ← Quay lại danh sách công việc
        </Link>
      </div>

      <div className="crud-container" style={{ gap: "25px", padding: "35px" }}>
        <div style={{ display: "flex", justifyContent: "space-between", alignItems: "flex-start", borderBottom: "1px solid var(--border-color)", paddingBottom: "20px" }}>
          <div>
            <span style={{ fontSize: "12px", color: "var(--text-muted)", fontWeight: 600 }}>MÃ CÔNG VIỆC: #{task.id}</span>
            <h2 style={{ margin: "5px 0 0 0", color: "#fff", fontSize: "24px" }}>{task.name}</h2>
          </div>
          <div style={{ display: "flex", gap: "10px" }}>
            <span className={`badge ${getPriorityClass(task.priority)}`}>{task.priority}</span>
            <span className={`badge ${getStatusClass(task.status)}`}>{task.status}</span>
          </div>
        </div>

        <div>
          <h4 style={{ margin: "0 0 10px 0", color: "var(--text-secondary)", fontSize: "14px" }}>MÔ TẢ CHI TIẾT</h4>
          <p style={{ color: "var(--text-primary)", fontSize: "15px", lineHeight: "1.6", background: "rgba(255,255,255,0.01)", border: "1px solid var(--border-color)", padding: "18px", borderRadius: "var(--radius-sm)", margin: 0 }}>
            {task.description || "Không có mô tả bổ sung cho công việc này."}
          </p>
        </div>

        <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", paddingTop: "15px", borderTop: "1px solid var(--border-color)" }}>
          <button className="btn-secondary" onClick={handleToggleStatus}>
            Đổi trạng thái: {task.status === "Đang chờ" ? "Tiến hành làm" : task.status === "Đang thực hiện" ? "Đóng/Hoàn thành" : "Làm lại"}
          </button>
          
          <button className="btn-submit" onClick={() => navigate("/tasks")}>
            Xác nhận & Quay lại
          </button>
        </div>
      </div>
    </div>
  );
}
