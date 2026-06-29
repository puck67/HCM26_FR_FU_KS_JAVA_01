import React, { useEffect, useState, useCallback } from 'react';
import { useParams, Link, useNavigate } from 'react-router-dom';
import type { Task, TaskStatus } from '../types/task';
import { fetchTaskByIdApi } from '../api/task';
import { Spinner } from '../components/common/Spinner';
import { ErrorView } from '../components/common/ErrorView';
import { useTaskContext } from '../context/useTaskContext';
import { Modal } from '../components/task/Modal';
import { Button } from '../components/common/Button';
import { ConfirmModal } from '../components/common/ConfirmModal';

export const TaskDetail: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const { deleteTask, updateTask } = useTaskContext();

  const [task, setTask] = useState<Task | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [isEditModalOpen, setIsEditModalOpen] = useState<boolean>(false);
  const [isConfirmDeleteOpen, setIsConfirmDeleteOpen] = useState<boolean>(false);

  const loadSingleTask = useCallback(async () => {
    if (!id) {
      setError('Mã công việc không hợp lệ.');
      setLoading(false);
      return;
    }
    setLoading(true);
    setError(null);
    try {
      const data = await fetchTaskByIdApi(id);
      if (!data) {
        setError('Không tìm thấy công việc.');
      } else {
        setTask(data);
      }
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Lỗi khi tải thông tin công việc.');
    } finally {
      setLoading(false);
    }
  }, [id]);

  useEffect(() => {
    loadSingleTask();
  }, [loadSingleTask]);

  const handleConfirmDelete = () => {
    if (task?.id) {
      deleteTask(task.id);
      setIsConfirmDeleteOpen(false);
      navigate('/tasks');
    }
  };

  const handleEditSubmit = (name: string, description: string, status?: TaskStatus) => {
    if (task?.id) {
      updateTask(task.id, name, description, status);
      setTask({
        ...task,
        name,
        description,
        status: status || task.status
      });
    }
  };

  const getStatusBadge = (status?: TaskStatus) => {
    switch (status) {
      case 'Completed':
        return 'bg-emerald-50 text-emerald-700 border-emerald-200';
      case 'In Progress':
        return 'bg-amber-50 text-amber-700 border-amber-200';
      default:
        return 'bg-slate-100 text-slate-700 border-slate-200';
    }
  };

  const getStatusText = (status?: TaskStatus) => {
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
    <div className="max-w-3xl mx-auto space-y-6">
      <div>
        <Link
          to="/tasks"
          className="inline-flex items-center text-sm font-semibold text-red-900 hover:text-red-950 transition-colors mb-4"
        >
          <svg className="w-4 h-4 mr-1" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 19l-7-7 7-7" />
          </svg>
          Quay lại danh sách
        </Link>
      </div>

      {loading ? (
        <Spinner />
      ) : error ? (
        <ErrorView message={error} onRetry={loadSingleTask} />
      ) : !task ? (
        <ErrorView message="Không có dữ liệu công việc." />
      ) : (
        <div className="bg-white rounded-xl border border-slate-200 p-6 sm:p-8 shadow-2xs space-y-6">
          <div className="flex flex-col sm:flex-row sm:items-start justify-between gap-4 pb-6 border-b border-slate-100">
            <div>
              <div className="flex items-center space-x-3 mb-2">
                <span className={`text-xs font-medium px-2.5 py-1 rounded-md border ${getStatusBadge(task.status)}`}>
                  {getStatusText(task.status)}
                </span>
                <span className="text-xs text-slate-400 font-mono">ID: {task.id}</span>
              </div>
              <h1 className="text-xl sm:text-2xl font-bold text-slate-900 m-0">{task.name}</h1>
            </div>

            <div className="flex items-center space-x-2 shrink-0">
              <Button variant="secondary" size="sm" onClick={() => setIsEditModalOpen(true)}>
                Chỉnh sửa
              </Button>
              <Button variant="danger" size="sm" onClick={() => setIsConfirmDeleteOpen(true)}>
                Xóa
              </Button>
            </div>
          </div>

          <div className="space-y-4">
            <div>
              <h3 className="text-xs font-semibold text-slate-500 uppercase tracking-wider mb-2">Mô tả công việc</h3>
              <div className="bg-slate-50 rounded-lg p-4 border border-slate-100 text-slate-700 text-sm leading-relaxed whitespace-pre-wrap">
                {task.description || <span className="italic text-slate-400">Không có mô tả cho công việc này.</span>}
              </div>
            </div>

            <div className="grid grid-cols-1 sm:grid-cols-2 gap-4 pt-2">
              <div className="bg-slate-50 p-4 rounded-lg border border-slate-100">
                <span className="text-xs text-slate-500 font-medium block">Ngày tạo</span>
                <span className="text-sm font-semibold text-slate-800 mt-1 block">{task.createdAt || 'N/A'}</span>
              </div>
              <div className="bg-slate-50 p-4 rounded-lg border border-slate-100">
                <span className="text-xs text-slate-500 font-medium block">Trạng thái hiện tại</span>
                <span className="text-sm font-semibold text-slate-800 mt-1 block">{getStatusText(task.status)}</span>
              </div>
            </div>
          </div>
        </div>
      )}

      {task && (
        <Modal
          isOpen={isEditModalOpen}
          onClose={() => setIsEditModalOpen(false)}
          onSubmit={handleEditSubmit}
          initialTask={task}
        />
      )}

      <ConfirmModal
        isOpen={isConfirmDeleteOpen}
        title="Xác nhận xóa công việc"
        message="Bạn có chắc chắn muốn xóa công việc này? Thao tác này không thể hoàn tác."
        onConfirm={handleConfirmDelete}
        onCancel={() => setIsConfirmDeleteOpen(false)}
      />
    </div>
  );
};
