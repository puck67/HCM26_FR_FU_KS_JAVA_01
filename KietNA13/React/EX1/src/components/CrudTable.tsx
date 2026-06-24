import React from 'react';
import { ButtonList, type ButtonConfig } from "./ButtonList";

type CrudTableProps<T extends object> = {
  data: T[];
  onEdit?: (item: T) => void;
  onDelete?: (item: T) => void;
  extraActions?: (item: T) => ButtonConfig[];
};

export function CrudTable<T extends object>({
  data,
  onEdit,
  onDelete,
  extraActions
}: CrudTableProps<T>) {
  if (data.length === 0) return <div className="empty-state">No data available.</div>;

  // We assume all items have the same structure. We omit rendering complex objects directly if any exist.
  // We can filter out fields like 'id' if we want, but for generic use we show all properties.
  const columns = Object.keys(data[0]);

  return (
    <div className="table-container">
      <table>
        <thead>
          <tr>
            {columns.map((col) => (
              <th key={col}>{col.replace(/([A-Z])/g, ' $1').toUpperCase()}</th>
            ))}
            <th>Actions</th>
          </tr>
        </thead>

        <tbody>
          {data.map((item, rowIndex) => {
            const rowActions: ButtonConfig[] = [
              ...(onEdit ? [{ title: "Edit", action: () => onEdit(item), style: "btn btn-outline" }] : []),
              ...(onDelete ? [{ title: "Delete", action: () => onDelete(item), style: "btn btn-danger" }] : []),
              ...(extraActions ? extraActions(item) : []),
            ];

            return (
              <tr key={rowIndex}>
                {columns.map((col) => (
                  <td key={col}>{String((item as any)[col])}</td>
                ))}

                <td>
                  <ButtonList items={rowActions} />
                </td>
              </tr>
            );
          })}
        </tbody>
      </table>
    </div>
  );
}
