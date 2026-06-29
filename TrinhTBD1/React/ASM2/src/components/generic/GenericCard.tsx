import React from 'react';

export interface GenericCardProps {
  children: React.ReactNode;
  className?: string;
  title?: string;
}

export const GenericCard: React.FC<GenericCardProps> = ({
  children,
  className = '',
  title,
}) => {
  return (
    <div className={`bg-zinc-900/80 border border-zinc-800 rounded-lg p-6 ${className}`}>
      {title && (
        <div className="mb-5 pb-3 border-b border-zinc-800">
          <h2 className="text-lg font-semibold text-zinc-100">{title}</h2>
        </div>
      )}
      {children}
    </div>
  );
};
