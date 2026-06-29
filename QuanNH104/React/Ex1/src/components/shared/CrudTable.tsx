import React from "react";
import Button from "./Button";

export interface RowAction<T> {
  label: string | ((item: T) => string);
  onClick: (item: T) => void;
  variant?: "primary" | "secondary" | "danger" | "success" | "warning" | "cyan" | "neon";
  hidden?: (item: T) => boolean;
  disabled?: (item: T) => boolean;
}

interface CrudTableProps<T extends object> {
  data: T[];
  onEdit?: (item: T) => void;
  onDelete?: (item: T) => void;
  extraActions?: RowAction<T>[];
}

const CrudTable = <T extends object>({
  data,
  onEdit,
  onDelete,
  extraActions = [],
}: CrudTableProps<T>) => {
  // Lấy danh sách cột từ object đầu tiên nếu có data
  const columns = data.length > 0 ? (Object.keys(data[0]) as Array<keyof T>) : [];

  // Hàm render nội dung cell dựa trên cột và giá trị
  const renderCell = (col: keyof T, val: T[keyof T]) => {
    const colStr = String(col).toLowerCase();
    
    // Định dạng tiền tệ VND nếu cột liên quan đến giá cả
    if (colStr.includes("price") && typeof val === "number") {
      return new Intl.NumberFormat("vi-VN", {
        style: "currency",
        currency: "VND",
      }).format(val);
    }

    // Hiển thị trạng thái máy trạm dưới dạng badge phong cách Cyberpunk
    if (colStr === "status" && typeof val === "string") {
      let badgeClass = "badge-available";
      if (val === "Playing") badgeClass = "badge-playing";
      if (val === "Maintenance") badgeClass = "badge-maintenance";
      
      return <span className={`status-badge ${badgeClass}`}>{val}</span>;
    }

    return String(val);
  };

  // Xác định xem có cột Actions không
  const hasActions = !!onEdit || !!onDelete || extraActions.length > 0;

  return (
    <div className="table-responsive">
      <table className="crud-table">
        <thead>
          <tr>
            {columns.map((col) => (
              <th key={String(col)}>{String(col).toUpperCase()}</th>
            ))}
            {hasActions && <th>ACTIONS</th>}
          </tr>
        </thead>
        <tbody>
          {data.length === 0 ? (
            <tr>
              <td colSpan={columns.length + (hasActions ? 1 : 0)} className="text-center empty-row">
                Không có dữ liệu hiển thị.
              </td>
            </tr>
          ) : (
            data.map((item, rowIdx) => {
              // Cần sinh key duy nhất cho hàng, lấy id nếu có, nếu không lấy index
              const itemId = (item as { id?: string | number }).id || rowIdx;
              
              return (
                <tr key={String(itemId)}>
                  {columns.map((col) => (
                    <td key={String(col)} data-label={String(col).toUpperCase()}>
                      {renderCell(col, item[col])}
                    </td>
                  ))}
                  {hasActions && (
                    <td className="actions-cell" data-label="ACTIONS">
                      <div className="actions-container">
                        {/* Render các nút custom extraActions trước */}
                        {extraActions.map((action, actionIdx) => {
                          const isHidden = action.hidden ? action.hidden(item) : false;
                          if (isHidden) return null;

                          const labelText = typeof action.label === "function" 
                            ? action.label(item) 
                            : action.label;

                          const isDisabled = action.disabled ? action.disabled(item) : false;

                          return (
                            <Button
                              key={`extra-${actionIdx}`}
                              onClick={() => action.onClick(item)}
                              variant={action.variant || "secondary"}
                              disabled={isDisabled}
                              className="action-btn-sm"
                            >
                              {labelText}
                            </Button>
                          );
                        })}

                        {/* Nút Edit */}
                        {onEdit && (
                          <Button
                            onClick={() => onEdit(item)}
                            variant="warning"
                            className="action-btn-sm"
                          >
                            Edit
                          </Button>
                        )}

                        {/* Nút Delete */}
                        {onDelete && (
                          <Button
                            onClick={() => onDelete(item)}
                            variant="danger"
                            className="action-btn-sm"
                          >
                            Delete
                          </Button>
                        )}
                      </div>
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
};

export default CrudTable;
