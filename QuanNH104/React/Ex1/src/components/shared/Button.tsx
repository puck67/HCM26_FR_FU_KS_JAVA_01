import React from "react";

interface ButtonProps {
  label?: string;
  onClick?: () => void;
  variant?: "primary" | "secondary" | "danger" | "success" | "warning" | "cyan" | "neon";
  disabled?: boolean;
  className?: string;
  children?: React.ReactNode;
  type?: "button" | "submit" | "reset";
}

const Button: React.FC<ButtonProps> = ({
  label,
  onClick,
  variant = "primary",
  disabled = false,
  className = "",
  children,
  type = "button",
}) => {
  const baseStyle = "shared-btn";
  const variantClass = `btn-${variant}`;

  return (
    <button
      type={type}
      onClick={onClick}
      disabled={disabled}
      className={`${baseStyle} ${variantClass} ${className}`}
    >
      {label || children}
    </button>
  );
};

export default Button;
