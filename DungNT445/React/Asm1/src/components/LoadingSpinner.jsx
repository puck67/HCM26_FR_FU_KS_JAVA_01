import React from 'react';

export const LoadingSpinner = ({ message = 'Loading tasks...' }) => {
  return (
    <div className="flex flex-col items-center justify-center py-16 px-4">
      <div className="relative w-16 h-16">
        <div className="absolute inset-0 rounded-full border-4 border-indigo-100 animate-pulse"></div>
        <div className="absolute inset-0 rounded-full border-4 border-indigo-600 border-t-transparent animate-spin"></div>
      </div>
      <p className="mt-4 text-slate-500 font-medium text-sm animate-pulse">{message}</p>
    </div>
  );
};
