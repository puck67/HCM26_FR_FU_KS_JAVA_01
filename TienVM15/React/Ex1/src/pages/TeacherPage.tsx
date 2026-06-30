import { useState } from "react";
import { CrudActionBar, CrudTable } from "../components";
import type { ColumnDef } from "../components";

interface Teacher {
  id: number;
  name: string;
  department: string;
  subject: string;
  experience: number;
  rating: number;
}

const seed: Teacher[] = [
  { id: 1, name: "Prof. Alan Turing", department: "Computer Science", subject: "Algorithms", experience: 15, rating: 4.9 },
  { id: 2, name: "Dr. Marie Curie", department: "Physics", subject: "Quantum Mechanics", experience: 20, rating: 4.8 },
  { id: 3, name: "Dr. Richard Feynman", department: "Physics", subject: "Electrodynamics", experience: 18, rating: 4.7 },
  { id: 4, name: "Prof. Ada Lovelace", department: "Mathematics", subject: "Discrete Math", experience: 10, rating: 4.5 },
];

const columns: ColumnDef<Teacher>[] = [
  { key: "id", label: "ID", width: "60px" },
  { key: "name", label: "Name" },
  { key: "department", label: "Department" },
  { key: "subject", label: "Subject" },
  {
    key: "experience",
    label: "Experience",
    render: (val) => `${val} yrs`,
  },
  {
    key: "rating",
    label: "Rating",
    render: (val) => {
      const stars = Math.round(Number(val));
      return (
        <span className="star-rating">
          {"★".repeat(stars)}{"☆".repeat(5 - stars)}
          <span className="star-rating__score"> {Number(val).toFixed(1)}</span>
        </span>
      );
    },
  },
];

export default function TeacherPage() {
  const [teachers, setTeachers] = useState<Teacher[]>(seed);

  const handleDelete = (t: Teacher) => {
    if (confirm(`Remove "${t.name}"?`)) {
      setTeachers((prev) => prev.filter((x) => x.id !== t.id));
    }
  };

  return (
    <div className="page">
      <CrudActionBar
        title="Teacher Management"
        onAdd={() => alert("Open Add Teacher form")}
        onExport={() => alert("Export teachers")}
      />

      <CrudTable<Teacher>
        data={teachers}
        columns={columns}
        onEdit={(t) => alert(`Edit: ${t.name}`)}
        onDelete={handleDelete}
        showIndex
        keyExtractor={(t) => t.id}
        emptyMessage="No teachers found."
        searchable={{
          placeholder: "Search teachers by name, subject or department...",
          keys: ["name", "subject", "department"],
        }}
      />
    </div>
  );
}
