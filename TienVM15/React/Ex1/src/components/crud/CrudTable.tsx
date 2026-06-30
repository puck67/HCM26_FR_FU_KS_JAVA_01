import { useState } from "react";
import { ButtonList } from "../ui/ButtonList";
import type { ButtonConfig } from "../ui/ButtonList";

// ─── Types ──────────────────────────────────────────────────────────────────

export type ColumnRenderer<T> = (value: unknown, item: T, rowIndex: number) => React.ReactNode;

export interface ColumnDef<T extends object> {
  /** key of field in T, or a unique string for custom columns */
  key: string;
  /** Display label for the column header */
  label?: string;
  /** Optional custom cell renderer */
  render?: ColumnRenderer<T>;
  /** Whether to hide this column */
  hidden?: boolean;
  /** Width (e.g. "120px", "20%") */
  width?: string;
}

export interface CrudTableProps<T extends object> {
  data: T[];
  /** Override or extend default column definitions. If omitted, auto-derives from data[0] keys */
  columns?: ColumnDef<T>[];
  onEdit?: (item: T) => void;
  onDelete?: (item: T) => void;
  extraActions?: (item: T) => ButtonConfig[];
  /** Empty state message */
  emptyMessage?: string;
  /** Show row numbers */
  showIndex?: boolean;
  /** Loading state */
  loading?: boolean;
  /** Unique key extractor */
  keyExtractor?: (item: T, index: number) => string | number;
  /** Enable built-in client-side pagination */
  pagination?: {
    pageSize?: number;
  };
  /** Enable built-in client-side text filtering */
  searchable?: {
    placeholder?: string;
    keys: (keyof T)[];
  };
}

// ─── Helpers ─────────────────────────────────────────────────────────────────

function formatCellValue(value: unknown): React.ReactNode {
  if (value === null || value === undefined) return <span className="cell-null">—</span>;
  if (typeof value === "boolean") {
    return (
      <span className={`badge ${value ? "badge--success" : "badge--danger"}`}>
        {value ? "Yes" : "No"}
      </span>
    );
  }
  return String(value);
}

function deriveColumns<T extends object>(data: T[]): ColumnDef<T>[] {
  if (data.length === 0) return [];
  return Object.keys(data[0]).map((key) => ({ key, label: toLabel(key) }));
}

function toLabel(key: string): string {
  return key
    .replace(/([A-Z])/g, " $1")
    .replace(/_/g, " ")
    .replace(/^\w/, (c) => c.toUpperCase())
    .trim();
}

// ─── Component ───────────────────────────────────────────────────────────────

