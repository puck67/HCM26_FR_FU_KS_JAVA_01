import { Skeleton } from './Skeleton';

const SKELETON_CARD_COUNT = 6;

export const PageSkeleton = () => (
  <div
    className="mx-auto max-w-7xl px-4 py-8 sm:px-6 lg:px-8"
    aria-label="Loading page"
    aria-busy="true"
  >
    <Skeleton className="h-8 w-48" />
    <Skeleton className="mt-2 h-4 w-64" />
    <div className="mt-8 grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
      {Array.from({ length: SKELETON_CARD_COUNT }, (_, i) => (
        <div
          key={i}
          className="rounded-xl border border-gray-200 bg-white p-5 shadow-sm"
        >
          <Skeleton className="h-5 w-3/4" />
          <Skeleton className="mt-2 h-4 w-full" />
          <Skeleton className="mt-1 h-4 w-2/3" />
          <div className="mt-4 flex gap-2">
            <Skeleton className="h-8 w-14" />
            <Skeleton className="h-8 w-14" />
          </div>
        </div>
      ))}
    </div>
  </div>
);
