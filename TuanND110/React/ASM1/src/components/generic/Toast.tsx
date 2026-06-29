import React, { useEffect } from 'react';
import { CheckCircle, AlertCircle, Info, X } from 'lucide-react';

// ─── Types ────────────────────────────────────────────────────────────────────

export type ToastVariant = 'success' | 'error' | 'info';

export interface ToastProps {
  /** Notification message */
  message: string;
  /** Visual variant */
  variant?: ToastVariant;
  /** Called when the toast is dismissed (auto or manual) */
  onDismiss: () => void;
  /** Auto-dismiss delay in ms (default 3000). Pass 0 to disable. */
  duration?: number;
}

// ─── Constants ────────────────────────────────────────────────────────────────

const VARIANT_CONFIG: Record<
  ToastVariant,
  { border: string; icon: React.ReactNode }
> = {
  success: {
    border: 'border-emerald-200',
    icon: <CheckCircle size={18} className="text-emerald-600 shrink-0" />,
  },
  error: {
    border: 'border-red-200',
    icon: <AlertCircle size={18} className="text-red-600 shrink-0" />,
  },
  info: {
    border: 'border-blue-200',
    icon: <Info size={18} className="text-blue-600 shrink-0" />,
  },
};

// ─── Component ────────────────────────────────────────────────────────────────

/**
 * Toast notification.
 * Self-dismisses after `duration` ms. Renders fixed bottom-right.
 * Replaces native `window.alert()` for user feedback.
 */
export const Toast: React.FC<ToastProps> = ({
  message,
  variant = 'success',
  onDismiss,
  duration = 3000,
}) => {
  useEffect(() => {
    if (!duration) return;
    const timer = setTimeout(onDismiss, duration);
    return () => clearTimeout(timer);
  }, [onDismiss, duration]);

  const config = VARIANT_CONFIG[variant];

  return (
    <div
      role="status"
      aria-live="polite"
      className={[
        'fixed bottom-5 right-5 z-[60]',
        'flex items-center gap-3 px-4 py-3',
        'bg-white border rounded-lg shadow-lg max-w-sm',
        'animate-slideIn',
        config.border,
      ].join(' ')}
    >
      {config.icon}
      <span className="text-sm font-medium text-slate-800 flex-1 pr-1">
        {message}
      </span>
      <button
        onClick={onDismiss}
        aria-label="Dismiss notification"
        className="shrink-0 p-0.5 rounded hover:bg-slate-100 text-slate-400 hover:text-slate-600 transition-colors"
      >
        <X size={13} />
      </button>
    </div>
  );
};
