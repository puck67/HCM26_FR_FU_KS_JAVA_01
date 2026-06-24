import React, { useState, useEffect } from "react";
import { Button } from "./Button";

export type FieldConfig = {
  name: string;
  label: string;
  type: "text" | "number" | "email" | "date" | "select" | "checkbox";
  options?: string[];
};

type CrudModalProps<T extends object> = {
  isOpen: boolean;
  onClose: () => void;
  onSave: (data: T) => void;
  title: string;
  fields: FieldConfig[];
  initialData?: T | null;
};

export function CrudModal<T extends object>({
  isOpen,
  onClose,
  onSave,
  title,
  fields,
  initialData
}: CrudModalProps<T>) {
  const [formData, setFormData] = useState<any>({});

  useEffect(() => {
    if (isOpen) {
      if (initialData) {
        setFormData({ ...initialData });
      } else {
        const defaultData: any = {};
        fields.forEach((field) => {
          if (field.type === "checkbox") {
            defaultData[field.name] = false;
          } else if (field.type === "number") {
            defaultData[field.name] = 0;
          } else {
            defaultData[field.name] = "";
          }
        });
        setFormData(defaultData);
      }
    }
  }, [isOpen, initialData, fields]);

  if (!isOpen) return null;

  const handleChange = (name: string, value: any) => {
    setFormData((prev: any) => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    onSave(formData as T);
    onClose();
  };

  return (
    <div className="modal-overlay">
      <div className="modal-container">
        <div className="modal-header">
          <h3>{title}</h3>
          <button className="modal-close-btn" onClick={onClose}>
            &times;
          </button>
        </div>
        <form onSubmit={handleSubmit}>
          <div className="modal-body">
            {fields.map((field) => (
              <div key={field.name} className="form-group">
                <label htmlFor={field.name}>{field.label}</label>
                {field.type === "select" ? (
                  <select
                    id={field.name}
                    value={formData[field.name] || ""}
                    onChange={(e) => handleChange(field.name, e.target.value)}
                    required
                  >
                    <option value="" disabled>-- Chọn --</option>
                    {field.options?.map((opt) => (
                      <option key={opt} value={opt}>
                        {opt}
                      </option>
                    ))}
                  </select>
                ) : field.type === "checkbox" ? (
                  <div className="checkbox-wrapper">
                    <input
                      type="checkbox"
                      id={field.name}
                      checked={!!formData[field.name]}
                      onChange={(e) => handleChange(field.name, e.target.checked)}
                    />
                    <span>{field.label}</span>
                  </div>
                ) : (
                  <input
                    type={field.type}
                    id={field.name}
                    value={formData[field.name] === undefined ? "" : formData[field.name]}
                    onChange={(e) =>
                      handleChange(
                        field.name,
                        field.type === "number"
                          ? parseFloat(e.target.value) || 0
                          : e.target.value
                      )
                    }
                    required
                  />
                )}
              </div>
            ))}
          </div>
          <div className="modal-footer">
            <Button
              title="Hủy"
              action={onClose}
              style="btn-cancel"
            />
            <button type="submit" className="btn-submit">
              Lưu lại
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
