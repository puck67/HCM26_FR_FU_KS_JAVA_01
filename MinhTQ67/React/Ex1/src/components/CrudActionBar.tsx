import { Plus, Download, Upload, Search } from "lucide-react";
import { ButtonList, type ButtonConfig } from "./ButtonList";

export function CrudActionBar({
  onAdd,
  onImport,
  onExport
}: {
  onAdd?: () => void;
  onImport?: () => void;
  onExport?: () => void;
}) {
  const buttons: ButtonConfig[] = [
    ...(onImport ? [{ 
      title: <span className="flex items-center gap-2"><Upload size={16}/> Import</span>, 
      action: onImport, 
      style: "px-5 py-2.5 text-[0.95rem] bg-slate-100 text-slate-700 rounded-xl hover:bg-slate-200 transition-colors font-medium" 
    }] : []),
    ...(onExport ? [{ 
      title: <span className="flex items-center gap-2"><Download size={16}/> Export</span>, 
      action: onExport, 
      style: "px-5 py-2.5 text-[0.95rem] bg-slate-100 text-slate-700 rounded-xl hover:bg-slate-200 transition-colors font-medium" 
    }] : []),
    ...(onAdd ? [{ 
      title: <span className="flex items-center justify-center gap-2"><Plus size={18}/> Add</span>, 
      action: onAdd, 
      style: "px-10 py-2.5 text-[0.95rem] bg-white text-emerald-600 border border-slate-100 rounded-xl shadow-[0_2px_12px_rgba(0,0,0,0.06)] hover:bg-emerald-500 hover:text-white hover:border-emerald-500 hover:shadow-[0_6px_20px_rgba(16,185,129,0.35)] hover:-translate-y-[1px] transition-all duration-300 font-semibold ml-2" 
    }] : []),
  ];

  return (
    <div className="flex items-center justify-between mb-2">
      <div className="relative">
        <Search size={18} className="absolute left-4 top-1/2 -translate-y-1/2 text-slate-400" />
        <input 
          type="text" 
          placeholder="Search records..." 
          className="pl-11 pr-5 py-2.5 rounded-xl border border-slate-200 bg-white shadow-[0_2px_8px_rgba(0,0,0,0.04)] outline-none w-[320px] transition-all focus:border-blue-500 focus:shadow-[0_0_0_4px_rgba(59,130,246,0.1)] text-[0.95rem] font-medium"
        />
      </div>
      <ButtonList items={buttons} />
    </div>
  );
}
