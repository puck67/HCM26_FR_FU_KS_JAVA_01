import { Button } from "./Button";
import type { ButtonProps } from "./Button";

export type ButtonConfig = Omit<ButtonProps, "className"> & {
  className?: string;
};

export interface ButtonListProps {
  items: ButtonConfig[];
  gap?: "sm" | "md" | "lg";
  direction?: "row" | "column";
}

const gapClasses = {
  sm: "gap-1",
  md: "gap-2",
  lg: "gap-4",
};

export function ButtonList({
  items,
  gap = "md",
  direction = "row",
}: ButtonListProps) {
  return (
    <div
      className={`button-list button-list--${direction} ${gapClasses[gap]}`}
    >
      {items.map((btn, i) => (
        <Button key={i} {...btn} />
      ))}
    </div>
  );
}
