import React, { useState, useEffect } from 'react';

export function CrudModal({
  isOpen,
  onClose,
  onSubmit,
  title,
  initialData,
  fields
}) {
  const [formData, setFormData] = useState({});

  // Sync state with initialData when modal opens or initialData changes
  useEffect(() => {
    if (isOpen) {
      const defaultData = {};
      fields.forEach(field => {
        defaultData[field.name] = initialData && initialData[field.name] !== undefined
          ? initialData[field.name]
          : (field.defaultValue !== undefined ? field.defaultValue : '');
      });
      setFormData(defaultData);
    }
  }, [isOpen, initialData, fields]);

  if (!isOpen) return null;

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleFormSubmit = (e) => {
    e.preventDefault();
    
    // Pass ID back if we are editing
    const submissionData = initialData?.id 
      ? { ...formData, id: initialData.id } 
      : { ...formData };
      
    onSubmit(submissionData);
  };

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal-content" onClick={e => e.stopPropagation()}>
        <div className="modal-header">
          <h3 className="modal-title">{title}</h3>
          <button className="modal-close" onClick={onClose}>&times;</button>
        </div>
        
        <form onSubmit={handleFormSubmit} className="modal-form">
          <div className="modal-body">
            {fields.map((field) => (
              <div className="form-group" key={field.name}>
                <label className="form-label" htmlFor={field.name}>
                  {field.label} {field.required && <span style={{ color: 'var(--danger)' }}>*</span>}
                </label>
                
                {field.type === 'select' ? (
                  <select
                    id={field.name}
                    name={field.name}
                    value={formData[field.name] || ''}
                    onChange={handleChange}
                    required={field.required}
                    className="form-select"
                  >
                    <option value="" disabled>Select {field.label.toLowerCase()}...</option>
                    {field.options.map(opt => (
                      <option key={opt} value={opt}>{opt}</option>
                    ))}
                  </select>
                ) : field.type === 'textarea' ? (
                  <textarea
                    id={field.name}
                    name={field.name}
                    value={formData[field.name] || ''}
                    onChange={handleChange}
                    required={field.required}
                    className="form-input"
                    rows={3}
                    style={{ resize: 'vertical' }}
                    placeholder={`Enter ${field.label.toLowerCase()}...`}
                  />
                ) : (
                  <input
                    id={field.name}
                    type={field.type || 'text'}
                    name={field.name}
                    value={formData[field.name] || ''}
                    onChange={handleChange}
                    required={field.required}
                    className="form-input"
                    placeholder={`Enter ${field.label.toLowerCase()}...`}
                  />
                )}
              </div>
            ))}
          </div>
          
          <div className="modal-footer">
            <button type="button" className="btn btn-secondary" onClick={onClose}>
              Cancel
            </button>
            <button type="submit" className="btn btn-primary">
              Save Changes
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
