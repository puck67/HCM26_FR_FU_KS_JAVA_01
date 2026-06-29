import React, { useState } from "react";
import { LmsDashboard } from "./components/LmsDashboard";
import { LmsModalForm } from "./components/LmsModalForm";
import { GenericCrudPage } from "./components/GenericCrudPage";
import { Modal } from "./components/Modal";
import { NotificationToast } from "./components/NotificationToast";
import { LmsImportModal } from "./components/LmsImportModal";
import { LmsExportModal } from "./components/LmsExportModal";
import type { Course, Lesson, Student, Enrollment } from "./types";
import "./App.css";

type TabType = "dashboard" | "courses" | "lessons" | "students" | "enrollments";

const TAB_NAMES_VIETNAMESE: Record<string, string> = {
  dashboard: "Dashboard",
  courses: "Khóa học",
  lessons: "Bài học",
  students: "Học viên",
  enrollments: "Đăng ký & tài chính"
};

const ENTITY_NAMES_VIETNAMESE: Record<string, string> = {
  course: "khóa học",
  lesson: "bài học",
  student: "học viên",
  enrollment: "đơn đăng ký"
};

// --- Static Initial Mock Data ---
const INITIAL_COURSES: Course[] = [
  {
    id: "C01",
    title: "React & TypeScript Masterclass",
    instructor: "Sarah Connor",
    category: "Frontend Development",
    duration: "40 Hours",
    level: "Intermediate",
    status: "Published",
  },
  {
    id: "C02",
    title: "Python for Data Science",
    instructor: "David Miller",
    category: "Data Science",
    duration: "36 Hours",
    level: "Beginner",
    status: "Published",
  },
  {
    id: "C03",
    title: "Advanced Node.js & Microservices",
    instructor: "Alex Rivera",
    category: "Backend Development",
    duration: "50 Hours",
    level: "Advanced",
    status: "Draft",
  },
  {
    id: "C04",
    title: "Docker & Kubernetes Basics",
    instructor: "Nate Robinson",
    category: "DevOps",
    duration: "24 Hours",
    level: "Beginner",
    status: "Published",
  },
  {
    id: "C05",
    title: "UI/UX Design Fundamentals",
    instructor: "Elena Rostova",
    category: "Frontend Development",
    duration: "30 Hours",
    level: "Beginner",
    status: "Published",
  },
  {
    id: "C06",
    title: "Go (Golang) Backend Bootcamp",
    instructor: "Ken Thompson",
    category: "Backend Development",
    duration: "45 Hours",
    level: "Intermediate",
    status: "Draft",
  },
  {
    id: "C07",
    title: "Deep Learning with PyTorch",
    instructor: "Yann LeCun",
    category: "Data Science",
    duration: "60 Hours",
    level: "Advanced",
    status: "Published",
  }
];

const INITIAL_LESSONS: Lesson[] = [
  {
    id: "L01",
    courseTitle: "React & TypeScript Masterclass",
    title: "Introduction to tsx and Props",
    duration: "15 mins",
    format: "Video",
  },
  {
    id: "L02",
    courseTitle: "React & TypeScript Masterclass",
    title: "Generic Components in React",
    duration: "25 mins",
    format: "Video",
  },
  {
    id: "L03",
    courseTitle: "Python for Data Science",
    title: "Pandas and Numpy basics",
    duration: "45 mins",
    format: "Article",
  },
  {
    id: "L04",
    courseTitle: "Advanced Node.js & Microservices",
    title: "Docker & Containers Setup",
    duration: "30 mins",
    format: "Video",
  },
  {
    id: "L05",
    courseTitle: "Docker & Kubernetes Basics",
    title: "Understanding Container Images",
    duration: "20 mins",
    format: "Video",
  },
  {
    id: "L06",
    courseTitle: "UI/UX Design Fundamentals",
    title: "Design Systems and Typography",
    duration: "35 mins",
    format: "Article",
  },
  {
    id: "L07",
    courseTitle: "Go (Golang) Backend Bootcamp",
    title: "Goroutines and Channels",
    duration: "40 mins",
    format: "Quiz",
  },
  {
    id: "L08",
    courseTitle: "Deep Learning with PyTorch",
    title: "Backpropagation from Scratch",
    duration: "50 mins",
    format: "Video",
  }
];

