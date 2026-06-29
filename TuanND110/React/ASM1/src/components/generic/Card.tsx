import React from 'react';

// ─── Types ────────────────────────────────────────────────────────────────────

export interface CardProps {
  /** Card body content */
  children: React.ReactNode;
  /** Extra class names */
  className?: string;
  /** Optional click handler — adds hover/cursor styles when provided */
  onClick?: () => void;
  /** Render without default padding */
  noPadding?: boolean;
  /** HTML element tag to render as */
  as?: React.ElementType;
}

// ─── Component ────────────────────────────────────────────────────────────────

/**
 * Generic Card container.
 * White background, slate border, subtle shadow.
 * Adds hover lift effect when `onClick` is provided.
 */
export const Card: React.FC<CardProps> = ({
  children,
  className = '',
  onClick,
  noPadding = false,
  as: Tag = 'div',
}) => {
  const interactive = Boolean(onClick);

  return (
    <Tag
      onClick={onClick}
      className={[
        'rounded-xl bg-white border border-slate-200',
        'shadow-sm transition-all duration-150',
        interactive
          ? 'cursor-pointer hover:shadow-md hover:-translate-y-0.5 hover:border-slate-300 active:translate-y-0'
          : '',
        noPadding ? '' : 'p-5',
        className,
      ]
        .filter(Boolean)
        .join(' ')}
    >
      {children}
    </Tag>
  );
};
