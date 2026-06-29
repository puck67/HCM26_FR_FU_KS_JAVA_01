import React from "react";

type ButtonProps = {
  title: string;
  action: () => void;
  style?: string;
  disabled?: boolean;
  children?: React.ReactNode;
};

export function Button({
  title,
  action,
  style,
  disabled = false,
  children
}: ButtonProps) {
  return (
    <button
      onClick={action}
      disabled={disabled}
      className={`
        px-3.5 py-1.5 
        rounded-lg 
        text-sm 
        font-semibold 
        transition-all 
        duration-200 
        flex 
        items-center 
        justify-center 
        gap-1.5 
        cursor-pointer 
        select-none
        active:scale-97
        disabled:opacity-50 
        disabled:pointer-events-none
        ${style || "bg-brand-accent hover:bg-brand-accent-hover text-white shadow-lg shadow-brand-accent/20"}
      `}
    >
      {children}
      <span>{title}</span>
    </button>
  );
}