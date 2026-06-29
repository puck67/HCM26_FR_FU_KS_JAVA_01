import React from 'react';
import Link from 'next/link';
import { GenericButton } from './GenericButton';

export interface GenericToolbarProps {
  title: string;
  entityName?: string;
  createUrl?: string;
  createLabel?: string;
  extraActions?: React.ReactNode;
}

export const GenericToolbar: React.FC<GenericToolbarProps> = ({
  title,
  entityName = 'Item',
  createUrl,
  createLabel,
  extraActions,
}) => {
  const defaultCreateLabel = createLabel || `Add ${entityName}`;

  return (
    <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4 pb-4 mb-6 border-b border-zinc-800">
      <h1 className="text-2xl font-bold text-zinc-100 tracking-tight">
        {title}
      </h1>
      
      <div className="flex items-center gap-3">
        {extraActions}
        {createUrl && (
          <Link href={createUrl}>
            <GenericButton
              variant="primary"
              size="md"
              label={defaultCreateLabel}
            />
          </Link>
        )}
      </div>
    </div>
  );
};
