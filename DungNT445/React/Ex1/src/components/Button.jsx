import React from 'react';

export function Button({ title, action, variant = 'secondary', icon }) {
  const variantClass = `btn btn-${variant}`;
  return (
    <button
      className={variantClass}
      onClick={action}
      style={{ display: 'inline-flex', alignItems: 'center', gap: '0.5rem' }}
    >
      {icon && <span className="btn-icon" style={{ display: 'inline-flex', alignItems: 'center' }}>{icon}</span>}
      {title}
    </button>
  );
}
