import { useState, useEffect } from "react";
import { CrudTable } from "./components/CrudTable";
import { CrudActionBar } from "./components/CrudActionBar";
import "./App.css";

// --- Interfaces ---
interface Task {
  id: string;
  title: string;
  category: string;
  priority: "High" | "Medium" | "Low";
  completed: boolean;
}

interface Student {
  id: string;
  name: string;
  email: string;
  major: string;
  gpa: number;
}

interface Teacher {
  id: string;
  name: string;
  email: string;
  department: string;
  salary: number;
}

type TabType = "tasks" | "students" | "teachers";

// --- Sample Initial Data ---
const initialTasks: Task[] = [
  { id: "task-1", title: "Design Core Database Schema", category: "Database", priority: "High", completed: false },
  { id: "task-2", title: "Implement OAuth2 Authentication", category: "Security", priority: "High", completed: true },
  { id: "task-3", title: "Develop Generic CRUD Components", category: "Frontend", priority: "Medium", completed: false },
  { id: "task-4", title: "Optimize Database Queries", category: "Performance", priority: "Low", completed: false },
];

const initialStudents: Student[] = [
  { id: "stud-1", name: "Nguyen Van A", email: "van.a@university.edu", major: "Computer Science", gpa: 3.6 },
  { id: "stud-2", name: "Tran Thi B", email: "thi.b@university.edu", major: "Data Science", gpa: 3.9 },
  { id: "stud-3", name: "Le Van C", email: "van.c@university.edu", major: "Cyber Security", gpa: 3.1 },
];

const initialTeachers: Teacher[] = [
  { id: "teach-1", name: "Dr. Alexander Fleming", email: "a.fleming@faculty.edu", department: "Bioinformatics", salary: 5200 },
  { id: "teach-2", name: "Prof. Grace Hopper", email: "g.hopper@faculty.edu", department: "Computer Science", salary: 6000 },
  { id: "teach-3", name: "Dr. Richard Feynman", email: "r.feynman@faculty.edu", department: "Quantum Physics", salary: 5800 },
];

