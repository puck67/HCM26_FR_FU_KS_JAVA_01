import { useState, useEffect } from "react";
import { Modal } from "./Modal";

interface LmsExportModalProps {
  isOpen: boolean;
  onClose: () => void;
  entityType: string | null;
  columns: string[];
  onExport: (format: "xlsx" | "csv" | "pdf", columns: string[]) => void;
}

const COLUMNS_VIETNAMESE: Record<string, string> = {
  id: "Mã",
  title: "Tiêu đề",
  instructor: "Giảng viên",
  category: "Danh mục",
  duration: "Thời lượng",
  level: "Cấp độ",
  status: "Trạng thái",
  courseTitle: "Khóa học",
  format: "Định dạng",
  name: "Họ & tên",
  email: "Email",
  joinedDate: "Ngày tham gia",
  studentName: "Học viên",
  enrollmentDate: "Ngày đăng ký",
  paymentStatus: "Thanh toán"
};

const ENTITY_NAMES_VIETNAMESE: Record<string, string> = {
  courses: "Khóa học",
  lessons: "Bài học",
  students: "Học viên",
  enrollments: "Đăng ký & tài chính"
};

export function LmsExportModal({ isOpen, onClose, entityType, columns, onExport }: LmsExportModalProps) {
  const [format, setFormat] = useState<"xlsx" | "csv" | "pdf">("xlsx");
  const [selectedColumns, setSelectedColumns] = useState<string[]>(() => columns);
  const [progress, setProgress] = useState(0);
  const [status, setStatus] = useState<"idle" | "exporting" | "success">("idle");

  // Simulate file compiling progress
  useEffect(() => {
    if (status === "exporting") {
      const interval = setInterval(() => {
        setProgress((prev) => {
          if (prev >= 100) {
            clearInterval(interval);
            setStatus("success");
            onExport(format, selectedColumns);
            return 100;
          }
          return prev + 20;
        });
      }, 100);
      return () => clearInterval(interval);
    }
  }, [status, format, selectedColumns, onExport]);

  if (!isOpen || !entityType) return null;

  const handleToggleColumn = (col: string) => {
    setSelectedColumns((prev) =>
      prev.includes(col) ? prev.filter((c) => c !== col) : [...prev, col]
    );
  };

  const handleToggleAll = () => {
    if (selectedColumns.length === columns.length) {
      setSelectedColumns([]);
    } else {
      setSelectedColumns([...columns]);
    }
  };

  const handleStartExport = () => {
    if (selectedColumns.length === 0) return;
    setStatus("exporting");
    setProgress(0);
  };

  const entityLabel = ENTITY_NAMES_VIETNAMESE[entityType] || entityType.toUpperCase();

  return (
    <Modal
      isOpen={isOpen}
      onClose={onClose}
      title={`Xuất dữ liệu - ${entityLabel}`}
      footer={
        <>
          <button type="button" className="btn btn-secondary" onClick={onClose} disabled={status === "exporting"}>
            Hủy
          </button>
          {status === "idle" && (
            <button type="button" className="btn btn-primary" onClick={handleStartExport} disabled={selectedColumns.length === 0}>
              Xuất tệp
            </button>
          )}
          {status === "success" && (
            <button type="button" className="btn btn-success" onClick={onClose}>
              Đóng
            </button>
          )}
        </>
      }
    >
      <div style={{ display: "flex", flexDirection: "column", gap: "16px" }}>
        {status === "idle" && (
          <>
            <div>
              <p className="text-body-md font-bold" style={{ marginBottom: "8px", color: "var(--color-on-surface)" }}>Định dạng tệp:</p>
              <div style={{ display: "flex", gap: "16px" }}>
                <label style={{ display: "flex", alignItems: "center", gap: "6px", fontSize: "13px", cursor: "pointer" }}>
                  <input type="radio" name="format" checked={format === "xlsx"} onChange={() => setFormat("xlsx")} />
                  Excel (.xlsx)
                </label>
                <label style={{ display: "flex", alignItems: "center", gap: "6px", fontSize: "13px", cursor: "pointer" }}>
                  <input type="radio" name="format" checked={format === "csv"} onChange={() => setFormat("csv")} />
                  CSV (.csv)
                </label>
                <label style={{ display: "flex", alignItems: "center", gap: "6px", fontSize: "13px", cursor: "pointer" }}>
                  <input type="radio" name="format" checked={format === "pdf"} onChange={() => setFormat("pdf")} />
                  PDF (.pdf)
                </label>
              </div>
            </div>

            <div>
              <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: "8px" }}>
                <p className="text-body-md font-bold" style={{ color: "var(--color-on-surface)" }}>Chọn cột hiển thị:</p>
                <button type="button" onClick={handleToggleAll} style={{ background: "none", border: "none", color: "var(--color-primary)", fontSize: "12px", fontWeight: 600, cursor: "pointer" }}>
                  {selectedColumns.length === columns.length ? "Bỏ chọn tất cả" : "Chọn tất cả"}
                </button>
              </div>
              
              <div style={{ display: "grid", gridTemplateColumns: "repeat(2, 1fr)", gap: "10px", padding: "12px", border: "1px solid var(--color-outline-variant)", borderRadius: "var(--radius-md)", backgroundColor: "var(--color-surface-container-low)" }}>
                {columns.map((col) => (
                  <label key={col} style={{ display: "flex", alignItems: "center", gap: "8px", fontSize: "12px", cursor: "pointer", color: "var(--color-on-surface-variant)" }}>
                    <input type="checkbox" checked={selectedColumns.includes(col)} onChange={() => handleToggleColumn(col)} />
                    {COLUMNS_VIETNAMESE[col] || col}
                  </label>
                ))}
              </div>
            </div>
          </>
        )}

        {status === "exporting" && (
          <div style={{ padding: "24px 0", textAlign: "center" }}>
            <span className="material-symbols-outlined" style={{ fontSize: "40px", color: "var(--color-primary)", animation: "spin 0.8s linear infinite", marginBottom: "16px" }}>
              downloading
            </span>
            <p className="text-body-md font-medium" style={{ marginBottom: "8px" }}>Đang biên dịch và tải xuống tệp tin...</p>
            <div style={{ height: "6px", backgroundColor: "var(--color-outline-variant)", borderRadius: "var(--radius-full)", overflow: "hidden", maxWidth: "300px", margin: "0 auto" }}>
              <div style={{ width: "100%", height: "100%", backgroundColor: "var(--color-primary)", borderRadius: "var(--radius-full)", transform: `scaleX(${progress / 100})`, transformOrigin: "left", transition: "transform 0.1s ease" }}></div>
            </div>
            <p className="text-xs text-outline" style={{ marginTop: "6px" }}>{progress}%</p>
          </div>
        )}

        {status === "success" && (
          <div style={{ textAlign: "center", padding: "16px 0" }}>
            <span className="material-symbols-outlined fill-icon" style={{ fontSize: "56px", color: "var(--color-success)", marginBottom: "12px" }}>
              download_done
            </span>
            <h4 className="text-body-lg font-bold" style={{ color: "var(--color-on-surface)" }}>Tải xuống bắt đầu!</h4>
            <p className="text-sm text-outline" style={{ marginTop: "6px" }}>
              Đã tạo tệp xuất dữ liệu thành công cho danh sách <strong>{entityLabel}</strong> dưới định dạng <strong>.{format.toUpperCase()}</strong>.
            </p>
          </div>
        )}
      </div>
    </Modal>
  );
}
