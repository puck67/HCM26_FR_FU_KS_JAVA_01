import { Button } from "./Button";

export type ButtonConfig = {
  title: string;
  action: () => void;
  style?: string;
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
        />
      ))}
    </>
  );
}
