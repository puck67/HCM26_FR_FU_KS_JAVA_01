import { cn } from '../utils/cn';

export interface BadgeProps {
  completed: boolean;
  className?: string;
}

export const Badge = ({ completed, className }: BadgeProps) => (
  <span
    className={cn(
      'inline-flex items-center rounded-full px-2.5 py-0.5 text-xs font-medium',
      completed
        ? 'bg-emerald-100 text-emerald-800'
        : 'bg-amber-100 text-amber-800',
      className,
    )}
  >
    {completed ? 'Completed' : 'Pending'}
  </span>
);
