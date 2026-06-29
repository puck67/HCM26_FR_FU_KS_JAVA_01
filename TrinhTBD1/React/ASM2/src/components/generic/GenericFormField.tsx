import React from 'react';

export interface GenericFormFieldProps {
  id: string;
  name: string;
  label: string;
  type?: 'text' | 'textarea';
  value: string;
  onChange: (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => void;
  onBlur: (e: React.FocusEvent<HTMLInputElement | HTMLTextAreaElement>) => void;
  error?: string;
  touched?: boolean;
  placeholder?: string;
  required?: boolean;
  maxLength?: number;
  rows?: number;
  helpText?: string;
}

export const GenericFormField: React.FC<GenericFormFieldProps> = ({
  id,
  name,
  label,
  type = 'text',
  value,
  onChange,
  onBlur,
  error,
  touched,
  placeholder,
  required = false,
  maxLength,
  rows = 4,
  helpText,
}) => {
  const isInvalid = Boolean(touched && error);

  const baseInputStyles = `w-full px-3.5 py-2 bg-zinc-950 border rounded text-zinc-100 placeholder-zinc-500 text-sm focus:outline-none transition-colors ${
    isInvalid
      ? 'border-red-600 focus:border-red-600'
      : 'border-zinc-700 focus:border-[#800020] hover:border-zinc-600'
  }`;

  return (
    <div className="mb-4">
      <div className="flex justify-between items-center mb-1">
        <label htmlFor={id} className="block text-sm font-medium text-zinc-200">
          {label} {required && <span className="text-red-500">*</span>}
        </label>
        {maxLength && (
          <span className={`text-xs ${value.length > maxLength ? 'text-red-400 font-medium' : 'text-zinc-500'}`}>
            {value.length}/{maxLength}
          </span>
        )}
      </div>

      {type === 'textarea' ? (
        <textarea
          id={id}
          name={name}
          rows={rows}
          value={value}
          onChange={onChange}
          onBlur={onBlur}
          placeholder={placeholder}
          maxLength={maxLength}
          className={baseInputStyles}
        />
      ) : (
        <input
          id={id}
          name={name}
          type="text"
          value={value}
          onChange={onChange}
          onBlur={onBlur}
          placeholder={placeholder}
          maxLength={maxLength}
          className={baseInputStyles}
        />
      )}

      {isInvalid ? (
        <p className="mt-1 text-xs text-red-400">{error}</p>
      ) : helpText ? (
        <p className="mt-1 text-xs text-zinc-500">{helpText}</p>
      ) : null}
    </div>
  );
};
