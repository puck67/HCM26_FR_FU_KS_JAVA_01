import React from 'react';

export interface CardProps {
  children:   React.ReactNode;
  className?: string;
  onClick?:   () => void;
  /** Remove default padding */
  noPadding?: boolean;
  as?:        React.ElementType;
}

/**
 * Generic Card — white background, slate border, subtle shadow.
 * Adds hover lift when onClick is provided.
 */
export const Card: React.FC<CardProps> = ({
  children,
  className = '',
  onClick,
  noPadding = false,
  as: Tag   = 'div',
}) => (
  <Tag
    onClick={onClick}
    className={[
      'rounded-xl bg-white border border-slate-200 shadow-sm transition-all duration-150',
      onClick
        ? 'cursor-pointer hover:shadow-md hover:-translate-y-0.5 hover:border-slate-300 active:translate-y-0'
        : '',
      noPadding ? '' : 'p-5',
      className,
    ].filter(Boolean).join(' ')}
  >
    {children}
  </Tag>
);
