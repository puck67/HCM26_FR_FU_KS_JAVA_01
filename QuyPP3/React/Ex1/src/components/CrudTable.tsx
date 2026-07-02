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
  extraActions?: (item: T) => ButtonConfig[];
};

export function CrudTable<T extends object>({
  data,
  columns,
  onEdit,
  onDelete,
  extraActions
}: CrudTableProps<T>) {
  return (
    <div className="table-responsive">
      <table className="crud-table">
        <thead>
          <tr>
            {columns.map((col, index) => (
              <th key={index}>
                {col.header}
              </th>
            ))}
            {(onEdit || onDelete || extraActions) && <th className="actions-header">Actions</th>}
          </tr>
        </thead>

        <tbody>
          {data.length === 0 ? (
            <tr>
              <td colSpan={columns.length + ((onEdit || onDelete || extraActions) ? 1 : 0)} className="no-records">
                No records found.
              </td>
            </tr>
          ) : (
            data.map((item, rowIndex) => {
              const rowActions: ButtonConfig[] = [
                ...(onEdit ? [{ title: "Edit", action: () => onEdit(item), variant: "secondary" as const }] : []),
                ...(onDelete ? [{ title: "Delete", action: () => onDelete(item), variant: "danger" as const }] : []),
                ...(extraActions ? extraActions(item) : []),
              ];

              return (
                <tr key={rowIndex}>
                  {columns.map((col, colIndex) => (
                    <td key={colIndex}>
                      {col.render 
                        ? col.render(item) 
                        : String((item as any)[col.key] ?? "")}
                    </td>
                  ))}

                  {rowActions.length > 0 && (
                    <td className="actions-cell">
                      <ButtonList items={rowActions} />
                    </td>
                  )}
                </tr>
              );
            })
          )}
        </tbody>
      </table>
    </div>
  );
}
