import React from 'react';
import { Spinner } from './Spinner.tsx';

// ─── Types ────────────────────────────────────────────────────────────────────

export interface ListViewProps<TItem> {
  /** Array of items to render */
  items: TItem[];
  /** Unique key extractor for each item */
  keyExtractor: (item: TItem) => string;
  /** Render function for each item */
  renderItem: (item: TItem, index: number) => React.ReactNode;
  /** Show loading spinner */
  isLoading?: boolean;
  /** Message when items array is empty */
  emptyMessage?: string;
  /** Content slot above the list (e.g. search/filter bar) */
  headerSlot?: React.ReactNode;
  /** Extra class names on the wrapper */
  className?: string;
}

// ─── Component ────────────────────────────────────────────────────────────────

/**
 * Generic ListView<T>.
 * Handles loading state, empty state, and item rendering via render prop.
 * Fully type-safe with TypeScript generics.
 */
export function ListView<TItem>({
  items,
  keyExtractor,
  renderItem,
  isLoading = false,
  emptyMessage = 'No items found.',
  headerSlot,
  className = '',
}: ListViewProps<TItem>) {
  return (
    <div className={`flex flex-col gap-4 ${className}`}>
      {headerSlot && <div>{headerSlot}</div>}

      {isLoading ? (
        <div className="flex justify-center items-center py-16">
          <Spinner size="lg" />
        </div>
      ) : items.length === 0 ? (
        <div className="flex flex-col items-center justify-center py-16 text-slate-400 gap-3">
          <svg className="w-12 h-12 text-slate-300" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5}
              d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" />
          </svg>
          <p className="text-sm font-medium">{emptyMessage}</p>
        </div>
      ) : (
        <div className="flex flex-col gap-3">
          {items.map((item, index) => (
            <React.Fragment key={keyExtractor(item)}>
              {renderItem(item, index)}
            </React.Fragment>
          ))}
        </div>
      )}
    </div>
  );
}
