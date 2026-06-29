import React from 'react';
import { CheckCircle, AlertCircle, Info, AlertTriangle, X } from 'lucide-react';

export type AlertVariant = 'success' | 'error' | 'info' | 'warning';

export interface AlertProps {
  children:    React.ReactNode;
  variant?:    AlertVariant;
  title?:      string;
  /** Show dismiss button when provided */
  onDismiss?:  () => void;
  className?:  string;
}

const CONFIG: Record<AlertVariant, { wrapper: string; icon: React.ReactNode }> = {
  success: { wrapper: 'border-emerald-200 bg-emerald-50 text-emerald-800', icon: <CheckCircle  size={16} className="text-emerald-600 shrink-0" /> },
  error:   { wrapper: 'border-red-200     bg-red-50     text-red-800',     icon: <AlertCircle  size={16} className="text-red-600 shrink-0" /> },
  info:    { wrapper: 'border-blue-200    bg-blue-50    text-blue-800',    icon: <Info         size={16} className="text-blue-600 shrink-0" /> },
  warning: { wrapper: 'border-amber-200   bg-amber-50   text-amber-800',   icon: <AlertTriangle size={16} className="text-amber-600 shrink-0" /> },
};

/**
 * Generic Alert / inline banner with icon, optional title and dismiss button.
 */
export const Alert: React.FC<AlertProps> = ({
  children,
  variant   = 'info',
  title,
  onDismiss,
  className = '',
}) => {
  const { wrapper, icon } = CONFIG[variant];

  return (
    <div
      role="alert"
      className={[
        'flex items-start gap-3 px-4 py-3 rounded-lg border text-sm',
        wrapper,
        className,
      ].filter(Boolean).join(' ')}
    >
      <span className="mt-0.5">{icon}</span>
      <div className="flex-1 min-w-0">
        {title && <p className="font-semibold mb-0.5">{title}</p>}
        <div>{children}</div>
      </div>
      {onDismiss && (
        <button onClick={onDismiss} aria-label="Dismiss" className="shrink-0 p-0.5 hover:opacity-70 transition-opacity rounded">
          <X size={14} />
        </button>
      )}
    </div>
  );
};
