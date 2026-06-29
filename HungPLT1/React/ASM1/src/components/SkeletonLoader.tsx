import React from 'react';

export const SkeletonLoader: React.FC = () => {
  return (
    <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
      {[1, 2, 3, 4].map((i) => (
        <div key={i} className="bg-slate-900 border border-slate-800 rounded-xl p-5 animate-pulse">
          <div className="flex justify-between items-start mb-3">
            <div className="h-6 w-32 bg-slate-800 rounded-lg"></div>
            <div className="h-5 w-20 bg-slate-800 rounded-full"></div>
          </div>
          <div className="space-y-2 mb-4">
            <div className="h-4 w-full bg-slate-800 rounded-md"></div>
            <div className="h-4 w-5/6 bg-slate-800 rounded-md"></div>
          </div>
          <div className="flex justify-between items-center pt-3 border-t border-slate-800/50">
            <div className="h-4 w-24 bg-slate-800 rounded-md"></div>
            <div className="h-8 w-16 bg-slate-800 rounded-lg"></div>
          </div>
        </div>
      ))}
    </div>
  );
};
