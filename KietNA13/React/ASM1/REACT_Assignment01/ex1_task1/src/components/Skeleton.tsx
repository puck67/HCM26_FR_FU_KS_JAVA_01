import { cn } from '../utils/cn';

export interface SkeletonProps {
  className?: string;
}

export const Skeleton = ({ className }: SkeletonProps) => (
  <div
    className={cn('animate-pulse rounded-md bg-gray-200', className)}
    aria-hidden="true"
  />
);
