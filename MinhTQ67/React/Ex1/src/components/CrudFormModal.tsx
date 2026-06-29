import { useState, useEffect } from "react";
import { Modal } from "./Modal";
import { Button } from "./Button";

export type FieldConfig = {
  name: string;
  label: string;
  type: "text" | "number" | "select";
  options?: string[]; // for select
};

export function CrudFormModal({ 
  isOpen, 
  onClose, 
  onSave, 
  title, 
  fields, 
  initialData 
}: {
  isOpen: boolean;
  onClose: () => void;
  onSave: (data: any) => void;
  title: string;
  fields: FieldConfig[];
  initialData?: any;
}) {
  const [formData, setFormData] = useState<any>({});

  useEffect(() => {
    if (isOpen) {
      if (initialData) {
        setFormData(initialData);
      } else {
        // Initialize empty state
        const emptyState: any = {};
        fields.forEach(f => {
          emptyState[f.name] = f.type === 'select' && f.options ? f.options[0] : '';
        });
        setFormData(emptyState);
      }
    }
  }, [isOpen, initialData, fields]);

  if (!isOpen) return null;

  return (
    <Modal isOpen={isOpen} onClose={onClose} title={title}>
      <div className="flex flex-col gap-4">
        {fields.map(field => (
          <div key={field.name}>
            <label className="block text-sm font-medium text-slate-700 mb-1">{field.label}</label>
            {field.type === 'select' ? (
              <select 
                className="w-full px-3 py-2 border border-slate-200 rounded-lg outline-none focus:border-blue-500 focus:ring-1 focus:ring-blue-500 bg-white"
                value={formData[field.name] || ''}
                onChange={(e) => setFormData({...formData, [field.name]: e.target.value})}
              >
                {field.options?.map(opt => <option key={opt} value={opt}>{opt}</option>)}
              </select>
            ) : (
              <input 
                type={field.type} 
                className="w-full px-3 py-2 border border-slate-200 rounded-lg outline-none focus:border-blue-500 focus:ring-1 focus:ring-blue-500 transition-all"
                value={formData[field.name] || ''}
                onChange={(e) => setFormData({...formData, [field.name]: field.type === 'number' ? Number(e.target.value) : e.target.value})}
                placeholder={`Enter ${field.label.toLowerCase()}`}
              />
            )}
          </div>
        ))}
        <div className="flex gap-3 justify-end mt-4 pt-4 border-t border-slate-100">
          <Button title="Cancel" action={onClose} style="px-4 py-2 bg-slate-100 text-slate-700 hover:bg-slate-200" />
          <Button title="Save" action={() => onSave(formData)} style="px-4 py-2 bg-blue-500 text-white shadow-md shadow-blue-500/30 hover:bg-blue-600 hover:-translate-y-[1px]" />
        </div>
      </div>
    </Modal>
  );
}
