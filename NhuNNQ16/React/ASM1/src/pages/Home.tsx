import { Link } from 'react-router-dom';
import { useTasks } from '../hooks/useTasks';

export default function Home() {
  const { tasks, loading } = useTasks();

  const totalTasks = tasks.length;
  const completedTasks = tasks.filter((t) => t.completed).length;
  const pendingTasks = totalTasks - completedTasks;
  const completionRate = totalTasks > 0 ? Math.round((completedTasks / totalTasks) * 100) : 0;

  return (
    <div className="max-w-5xl mx-auto px-4 sm:px-6 lg:px-8 py-16">
      {/* Hero Section */}
      <div className="text-center mb-16 relative">
        {/* Glow effect background */}
        <div className="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-72 h-72 bg-indigo-500/10 rounded-full blur-3xl pointer-events-none" />

        <span className="inline-flex items-center gap-1.5 px-3 py-1 rounded-full text-xs font-semibold bg-indigo-500/10 text-indigo-400 border border-indigo-500/20 mb-6">
          <span className="w-1.5 h-1.5 rounded-full bg-indigo-400 animate-pulse" />
          Task Management System
        </span>

        <h1 className="text-4xl sm:text-5xl font-extrabold tracking-tight text-white mb-6">
          Quản lý công việc thông minh với{' '}
          <span className="bg-gradient-to-r from-indigo-400 via-purple-400 to-pink-400 bg-clip-text text-transparent">
            TaskFlow
          </span>
        </h1>
        <p className="text-slate-400 text-lg max-w-2xl mx-auto mb-10 leading-relaxed">
          Tối ưu hóa hiệu suất làm việc hàng ngày của bạn. Dễ dàng theo dõi tiến độ, phân loại trạng thái và chỉnh sửa thông tin chi tiết mọi lúc.
        </p>

        <div className="flex justify-center gap-4">
          <Link
            to="/tasks"
            className="px-6 py-3.5 rounded-xl bg-gradient-to-r from-indigo-600 to-purple-600 text-white font-semibold hover:from-indigo-500 hover:to-purple-500 shadow-lg shadow-indigo-500/20 active:scale-[0.98] transition-all duration-200 text-base"
          >
            Bắt đầu sử dụng →
          </Link>
        </div>
      </div>

      {/* Real-time Dashboard Summary */}
      <div className="bg-slate-800/40 border border-slate-800/80 rounded-2xl p-6 sm:p-8 backdrop-blur-sm mb-16">
        <h2 className="text-lg font-bold text-slate-200 mb-6 flex items-center gap-2">
          <svg className="w-5 h-5 text-indigo-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M11 3.055A9.003 9.003 0 1020.945 13H11V3.055z" />
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M20.488 9H15V3.512A9.025 9.025 0 0120.488 9z" />
          </svg>
          Thống kê tiến độ thời gian thực
        </h2>

        {loading ? (
          <div className="flex items-center justify-center py-8">
            <div className="w-8 h-8 border-2 border-indigo-500 border-t-transparent rounded-full animate-spin" />
          </div>
        ) : (
          <div className="grid grid-cols-1 lg:grid-cols-3 gap-8 items-center">
            {/* Stats count */}
            <div className="grid grid-cols-3 lg:grid-cols-1 gap-4 lg:col-span-1">
              <div className="bg-slate-900/60 border border-slate-800 p-4 rounded-xl text-center lg:text-left">
                <span className="text-xs font-semibold text-slate-500 block mb-1">Tổng Số Task</span>
                <span className="text-2xl font-bold text-white font-mono">{totalTasks}</span>
              </div>
              <div className="bg-slate-900/60 border border-slate-800 p-4 rounded-xl text-center lg:text-left">
                <span className="text-xs font-semibold text-slate-500 block mb-1">Đã Xong</span>
                <span className="text-2xl font-bold text-emerald-400 font-mono">{completedTasks}</span>
              </div>
              <div className="bg-slate-900/60 border border-slate-800 p-4 rounded-xl text-center lg:text-left">
                <span className="text-xs font-semibold text-slate-500 block mb-1">Chờ Xử Lý</span>
                <span className="text-2xl font-bold text-amber-400 font-mono">{pendingTasks}</span>
              </div>
            </div>

            {/* Circular Progress & Info */}
            <div className="lg:col-span-2 flex flex-col justify-center bg-slate-900/40 border border-slate-800/60 p-6 rounded-xl">
              <div className="flex justify-between items-center mb-3">
                <span className="text-sm font-semibold text-slate-350">Tỷ lệ hoàn thành công việc</span>
                <span className="text-sm font-bold text-indigo-400 font-mono">{completionRate}%</span>
              </div>
              {/* Progress bar container */}
              <div className="w-full bg-slate-800 h-3 rounded-full overflow-hidden mb-4">
                <div
                  className="bg-gradient-to-r from-indigo-500 via-purple-500 to-emerald-500 h-full rounded-full transition-all duration-500 ease-out"
                  style={{ width: `${completionRate}%` }}
                />
              </div>
              <p className="text-xs text-slate-400">
                {completionRate === 100
                  ? '🎉 Tuyệt vời! Bạn đã hoàn thành tất cả công việc của mình.'
                  : completionRate > 50
                  ? '🚀 Tuyệt, bạn đã đi được hơn nửa chặng đường. Tiếp tục phát huy nhé!'
                  : '💡 Bắt đầu giải quyết các công việc còn lại bằng cách nhấn vào nút bên dưới.'}
              </p>
            </div>
          </div>
        )}
      </div>

      {/* Grid Features */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <div className="group bg-slate-950/60 border border-slate-850 p-6 rounded-2xl hover:border-slate-800 hover:-translate-y-1 transition-all duration-300">
          <div className="w-12 h-12 rounded-xl bg-indigo-500/10 flex items-center justify-center text-indigo-400 mb-4 group-hover:scale-110 transition-transform duration-300">
            <svg className="w-6 h-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 9v3m0 0v3m0-3h3m-3 0H9m12 0a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
          </div>
          <h3 className="font-bold text-slate-200 mb-2">Thêm Task Mới</h3>
          <p className="text-slate-450 text-sm leading-relaxed">
            Nhập tên công việc và thông tin mô tả chi tiết, form được validate chặt chẽ với Formik và thư viện Yup.
          </p>
        </div>

        <div className="group bg-slate-950/60 border border-slate-850 p-6 rounded-2xl hover:border-slate-800 hover:-translate-y-1 transition-all duration-300">
          <div className="w-12 h-12 rounded-xl bg-purple-500/10 flex items-center justify-center text-purple-400 mb-4 group-hover:scale-110 transition-transform duration-300">
            <svg className="w-6 h-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
          </div>
          <h3 className="font-bold text-slate-200 mb-2">Theo dõi Trạng thái</h3>
          <p className="text-slate-450 text-sm leading-relaxed">
            Đánh dấu nhanh hoàn thành hoặc chưa hoàn thành trực tiếp trên màn hình danh sách với hiệu ứng gạch ngang.
          </p>
        </div>

        <div className="group bg-slate-950/60 border border-slate-850 p-6 rounded-2xl hover:border-slate-800 hover:-translate-y-1 transition-all duration-300">
          <div className="w-12 h-12 rounded-xl bg-pink-500/10 flex items-center justify-center text-pink-400 mb-4 group-hover:scale-110 transition-transform duration-300">
            <svg className="w-6 h-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" />
            </svg>
          </div>
          <h3 className="font-bold text-slate-200 mb-2">Chỉnh sửa Linh hoạt</h3>
          <p className="text-slate-450 text-sm leading-relaxed">
            Xem chi tiết mô tả đầy đủ của từng task, và chỉnh sửa thông tin công việc ngay trên trang chi tiết.
          </p>
        </div>
      </div>
    </div>
  );
}
