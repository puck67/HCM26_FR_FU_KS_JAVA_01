import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useTasks } from "../TaskContext";
import { CrudTable } from "../components/CrudTable";
import { CrudActionBar } from "../components/CrudActionBar";
import TaskFormModal from "./TaskFormModal";
import type { Task } from "../taskReducer";

export default function Tasks() {
  const { tasks, loading, error, dispatch } = useTasks();
  const navigate = useNavigate();

  // Search filter
  const [searchQuery, setSearchQuery] = useState("");

  // Modal State
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [modalMode, setModalMode] = useState<"add" | "edit">("add");
  const [selectedTask, setSelectedTask] = useState<Task | undefined>(undefined);

  const handleAddClick = () => {
    setSelectedTask(undefined);
    setModalMode("add");
    setIsModalOpen(true);
  };

  const handleEditClick = (task: Task) => {
    setSelectedTask(task);
    setModalMode("edit");
    setIsModalOpen(true);
  };

  const handleDeleteClick = (task: Task) => {
    if (window.confirm(`Bạn có chắc chắn muốn xóa công việc "${task.name}"?`)) {
      dispatch({ type: "DELETE_TASK", payload: task.id });
    }
  };

  const handleImport = () => {
    const maxId = tasks.reduce((max, t) => {
      const num = parseInt(t.id, 10);
      return isNaN(num) ? max : Math.max(max, num);
    }, 0);
    const sample: Task[] = [
      { id: String(maxId + 1), name: "Cấu hình Docker Compose", description: "Viết Dockerfile và docker-compose.yml kết nối web service và db.", status: "Đang chờ", priority: "Cao" },
      { id: String(maxId + 2), name: "Tích hợp dịch vụ thanh toán", description: "Kết nối cổng VNPay hoặc Stripe Sandbox để thử nghiệm checkout.", status: "Đang thực hiện", priority: "Cao" }
    ];
    sample.forEach(t => dispatch({ type: "ADD_TASK", payload: t }));
  };

  const handleExport = () => {
    if (tasks.length === 0) {
      alert("Không có dữ liệu để xuất!");
      return;
    }
    const blob = new Blob([JSON.stringify(tasks, null, 2)], { type: "application/json" });
    const url = URL.createObjectURL(blob);
    const downloadAnchor = document.createElement("a");
    downloadAnchor.href = url;
    downloadAnchor.download = `danh_sach_cong_viec_${Date.now()}.json`;
    document.body.appendChild(downloadAnchor);
    downloadAnchor.click();
    downloadAnchor.remove();
    URL.revokeObjectURL(url);
  };

  const filteredTasks = tasks.filter(
    (t) =>
      t.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
      t.description.toLowerCase().includes(searchQuery.toLowerCase()) ||
      t.status.toLowerCase().includes(searchQuery.toLowerCase()) ||
      t.priority.toLowerCase().includes(searchQuery.toLowerCase())
  );

  return (
    <div className="tasks-page fade-in">
      <div className="crud-container">
        <div className="crud-header">
          <div style={{ display: "flex", flexDirection: "column", gap: "6px" }}>
            <span className="crud-title">Danh sách công việc ({filteredTasks.length})</span>
            <span style={{ fontSize: "12px", color: "var(--text-muted)" }}>Nhấp vào tiêu đề hoặc mô tả để xem chi tiết công việc.</span>
          </div>

          <div style={{ display: "flex", gap: "15px", alignItems: "center" }}>
            <div className="search-bar-container" style={{ padding: "8px 12px", width: "240px" }}>
              <input
                type="text"
                placeholder="Tìm kiếm công việc..."
                className="search-input"
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
              />
            </div>
            <CrudActionBar
              onAdd={handleAddClick}
              onImport={handleImport}
              onExport={handleExport}
            />
          </div>
        </div>

        {loading ? (
          <div className="loading-container">
            <div className="spinner"></div>
            <p>Đang tải danh sách công việc từ hệ thống...</p>
          </div>
        ) : error ? (
          <div className="error-alert">⚠️ {error}</div>
        ) : (
          <CrudTable
            data={filteredTasks.map((t) => ({
              "MÃ SỐ": t.id,
              "TÊN CÔNG VIỆC": t.name,
              "MÔ TẢ CHI TIẾT": t.description.length > 55 ? t.description.slice(0, 52) + "..." : t.description,
              "MỨC ĐỘ ƯU TIÊN": t.priority,
              "TRẠNG THÁI": t.status
            }))}
            idKey="MÃ SỐ"
            onEdit={(row: any) => {
              const original = tasks.find((t) => t.id === row["MÃ SỐ"]);
              if (original) handleEditClick(original);
            }}
            onDelete={(row: any) => {
              const original = tasks.find((t) => t.id === row["MÃ SỐ"]);
              if (original) handleDeleteClick(original);
            }}
            extraActions={(row: any) => [
              {
                title: "Chi tiết",
                action: () => {
                  navigate(`/tasks/${row["MÃ SỐ"]}`);
                },
                style: "btn-info"
              }
            ]}
          />
        )}
      </div>

      <TaskFormModal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        initialData={selectedTask}
        mode={modalMode}
      />
    </div>
  );
}
