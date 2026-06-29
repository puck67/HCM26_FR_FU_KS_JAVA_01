import { useState } from "react";
import { CrudActionBar } from "@/components/CrudActionBar";
import { CrudTable } from "@/components/CrudTable";
import type { Student } from "@/types/Student";

const INITIAL_STUDENTS: Student[] = [
    { id: 1, name: "Lê Hoàng Minh", email: "minhlh@email.com" },
    { id: 2, name: "Phạm Mai Phương", email: "phuongpm@email.com" },
    { id: 3, name: "Vũ Đức Thắng", email: "thangvd@email.com" },
    { id: 4, name: "Đỗ Hải Yến", email: "yendh@email.com" },
    { id: 5, name: "Trịnh Văn Long", email: "longtv@email.com" },
];

export function StudentManagement() {
    const [students, setStudents] = useState<Student[]>(INITIAL_STUDENTS);
    const [showForm, setShowForm] = useState(false);
    const [editingStudent, setEditingStudent] = useState<Student | null>(null);
    const [searchQuery, setSearchQuery] = useState("");

    const [formData, setFormData] = useState({
        name: "",
        email: ""
    });

    const filteredStudents = students.filter(
        (s) =>
            s.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
            s.email.toLowerCase().includes(searchQuery.toLowerCase()) ||
            String(s.id).includes(searchQuery)
    );

    const handleOpenAdd = () => {
        setEditingStudent(null);
        setFormData({ name: "", email: "" });
        setShowForm(true);
    };

    const handleOpenEdit = (student: Student) => {
        setEditingStudent(student);
        setFormData({ name: student.name, email: student.email });
        setShowForm(true);
    };

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setFormData((prev) => ({
            ...prev,
            [name]: value
        }));
    };

    const handleSave = () => {
        const { name, email } = formData;
        if (!name.trim() || !email.trim()) return;

        if (editingStudent) {
            setStudents((prev) =>
                prev.map((s) =>
                    s.id === editingStudent.id
                        ? { ...s, name: name.trim(), email: email.trim() }
                        : s
                )
            );
        } else {
            const newId = students.length > 0 ? Math.max(...students.map((s) => s.id)) + 1 : 1;
            setStudents((prev) => [
                ...prev,
                { id: newId, name: name.trim(), email: email.trim() },
            ]);
        }

        setShowForm(false);
        setEditingStudent(null);
    };

    const handleDelete = (id: number) => {
        if (confirm("Do you want to delete this student?")) {
            setStudents((prev) => prev.filter((s) => s.id !== id));
        }
    };

    const handleImport = () => {
        const sampleImport: Student[] = [
            { id: Date.now(), name: "Import Student 1", email: "import1@email.com" },
            { id: Date.now() + 1, name: "Import Student 2", email: "import2@email.com" },
        ];
        setStudents((prev) => [...prev, ...sampleImport]);
    };

    const handleExport = () => {
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
            <header className="page-header">
                <div className="header-left">
                    <div className="header-icon"></div>
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

            <div className="toolbar">
                <div className="search-box">
                    <span className="search-icon"></span>
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
                    onAdd={handleOpenAdd}
                    onImport={handleImport}
                    onExport={handleExport}
                />
            </div>

            {searchQuery && (
                <div className="search-info">
                    Showing <strong>{filteredStudents.length}</strong> of <strong>{students.length}</strong> students
                    for "<strong>{searchQuery}</strong>"
                </div>
            )}

            <CrudTable
                data={filteredStudents}
                onEdit={handleOpenEdit}
                onDelete={(s) => handleDelete(s.id)}
            />

            {showForm && (
                <div className="modal-overlay" onClick={() => setShowForm(false)}>
                    <div className="modal" onClick={(e) => e.stopPropagation()}>
                        <div className="modal-header">
                            <div className="modal-title-group">
                                <span className="modal-icon"></span>
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
                                    name="name"
                                    type="text"
                                    placeholder="e.g. Nguyễn Văn A"
                                    value={formData.name}
                                    onChange={handleChange}
                                    autoFocus
                                />
                            </div>
                            <div className="form-group">
                                <label htmlFor="student-email">Email Address</label>
                                <input
                                    id="student-email"
                                    name="email"
                                    type="email"
                                    placeholder="e.g. vana@email.com"
                                    value={formData.email}
                                    onChange={handleChange}
                                />
                            </div>
                        </div>

                        <div className="modal-footer">
                            <button className="btn btn-ghost" onClick={() => setShowForm(false)}>
                                Cancel
                            </button>
                            <button
                                className="btn btn-primary"
                                onClick={handleSave}
                                disabled={!formData.name.trim() || !formData.email.trim()}
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

