import React from 'react';

export interface GenericBadgeProps {
  label: string;
  variant?: 'completed' | 'pending' | 'neutral';
  className?: string;
}

export const GenericBadge: React.FC<GenericBadgeProps> = ({
  label,
  variant = 'neutral',
  className = '',
}) => {
  const variantStyles = {
    completed: 'bg-[#540015]/60 text-red-200 border border-[#800020]/40',
    pending: 'bg-zinc-800 text-zinc-400 border border-zinc-700',
    neutral: 'bg-zinc-800 text-zinc-300 border border-zinc-700'
  };

  return (
    <span className={`inline-block px-2.5 py-0.5 rounded text-xs font-medium tracking-wide ${variantStyles[variant]} ${className}`}>
      {label}
    </span>
  );
};
