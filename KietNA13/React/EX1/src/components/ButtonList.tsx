import React from 'react';
import { Button } from './Button';

export type ButtonConfig = {
  title: string;
  action: () => void;
  style?: string;
};

export function ButtonList({ items }: { items: ButtonConfig[] }) {
  return (
    <div className="button-list">
      {items.map((btn, i) => (
        <Button
          key={i}
          title={btn.title}
          action={btn.action}
          style={btn.style}
        />
      ))}
    </div>
  );
}
