import React from 'react';

interface SkeletonProps {
  className?: string;
}

export const Skeleton: React.FC<SkeletonProps> = ({ className = '' }) => {
  return (
    <div className={`animate-pulse bg-slate-800/40 rounded-lg ${className}`}></div>
  );
};

export const TaskCardSkeleton: React.FC = () => {
  return (
    <div className="glass-card p-6 rounded-2xl flex flex-col gap-4 border border-white/5 relative overflow-hidden">
      {/* Glow Effect */}
      <div className="absolute -inset-px bg-gradient-to-r from-violet-500/5 to-indigo-500/5 rounded-2xl pointer-events-none" />
      
      <div className="flex items-start justify-between gap-4">
        <Skeleton className="h-6 w-3/4" />
        <Skeleton className="h-5 w-20 rounded-full" />
      </div>
      
      <Skeleton className="h-4 w-full mt-2" />
      <Skeleton className="h-4 w-5/6" />
      
      <div className="mt-4 pt-4 border-t border-white/5 flex items-center justify-between">
        <Skeleton className="h-3 w-28" />
        <div className="flex gap-2">
          <Skeleton className="h-8 w-8 rounded-lg" />
          <Skeleton className="h-8 w-8 rounded-lg" />
        </div>
      </div>
    </div>
  );
};

export const DashboardStatsSkeleton: React.FC = () => {
  return (
    <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
      {[1, 2, 3].map((i) => (
        <div key={i} className="glass-card p-6 rounded-2xl border border-white/5 flex items-center gap-4">
          <Skeleton className="h-12 w-12 rounded-xl" />
          <div className="flex-1">
            <Skeleton className="h-3 w-16 mb-2" />
            <Skeleton className="h-8 w-12" />
          </div>
        </div>
      ))}
    </div>
  );
};
