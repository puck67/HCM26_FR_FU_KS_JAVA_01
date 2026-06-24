import React from 'react';
import { ButtonList } from './ButtonList';
import { EditIcon, DeleteIcon } from './Icons';

export function CrudTable({
  data,
  columns,
  onEdit,
  onDelete,
  extraActions
}) {
  if (!data || data.length === 0) {
    return (
      <div className="no-data-card">
        <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="var(--text-muted)" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round" style={{ marginBottom: '1rem' }}>
          <path d="M22 19a2 2 0 0 1-2 2H4a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h5l2 3h9a2 2 0 0 1 2 2z"></path>
        </svg>
        <p>No records found. Click "Add" or "Import" to populate data.</p>
      </div>
    );
  }

  // Helper to render values with beautiful formatting
  const renderCellContent = (item, col) => {
    const value = item[col.key];
    
    // Delegate rendering to columns configuration callback if defined
    if (col.render) {
      return col.render(value, item);
    }
    
    if (value === null || value === undefined) return '';

    // Format boolean
    if (typeof value === 'boolean') {
      return value ? 'Yes' : 'No';
    }

    return String(value);
  };

  return (
    <div className="table-container">
      <table className="crud-table">
        <thead>
          <tr>
            {columns.map((col) => (
              <th key={col.key}>{col.label}</th>
            ))}
            {(onEdit || onDelete || extraActions) && <th style={{ width: '180px' }}>Actions</th>}
          </tr>
        </thead>
        <tbody>
          {data.map((item, rowIndex) => {
            const rowActions = [
              ...(onEdit ? [{ title: "Edit", action: () => onEdit(item), variant: "secondary", icon: <EditIcon /> }] : []),
              ...(onDelete ? [{ title: "Delete", action: () => onDelete(item), variant: "danger", icon: <DeleteIcon /> }] : []),
              ...(extraActions ? extraActions(item) : []),
            ];

            return (
              <tr key={item.id || rowIndex}>
                {columns.map((col) => (
                  <td key={col.key}>{renderCellContent(item, col)}</td>
                ))}
                {(onEdit || onDelete || extraActions) && (
                  <td>
                    <div className="row-actions">
                      <ButtonList items={rowActions} />
                    </div>
                  </td>
                )}
              </tr>
            );
          })}
        </tbody>
      </table>
    </div>
  );
}
