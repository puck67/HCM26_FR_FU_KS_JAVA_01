import { useState, useEffect } from "react";
import { Modal } from "./Modal";

interface LmsImportModalProps {
  isOpen: boolean;
  onClose: () => void;
  entityType: "course" | "lesson" | "student" | "enrollment" | null;
  onImport: (items: any[]) => void;
}

// Pure helper function in module scope to satisfy react-doctor rules
const getMockImportedItems = (type: string) => {
  const timestamp = new Date().toISOString().split("T")[0];
  const rand = Math.floor(Math.random() * 100);
  
  if (type === "course") {
    return [{
      id: `C${rand}`,
      title: `Khóa học Nhập khẩu #${rand}`,
      instructor: "Giảng viên Nhập khẩu",
      category: "Frontend Development",
      duration: "30 Hours",
      level: "Beginner",
      status: "Draft"
    }];
  } else if (type === "student") {
    return [{
      id: `S${rand}`,
      name: `Học viên Nhập khẩu #${rand}`,
      email: `student_import${rand}@gmail.com`,
      joinedDate: timestamp,
      status: "Active"
    }];
  } else if (type === "lesson") {
    return [{
      id: `L${rand}`,
      courseTitle: "React & TypeScript Masterclass",
      title: `Bài học nhập khẩu từ Excel #${rand}`,
      duration: "20 mins",
      format: "Video"
    }];
  } else {
    return [{
      id: `E${rand}`,
      studentName: "Alice Johnson",
      courseTitle: "React & TypeScript Masterclass",
      enrollmentDate: timestamp,
      paymentStatus: "Paid"
    }];
  }
};

export function LmsImportModal({ isOpen, onClose, entityType, onImport }: LmsImportModalProps) {
  const [file, setFile] = useState<File | null>(null);
  const [progress, setProgress] = useState(0);
  const [status, setStatus] = useState<"idle" | "uploading" | "success">("idle");

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files[0]) {
      setFile(e.target.files[0]);
    }
  };

  const handleStartImport = () => {
    if (!file) return;
    setStatus("uploading");
    setProgress(0);
  };

  // Simulate file upload progress
  useEffect(() => {
    if (status === "uploading" && entityType) {
      const interval = setInterval(() => {
        setProgress((prev) => {
          if (prev >= 100) {
            clearInterval(interval);
            setStatus("success");
            
            const importedItems = getMockImportedItems(entityType);
            onImport(importedItems);
            return 100;
          }
          return prev + 10;
        });
      }, 150);
      return () => clearInterval(interval);
    }
  }, [status, entityType, onImport]);

  // Early return moved to bottom to satisfy hooks rule
  if (!isOpen || !entityType) return null;

  return (
    <Modal
      isOpen={isOpen}
      onClose={onClose}
      title={`Nhập dữ liệu Excel/CSV - ${entityType.toUpperCase()}`}
      footer={
        <>
          <button type="button" className="btn btn-secondary" onClick={onClose} disabled={status === "uploading"}>
            Hủy
          </button>
          {status === "idle" && (
            <button type="button" className="btn btn-primary" onClick={handleStartImport} disabled={!file}>
              Bắt đầu nhập
            </button>
          )}
          {status === "success" && (
            <button type="button" className="btn btn-success" onClick={onClose}>
              Hoàn tất
            </button>
          )}
        </>
      }
    >
      <div style={{ display: "flex", flexDirection: "column", gap: "16px" }}>
        {status === "idle" && (
          <div 
            style={{ 
              border: "2px dashed var(--color-outline-variant)", 
              borderRadius: "var(--radius-lg)", 
              padding: "32px 16px", 
              textAlign: "center",
              cursor: "pointer",
              position: "relative",
              backgroundColor: "var(--color-surface-container-low)"
            }}
          >
            <input 
              type="file" 
              accept=".xlsx,.xls,.csv" 
              onChange={handleFileChange}
              aria-label="Tải lên tệp Excel hoặc CSV"
              style={{
                position: "absolute",
                top: 0,
                left: 0,
                width: "100%",
                height: "100%",
                opacity: 0,
                cursor: "pointer"
              }}
            />
            <span className="material-symbols-outlined" style={{ fontSize: "48px", color: "var(--color-primary)", marginBottom: "12px" }}>
              upload_file
            </span>
            <p className="text-body-md font-medium" style={{ color: "var(--color-on-surface)" }}>
              {file ? file.name : "Kéo thả tệp Excel/CSV vào đây hoặc nhấp để duyệt"}
            </p>
            <p className="text-xs text-outline" style={{ marginTop: "4px" }}>
              Hỗ trợ định dạng .xlsx, .xls, .csv (Tối đa 5MB)
            </p>
          </div>
        )}

        {status === "uploading" && (
          <div style={{ padding: "20px 0", textAlign: "center" }}>
            <span className="material-symbols-outlined" style={{ fontSize: "40px", color: "var(--color-primary)", animation: "spin 0.8s linear infinite", marginBottom: "16px" }}>
              autorenew
            </span>
            <p className="text-body-md font-medium" style={{ marginBottom: "8px" }}>Đang tải lên và phân tích dữ liệu...</p>
            <div style={{ height: "6px", backgroundColor: "var(--color-outline-variant)", borderRadius: "var(--radius-full)", overflow: "hidden", maxWidth: "300px", margin: "0 auto" }}>
              <div style={{ width: "100%", height: "100%", backgroundColor: "var(--color-primary)", borderRadius: "var(--radius-full)", transform: `scaleX(${progress / 100})`, transformOrigin: "left", transition: "transform 0.15s ease" }}></div>
            </div>
            <p className="text-xs text-outline" style={{ marginTop: "6px" }}>{progress}%</p>
          </div>
        )}

        {status === "success" && (
          <div style={{ textAlign: "center", padding: "16px 0" }}>
            <span className="material-symbols-outlined fill-icon" style={{ fontSize: "56px", color: "var(--color-success)", marginBottom: "12px" }}>
              check_circle
            </span>
            <h4 className="text-body-lg font-bold" style={{ color: "var(--color-on-surface)" }}>Nhập dữ liệu thành công!</h4>
            <p className="text-sm text-outline" style={{ marginTop: "6px" }}>
              Dữ liệu của tệp <strong>{file?.name}</strong> đã được đồng bộ vào danh sách {entityType}.
            </p>
          </div>
        )}
      </div>
    </Modal>
  );
}
