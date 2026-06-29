import { useState } from "react";
import { CrudTable } from "../components/CrudTable";
import { CrudActionBar } from "../components/CrudActionBar";
import { ConfirmModal } from "../components/ConfirmModal";
import { ImportModal } from "../components/ImportModal";
import { CrudFormModal, type FieldConfig } from "../components/CrudFormModal";
import { ViewModal } from "../components/ViewModal";

type Student = { id: string; name: string; grade: string; gpa: number };

export default function StudentPage() {
  const [students, setStudents] = useState<Student[]>([
    { id: "S01", name: "Alice Nguyen", grade: "10A", gpa: 3.8 },
    { id: "S02", name: "Bob Tran", grade: "11B", gpa: 3.5 },
    { id: "S03", name: "Charlie Le", grade: "12C", gpa: 3.9 },
    { id: "S04", name: "David Pham", grade: "10A", gpa: 3.2 },
    { id: "S05", name: "Eve Hoang", grade: "11B", gpa: 4.0 },
    { id: "S06", name: "Frank Vu", grade: "12C", gpa: 3.6 },
  ]);

  const [isAddOpen, setIsAddOpen] = useState(false);
  const [isImportOpen, setIsImportOpen] = useState(false);
  const [viewingStudent, setViewingStudent] = useState<Student | null>(null);
  const [editingStudent, setEditingStudent] = useState<Student | null>(null);
  const [deletingStudent, setDeletingStudent] = useState<Student | null>(null);

  const fields: FieldConfig[] = [
    { name: "id", label: "Student ID", type: "text" },
    { name: "name", label: "Full Name", type: "text" },
    { name: "grade", label: "Class/Grade", type: "text" },
    { name: "gpa", label: "GPA", type: "number" }
  ];

  const handleExport = () => {
    const csvContent = "data:text/csv;charset=utf-8," 
      + ["id", "name", "grade", "gpa"].join(",") + "\\n"
      + students.map(s => `${s.id},"${s.name}",${s.grade},${s.gpa}`).join("\\n");
    const link = document.createElement("a");
    link.setAttribute("href", encodeURI(csvContent));
    link.setAttribute("download", "students_export.csv");
    link.click();
  };

  const handleSaveAdd = (data: any) => {
    setStudents([...students, data as Student]);
    setIsAddOpen(false);
  };

  const handleSaveEdit = (data: any) => {
    setStudents(students.map(s => s.id === data.id ? data : s));
    setEditingStudent(null);
  };

  const handleDelete = () => {
    if (!deletingStudent) return;
    setStudents(students.filter(s => s.id !== deletingStudent.id));
    setDeletingStudent(null);
  };

  return (
    <div className="w-full max-w-[1600px] mx-auto animate-fadeIn flex flex-col gap-5">
      <CrudActionBar
        onAdd={() => setIsAddOpen(true)}
        onImport={() => setIsImportOpen(true)}
        onExport={handleExport}
      />
      
      <div className="bg-white border border-slate-200 rounded-2xl shadow-sm overflow-hidden">
        <CrudTable
          data={students}
          onView={(s) => setViewingStudent(s as Student)}
          onEdit={(s) => setEditingStudent(s as Student)}
          onDelete={(s) => setDeletingStudent(s as Student)}
        />
      </div>

      <ViewModal isOpen={!!viewingStudent} onClose={() => setViewingStudent(null)} title="Student Details" data={viewingStudent} />
      <CrudFormModal isOpen={isAddOpen} onClose={() => setIsAddOpen(false)} onSave={handleSaveAdd} title="Add New Student" fields={fields} />
      <CrudFormModal isOpen={!!editingStudent} onClose={() => setEditingStudent(null)} onSave={handleSaveEdit} title="Edit Student" fields={fields} initialData={editingStudent} />
      <ImportModal isOpen={isImportOpen} onClose={() => setIsImportOpen(false)} onImport={() => alert("File imported!")} />
      <ConfirmModal isOpen={!!deletingStudent} onClose={() => setDeletingStudent(null)} onConfirm={handleDelete} title="Delete Student" message={`Are you sure you want to delete student "${deletingStudent?.name}"?`} />
    </div>
  );
}
