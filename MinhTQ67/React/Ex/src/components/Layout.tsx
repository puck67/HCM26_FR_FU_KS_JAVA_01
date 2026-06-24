import { ReactNode } from "react";
import Sidebar from "./Sidebar";
import Header from "./Header";

export default function Layout({ 
  children, 
  activeTab, 
  setActiveTab 
}: { 
  children: ReactNode;
  activeTab: string;
  setActiveTab: (tab: string) => void;
}) {
  const titles: Record<string, string> = {
    dashboard: "System Dashboard",
    tasks: "Task Management",
    students: "Student Management",
    teachers: "Teacher Management",
    products: "Product Management",
    users: "User Management"
  };

  return (
    <div className="flex h-screen bg-slate-50 overflow-hidden font-outfit text-slate-900">
      <Sidebar activeTab={activeTab} setActiveTab={setActiveTab} />
      <div className="flex-1 flex flex-col min-w-0">
        <Header />
        <main className="flex-1 p-8 overflow-y-auto">
          <div className="w-full max-w-[1600px] mx-auto mb-6">
            <h1 className="text-[2.25rem] font-extrabold text-slate-900 tracking-tight">{titles[activeTab] || "Dashboard"}</h1>
          </div>
          {children}
        </main>
      </div>
    </div>
  );
}
