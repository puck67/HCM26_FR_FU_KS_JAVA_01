import React from 'react';

export const Spinner: React.FC = () => {
  return (
    <div className="flex flex-col items-center justify-center py-16">
      <div className="w-10 h-10 border-3 border-red-200 border-t-red-900 rounded-full animate-spin"></div>
      <p className="mt-3 text-xs font-medium text-slate-500">Đang tải dữ liệu...</p>
    </div>
  );
};
