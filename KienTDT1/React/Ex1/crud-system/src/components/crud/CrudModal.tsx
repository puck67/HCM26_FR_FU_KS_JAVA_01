import React, { useState, useEffect } from "react";

type CrudModalProps = {
  isOpen: boolean;
  onClose: () => void;
  onSubmit: (data: { name: string; status: string }) => void;
  title: string;
  initialValues?: { name: string; status: string };
  submitLabel?: string;
};

export function CrudModal({
  isOpen,
  onClose,
  onSubmit,
  title,
  initialValues,
  submitLabel = "Save"
}: CrudModalProps) {
  const [name, setName] = useState("");
  const [status, setStatus] = useState("Doing");

  useEffect(() => {
    if (isOpen) {
      if (initialValues) {
        setName(initialValues.name);
        setStatus(initialValues.status);
      } else {
        setName("");
        setStatus("Doing");
      }
    }
  }, [initialValues, isOpen]);

  if (!isOpen) return null;

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!name.trim()) return;
    onSubmit({ name: name.trim(), status });
    onClose();
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4">
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
              Task Name
            </label>
            <input
              type="text"
              required
              placeholder="e.g., Learn React state management"
              value={name}
              onChange={(e) => setName(e.target.value)}
              className="w-full bg-slate-950 border border-brand-border rounded-xl px-4 py-2.5 text-sm text-brand-text placeholder-brand-text-muted focus:border-brand-accent focus:ring-1 focus:ring-brand-accent focus:outline-none transition"
              autoFocus
            />
          </div>

          <div>
            <label className="block text-xs font-semibold text-brand-text-muted uppercase tracking-wider mb-2">
              Status
            </label>
            <select
              value={status}
              onChange={(e) => setStatus(e.target.value)}
              className="w-full bg-slate-950 border border-brand-border rounded-xl px-4 py-2.5 text-sm text-brand-text focus:border-brand-accent focus:outline-none transition"
            >
              <option value="Doing">Doing</option>
              <option value="Done">Done</option>
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

type ConfirmModalProps = {
  isOpen: boolean;
  onClose: () => void;
  onConfirm: () => void;
  title: string;
  message: string;
  confirmLabel?: string;
  type?: "danger" | "info";
};

export function ConfirmModal({
  isOpen,
  onClose,
  onConfirm,
  title,
  message,
  confirmLabel = "Confirm",
  type = "info"
}: ConfirmModalProps) {
  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4">
      {/* Backdrop */}
      <div 
        className="fixed inset-0 bg-slate-950/80 backdrop-blur-sm transition-opacity" 
        onClick={onClose}
      />
      
      {/* Modal Content */}
      <div className="relative bg-slate-900 border border-brand-border rounded-2xl w-full max-w-md p-6 shadow-2xl animate-scale-in z-10">
        <div className="flex items-start gap-4">
          <div className={`p-3 rounded-xl border shrink-0 ${
            type === "danger" 
              ? "bg-rose-500/10 border-rose-500/20 text-rose-400" 
              : "bg-indigo-500/10 border-indigo-500/20 text-indigo-400"
          }`}>
            {type === "danger" ? (
              <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2.5" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
              </svg>
            ) : (
              <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2.5" d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
            )}
          </div>

          <div className="flex-1">
            <h3 className="text-lg font-bold text-brand-text">{title}</h3>
            <p className="text-sm text-brand-text-muted mt-2 leading-relaxed">
              {message}
            </p>
          </div>
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
            type="button"
            onClick={() => {
              onConfirm();
              onClose();
            }}
            className={`px-4 py-2 rounded-xl text-sm font-semibold text-white shadow-lg transition cursor-pointer ${
              type === "danger" 
                ? "bg-rose-600 hover:bg-rose-500 shadow-rose-600/25 border border-rose-500/20" 
                : "bg-indigo-600 hover:bg-indigo-500 shadow-indigo-600/25 border border-indigo-500/20"
            }`}
          >
            {confirmLabel}
          </button>
        </div>
      </div>
    </div>
  );
}
