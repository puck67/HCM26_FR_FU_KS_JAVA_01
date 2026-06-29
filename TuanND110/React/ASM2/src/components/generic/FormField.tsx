import React from 'react';
import { useField } from 'formik';

interface FormFieldProps {
  label: string;
  name: string;
  type?: string;
  as?: 'input' | 'textarea';
  placeholder?: string;
  rows?: number;
  required?: boolean;
  className?: string;
  maxLength?: number;
}

export const FormField: React.FC<FormFieldProps> = ({
  label,
  as = 'input',
  required = false,
  className = '',
  rows = 4,
  maxLength,
  ...props
}) => {
  const [field, meta] = useField(props.name);
  const hasError = meta.touched && meta.error;
  const currentLength = String(field.value || '').length;

  const inputBaseStyle = 'w-full px-3 py-2 rounded-md border bg-white text-slate-900 placeholder-slate-400 focus:outline-none transition-all duration-150 text-sm';
  const normalBorder = 'border-slate-300 focus:border-blue-500 focus:ring-1 focus:ring-blue-500';
  const errorBorder = 'border-red-500 focus:border-red-500 focus:ring-1 focus:ring-red-500 bg-red-50/10';

  return (
    <div className={`flex flex-col gap-1.5 w-full ${className}`}>
      <div className="flex justify-between items-center">
        <label htmlFor={props.name} className="text-xs font-semibold uppercase tracking-wider text-slate-700">
          {label} {required && <span className="text-rose-500">*</span>}
        </label>
        {maxLength && (
          <span className={`text-[10px] font-semibold tracking-wide ${currentLength >= maxLength ? 'text-red-500' : 'text-slate-400'}`}>
            {currentLength} / {maxLength}
          </span>
        )}
      </div>

      {as === 'textarea' ? (
        <textarea
          id={props.name}
          rows={rows}
          maxLength={maxLength}
          className={`${inputBaseStyle} resize-none ${hasError ? errorBorder : normalBorder}`}
          {...field}
          placeholder={props.placeholder}
        />
      ) : (
        <input
          id={props.name}
          type={props.type || 'text'}
          maxLength={maxLength}
          className={`${inputBaseStyle} ${hasError ? errorBorder : normalBorder}`}
          {...field}
          placeholder={props.placeholder}
        />
      )}

      {hasError ? (
        <div className="text-xs font-medium text-red-500 mt-0.5 flex items-center gap-1.5 animate-fadeIn">
          <svg className="w-3.5 h-3.5 shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z" />
          </svg>
          {meta.error}
        </div>
      ) : null}
    </div>
  );
};
