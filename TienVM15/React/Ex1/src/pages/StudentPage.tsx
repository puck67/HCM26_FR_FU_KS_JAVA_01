import { useState } from "react";
import { CrudActionBar, CrudTable } from "../components";
import type { ColumnDef } from "../components";

interface Student {
  id: number;
  name: string;
  email: string;
  grade: string;
  gpa: number;
  enrolled: boolean;
}

const seed: Student[] = [
  { id: 1, name: "Alice Johnson", email: "alice@school.edu", grade: "A", gpa: 3.9, enrolled: true },
  { id: 2, name: "Bob Smith", email: "bob@school.edu", grade: "B", gpa: 3.2, enrolled: true },
  { id: 3, name: "Carol White", email: "carol@school.edu", grade: "A+", gpa: 4.0, enrolled: false },
  { id: 4, name: "David Lee", email: "david@school.edu", grade: "C", gpa: 2.5, enrolled: true },
  { id: 5, name: "Eva Green", email: "eva@school.edu", grade: "B+", gpa: 3.5, enrolled: true },
];

const gradeColors: Record<string, string> = {
  "A+": "badge--success",
  "A": "badge--success",
  "B+": "badge--info",
  "B": "badge--info",
  "C": "badge--warning",
  "D": "badge--danger",
  "F": "badge--danger",
};

const columns: ColumnDef<Student>[] = [
  { key: "id", label: "ID", width: "60px" },
  { key: "name", label: "Full Name" },
  { key: "email", label: "Email" },
  {
    key: "grade",
    label: "Grade",
    render: (val) => (
      <span className={`badge ${gradeColors[String(val)] ?? "badge--info"}`}>
        {String(val)}
      </span>
    ),
  },
  {
    key: "gpa",
    label: "GPA",
    render: (val) => <strong>{Number(val).toFixed(1)}</strong>,
  },
  {
    key: "enrolled",
    label: "Status",
    render: (val) => (
      <span className={`badge ${val ? "badge--success" : "badge--danger"}`}>
        {val ? "Enrolled" : "Inactive"}
      </span>
    ),
  },
];

export default function StudentPage() {
  const [students, setStudents] = useState<Student[]>(seed);

  const handleDelete = (s: Student) => {
    if (confirm(`Remove student "${s.name}"?`)) {
      setStudents((prev) => prev.filter((x) => x.id !== s.id));
    }
  };

  return (
    <div className="page">
      <CrudActionBar
        title="Student Management"
        onAdd={() => alert("Open Add Student form")}
        onImport={() => alert("Import students from Excel")}
        onExport={() => alert("Export students")}
      />

      <CrudTable<Student>
        data={students}
        columns={columns}
        onEdit={(s) => alert(`Edit: ${s.name}`)}
        onDelete={handleDelete}
        showIndex
        keyExtractor={(s) => s.id}
        emptyMessage="No students found."
        pagination={{ pageSize: 3 }}
        searchable={{
          placeholder: "Search students by name or email...",
          keys: ["name", "email"],
        }}
      />
    </div>
  );
}
