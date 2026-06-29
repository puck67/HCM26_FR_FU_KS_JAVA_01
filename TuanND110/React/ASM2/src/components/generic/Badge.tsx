import React from 'react';
import type { TaskStatus } from '../../types/task';

// ─── Types ────────────────────────────────────────────────────────────────────

export type BadgeVariant = TaskStatus | 'default';

export interface BadgeProps {
  /** Display label */
  label: string;
  /** Visual variant — maps directly to TaskStatus values */
  variant?: BadgeVariant;
  /** Extra class names */
  className?: string;
}

// ─── Constants ────────────────────────────────────────────────────────────────

const VARIANT_STYLES: Record<BadgeVariant, string> = {
  todo:        'border-amber-300  bg-amber-50   text-amber-700',
  in_progress: 'border-blue-300   bg-blue-50    text-blue-700',
  completed:   'border-emerald-300 bg-emerald-50 text-emerald-700',
  default:     'border-slate-300  bg-slate-50   text-slate-600',
};

const STATUS_LABELS: Record<TaskStatus, string> = {
  todo:        'To Do',
  in_progress: 'In Progress',
  completed:   'Completed',
};

// ─── Component ────────────────────────────────────────────────────────────────

/**
 * Generic Badge / status chip.
 * Pass a `TaskStatus` as variant for automatic colour mapping.
 */
export const Badge: React.FC<BadgeProps> = ({
  label,
  variant = 'default',
  className = '',
}) => {
  return (
    <span
      className={[
        'inline-flex items-center px-2 py-0.5 rounded-full border',
        'text-[10px] font-semibold uppercase tracking-wider whitespace-nowrap',
        VARIANT_STYLES[variant],
        className,
      ]
        .filter(Boolean)
        .join(' ')}
    >
      {label}
    </span>
  );
};

/** Helper — converts a TaskStatus to the correct Badge props */
export function statusToBadgeProps(status: TaskStatus): Pick<BadgeProps, 'label' | 'variant'> {
  return { label: STATUS_LABELS[status], variant: status };
}
