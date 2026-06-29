import { CheckSquare, GraduationCap, Users, Package, Shield, LayoutDashboard, LogOut, Settings, BarChart3 } from 'lucide-react';

export default function Sidebar({ activeTab, setActiveTab }: {
  activeTab: string;
  setActiveTab: (tab: string) => void;
}) {
  const navItems = [
    { id: 'dashboard', label: 'Dashboard & Stats', icon: BarChart3 },
    { id: 'tasks', label: 'Tasks', icon: CheckSquare },
    { id: 'students', label: 'Students', icon: GraduationCap },
    { id: 'teachers', label: 'Teachers', icon: Users },
    { id: 'products', label: 'Products', icon: Package },
    { id: 'users', label: 'Users', icon: Shield }
  ];

  return (
    <aside className="w-[260px] bg-white border-r border-slate-200 flex flex-col shadow-[4px_0_24px_rgba(0,0,0,0.02)] z-10">
      {/* Brand */}
      <div className="h-[70px] flex items-center px-6 border-b border-slate-100 gap-3">
        <div className="w-8 h-8 bg-gradient-to-br from-blue-500 to-indigo-600 rounded-lg shadow-md shadow-blue-500/20 flex items-center justify-center">
          <LayoutDashboard size={18} className="text-white" />
        </div>
        <span className="text-lg font-bold bg-clip-text text-transparent bg-gradient-to-r from-slate-900 to-slate-600">
          AdminPanel
        </span>
      </div>

      {/* Navigation */}
      <div className="flex-1 overflow-y-auto py-6 px-4 flex flex-col gap-1.5">
        <div className="px-3 mb-2 text-xs font-semibold text-slate-400 uppercase tracking-wider">
          Main Menu
        </div>
        {navItems.map(item => {
          const Icon = item.icon;
          const isActive = activeTab === item.id;
          return (
            <button
              key={item.id}
              className={`flex items-center gap-3 w-full text-left px-3 py-2.5 rounded-xl cursor-pointer text-[0.95rem] font-medium transition-all duration-200 ${
                isActive 
                  ? 'bg-blue-50 text-blue-600 shadow-sm ring-1 ring-blue-500/10' 
                  : 'text-slate-500 hover:bg-slate-50 hover:text-slate-900'
              }`}
              onClick={() => setActiveTab(item.id)}
            >
              <Icon size={20} className={isActive ? 'text-blue-600' : 'text-slate-400'} />
              {item.label}
            </button>
          );
        })}
      </div>

      {/* Footer / Settings */}
      <div className="p-4 border-t border-slate-100">
        <button className="flex items-center gap-3 w-full text-left px-3 py-2.5 rounded-xl cursor-pointer text-[0.95rem] font-medium text-slate-500 hover:bg-slate-50 hover:text-slate-900 transition-colors">
          <Settings size={20} className="text-slate-400" />
          Settings
        </button>
        <button className="group flex items-center gap-3 w-full text-left px-3 py-2.5 rounded-xl cursor-pointer text-[0.95rem] font-medium text-slate-500 hover:bg-red-50 hover:text-red-600 transition-colors mt-1">
          <LogOut size={20} className="text-slate-400 group-hover:text-red-500" />
          Logout
        </button>
      </div>
    </aside>
  );
}
