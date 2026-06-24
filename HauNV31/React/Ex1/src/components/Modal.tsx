import React from 'react';
import { createPortal } from 'react-dom';

export type ModalProps = {
  isOpen: boolean;
  title: string;
  onClose: () => void;
  onConfirm: () => void;
  children: React.ReactNode;
  confirmText?: string;
  type?: 'primary' | 'success' | 'danger';
  showCancel?: boolean;
};

export function Modal({ isOpen, title, onClose, onConfirm, children, confirmText = "Confirm", type = "primary", showCancel = true }: ModalProps) {
  if (!isOpen) return null;

  return createPortal(
    <div className="vr-modal-overlay">
      <div className="vr-modal-content">
        <h3>{title}</h3>
        <div className="vr-modal-body">
          {children}
        </div>
        <div className="vr-modal-footer">
          {showCancel && <button className="btn btn-ghost" onClick={onClose}>Cancel</button>}
          <button className={`btn btn-${type}`} onClick={onConfirm}>{confirmText}</button>
        </div>
      </div>
    </div>,
    document.body
  );
}
