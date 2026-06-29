import React from 'react';
import { useField } from 'formik';

interface FormFieldProps<T extends string> {
  label: string;
  name: T;
  type?: string;
  placeholder?: string;
  isTextArea?: boolean;
}

export function FormField<T extends string>({
  label,
  name,
  type = 'text',
  placeholder,
  isTextArea = false,
}: FormFieldProps<T>) {
  const [field, meta] = useField(name);

  return (
    <div className="mb-4">
      <label htmlFor={name} className="block text-sm font-medium text-slate-300 mb-1">
        {label}
      </label>
      {isTextArea ? (
        <textarea
          {...field}
          id={name}
          placeholder={placeholder}
          className={`w-full px-4 py-2 bg-slate-900 border rounded-lg text-slate-100 focus:outline-none focus:ring-2 focus:ring-violet-500 transition duration-150 ${
            meta.touched && meta.error ? 'border-red-500 focus:ring-red-500' : 'border-slate-700'
          }`}
          rows={4}
        />
      ) : (
        <input
          {...field}
          id={name}
          type={type}
          placeholder={placeholder}
          className={`w-full px-4 py-2 bg-slate-900 border rounded-lg text-slate-100 focus:outline-none focus:ring-2 focus:ring-violet-500 transition duration-150 ${
            meta.touched && meta.error ? 'border-red-500 focus:ring-red-500' : 'border-slate-700'
          }`}
        />
      )}
      {meta.touched && meta.error ? (
        <div className="mt-1 text-xs text-red-400 font-semibold">{meta.error}</div>
      ) : null}
    </div>
  );
}