export default function App() {
  // --- State ---
  const [activeTab, setActiveTab] = useState<TabType>("tasks");
  
  const [tasks, setTasks] = useState<Task[]>(() => {
    const saved = localStorage.getItem("crud_tasks");
    return saved ? JSON.parse(saved) : initialTasks;
  });
  
  const [students, setStudents] = useState<Student[]>(() => {
    const saved = localStorage.getItem("crud_students");
    return saved ? JSON.parse(saved) : initialStudents;
  });
  
  const [teachers, setTeachers] = useState<Teacher[]>(() => {
    const saved = localStorage.getItem("crud_teachers");
    return saved ? JSON.parse(saved) : initialTeachers;
  });

  // Modal State
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingItem, setEditingItem] = useState<any | null>(null);

  // Form Fields State
  const [formData, setFormData] = useState<Record<string, any>>({});

  // Toast state
  const [toast, setToast] = useState<{ message: string; type: "success" | "info" | "danger" } | null>(null);

  // --- LocalStorage Sync ---
  useEffect(() => {
    localStorage.setItem("crud_tasks", JSON.stringify(tasks));
  }, [tasks]);

  useEffect(() => {
    localStorage.setItem("crud_students", JSON.stringify(students));
  }, [students]);

  useEffect(() => {
    localStorage.setItem("crud_teachers", JSON.stringify(teachers));
  }, [teachers]);

  // --- Toast Trigger helper ---
  const showToast = (message: string, type: "success" | "info" | "danger" = "success") => {
    setToast({ message, type });
  };

  useEffect(() => {
    if (toast) {
      const timer = setTimeout(() => setToast(null), 3000);
      return () => clearTimeout(timer);
    }
  }, [toast]);

  // --- CRUD Operation Handlers ---

  // 1. ADD / OPEN FORM
  const handleOpenAddForm = () => {
    setEditingItem(null);
    if (activeTab === "tasks") {
      setFormData({ title: "", category: "", priority: "Medium", completed: false });
    } else if (activeTab === "students") {
      setFormData({ name: "", email: "", major: "", gpa: 3.0 });
    } else if (activeTab === "teachers") {
      setFormData({ name: "", email: "", department: "", salary: 4000 });
    }
    setIsModalOpen(true);
  };

  // 2. EDIT / OPEN FORM WITH DATA
  const handleOpenEditForm = (item: any) => {
    setEditingItem(item);
    setFormData({ ...item });
    setIsModalOpen(true);
  };

  // 3. DELETE
  const handleDeleteItem = (item: any) => {
    if (confirm(`Are you sure you want to delete "${item.title || item.name}"?`)) {
      if (activeTab === "tasks") {
        setTasks(tasks.filter((t) => t.id !== item.id));
        showToast("Task deleted successfully", "danger");
      } else if (activeTab === "students") {
        setStudents(students.filter((s) => s.id !== item.id));
        showToast("Student profile deleted", "danger");
      } else if (activeTab === "teachers") {
        setTeachers(teachers.filter((t) => t.id !== item.id));
        showToast("Teacher record deleted", "danger");
      }
    }
  };

  // 4. SUBMIT FORM (Save Add / Edit)
  const handleSubmitForm = (e: React.FormEvent) => {
    e.preventDefault();

    if (activeTab === "tasks") {
      if (editingItem) {
        // Edit Mode
        setTasks(tasks.map((t) => (t.id === editingItem.id ? { ...(formData as Task) } : t)));
        showToast("Task updated successfully");
      } else {
        // Add Mode
        const newTask: Task = {
          ...(formData as Task),
          id: `task-${Date.now()}`,
        };
        setTasks([newTask, ...tasks]);
        showToast("New task created");
      }
    } else if (activeTab === "students") {
      if (editingItem) {
        setStudents(students.map((s) => (s.id === editingItem.id ? { ...(formData as Student) } : s)));
        showToast("Student profile updated");
      } else {
        const newStudent: Student = {
          ...(formData as Student),
          id: `stud-${Date.now()}`,
        };
        setStudents([newStudent, ...students]);
        showToast("Student profile added");
      }
    } else if (activeTab === "teachers") {
      if (editingItem) {
        setTeachers(teachers.map((t) => (t.id === editingItem.id ? { ...(formData as Teacher) } : t)));
        showToast("Teacher profile updated");
      } else {
        const newTeacher: Teacher = {
          ...(formData as Teacher),
          id: `teach-${Date.now()}`,
        };
        setTeachers([newTeacher, ...teachers]);
        showToast("Teacher record created");
      }
    }

    setIsModalOpen(false);
  };

  // 5. IMPORT SAMPLE DATA
  const handleImport = () => {
    if (activeTab === "tasks") {
      setTasks(initialTasks);
      showToast("Tasks reset to sample data", "info");
    } else if (activeTab === "students") {
      setStudents(initialStudents);
      showToast("Students reset to sample data", "info");
    } else if (activeTab === "teachers") {
      setTeachers(initialTeachers);
      showToast("Teachers reset to sample data", "info");
    }
  };

  // 6. EXPORT DATA (Generate File Download)
  const handleExport = () => {
    let dataToExport: any[] = [];
    let filename = "";

    if (activeTab === "tasks") {
      dataToExport = tasks;
      filename = "tasks_export.json";
    } else if (activeTab === "students") {
      dataToExport = students;
      filename = "students_export.json";
    } else if (activeTab === "teachers") {
      dataToExport = teachers;
      filename = "teachers_export.json";
    }

    const dataStr = "data:text/json;charset=utf-8," + encodeURIComponent(JSON.stringify(dataToExport, null, 2));
    const downloadAnchor = document.createElement("a");
    downloadAnchor.setAttribute("href", dataStr);
    downloadAnchor.setAttribute("download", filename);
    document.body.appendChild(downloadAnchor);
    downloadAnchor.click();
    downloadAnchor.remove();

    showToast(`Exported current data to ${filename}`, "success");
  };

  // --- Extra Row Actions ---
  const getExtraActions = (item: any) => {
    if (activeTab === "tasks") {
      const taskItem = item as Task;
      const isCompleted = taskItem.completed;
      return [
        {
          title: isCompleted ? (
            <svg xmlns="http://www.w3.org/2000/svg" width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round" style={{ display: 'block' }}>
              <path d="M3 12a9 9 0 1 0 9-9 9.75 9.75 0 0 0-6.74 2.74L3 8" />
              <path d="M3 3v5h5" />
            </svg>
          ) : (
            <svg xmlns="http://www.w3.org/2000/svg" width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round" style={{ display: 'block' }}>
              <path d="M20 6 9 17l-5-5" />
            </svg>
          ),
          tooltip: isCompleted ? "Mark Pending" : "Complete",
          action: () => {
            setTasks(
              tasks.map((t) => (t.id === taskItem.id ? { ...t, completed: !t.completed } : t))
            );
            showToast(taskItem.completed ? "Task status set to Pending" : "Task marked as Completed", "info");
          },
          style: "btn-action btn-custom-action"
        }
      ];
    }

    if (activeTab === "students") {
      const studentItem = item as Student;
      return [
        {
          title: (
            <svg xmlns="http://www.w3.org/2000/svg" width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round" style={{ display: 'block' }}>
              <path d="m18 15-6-6-6 6" />
            </svg>
          ),
          tooltip: "Boost GPA",
          action: () => {
            if (studentItem.gpa >= 4.0) {
              showToast("GPA is already at maximum (4.0)", "info");
              return;
            }
            const updatedGpa = Math.min(4.0, Math.round((studentItem.gpa + 0.1) * 10) / 10);
            setStudents(
              students.map((s) => (s.id === studentItem.id ? { ...s, gpa: updatedGpa } : s))
            );
            showToast(`GPA boosted for ${studentItem.name} to ${updatedGpa}`, "success");
          },
          style: "btn-action btn-custom-action"
        }
      ];
    }

    if (activeTab === "teachers") {
      const teacherItem = item as Teacher;
      return [
        {
          title: (
            <svg xmlns="http://www.w3.org/2000/svg" width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round" style={{ display: 'block' }}>
              <line x1="12" x2="12" y1="5" y2="19" />
              <line x1="5" x2="19" y1="12" y2="12" />
            </svg>
          ),
          tooltip: "+10% Raise",
          action: () => {
            const updatedSalary = Math.round(teacherItem.salary * 1.1);
            setTeachers(
              teachers.map((t) => (t.id === teacherItem.id ? { ...t, salary: updatedSalary } : t))
            );
            showToast(`Salary raised to $${updatedSalary}/mo`, "success");
          },
          style: "btn-action btn-custom-action"
        }
      ];
    }

    return [];
  };


  // --- Stat Calculators ---
  const getStats = () => {
    if (activeTab === "tasks") {
      const total = tasks.length;
      const completed = tasks.filter((t) => t.completed).length;
      const completionRate = total > 0 ? Math.round((completed / total) * 100) : 0;
      return [
        { label: "Total Tasks", value: total, icon: "📋" },
        { label: "Completed", value: completed, icon: "✅" },
        { label: "Completion Rate", value: `${completionRate}%`, icon: "📈" }
      ];
    } else if (activeTab === "students") {
      const total = students.length;
      const avgGpa = total > 0 ? (students.reduce((acc, s) => acc + s.gpa, 0) / total).toFixed(2) : "0.00";
      const topStudents = students.filter((s) => s.gpa >= 3.5).length;
      return [
        { label: "Total Students", value: total, icon: "🎓" },
        { label: "Average GPA", value: avgGpa, icon: "📊" },
        { label: "Honors (>= 3.5)", value: topStudents, icon: "⭐" }
      ];
    } else {
      const total = teachers.length;
      const totalPayroll = teachers.reduce((acc, t) => acc + t.salary, 0);
      const avgSalary = total > 0 ? Math.round(totalPayroll / total) : 0;
      return [
        { label: "Faculty Count", value: total, icon: "🏫" },
        { label: "Average Salary", value: `$${avgSalary}/mo`, icon: "💰" },
        { label: "Total Payroll", value: `$${totalPayroll}/mo`, icon: "💸" }
      ];
    }
  };

  return (
    <div className="app-container">
      {/* Toast Notification */}
      {toast && (
        <div className={`toast-notification show ${toast.type}`}>
          <div className="toast-content">
            <span className="toast-icon">
              {toast.type === "success" ? "✓" : toast.type === "danger" ? "✕" : "ℹ"}
            </span>
            <span className="toast-message">{toast.message}</span>
          </div>
        </div>
      )}

      {/* Main Header */}
      <header className="app-header">
        <div className="logo-section">
          <div className="logo-icon">∞</div>
          <div className="logo-text">
            <h1>Antigravity CRUD</h1>
            <p>Unified Enterprise Data Operations</p>
          </div>
        </div>

        {/* Tab switcher */}
        <nav className="tab-navigation">
          <button
            className={`tab-btn ${activeTab === "tasks" ? "active" : ""}`}
            onClick={() => setActiveTab("tasks")}
          >
            <span className="tab-icon">📋</span>
            <span className="tab-label">Tasks</span>
            <span className="tab-badge">{tasks.length}</span>
          </button>
          <button
            className={`tab-btn ${activeTab === "students" ? "active" : ""}`}
            onClick={() => setActiveTab("students")}
          >
            <span className="tab-icon">🎓</span>
            <span className="tab-label">Students</span>
            <span className="tab-badge">{students.length}</span>
          </button>
          <button
            className={`tab-btn ${activeTab === "teachers" ? "active" : ""}`}
            onClick={() => setActiveTab("teachers")}
          >
            <span className="tab-icon">🏫</span>
            <span className="tab-label">Teachers</span>
            <span className="tab-badge">{teachers.length}</span>
          </button>
        </nav>
      </header>

      {/* Stat Panels */}
      <section className="stats-dashboard">
        {getStats().map((stat, i) => (
          <div className="stat-card" key={i}>
            <div className="stat-icon-wrapper">{stat.icon}</div>
            <div className="stat-info">
              <span className="stat-label">{stat.label}</span>
              <h3 className="stat-value">{stat.value}</h3>
            </div>
          </div>
        ))}
      </section>

      {/* Main Work Area */}
      <main className="content-workspace">
        <div className="workspace-header">
          <div className="workspace-title-area">
            <h2>
              {activeTab === "tasks" && "Task Workspace"}
              {activeTab === "students" && "Student Directory"}
              {activeTab === "teachers" && "Faculty Board"}
            </h2>
            <p>
              {activeTab === "tasks" && "Manage development items, priorities and resolution statuses."}
              {activeTab === "students" && "Overview student metrics, records, majors, and GPAs."}
              {activeTab === "teachers" && "Supervise faculty designations, departments, and payroll structure."}
            </p>
          </div>

          <CrudActionBar
            onAdd={handleOpenAddForm}
            onImport={handleImport}
            onExport={handleExport}
          />
        </div>

        {/* Table Rendering */}
        <div className="workspace-body">
          {activeTab === "tasks" && (
            <CrudTable
              data={tasks}
              onEdit={handleOpenEditForm}
              onDelete={handleDeleteItem}
              extraActions={getExtraActions}
            />
          )}

          {activeTab === "students" && (
            <CrudTable
              data={students}
              onEdit={handleOpenEditForm}
              onDelete={handleDeleteItem}
              extraActions={getExtraActions}
            />
          )}

          {activeTab === "teachers" && (
            <CrudTable
              data={teachers}
              onEdit={handleOpenEditForm}
              onDelete={handleDeleteItem}
              extraActions={getExtraActions}
            />
          )}
        </div>
      </main>

      {/* Footer */}
      <footer className="app-footer">
        <p>Built with React, Vite & Modern Reusable Component Architecture</p>
      </footer>

      {/* Dialog Modal */}
      {isModalOpen && (
        <div className="modal-overlay" onClick={() => setIsModalOpen(false)}>
          <div className="modal-card" onClick={(e) => e.stopPropagation()}>
            <div className="modal-header">
              <h3>
                {editingItem ? "Edit Record" : "Add Record"} ({activeTab === "tasks" ? "Task" : activeTab === "students" ? "Student" : "Teacher"})
              </h3>
              <button className="close-btn" onClick={() => setIsModalOpen(false)}>✕</button>
            </div>
            
            <form onSubmit={handleSubmitForm} className="modal-form">
              {activeTab === "tasks" && (
                <>
                  <div className="form-group">
                    <label htmlFor="task-title">Title</label>
                    <input
                      id="task-title"
                      type="text"
                      required
                      placeholder="e.g. Add user dashboard"
                      value={formData.title || ""}
                      onChange={(e) => setFormData({ ...formData, title: e.target.value })}
                    />
                  </div>

                  <div className="form-group">
                    <label htmlFor="task-category">Category</label>
                    <input
                      id="task-category"
                      type="text"
                      required
                      placeholder="e.g. Frontend, DB, Security"
                      value={formData.category || ""}
                      onChange={(e) => setFormData({ ...formData, category: e.target.value })}
                    />
                  </div>

                  <div className="form-group">
                    <label htmlFor="task-priority">Priority</label>
                    <select
                      id="task-priority"
                      value={formData.priority || "Medium"}
                      onChange={(e) => setFormData({ ...formData, priority: e.target.value })}
                    >
                      <option value="High">High</option>
                      <option value="Medium">Medium</option>
                      <option value="Low">Low</option>
                    </select>
                  </div>

                  <div className="form-group checkbox-group">
                    <label htmlFor="task-completed" className="checkbox-label">
                      <input
                        id="task-completed"
                        type="checkbox"
                        checked={formData.completed || false}
                        onChange={(e) => setFormData({ ...formData, completed: e.target.checked })}
                      />
                      <span>Mark completed immediately</span>
                    </label>
                  </div>
                </>
              )}

              {activeTab === "students" && (
                <>
                  <div className="form-group">
                    <label htmlFor="student-name">Full Name</label>
                    <input
                      id="student-name"
                      type="text"
                      required
                      placeholder="e.g. Tran Minh Tuan"
                      value={formData.name || ""}
                      onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                    />
                  </div>

                  <div className="form-group">
                    <label htmlFor="student-email">Email Address</label>
                    <input
                      id="student-email"
                      type="email"
                      required
                      placeholder="e.g. tuan.tm@university.edu"
                      value={formData.email || ""}
                      onChange={(e) => setFormData({ ...formData, email: e.target.value })}
                    />
                  </div>

                  <div className="form-group">
                    <label htmlFor="student-major">Major</label>
                    <input
                      id="student-major"
                      type="text"
                      required
                      placeholder="e.g. Computer Science"
                      value={formData.major || ""}
                      onChange={(e) => setFormData({ ...formData, major: e.target.value })}
                    />
                  </div>

                  <div className="form-group">
                    <label htmlFor="student-gpa">Current GPA (0.0 - 4.0)</label>
                    <input
                      id="student-gpa"
                      type="number"
                      min="0.0"
                      max="4.0"
                      step="0.1"
                      required
                      placeholder="e.g. 3.5"
                      value={formData.gpa || 3.0}
                      onChange={(e) => setFormData({ ...formData, gpa: parseFloat(e.target.value) || 0 })}
                    />
                  </div>
                </>
              )}

              {activeTab === "teachers" && (
                <>
                  <div className="form-group">
                    <label htmlFor="teacher-name">Full Name</label>
                    <input
                      id="teacher-name"
                      type="text"
                      required
                      placeholder="e.g. Dr. Ngo Bao Chau"
                      value={formData.name || ""}
                      onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                    />
                  </div>

                  <div className="form-group">
                    <label htmlFor="teacher-email">Email Address</label>
                    <input
                      id="teacher-email"
                      type="email"
                      required
                      placeholder="e.g. nbchau@faculty.edu"
                      value={formData.email || ""}
                      onChange={(e) => setFormData({ ...formData, email: e.target.value })}
                    />
                  </div>

                  <div className="form-group">
                    <label htmlFor="teacher-dept">Department</label>
                    <input
                      id="teacher-dept"
                      type="text"
                      required
                      placeholder="e.g. Mathematics"
                      value={formData.department || ""}
                      onChange={(e) => setFormData({ ...formData, department: e.target.value })}
                    />
                  </div>

                  <div className="form-group">
                    <label htmlFor="teacher-salary">Monthly Salary ($)</label>
                    <input
                      id="teacher-salary"
                      type="number"
                      min="0"
                      step="100"
                      required
                      placeholder="e.g. 5000"
                      value={formData.salary || 4000}
                      onChange={(e) => setFormData({ ...formData, salary: parseInt(e.target.value) || 0 })}
                    />
                  </div>
                </>
              )}

              <div className="modal-actions">
                <button type="button" className="btn-secondary" onClick={() => setIsModalOpen(false)}>
                  Cancel
                </button>
                <button type="submit" className="btn-primary">
                  Save Changes
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}
