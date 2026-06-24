import { useState, useMemo } from 'react';
import type { ReactNode } from 'react';
import { ButtonList } from './ButtonList';
import type { ButtonConfig } from './ButtonList';

type CrudTableProps<T extends object> = {
  data: T[];
  onEdit?: (item: T) => void;
  onDelete?: (item: T) => void;
  extraActions?: (item: T) => ButtonConfig[];
  searchQuery?: string;
};

function formatHeader(key: string): string {
  const result = key.replace(/([A-Z])/g, ' $1');
  return result.charAt(0).toUpperCase() + result.slice(1);
}

function formatValue(key: string, value: unknown): ReactNode {
  if (value === null || value === undefined) return '';

  const stringVal = String(value);

  if (key.toLowerCase() === 'status') {
    let badgeClass = 'status-badge-default';
    const lowerVal = stringVal.toLowerCase();
    if (lowerVal === 'completed' || lowerVal === 'active' || lowerVal === 'done') {
      badgeClass = 'status-badge-success';
    } else if (lowerVal === 'pending' || lowerVal === 'in_progress' || lowerVal === 'warning') {
      badgeClass = 'status-badge-warning';
    } else if (lowerVal === 'on leave' || lowerVal === 'inactive' || lowerVal === 'failed') {
      badgeClass = 'status-badge-danger';
    }
    return <span className={`status-badge ${badgeClass}`}>{stringVal}</span>;
  }

  if (key.toLowerCase() === 'priority') {
    let badgeClass = 'priority-badge-low';
    const lowerVal = stringVal.toLowerCase();
    if (lowerVal === 'high') {
      badgeClass = 'priority-badge-high';
    } else if (lowerVal === 'medium') {
      badgeClass = 'priority-badge-medium';
    }
    return <span className={`priority-badge ${badgeClass}`}>{stringVal}</span>;
  }

  if (key.toLowerCase() === 'salary' && typeof value === 'number') {
    return new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' }).format(value);
  }

  if (key.toLowerCase() === 'gpa' && typeof value === 'number') {
    return <span className="gpa-badge">{value.toFixed(2)}</span>;
  }

  if (key.toLowerCase() === 'id') {
    return <code className="id-badge">#{stringVal.substring(0, 8)}</code>;
  }

  if (key.toLowerCase() === 'email' || stringVal.includes('@')) {
    return <a href={`mailto:${stringVal}`} className="email-link">{stringVal}</a>;
  }

  return stringVal;
}

