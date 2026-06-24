import React from 'react';

export interface ButtonProps {
  title: string;
  action: () => void;
  style?: string;
  icon?: React.ReactNode;
}

export function Button({ title, action, style, icon }: ButtonProps) {
  return (
    <button
      className={`app-btn ${style || ''}`}
      title={title}
      onClick={(e) => {
        e.stopPropagation();
        action();
      }}
    >
      {icon && <span className="app-btn-icon">{icon}</span>}
      <span className="app-btn-text">{title}</span>
    </button>
  );
}
