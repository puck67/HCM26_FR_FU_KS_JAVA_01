import { Button } from "./Button";
import type { ButtonConfig } from "./types";

type Props = {
  items: ButtonConfig[];
};

export function ButtonList({ items }: Props) {
  return (
    <div className="flex flex-wrap items-center gap-2">
      {items.map((btn, index) => (
        <Button
          key={index}
          title={btn.title}
          action={btn.action}
          style={btn.style}
        />
      ))}
    </div>
  );
}