import { useState, useMemo } from 'react';
import { Link } from 'react-router-dom';
import { useTasks } from '../hooks/useTasks';
import TaskForm from '../components/TaskForm';

// Định nghĩa kiểu lọc dữ liệu
type FilterStatus = 'ALL' | 'PENDING' | 'COMPLETED';

export default function Tasks() {
  // useState cho điều khiển UI: Ẩn/hiện Form, Lọc trạng thái, Tìm kiếm
  const [showForm, setShowForm] = useState(false);
  const [filter, setFilter] = useState<FilterStatus>('ALL');
  const [searchQuery, setSearchQuery] = useState('');

  // Lấy dữ liệu và các hàm từ custom hook (đã kết nối Context)
  const { tasks, loading, error, addTask, deleteTask, toggleComplete } = useTasks();

  // Lọc và Tìm kiếm danh sách công việc sử dụng useMemo để tối ưu hiệu năng
  const filteredTasks = useMemo(() => {
    return tasks.filter((task) => {
      const matchesSearch = task.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
                            (task.description || '').toLowerCase().includes(searchQuery.toLowerCase());
      
      if (filter === 'PENDING') return matchesSearch && !task.completed;
      if (filter === 'COMPLETED') return matchesSearch && task.completed;
      return matchesSearch;
    });
  }, [tasks, filter, searchQuery]);

  // Thống kê nhanh danh sách hiển thị
  const totalCount = tasks.length;
  const completedCount = tasks.filter((t) => t.completed).length;
  const pendingCount = totalCount - completedCount;

  if (loading) {
    return (
      <div className="max-w-4xl mx-auto px-4 py-24 text-center">
        <div className="inline-block relative w-12 h-12">
          <div className="absolute top-0 left-0 w-full h-full border-4 border-indigo-500/20 rounded-full" />
          <div className="absolute top-0 left-0 w-full h-full border-4 border-indigo-500 border-t-transparent rounded-full animate-spin" />
        </div>
        <p className="mt-4 text-slate-400 font-medium">Đang tải danh sách công việc...</p>
      </div>
    );
  }

  if (error) {
    return (
      <div className="max-w-4xl mx-auto px-4 py-12">
        <div className="bg-rose-500/10 border border-rose-500/30 text-rose-250 p-6 rounded-2xl flex items-start gap-4">
          <span className="flex h-10 w-10 items-center justify-center rounded-xl bg-rose-500/20 text-rose-400">
            ❌
          </span>
          <div>
            <h3 className="font-bold text-lg text-rose-300">Đã xảy ra lỗi tải dữ liệu</h3>
            <p className="mt-1 text-sm text-slate-400">{error}</p>
            <button
              onClick={() => window.location.reload()}
              className="mt-3 px-4 py-1.5 bg-rose-600/30 text-rose-300 hover:bg-rose-600/40 rounded-lg text-xs font-semibold transition"
            >
              Thử lại
            </button>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="max-w-4xl mx-auto px-4 sm:px-6 py-10">
      {/* Title & Add button */}
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4 mb-8">
        <div>
          <h1 className="text-3xl font-extrabold text-white tracking-tight">Danh sách Tasks</h1>
          <p className="text-slate-400 text-sm mt-1">
            Có {pendingCount} công việc cần giải quyết trong số {totalCount} task.
          </p>
        </div>

        <button
          onClick={() => setShowForm(!showForm)}
          className={`px-5 py-2.5 rounded-xl font-semibold flex items-center justify-center gap-2 transition duration-200 shadow-md ${
            showForm
              ? 'bg-slate-800 text-slate-300 hover:bg-slate-700/80'
              : 'bg-indigo-600 text-white hover:bg-indigo-500 shadow-indigo-600/15'
          }`}
        >
          {showForm ? (
            <>
              <svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2.5} d="M6 18L18 6M6 6l12 12" />
              </svg>
              Đóng Form
            </>
          ) : (
            <>
              <svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2.5} d="M12 4v16m8-8H4" />
              </svg>
              Thêm Task Mới
            </>
          )}
        </button>
      </div>

      {/* Form tạo mới Task */}
      {showForm && (
        <div className="bg-slate-900 border border-slate-800 rounded-2xl p-6 mb-8 shadow-xl shadow-slate-950/40 relative overflow-hidden transition-all duration-300">
          <div className="absolute top-0 left-0 right-0 h-1 bg-gradient-to-r from-indigo-500 via-purple-500 to-pink-500" />
          <h2 className="font-bold text-slate-200 mb-4 text-lg">Tạo mới một công việc</h2>
          <TaskForm
            onSubmit={(name, description) => {
              addTask(name, description);
              setShowForm(false);
            }}
            onCancel={() => setShowForm(false)}
            submitLabel="Tạo Task Mới"
          />
        </div>
      )}

      {/* Control bar: Tìm kiếm & Lọc */}
      <div className="flex flex-col md:flex-row gap-4 mb-6 items-stretch justify-between bg-slate-950/40 p-4 border border-slate-850 rounded-2xl">
        {/* Tìm kiếm */}
        <div className="relative flex-1">
          <svg className="absolute left-3.5 top-1/2 -translate-y-1/2 w-4 h-4 text-slate-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2.5} d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
          </svg>
          <input
            type="text"
            placeholder="Tìm kiếm công việc theo tên hoặc mô tả..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            className="w-full bg-slate-900 border border-slate-800 rounded-xl pl-10 pr-4 py-2.5 text-sm text-slate-250 placeholder-slate-500 outline-none focus:border-indigo-500 transition duration-200"
          />
        </div>

        {/* Bộ lọc trạng thái */}
        <div className="flex bg-slate-900 p-1 border border-slate-800 rounded-xl gap-1">
          <button
            onClick={() => setFilter('ALL')}
            className={`px-3 py-1.5 text-xs font-semibold rounded-lg transition duration-150 ${
              filter === 'ALL'
                ? 'bg-indigo-650 text-white shadow-sm'
                : 'text-slate-400 hover:text-slate-200'
            }`}
          >
            Tất cả ({totalCount})
          </button>
          <button
            onClick={() => setFilter('PENDING')}
            className={`px-3 py-1.5 text-xs font-semibold rounded-lg transition duration-150 ${
              filter === 'PENDING'
                ? 'bg-amber-600/30 text-amber-350 border border-amber-500/20'
                : 'text-slate-400 hover:text-slate-200'
            }`}
          >
            Chưa xong ({pendingCount})
          </button>
          <button
            onClick={() => setFilter('COMPLETED')}
            className={`px-3 py-1.5 text-xs font-semibold rounded-lg transition duration-150 ${
              filter === 'COMPLETED'
                ? 'bg-emerald-600/30 text-emerald-350 border border-emerald-500/20'
                : 'text-slate-400 hover:text-slate-200'
            }`}
          >
            Đã xong ({completedCount})
          </button>
        </div>
      </div>

      {/* Task List */}
      {filteredTasks.length === 0 ? (
        <div className="bg-slate-950/20 border border-dashed border-slate-850 rounded-2xl py-14 text-center">
          <span className="text-4xl block mb-4">🔍</span>
          <p className="text-slate-400 text-sm font-medium">Không tìm thấy công việc nào phù hợp</p>
          <p className="text-slate-500 text-xs mt-1">Hãy tạo task mới hoặc thử thay đổi từ khóa tìm kiếm</p>
        </div>
      ) : (
        <div className="space-y-3">
          {filteredTasks.map((task) => (
            <div
              key={task.id}
              className={`group flex items-center gap-4 bg-slate-900 border rounded-2xl px-5 py-4 shadow-sm hover:shadow-md hover:bg-slate-850/60 transition duration-200 ${
                task.completed ? 'border-slate-850' : 'border-slate-800'
              }`}
            >
              {/* Checkbox đánh dấu hoàn thành */}
              <div className="relative flex items-center justify-center">
                <input
                  type="checkbox"
                  checked={task.completed}
                  onChange={() => toggleComplete(task.id)}
                  className="w-5 h-5 rounded-lg border-2 border-slate-700 bg-slate-850 checked:bg-indigo-650 checked:border-indigo-600 focus:ring-indigo-500/30 text-indigo-650 transition cursor-pointer"
                />
              </div>

              {/* Tên công việc & Liên kết xem chi tiết */}
              <div className="flex-1 min-w-0">
                <Link
                  to={`/tasks/${task.id}`}
                  className={`block font-semibold text-sm hover:text-indigo-400 transition truncate ${
                    task.completed ? 'line-through text-slate-500 font-normal' : 'text-slate-200'
                  }`}
                >
                  {task.name}
                </Link>
                {task.description && (
                  <p className="text-xs text-slate-500 mt-1 line-clamp-1">
                    {task.description}
                  </p>
                )}
              </div>

              {/* Trạng thái badge */}
              <div>
                <span
                  className={`inline-flex items-center gap-1 text-[10px] font-bold uppercase tracking-wider px-2.5 py-1 rounded-full ${
                    task.completed
                      ? 'bg-emerald-500/10 text-emerald-450 border border-emerald-500/20'
                      : 'bg-amber-500/10 text-amber-450 border border-amber-500/20'
                  }`}
                >
                  <span className={`w-1 h-1 rounded-full ${task.completed ? 'bg-emerald-400' : 'bg-amber-400'}`} />
                  {task.completed ? 'Đã Xong' : 'Chưa Xong'}
                </span>
              </div>

              {/* Action buttons (Xem chi tiết, Xóa) */}
              <div className="flex items-center gap-1.5 opacity-60 group-hover:opacity-100 transition-opacity duration-200">
                <Link
                  to={`/tasks/${task.id}`}
                  className="p-1.5 text-slate-400 hover:text-indigo-400 hover:bg-slate-800 rounded-lg transition"
                  title="Chi tiết công việc"
                >
                  <svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" />
                  </svg>
                </Link>
                <button
                  onClick={() => {
                    if (confirm(`Bạn có chắc chắn muốn xóa task "${task.name}"?`)) {
                      deleteTask(task.id);
                    }
                  }}
                  className="p-1.5 text-slate-400 hover:text-rose-450 hover:bg-slate-800 rounded-lg transition"
                  title="Xóa công việc"
                >
                  <svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                  </svg>
                </button>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
