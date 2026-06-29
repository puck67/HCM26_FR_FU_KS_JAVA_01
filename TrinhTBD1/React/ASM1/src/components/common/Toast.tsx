import React from 'react';

interface ToastProps {
  message: string | null;
  type?: 'success' | 'info' | 'error';
  onClose: () => void;
}

export const Toast: React.FC<ToastProps> = ({ message, type = 'success', onClose }) => {
  if (!message) return null;

  const typeStyles = {
    success: 'bg-slate-900 text-white border-slate-800',
    info: 'bg-red-900 text-white border-red-950',
    error: 'bg-red-800 text-white border-red-900'
  };

  return (
    <div className="fixed bottom-5 right-5 z-50 animate-bounce">
      <div className={`flex items-center justify-between px-4 py-3 rounded-xl shadow-lg border text-sm max-w-md space-x-3 ${typeStyles[type]}`}>
        <div className="flex items-center space-x-2">
          <svg className="w-5 h-5 text-emerald-400 shrink-0" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 13l4 4L19 7" />
          </svg>
          <span className="font-medium">{message}</span>
        </div>
        <button
          type="button"
          onClick={onClose}
          className="text-slate-400 hover:text-white p-1 rounded-md transition-colors cursor-pointer ml-2"
        >
          <svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
          </svg>
        </button>
      </div>
    </div>
  );
};

export default Toast;
