import { useState } from "react";
import { CrudTable } from "../components/CrudTable";
import { CrudActionBar } from "../components/CrudActionBar";
import { ConfirmModal } from "../components/ConfirmModal";
import { ImportModal } from "../components/ImportModal";
import { CrudFormModal, type FieldConfig } from "../components/CrudFormModal";
import { ViewModal } from "../components/ViewModal";

type Teacher = { id: string; name: string; department: string; courses: number };

export default function TeacherPage() {
  const [teachers, setTeachers] = useState<Teacher[]>([
    { id: "T-01", name: "Dr. Smith", department: "Computer Science", courses: 3 },
    { id: "T-02", name: "Prof. Johnson", department: "Mathematics", courses: 4 },
  ]);

  const [isAddOpen, setIsAddOpen] = useState(false);
  const [isImportOpen, setIsImportOpen] = useState(false);
  const [viewingTeacher, setViewingTeacher] = useState<Teacher | null>(null);
  const [editingTeacher, setEditingTeacher] = useState<Teacher | null>(null);
  const [deletingTeacher, setDeletingTeacher] = useState<Teacher | null>(null);

  const fields: FieldConfig[] = [
    { name: "id", label: "Teacher ID", type: "text" },
    { name: "name", label: "Full Name", type: "text" },
    { name: "subject", label: "Subject", type: "text" },
    { name: "experience", label: "Experience", type: "text" }
  ];

  const handleExport = () => {
    const csvContent = "data:text/csv;charset=utf-8," 
      + ["id", "name", "subject", "experience"].join(",") + "\\n"
      + teachers.map(t => `${t.id},"${t.name}",${t.subject},${t.experience}`).join("\\n");
    const link = document.createElement("a");
    link.setAttribute("href", encodeURI(csvContent));
    link.setAttribute("download", "teachers_export.csv");
    link.click();
  };

  const handleSaveAdd = (data: any) => {
    setTeachers([...teachers, data as Teacher]);
    setIsAddOpen(false);
  };

  const handleSaveEdit = (data: any) => {
    setTeachers(teachers.map(t => t.id === data.id ? data : t));
    setEditingTeacher(null);
  };

  const handleDelete = () => {
    if (!deletingTeacher) return;
    setTeachers(teachers.filter(t => t.id !== deletingTeacher.id));
    setDeletingTeacher(null);
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
          data={teachers}
          onView={(t) => setViewingTeacher(t as Teacher)}
          onEdit={(t) => setEditingTeacher(t as Teacher)}
          onDelete={(t) => setDeletingTeacher(t as Teacher)}
        />
      </div>

      <ViewModal isOpen={!!viewingTeacher} onClose={() => setViewingTeacher(null)} title="Teacher Details" data={viewingTeacher} />
      <CrudFormModal isOpen={isAddOpen} onClose={() => setIsAddOpen(false)} onSave={handleSaveAdd} title="Add New Teacher" fields={fields} />
      <CrudFormModal isOpen={!!editingTeacher} onClose={() => setEditingTeacher(null)} onSave={handleSaveEdit} title="Edit Teacher" fields={fields} initialData={editingTeacher} />
      <ImportModal isOpen={isImportOpen} onClose={() => setIsImportOpen(false)} onImport={() => alert("File imported!")} />
      <ConfirmModal isOpen={!!deletingTeacher} onClose={() => setDeletingTeacher(null)} onConfirm={handleDelete} title="Delete Teacher" message={`Are you sure you want to delete teacher "${deletingTeacher?.name}"?`} />
    </div>
  );
}
