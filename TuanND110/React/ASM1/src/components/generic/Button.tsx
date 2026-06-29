import React from 'react';
import { Loader2 } from 'lucide-react';

// ─── Types ────────────────────────────────────────────────────────────────────

export type ButtonVariant = 'primary' | 'secondary' | 'danger' | 'success' | 'outline' | 'ghost';
export type ButtonSize = 'sm' | 'md' | 'lg';

export interface ButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
  /** Visual style variant */
  variant?: ButtonVariant;
  /** Size preset */
  size?: ButtonSize;
  /** Shows spinner and disables interaction */
  isLoading?: boolean;
  /** Icon rendered before children */
  leadingIcon?: React.ReactNode;
  /** Icon rendered after children */
  trailingIcon?: React.ReactNode;
  /** Render as full-width block */
  fullWidth?: boolean;
}

// ─── Constants ────────────────────────────────────────────────────────────────

const VARIANT_STYLES: Record<ButtonVariant, string> = {
  primary:
    'bg-blue-600 hover:bg-blue-700 text-white shadow-sm focus:ring-blue-500',
  secondary:
    'bg-slate-100 hover:bg-slate-200 text-slate-700 border border-slate-200 focus:ring-slate-300',
  danger:
    'bg-red-600 hover:bg-red-700 text-white shadow-sm focus:ring-red-500',
  success:
    'bg-emerald-600 hover:bg-emerald-700 text-white shadow-sm focus:ring-emerald-500',
  outline:
    'bg-white border border-slate-300 hover:bg-slate-50 text-slate-700 focus:ring-slate-400',
  ghost:
    'bg-transparent hover:bg-slate-100 text-slate-600 focus:ring-slate-300',
};

const SIZE_STYLES: Record<ButtonSize, string> = {
  sm: 'px-3 py-1.5 text-xs',
  md: 'px-4 py-2 text-sm',
  lg: 'px-5 py-2.5 text-base',
};

// ─── Component ────────────────────────────────────────────────────────────────

/**
 * Generic Button component.
 * Supports multiple visual variants, sizes, loading state and icon slots.
 */
export const Button: React.FC<ButtonProps> = ({
  children,
  variant = 'primary',
  size = 'md',
  isLoading = false,
  leadingIcon,
  trailingIcon,
  fullWidth = false,
  className = '',
  disabled,
  ...rest
}) => {
  return (
    <button
      {...rest}
      disabled={disabled || isLoading}
      className={[
        'inline-flex items-center justify-center gap-2 rounded-md font-semibold',
        'transition-all duration-150 focus:outline-none focus:ring-2 focus:ring-offset-1',
        'disabled:opacity-50 disabled:cursor-not-allowed',
        VARIANT_STYLES[variant],
        SIZE_STYLES[size],
        fullWidth ? 'w-full' : '',
        className,
      ]
        .filter(Boolean)
        .join(' ')}
    >
      {isLoading ? (
        <Loader2 size={14} className="animate-spin shrink-0" />
      ) : (
        leadingIcon && <span className="shrink-0">{leadingIcon}</span>
      )}
      {children}
      {!isLoading && trailingIcon && (
        <span className="shrink-0">{trailingIcon}</span>
      )}
    </button>
  );
};
