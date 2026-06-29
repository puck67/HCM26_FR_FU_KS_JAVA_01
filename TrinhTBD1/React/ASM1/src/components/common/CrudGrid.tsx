import React, { type ReactNode } from 'react';
import type { Identifiable } from '../../types/crud';
import { Spinner } from './Spinner';
import { ErrorView } from './ErrorView';
import { Button } from './Button';

interface CrudGridProps<T extends Identifiable> {
  items: T[];
  loading: boolean;
  error: string | null;
  entityName: string;
  renderItem: (item: T) => ReactNode;
  onRetry?: () => void;
  onOpenCreate?: () => void;
}

export function CrudGrid<T extends Identifiable>({
  items,
  loading,
  error,
  entityName,
  renderItem,
  onRetry,
  onOpenCreate
}: CrudGridProps<T>) {
  if (loading) {
    return <Spinner />;
  }

  if (error) {
    return <ErrorView message={error} onRetry={onRetry} />;
  }

  if (!items || items.length === 0) {
    return (
      <div className="bg-white rounded-2xl border border-slate-200 p-12 text-center my-6">
        <div className="w-16 h-16 bg-slate-100 text-slate-400 rounded-full flex items-center justify-center mx-auto mb-4">
          <svg className="w-8 h-8" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
          </svg>
        </div>
        <h3 className="text-lg font-bold text-slate-800">No {entityName.toLowerCase()}s found</h3>
        <p className="text-sm text-slate-500 max-w-sm mx-auto mt-1">
          The list is currently empty or no items matched your filter.
        </p>
        {onOpenCreate && (
          <div className="mt-6">
            <Button
              onClick={onOpenCreate}
              icon={
                <svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 4v16m8-8H4" />
                </svg>
              }
            >
              Add {entityName}
            </Button>
          </div>
        )}
      </div>
    );
  }

  return (
    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
      {items.map((item) => (
        <React.Fragment key={item.id}>{renderItem(item)}</React.Fragment>
      ))}
    </div>
  );
}
