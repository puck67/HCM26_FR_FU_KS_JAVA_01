import type React from 'react';
import { useState } from 'react';

export type FieldConfig = {
  name: string;
  label: string;
  type: 'text' | 'email' | 'number' | 'date' | 'select';
  options?: { value: string; label: string }[] | string[];
  required?: boolean;
  min?: number;
  max?: number;
  placeholder?: string;
};

interface DynamicFormProps {
  fields: FieldConfig[];
  initialValues?: Record<string, unknown>;
  onSubmit: (values: Record<string, unknown>) => void;
  onCancel: () => void;
}

export function DynamicForm({ fields, initialValues, onSubmit, onCancel }: DynamicFormProps) {
  const [formData, setFormData] = useState<Record<string, unknown>>(() => {
    const defaultData: Record<string, unknown> = {};
    fields.forEach((field) => {
      defaultData[field.name] = initialValues?.[field.name] ?? '';
    });
    return defaultData;
  });

  const [errors, setErrors] = useState<Record<string, string>>({});

  const handleChange = (name: string, value: unknown) => {
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
    if (errors[name]) {
      setErrors((prev) => {
        const next = { ...prev };
        delete next[name];
        return next;
      });
    }
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    const newErrors: Record<string, string> = {};

    fields.forEach((field) => {
      const val = formData[field.name];

      if (field.required && (val === undefined || val === null || val === '')) {
        newErrors[field.name] = `${field.label} is required`;
        return;
      }

      if (field.type === 'email' && val) {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(String(val))) {
          newErrors[field.name] = 'Invalid email address';
        }
      }

      if (field.type === 'number' && val !== '') {
        const num = Number(val);
        if (isNaN(num)) {
          newErrors[field.name] = 'Must be a valid number';
        } else {
          if (field.min !== undefined && num < field.min) {
            newErrors[field.name] = `Must be at least ${field.min}`;
          }
          if (field.max !== undefined && num > field.max) {
            newErrors[field.name] = `Must be at most ${field.max}`;
          }
        }
      }
    });

    if (Object.keys(newErrors).length > 0) {
      setErrors(newErrors);
      return;
    }

    const processedData = { ...formData };
    fields.forEach((field) => {
      if (field.type === 'number' && processedData[field.name] !== '') {
        processedData[field.name] = Number(processedData[field.name]);
      }
    });

    onSubmit(processedData);
  };

  return (
    <form onSubmit={handleSubmit} className="dynamic-form">
      {fields.map((field) => {
        const isError = !!errors[field.name];
        return (
          <div key={field.name} className="form-group">
            <label className="form-label">
              {field.label} {field.required && <span className="form-required">*</span>}
            </label>
            
            {field.type === 'select' ? (
              <select
                className={`form-input form-select ${isError ? 'form-input-error' : ''}`}
                value={String(formData[field.name] ?? '')}
                onChange={(e) => handleChange(field.name, e.target.value)}
              >
                <option value="">Select option...</option>
                {field.options?.map((opt) => {
                  const val = typeof opt === 'string' ? opt : opt.value;
                  const label = typeof opt === 'string' ? opt : opt.label;
                  return (
                    <option key={val} value={val}>
                      {label}
                    </option>
                  );
                })}
              </select>
            ) : (
              <input
                className={`form-input ${isError ? 'form-input-error' : ''}`}
                type={field.type}
                placeholder={field.placeholder || `Enter ${field.label.toLowerCase()}`}
                value={String(formData[field.name] ?? '')}
                onChange={(e) => handleChange(field.name, e.target.value)}
                min={field.min}
                max={field.max}
              />
            )}
            
            {isError && <span className="form-error-msg">{errors[field.name]}</span>}
          </div>
        );
      })}

      <div className="form-actions">
        <button type="button" className="app-btn btn-secondary" onClick={onCancel}>
          Cancel
        </button>
        <button type="submit" className="app-btn btn-primary">
          Submit
        </button>
      </div>
    </form>
  );
}
