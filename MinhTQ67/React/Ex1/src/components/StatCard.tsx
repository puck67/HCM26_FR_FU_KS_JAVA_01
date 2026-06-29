import { ReactNode } from "react";

export function StatCard({ title, value, icon, trend, trendUp, colorClass = "blue" }: {
  title: string;
  value: string | number;
  icon: ReactNode;
  trend?: string;
  trendUp?: boolean;
  colorClass?: string;
}) {
  const colorMap: Record<string, string> = {
    blue: "bg-blue-50 text-blue-600 group-hover:bg-blue-600 group-hover:text-white",
    emerald: "bg-emerald-50 text-emerald-600 group-hover:bg-emerald-600 group-hover:text-white",
    purple: "bg-purple-50 text-purple-600 group-hover:bg-purple-600 group-hover:text-white",
    amber: "bg-amber-50 text-amber-600 group-hover:bg-amber-600 group-hover:text-white",
    rose: "bg-rose-50 text-rose-600 group-hover:bg-rose-600 group-hover:text-white",
  };

  return (
    <div className="bg-white border border-slate-200 rounded-2xl p-6 shadow-sm flex flex-col gap-4 relative overflow-hidden group hover:shadow-[0_8px_30px_rgb(0,0,0,0.04)] hover:-translate-y-1 hover:border-slate-300 transition-all duration-300">
      <div className="flex justify-between items-start">
        <div className="flex flex-col gap-1">
          <span className="text-slate-500 font-medium text-[0.95rem]">{title}</span>
          <span className="text-[2rem] font-bold text-slate-900 tracking-tight leading-none">{value}</span>
        </div>
        <div className={`w-12 h-12 rounded-xl flex items-center justify-center transition-colors shadow-sm ${colorMap[colorClass] || colorMap.blue}`}>
          {icon}
        </div>
      </div>
      {trend && (
        <div className="flex items-center gap-2 text-[0.85rem] font-medium mt-1">
          <span className={trendUp ? "text-emerald-600 bg-emerald-50 px-2 py-0.5 rounded-md" : "text-rose-600 bg-rose-50 px-2 py-0.5 rounded-md"}>
            {trend}
          </span>
          <span className="text-slate-400">vs last month</span>
        </div>
      )}
    </div>
  );
}