export function CrudTable<T extends object>({
  data,
  columns,
  onEdit,
  onDelete,
  extraActions,
  emptyMessage = "No records found.",
  showIndex = false,
  loading = false,
  keyExtractor,
  pagination,
  searchable,
}: CrudTableProps<T>) {
  const [searchTerm, setSearchTerm] = useState("");
  const [currentPage, setCurrentPage] = useState(1);

  // 1. Apply Filtering/Searching
  const filteredData = data.filter((item) => {
    if (!searchTerm || !searchable) return true;
    return searchable.keys.some((k) => {
      const val = item[k];
      return String(val ?? "").toLowerCase().includes(searchTerm.toLowerCase());
    });
  });

  // 2. Apply Pagination
  const pageSize = pagination?.pageSize ?? 5;
  const isPaginationEnabled = !!pagination;
  const totalPages = Math.ceil(filteredData.length / pageSize);
  
  // Adjust page index if data size changes
  const activePage = Math.min(currentPage, Math.max(1, totalPages));
  
  const paginatedData = isPaginationEnabled
    ? filteredData.slice((activePage - 1) * pageSize, activePage * pageSize)
    : filteredData;

  const resolvedColumns: ColumnDef<T>[] = (columns ?? deriveColumns(data)).filter(
    (col) => !col.hidden
  );

  const hasActions = onEdit || onDelete || extraActions;

  return (
    <div className="table-container-layout">
      {/* Search Header */}
      {searchable && (
        <div className="table-search-bar">
          <svg className="search-icon" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
            <circle cx="11" cy="11" r="8"></circle>
            <line x1="21" y1="21" x2="16.65" y2="16.65"></line>
          </svg>
          <input
            type="text"
            className="search-input"
            placeholder={searchable.placeholder ?? "Search records..."}
            value={searchTerm}
            onChange={(e) => {
              setSearchTerm(e.target.value);
              setCurrentPage(1); // reset to page 1 on search
            }}
          />
        </div>
      )}

      {loading ? (
        <div className="table-state">
          <div className="spinner" />
          <p>Loading...</p>
        </div>
      ) : filteredData.length === 0 ? (
        <div className="table-state">
          <svg className="table-empty-icon" viewBox="0 0 64 64" fill="none">
            <rect width="64" height="64" rx="12" fill="#f1f5f9" opacity="0.05" />
            <path d="M16 44h32M24 28h16M28 36h8" stroke="var(--text-secondary)" strokeWidth="2.5" strokeLinecap="round" />
          </svg>
          <p className="table-empty-text">{emptyMessage}</p>
        </div>
      ) : (
        <>
          <div className="table-wrapper">
            <table className="crud-table">
              <thead>
                <tr>
                  {showIndex && <th className="th-index">#</th>}
                  {resolvedColumns.map((col) => (
                    <th key={col.key} style={col.width ? { width: col.width } : undefined}>
                      {col.label ?? toLabel(col.key)}
                    </th>
                  ))}
                  {hasActions && <th className="th-actions">Actions</th>}
                </tr>
              </thead>

              <tbody>
                {paginatedData.map((item, rowIndex) => {
                  const globalRowIndex = isPaginationEnabled ? (activePage - 1) * pageSize + rowIndex : rowIndex;
                  const rowKey = keyExtractor ? keyExtractor(item, globalRowIndex) : globalRowIndex;

                  const rowActions: ButtonConfig[] = [
                    ...(onEdit
                      ? [
                          {
                            title: "Edit",
                            action: () => onEdit(item),
                            variant: "secondary" as const,
                            size: "sm" as const,
                            icon: (
                              <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                                <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7" />
                                <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z" />
                              </svg>
                            ),
                          },
                        ]
                      : []),
                    ...(onDelete
                      ? [
                          {
                            title: "Delete",
                            action: () => onDelete(item),
                            variant: "danger" as const,
                            size: "sm" as const,
                            icon: (
                              <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                                <polyline points="3 6 5 6 21 6" />
                                <path d="M19 6l-1 14H6L5 6" />
                                <path d="M10 11v6M14 11v6" />
                                <path d="M9 6V4h6v2" />
                              </svg>
                            ),
                          },
                        ]
                      : []),
                    ...(extraActions ? extraActions(item) : []),
                  ];

                  return (
                    <tr key={rowKey} className="table-row">
                      {showIndex && <td className="td-index">{globalRowIndex + 1}</td>}
                      {resolvedColumns.map((col) => (
                        <td key={col.key}>
                          {col.render
                            ? col.render((item as Record<string, unknown>)[col.key], item, globalRowIndex)
                            : formatCellValue((item as Record<string, unknown>)[col.key])}
                        </td>
                      ))}
                      {hasActions && (
                        <td className="td-actions">
                          <ButtonList items={rowActions} gap="sm" />
                        </td>
                      )}
                    </tr>
                  );
                })}
              </tbody>
            </table>
          </div>

          {/* Pagination Footer */}
          {isPaginationEnabled && totalPages > 1 && (
            <div className="table-pagination-footer">
              <span className="pagination-info">
                Showing page <strong>{activePage}</strong> of <strong>{totalPages}</strong> ({filteredData.length} records)
              </span>
              <div className="pagination-buttons">
                <button
                  className="btn btn-secondary btn-sm"
                  disabled={activePage === 1}
                  onClick={() => setCurrentPage((p) => Math.max(1, p - 1))}
                >
                  Previous
                </button>
                <button
                  className="btn btn-secondary btn-sm"
                  disabled={activePage === totalPages}
                  onClick={() => setCurrentPage((p) => Math.min(totalPages, p + 1))}
                >
                  Next
                </button>
              </div>
            </div>
          )}
        </>
      )}
    </div>
  );
}
