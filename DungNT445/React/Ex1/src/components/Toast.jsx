import React from 'react';

export function Toast({ toasts, removeToast }) {
  if (!toasts || toasts.length === 0) return null;

  return (
    <div className="toast-container">
      {toasts.map((toast) => {
        let toastClass = 'toast';
        if (toast.type) toastClass += ` toast-${toast.type}`;

        return (
          <div key={toast.id} className={toastClass}>
            <div>
              <div className="toast-message">{toast.message}</div>
              {toast.subtext && <div className="toast-subtext">{toast.subtext}</div>}
            </div>
            <button className="toast-close" onClick={() => removeToast(toast.id)}>
              &times;
            </button>
          </div>
        );
      })}
    </div>
  );
}
