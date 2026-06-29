import { ButtonList, type ButtonConfig } from "./ButtonList";

type CrudTableProps<T extends object> = {
  data: T[];
  onEdit?: (item: T) => void;
  onDelete?: (item: T) => void;
  extraActions?: (item: T) => ButtonConfig[];
  idKey: keyof T;
};

export function CrudTable<T extends object>({
  data,
  onEdit,
  onDelete,
  extraActions,
  idKey
}: CrudTableProps<T>) {
  if (data.length === 0) return <p className="no-data-msg">Không có dữ liệu.</p>;

  const columns = Object.keys(data[0]);

  const getValueClass = (val: string) => {
    const v = val.toLowerCase().trim();
    if (v === "hoàn thành" || v === "completed" || v === "đã hoàn thành") return "badge-success";
    if (v === "đang làm" || v === "in progress" || v === "đang thực hiện") return "badge-warning";
    if (v === "chờ xử lý" || v === "pending" || v === "đang chờ") return "badge-info";
    if (v === "cao" || v === "high") return "badge-danger";
    if (v === "trung bình" || v === "medium") return "badge-amber";
    if (v === "thấp" || v === "low") return "badge-muted";
    return "";
  };

  return (
    <div className="table-responsive">
      <table border={1} cellPadding={8}>
        <thead>
          <tr>
            {columns.map((col) => (
              <th key={col}>{col.replace(/_/g, " ").toUpperCase()}</th>
            ))}
            <th>Thao tác</th>
          </tr>
        </thead>
        <tbody>
          {data.map((item, rowIndex) => {
            const rowActions: ButtonConfig[] = [
              ...(onEdit ? [{ title: "Sửa", action: () => onEdit(item) }] : []),
              ...(onDelete ? [{ title: "Xóa", action: () => onDelete(item) }] : []),
              ...(extraActions ? extraActions(item) : []),
            ];

            const rowKeyValue = item[idKey] ? String(item[idKey]) : String(rowIndex);

            return (
              <tr key={rowKeyValue}>
                {columns.map((col) => {
                  const val = String((item as any)[col]);
                  const badgeClass = getValueClass(val);
                  return (
                    <td key={col}>
                      {badgeClass ? (
                        <span className={`badge ${badgeClass}`}>{val}</span>
                      ) : (
                        val
                      )}
                    </td>
                  );
                })}
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
