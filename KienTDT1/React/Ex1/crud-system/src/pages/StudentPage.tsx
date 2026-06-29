import { useState } from "react";
import { CrudTable } from "../components/crud/CrudTable";
import { CrudActionBar } from "../components/crud/CrudActionBar";
import { StudentModal } from "../components/crud/StudentModal";
import { ConfirmModal } from "../components/crud/CrudModal";

export type Student = {
  id: number;
  name: string;
  age: number;
  class: string;
};

export const students: Student[] = [
  { id: 1, name: "Nam", age: 20, class: "IT" },
  { id: 2, name: "An", age: 21, class: "SE" }
];

type StudentPageProps = {
  students: Student[];
  setStudents: React.Dispatch<React.SetStateAction<Student[]>>;
};

export default function StudentPage({ students: activeStudents, setStudents }: StudentPageProps) {
  // Modal states
  const [isAddStudentOpen, setIsAddStudentOpen] = useState(false);
  const [editingStudent, setEditingStudent] = useState<Student | null>(null);
  const [deletingStudent, setDeletingStudent] = useState<Student | null>(null);

  // CSV states
  const [isImportOpen, setIsImportOpen] = useState(false);
  const [importText, setImportText] = useState("");
  const [exportData, setExportData] = useState<string | null>(null);
  const [copied, setCopied] = useState(false);

  const nextId = () => Math.max(0, ...activeStudents.map((item) => item.id)) + 1;

  // Add Student
  const handleAddStudentSubmit = (values: { name: string; age: number; class: string }) => {
    setStudents([...activeStudents, { id: nextId(), name: values.name, age: values.age, class: values.class }]);
  };

  // Edit Student
  const handleEditStudentSubmit = (values: { name: string; age: number; class: string }) => {
    if (!editingStudent) return;
    setStudents(
      activeStudents.map((stud) =>
        stud.id === editingStudent.id ? { ...stud, name: values.name, age: values.age, class: values.class } : stud
      )
    );
  };

  // Delete Student
  const handleDeleteStudentConfirm = () => {
    if (!deletingStudent) return;
    setStudents(activeStudents.filter((stud) => stud.id !== deletingStudent.id));
  };

  // CSV Import
  const handleImportSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!importText.trim()) return;

    let currentNextId = nextId();
    const imported = importText
      .split(/\r?\n/)
      .map((line) => line.trim())
      .filter(Boolean)
      .map((line) => {
        const [idText, name, ageText, className] = line.split(",");
        const parsedId = Number(idText);
        const finalId = isNaN(parsedId) || parsedId <= 0 ? currentNextId++ : parsedId;
        return {
          id: finalId,
          name: name?.trim() || "Untitled Student",
          age: Number(ageText) || 20,
          class: className?.trim() || "IT",
        } as Student;
      });

    setStudents([...activeStudents, ...imported]);
    setImportText("");
    setIsImportOpen(false);
  };

  // CSV Export
  const handleExport = () => {
    const csv = activeStudents.map((stud) => `${stud.id},${stud.name},${stud.age},${stud.class}`).join("\n");
    setExportData(csv);
  };

  const handleCopyExport = () => {
    if (!exportData) return;
    navigator.clipboard.writeText(exportData);
    setCopied(true);
    setTimeout(() => setCopied(false), 2000);
  };

  return (
    <div className="space-y-6 animate-fade-in">
      {/* Header Title */}
      <div className="border-b border-brand-border pb-5">
        <h2 className="text-2xl md:text-3xl font-extrabold tracking-tight text-brand-text">Students Manager</h2>
        <p className="text-sm text-brand-text-muted mt-1">Manage enrollments, ages, class splits, and import student data.</p>
      </div>

      {/* Action Bar */}
      <CrudActionBar
        title="Student Actions"
        description="Manage student profiles and import/export data."
        addButtonLabel="Add Student"
        onAdd={() => setIsAddStudentOpen(true)}
        onImport={() => {
          setImportText("");
          setIsImportOpen(true);
        }}
        onExport={handleExport}
      />

      {/* Table */}
      <CrudTable
        data={activeStudents}
        onEdit={(item) => setEditingStudent(item)}
        onDelete={(item) => setDeletingStudent(item)}
      />

      {/* Add Student Modal */}
      <StudentModal
        isOpen={isAddStudentOpen}
        onClose={() => setIsAddStudentOpen(false)}
        onSubmit={handleAddStudentSubmit}
        title="Enroll New Student"
        submitLabel="Enroll Student"
      />

      {/* Edit Student Modal */}
      <StudentModal
        isOpen={editingStudent !== null}
        onClose={() => setEditingStudent(null)}
        onSubmit={handleEditStudentSubmit}
        title="Edit Student Profile"
        submitLabel="Update Profile"
        initialValues={editingStudent ? { name: editingStudent.name, age: editingStudent.age, class: editingStudent.class } : undefined}
      />

      {/* Delete Student Confirmation Modal */}
      <ConfirmModal
        isOpen={deletingStudent !== null}
        onClose={() => setDeletingStudent(null)}
        onConfirm={handleDeleteStudentConfirm}
        title="Deregister Student"
        message={deletingStudent ? `Are you sure you want to deregister student '${deletingStudent.name}'? All records will be wiped.` : ""}
        confirmLabel="Deregister Student"
        type="danger"
      />

      {/* Import CSV Modal */}
      {isImportOpen && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4 animate-fade-in">
          <div className="fixed inset-0 bg-slate-950/80 backdrop-blur-sm" onClick={() => setIsImportOpen(false)} />
          <div className="relative bg-slate-900 border border-brand-border rounded-2xl w-full max-w-lg p-6 shadow-2xl animate-scale-in z-10">
            <div className="flex items-center justify-between border-b border-brand-border pb-4 mb-4">
              <h3 className="text-lg font-bold text-brand-text">Import Students from CSV</h3>
              <button 
                onClick={() => setIsImportOpen(false)}
                className="text-brand-text-muted hover:text-brand-text p-1 rounded-lg hover:bg-slate-800 transition"
              >
                <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M6 18L18 6M6 6l12 12" />
                </svg>
              </button>
            </div>
            
            <form onSubmit={handleImportSubmit} className="space-y-4">
              <div>
                <label className="block text-xs font-semibold text-brand-text-muted uppercase tracking-wider mb-2">
                  CSV Content
                </label>
                <p className="text-xs text-brand-text-muted mb-3">
                  Paste comma-separated rows. Format: <code className="text-purple-400 font-mono">id,name,age,class</code> (or omit id to auto-generate).
                </p>
                <textarea
                  required
                  rows={6}
                  placeholder="1,Nguyen Van A,20,IT&#10;2,Tran Thi B,21,SE"
                  value={importText}
                  onChange={(e) => setImportText(e.target.value)}
                  className="w-full bg-slate-950 border border-brand-border rounded-xl px-4 py-3 text-sm text-brand-text placeholder-brand-text-muted focus:border-brand-accent focus:ring-1 focus:ring-brand-accent focus:outline-none transition font-mono"
                />
              </div>

              <div className="flex justify-end gap-3 pt-4 border-t border-brand-border mt-6">
                <button
                  type="button"
                  onClick={() => setIsImportOpen(false)}
                  className="px-4 py-2 rounded-xl text-sm font-semibold border border-brand-border hover:bg-slate-800 text-brand-text transition"
                >
                  Cancel
                </button>
                <button
                  type="submit"
                  disabled={!importText.trim()}
                  className="px-4 py-2 rounded-xl text-sm font-semibold bg-emerald-600 hover:bg-emerald-500 disabled:opacity-50 text-white shadow-lg shadow-emerald-600/25 transition cursor-pointer"
                >
                  Import Data
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* Export CSV Modal */}
      {exportData !== null && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4 animate-fade-in">
          <div className="fixed inset-0 bg-slate-950/80 backdrop-blur-sm" onClick={() => setExportData(null)} />
          <div className="relative bg-slate-900 border border-brand-border rounded-2xl w-full max-w-lg p-6 shadow-2xl animate-scale-in z-10">
            <div className="flex items-center justify-between border-b border-brand-border pb-4 mb-4">
              <h3 className="text-lg font-bold text-brand-text">Export Students CSV</h3>
              <button 
                onClick={() => setExportData(null)}
                className="text-brand-text-muted hover:text-brand-text p-1 rounded-lg hover:bg-slate-800 transition"
              >
                <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M6 18L18 6M6 6l12 12" />
                </svg>
              </button>
            </div>
            
            <div className="space-y-4">
              <div>
                <label className="block text-xs font-semibold text-brand-text-muted uppercase tracking-wider mb-2">
                  CSV Output
                </label>
                <textarea
                  readOnly
                  rows={6}
                  value={exportData}
                  className="w-full bg-slate-950 border border-brand-border rounded-xl px-4 py-3 text-sm text-brand-text focus:outline-none transition font-mono cursor-text"
                  onClick={(e) => (e.target as HTMLTextAreaElement).select()}
                />
              </div>

              <div className="flex justify-end gap-3 pt-4 border-t border-brand-border mt-6">
                <button
                  type="button"
                  onClick={() => setExportData(null)}
                  className="px-4 py-2 rounded-xl text-sm font-semibold border border-brand-border hover:bg-slate-800 text-brand-text transition"
                >
                  Close
                </button>
                <button
                  type="button"
                  onClick={handleCopyExport}
                  className={`px-4 py-2 rounded-xl text-sm font-semibold text-white shadow-lg transition cursor-pointer flex items-center gap-1.5 ${
                    copied 
                      ? "bg-emerald-600 shadow-emerald-600/25" 
                      : "bg-indigo-600 hover:bg-indigo-500 shadow-indigo-600/25"
                  }`}
                >
                  {copied ? (
                    <>
                      <svg className="w-4.5 h-4.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2.5" d="M5 13l4 4L19 7" />
                      </svg>
                      Copied!
                    </>
                  ) : (
                    <>
                      <svg className="w-4.5 h-4.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M8 5H6a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2v-1M8 5a2 2 0 00-2 2h2a2 2 0 002-2M8 5a2 2 0 012-2h2a2 2 0 012 2m-5 4h1a1 1 0 110 2h-1a1 1 0 110-2zm0 4h1a1 1 0 110 2h-1a1 1 0 110-2z" />
                      </svg>
                      Copy to Clipboard
                    </>
                  )}
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}