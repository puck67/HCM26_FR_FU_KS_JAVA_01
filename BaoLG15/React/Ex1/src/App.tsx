import { useState, useEffect } from 'react';
import { CrudTable } from './components/CrudTable';
import { CrudActionBar } from './components/CrudActionBar';
import { Modal } from './components/Modal';
import { DynamicForm } from './components/DynamicForm';
import type { FieldConfig } from './components/DynamicForm';
import type { ButtonConfig } from './components/ButtonList';

interface Task {
  id: string;
  title: string;
  priority: 'High' | 'Medium' | 'Low';
  status: 'Pending' | 'Completed';
  dueDate: string;
}

interface Student {
  id: string;
  fullName: string;
  email: string;
  gpa: number;
  major: string;
  enrollmentYear: number;
}

interface Teacher {
  id: string;
  fullName: string;
  email: string;
  department: string;
  salary: number;
  status: 'Active' | 'On Leave';
}

type TabType = 'Task' | 'Student' | 'Teacher';

const initialTasks: Task[] = [
  { id: 't-1', title: 'Setup Sprint backlog board', priority: 'High', status: 'Completed', dueDate: '2026-06-30' },
  { id: 't-2', title: 'Review API contracts', priority: 'Medium', status: 'Pending', dueDate: '2026-07-02' },
  { id: 't-3', title: 'Build generic CRUD components', priority: 'High', status: 'Pending', dueDate: '2026-06-25' },
  { id: 't-4', title: 'Design premium styles', priority: 'High', status: 'Completed', dueDate: '2026-06-24' },
  { id: 't-5', title: 'Write walkthrough guide', priority: 'Low', status: 'Pending', dueDate: '2026-07-10' },
];

const initialStudents: Student[] = [
  { id: 's-101', fullName: 'Nguyen Van A', email: 'a.nguyen@fsoft.com.vn', gpa: 3.8, major: 'Computer Science', enrollmentYear: 2022 },
  { id: 's-102', fullName: 'Tran Thi B', email: 'b.tran@fsoft.com.vn', gpa: 3.25, major: 'Software Engineering', enrollmentYear: 2023 },
  { id: 's-103', fullName: 'Le Van C', email: 'c.le@fsoft.com.vn', gpa: 2.9, major: 'Information Tech', enrollmentYear: 2022 },
  { id: 's-104', fullName: 'Pham Minh D', email: 'd.pham@fsoft.com.vn', gpa: 3.95, major: 'Data Science', enrollmentYear: 2024 },
];

const initialTeachers: Teacher[] = [
  { id: 'e-201', fullName: 'Dr. Robert Smith', email: 'robert.smith@university.edu', department: 'Computer Science', salary: 8500, status: 'Active' },
  { id: 'e-202', fullName: 'Prof. Emily Watson', email: 'emily.watson@university.edu', department: 'Mathematics', salary: 7800, status: 'Active' },
  { id: 'e-203', fullName: 'Dr. Albert Einstein', email: 'albert.e@university.edu', department: 'Physics', salary: 9500, status: 'On Leave' },
];

