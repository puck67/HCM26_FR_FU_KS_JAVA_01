import React from 'react';
import { AlertCircle, RotateCcw } from 'lucide-react';

export const ErrorAlert = ({ message, onRetry }) => {
  return (
    <div className="max-w-md mx-auto my-8 p-6 bg-rose-50 border border-rose-200 rounded-2xl shadow-sm text-center">
      <div className="inline-flex p-3 bg-rose-100 rounded-full text-rose-600 mb-4">
        <AlertCircle className="w-8 h-8" />
      </div>
      <h3 className="text-lg font-semibold text-rose-900 mb-2">Failed to Load Data</h3>
      <p className="text-sm text-rose-700 mb-6">{message}</p>
      {onRetry && (
        <button
          onClick={onRetry}
          className="inline-flex items-center gap-2 px-4 py-2 bg-rose-600 hover:bg-rose-700 text-white rounded-lg text-sm font-medium shadow-sm transition-colors cursor-pointer"
        >
          <RotateCcw className="w-4 h-4" />
          <span>Retry Loading</span>
        </button>
      )}
    </div>
  );
};
