import { Button } from "./Button";

type Props = {
  onAdd?: () => void;
  onImport?: () => void;
  onExport?: () => void;
  title?: string;
  description?: string;
  addButtonLabel?: string;
};

export function CrudActionBar({ 
  onAdd, 
  onImport, 
  onExport,
  title = "Task Actions",
  description = "Manage your daily tasks and import/export data.",
  addButtonLabel = "Add Task"
}: Props) {
  return (
    <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 bg-brand-card border border-brand-border p-4 rounded-2xl backdrop-blur-md shadow-xl animate-fade-in mb-6">
      <div>
        <h2 className="text-sm font-semibold text-brand-text">{title}</h2>
        <p className="text-xs text-brand-text-muted mt-0.5">{description}</p>
      </div>
      
      <div className="flex flex-wrap items-center gap-2.5">
        {onAdd && (
          <Button
            title={addButtonLabel}
            action={onAdd}
            style="bg-indigo-600 hover:bg-indigo-500 text-white shadow-lg shadow-indigo-600/25 border border-indigo-500/20"
          >
            <svg className="w-4.5 h-4.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2.5" d="M12 4v16m8-8H4" />
            </svg>
          </Button>
        )}
        
        {onImport && (
          <Button
            title="Import CSV"
            action={onImport}
            style="bg-slate-800/80 hover:bg-slate-700/80 border border-brand-border text-brand-text hover:border-brand-border-hover shadow-md"
          >
            <svg className="w-4.5 h-4.5 text-emerald-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M4 16v1a3 3 0 003 3h10a3 3 0 003-3v-1m-4-8l-4-4m0 0L8 8m4-4v12" />
            </svg>
          </Button>
        )}
        
        {onExport && (
          <Button
            title="Export CSV"
            action={onExport}
            style="bg-slate-800/80 hover:bg-slate-700/80 border border-brand-border text-brand-text hover:border-brand-border-hover shadow-md"
          >
            <svg className="w-4.5 h-4.5 text-amber-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M4 16v1a3 3 0 003 3h10a3 3 0 003-3v-1m-4-4l-4 4m0 0l-4-4m4 4V4" />
            </svg>
          </Button>
        )}
      </div>
    </div>
  );
}