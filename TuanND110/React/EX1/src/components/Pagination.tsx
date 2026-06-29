import React from "react";

interface PaginationProps {
  currentPage: number;
  totalItems: number;
  itemsPerPage: number;
  onPageChange: (page: number) => void;
}

export const Pagination: React.FC<PaginationProps> = ({
  currentPage,
  totalItems,
  itemsPerPage,
  onPageChange,
}) => {
  const totalPages = Math.ceil(totalItems / itemsPerPage);

  if (totalPages <= 1) {
    return (
      <div className="pagination">
        <span className="pagination-info">Hiển thị {totalItems} bản ghi</span>
      </div>
    );
  }

  const startItem = (currentPage - 1) * itemsPerPage + 1;
  const endItem = Math.min(currentPage * itemsPerPage, totalItems);

  // Generate page numbers to display
  const pageNumbers: number[] = [];
  for (let i = 1; i <= totalPages; i++) {
    pageNumbers.push(i);
  }

  return (
    <div className="pagination">
      <span className="pagination-info">
        Hiển thị {startItem}-{endItem} trong số {totalItems} bản ghi
      </span>
      <div className="pagination-controls">
        <button
          type="button"
          className="page-btn"
          onClick={() => onPageChange(1)}
          disabled={currentPage === 1}
          aria-label="Trang đầu"
          title="Trang đầu"
        >
          <span className="material-symbols-outlined text-sm">first_page</span>
        </button>
        <button
          type="button"
          className="page-btn"
          onClick={() => onPageChange(currentPage - 1)}
          disabled={currentPage === 1}
          aria-label="Trang trước"
          title="Trang trước"
        >
          <span className="material-symbols-outlined text-sm">chevron_left</span>
        </button>

        {pageNumbers.map((number) => (
          <button
            key={number}
            type="button"
            className={`page-btn ${currentPage === number ? "active" : ""}`}
            onClick={() => onPageChange(number)}
          >
            {number}
          </button>
        ))}

        <button
          type="button"
          className="page-btn"
          onClick={() => onPageChange(currentPage + 1)}
          disabled={currentPage === totalPages}
          aria-label="Trang sau"
          title="Trang sau"
        >
          <span className="material-symbols-outlined text-sm">chevron_right</span>
        </button>
        <button
          type="button"
          className="page-btn"
          onClick={() => onPageChange(totalPages)}
          disabled={currentPage === totalPages}
          aria-label="Trang cuối"
          title="Trang cuối"
        >
          <span className="material-symbols-outlined text-sm">last_page</span>
        </button>
      </div>
    </div>
  );
};
