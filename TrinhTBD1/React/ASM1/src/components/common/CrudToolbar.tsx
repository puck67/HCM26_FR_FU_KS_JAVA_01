import React from 'react';
import type { CrudToolbarProps } from '../../types/crud';
import { Button } from './Button';

export const CrudToolbar: React.FC<CrudToolbarProps> = ({
  title,
  entityName,
  createLabel,
  searchTerm,
  onSearchChange,
  statusFilter,
  onStatusChange,
  statusOptions = [],
  onOpenCreate
}) => {
  const buttonText = createLabel || `Thêm ${entityName.toLowerCase()}`;

  return (
    <div id="crud-toolbar" className="bg-white rounded-xl border border-slate-200 p-4 mb-6 shadow-2xs space-y-4">
      {title && (
        <div className="flex items-center justify-between pb-3 border-b border-slate-100">
          <h2 className="text-base font-bold text-slate-900 m-0">{title}</h2>
        </div>
      )}
      <div className="flex flex-col md:flex-row gap-3 items-center justify-between">
        <div className="flex flex-col sm:flex-row items-center gap-3 w-full md:w-auto flex-1">
          <div className="relative w-full sm:w-72">
            <input
              id="search-input"
              data-testid="search-input"
              type="text"
              placeholder="Tìm kiếm..."
              value={searchTerm || ''}
              onChange={(e) => onSearchChange(e.target.value)}
              className="w-full pl-9 pr-4 py-2 border border-slate-200 rounded-lg text-sm focus:outline-hidden focus:ring-2 focus:ring-red-900 focus:border-red-900 bg-white"
            />
            <svg
              className="w-4 h-4 text-slate-400 absolute left-3 top-3"
              fill="none"
              viewBox="0 0 24 24"
              stroke="currentColor"
            >
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
            </svg>
          </div>

          {statusOptions.length > 0 && onStatusChange && (
            <div className="w-full sm:w-48">
              <select
                id="status-filter-select"
                data-testid="status-filter-select"
                value={statusFilter || 'ALL'}
                onChange={(e) => onStatusChange(e.target.value)}
                className="w-full py-2 px-3 border border-slate-200 rounded-lg text-sm bg-white focus:outline-hidden focus:ring-2 focus:ring-red-900 focus:border-red-900 cursor-pointer text-slate-700"
              >
                {statusOptions.map((opt) => (
                  <option key={opt.value} value={opt.value}>
                    {opt.label}
                  </option>
                ))}
              </select>
            </div>
          )}
        </div>

        <Button
          id="create-entity-btn"
          data-testid="create-entity-btn"
          onClick={onOpenCreate}
          icon={
            <svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 4v16m8-8H4" />
            </svg>
          }
        >
          {buttonText}
        </Button>
      </div>
    </div>
  );
};
