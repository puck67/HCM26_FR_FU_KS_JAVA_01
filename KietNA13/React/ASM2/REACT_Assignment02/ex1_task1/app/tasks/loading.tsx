const SKELETON_ROWS = ['sk-1', 'sk-2', 'sk-3', 'sk-4', 'sk-5'] as const;

export default function Loading() {
  return (
    <div className="flex flex-col gap-6">
      {/* Header skeleton */}
      <div className="flex items-center justify-between">
        <div className="space-y-2">
          <div className="h-8 w-36 bg-slate-800 rounded-lg animate-pulse" />
          <div className="h-4 w-20 bg-slate-800 rounded animate-pulse" />
        </div>
        <div className="h-9 w-24 bg-slate-800 rounded-xl animate-pulse" />
      </div>

      {/* Row skeletons */}
      <div className="flex flex-col gap-3">
        {SKELETON_ROWS.map((id) => (
          <div
            key={id}
            className="flex items-center gap-4 p-4 bg-slate-800 border border-slate-700 rounded-xl animate-pulse"
          >
            <div className="w-3 h-3 rounded-full bg-slate-700 flex-shrink-0" />
            <div className="flex-1 space-y-2">
              <div className="h-4 bg-slate-700 rounded w-2/3" />
              <div className="h-3 bg-slate-700 rounded w-1/2" />
            </div>
            <div className="flex gap-2">
              <div className="h-7 w-24 bg-slate-700 rounded-lg" />
              <div className="h-7 w-12 bg-slate-700 rounded-lg" />
              <div className="h-7 w-16 bg-slate-700 rounded-lg" />
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
