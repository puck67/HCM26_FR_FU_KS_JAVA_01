import { useState } from "react";
import { CrudTable, type ColumnConfig } from "./components/CrudTable";
import { CrudActionBar } from "./components/CrudActionBar";

// --- Interfaces ---
interface Student {
  id: string;
  name: string;
  email: string;
  meta: { enrollmentDate: string };
  status: "Active" | "Suspended";
}

interface Teacher {
  id: string;
  name: string;
  subject: string;
  department: string;
  experience: number;
}

interface Task {
  id: string;
  title: string;
  description: string;
  priority: "Low" | "Medium" | "High";
  completed: boolean;
}

// --- Initial Mock Data ---
const initialStudents: Student[] = [
  { id: "STU-001", name: "Alice Johnson", email: "alice.j@university.edu", meta: { enrollmentDate: "2024-09-01" }, status: "Active" },
  { id: "STU-002", name: "Bob Smith", email: "bob.smith@university.edu", meta: { enrollmentDate: "2025-01-15" }, status: "Active" },
  { id: "STU-003", name: "Charlie Brown", email: "charlie.b@university.edu", meta: { enrollmentDate: "2023-09-10" }, status: "Suspended" },
  { id: "STU-004", name: "Diana Prince", email: "diana.p@university.edu", meta: { enrollmentDate: "2024-02-28" }, status: "Active" }
];

const initialTeachers: Teacher[] = [
  { id: "TCH-001", name: "Dr. Robert Langdon", subject: "Symbology", department: "Arts & History", experience: 12 },
  { id: "TCH-002", name: "Prof. Minerva McGonagall", subject: "Transfiguration", department: "Magic & Sorcery", experience: 35 },
  { id: "TCH-003", name: "Dr. Walter White", subject: "Chemistry", department: "Physical Sciences", experience: 15 }
];

const initialTasks: Task[] = [
  { id: "TSK-001", title: "Grade Final Exams", description: "Review and grade all submissions for Semester 1", priority: "High", completed: false },
  { id: "TSK-002", title: "Update Syllabus", description: "Add new reading list to the course webpage", priority: "Medium", completed: true },
  { id: "TSK-003", title: "Order Lab Supplies", description: "Restock beakers, pipettes, and safety goggles", priority: "Low", completed: false }
];

// --- Toast notification helper interface ---
interface Toast {
  id: number;
  message: string;
  type: "success" | "danger" | "warning" | "info";
}

