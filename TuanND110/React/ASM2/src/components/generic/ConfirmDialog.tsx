import React from 'react';
import { AlertTriangle } from 'lucide-react';
import { Modal } from './Modal.tsx';
import { Button } from './Button.tsx';

// ─── Types ────────────────────────────────────────────────────────────────────

export interface ConfirmDialogProps {
  isOpen: boolean;
  title?: string;
  message: string;
  confirmLabel?: string;
  cancelLabel?: string;
  confirmVariant?: 'danger' | 'primary' | 'success';
  isConfirmLoading?: boolean;
  onConfirm: () => void;
  onCancel: () => void;
}

// ─── Component ────────────────────────────────────────────────────────────────

/**
 * ConfirmDialog — built on Modal.
 * Replaces native window.confirm() with an accessible branded dialog.
 */
export const ConfirmDialog: React.FC<ConfirmDialogProps> = ({
  isOpen,
  title = 'Confirm Action',
  message,
  confirmLabel = 'Confirm',
  cancelLabel = 'Cancel',
  confirmVariant = 'danger',
  isConfirmLoading = false,
  onConfirm,
  onCancel,
}) => {
  return (
    <Modal
      isOpen={isOpen}
      onClose={onCancel}
      size="sm"
      disableClose={isConfirmLoading}
      footerSlot={
        <>
          <Button variant="secondary" size="sm" onClick={onCancel} disabled={isConfirmLoading}>
            {cancelLabel}
          </Button>
          <Button variant={confirmVariant} size="sm" isLoading={isConfirmLoading} onClick={onConfirm}>
            {confirmLabel}
          </Button>
        </>
      }
    >
      <div className="flex items-start gap-3">
        <div className="shrink-0 p-2 rounded-full bg-red-50 border border-red-100">
          <AlertTriangle size={18} className="text-red-600" />
        </div>
        <div>
          <p className="font-bold text-slate-900 text-sm">{title}</p>
          <p className="mt-1 text-sm text-slate-600 leading-relaxed">{message}</p>
        </div>
      </div>
    </Modal>
  );
};
