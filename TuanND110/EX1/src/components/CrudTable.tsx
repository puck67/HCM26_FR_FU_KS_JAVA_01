import { useState, useRef } from "react";
import { ButtonList } from "./ButtonList";
import type { ButtonConfig } from "./ButtonList";
import { Pagination } from "./Pagination";

// Vietnamese translation dictionary for table columns
const COLUMNS_VIETNAMESE: Record<string, string> = {
    id: "MÃ",
    title: "TIÊU ĐỀ",
    instructor: "GIẢNG VIÊN",
    category: "DANH MỤC",
    duration: "THỜI LƯỢNG",
    level: "CẤP ĐỘ",
    status: "TRẠNG THÁI",
    courseTitle: "KHÓA HỌC",
    format: "ĐỊNH DẠNG",
    name: "HỌ & TÊN",
    email: "EMAIL",
    joinedDate: "NGÀY THAM GIA",
    studentName: "HỌC VIÊN",
    enrollmentDate: "NGÀY ĐĂNG KÝ",
    paymentStatus: "THANH TOÁN"
};

// Helper to format header text if not in translation list
const formatHeader = (col: string) => {
    if (COLUMNS_VIETNAMESE[col]) return COLUMNS_VIETNAMESE[col];
    return col
        .replace(/([A-Z])/g, ' $1')
        .trim()
        .toUpperCase();
};

const STATUS_VIETNAMESE: Record<string, { label: string; className: string }> = {
    active: { label: "Hoạt động", className: "badge success" },
    inactive: { label: "Ngưng hoạt động", className: "badge secondary" },
    published: { label: "Đã đăng", className: "badge success" },
    draft: { label: "Bản nháp", className: "badge warning" },
    paid: { label: "Đã thanh toán", className: "badge success" },
    pending: { label: "Chờ xử lý", className: "badge warning" },
    unpaid: { label: "Chưa thanh toán", className: "badge danger" },
    refunded: { label: "Đã hoàn tiền", className: "badge danger" }
};

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
    const [currentPage, setCurrentPage] = useState(1);
    const itemsPerPage = 5;

    // Adjust state inline during render when dataset size changes
    const prevDataLengthRef = useRef(data.length);
    if (data.length !== prevDataLengthRef.current) {
        prevDataLengthRef.current = data.length;
        setCurrentPage(1);
    }

    if (data.length === 0) {
        return (
            <div className="table-container" style={{ textAlign: 'center', padding: '64px 20px', backgroundColor: 'var(--color-surface-card)' }}>
                <span className="material-symbols-outlined" style={{ fontSize: '48px', color: 'var(--color-outline)', marginBottom: '16px' }}>
                    inbox
                </span>
                <p className="text-body-md font-medium" style={{ color: 'var(--color-secondary)' }}>Không tìm thấy dữ liệu nào</p>
            </div>
        );
    }

    const columns = data.length > 0 ? (Object.keys(data[0]) as Array<keyof T>) : [];
    
    // Slice data for pagination
    const startIndex = (currentPage - 1) * itemsPerPage;
    const paginatedData = data.slice(startIndex, startIndex + itemsPerPage);

    return (
        <div className="table-container">
            <table className="data-table">
                <thead>
                    <tr>
                        {columns.map((col) => (
                            <th key={String(col)}>
                                <div className="table-sort-header">
                                    <span>{formatHeader(String(col))}</span>
                                    <span className="material-symbols-outlined text-sm">unfold_more</span>
                                </div>
                            </th>
                        ))}
                        <th style={{ textAlign: 'center', width: '60px' }}>
                            THAO TÁC
                        </th>
                    </tr>
                </thead>

                <tbody>
                    {paginatedData.map((item, rowIndex) => {
                        const rowActions: ButtonConfig[] = [
                            ...(onEdit ? [{ 
                                title: <span className="material-symbols-outlined" style={{ fontSize: '18px' }}>edit</span>, 
                                action: () => onEdit(item), 
                                style: "action-btn" 
                            }] : []),
                            ...(onDelete ? [{ 
                                title: <span className="material-symbols-outlined" style={{ fontSize: '18px' }}>delete</span>, 
                                action: () => onDelete(item), 
                                style: "action-btn danger" 
                            }] : []),
                            ...(extraActions ? extraActions(item) : []),
                        ];

                        const rowKey = (item as any).id !== undefined ? (item as any).id : rowIndex;

                        return (
                            <tr key={rowKey}>
                                {columns.map((col) => {
                                    const value = item[col];
                                    const colName = String(col).toLowerCase();
                                    const isStatusCol = colName.endsWith('status') || colName === 'status';

                                    if (isStatusCol && typeof value === 'string') {
                                        const lowerVal = value.toLowerCase();
                                        const statusInfo = STATUS_VIETNAMESE[lowerVal] || { label: value, className: "badge secondary" };
                                        return (
                                            <td key={String(col)}>
                                                <span className={statusInfo.className}>
                                                    {statusInfo.label}
                                                </span>
                                            </td>
                                        );
                                    }

                                    if (colName === 'id' && typeof value === 'string') {
                                        return (
                                            <td key={String(col)}>
                                                <code style={{ 
                                                    fontFamily: 'monospace',
                                                    fontSize: '12px',
                                                    backgroundColor: 'var(--color-surface-container)',
                                                    color: 'var(--color-primary)',
                                                    padding: '2px 6px',
                                                    borderRadius: '4px',
                                                    fontWeight: 600
                                                }}>{value}</code>
                                            </td>
                                        );
                                    }

                                    return (
                                        <td key={String(col)}>
                                            <span className="text-body-md font-medium text-on-surface" style={{ wordBreak: 'break-word' }}>
                                                {String(value ?? '')}
                                            </span>
                                        </td>
                                    );
                                })}
                                <td style={{ textAlign: 'center' }}>
                                    <div className="table-actions">
                                        <ButtonList items={rowActions} />
                                    </div>
                                </td>
                            </tr>
                        );
                    })}
                </tbody>
            </table>
            <Pagination
                currentPage={currentPage}
                totalItems={data.length}
                itemsPerPage={itemsPerPage}
                onPageChange={setCurrentPage}
            />
        </div>
    );
}