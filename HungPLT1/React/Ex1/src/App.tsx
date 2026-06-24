import { useState } from "react";
import { CrudTable } from "./components/CrudTable";
import { CrudActionBar } from "./components/CrudActionBar";
import { CrudModal } from "./components/CrudModal";
import type { FieldConfig } from "./components/CrudModal";

// Define Interfaces for our 3 models
interface Student {
  "Mã SV": string;
  "Họ tên": string;
  "Email": string;
  "Ngành học": string;
  "Điểm GPA": number;
}

interface Teacher {
  "Mã GV": string;
  "Họ tên": string;
  "Môn dạy": string;
  "Khoa": string;
  "Lương (VND)": number;
}

interface Task {
  "Mã CV": string;
  "Tiêu đề": string;
  "Hạn chót": string;
  "Độ ưu tiên": string;
  "Trạng thái": boolean;
}

// Initial Mock Data
const initialStudents: Student[] = [
  { "Mã SV": "SV001", "Họ tên": "Nguyễn Văn Anh", "Email": "anhnv@student.edu.vn", "Ngành học": "Khoa học Máy tính", "Điểm GPA": 3.82 },
  { "Mã SV": "SV002", "Họ tên": "Trần Thị Bình", "Email": "binhtt@student.edu.vn", "Ngành học": "Kỹ thuật Phần mềm", "Điểm GPA": 3.45 },
  { "Mã SV": "SV003", "Họ tên": "Phạm Hồng Chương", "Email": "chuongph@student.edu.vn", "Ngành học": "An toàn Thông tin", "Điểm GPA": 3.12 }
];

const initialTeachers: Teacher[] = [
  { "Mã GV": "GV001", "Họ tên": "Lê Hoàng Nam", "Môn dạy": "Lập trình Web", "Khoa": "Công nghệ Thông tin", "Lương (VND)": 18500000 },
  { "Mã GV": "GV002", "Họ tên": "Vũ Mai Phương", "Môn dạy": "Cơ sở Dữ liệu", "Khoa": "Hệ thống Thông tin", "Lương (VND)": 16000000 }
];

const initialTasks: Task[] = [
  { "Mã CV": "CV001", "Tiêu đề": "Hoàn thành bài tập lớn React", "Hạn chót": "2026-06-25", "Độ ưu tiên": "Cao", "Trạng thái": false },
  { "Mã CV": "CV002", "Tiêu đề": "Chuẩn bị tài liệu giảng dạy tuần 5", "Hạn chót": "2026-06-28", "Độ ưu tiên": "Trung bình", "Trạng thái": true }
];

// Notification Interface
interface AppNotification {
  id: number;
  message: string;
  type: "success" | "warning" | "info";
}

