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
  if (data.length === 0) return <p>No data.</p>;

  const columns = Object.keys(data[0]);

  return (
    <table border={1} cellPadding={8}>
      <thead>
        <tr>
          {columns.map((col) => (
            <th key={col}>{col.toUpperCase()}</th>
          ))}
          <th>Actions</th>
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
  );
}
