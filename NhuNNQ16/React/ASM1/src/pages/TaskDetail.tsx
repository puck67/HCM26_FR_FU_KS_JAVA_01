import { useState } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import { useTasks } from '../hooks/useTasks';
import TaskForm from '../components/TaskForm';

export default function TaskDetail() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const { tasks, updateTask, deleteTask, toggleComplete } = useTasks();

  // useState điều khiển UI: Chế độ chỉnh sửa
  const [isEditing, setIsEditing] = useState(false);

  // Tìm kiếm task cụ thể theo id
  const task = tasks.find((t) => t.id === Number(id));

  // Nếu không tồn tại task
  if (!task) {
    return (
      <div className="max-w-2xl mx-auto px-4 py-16 text-center">
        <div className="text-5xl mb-4">🔍</div>
        <h2 className="text-xl font-bold text-slate-200 mb-2">Không tìm thấy công việc</h2>
        <p className="text-slate-450 text-sm mb-6">Có vẻ như công việc này đã bị xóa hoặc không tồn tại.</p>
        <Link
          to="/tasks"
          className="inline-flex items-center gap-2 px-5 py-2.5 rounded-xl bg-slate-800 text-slate-200 hover:bg-slate-700 transition font-medium text-sm"
        >
          ← Quay lại danh sách
        </Link>
      </div>
    );
  }

  // Cập nhật thông tin task
  const handleUpdate = (name: string, description: string) => {
    updateTask({ ...task, name, description });
    setIsEditing(false);
  };

  // Xử lý xóa task
  const handleDelete = () => {
    if (window.confirm(`Bạn có chắc chắn muốn xóa công việc "${task.name}" không?`)) {
      deleteTask(task.id);
      navigate('/tasks');
    }
  };

  return (
    <div className="max-w-2xl mx-auto px-4 sm:px-6 py-10">
      {/* Quay lại */}
      <Link
        to="/tasks"
        className="inline-flex items-center gap-1.5 text-sm text-slate-400 hover:text-indigo-400 font-semibold mb-6 transition"
      >
        <svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2.5} d="M10 19l-7-7m0 0l7-7m-7 7h18" />
        </svg>
        Quay lại danh sách
      </Link>

      <div className="bg-slate-900 border border-slate-800 rounded-3xl p-6 sm:p-8 shadow-xl shadow-slate-950/50 relative overflow-hidden">
        {/* Decorative corner glow */}
        <div className="absolute -top-16 -right-16 w-32 h-32 bg-indigo-500/10 rounded-full blur-2xl pointer-events-none" />

        {isEditing ? (
          // Chế độ chỉnh sửa sử dụng Formik
          <div>
            <div className="flex items-center gap-3 mb-6">
              <span className="flex h-9 w-9 items-center justify-center rounded-xl bg-indigo-500/10 text-indigo-400">
                <svg className="w-4.5 h-4.5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" />
                </svg>
              </span>
              <h2 className="text-xl font-bold text-slate-200">Chỉnh sửa công việc</h2>
            </div>
            <TaskForm
              initialValues={{ name: task.name, description: task.description }}
              onSubmit={handleUpdate}
              onCancel={() => setIsEditing(false)}
              submitLabel="Lưu thay đổi"
            />
          </div>
        ) : (
          // Chế độ hiển thị chi tiết công việc
          <div>
            {/* Header thông tin chính */}
            <div className="flex flex-col sm:flex-row sm:items-start justify-between gap-4 border-b border-slate-800 pb-6 mb-6">
              <div>
                <span className="text-xs font-mono text-slate-500 block mb-1">ID: #{task.id}</span>
                <h1 className="text-2xl font-extrabold text-white tracking-tight leading-tight">
                  {task.name}
                </h1>
              </div>

              <button
                onClick={() => toggleComplete(task.id)}
                className={`inline-flex items-center gap-1.5 px-3 py-1.5 rounded-full text-xs font-bold uppercase tracking-wider transition ${
                  task.completed
                    ? 'bg-emerald-500/10 text-emerald-400 border border-emerald-500/20 hover:bg-emerald-500/20'
                    : 'bg-amber-500/10 text-amber-400 border border-amber-500/20 hover:bg-amber-500/20'
                }`}
              >
                <span className={`w-1.5 h-1.5 rounded-full ${task.completed ? 'bg-emerald-400' : 'bg-amber-400'}`} />
                {task.completed ? 'Đã Hoàn Thành' : 'Chưa Hoàn Thành'}
              </button>
            </div>

            {/* Chi tiết nội dung */}
            <div className="space-y-6 mb-8">
              <div>
                <h3 className="text-sm font-semibold text-slate-400 uppercase tracking-wider mb-2">
                  Mô tả công việc
                </h3>
                <div className="bg-slate-950/40 border border-slate-850 p-4 rounded-xl">
                  {task.description ? (
                    <p className="text-slate-200 text-sm whitespace-pre-line leading-relaxed">
                      {task.description}
                    </p>
                  ) : (
                    <p className="text-slate-500 text-sm italic">Không có mô tả chi tiết cho công việc này.</p>
                  )}
                </div>
              </div>
            </div>

            {/* Hành động */}
            <div className="flex flex-wrap items-center justify-between gap-4 pt-2">
              <div className="flex gap-2">
                <button
                  onClick={() => setIsEditing(true)}
                  className="px-4 py-2 bg-indigo-650 hover:bg-indigo-600 text-white rounded-xl text-sm font-semibold flex items-center gap-1.5 transition"
                >
                  <svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" />
                  </svg>
                  Chỉnh sửa
                </button>
                <button
                  onClick={handleDelete}
                  className="px-4 py-2 bg-rose-500/10 hover:bg-rose-500/20 text-rose-400 rounded-xl text-sm font-semibold flex items-center gap-1.5 transition"
                >
                  <svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                  </svg>
                  Xóa bỏ
                </button>
              </div>

              {/* Nút đánh dấu nhanh ngay ở đây */}
              <button
                onClick={() => toggleComplete(task.id)}
                className="px-4 py-2 border border-slate-700 hover:bg-slate-800 text-slate-300 rounded-xl text-sm font-semibold transition"
              >
                {task.completed ? 'Đánh dấu chưa xong' : 'Đánh dấu hoàn thành'}
              </button>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}
