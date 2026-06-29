import { useState } from "react";
import { CrudActionBar } from "@/components/CrudActionBar";
import { CrudTable } from "@/components/CrudTable";
import type { Student } from "@/types/Student";

const INITIAL_STUDENTS: Student[] = [
    { id: 1, name: "Nguyễn Văn A", email: "vana@email.com" },
    { id: 2, name: "Trần Thị B", email: "thib@email.com" },
    { id: 3, name: "Lê Văn C", email: "vanc@email.com" },
    { id: 4, name: "Phạm Minh D", email: "minhd@email.com" },
    { id: 5, name: "Hoàng Thị E", email: "thie@email.com" },
];

export function StudentManagement() {
    const [students, setStudents] = useState<Student[]>(INITIAL_STUDENTS);
    const [showForm, setShowForm] = useState(false);
    const [editingStudent, setEditingStudent] = useState<Student | null>(null);
    const [searchQuery, setSearchQuery] = useState("");

    // Form state
    const [formName, setFormName] = useState("");
    const [formEmail, setFormEmail] = useState("");

    // ── Filtered students ──
    const filteredStudents = students.filter(
        (s) =>
            s.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
            s.email.toLowerCase().includes(searchQuery.toLowerCase()) ||
            String(s.id).includes(searchQuery)
    );

    // ── Open Add Form ──
    const openStudentForm = () => {
        setEditingStudent(null);
        setFormName("");
        setFormEmail("");
        setShowForm(true);
    };

    // ── Open Edit Form ──
    const openStudentEditForm = (student: Student) => {
        setEditingStudent(student);
        setFormName(student.name);
        setFormEmail(student.email);
        setShowForm(true);
    };

    // ── Save (Add or Update) ──
    const saveStudent = () => {
        if (!formName.trim() || !formEmail.trim()) return;

        if (editingStudent) {
            setStudents((prev) =>
                prev.map((s) =>
                    s.id === editingStudent.id
                        ? { ...s, name: formName.trim(), email: formEmail.trim() }
                        : s
                )
            );
        } else {
            const newId = students.length > 0 ? Math.max(...students.map((s) => s.id)) + 1 : 1;
            setStudents((prev) => [
                ...prev,
                { id: newId, name: formName.trim(), email: formEmail.trim() },
            ]);
        }

        setShowForm(false);
        setEditingStudent(null);
    };

    // ── Delete ──
    const deleteStudent = (id: number) => {
        if (confirm("Do you want to delete this student?")) {
            setStudents((prev) => prev.filter((s) => s.id !== id));
        }
    };

    // ── Import (sample data) ──
    const importStudents = () => {
        const sampleImport: Student[] = [
            { id: Date.now(), name: "Import Student 1", email: "import1@email.com" },
            { id: Date.now() + 1, name: "Import Student 2", email: "import2@email.com" },
        ];
        setStudents((prev) => [...prev, ...sampleImport]);
    };

    // ── Export (download JSON) ──
    const exportStudents = () => {
        const json = JSON.stringify(students, null, 2);
        const blob = new Blob([json], { type: "application/json" });
        const url = URL.createObjectURL(blob);
        const a = document.createElement("a");
        a.href = url;
        a.download = "students.json";
        a.click();
        URL.revokeObjectURL(url);
    };

    return (
        <div className="page">
            {/* ── Header ── */}
            <header className="page-header">
                <div className="header-left">
                    <div className="header-icon">🎓</div>
                    <div>
                        <h1>Student Management</h1>
                        <p className="header-subtitle">Manage your student records</p>
                    </div>
                </div>
                <div className="header-badge">
                    <span className="badge">{students.length}</span>
                    <span className="badge-label">Total Students</span>
                </div>
            </header>

            {/* ── Toolbar: Search + Actions ── */}
            <div className="toolbar">
                <div className="search-box">
                    <span className="search-icon">🔍</span>
                    <input
                        type="text"
                        placeholder="Search by name, email or ID..."
                        value={searchQuery}
                        onChange={(e) => setSearchQuery(e.target.value)}
                    />
                    {searchQuery && (
                        <button className="search-clear" onClick={() => setSearchQuery("")}>✕</button>
                    )}
                </div>
                <CrudActionBar
                    onAdd={() => openStudentForm()}
                    onImport={() => importStudents()}
                    onExport={() => exportStudents()}
                />
            </div>

            {/* ── Search Result Info ── */}
            {searchQuery && (
                <div className="search-info">
                    Showing <strong>{filteredStudents.length}</strong> of <strong>{students.length}</strong> students
                    for "<strong>{searchQuery}</strong>"
                </div>
            )}

            {/* ── Table ── */}
            <CrudTable
                data={filteredStudents}
                onEdit={(s) => openStudentEditForm(s)}
                onDelete={(s) => deleteStudent(s.id)}
            />

            {/* ── Modal Form ── */}
            {showForm && (
                <div className="modal-overlay" onClick={() => setShowForm(false)}>
                    <div className="modal" onClick={(e) => e.stopPropagation()}>
                        <div className="modal-header">
                            <div className="modal-title-group">
                                <span className="modal-icon">{editingStudent ? "✏️" : "➕"}</span>
                                <div>
                                    <h2>{editingStudent ? "Edit Student" : "Add New Student"}</h2>
                                    <p className="modal-desc">
                                        {editingStudent
                                            ? "Update the student information below"
                                            : "Fill in the details to add a new student"}
                                    </p>
                                </div>
                            </div>
                            <button className="modal-close" onClick={() => setShowForm(false)}>✕</button>
                        </div>

                        <div className="form-body">
                            <div className="form-group">
                                <label htmlFor="student-name">Full Name</label>
                                <input
                                    id="student-name"
                                    type="text"
                                    placeholder="e.g. Nguyễn Văn A"
                                    value={formName}
                                    onChange={(e) => setFormName(e.target.value)}
                                    autoFocus
                                />
                            </div>
                            <div className="form-group">
                                <label htmlFor="student-email">Email Address</label>
                                <input
                                    id="student-email"
                                    type="email"
                                    placeholder="e.g. vana@email.com"
                                    value={formEmail}
                                    onChange={(e) => setFormEmail(e.target.value)}
                                />
                            </div>
                        </div>

                        <div className="modal-footer">
                            <button className="btn btn-ghost" onClick={() => setShowForm(false)}>
                                Cancel
                            </button>
                            <button
                                className="btn btn-primary"
                                onClick={saveStudent}
                                disabled={!formName.trim() || !formEmail.trim()}
                            >
                                {editingStudent ? "Save Changes" : "Add Student"}
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}
