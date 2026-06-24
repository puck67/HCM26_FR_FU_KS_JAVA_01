import React from 'react';
import { Button } from './Button';

export function ButtonList({ items }) {
  if (!items || items.length === 0) return null;
  
  return (
    <>
      {items.map((btn, i) => (
        <Button
          key={i}
          title={btn.title}
          action={btn.action}
          variant={btn.variant}
          icon={btn.icon}
        />
      ))}
    </>
  );
}
