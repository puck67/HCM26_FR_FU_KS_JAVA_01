import { ReactNode } from 'react';

export function Button({ title, action, style }: {
  title: ReactNode;
  action: () => void;
  style?: string;
}) {
  return (
    <button
      className={`inline-flex items-center justify-center rounded-lg font-outfit font-medium cursor-pointer transition-all active:scale-95 border border-transparent ${style || ''}`}
      onClick={action}
      title={typeof title === 'string' ? title : undefined}
    >
      {title}
    </button>
  );
}
