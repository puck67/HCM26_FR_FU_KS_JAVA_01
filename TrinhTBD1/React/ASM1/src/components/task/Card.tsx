import React from 'react';
import { Link } from 'react-router-dom';
import type { Task, TaskStatus } from '../../types/task';

interface CardProps {
  task: Task;
  onEdit: (task: Task) => void;
  onDelete: (id: string) => void;
  onStatusChange: (id: string, name: string, description: string, status: TaskStatus) => void;
}

export const Card: React.FC<CardProps> = ({ task, onEdit, onDelete, onStatusChange }) => {
  if (!task) return null;

  const getStatusStyle = (status: TaskStatus) => {
    switch (status) {
      case 'Completed':
        return 'bg-emerald-50 text-emerald-700 border-emerald-200';
      case 'In Progress':
        return 'bg-amber-50 text-amber-700 border-amber-200';
      default:
        return 'bg-slate-100 text-slate-700 border-slate-200';
    }
  };

  const getStatusText = (status: TaskStatus) => {
    switch (status) {
      case 'Completed':
        return 'Hoàn thành';
      case 'In Progress':
        return 'Đang thực hiện';
      default:
        return 'Chờ xử lý';
    }
  };

  return (
    <div data-testid={`task-card-${task.id}`} className="bg-white rounded-xl border border-slate-200 p-5 shadow-2xs hover:border-slate-300 transition-all flex flex-col justify-between group">
      <div>
        <div className="flex items-start justify-between gap-3 mb-3">
          <span data-testid="task-status-badge" className={`text-xs font-medium px-2.5 py-1 rounded-md border ${getStatusStyle(task.status)}`}>
            {getStatusText(task.status)}
          </span>
          <span className="text-xs text-slate-400 font-mono">{task.createdAt || 'N/A'}</span>
        </div>
        <h3 className="text-base font-bold text-slate-900 line-clamp-1 group-hover:text-red-900 transition-colors">
          <Link to={`/tasks/${task.id}`} data-testid="task-title-link">{task.name || 'Công việc chưa đặt tên'}</Link>
        </h3>
        <p className="text-sm text-slate-600 mt-2 line-clamp-3 min-h-[3rem]">
          {task.description || <span className="italic text-slate-400">Không có mô tả.</span>}
        </p>
      </div>

      <div className="mt-5 pt-3 border-t border-slate-100 flex items-center justify-between gap-2">
        <div className="flex items-center space-x-1">
          <Link
            to={`/tasks/${task.id}`}
            data-testid="view-task-btn"
            className="text-xs font-medium text-slate-600 hover:text-red-900 p-1.5 hover:bg-slate-50 rounded-md transition-colors"
            title="Xem chi tiết"
          >
            <svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" />
            </svg>
          </Link>
          <button
            type="button"
            data-testid="edit-task-btn"
            onClick={() => onEdit(task)}
            className="text-xs font-medium text-slate-600 hover:text-amber-600 p-1.5 hover:bg-slate-50 rounded-md transition-colors cursor-pointer"
            title="Chỉnh sửa"
          >
            <svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" />
            </svg>
          </button>
          <button
            type="button"
            data-testid="delete-task-btn"
            onClick={() => onDelete(task.id)}
            className="text-xs font-medium text-slate-600 hover:text-red-600 p-1.5 hover:bg-slate-50 rounded-md transition-colors cursor-pointer"
            title="Xóa"
          >
            <svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
            </svg>
          </button>
        </div>

        <select
          data-testid="quick-status-select"
          value={task.status || 'Pending'}
          onChange={(e) => onStatusChange(task.id, task.name, task.description, e.target.value as TaskStatus)}
          className="text-xs border border-slate-200 rounded-md bg-slate-50 px-2 py-1 focus:outline-hidden focus:ring-2 focus:ring-red-900 cursor-pointer text-slate-700"
        >
          <option value="Pending">Chờ xử lý</option>
          <option value="In Progress">Đang thực hiện</option>
          <option value="Completed">Hoàn thành</option>
        </select>
      </div>
    </div>
  );
};
