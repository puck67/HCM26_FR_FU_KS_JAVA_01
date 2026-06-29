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
  if (data.length === 0) return <p>No data.</p>;

  const columns = Object.keys(data[0]);

  return (
    <div className="glass-table-wrapper">
      <table className="glass-table">
        <thead>
          <tr>
            {columns.map((col) => (
              <th key={col}>{col.toUpperCase()}</th>
            ))}
            <th>ACTIONS</th>
          </tr>
        </thead>

        <tbody>
          {data.map((item, rowIndex) => {
            const rowActions: ButtonConfig[] = [
              ...(onEdit ? [{ title: "Edit", action: () => onEdit(item) }] : []),
              ...(onDelete ? [{ title: "Delete", action: () => onDelete(item) }] : []),
              ...(extraActions ? extraActions(item) : []),
            ];

            return (
              <tr key={rowIndex}>
                {columns.map((col) => {
                  const val = (item as any)[col];
                  let displayVal = String(val);
                  if (typeof val === 'object' && val !== null) {
                    displayVal = Array.isArray(val) ? `[${val.length} mục]` : "{...}";
                  }
                  return <td key={col}>{displayVal}</td>;
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
