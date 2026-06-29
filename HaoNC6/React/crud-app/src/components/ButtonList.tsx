import type { ReactNode } from "react";
import { Button } from "./Button";

export type ButtonConfig = {
  title: ReactNode;
  action: () => void;
  style?: string;
  tooltip?: string;
};

export function ButtonList({ items }: { items: ButtonConfig[] }) {
  return (
    <>
      {items.map((btn, i) => (
        <Button
          key={i}
          title={btn.title}
          action={btn.action}
          style={btn.style}
          tooltip={btn.tooltip}
        />
      ))}
    </>
  );
}