const INITIAL_STUDENTS: Student[] = [
  {
    id: "S01",
    name: "Alice Johnson",
    email: "alice@gmail.com",
    joinedDate: "2026-01-15",
    status: "Active",
  },
  {
    id: "S02",
    name: "Bob Smith",
    email: "bob@gmail.com",
    joinedDate: "2026-02-10",
    status: "Active",
  },
  {
    id: "S03",
    name: "Charlie Brown",
    email: "charlie@gmail.com",
    joinedDate: "2026-03-01",
    status: "Inactive",
  },
  {
    id: "S04",
    name: "Emily Watson",
    email: "emily@gmail.com",
    joinedDate: "2026-03-12",
    status: "Active",
  },
  {
    id: "S05",
    name: "Frank Miller",
    email: "frank@gmail.com",
    joinedDate: "2026-04-05",
    status: "Active",
  },
  {
    id: "S06",
    name: "Grace Hopper",
    email: "grace@cobol.org",
    joinedDate: "2026-04-18",
    status: "Active",
  },
  {
    id: "S07",
    name: "Henry Cavill",
    email: "henry@gmail.com",
    joinedDate: "2026-05-02",
    status: "Inactive",
  }
];

const INITIAL_ENROLLMENTS: Enrollment[] = [
  {
    id: "E01",
    studentName: "Alice Johnson",
    courseTitle: "React & TypeScript Masterclass",
    enrollmentDate: "2026-06-10",
    paymentStatus: "Paid",
  },
  {
    id: "E02",
    studentName: "Bob Smith",
    courseTitle: "Python for Data Science",
    enrollmentDate: "2026-06-12",
    paymentStatus: "Pending",
  },
  {
    id: "E03",
    studentName: "Alice Johnson",
    courseTitle: "Python for Data Science",
    enrollmentDate: "2026-06-15",
    paymentStatus: "Paid",
  },
  {
    id: "E04",
    studentName: "Emily Watson",
    courseTitle: "Docker & Kubernetes Basics",
    enrollmentDate: "2026-06-18",
    paymentStatus: "Paid",
  },
  {
    id: "E05",
    studentName: "Frank Miller",
    courseTitle: "UI/UX Design Fundamentals",
    enrollmentDate: "2026-06-20",
    paymentStatus: "Pending",
  },
  {
    id: "E06",
    studentName: "Grace Hopper",
    courseTitle: "Go (Golang) Backend Bootcamp",
    enrollmentDate: "2026-06-22",
    paymentStatus: "Paid",
  },
  {
    id: "E07",
    studentName: "Emily Watson",
    courseTitle: "React & TypeScript Masterclass",
    enrollmentDate: "2026-06-23",
    paymentStatus: "Refunded",
  }
];

// --- Pure Helper Functions (Module Scope) ---

