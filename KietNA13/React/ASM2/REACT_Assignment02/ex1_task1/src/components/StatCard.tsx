interface StatCardProps {
  label: string;
  value: number;
  colorClass: string;
  icon: string;
}

export default function StatCard({ label, value, colorClass, icon }: StatCardProps) {
  return (
    <div
      className={`relative overflow-hidden bg-gradient-to-br ${colorClass} border rounded-2xl p-6 flex items-center gap-4`}
    >
      <span className="text-3xl select-none">{icon}</span>
      <div>
        <p className="text-3xl font-extrabold text-white">{value}</p>
        <p className="text-sm text-slate-400 mt-0.5">{label}</p>
      </div>
    </div>
  );
}
