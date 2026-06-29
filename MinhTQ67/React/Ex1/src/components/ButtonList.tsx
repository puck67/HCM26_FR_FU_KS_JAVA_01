import { ReactNode } from "react";
import { Button } from "./Button";

export type ButtonConfig = {
  title: ReactNode;
  action: () => void;
  style?: string;
};

export function ButtonList({ items }: { items: ButtonConfig[] }) {
  return (
    <div className="flex gap-2 flex-wrap items-center">
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
