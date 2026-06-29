import React from 'react';

export function Button({ title, action, style = 'btn btn-primary' }: {
  title: string;
  action: () => void;
  style?: string;
}) {
  return (
    <button
      className={style}
      onClick={action}
    >
      {title}
    </button>
  );
}
