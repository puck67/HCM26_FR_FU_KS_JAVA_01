import type React from 'react';
import { Button } from './Button';

export type ButtonConfig = {
  title: string;
  action: () => void;
  style?: string;
  icon?: React.ReactNode;
};

export interface ButtonListProps {
  items: ButtonConfig[];
}

export function ButtonList({ items }: ButtonListProps) {
  return (
    <div className="app-btn-list">
      {items.map((btn, i) => (
        <Button
          key={i}
          title={btn.title}
          action={btn.action}
          style={btn.style}
          icon={btn.icon}
        />
      ))}
    </div>
  );
}