export default function App() {
  const [activeTab, setActiveTab] = useState<"students" | "teachers" | "tasks">("students");
  const [searchQuery, setSearchQuery] = useState("");
  
  // Data States
  const [students, setStudents] = useState<Student[]>(initialStudents);
  const [teachers, setTeachers] = useState<Teacher[]>(initialTeachers);
  const [tasks, setTasks] = useState<Task[]>(initialTasks);

  // Toast State
  const [toasts, setToasts] = useState<Toast[]>([]);
  const showToast = (message: string, type: Toast["type"] = "info") => {
    const id = Date.now();
    setToasts((prev) => [...prev, { id, message, type }]);
    setTimeout(() => {
      setToasts((prev) => prev.filter((t) => t.id !== id));
    }, 4000);
  };

  // Modal States
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [modalMode, setModalMode] = useState<"add" | "edit">("add");
  const [selectedItem, setSelectedItem] = useState<any>(null);

  // Form Field States
  // Student Form fields
  const [studentName, setStudentName] = useState("");
  const [studentEmail, setStudentEmail] = useState("");
  const [studentEnrollDate, setStudentEnrollDate] = useState("");
  const [studentStatus, setStudentStatus] = useState<"Active" | "Suspended">("Active");

  // Teacher Form fields
  const [teacherName, setTeacherName] = useState("");
  const [teacherSubject, setTeacherSubject] = useState("");
  const [teacherDept, setTeacherDept] = useState("");
  const [teacherExp, setTeacherExp] = useState(1);

  // Task Form fields
  const [taskTitle, setTaskTitle] = useState("");
  const [taskDesc, setTaskDesc] = useState("");
  const [taskPriority, setTaskPriority] = useState<"Low" | "Medium" | "High">("Medium");
  const [taskCompleted, setTaskCompleted] = useState(false);

  // Helper to open Modal for Add
  const handleOpenAdd = () => {
    setModalMode("add");
    setSelectedItem(null);
    
    // Reset all form inputs
    setStudentName("");
    setStudentEmail("");
    setStudentEnrollDate(new Date().toISOString().split("T")[0]);
    setStudentStatus("Active");

    setTeacherName("");
    setTeacherSubject("");
    setTeacherDept("");
    setTeacherExp(1);

    setTaskTitle("");
    setTaskDesc("");
    setTaskPriority("Medium");
    setTaskCompleted(false);

    setIsModalOpen(true);
  };

  // Helper to open Modal for Edit
  const handleOpenEdit = (item: any) => {
    setModalMode("edit");
    setSelectedItem(item);
    
    if (activeTab === "students") {
      const student = item as Student;
      setStudentName(student.name);
      setStudentEmail(student.email);
      setStudentEnrollDate(student.meta.enrollmentDate);
      setStudentStatus(student.status);
    } else if (activeTab === "teachers") {
      const teacher = item as Teacher;
      setTeacherName(teacher.name);
      setTeacherSubject(teacher.subject);
      setTeacherDept(teacher.department);
      setTeacherExp(teacher.experience);
    } else {
      const task = item as Task;
      setTaskTitle(task.title);
      setTaskDesc(task.description);
      setTaskPriority(task.priority);
      setTaskCompleted(task.completed);
    }

    setIsModalOpen(true);
  };

  // Delete Action handler
  const handleDelete = (item: any) => {
    if (activeTab === "students") {
      setStudents((prev) => prev.filter((s) => s.id !== item.id));
      showToast(`Student ${item.name} has been removed.`, "danger");
    } else if (activeTab === "teachers") {
      setTeachers((prev) => prev.filter((t) => t.id !== item.id));
      showToast(`Teacher ${item.name} has been removed.`, "danger");
    } else {
      setTasks((prev) => prev.filter((t) => t.id !== item.id));
      showToast(`Task "${item.title}" has been deleted.`, "danger");
    }
  };

  // Form Submission
  const handleFormSubmit = (e: React.FormEvent) => {
    e.preventDefault();

    if (activeTab === "students") {
      if (!studentName || !studentEmail) {
        showToast("Please fill in all required fields.", "warning");
        return;
      }
      if (modalMode === "add") {
        const newStudent: Student = {
          id: `STU-00${students.length + 1}`,
          name: studentName,
          email: studentEmail,
          meta: { enrollmentDate: studentEnrollDate },
          status: studentStatus,
        };
        setStudents((prev) => [newStudent, ...prev]);
        showToast(`Student ${studentName} added successfully!`, "success");
      } else {
        setStudents((prev) =>
          prev.map((s) =>
            s.id === selectedItem.id
              ? { ...s, name: studentName, email: studentEmail, meta: { enrollmentDate: studentEnrollDate }, status: studentStatus }
              : s
          )
        );
        showToast(`Student ${studentName} updated successfully!`, "success");
      }
    } else if (activeTab === "teachers") {
      if (!teacherName || !teacherSubject || !teacherDept) {
        showToast("Please fill in all required fields.", "warning");
        return;
      }
      if (modalMode === "add") {
        const newTeacher: Teacher = {
          id: `TCH-00${teachers.length + 1}`,
          name: teacherName,
          subject: teacherSubject,
          department: teacherDept,
          experience: Number(teacherExp),
        };
        setTeachers((prev) => [newTeacher, ...prev]);
        showToast(`Teacher ${teacherName} added successfully!`, "success");
      } else {
        setTeachers((prev) =>
          prev.map((t) =>
            t.id === selectedItem.id
              ? { ...t, name: teacherName, subject: teacherSubject, department: teacherDept, experience: Number(teacherExp) }
              : t
          )
        );
        showToast(`Teacher ${teacherName} updated successfully!`, "success");
      }
    } else {
      if (!taskTitle) {
        showToast("Please enter a task title.", "warning");
        return;
      }
      if (modalMode === "add") {
        const newTask: Task = {
          id: `TSK-00${tasks.length + 1}`,
          title: taskTitle,
          description: taskDesc,
          priority: taskPriority,
          completed: taskCompleted,
        };
        setTasks((prev) => [newTask, ...prev]);
        showToast(`Task "${taskTitle}" created!`, "success");
      } else {
        setTasks((prev) =>
          prev.map((t) =>
            t.id === selectedItem.id
              ? { ...t, title: taskTitle, description: taskDesc, priority: taskPriority, completed: taskCompleted }
              : t
          )
        );
        showToast(`Task "${taskTitle}" updated!`, "success");
      }
    }

    setIsModalOpen(false);
  };

  // Import Action mockup
  const handleImport = () => {
    if (activeTab === "students") {
      const extraStudents: Student[] = [
        { id: `STU-00${Date.now().toString().slice(-3)}`, name: "Emma Watson", email: "emma.w@university.edu", meta: { enrollmentDate: "2024-05-12" }, status: "Active" },
        { id: `STU-00${(Date.now() + 1).toString().slice(-3)}`, name: "Franklin Roosevelt", email: "franklin.r@university.edu", meta: { enrollmentDate: "2023-11-20" }, status: "Suspended" }
      ];
      setStudents((prev) => [...extraStudents, ...prev]);
      showToast("Imported 2 new students successfully.", "success");
    } else if (activeTab === "teachers") {
      const extraTeachers: Teacher[] = [
        { id: `TCH-00${Date.now().toString().slice(-3)}`, name: "Dr. Alan Turing", subject: "Computer Science", department: "Mathematics", experience: 20 }
      ];
      setTeachers((prev) => [...extraTeachers, ...prev]);
      showToast("Imported 1 new teacher successfully.", "success");
    } else {
      const extraTasks: Task[] = [
        { id: `TSK-00${Date.now().toString().slice(-3)}`, title: "Setup Dev Environment", description: "Configure React & TS build configurations", priority: "High", completed: false }
      ];
      setTasks((prev) => [...extraTasks, ...prev]);
      showToast("Imported 1 new task successfully.", "success");
    }
  };

  // Export Action (Real browser CSV generator & download!)
  const handleExport = () => {
    let csvContent = "data:text/csv;charset=utf-8,";
    let filename = "";

    if (activeTab === "students") {
      csvContent += "ID,Name,Email,Enrollment Date,Status\n";
      students.forEach((s) => {
        csvContent += `"${s.id}","${s.name}","${s.email}","${s.meta.enrollmentDate}","${s.status}"\n`;
      });
      filename = "students_export.csv";
    } else if (activeTab === "teachers") {
      csvContent += "ID,Name,Subject,Department,Experience (Years)\n";
      teachers.forEach((t) => {
        csvContent += `"${t.id}","${t.name}","${t.subject}","${t.department}",${t.experience}\n`;
      });
      filename = "teachers_export.csv";
    } else {
      csvContent += "ID,Title,Description,Priority,Completed\n";
      tasks.forEach((t) => {
        csvContent += `"${t.id}","${t.title}","${t.description}","${t.priority}",${t.completed}\n`;
      });
      filename = "tasks_export.csv";
    }

    const encodedUri = encodeURI(csvContent);
    const link = document.createElement("a");
    link.setAttribute("href", encodedUri);
    link.setAttribute("download", filename);
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);

    showToast(`Exported current dataset to ${filename}`, "success");
  };

  // --- Column Configurations ---
  const studentColumns: ColumnConfig<Student>[] = [
    { key: "id", header: "ID" },
    { key: "name", header: "Student Name" },
    { key: "email", header: "Email Address" },
    { 
      key: "meta.enrollmentDate", 
      header: "Enrolled On",
      render: (s) => {
        const date = new Date(s.meta.enrollmentDate);
        return date.toLocaleDateString("en-US", { year: "numeric", month: "short", day: "numeric" });
      }
    },
    { 
      key: "status", 
      header: "Status",
      render: (s) => (
        <span className={`badge ${s.status === "Active" ? "badge-success" : "badge-danger"}`}>
          {s.status}
        </span>
      )
    },
  ];

  const teacherColumns: ColumnConfig<Teacher>[] = [
    { key: "id", header: "ID" },
    { key: "name", header: "Teacher Name" },
    { key: "subject", header: "Subject Specialist" },
    { key: "department", header: "Department" },
    { 
      key: "experience", 
      header: "Tenure / Experience",
      render: (t) => (
        <span className="badge badge-purple">
          {t.experience} {t.experience === 1 ? "Year" : "Years"}
        </span>
      )
    },
  ];

  const taskColumns: ColumnConfig<Task>[] = [
    { key: "id", header: "ID" },
    { key: "title", header: "Task Title" },
    { key: "description", header: "Description" },
    { 
      key: "priority", 
      header: "Priority",
      render: (t) => {
        const badgeClass = t.priority === "High" ? "badge-danger" : t.priority === "Medium" ? "badge-warning" : "badge-info";
        return <span className={`badge ${badgeClass}`}>{t.priority}</span>;
      }
    },
    { 
      key: "completed", 
      header: "Status",
      render: (t) => (
        <span className={`badge ${t.completed ? "badge-success" : "badge-warning"}`}>
          {t.completed ? "✓ Completed" : "⚡ Pending"}
        </span>
      )
    },
  ];

  // --- Extra Actions ---
  const extraStudentActions = (student: Student) => [
    {
      title: student.status === "Active" ? "Suspend" : "Activate",
      action: () => {
        setStudents((prev) =>
          prev.map((s) =>
            s.id === student.id ? { ...s, status: s.status === "Active" ? "Suspended" : "Active" } : s
          )
        );
        showToast(`Student status toggled to ${student.status === "Active" ? "Suspended" : "Active"}.`, "warning");
      },
      variant: (student.status === "Active" ? "warning" : "success") as any
    }
  ];

  const extraTeacherActions = (teacher: Teacher) => [
    {
      title: "Promote",
      action: () => {
        setTeachers((prev) =>
          prev.map((t) =>
            t.id === teacher.id ? { ...t, experience: t.experience + 1 } : t
          )
        );
        showToast(`Dr./Prof. ${teacher.name} promoted (+1 Year Tenure)!`, "success");
      },
      variant: "success" as any
    }
  ];

  const extraTaskActions = (task: Task) => [
    {
      title: task.completed ? "Reopen" : "Complete",
      action: () => {
        setTasks((prev) =>
          prev.map((t) =>
            t.id === task.id ? { ...t, completed: !t.completed } : t
          )
        );
        showToast(`Task status toggled to ${task.completed ? "Pending" : "Completed"}.`, "info");
      },
      variant: (task.completed ? "warning" : "success") as any
    }
  ];

  // --- Filter and Search Datasets ---
  const getFilteredStudents = () => {
    return students.filter(
      (s) =>
        s.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
        s.email.toLowerCase().includes(searchQuery.toLowerCase()) ||
        s.id.toLowerCase().includes(searchQuery.toLowerCase())
    );
  };

  const getFilteredTeachers = () => {
    return teachers.filter(
      (t) =>
        t.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
        t.subject.toLowerCase().includes(searchQuery.toLowerCase()) ||
        t.department.toLowerCase().includes(searchQuery.toLowerCase()) ||
        t.id.toLowerCase().includes(searchQuery.toLowerCase())
    );
  };

  const getFilteredTasks = () => {
    return tasks.filter(
      (t) =>
        t.title.toLowerCase().includes(searchQuery.toLowerCase()) ||
        t.description.toLowerCase().includes(searchQuery.toLowerCase()) ||
        t.priority.toLowerCase().includes(searchQuery.toLowerCase()) ||
        t.id.toLowerCase().includes(searchQuery.toLowerCase())
    );
  };

  return (
    <>
      {/* Toast Notification HUD */}
      <div className="toast-container">
        {toasts.map((toast) => (
          <div key={toast.id} className={`toast toast-${toast.type}`}>
            {toast.message}
          </div>
        ))}
      </div>

      {/* Main Dashboard Header */}
      <header className="header">
        <h1>OmniCRUD Control Center</h1>
        <p>A unified, type-safe generic system for managing students, teachers, and workflow tasks.</p>
      </header>

      {/* Navigation tabs */}
      <div className="navigation-tabs">
        <button
          className={`tab-btn ${activeTab === "students" ? "active" : ""}`}
          onClick={() => { setActiveTab("students"); setSearchQuery(""); }}
        >
          🎓 Students ({students.length})
        </button>
        <button
          className={`tab-btn ${activeTab === "teachers" ? "active" : ""}`}
          onClick={() => { setActiveTab("teachers"); setSearchQuery(""); }}
        >
          🏫 Teachers ({teachers.length})
        </button>
        <button
          className={`tab-btn ${activeTab === "tasks" ? "active" : ""}`}
          onClick={() => { setActiveTab("tasks"); setSearchQuery(""); }}
        >
          📋 Tasks ({tasks.length})
        </button>
      </div>

      {/* Search and CRUD Action Bar controls */}
      <div className="controls-row">
        <div className="search-wrapper">
          <svg
            className="search-icon"
            width="18"
            height="18"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            strokeWidth="2.5"
            strokeLinecap="round"
            strokeLinejoin="round"
          >
            <circle cx="11" cy="11" r="8"></circle>
            <line x1="21" y1="21" x2="16.65" y2="16.65"></line>
          </svg>
          <input
            type="text"
            className="search-input"
            placeholder={`Search ${activeTab}...`}
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
          />
        </div>

        <CrudActionBar
          onAdd={handleOpenAdd}
          onImport={handleImport}
          onExport={handleExport}
        />
      </div>

      {/* Dynamic Generic CRUD Table presentation */}
      {activeTab === "students" && (
        <CrudTable
          data={getFilteredStudents()}
          columns={studentColumns}
          onEdit={handleOpenEdit}
          onDelete={handleDelete}
          extraActions={extraStudentActions}
        />
      )}

      {activeTab === "teachers" && (
        <CrudTable
          data={getFilteredTeachers()}
          columns={teacherColumns}
          onEdit={handleOpenEdit}
          onDelete={handleDelete}
          extraActions={extraTeacherActions}
        />
      )}

      {activeTab === "tasks" && (
        <CrudTable
          data={getFilteredTasks()}
          columns={taskColumns}
          onEdit={handleOpenEdit}
          onDelete={handleDelete}
          extraActions={extraTaskActions}
        />
      )}

      {/* Unified overlay Dialog Form Modal */}
      {isModalOpen && (
        <div className="modal-overlay" onClick={() => setIsModalOpen(false)}>
          <div className="modal-content" onClick={(e) => e.stopPropagation()}>
            <div className="modal-header">
              <h3>
                {modalMode === "add" ? "+ New" : "✎ Edit"}{" "}
                {activeTab === "students" ? "Student Record" : activeTab === "teachers" ? "Teacher Profile" : "Workflow Task"}
              </h3>
              <button className="modal-close-btn" onClick={() => setIsModalOpen(false)}>
                &times;
              </button>
            </div>

            <form onSubmit={handleFormSubmit}>
              {/* Dynamic inputs based on active tab */}
              {activeTab === "students" && (
                <>
                  <div className="form-group">
                    <label>Full Name</label>
                    <input
                      type="text"
                      className="form-control"
                      value={studentName}
                      onChange={(e) => setStudentName(e.target.value)}
                      placeholder="e.g. Alice Johnson"
                      required
                    />
                  </div>
                  <div className="form-group">
                    <label>Email Address</label>
                    <input
                      type="email"
                      className="form-control"
                      value={studentEmail}
                      onChange={(e) => setStudentEmail(e.target.value)}
                      placeholder="e.g. alice@university.edu"
                      required
                    />
                  </div>
                  <div className="form-group">
                    <label>Enrollment Date</label>
                    <input
                      type="date"
                      className="form-control"
                      value={studentEnrollDate}
                      onChange={(e) => setStudentEnrollDate(e.target.value)}
                      required
                    />
                  </div>
                  <div className="form-group">
                    <label>Status</label>
                    <select
                      className="form-control"
                      value={studentStatus}
                      onChange={(e) => setStudentStatus(e.target.value as any)}
                    >
                      <option value="Active">Active</option>
                      <option value="Suspended">Suspended</option>
                    </select>
                  </div>
                </>
              )}

              {activeTab === "teachers" && (
                <>
                  <div className="form-group">
                    <label>Full Name</label>
                    <input
                      type="text"
                      className="form-control"
                      value={teacherName}
                      onChange={(e) => setTeacherName(e.target.value)}
                      placeholder="e.g. Dr. Robert Langdon"
                      required
                    />
                  </div>
                  <div className="form-group">
                    <label>Subject Specialty</label>
                    <input
                      type="text"
                      className="form-control"
                      value={teacherSubject}
                      onChange={(e) => setTeacherSubject(e.target.value)}
                      placeholder="e.g. Symbology"
                      required
                    />
                  </div>
                  <div className="form-group">
                    <label>Department</label>
                    <input
                      type="text"
                      className="form-control"
                      value={teacherDept}
                      onChange={(e) => setTeacherDept(e.target.value)}
                      placeholder="e.g. Arts & History"
                      required
                    />
                  </div>
                  <div className="form-group">
                    <label>Years of Experience</label>
                    <input
                      type="number"
                      className="form-control"
                      value={teacherExp}
                      onChange={(e) => setTeacherExp(Number(e.target.value))}
                      min="0"
                      required
                    />
                  </div>
                </>
              )}

              {activeTab === "tasks" && (
                <>
                  <div className="form-group">
                    <label>Task Title</label>
                    <input
                      type="text"
                      className="form-control"
                      value={taskTitle}
                      onChange={(e) => setTaskTitle(e.target.value)}
                      placeholder="e.g. Grade Final Exams"
                      required
                    />
                  </div>
                  <div className="form-group">
                    <label>Description</label>
                    <textarea
                      className="form-control"
                      rows={3}
                      value={taskDesc}
                      onChange={(e) => setTaskDesc(e.target.value)}
                      placeholder="e.g. Review and grade all submissions for Semester 1"
                    />
                  </div>
                  <div className="form-group">
                    <label>Priority</label>
                    <select
                      className="form-control"
                      value={taskPriority}
                      onChange={(e) => setTaskPriority(e.target.value as any)}
                    >
                      <option value="Low">Low</option>
                      <option value="Medium">Medium</option>
                      <option value="High">High</option>
                    </select>
                  </div>
                  <div className="form-group" style={{ display: "flex", gap: "10px", alignItems: "center" }}>
                    <input
                      type="checkbox"
                      id="completed"
                      checked={taskCompleted}
                      onChange={(e) => setTaskCompleted(e.target.checked)}
                      style={{ width: "18px", height: "18px", accentColor: "var(--color-primary)" }}
                    />
                    <label htmlFor="completed" style={{ margin: 0, cursor: "pointer" }}>Mark as Completed</label>
                  </div>
                </>
              )}

              <div className="form-actions">
                <button type="button" className="btn btn-secondary" onClick={() => setIsModalOpen(false)}>
                  Cancel
                </button>
                <button type="submit" className="btn btn-primary">
                  {modalMode === "add" ? "Save Record" : "Apply Changes"}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </>
  );
}