export default function App() {
  // State for active tab: 'students' | 'teachers' | 'tasks'
  const [activeTab, setActiveTab] = useState<"students" | "teachers" | "tasks">("students");

  // State for entities
  const [students, setStudents] = useState<Student[]>(initialStudents);
  const [teachers, setTeachers] = useState<Teacher[]>(initialTeachers);
  const [tasks, setTasks] = useState<Task[]>(initialTasks);

  // Modal State
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [modalTitle, setModalTitle] = useState("");
  const [modalFields, setModalFields] = useState<FieldConfig[]>([]);
  const [editingItem, setEditingItem] = useState<any>(null);

  // Notification State
  const [notifications, setNotifications] = useState<AppNotification[]>([]);

  const addNotification = (message: string, type: "success" | "warning" | "info" = "success") => {
    const id = Date.now();
    setNotifications((prev) => [...prev, { id, message, type }]);
    setTimeout(() => {
      setNotifications((prev) => prev.filter((n) => n.id !== id));
    }, 4000);
  };

  // Field configurations for Modals
  const studentFieldsConfig: FieldConfig[] = [
    { name: "Mã SV", label: "Mã Sinh viên", type: "text" },
    { name: "Họ tên", label: "Họ và Tên", type: "text" },
    { name: "Email", label: "Địa chỉ Email", type: "email" },
    {
      name: "Ngành học",
      label: "Ngành học",
      type: "select",
      options: ["Khoa học Máy tính", "Kỹ thuật Phần mềm", "An toàn Thông tin", "Hệ thống Thông tin", "Thiết kế Đồ họa"]
    },
    { name: "Điểm GPA", label: "Điểm trung bình (GPA)", type: "number" }
  ];

  const teacherFieldsConfig: FieldConfig[] = [
    { name: "Mã GV", label: "Mã Giảng viên", type: "text" },
    { name: "Họ tên", label: "Họ và Tên", type: "text" },
    { name: "Môn dạy", label: "Môn học giảng dạy", type: "text" },
    {
      name: "Khoa",
      label: "Khoa khoa học",
      type: "select",
      options: ["Công nghệ Thông tin", "Hệ thống Thông tin", "Điện tử Viễn thông", "Kinh tế & Quản lý"]
    },
    { name: "Lương (VND)", label: "Lương hàng tháng (VND)", type: "number" }
  ];

  const taskFieldsConfig: FieldConfig[] = [
    { name: "Mã CV", label: "Mã Công việc", type: "text" },
    { name: "Tiêu đề", label: "Tiêu đề công việc", type: "text" },
    { name: "Hạn chót", label: "Hạn chót thực hiện", type: "date" },
    {
      name: "Độ ưu tiên",
      label: "Mức độ ưu tiên",
      type: "select",
      options: ["Thấp", "Trung bình", "Cao", "Khẩn cấp"]
    },
    { name: "Trạng thái", label: "Đã hoàn thành", type: "checkbox" }
  ];

  // Handler for adding items
  const handleOpenAddModal = () => {
    setEditingItem(null);
    if (activeTab === "students") {
      setModalTitle("Thêm Sinh Viên Mới");
      setModalFields(studentFieldsConfig);
    } else if (activeTab === "teachers") {
      setModalTitle("Thêm Giảng Viên Mới");
      setModalFields(teacherFieldsConfig);
    } else {
      setModalTitle("Thêm Công Việc Mới");
      setModalFields(taskFieldsConfig);
    }
    setIsModalOpen(true);
  };

  // Handler for editing items
  const handleOpenEditModal = (item: any) => {
    setEditingItem(item);
    if (activeTab === "students") {
      setModalTitle("Cập nhật Thông tin Sinh viên");
      setModalFields(studentFieldsConfig);
    } else if (activeTab === "teachers") {
      setModalTitle("Cập nhật Thông tin Giảng viên");
      setModalFields(teacherFieldsConfig);
    } else {
      setModalTitle("Cập nhật Thông tin Công việc");
      setModalFields(taskFieldsConfig);
    }
    setIsModalOpen(true);
  };

  // Delete Handlers
  const handleDelete = (item: any) => {
    if (activeTab === "students") {
      const code = item["Mã SV"];
      if (confirm(`Bạn có chắc chắn muốn xóa sinh viên ${item["Họ tên"]} (${code})?`)) {
        setStudents((prev) => prev.filter((s) => s["Mã SV"] !== code));
        addNotification(`Đã xóa sinh viên ${item["Họ tên"]} thành công!`, "warning");
      }
    } else if (activeTab === "teachers") {
      const code = item["Mã GV"];
      if (confirm(`Bạn có chắc chắn muốn xóa giảng viên ${item["Họ tên"]} (${code})?`)) {
        setTeachers((prev) => prev.filter((t) => t["Mã GV"] !== code));
        addNotification(`Đã xóa giảng viên ${item["Họ tên"]} thành công!`, "warning");
      }
    } else {
      const code = item["Mã CV"];
      if (confirm(`Bạn có chắc chắn muốn xóa công việc: "${item["Tiêu đề"]}"?`)) {
        setTasks((prev) => prev.filter((t) => t["Mã CV"] !== code));
        addNotification(`Đã xóa công việc thành công!`, "warning");
      }
    }
  };

  // Save changes (both Add and Edit)
  const handleSave = (data: any) => {
    if (activeTab === "students") {
      const studentData = data as Student;
      const key = studentData["Mã SV"];
      if (editingItem) {
        setStudents((prev) => prev.map((s) => (s["Mã SV"] === editingItem["Mã SV"] ? studentData : s)));
        addNotification(`Cập nhật sinh viên ${studentData["Họ tên"]} thành công!`, "success");
      } else {
        if (students.some((s) => s["Mã SV"] === key)) {
          alert(`Mã SV ${key} đã tồn tại! Vui lòng chọn mã khác.`);
          return;
        }
        setStudents((prev) => [...prev, studentData]);
        addNotification(`Thêm mới sinh viên ${studentData["Họ tên"]} thành công!`, "success");
      }
    } else if (activeTab === "teachers") {
      const teacherData = data as Teacher;
      const key = teacherData["Mã GV"];
      if (editingItem) {
        setTeachers((prev) => prev.map((t) => (t["Mã GV"] === editingItem["Mã GV"] ? teacherData : t)));
        addNotification(`Cập nhật giảng viên ${teacherData["Họ tên"]} thành công!`, "success");
      } else {
        if (teachers.some((t) => t["Mã GV"] === key)) {
          alert(`Mã GV ${key} đã tồn tại! Vui lòng chọn mã khác.`);
          return;
        }
        setTeachers((prev) => [...prev, teacherData]);
        addNotification(`Thêm mới giảng viên ${teacherData["Họ tên"]} thành công!`, "success");
      }
    } else {
      const taskData = data as Task;
      const key = taskData["Mã CV"];
      if (editingItem) {
        setTasks((prev) => prev.map((t) => (t["Mã CV"] === editingItem["Mã CV"] ? taskData : t)));
        addNotification(`Cập nhật công việc "${taskData["Tiêu đề"]}" thành công!`, "success");
      } else {
        if (tasks.some((t) => t["Mã CV"] === key)) {
          alert(`Mã CV ${key} đã tồn tại! Vui lòng chọn mã khác.`);
          return;
        }
        setTasks((prev) => [...prev, taskData]);
        addNotification(`Thêm mới công việc thành công!`, "success");
      }
    }
  };

  // Mock Import simulation
  const handleImport = () => {
    if (activeTab === "students") {
      const newImport: Student = {
        "Mã SV": `SV00${students.length + 1}`,
        "Họ tên": "Trương Minh Đăng (Imported)",
        "Email": "dangtm@student.edu.vn",
        "Ngành học": "Thiết kế Đồ họa",
        "Điểm GPA": 3.65
      };
      setStudents((prev) => [...prev, newImport]);
      addNotification("Đã import 1 bản ghi Sinh viên mẫu!", "success");
    } else if (activeTab === "teachers") {
      const newImport: Teacher = {
        "Mã GV": `GV00${teachers.length + 1}`,
        "Họ tên": "Phạm Thị Quỳnh (Imported)",
        "Môn dạy": "Cấu trúc dữ liệu và giải thuật",
        "Khoa": "Công nghệ Thông tin",
        "Lương (VND)": 17800000
      };
      setTeachers((prev) => [...prev, newImport]);
      addNotification("Đã import 1 bản ghi Giảng viên mẫu!", "success");
    } else {
      const newImport: Task = {
        "Mã CV": `CV00${tasks.length + 1}`,
        "Tiêu đề": "Chuẩn bị bài thuyết trình đề tài khoa học",
        "Hạn chót": "2026-07-02",
        "Độ ưu tiên": "Khẩn cấp",
        "Trạng thái": false
      };
      setTasks((prev) => [...prev, newImport]);
      addNotification("Đã import 1 bản ghi Công việc mẫu!", "success");
    }
  };

  // Professional CSV Export Trigger
  const handleExport = () => {
    let csvContent = "data:text/csv;charset=utf-8,\uFEFF";
    let filename = "";

    if (activeTab === "students") {
      filename = "danh-sach-sinh-vien.csv";
      csvContent += "Mã SV,Họ tên,Email,Ngành học,Điểm GPA\n";
      students.forEach((s) => {
        csvContent += `"${s["Mã SV"]}","${s["Họ tên"]}","${s["Email"]}","${s["Ngành học"]}",${s["Điểm GPA"]}\n`;
      });
    } else if (activeTab === "teachers") {
      filename = "danh-sach-giang-vien.csv";
      csvContent += "Mã GV,Họ tên,Môn dạy,Khoa,Lương (VND)\n";
      teachers.forEach((t) => {
        csvContent += `"${t["Mã GV"]}","${t["Họ tên"]}","${t["Môn dạy"]}","${t["Khoa"]}",${t["Lương (VND)"]}\n`;
      });
    } else {
      filename = "danh-sach-cong-viec.csv";
      csvContent += "Mã CV,Tiêu đề,Hạn chót,Độ ưu tiên,Trạng thái\n";
      tasks.forEach((t) => {
        csvContent += `"${t["Mã CV"]}","${t["Tiêu đề"]}","${t["Hạn chót"]}","${t["Độ ưu tiên"]}",${t["Trạng thái"] ? "Đã hoàn thành" : "Chờ xử lý"}\n`;
      });
    }

    const encodedUri = encodeURI(csvContent);
    const link = document.createElement("a");
    link.setAttribute("href", encodedUri);
    link.setAttribute("download", filename);
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);

    addNotification(`Đã xuất file CSV thành công!`, "success");
  };

  // Define Extra Row Actions to match screenshot exactly
  const getExtraActions = (item: any) => {
    if (activeTab === "students") {
      return [
        {
          title: "Học bạ",
          action: () => {
            alert(`--- Học bạ Sinh viên ---\nTên: ${item["Họ tên"]}\nNgành: ${item["Ngành học"]}\nGPA tích lũy: ${item["Điểm GPA"]}\nĐánh giá xếp loại: ${item["Điểm GPA"] >= 3.6 ? "Xuất sắc 🏆" : item["Điểm GPA"] >= 3.2 ? "Giỏi 🥇" : "Khá 🥈"}`);
          },
          style: "btn-custom"
        }
      ];
    } else if (activeTab === "teachers") {
      return [
        {
          title: "Phân lớp",
          action: () => {
            const className = prompt(`Nhập mã lớp phân giảng cho GV ${item["Họ tên"]}:`, "WD-18301");
            if (className) {
              addNotification(`Đã phân công GV ${item["Họ tên"]} phụ trách lớp ${className}!`, "info");
            }
          },
          style: "btn-custom"
        }
      ];
    } else {
      const task = item as Task;
      return [
        {
          title: task["Trạng thái"] ? "Khôi phục" : "Hoàn thành",
          action: () => {
            setTasks((prev) =>
              prev.map((t) =>
                t["Mã CV"] === task["Mã CV"] ? { ...t, "Trạng thái": !t["Trạng thái"] } : t
              )
            );
            addNotification(
              task["Trạng thái"]
                ? `Đã khôi phục công việc về trạng thái chờ xử lý.`
                : `Đã đánh dấu hoàn thành công việc!`,
              "info"
            );
          },
          style: task["Trạng thái"] ? "btn-custom" : "btn-add"
        }
      ];
    }
  };

  return (
    <div className="app-container">
      {/* Notifications overlay */}
      <div className="notification-container">
        {notifications.map((n) => (
          <div key={n.id} className={`notification ${n.type}`}>
            <span>{n.message}</span>
          </div>
        ))}
      </div>

      <header className="app-header">
        <h1 className="app-title">Hệ Thống Quản Lý CRUD</h1>
        <p className="app-subtitle">Nền tảng quản trị thông minh với React Generic Components</p>
      </header>

      {/* Modern Tabs Navigation with Icons matching screenshot */}
      <div className="tabs-container">
        <button
          className={`tab-btn ${activeTab === "students" ? "active" : ""}`}
          onClick={() => setActiveTab("students")}
        >
          🎓 Sinh viên
        </button>
        <button
          className={`tab-btn ${activeTab === "teachers" ? "active" : ""}`}
          onClick={() => setActiveTab("teachers")}
        >
          👩‍🏫 Giảng viên
        </button>
        <button
          className={`tab-btn ${activeTab === "tasks" ? "active" : ""}`}
          onClick={() => setActiveTab("tasks")}
        >
          📋 Công việc
        </button>
      </div>

      {/* CRUD Action Bar */}
      <CrudActionBar
        onAdd={handleOpenAddModal}
        onImport={handleImport}
        onExport={handleExport}
      />

      {/* CRUD Table Rendered Dynamically based on current Tab State */}
      {activeTab === "students" && (
        <CrudTable<Student>
          data={students}
          onEdit={handleOpenEditModal}
          onDelete={handleDelete}
          extraActions={getExtraActions}
        />
      )}

      {activeTab === "teachers" && (
        <CrudTable<Teacher>
          data={teachers}
          onEdit={handleOpenEditModal}
          onDelete={handleDelete}
          extraActions={getExtraActions}
        />
      )}

      {activeTab === "tasks" && (
        <CrudTable<Task>
          data={tasks}
          onEdit={handleOpenEditModal}
          onDelete={handleDelete}
          extraActions={getExtraActions}
        />
      )}

      {/* Generic Modal Form used for adding & editing entries */}
      <CrudModal<any>
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        onSave={handleSave}
        title={modalTitle}
        fields={modalFields}
        initialData={editingItem}
      />

      {/* Professional Footer matching screenshot */}
      <footer className="app-footer">
        <div>Copyright © Hệ Thống Quản Lý CRUD</div>
        <div className="footer-links">
          <a href="#contact">Contact</a>
          <a href="#help">Help</a>
        </div>
      </footer>
    </div>
  );
}
