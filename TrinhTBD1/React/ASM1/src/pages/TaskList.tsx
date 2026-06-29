import React from 'react';
import { useTaskContext } from '../context/useTaskContext';
import { Card } from '../components/task/Card';
import { Filter } from '../components/task/Filter';
import { Modal } from '../components/task/Modal';
import { CrudGrid } from '../components/common/CrudGrid';
import { ConfirmModal } from '../components/common/ConfirmModal';
import type { Task, TaskStatus } from '../types/task';

export const TaskList: React.FC = () => {
  const {
    tasks,
    loading,
    error,
    searchTerm,
    setSearchTerm,
    statusFilter,
    setStatusFilter,
    isModalOpen,
    setIsModalOpen,
    editingTask,
    setEditingTask,
    deletingTaskId,
    setDeletingTaskId,
    addTask,
    updateTask,
    deleteTask,
    reloadTasks
  } = useTaskContext();

  const handleOpenCreate = () => {
    setEditingTask(null);
    setIsModalOpen(true);
  };

  const handleOpenEdit = (task: Task) => {
    setEditingTask(task);
    setIsModalOpen(true);
  };

  const handleRequestDelete = (id: string) => {
    setDeletingTaskId(id);
  };

  const handleConfirmDelete = () => {
    if (deletingTaskId) {
      deleteTask(deletingTaskId);
      setDeletingTaskId(null);
    }
  };

  const handleSubmitForm = (name: string, description: string, status?: TaskStatus) => {
    if (editingTask) {
      updateTask(editingTask.id, name, description, status);
    } else {
      addTask(name, description, status);
    }
  };

  const handleStatusChange = (id: string, name: string, description: string, newStatus: TaskStatus) => {
    updateTask(id, name, description, newStatus);
  };

  const filteredTasks = (tasks || []).filter((task) => {
    if (!task) return false;
    const nameMatch = (task.name || '').toLowerCase().includes((searchTerm || '').toLowerCase().trim());
    const descMatch = (task.description || '').toLowerCase().includes((searchTerm || '').toLowerCase().trim());
    const matchesSearch = nameMatch || descMatch;

    const matchesStatus = statusFilter === 'ALL' || task.status === statusFilter;
    return matchesSearch && matchesStatus;
  });

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <h1 className="text-xl font-bold text-slate-900 m-0">Danh sách công việc</h1>
      </div>

      <Filter
        searchTerm={searchTerm}
        onSearchChange={setSearchTerm}
        statusFilter={statusFilter}
        onStatusChange={setStatusFilter}
        onOpenCreateModal={handleOpenCreate}
      />

      <CrudGrid<Task>
        items={filteredTasks}
        loading={loading}
        error={error}
        entityName="Công việc"
        onRetry={reloadTasks}
        onOpenCreate={handleOpenCreate}
        renderItem={(task) => (
          <Card
            task={task}
            onEdit={handleOpenEdit}
            onDelete={handleRequestDelete}
            onStatusChange={handleStatusChange}
          />
        )}
      />

      <Modal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        onSubmit={handleSubmitForm}
        initialTask={editingTask}
      />

      <ConfirmModal
        isOpen={!!deletingTaskId}
        title="Xác nhận xóa công việc"
        message="Bạn có chắc chắn muốn xóa công việc này? Hành động này không thể hoàn tác."
        onConfirm={handleConfirmDelete}
        onCancel={() => setDeletingTaskId(null)}
      />
    </div>
  );
};
