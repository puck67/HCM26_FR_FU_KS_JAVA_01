import { useState, useEffect } from "react";
import { Edit2, Trash2, Eye, ChevronUp, ChevronDown, ChevronsUpDown, ChevronLeft, ChevronRight } from "lucide-react";
import { ButtonList, type ButtonConfig } from "./ButtonList";

type CrudTableProps<T extends object> = {
  data: T[];
  onView?: (item: T) => void;
  onEdit?: (item: T) => void;
  onDelete?: (item: T) => void;
  extraActions?: (item: T) => ButtonConfig[];
  pageSize?: number;
};

export function CrudTable<T extends object>({
  data,
  onView,
  onEdit,
  onDelete,
  extraActions,
  pageSize = 5
}: CrudTableProps<T>) {
  const [sortConfig, setSortConfig] = useState<{ key: string; direction: 'asc' | 'desc' } | null>(null);
  const [currentPage, setCurrentPage] = useState(1);
  const [internalPageSize, setInternalPageSize] = useState(pageSize);

  useEffect(() => {
    setCurrentPage(1);
  }, [data.length]);

  if (data.length === 0) return <div className="p-16 text-center text-slate-400 italic">No data available in this section.</div>;

  const columns = Object.keys(data[0]);

  // Sorting Logic
  const sortedData = [...data].sort((a, b) => {
    if (!sortConfig) return 0;
    const { key, direction } = sortConfig;
    const valA = (a as any)[key];
    const valB = (b as any)[key];

    if (valA < valB) return direction === 'asc' ? -1 : 1;
    if (valA > valB) return direction === 'asc' ? 1 : -1;
    return 0;
  });

  // Pagination Logic
  const totalPages = Math.ceil(sortedData.length / internalPageSize);
  const validPage = Math.min(Math.max(1, currentPage), Math.max(1, totalPages));
  const paginatedData = sortedData.slice((validPage - 1) * internalPageSize, validPage * internalPageSize);

  const handleSort = (key: string) => {
    let direction: 'asc' | 'desc' = 'asc';
    if (sortConfig && sortConfig.key === key && sortConfig.direction === 'asc') {
      direction = 'desc';
    }
    setSortConfig({ key, direction });
    setCurrentPage(1);
  };

  return (
    <div className="flex flex-col w-full">
      <div className="overflow-auto w-full max-h-[72vh] custom-scrollbar">
        <table className="w-full border-collapse text-left whitespace-nowrap">
          <thead className="sticky top-0 z-10 shadow-[0_1px_2px_rgba(0,0,0,0.05)]">
            <tr>
              {columns.map((col) => (
                <th
                  key={col}
                  onClick={() => handleSort(col)}
                  className="px-6 py-4 text-slate-500 text-[0.75rem] font-bold uppercase tracking-wider border-b border-slate-200 bg-slate-50/95 backdrop-blur-md cursor-pointer hover:bg-slate-100/95 transition-colors group select-none"
                >
                  <div className="flex items-center gap-2">
                    {col}
                    <span className="text-slate-400 group-hover:text-slate-600 transition-colors flex items-center">
                      {sortConfig?.key === col ? (
                        sortConfig.direction === 'asc' ? <ChevronUp size={15} className="text-blue-600" /> : <ChevronDown size={15} className="text-blue-600" />
                      ) : (
                        <ChevronsUpDown size={15} className="opacity-0 group-hover:opacity-100" />
                      )}
                    </span>
                  </div>
                </th>
              ))}
              <th className="px-6 py-4 text-slate-500 text-[0.75rem] font-bold uppercase tracking-wider border-b border-slate-200 bg-slate-50/95 backdrop-blur-md text-right">
                Actions
              </th>
            </tr>
          </thead>
          <tbody className="divide-y divide-slate-50">
            {paginatedData.map((item, rowIndex) => {
              const rowActions: ButtonConfig[] = [
                ...(onView ? [{ title: <Eye size={15} />, action: () => onView(item), style: "w-8 h-8 bg-slate-50 text-slate-600 hover:bg-slate-200 rounded-lg transition-colors" }] : []),
                ...(onEdit ? [{ title: <Edit2 size={15} />, action: () => onEdit(item), style: "w-8 h-8 bg-blue-50 text-blue-600 hover:bg-blue-100 rounded-lg transition-colors" }] : []),
                ...(onDelete ? [{ title: <Trash2 size={15} />, action: () => onDelete(item), style: "w-8 h-8 bg-red-50 text-red-600 hover:bg-red-100 rounded-lg transition-colors" }] : []),
                ...(extraActions ? extraActions(item) : []),
              ];

              return (
                <tr key={rowIndex} className="hover:bg-blue-50/30 transition-colors group">
                  {columns.map((col, i) => (
                    <td key={col} className={`px-6 py-4 align-middle ${i === 0 ? 'font-semibold text-slate-900' : 'text-slate-600 font-medium'} text-[0.95rem]`}>
                      {String((item as any)[col])}
                    </td>
                  ))}
                  <td className="px-6 py-4 align-middle flex justify-end">
                    <div className="opacity-80 group-hover:opacity-100 transition-opacity">
                      <ButtonList items={rowActions} />
                    </div>
                  </td>
                </tr>
              );
            })}
          </tbody>
        </table>
      </div>

      <div className="flex items-center justify-between px-6 py-4 border-t border-slate-100 bg-white">
        <div className="flex items-center gap-6">
          <div className="text-[0.85rem] text-slate-500 font-medium">
            Showing <span className="font-semibold text-slate-900">{data.length > 0 ? (validPage - 1) * internalPageSize + 1 : 0}</span> to <span className="font-semibold text-slate-900">{Math.min(validPage * internalPageSize, data.length)}</span> of <span className="font-semibold text-slate-900">{data.length}</span> results
          </div>

          <div className="flex items-center gap-2 border-l border-slate-200 pl-6">
            <span className="text-[0.85rem] text-slate-500 font-medium">Rows per page:</span>
            <select
              className="bg-slate-50 border border-slate-200 text-slate-700 text-[0.85rem] font-semibold rounded-lg px-2 py-1 outline-none focus:border-blue-500 focus:ring-1 focus:ring-blue-500 transition-colors cursor-pointer"
              value={internalPageSize}
              onChange={(e) => {
                setInternalPageSize(Number(e.target.value));
                setCurrentPage(1);
              }}
            >
              <option value={3}>3</option>
              <option value={5}>5</option>
              <option value={10}>10</option>
              <option value={20}>20</option>
              <option value={50}>50</option>
            </select>
          </div>
        </div>

        <div className="flex items-center gap-2">
          <button
            disabled={validPage === 1 || totalPages === 0}
            onClick={() => setCurrentPage(p => p - 1)}
            className="p-1.5 border border-slate-200 rounded-lg text-slate-500 disabled:opacity-40 disabled:cursor-not-allowed hover:bg-slate-50 transition-colors shadow-sm"
          >
            <ChevronLeft size={18} />
          </button>

          <div className="flex gap-1.5 px-2">
            {totalPages > 0 ? Array.from({ length: totalPages }).map((_, i) => {
              const pageNum = i + 1;
              return (
                <button
                  key={pageNum}
                  onClick={() => setCurrentPage(pageNum)}
                  className={`w-8 h-8 flex items-center justify-center rounded-lg text-[0.85rem] font-semibold transition-all ${validPage === pageNum
                    ? 'bg-blue-600 text-white shadow-[0_3px_10px_rgba(37,99,235,0.3)]'
                    : 'text-slate-600 hover:bg-slate-100 hover:text-slate-900'
                    }`}
                >
                  {pageNum}
                </button>
              );
            }) : (
              <div className="w-8 h-8 flex items-center justify-center rounded-lg text-[0.85rem] font-semibold bg-blue-600 text-white shadow-[0_3px_10px_rgba(37,99,235,0.3)]">
                1
              </div>
            )}
          </div>

          <button
            disabled={validPage === totalPages || totalPages === 0}
            onClick={() => setCurrentPage(p => p + 1)}
            className="p-1.5 border border-slate-200 rounded-lg text-slate-500 disabled:opacity-40 disabled:cursor-not-allowed hover:bg-slate-50 transition-colors shadow-sm"
          >
            <ChevronRight size={18} />
          </button>
        </div>
      </div>
    </div>
  );
}
