import type { ReactNode } from "react";

export type ButtonVariant = "primary" | "secondary" | "danger" | "success" | "ghost";
export type ButtonSize = "sm" | "md" | "lg";

export interface ButtonProps {
  title: string;
  action: () => void;
  variant?: ButtonVariant;
  size?: ButtonSize;
  icon?: ReactNode;
  disabled?: boolean;
  className?: string;
}

const variantClasses: Record<ButtonVariant, string> = {
  primary: "btn-primary",
  secondary: "btn-secondary",
  danger: "btn-danger",
  success: "btn-success",
  ghost: "btn-ghost",
};

const sizeClasses: Record<ButtonSize, string> = {
  sm: "btn-sm",
  md: "btn-md",
  lg: "btn-lg",
};

export function Button({
  title,
  action,
  variant = "primary",
  size = "md",
  icon,
  disabled = false,
  className = "",
}: ButtonProps) {
  return (
    <button
      className={`btn ${variantClasses[variant]} ${sizeClasses[size]} ${className}`}
      onClick={action}
      disabled={disabled}
    >
      {icon && <span className="btn-icon">{icon}</span>}
      {title}
    </button>
  );
}
