import React from 'react';
import { GenericButton } from './GenericButton';

export interface GenericModalProps {
  isOpen: boolean;
  title: string;
  message: string;
  onConfirm: () => void;
  onCancel: () => void;
  confirmLabel?: string;
  cancelLabel?: string;
  loading?: boolean;
}

export const GenericModal: React.FC<GenericModalProps> = ({
  isOpen,
  title,
  message,
  onConfirm,
  onCancel,
  confirmLabel = 'Confirm',
  cancelLabel = 'Cancel',
  loading = false,
}) => {
  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/70 backdrop-blur-xs">
      <div className="bg-zinc-900 border border-zinc-800 rounded-lg p-6 max-w-md w-full shadow-2xl space-y-4">
        <h3 className="text-lg font-semibold text-zinc-100">{title}</h3>
        <p className="text-sm text-zinc-300">{message}</p>

        <div className="flex items-center justify-end gap-3 pt-3 border-t border-zinc-800">
          <GenericButton
            type="button"
            variant="outline"
            label={cancelLabel}
            onClick={onCancel}
            disabled={loading}
          />
          <GenericButton
            type="button"
            variant="danger"
            label={confirmLabel}
            onClick={onConfirm}
            loading={loading}
          />
        </div>
      </div>
    </div>
  );
};
