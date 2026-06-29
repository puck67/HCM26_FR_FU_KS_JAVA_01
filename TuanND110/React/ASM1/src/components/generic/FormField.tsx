import React from 'react';
import { useField } from 'formik';
import { AlertTriangle } from 'lucide-react';

// ─── Types ────────────────────────────────────────────────────────────────────

export type FormFieldElement = 'input' | 'textarea';

export interface FormFieldProps {
  /** Formik field name (also used as id) */
  name: string;
  /** Visible label text */
  label: string;
  /** HTML element to render */
  as?: FormFieldElement;
  /** Input type (only for as="input") */
  type?: string;
  /** Placeholder text */
  placeholder?: string;
  /** Mark field as required (shows red asterisk) */
  required?: boolean;
  /** Textarea row count */
  rows?: number;
  /** Maximum character length — enforces maxLength + shows live counter */
  maxLength?: number;
  /** Extra class names applied to the wrapper */
  className?: string;
}

// ─── Styles ───────────────────────────────────────────────────────────────────

const BASE_INPUT =
  'w-full px-3 py-2 rounded-md border bg-white text-slate-900 placeholder-slate-400 ' +
  'focus:outline-none transition-all duration-150 text-sm';

const BORDER_NORMAL = 'border-slate-300 focus:border-blue-500 focus:ring-1 focus:ring-blue-500';
const BORDER_ERROR  = 'border-red-400 focus:border-red-500 focus:ring-1 focus:ring-red-500 bg-red-50/20';

// ─── Component ────────────────────────────────────────────────────────────────

/**
 * Generic Formik-connected form field.
 * Renders an <input> or <textarea>, with validation error display
 * and optional character counter when maxLength is supplied.
 */
export const FormField: React.FC<FormFieldProps> = ({
  label,
  as = 'input',
  type = 'text',
  required = false,
  rows = 4,
  maxLength,
  className = '',
  placeholder,
  ...props
}) => {
  const [field, meta] = useField(props.name);
  const hasError     = Boolean(meta.touched && meta.error);
  const currentLen   = String(field.value ?? '').length;
  const atLimit      = maxLength !== undefined && currentLen >= maxLength;

  const borderClass = hasError ? BORDER_ERROR : BORDER_NORMAL;

  return (
    <div className={`flex flex-col gap-1.5 w-full ${className}`}>
      {/* Label row */}
      <div className="flex items-center justify-between">
        <label
          htmlFor={props.name}
          className="text-xs font-semibold uppercase tracking-wider text-slate-700"
        >
          {label}
          {required && <span className="text-rose-500 ml-0.5">*</span>}
        </label>

        {maxLength !== undefined && (
          <span
            className={`text-[10px] font-semibold tabular-nums ${
              atLimit ? 'text-red-500' : 'text-slate-400'
            }`}
          >
            {currentLen} / {maxLength}
          </span>
        )}
      </div>

      {/* Field */}
      {as === 'textarea' ? (
        <textarea
          id={props.name}
          rows={rows}
          maxLength={maxLength}
          placeholder={placeholder}
          className={`${BASE_INPUT} resize-none ${borderClass}`}
          {...field}
        />
      ) : (
        <input
          id={props.name}
          type={type}
          maxLength={maxLength}
          placeholder={placeholder}
          className={`${BASE_INPUT} ${borderClass}`}
          {...field}
        />
      )}

      {/* Validation error */}
      {hasError && (
        <p className="flex items-center gap-1.5 text-xs font-medium text-red-500 animate-fadeIn">
          <AlertTriangle size={12} className="shrink-0" />
          {meta.error}
        </p>
      )}
    </div>
  );
};
