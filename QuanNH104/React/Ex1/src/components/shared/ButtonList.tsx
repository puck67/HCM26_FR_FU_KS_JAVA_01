import React from "react";
import Button from "./Button";

export interface ButtonConfig {
  id?: string;
  label: string;
  onClick: () => void;
  variant?: "primary" | "secondary" | "danger" | "success" | "warning" | "cyan" | "neon";
  disabled?: boolean;
  hidden?: boolean;
}

interface ButtonListProps {
  buttons: ButtonConfig[];
  className?: string;
}

const ButtonList: React.FC<ButtonListProps> = ({ buttons, className = "" }) => {
  const visibleButtons = buttons.filter((btn) => !btn.hidden);

  if (visibleButtons.length === 0) return null;

  return (
    <div className={`btn-list-container ${className}`}>
      {visibleButtons.map((btn, index) => (
        <Button
          key={btn.id || `btn-${index}`}
          onClick={btn.onClick}
          variant={btn.variant}
          disabled={btn.disabled}
        >
          {btn.label}
        </Button>
      ))}
    </div>
  );
};

export default ButtonList;
