import { CheckCircle, Clock, AlertTriangle, Box, TrendingUp, AlertCircle as AlertCircleIcon, Users, UserCheck, ShieldAlert } from "lucide-react";
import { StatCard } from "../components/StatCard";

export default function DashboardPage() {
  return (
    <div className="w-full max-w-[1600px] mx-auto animate-fadeIn flex flex-col gap-8">
      
      {/* Tasks Overview */}
      <section>
        <h2 className="text-[1.25rem] font-bold text-slate-800 mb-4 tracking-tight">Tasks Overview</h2>
        <div className="grid grid-cols-1 md:grid-cols-3 gap-5">
          <StatCard title="Total Tasks" value={142} icon={<Clock size={24} />} trend="+8%" trendUp={true} colorClass="blue" />
          <StatCard title="Completed Tasks" value={89} icon={<CheckCircle size={24} />} trend="+15%" trendUp={true} colorClass="emerald" />
          <StatCard title="High Priority" value={12} icon={<AlertTriangle size={24} />} trend="-5%" trendUp={false} colorClass="amber" />
        </div>
      </section>

      {/* Products & Inventory */}
      <section>
        <h2 className="text-[1.25rem] font-bold text-slate-800 mb-4 tracking-tight">Inventory Status</h2>
        <div className="grid grid-cols-1 md:grid-cols-3 gap-5">
          <StatCard title="Total Products" value={1248} icon={<Box size={24} />} trend="+12%" trendUp={true} colorClass="blue" />
          <StatCard title="Total Stock Items" value={15200} icon={<TrendingUp size={24} />} trend="+5%" trendUp={true} colorClass="emerald" />
          <StatCard title="Low Stock Alerts" value={23} icon={<AlertCircleIcon size={24} />} trend="-2%" trendUp={false} colorClass="rose" />
        </div>
      </section>

      {/* Users & Access */}
      <section>
        <h2 className="text-[1.25rem] font-bold text-slate-800 mb-4 tracking-tight">System Users</h2>
        <div className="grid grid-cols-1 md:grid-cols-3 gap-5">
          <StatCard title="Total Users" value={284} icon={<Users size={24} />} trend="+12%" trendUp={true} colorClass="blue" />
          <StatCard title="Active Users" value={256} icon={<UserCheck size={24} />} trend="+4%" trendUp={true} colorClass="emerald" />
          <StatCard title="Administrators" value={12} icon={<ShieldAlert size={24} />} trend="0%" trendUp={true} colorClass="purple" />
        </div>
      </section>

    </div>
  );
}
