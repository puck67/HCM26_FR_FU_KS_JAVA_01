import { NavLink } from 'react-router-dom';

// Component thanh điều hướng cao cấp - hỗ trợ responsive và hover effects mượt mà
export default function Navbar() {
  return (
    <header className="sticky top-0 z-50 w-full border-b border-slate-800/80 bg-slate-950/80 backdrop-blur-md">
      <div className="max-w-5xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex h-16 items-center justify-between">
          {/* Logo & Nhãn hiệu */}
          <div className="flex items-center gap-8">
            <NavLink to="/" className="flex items-center gap-2.5 group">
              <span className="flex h-10 w-10 items-center justify-center rounded-xl bg-gradient-to-br from-indigo-500 via-purple-500 to-pink-500 text-white shadow-lg shadow-indigo-500/20 group-hover:scale-105 group-hover:rotate-3 transition-all duration-300">
                <svg className="w-5.5 h-5.5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2.5} d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2m-6 9l2 2 4-4" />
                </svg>
              </span>
              <span className="font-extrabold text-xl bg-gradient-to-r from-white via-indigo-100 to-indigo-300 bg-clip-text text-transparent tracking-tight">
                TaskFlow
              </span>
            </NavLink>

            {/* Menu chính cho màn hình vừa/lớn */}
            <nav className="hidden md:flex items-center gap-2">
              <NavLink
                to="/"
                className={({ isActive }) =>
                  `px-4 py-2 rounded-xl text-sm font-semibold transition-all duration-200 ${
                    isActive
                      ? 'bg-slate-800/80 text-indigo-400 font-bold border border-slate-700/50 shadow-inner'
                      : 'text-slate-450 hover:bg-slate-800/40 hover:text-slate-100 border border-transparent'
                  }`
                }
              >
                Trang chủ
              </NavLink>
              <NavLink
                to="/tasks"
                className={({ isActive }) =>
                  `px-4 py-2 rounded-xl text-sm font-semibold transition-all duration-200 ${
                    isActive
                      ? 'bg-slate-800/80 text-indigo-400 font-bold border border-slate-700/50 shadow-inner'
                      : 'text-slate-450 hover:bg-slate-800/40 hover:text-slate-100 border border-transparent'
                  }`
                }
              >
                Danh sách Tasks
              </NavLink>
            </nav>
          </div>

          {/* Menu phụ & Mobile indicator */}
          <div className="flex md:hidden items-center gap-2">
            <NavLink
              to="/"
              className={({ isActive }) =>
                `text-xs font-semibold px-3 py-1.5 rounded-lg border ${
                  isActive
                    ? 'bg-slate-800 text-indigo-400 border-slate-700'
                    : 'text-slate-400 border-transparent hover:text-slate-100'
                }`
              }
            >
              Home
            </NavLink>
            <NavLink
              to="/tasks"
              className={({ isActive }) =>
                `text-xs font-semibold px-3 py-1.5 rounded-lg border ${
                  isActive
                    ? 'bg-slate-800 text-indigo-400 border-slate-700'
                    : 'text-slate-400 border-transparent hover:text-slate-100'
                }`
              }
            >
              Tasks
            </NavLink>
          </div>
        </div>
      </div>
    </header>
  );
}
