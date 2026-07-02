import { useNavigate } from "react-router-dom";
import { ButtonList, type ButtonConfig } from "./ButtonList";

export type ColumnConfig<T> = {
  key: keyof T | string;
  header: string;
  render?: (item: T) => React.ReactNode;
};

type CrudTableProps<T extends object> = {
  data: T[];
  columns: ColumnConfig<T>[];
  onEdit?: (item: T) => void;
  onDelete?: (item: T) => void;
  viewRoutePrefix?: string; // Allows seamless transitions to detail pages
};

export function CrudTable<T extends object & { id: string }>({
  data,
  columns,
  onEdit,
  onDelete,
}: CrudTableProps<T>) {
  const navigate = useNavigate();

  return (
    <div className="w-full overflow-x-auto rounded-xl border border-slate-200 bg-white shadow-sm">
      <table className="w-full border-collapse text-left text-sm text-slate-600">
        <thead className="bg-slate-50 text-xs font-semibold uppercase text-slate-700 border-b border-slate-200">
          <tr>
            {columns.map((col, idx) => (
              <th key={idx} className="px-6 py-4">{col.header}</th>
            ))}
            <th className="px-6 py-4 text-right">Actions</th>
          </tr>
        </thead>
        <tbody className="divide-y divide-slate-100">
          {data.length === 0 ? (
            <tr>
              <td colSpan={columns.length + 1} className="px-6 py-10 text-center text-slate-400 font-medium">
                No data available in this view.
              </td>
            </tr>
          ) : (
            data.map((item, rowIndex) => {
              const rowActions: ButtonConfig[] = [
                {
                  title: "View",
                  action: () => navigate(`/tasks/${item.id}`),
                  style: "text-blue-600 hover:bg-blue-50 px-2 py-1 rounded-md text-xs font-semibold transition"
                },
                ...(onEdit ? [{
                  title: "Edit",
                  action: () => onEdit(item),
                  style: "text-amber-600 hover:bg-amber-50 px-2 py-1 rounded-md text-xs font-semibold transition"
                }] : []),
                ...(onDelete ? [{
                  title: "Delete",
                  action: () => onDelete(item),
                  style: "text-rose-600 hover:bg-rose-50 px-2 py-1 rounded-md text-xs font-semibold transition"
                }] : []),
              ];

              return (
                <tr key={rowIndex} className="hover:bg-slate-50/70 transition-colors">
                  {columns.map((col, colIndex) => (
                    <td key={colIndex} className="px-6 py-4 whitespace-nowrap font-medium text-slate-800">
                      {col.render ? col.render(item) : String((item as any)[col.key] ?? "")}
                    </td>
                  ))}
                  <td className="px-6 py-4 text-right">
                    <div className="flex justify-end gap-2">
                      <ButtonList items={rowActions} />
                    </div>
                  </td>
                </tr>
              );
            })
          )}
        </tbody>
      </table>
    </div>
  );
}
