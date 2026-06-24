import { ButtonList } from "./ButtonList";
import type { ButtonConfig } from "./types";

type CrudTableProps<T extends object> = {
  data: T[];
  onEdit?: (item: T) => void;
  onDelete?: (item: T) => void;
  extraActions?: (item: T) => ButtonConfig[];
};

export function CrudTable<T extends object>({
  data,
  onEdit,
  onDelete,
  extraActions,
}: CrudTableProps<T>) {
  if (!data || data.length === 0) {
    return (
      <div className="flex flex-col items-center justify-center p-12 text-center bg-brand-card border border-brand-border rounded-2xl backdrop-blur-md shadow-xl animate-fade-in">
        <div className="w-16 h-16 bg-slate-800/80 border border-brand-border rounded-2xl flex items-center justify-center mb-4 text-brand-text shadow-inner">
          <svg className="w-8 h-8 text-brand-text-muted" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="1.5" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2m-6 9l2 2 4-4" />
          </svg>
        </div>
        <h3 className="text-base font-bold text-brand-text">No tasks available</h3>
        <p className="text-xs text-brand-text-muted mt-1 max-w-xs">
          Get started by adding a new task using the "Add Task" button or import from a CSV file.
        </p>
      </div>
    );
  }

  const columns = Object.keys(data[0]);

  // Helper to format header text
  const formatHeader = (col: string) => {
    if (col.toLowerCase() === "id") return "ID";
    return col.charAt(0).toUpperCase() + col.slice(1);
  };

  // Helper to render cellular values nicely (e.g. status badges)
  const renderCellContent = (col: string, val: unknown) => {
    const stringVal = String(val);
    
    if (col.toLowerCase() === "status") {
      if (stringVal.toLowerCase() === "done") {
        return (
          <span className="px-2.5 py-1 text-xs font-semibold rounded-full bg-emerald-500/10 text-emerald-400 border border-emerald-500/20 inline-flex items-center gap-1">
            <span className="w-1.5 h-1.5 rounded-full bg-emerald-400 animate-pulse"></span>
            Done
          </span>
        );
      }
      return (
        <span className="px-2.5 py-1 text-xs font-semibold rounded-full bg-amber-500/10 text-amber-400 border border-amber-500/20 inline-flex items-center gap-1">
          <span className="w-1.5 h-1.5 rounded-full bg-amber-400"></span>
          Doing
        </span>
      );
    }

    if (col.toLowerCase() === "id") {
      return <span className="font-mono text-xs text-brand-text-muted bg-slate-800/60 px-1.5 py-0.5 rounded border border-brand-border">#{stringVal}</span>;
    }

    if (col.toLowerCase() === "class") {
      const upperClass = stringVal.toUpperCase();
      if (upperClass === "IT") {
        return (
          <span className="px-2.5 py-1 text-xs font-semibold rounded-full bg-purple-500/10 text-purple-400 border border-purple-500/20 inline-flex items-center">
            IT
          </span>
        );
      }
      if (upperClass === "SE") {
        return (
          <span className="px-2.5 py-1 text-xs font-semibold rounded-full bg-cyan-500/10 text-cyan-400 border border-cyan-500/20 inline-flex items-center">
            SE
          </span>
        );
      }
      return (
        <span className="px-2.5 py-1 text-xs font-semibold rounded-full bg-slate-500/10 text-slate-400 border border-slate-500/20 inline-flex items-center">
          {stringVal}
        </span>
      );
    }

    return <span className="text-brand-text font-medium">{stringVal}</span>;
  };

  return (
    <div className="overflow-x-auto rounded-2xl border border-brand-border bg-brand-card shadow-2xl backdrop-blur-md animate-fade-in">
      <table className="w-full border-collapse text-left">
        <thead>
          <tr className="bg-slate-900/60 border-b border-brand-border">
            {columns.map((col) => (
              <th
                className="px-6 py-4 text-xs font-bold uppercase tracking-wider text-brand-text-muted"
                key={col}
              >
                {formatHeader(col)}
              </th>
            ))}
            <th className="px-6 py-4 text-xs font-bold uppercase tracking-wider text-brand-text-muted text-right">
              Actions
            </th>
          </tr>
        </thead>
        
        <tbody className="divide-y divide-brand-border/40">
          {data.map((item, index) => {
            const actions: ButtonConfig[] = [
              ...(onEdit
                ? [
                    {
                      title: "Edit",
                      action: () => onEdit(item),
                      style: "bg-slate-800 hover:bg-slate-700 border border-brand-border hover:border-brand-border-hover text-brand-text",
                    },
                  ]
                : []),
              ...(onDelete
                ? [
                    {
                      title: "Delete",
                      action: () => onDelete(item),
                      style: "bg-rose-500/10 hover:bg-rose-500/20 border border-rose-500/20 text-rose-400 hover:text-rose-300",
                    },
                  ]
                : []),
              ...(extraActions ? extraActions(item) : []),
            ];

            return (
              <tr 
                key={index}
                className="hover:bg-brand-card-hover/40 transition-colors duration-150"
              >
                {columns.map((col) => (
                  <td className="px-6 py-4.5 whitespace-nowrap align-middle" key={col}>
                    {renderCellContent(col, (item as Record<string, unknown>)[col])}
                  </td>
                ))}
                
                <td className="px-6 py-4.5 whitespace-nowrap text-right align-middle">
                  <div className="inline-flex justify-end">
                    <ButtonList items={actions} />
                  </div>
                </td>
              </tr>
            );
          })}
        </tbody>
      </table>
    </div>
  );
}