'use client';

import { ButtonList, type ButtonConfig } from "./ButtonList";

type CrudTableProps<T extends object> = {
  data: T[];
  onEdit?: (item: T) => void;
  onDelete?: (item: T) => void;
  extraActions?: (item: T) => ButtonConfig[];
  onRowClick?: (item: T) => void;
  rowStyle?: (item: T) => string;
};

export function CrudTable<T extends object>({
  data,
  onEdit,
  onDelete,
  extraActions,
  onRowClick,
  rowStyle
}: CrudTableProps<T>) {
  if (data.length === 0) return <div className="text-center py-8 text-gray-500">No data found.</div>;

  const columns = Object.keys(data[0]).filter(key => key !== 'id'); // usually hide id or format it

  return (
    <div className="overflow-x-auto shadow-md rounded-lg">
      <table className="w-full text-sm text-left text-gray-700">
        <thead className="text-xs text-gray-700 uppercase bg-gray-100">
          <tr>
            <th className="px-6 py-3">ID</th>
            {columns.map((col) => (
              <th key={col} className="px-6 py-3">{col}</th>
            ))}
            <th className="px-6 py-3 text-right">Actions</th>
          </tr>
        </thead>
        <tbody>
          {data.map((item, rowIndex) => {
            const rowActions: ButtonConfig[] = [
              ...(onEdit ? [{ title: "Edit", action: () => onEdit(item), style: "bg-blue-100 text-blue-700 hover:bg-blue-200" }] : []),
              ...(onDelete ? [{ title: "Delete", action: () => onDelete(item), style: "bg-red-100 text-red-700 hover:bg-red-200" }] : []),
              ...(extraActions ? extraActions(item) : []),
            ];

            const customStyle = rowStyle ? rowStyle(item) : 'bg-white text-gray-900';

            return (
              <tr 
                key={(item as any).id || rowIndex} 
                className={`border-b hover:bg-gray-50 ${onRowClick ? 'cursor-pointer' : ''} ${customStyle}`}
                onClick={() => onRowClick && onRowClick(item)}
              >
                <td className="px-6 py-4 font-medium whitespace-nowrap">{(item as any).id}</td>
                {columns.map((col) => {
                  let val = (item as any)[col];
                  if (typeof val === 'boolean') val = val ? 'Yes' : 'No';
                  return <td key={col} className="px-6 py-4 truncate max-w-xs">{String(val)}</td>
                })}
                <td className="px-6 py-4 text-right" onClick={e => e.stopPropagation()}>
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
