import { ButtonList, ButtonConfig } from "./ButtonList";

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
            <div className="table-empty">
                <p className="table-empty-title">No students found</p>
                <p className="table-empty-desc">Get started by adding your first student</p>
            </div>
        );
    }

    const columns = Object.keys(data[0]);

    return (
        <div className="table-wrapper">
            <table>
                <thead>
                    <tr>
                        <th className="th-index">#</th>
                        {columns.map((col) => (
                            <th key={col}>{col.charAt(0).toUpperCase() + col.slice(1)}</th>
                        ))}
                        <th className="th-actions">Actions</th>
                    </tr>
                </thead>

                <tbody>
                    {data.map((item, rowIndex) => {
                        const rowActions: ButtonConfig[] = [
                            ...(onEdit ? [{ title: "Edit", action: () => onEdit(item), style: "btn-row btn-row-edit" }] : []),
                            ...(onDelete ? [{ title: "Delete", action: () => onDelete(item), style: "btn-row btn-row-delete" }] : []),
                            ...(extraActions ? extraActions(item) : []),
                        ];

                        return (
                            <tr key={rowIndex}>
                                <td className="td-index">{rowIndex + 1}</td>
                                {columns.map((col) => (
                                    <td key={col}>{String((item as any)[col])}</td>
                                ))}
                                <td className="td-actions">
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