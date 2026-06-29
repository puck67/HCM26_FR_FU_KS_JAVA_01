import React from 'react';
import { Link } from 'react-router-dom';
import { useTaskContext } from '../context/useTaskContext';

export const Home: React.FC = () => {
  const { tasks, loading } = useTaskContext();

  const totalTasks = tasks?.length || 0;
  const completedTasks = tasks?.filter((t) => t.status === 'Completed').length || 0;
  const inProgressTasks = tasks?.filter((t) => t.status === 'In Progress').length || 0;
  const pendingTasks = tasks?.filter((t) => t.status === 'Pending').length || 0;

  const getStatusBadge = (status: string) => {
    switch (status) {
      case 'Completed':
        return <span className="text-xs px-2.5 py-1 rounded-md bg-emerald-50 text-emerald-700 font-medium border border-emerald-200">Hoàn thành</span>;
      case 'In Progress':
        return <span className="text-xs px-2.5 py-1 rounded-md bg-amber-50 text-amber-700 font-medium border border-amber-200">Đang thực hiện</span>;
      default:
        return <span className="text-xs px-2.5 py-1 rounded-md bg-slate-100 text-slate-700 font-medium border border-slate-200">Chờ xử lý</span>;
    }
  };

  return (
    <div className="space-y-6">
      <div className="bg-white rounded-xl border border-slate-200 p-6 flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4">
        <div>
          <h1 className="text-xl font-bold text-slate-900 m-0">Tổng quan hệ thống</h1>
          <p className="text-sm text-slate-500 mt-1">Theo dõi tiến độ và trạng thái các công việc trong dự án.</p>
        </div>
        <Link
          to="/tasks"
          className="px-4 py-2 bg-red-900 hover:bg-red-950 text-white font-medium rounded-lg text-sm transition-colors shadow-xs"
        >
          Xem bảng công việc &rarr;
        </Link>
      </div>

      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
        <div className="bg-white p-5 rounded-xl border border-slate-200 shadow-2xs">
          <p className="text-xs font-semibold text-slate-500 uppercase tracking-wider">Tổng công việc</p>
          <p className="text-2xl font-bold text-slate-900 mt-1">{loading ? '...' : totalTasks}</p>
        </div>

        <div className="bg-white p-5 rounded-xl border border-slate-200 shadow-2xs">
          <p className="text-xs font-semibold text-amber-700 uppercase tracking-wider">Đang thực hiện</p>
          <p className="text-2xl font-bold text-amber-700 mt-1">{loading ? '...' : inProgressTasks}</p>
        </div>

        <div className="bg-white p-5 rounded-xl border border-slate-200 shadow-2xs">
          <p className="text-xs font-semibold text-emerald-700 uppercase tracking-wider">Hoàn thành</p>
          <p className="text-2xl font-bold text-emerald-700 mt-1">{loading ? '...' : completedTasks}</p>
        </div>

        <div className="bg-white p-5 rounded-xl border border-slate-200 shadow-2xs">
          <p className="text-xs font-semibold text-slate-600 uppercase tracking-wider">Chờ xử lý</p>
          <p className="text-2xl font-bold text-slate-800 mt-1">{loading ? '...' : pendingTasks}</p>
        </div>
      </div>

      <div className="bg-white rounded-xl border border-slate-200 p-6 shadow-2xs">
        <div className="flex items-center justify-between mb-4 pb-3 border-b border-slate-100">
          <h2 className="text-base font-bold text-slate-900 m-0">Công việc gần đây</h2>
          <Link to="/tasks" className="text-xs font-semibold text-red-900 hover:text-red-950">
            Xem tất cả &rarr;
          </Link>
        </div>
        {loading ? (
          <p className="text-sm text-slate-500 py-4">Đang tải dữ liệu...</p>
        ) : tasks.length === 0 ? (
          <p className="text-sm text-slate-500 py-4">Chưa có công việc nào.</p>
        ) : (
          <div className="divide-y divide-slate-100">
            {tasks.slice(0, 5).map((task) => (
              <div key={task.id} className="py-3 flex items-center justify-between gap-4">
                <div className="min-w-0 flex-1">
                  <Link to={`/tasks/${task.id}`} className="font-semibold text-slate-800 hover:text-red-900 text-sm truncate block">
                    {task.name}
                  </Link>
                  <p className="text-xs text-slate-500 truncate mt-0.5">{task.description || 'Không có mô tả'}</p>
                </div>
                <div className="shrink-0">
                  {getStatusBadge(task.status)}
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};
