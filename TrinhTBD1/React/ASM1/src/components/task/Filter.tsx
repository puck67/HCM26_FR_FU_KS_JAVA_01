import React from 'react';
import { CrudToolbar } from '../common/CrudToolbar';

interface FilterProps {
  searchTerm: string;
  onSearchChange: (value: string) => void;
  statusFilter: string;
  onStatusChange: (value: string) => void;
  onOpenCreateModal: () => void;
}

export const Filter: React.FC<FilterProps> = ({
  searchTerm,
  onSearchChange,
  statusFilter,
  onStatusChange,
  onOpenCreateModal
}) => {
  const statusOptions = [
    { label: 'Tất cả trạng thái', value: 'ALL' },
    { label: 'Chờ xử lý', value: 'Pending' },
    { label: 'Đang thực hiện', value: 'In Progress' },
    { label: 'Hoàn thành', value: 'Completed' }
  ];

  return (
    <CrudToolbar
      title=""
      entityName="Công việc"
      createLabel="Tạo công việc mới"
      searchTerm={searchTerm}
      onSearchChange={onSearchChange}
      statusFilter={statusFilter}
      onStatusChange={onStatusChange}
      statusOptions={statusOptions}
      onOpenCreate={onOpenCreateModal}
    />
  );
};
