import type { ReactNode } from "react";

export function Button({ title, action, style, tooltip }: {
  title: ReactNode;
  action: () => void;
  style?: string;
  tooltip?: string;
}) {
  return (
    <button
      className={style}
      onClick={action}
      title={tooltip}
      aria-label={tooltip}
    >
      {title}
    </button>
  );
}
