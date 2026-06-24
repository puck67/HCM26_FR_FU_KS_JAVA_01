import { Button } from "./Button";

export type ButtonConfig = {
  title: string;
  action: () => void;
  style?: string;
  disabled?: boolean;
};

export function ButtonList({ items }: { items: ButtonConfig[] }) {
  return (
    <div className="flex flex-wrap gap-2">
      {items.map((btn, i) => (
        <Button
          key={i}
          title={btn.title}
          action={btn.action}
          style={btn.style}
          disabled={btn.disabled}
        />
      ))}
    </div>
  );
}
