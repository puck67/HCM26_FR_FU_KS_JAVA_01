import React, { useState, useEffect } from "react";

type StudentModalProps = {
  isOpen: boolean;
  onClose: () => void;
  onSubmit: (data: { name: string; age: number; class: string }) => void;
  title: string;
  initialValues?: { name: string; age: number; class: string };
  submitLabel?: string;
};

export function StudentModal({
  isOpen,
  onClose,
  onSubmit,
  title,
  initialValues,
  submitLabel = "Save"
}: StudentModalProps) {
  const [name, setName] = useState("");
  const [age, setAge] = useState(20);
  const [studentClass, setStudentClass] = useState("IT");

  useEffect(() => {
    if (isOpen) {
      if (initialValues) {
        setName(initialValues.name);
        setAge(initialValues.age);
        setStudentClass(initialValues.class);
      } else {
        setName("");
        setAge(20);
        setStudentClass("IT");
      }
    }
  }, [initialValues, isOpen]);

  if (!isOpen) return null;

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!name.trim()) return;
    onSubmit({ name: name.trim(), age: Number(age) || 20, class: studentClass });
    onClose();
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 animate-fade-in">
      {/* Backdrop */}
      <div 
        className="fixed inset-0 bg-slate-950/80 backdrop-blur-sm transition-opacity" 
        onClick={onClose}
      />
      
      {/* Modal Content */}
      <div className="relative bg-slate-900 border border-brand-border rounded-2xl w-full max-w-md p-6 shadow-2xl animate-scale-in z-10">
        <div className="flex items-center justify-between border-b border-brand-border pb-4 mb-4">
          <h3 className="text-lg font-bold text-brand-text">{title}</h3>
          <button 
            onClick={onClose}
            className="text-brand-text-muted hover:text-brand-text p-1 rounded-lg hover:bg-slate-800 transition"
          >
            <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        </div>

        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block text-xs font-semibold text-brand-text-muted uppercase tracking-wider mb-2">
              Student Name
            </label>
            <input
              type="text"
              required
              placeholder="e.g., Nguyen Van A"
              value={name}
              onChange={(e) => setName(e.target.value)}
              className="w-full bg-slate-950 border border-brand-border rounded-xl px-4 py-2.5 text-sm text-brand-text placeholder-brand-text-muted focus:border-brand-accent focus:ring-1 focus:ring-brand-accent focus:outline-none transition"
              autoFocus
            />
          </div>

          <div>
            <label className="block text-xs font-semibold text-brand-text-muted uppercase tracking-wider mb-2">
              Age
            </label>
            <input
              type="number"
              required
              min={15}
              max={100}
              placeholder="20"
              value={age}
              onChange={(e) => setAge(Number(e.target.value))}
              className="w-full bg-slate-950 border border-brand-border rounded-xl px-4 py-2.5 text-sm text-brand-text placeholder-brand-text-muted focus:border-brand-accent focus:ring-1 focus:ring-brand-accent focus:outline-none transition"
            />
          </div>

          <div>
            <label className="block text-xs font-semibold text-brand-text-muted uppercase tracking-wider mb-2">
              Class
            </label>
            <select
              value={studentClass}
              onChange={(e) => setStudentClass(e.target.value)}
              className="w-full bg-slate-950 border border-brand-border rounded-xl px-4 py-2.5 text-sm text-brand-text focus:border-brand-accent focus:outline-none transition"
            >
              <option value="IT">IT</option>
              <option value="SE">SE</option>
              <option value="CS">CS</option>
              <option value="IS">IS</option>
            </select>
          </div>

          <div className="flex justify-end gap-3 pt-4 border-t border-brand-border mt-6">
            <button
              type="button"
              onClick={onClose}
              className="px-4 py-2 rounded-xl text-sm font-semibold border border-brand-border hover:bg-slate-800 text-brand-text transition"
            >
              Cancel
            </button>
            <button
              type="submit"
              disabled={!name.trim()}
              className="px-4 py-2 rounded-xl text-sm font-semibold bg-indigo-600 hover:bg-indigo-500 disabled:opacity-50 text-white shadow-lg shadow-indigo-600/25 transition cursor-pointer"
            >
              {submitLabel}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