export default function App() {
  // --- Consolidated LMS Data State ---
  const [lmsData, setLmsData] = useState<{
    courses: Course[];
    lessons: Lesson[];
    students: Student[];
    enrollments: Enrollment[];
  }>({
    courses: INITIAL_COURSES,
    lessons: INITIAL_LESSONS,
    students: INITIAL_STUDENTS,
    enrollments: INITIAL_ENROLLMENTS,
  });

  // --- Consolidated Modal State ---
  const [modal, setModal] = useState<{
    isOpen: boolean;
    mode: "add" | "edit";
    entityType: "course" | "lesson" | "student" | "enrollment" | null;
    editingItem: any;
  }>({
    isOpen: false,
    mode: "add",
    entityType: null,
    editingItem: null,
  });

  const [formData, setFormData] = useState<any>({});
  const [activeTab, setActiveTab] = useState<TabType>("dashboard");

  const [notification, setNotification] = useState<{
    message: string;
    type: "success" | "info" | "error" | "warning";
    isOpen: boolean;
  }>({
    message: "",
    type: "info",
    isOpen: false,
  });

  const showNotification = (
    message: string,
    type: "success" | "info" | "error" | "warning" = "info"
  ) => {
    setNotification({ message, type, isOpen: true });
  };

  const [importModal, setImportModal] = useState<{
    isOpen: boolean;
    entityType: "course" | "lesson" | "student" | "enrollment" | null;
  }>({ isOpen: false, entityType: null });

  const [exportModal, setExportModal] = useState<{
    isOpen: boolean;
    entityType: string | null;
    columns: string[];
  }>({ isOpen: false, entityType: null, columns: [] });

  const handleExport = (entityType: string) => {
    const list = activeTabConfig?.data || [];
    const cols = list.length > 0 ? Object.keys(list[0]) : [];
    setExportModal({ isOpen: true, entityType, columns: cols });
  };

  // --- Modal Controllers ---
  const handleOpenAdd = (entityType: "course" | "lesson" | "student" | "enrollment") => {
    setFormData({});
    setModal({
      isOpen: true,
      mode: "add",
      entityType,
      editingItem: null,
    });
  };

  const handleOpenEdit = (entityType: "course" | "lesson" | "student" | "enrollment", item: any) => {
    setFormData({ ...item });
    setModal({
      isOpen: true,
      mode: "edit",
      entityType,
      editingItem: item,
    });
  };

  const handleCloseModal = () => {
    setModal({
      isOpen: false,
      mode: "add",
      entityType: null,
      editingItem: null,
    });
    setFormData({});
  };

  // --- Import / Export Handlers ---
  const handleImport = (entityType: "course" | "lesson" | "student" | "enrollment") => {
    setImportModal({ isOpen: true, entityType });
  };

  const handleImportSuccess = (items: any[]) => {
    const entityType = importModal.entityType;
    if (!entityType || items.length === 0) return;

    setLmsData((prev) => {
      if (entityType === "course") {
        return { ...prev, courses: [...prev.courses, ...items] };
      } else if (entityType === "lesson") {
        return { ...prev, lessons: [...prev.lessons, ...items] };
      } else if (entityType === "student") {
        return { ...prev, students: [...prev.students, ...items] };
      } else if (entityType === "enrollment") {
        return { ...prev, enrollments: [...prev.enrollments, ...items] };
      }
      return prev;
    });

    showNotification(`Đã nhập thành công ${items.length} bản ghi cho ${entityType}!`, "success");
    setImportModal({ isOpen: false, entityType: null });
  };

  const handleExportSuccess = (format: "xlsx" | "csv" | "pdf", columns: string[]) => {
    showNotification(
      `Đã xuất dữ liệu ${exportModal.entityType?.toUpperCase()} sang định dạng ${format.toUpperCase()} (gồm ${columns.length} cột) thành công!`,
      "success"
    );
    setExportModal({ isOpen: false, entityType: null, columns: [] });
  };

  // --- CRUD Save Action ---
  const handleSaveItem = (e: React.FormEvent) => {
    e.preventDefault();
    const { mode, entityType, editingItem } = modal;

    if (!entityType) return;

    if (entityType === "course") {
      if (mode === "add") {
        const newId = `C${String(lmsData.courses.length + 1).padStart(2, "0")}`;
        const newCourse: Course = {
          id: newId,
          title: formData.title || "New Course",
          instructor: formData.instructor || "TBA",
          category: formData.category || "General",
          duration: formData.duration || "10 Hours",
          level: formData.level || "Beginner",
          status: formData.status || "Draft",
        };
        setLmsData((prev) => ({ ...prev, courses: [...prev.courses, newCourse] }));
      } else {
        setLmsData((prev) => ({
          ...prev,
          courses: prev.courses.map((c) => (c.id === editingItem.id ? { ...c, ...formData } : c)),
        }));
      }
    } else if (entityType === "lesson") {
      if (mode === "add") {
        const newId = `L${String(lmsData.lessons.length + 1).padStart(2, "0")}`;
        const newLesson: Lesson = {
          id: newId,
          courseTitle: formData.courseTitle || lmsData.courses[0]?.title || "General",
          title: formData.title || "New Lesson",
          duration: formData.duration || "15 mins",
          format: formData.format || "Video",
        };
        setLmsData((prev) => ({ ...prev, lessons: [...prev.lessons, newLesson] }));
      } else {
        setLmsData((prev) => ({
          ...prev,
          lessons: prev.lessons.map((l) => (l.id === editingItem.id ? { ...l, ...formData } : l)),
        }));
      }
    } else if (entityType === "student") {
      if (mode === "add") {
        const newId = `S${String(lmsData.students.length + 1).padStart(2, "0")}`;
        const newStudent: Student = {
          id: newId,
          name: formData.name || "Student Name",
          email: formData.email || "student@email.com",
          joinedDate: new Date().toISOString().split("T")[0],
          status: formData.status || "Active",
        };
        setLmsData((prev) => ({ ...prev, students: [...prev.students, newStudent] }));
      } else {
        setLmsData((prev) => ({
          ...prev,
          students: prev.students.map((s) => (s.id === editingItem.id ? { ...s, ...formData } : s)),
        }));
      }
    } else if (entityType === "enrollment") {
      if (mode === "add") {
        const newId = `E${String(lmsData.enrollments.length + 1).padStart(2, "0")}`;
        const newEnrollment: Enrollment = {
          id: newId,
          studentName: formData.studentName || lmsData.students[0]?.name || "Student",
          courseTitle: formData.courseTitle || lmsData.courses[0]?.title || "Course",
          enrollmentDate: new Date().toISOString().split("T")[0],
          paymentStatus: formData.paymentStatus || "Pending",
        };
        setLmsData((prev) => ({ ...prev, enrollments: [...prev.enrollments, newEnrollment] }));
      } else {
        setLmsData((prev) => ({
          ...prev,
          enrollments: prev.enrollments.map((en) =>
            en.id === editingItem.id ? { ...en, ...formData } : en
          ),
        }));
      }
    }

    showNotification(mode === "add" ? "Đã thêm mới bản ghi thành công!" : "Đã lưu thay đổi thành công!", "success");
    handleCloseModal();
  };

  // --- CRUD Delete Action ---
  const handleDeleteItem = (
    entityType: "course" | "lesson" | "student" | "enrollment",
    item: any
  ) => {
    const confirmDelete = window.confirm(
      `Are you sure you want to delete ${item.title || item.name || item.id}?`
    );
    if (!confirmDelete) return;

    if (entityType === "course") {
      setLmsData((prev) => ({
        ...prev,
        courses: prev.courses.filter((c) => c.id !== item.id),
      }));
    } else if (entityType === "lesson") {
      setLmsData((prev) => ({
        ...prev,
        lessons: prev.lessons.filter((l) => l.id !== item.id),
      }));
    } else if (entityType === "student") {
      setLmsData((prev) => ({
        ...prev,
        students: prev.students.filter((s) => s.id !== item.id),
      }));
    } else if (entityType === "enrollment") {
      setLmsData((prev) => ({
        ...prev,
        enrollments: prev.enrollments.filter((en) => en.id !== item.id),
      }));
    }

    showNotification("Đã xóa bản ghi thành công!", "success");
  };

  // --- Table Row Extra Actions ---
  const getCourseActions = (course: Course) => [
    {
      title: course.status === "Published" ? (
        <span className="material-symbols-outlined" title="Chuyển thành bản nháp (Set Draft)">history</span>
      ) : (
        <span className="material-symbols-outlined" title="Xuất bản (Publish)">publish</span>
      ),
      action: () => {
        setLmsData((prev) => ({
          ...prev,
          courses: prev.courses.map((c) =>
            c.id === course.id
              ? { ...c, status: c.status === "Published" ? "Draft" : "Published" }
              : c
          ),
        }));
        showNotification(
          course.status === "Published" ? "Đã chuyển khóa học thành bản nháp!" : "Đã xuất bản khóa học thành công!",
          course.status === "Published" ? "info" : "success"
        );
      },
      style: course.status === "Published" ? "action-btn warning" : "action-btn success",
    },
  ];

  const getStudentActions = (student: Student) => [
    {
      title: student.status === "Active" ? (
        <span className="material-symbols-outlined" title="Ngưng kích hoạt (Deactivate)">person_off</span>
      ) : (
        <span className="material-symbols-outlined" title="Kích hoạt (Activate)">check_circle</span>
      ),
      action: () => {
        setLmsData((prev) => ({
          ...prev,
          students: prev.students.map((s) =>
            s.id === student.id
              ? { ...s, status: s.status === "Active" ? "Inactive" : "Active" }
              : s
          ),
        }));
        showNotification(
          student.status === "Active" ? "Đã ngưng hoạt động học viên!" : "Đã kích hoạt học viên thành công!",
          student.status === "Active" ? "info" : "success"
        );
      },
      style: student.status === "Active" ? "action-btn danger" : "action-btn success",
    },
  ];

  const getEnrollmentActions = (enrollment: Enrollment) => [
    {
      title: enrollment.paymentStatus === "Paid" ? (
        <span className="material-symbols-outlined" title="Hoàn tiền (Refund)">keyboard_return</span>
      ) : (
        <span className="material-symbols-outlined" title="Đánh dấu đã thanh toán (Mark Paid)">price_check</span>
      ),
      action: () => {
        setLmsData((prev) => ({
          ...prev,
          enrollments: prev.enrollments.map((en) =>
            en.id === enrollment.id
              ? {
                ...en,
                paymentStatus: en.paymentStatus === "Paid" ? "Refunded" : "Paid",
              }
              : en
          ),
        }));
        showNotification(
          enrollment.paymentStatus === "Paid" ? "Đã hoàn tiền đơn đăng ký!" : "Đã duyệt thanh toán thành công!",
          enrollment.paymentStatus === "Paid" ? "info" : "success"
        );
      },
      style: enrollment.paymentStatus === "Paid" ? "action-btn danger" : "action-btn success",
    },
  ];

  // --- Dashboard Revenue Calculation ---
  const totalRevenue = lmsData.enrollments.filter((e) => e.paymentStatus === "Paid").length * 199;

  // --- Dynamic Tab Configurations to render generically ---
  const activeTabConfig = (() => {
    switch (activeTab) {
      case "courses":
        return {
          data: lmsData.courses,
          entityType: "course" as const,
          exportKey: "courses",
          extraActions: getCourseActions,
          filterFn: (c: Course, query: string) =>
            c.title.toLowerCase().includes(query.toLowerCase()) ||
            c.instructor.toLowerCase().includes(query.toLowerCase()) ||
            c.category.toLowerCase().includes(query.toLowerCase())
        };
      case "lessons":
        return {
          data: lmsData.lessons,
          entityType: "lesson" as const,
          exportKey: "lessons",
          extraActions: undefined,
          filterFn: (l: Lesson, query: string) =>
            l.title.toLowerCase().includes(query.toLowerCase()) ||
            l.courseTitle.toLowerCase().includes(query.toLowerCase())
        };
      case "students":
        return {
          data: lmsData.students,
          entityType: "student" as const,
          exportKey: "students",
          extraActions: getStudentActions,
          filterFn: (s: Student, query: string) =>
            s.name.toLowerCase().includes(query.toLowerCase()) ||
            s.email.toLowerCase().includes(query.toLowerCase())
        };
      case "enrollments":
        return {
          data: lmsData.enrollments,
          entityType: "enrollment" as const,
          exportKey: "enrollments",
          extraActions: getEnrollmentActions,
          filterFn: (en: Enrollment, query: string) =>
            en.studentName.toLowerCase().includes(query.toLowerCase()) ||
            en.courseTitle.toLowerCase().includes(query.toLowerCase())
        };
      default:
        return null;
    }
  })();

  return (
    <div className="app-layout">
      {/* Sidebar */}
      <aside className="sidebar">
        <div className="sidebar-header">
          <div className="logo-icon">
            <span className="material-symbols-outlined fill-icon">school</span>
          </div>
          <div>
            <h1 className="text-headline-sm font-bold text-primary" style={{ lineHeight: 1 }}>MONA.Software</h1>
            <p className="text-label-md text-outline uppercase" style={{ letterSpacing: '1px' }}>Edutech LMS</p>
          </div>
        </div>

        <nav className="sidebar-nav">
          <div className="nav-group-title">Cơ bản</div>
          <button
            type="button"
            className={`nav-item ${activeTab === "dashboard" ? "active" : ""}`}
            onClick={() => setActiveTab("dashboard")}
          >
            <span className="material-symbols-outlined">dashboard</span>
            Dashboard
          </button>
          <button
            type="button"
            className={`nav-item ${activeTab === "students" ? "active" : ""}`}
            onClick={() => setActiveTab("students")}
          >
            <span className="material-symbols-outlined">group</span>
            Học viên
          </button>
          <button
            type="button"
            className={`nav-item ${activeTab === "courses" ? "active" : ""}`}
            onClick={() => setActiveTab("courses")}
          >
            <span className="material-symbols-outlined">class</span>
            Khóa học
          </button>
          <button
            type="button"
            className={`nav-item ${activeTab === "lessons" ? "active" : ""}`}
            onClick={() => setActiveTab("lessons")}
          >
            <span className="material-symbols-outlined">menu_book</span>
            Bài học
          </button>

          <div className="nav-group-title">Quản lý</div>
          <button
            type="button"
            className={`nav-item ${activeTab === "enrollments" ? "active" : ""}`}
            onClick={() => setActiveTab("enrollments")}
          >
            <span className="material-symbols-outlined">payments</span>
            Đăng ký & Tài chính
          </button>
        </nav>

        <div className="sidebar-footer">
          <button type="button" className="nav-item">
            <span className="material-symbols-outlined">settings</span>
            Cài đặt
          </button>
          <button type="button" className="nav-item" style={{ color: 'var(--color-error)' }}>
            <span className="material-symbols-outlined" style={{ color: 'var(--color-error)' }}>logout</span>
            Đăng xuất
          </button>
        </div>
      </aside>

      {/* Top Navbar */}
      <header className="top-navbar">
        <div className="breadcrumb">
          <span className="material-symbols-outlined">chevron_right</span>
          <span className="breadcrumb-item">
            {activeTab === 'enrollments' ? 'Quản lý' : 'Cơ bản'}
          </span>
          <span>/</span>
          <span className="breadcrumb-item active">
            {TAB_NAMES_VIETNAMESE[activeTab] || activeTab}
          </span>
        </div>

        <div className="header-actions">
          <div className="location-badge">
            <span className="material-symbols-outlined">location_on</span>
            Tân Bình HCM
            <span className="material-symbols-outlined" style={{ fontSize: '16px' }}>expand_more</span>
          </div>

          <div style={{ display: 'flex', gap: '8px', alignItems: 'center' }}>
            <button type="button" className="btn-icon-only" style={{ border: 'none' }}>
              <span className="material-symbols-outlined">search</span>
            </button>
            <button type="button" className="btn-icon-only" style={{ border: 'none', position: 'relative' }}>
              <span className="material-symbols-outlined">notifications</span>
              <span style={{ position: 'absolute', top: '8px', right: '8px', width: '8px', height: '8px', background: 'var(--color-error)', borderRadius: '50%' }}></span>
            </button>
          </div>

          <div className="user-profile">
            <div className="user-info">
              <p className="user-name">Admin MONA</p>
              <p className="user-role">Quản trị viên</p>
            </div>
            <div className="avatar"></div>
          </div>
        </div>
      </header>

      {/* Main Content */}
      <main className="main-wrapper">
        <div className="main-content">
          {activeTab === "dashboard" ? (
            <LmsDashboard
              coursesCount={lmsData.courses.length}
              lessonsCount={lmsData.lessons.length}
              activeStudentsCount={lmsData.students.filter((s) => s.status === "Active").length}
              totalRevenue={totalRevenue}
              recentEnrollments={lmsData.enrollments.slice(-3).reverse()}
              recentCourses={lmsData.courses.slice(0, 3)}
            />
          ) : (
            activeTabConfig && (
              <GenericCrudPage<any>
                data={activeTabConfig.data}
                onAdd={() => handleOpenAdd(activeTabConfig.entityType)}
                onEdit={(item) => handleOpenEdit(activeTabConfig.entityType, item)}
                onDelete={(item) => handleDeleteItem(activeTabConfig.entityType, item)}
                onImport={() => handleImport(activeTabConfig.entityType)}
                onExport={() => handleExport(activeTabConfig.exportKey)}
                extraActions={activeTabConfig.extraActions}
                filterFn={activeTabConfig.filterFn}
              />
            )
          )}
        </div>
      </main>

      {/* Reusable Dialog Modal */}
      <Modal
        isOpen={modal.isOpen}
        title={
          modal.mode === "add"
            ? `Thêm mới ${modal.entityType ? (ENTITY_NAMES_VIETNAMESE[modal.entityType] || modal.entityType) : ""}`
            : `Chỉnh sửa ${modal.entityType ? (ENTITY_NAMES_VIETNAMESE[modal.entityType] || modal.entityType) : ""}: ${modal.editingItem?.id}`
        }
        onClose={handleCloseModal}
        footer={
          <>
            <button type="button" className="btn btn-secondary" onClick={handleCloseModal}>
              Hủy
            </button>
            <button
              type="submit"
              className="btn btn-primary"
              form="lms-entity-form"
            >
              <span className="material-symbols-outlined" style={{ fontSize: '18px' }}>save</span>
              Lưu thay đổi
            </button>
          </>
        }
      >
        {modal.entityType && (
          <LmsModalForm
            entityType={modal.entityType}
            formData={formData}
            setFormData={setFormData}
            courses={lmsData.courses}
            students={lmsData.students}
            onSubmit={handleSaveItem}
          />
        )}
      </Modal>

      {importModal.isOpen && importModal.entityType && (
        <LmsImportModal
          isOpen={importModal.isOpen}
          onClose={() => setImportModal({ isOpen: false, entityType: null })}
          entityType={importModal.entityType}
          onImport={handleImportSuccess}
        />
      )}

      {exportModal.isOpen && exportModal.entityType && (
        <LmsExportModal
          isOpen={exportModal.isOpen}
          onClose={() => setExportModal({ isOpen: false, entityType: null, columns: [] })}
          entityType={exportModal.entityType}
          columns={exportModal.columns}
          onExport={handleExportSuccess}
        />
      )}

      <NotificationToast
        message={notification.message}
        type={notification.type}
        isOpen={notification.isOpen}
        onClose={() => setNotification((prev) => ({ ...prev, isOpen: false }))}
      />
    </div>
  );
}