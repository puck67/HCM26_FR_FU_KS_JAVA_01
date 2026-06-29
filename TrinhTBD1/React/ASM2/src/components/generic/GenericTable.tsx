import React from 'react';

export interface Column<T> {
  header: string;
  accessor?: keyof T | ((item: T) => React.ReactNode);
  className?: string;
}

export interface GenericTableProps<T> {
  columns: Column<T>[];
  data: T[];
  keyExtractor: (item: T) => string;
  emptyMessage?: string;
  renderActions?: (item: T) => React.ReactNode;
  rowClassName?: (item: T) => string;
}

export function GenericTable<T>({
  columns,
  data,
  keyExtractor,
  emptyMessage = 'No records found.',
  renderActions,
  rowClassName,
}: GenericTableProps<T>) {
  if (!data || data.length === 0) {
    return (
      <div className="text-center py-10 px-4 bg-zinc-900 border border-zinc-800 rounded-lg">
        <p className="text-sm text-zinc-400">{emptyMessage}</p>
      </div>
    );
  }

  return (
    <div className="overflow-x-auto rounded-lg border border-zinc-800 bg-zinc-900">
      <table className="w-full text-left text-sm text-zinc-300">
        <thead className="bg-zinc-950 text-xs font-semibold uppercase text-zinc-400 border-b border-zinc-800">
          <tr>
            {columns.map((col, idx) => (
              <th key={idx} scope="col" className={`px-5 py-3.5 ${col.className || ''}`}>
                {col.header}
              </th>
            ))}
            {renderActions && (
              <th scope="col" className="px-5 py-3.5 text-right">
                Actions
              </th>
            )}
          </tr>
        </thead>
        <tbody className="divide-y divide-zinc-800/80">
          {data.map((item) => {
            const key = keyExtractor(item);
            const customRowClass = rowClassName ? rowClassName(item) : '';
            return (
              <tr key={key} className={`transition-colors hover:bg-zinc-800/50 ${customRowClass}`}>
                {columns.map((col, idx) => {
                  let content: React.ReactNode = null;
                  if (typeof col.accessor === 'function') {
                    content = col.accessor(item);
                  } else if (col.accessor) {
                    content = String(item[col.accessor] ?? '');
                  }
                  return (
                    <td key={idx} className={`px-5 py-3.5 align-middle ${col.className || ''}`}>
                      {content}
                    </td>
                  );
                })}
                {renderActions && (
                  <td className="px-5 py-3.5 align-middle text-right whitespace-nowrap">
                    <div className="flex items-center justify-end gap-2">
                      {renderActions(item)}
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
