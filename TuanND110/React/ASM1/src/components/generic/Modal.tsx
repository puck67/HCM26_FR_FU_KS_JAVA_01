import React, { useEffect } from 'react';
import { X } from 'lucide-react';

// ─── Types ────────────────────────────────────────────────────────────────────

export interface ModalProps {
  /** Controls visibility */
  isOpen: boolean;
  /** Called when user requests to close (backdrop click, Escape key, X button) */
  onClose: () => void;
  /** Modal heading */
  title?: string;
  /** Width preset */
  size?: 'sm' | 'md' | 'lg' | 'xl';
  /** Prevent closing via backdrop click or Escape */
  disableClose?: boolean;
  /** Modal body content */
  children: React.ReactNode;
  /** Content rendered in the footer slot */
  footerSlot?: React.ReactNode;
}

// ─── Constants ────────────────────────────────────────────────────────────────

const SIZE_MAP: Record<NonNullable<ModalProps['size']>, string> = {
  sm: 'max-w-sm',
  md: 'max-w-md',
  lg: 'max-w-lg',
  xl: 'max-w-2xl',
};

// ─── Component ────────────────────────────────────────────────────────────────

/**
 * Generic Modal wrapper.
 * Handles backdrop, Escape key, scrolling, and focus trap basics.
 * Use as a building block for dialogs, drawers, etc.
 */
export const Modal: React.FC<ModalProps> = ({
  isOpen,
  onClose,
  title,
  size = 'md',
  disableClose = false,
  children,
  footerSlot,
}) => {
  // Close on Escape key
  useEffect(() => {
    if (!isOpen || disableClose) return;
    const handler = (e: KeyboardEvent) => {
      if (e.key === 'Escape') onClose();
    };
    document.addEventListener('keydown', handler);
    return () => document.removeEventListener('keydown', handler);
  }, [isOpen, disableClose, onClose]);

  // Prevent body scroll while open
  useEffect(() => {
    document.body.style.overflow = isOpen ? 'hidden' : '';
    return () => { document.body.style.overflow = ''; };
  }, [isOpen]);

  if (!isOpen) return null;

  return (
    <div
      role="dialog"
      aria-modal="true"
      aria-label={title}
      className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-slate-950/40 backdrop-blur-sm animate-fadeIn"
      onClick={disableClose ? undefined : onClose}
    >
      {/* Panel — stop click from bubbling to backdrop */}
      <div
        className={[
          'w-full bg-white border border-slate-200 rounded-xl shadow-xl',
          'overflow-hidden animate-scaleIn text-slate-900',
          SIZE_MAP[size],
        ].join(' ')}
        onClick={(e) => e.stopPropagation()}
      >
        {/* Header */}
        {(title || !disableClose) && (
          <div className="flex items-center justify-between px-5 py-4 border-b border-slate-100">
            {title && (
              <h2 className="text-base font-bold text-slate-900">{title}</h2>
            )}
            {!disableClose && (
              <button
                onClick={onClose}
                aria-label="Close modal"
                className="ml-auto p-1 hover:bg-slate-100 rounded text-slate-400 hover:text-slate-600 transition-colors"
              >
                <X size={16} />
              </button>
            )}
          </div>
        )}

        {/* Body */}
        <div className="p-5">{children}</div>

        {/* Footer */}
        {footerSlot && (
          <div className="flex justify-end gap-2.5 px-5 py-3 border-t border-slate-100 bg-slate-50/50">
            {footerSlot}
          </div>
        )}
      </div>
    </div>
  );
};
