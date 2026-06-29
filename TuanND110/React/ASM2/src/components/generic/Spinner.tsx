import React from 'react';

export type SpinnerSize = 'sm' | 'md' | 'lg' | 'xl';

export interface SpinnerProps {
  size?:      SpinnerSize;
  label?:     string;
  className?: string;
}

const SIZE_MAP: Record<SpinnerSize, string> = {
  sm: 'w-4  h-4  border-2',
  md: 'w-6  h-6  border-2',
  lg: 'w-9  h-9  border-[3px]',
  xl: 'w-14 h-14 border-4',
};

/**
 * Generic Spinner / loading indicator.
 */
export const Spinner: React.FC<SpinnerProps> = ({
  size      = 'md',
  label     = 'Loading…',
  className = '',
}) => (
  <div role="status" aria-label={label} className={`inline-block ${className}`}>
    <div className={`${SIZE_MAP[size]} rounded-full border-blue-600 border-t-transparent animate-spin`} />
    <span className="sr-only">{label}</span>
  </div>
);