export function CrudTable<T extends object>({
  data,
  onEdit,
  onDelete,
  extraActions,
  searchQuery = '',
}: CrudTableProps<T>) {
  const [sortColumn, setSortColumn] = useState<string | null>(null);
  const [sortDirection, setSortDirection] = useState<'asc' | 'desc'>('asc');
  const [currentPage, setCurrentPage] = useState(1);
  const [itemsPerPage, setItemsPerPage] = useState(5);

  const columns = useMemo(() => {
    if (data.length === 0) return [];
    return Object.keys(data[0]);
  }, [data]);

  const handleSort = (column: string) => {
    if (sortColumn === column) {
      setSortDirection(prev => (prev === 'asc' ? 'desc' : 'asc'));
    } else {
      setSortColumn(column);
      setSortDirection('asc');
    }
    setCurrentPage(1);
  };

  const processedData = useMemo(() => {
    let result = [...data];

    if (searchQuery.trim()) {
      const q = searchQuery.toLowerCase();
      result = result.filter(item =>
        Object.values(item).some(val =>
          String(val).toLowerCase().includes(q)
        )
      );
    }

    if (sortColumn) {
      result.sort((a, b) => {
        const valA = (a as Record<string, unknown>)[sortColumn];
        const valB = (b as Record<string, unknown>)[sortColumn];

        if (typeof valA === 'number' && typeof valB === 'number') {
          return sortDirection === 'asc' ? valA - valB : valB - valA;
        }

        const strA = String(valA).toLowerCase();
        const strB = String(valB).toLowerCase();

        if (strA < strB) return sortDirection === 'asc' ? -1 : 1;
        if (strA > strB) return sortDirection === 'asc' ? 1 : -1;
        return 0;
      });
    }

    return result;
  }, [data, searchQuery, sortColumn, sortDirection]);

  const totalPages = Math.ceil(processedData.length / itemsPerPage);
  const paginatedData = useMemo(() => {
    const startIndex = (currentPage - 1) * itemsPerPage;
    return processedData.slice(startIndex, startIndex + itemsPerPage);
  }, [processedData, currentPage, itemsPerPage]);

  const goToPage = (page: number) => {
    setCurrentPage(Math.max(1, Math.min(page, totalPages)));
  };

  if (data.length === 0) {
    return (
      <div className="empty-state">
        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5">
          <circle cx="12" cy="12" r="10" />
          <path d="M8 12h8" />
        </svg>
        <h3>No records found</h3>
        <p>Start by adding a new item or import default sample data.</p>
      </div>
    );
  }

  return (
    <div className="crud-table-wrapper">
      <div className="table-responsive">
        <table className="crud-table">
          <thead>
            <tr>
              {columns.map((col) => {
                const isSorted = sortColumn === col;
                return (
                  <th key={col} onClick={() => handleSort(col)} className="sortable-header">
                    <div className="header-cell-content">
                      <span>{formatHeader(col)}</span>
                      <span className={`sort-icon ${isSorted ? 'active' : ''}`}>
                        {isSorted ? (sortDirection === 'asc' ? ' ↑' : ' ↓') : ' ↕'}
                      </span>
                    </div>
                  </th>
                );
              })}
              <th className="actions-header">Actions</th>
            </tr>
          </thead>

          <tbody>
            {paginatedData.length === 0 ? (
              <tr>
                <td colSpan={columns.length + 1} className="no-search-results">
                  No matching results found for "{searchQuery}"
                </td>
              </tr>
            ) : (
              paginatedData.map((item, rowIndex) => {
                const rowActions: ButtonConfig[] = [
                  ...(onEdit
                    ? [
                        {
                          title: 'Edit',
                          action: () => onEdit(item),
                          style: 'btn-edit',
                          icon: (
                            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                              <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7" />
                              <path d="M18.5 2.5a2.121 2.121 0 1 1 3 3L12 15l-4 1 1-4 9.5-9.5z" />
                            </svg>
                          ),
                        },
                      ]
                    : []),
                  ...(onDelete
                    ? [
                        {
                          title: 'Delete',
                          action: () => onDelete(item),
                          style: 'btn-delete',
                          icon: (
                            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                              <polyline points="3 6 5 6 21 6" />
                              <path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2" />
                            </svg>
                          ),
                        },
                      ]
                    : []),
                  ...(extraActions ? extraActions(item) : []),
                ];

                return (
                  <tr key={rowIndex} className="table-row-item">
                    {columns.map((col) => (
                      <td key={col}>{formatValue(col, (item as Record<string, unknown>)[col])}</td>
                    ))}

                    <td className="actions-cell">
                      <ButtonList items={rowActions} />
                    </td>
                  </tr>
                );
              })
            )}
          </tbody>
        </table>
      </div>

      {processedData.length > 0 && (
        <div className="table-pagination">
          <div className="pagination-info">
            Showing <span>{Math.min(processedData.length, (currentPage - 1) * itemsPerPage + 1)}-{Math.min(processedData.length, currentPage * itemsPerPage)}</span> of <span>{processedData.length}</span> records
          </div>
          
          <div className="pagination-controls">
            <div className="items-per-page">
              <label>Rows per page:</label>
              <select
                value={itemsPerPage}
                onChange={(e) => {
                  setItemsPerPage(Number(e.target.value));
                  setCurrentPage(1);
                }}
              >
                <option value={5}>5</option>
                <option value={10}>10</option>
                <option value={20}>20</option>
                <option value={50}>50</option>
              </select>
            </div>

            <div className="pagination-buttons">
              <button
                className="pagination-btn"
                onClick={() => goToPage(1)}
                disabled={currentPage === 1}
              >
                «
              </button>
              <button
                className="pagination-btn"
                onClick={() => goToPage(currentPage - 1)}
                disabled={currentPage === 1}
              >
                ‹
              </button>
              <span className="pagination-current-page">
                Page {currentPage} of {totalPages || 1}
              </span>
              <button
                className="pagination-btn"
                onClick={() => goToPage(currentPage + 1)}
                disabled={currentPage === totalPages || totalPages === 0}
              >
                ›
              </button>
              <button
                className="pagination-btn"
                onClick={() => goToPage(totalPages)}
                disabled={currentPage === totalPages || totalPages === 0}
              >
                »
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
