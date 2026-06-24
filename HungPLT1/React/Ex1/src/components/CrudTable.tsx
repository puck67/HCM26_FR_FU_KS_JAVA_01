import { ButtonList } from "./ButtonList";
import type { ButtonConfig } from "./ButtonList";

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
  if (data.length === 0) {
    return (
      <div className="empty-state">
        <p>Không có dữ liệu hiển thị.</p>
      </div>
    );
  }

  const columns = Object.keys(data[0]);

  return (
    <div className="crud-table-wrapper">
      <table className="crud-table">
        <thead>
          <tr>
            {columns.map((col) => (
              <th key={col}>{col.toUpperCase()}</th>
            ))}
            <th>HÀNH ĐỘNG</th>
          </tr>
        </thead>

        <tbody>
          {data.map((item, rowIndex) => {
            const rowActions: ButtonConfig[] = [
              ...(onEdit ? [{ title: "Sửa", action: () => onEdit(item), style: "btn-edit" }] : []),
              ...(onDelete ? [{ title: "Xóa", action: () => onDelete(item), style: "btn-delete" }] : []),
              ...(extraActions ? extraActions(item) : []),
            ];

            return (
              <tr key={rowIndex}>
                {columns.map((col) => (
                  <td key={col}>
                    {typeof (item as any)[col] === "boolean"
                      ? ((item as any)[col] ? "✅ Hoàn thành" : "⏳ Chờ xử lý")
                      : String((item as any)[col])}
                  </td>
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