export default function App() {
  const [currentTab, setCurrentTab] = useState<TabType>('Task');
  const [searchQuery, setSearchQuery] = useState('');

  const [tasks, setTasks] = useState<Task[]>(initialTasks);
  const [students, setStudents] = useState<Student[]>(initialStudents);
  const [teachers, setTeachers] = useState<Teacher[]>(initialTeachers);

  const [alertMsg, setAlertMsg] = useState<string | null>(null);

  const [isAddOpen, setIsAddOpen] = useState(false);
  const [isEditOpen, setIsEditOpen] = useState(false);
  const [isDeleteOpen, setIsDeleteOpen] = useState(false);

  const [selectedItem, setSelectedItem] = useState<Task | Student | Teacher | null>(null);

  useEffect(() => {
    if (alertMsg) {
      const timer = setTimeout(() => setAlertMsg(null), 4000);
      return () => clearTimeout(timer);
    }
  }, [alertMsg]);

  const triggerAlert = (msg: string) => {
    setAlertMsg(msg);
  };

  const handleExport = () => {
    let dataToExport: Record<string, unknown>[];
    let filename: string;

    if (currentTab === 'Task') {
      dataToExport = tasks as unknown as Record<string, unknown>[];
      filename = 'tasks_list.csv';
    } else if (currentTab === 'Student') {
      dataToExport = students as unknown as Record<string, unknown>[];
      filename = 'students_list.csv';
    } else {
      dataToExport = teachers as unknown as Record<string, unknown>[];
      filename = 'teachers_list.csv';
    }

    if (dataToExport.length === 0) {
      triggerAlert('No data to export!');
      return;
    }

    const headers = Object.keys(dataToExport[0]).join(',');
    const rows = dataToExport.map((item) =>
      Object.values(item)
        .map((val) => `"${String(val).replace(/"/g, '""')}"`)
        .join(',')
    );
    const csvContent = 'data:text/csv;charset=utf-8,' + [headers, ...rows].join('\n');
    const encodedUri = encodeURI(csvContent);

    const link = document.createElement('a');
    link.setAttribute('href', encodedUri);
    link.setAttribute('download', filename);
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);

    triggerAlert(`Successfully exported ${currentTab} data to CSV.`);
  };

  const handleImport = () => {
    if (currentTab === 'Task') {
      const imports: Task[] = [
        { id: `t-${Date.now()}-1`, title: 'Deploy build pipeline', priority: 'High', status: 'Pending', dueDate: '2026-07-01' },
        { id: `t-${Date.now()}-2`, title: 'Conduct user feedback session', priority: 'Low', status: 'Pending', dueDate: '2026-07-15' },
      ];
      setTasks((prev) => [...prev, ...imports]);
      triggerAlert('Imported 2 sample Tasks.');
    } else if (currentTab === 'Student') {
      const imports: Student[] = [
        { id: `s-${Date.now()}-1`, fullName: 'Elena Rostova', email: 'elena.r@fsoft.com.vn', gpa: 3.65, major: 'Data Science', enrollmentYear: 2024 },
        { id: `s-${Date.now()}-2`, fullName: 'John Doe', email: 'john.doe@fsoft.com.vn', gpa: 3.12, major: 'Computer Science', enrollmentYear: 2023 },
      ];
      setStudents((prev) => [...prev, ...imports]);
      triggerAlert('Imported 2 sample Students.');
    } else {
      const imports: Teacher[] = [
        { id: `e-${Date.now()}-1`, fullName: 'Dr. Alan Turing', email: 'turing.a@university.edu', department: 'Computer Science', salary: 12000, status: 'Active' },
        { id: `e-${Date.now()}-2`, fullName: 'Dr. Marie Curie', email: 'curie.m@university.edu', department: 'Physics', salary: 11000, status: 'Active' },
      ];
      setTeachers((prev) => [...prev, ...imports]);
      triggerAlert('Imported 2 sample Teachers.');
    }
  };

  const taskFields: FieldConfig[] = [
    { name: 'title', label: 'Task Title', type: 'text', required: true, placeholder: 'Write something descriptive...' },
    { name: 'priority', label: 'Priority', type: 'select', required: true, options: ['High', 'Medium', 'Low'] },
    { name: 'status', label: 'Status', type: 'select', required: true, options: ['Pending', 'Completed'] },
    { name: 'dueDate', label: 'Due Date', type: 'date', required: true },
  ];

  const studentFields: FieldConfig[] = [
    { name: 'fullName', label: 'Full Name', type: 'text', required: true, placeholder: 'Firstname Lastname' },
    { name: 'email', label: 'Email Address', type: 'email', required: true, placeholder: 'name@fsoft.com.vn' },
    { name: 'gpa', label: 'Cumulative GPA', type: 'number', required: true, min: 0.0, max: 4.0, placeholder: 'e.g. 3.75' },
    { name: 'major', label: 'Major Field', type: 'select', required: true, options: ['Computer Science', 'Software Engineering', 'Information Tech', 'Data Science'] },
    { name: 'enrollmentYear', label: 'Enrollment Year', type: 'number', required: true, min: 2018, max: 2026, placeholder: 'e.g. 2023' },
  ];

  const teacherFields: FieldConfig[] = [
    { name: 'fullName', label: 'Full Name', type: 'text', required: true, placeholder: 'Title. First Last' },
    { name: 'email', label: 'University Email', type: 'email', required: true, placeholder: 'name@university.edu' },
    { name: 'department', label: 'Department', type: 'select', required: true, options: ['Computer Science', 'Mathematics', 'Physics', 'Engineering'] },
    { name: 'salary', label: 'Monthly Salary (USD)', type: 'number', required: true, min: 1000, max: 50000, placeholder: 'e.g. 8500' },
    { name: 'status', label: 'Employment Status', type: 'select', required: true, options: ['Active', 'On Leave'] },
  ];

  const getActiveFields = () => {
    if (currentTab === 'Task') return taskFields;
    if (currentTab === 'Student') return studentFields;
    return teacherFields;
  };

  const handleAddSubmit = (values: Record<string, unknown>) => {
    if (currentTab === 'Task') {
      const newItem: Task = {
        id: `t-${Date.now()}`,
        title: String(values.title),
        priority: values.priority as 'High' | 'Medium' | 'Low',
        status: values.status as 'Pending' | 'Completed',
        dueDate: String(values.dueDate),
      };
      setTasks((prev) => [newItem, ...prev]);
      triggerAlert(`Added new task "${newItem.title}" successfully.`);
    } else if (currentTab === 'Student') {
      const newItem: Student = {
        id: `s-${Date.now()}`,
        fullName: String(values.fullName),
        email: String(values.email),
        gpa: Number(values.gpa),
        major: String(values.major),
        enrollmentYear: Number(values.enrollmentYear),
      };
      setStudents((prev) => [newItem, ...prev]);
      triggerAlert(`Added student "${newItem.fullName}" successfully.`);
    } else {
      const newItem: Teacher = {
        id: `e-${Date.now()}`,
        fullName: String(values.fullName),
        email: String(values.email),
        department: String(values.department),
        salary: Number(values.salary),
        status: values.status as 'Active' | 'On Leave',
      };
      setTeachers((prev) => [newItem, ...prev]);
      triggerAlert(`Added teacher "${newItem.fullName}" successfully.`);
    }
    setIsAddOpen(false);
  };

  const handleEditSubmit = (values: Record<string, unknown>) => {
    if (currentTab === 'Task') {
      setTasks((prev) =>
        prev.map((item) => (item.id === selectedItem?.id ? { ...item, ...values } as Task : item))
      );
      triggerAlert(`Task updated successfully.`);
    } else if (currentTab === 'Student') {
      setStudents((prev) =>
        prev.map((item) => (item.id === selectedItem?.id ? { ...item, ...values } as Student : item))
      );
      triggerAlert(`Student records updated.`);
    } else {
      setTeachers((prev) =>
        prev.map((item) => (item.id === selectedItem?.id ? { ...item, ...values } as Teacher : item))
      );
      triggerAlert(`Teacher record updated.`);
    }
    setIsEditOpen(false);
    setSelectedItem(null);
  };

  const handleDeleteSubmit = () => {
    if (currentTab === 'Task') {
      setTasks((prev) => prev.filter((item) => item.id !== selectedItem?.id));
      triggerAlert('Task record deleted.');
    } else if (currentTab === 'Student') {
      setStudents((prev) => prev.filter((item) => item.id !== selectedItem?.id));
      triggerAlert('Student record deleted.');
    } else {
      setTeachers((prev) => prev.filter((item) => item.id !== selectedItem?.id));
      triggerAlert('Teacher record deleted.');
    }
    setIsDeleteOpen(false);
    setSelectedItem(null);
  };

  const handleEditClick = (item: Task | Student | Teacher) => {
    setSelectedItem(item);
    setIsEditOpen(true);
  };

  const handleDeleteClick = (item: Task | Student | Teacher) => {
    setSelectedItem(item);
    setIsDeleteOpen(true);
  };

  const getExtraActions = (item: Task | Student | Teacher): ButtonConfig[] => {
    if (currentTab === 'Task') {
      const task = item as Task;
      if (task.status === 'Pending') {
        return [
          {
            title: 'Complete',
            action: () => {
              setTasks((prev) =>
                prev.map((t) => (t.id === task.id ? { ...t, status: 'Completed' } : t))
              );
              triggerAlert(`Marked task "${task.title}" as completed.`);
            },
            style: 'btn-complete',
            icon: (
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round">
                <polyline points="20 6 9 17 4 12" />
              </svg>
            ),
          },
        ];
      }
    }

    if (currentTab === 'Teacher') {
      const teacher = item as Teacher;
      return [
        {
          title: 'Bonus 10%',
          action: () => {
            setTeachers((prev) =>
              prev.map((t) => (t.id === teacher.id ? { ...t, salary: Math.round(t.salary * 1.1) } : t))
            );
            triggerAlert(`Granted 10% salary bonus to ${teacher.fullName}.`);
          },
          style: 'btn-edit',
          icon: (
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
              <line x1="12" y1="1" x2="12" y2="23" />
              <path d="M17 5H9.5a3.5 3.5 0 0 0 0 7h5a3.5 3.5 0 0 1 0 7H6" />
            </svg>
          ),
        },
      ];
    }

    return [];
  };

  const getActiveTabLength = () => {
    if (currentTab === 'Task') return tasks.length;
    if (currentTab === 'Student') return students.length;
    return teachers.length;
  };

  return (
    <div className="admin-layout">
      <aside className="admin-sidebar">
        <div className="sidebar-logo">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round">
            <path d="M12 2L2 7l10 5 10-5-10-5z" />
            <path d="M2 17l10 5 10-5" />
            <path d="M2 12l10 5 10-5" />
          </svg>
          <span>FSoft Admin panel</span>
        </div>

        <nav className="sidebar-menu">
          <button
            className={`sidebar-item ${currentTab === 'Task' ? 'active' : ''}`}
            onClick={() => {
              setCurrentTab('Task');
              setSearchQuery('');
            }}
          >
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
              <polyline points="9 11 12 14 22 4" />
              <path d="M21 12v7a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h11" />
            </svg>
            <span>Task Management</span>
          </button>

          <button
            className={`sidebar-item ${currentTab === 'Student' ? 'active' : ''}`}
            onClick={() => {
              setCurrentTab('Student');
              setSearchQuery('');
            }}
          >
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
              <path d="M22 10v6M2 10l10-5 10 5-10 5z" />
              <path d="M6 12v5c0 2 2 3 6 3s6-1 6-3v-5" />
            </svg>
            <span>Student Management</span>
          </button>

          <button
            className={`sidebar-item ${currentTab === 'Teacher' ? 'active' : ''}`}
            onClick={() => {
              setCurrentTab('Teacher');
              setSearchQuery('');
            }}
          >
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
              <path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2" />
              <circle cx="9" cy="7" r="4" />
              <path d="M23 21v-2a4 4 0 0 0-3-3.87" />
              <path d="M16 3.13a4 4 0 0 1 0 7.75" />
            </svg>
            <span>Teacher Management</span>
          </button>
        </nav>

        <div className="sidebar-footer">
          <div className="profile-avatar">AD</div>
          <div className="profile-info">
            <span className="profile-name">Administrator</span>
            <span className="profile-role">Super Admin</span>
          </div>
        </div>
      </aside>

      <main className="admin-main">
        <header className="dashboard-header">
          <div className="header-title-section">
            <h1>{currentTab}s</h1>
            <p>Manage list details, perform search operations, and export data records</p>
          </div>
          <div className="profile-info" style={{ textAlign: 'right' }}>
            <span className="profile-name" style={{ fontSize: '18px', fontWeight: 700 }}>
              {getActiveTabLength()} Total
            </span>
            <span className="profile-role">Active Registry entries</span>
          </div>
        </header>

        {alertMsg && (
          <div className="status-banner">
            <span>{alertMsg}</span>
            <button className="status-banner-close" onClick={() => setAlertMsg(null)}>
              ✕
            </button>
          </div>
        )}

        <section className="dashboard-card">
          <CrudActionBar
            onAdd={() => setIsAddOpen(true)}
            onImport={handleImport}
            onExport={handleExport}
            searchQuery={searchQuery}
            onSearchChange={setSearchQuery}
            entityName={currentTab}
          />

          {currentTab === 'Task' && (
            <CrudTable
              data={tasks}
              onEdit={handleEditClick}
              onDelete={handleDeleteClick}
              extraActions={getExtraActions}
              searchQuery={searchQuery}
            />
          )}

          {currentTab === 'Student' && (
            <CrudTable
              data={students}
              onEdit={handleEditClick}
              onDelete={handleDeleteClick}
              extraActions={getExtraActions}
              searchQuery={searchQuery}
            />
          )}

          {currentTab === 'Teacher' && (
            <CrudTable
              data={teachers}
              onEdit={handleEditClick}
              onDelete={handleDeleteClick}
              extraActions={getExtraActions}
              searchQuery={searchQuery}
            />
          )}
        </section>
      </main>

      <Modal isOpen={isAddOpen} onClose={() => setIsAddOpen(false)} title={`Add New ${currentTab}`}>
        <DynamicForm
          key={isAddOpen ? 'add-active' : 'add-inactive'}
          fields={getActiveFields()}
          onSubmit={handleAddSubmit}
          onCancel={() => setIsAddOpen(false)}
        />
      </Modal>

      <Modal
        isOpen={isEditOpen}
        onClose={() => {
          setIsEditOpen(false);
          setSelectedItem(null);
        }}
        title={`Edit ${currentTab}`}
      >
        <DynamicForm
          key={selectedItem?.id || 'edit-inactive'}
          fields={getActiveFields()}
          initialValues={selectedItem ? (selectedItem as unknown as Record<string, unknown>) : undefined}
          onSubmit={handleEditSubmit}
          onCancel={() => {
            setIsEditOpen(false);
            setSelectedItem(null);
          }}
        />
      </Modal>

      <Modal
        isOpen={isDeleteOpen}
        onClose={() => {
          setIsDeleteOpen(false);
          setSelectedItem(null);
        }}
        title="Confirm Deletion"
      >
        <div className="confirm-dialog-content">
          Are you sure you want to delete this {currentTab.toLowerCase()}:{' '}
          <strong>
            {selectedItem
              ? ('title' in selectedItem
                ? selectedItem.title
                : (selectedItem as Student | Teacher).fullName)
              : ''}
          </strong>? This action cannot be
          undone.
        </div>
        <div className="form-actions">
          <button
            type="button"
            className="app-btn btn-secondary"
            onClick={() => {
              setIsDeleteOpen(false);
              setSelectedItem(null);
            }}
          >
            Cancel
          </button>
          <button type="button" className="app-btn btn-danger" onClick={handleDeleteSubmit}>
            Delete Permanently
          </button>
        </div>
      </Modal>
    </div>
  );
}
