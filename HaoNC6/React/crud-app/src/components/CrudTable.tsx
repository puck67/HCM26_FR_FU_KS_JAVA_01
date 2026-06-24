import { ButtonList } from "./ButtonList";
import type { ButtonConfig } from "./ButtonList";

const EditIcon = () => (
  <svg xmlns="http://www.w3.org/2000/svg" width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round" style={{ display: 'block' }}>
    <path d="M12 20h9" />
    <path d="M16.5 3.5a2.12 2.12 0 0 1 3 3L7 19l-4 1 1-4Z" />
  </svg>
);

const DeleteIcon = () => (
  <svg xmlns="http://www.w3.org/2000/svg" width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round" style={{ display: 'block' }}>
    <path d="M3 6h18" />
    <path d="M19 6v14c0 1-1 2-2 2H7c-1 0-2-1-2-2V6" />
    <path d="M8 6V4c0-1 1-2 2-2h4c1 0 2 1 2 2v2" />
  </svg>
);

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
  if (data.length === 0) return <p className="no-data">No data available.</p>;

  const columns = Object.keys(data[0]);

  const formatHeader = (key: string) => {
    // Convert camelCase to Title Case and uppercase abbreviation like id to ID
    if (key.toLowerCase() === "id") return "ID";
    return key
      .replace(/([A-Z])/g, " $1")
      .replace(/^./, (str) => str.toUpperCase())
      .trim();
  };

  return (
    <div className="table-container">
      <table className="crud-table">
        <thead>
          <tr>
            {columns.map((col) => (
              <th key={col}>{formatHeader(col)}</th>
            ))}
            <th className="actions-header">Actions</th>
          </tr>
        </thead>

        <tbody>
          {data.map((item, rowIndex) => {
            const rowActions: ButtonConfig[] = [
              ...(onEdit ? [{ title: <EditIcon />, action: () => onEdit(item), style: "btn-action btn-edit", tooltip: "Edit" }] : []),
              ...(onDelete ? [{ title: <DeleteIcon />, action: () => onDelete(item), style: "btn-action btn-delete", tooltip: "Delete" }] : []),
              ...(extraActions ? extraActions(item) : []),
            ];

            const rowKey = (item as any).id !== undefined ? String((item as any).id) : String(rowIndex);

            return (
              <tr key={rowKey}>
                {columns.map((col) => {
                  const val = (item as any)[col];
                  let displayVal = "";
                  
                  if (typeof val === "boolean") {
                    displayVal = val ? "Completed" : "Pending";
                  } else if (val === null || val === undefined) {
                    displayVal = "-";
                  } else {
                    displayVal = String(val);
                  }

                  let tdClass = "";
                  if (col === "status" || col === "completed") {
                    tdClass = `status-badge ${String(val).toLowerCase()}`;
                  }

                  return (
                    <td key={col}>
                      {col === "status" || col === "completed" ? (
                        <span className={tdClass}>{displayVal}</span>
                      ) : (
                        displayVal
                      )}
                    </td>
                  );
                })}

                <td className="actions-cell">
                  <div className="actions-wrapper">
                    <ButtonList items={rowActions} />
                  </div>
                </td>
              </tr>
            );
          })}
        </tbody>
      </table>
    </div>
  );
}
