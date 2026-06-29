import React from 'react';

export interface GenericButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
  variant?: 'primary' | 'secondary' | 'danger' | 'outline';
  size?: 'sm' | 'md' | 'lg';
  label?: string;
  loading?: boolean;
}

export const GenericButton: React.FC<GenericButtonProps> = ({
  variant = 'primary',
  size = 'md',
  label,
  loading = false,
  children,
  className = '',
  disabled,
  ...props
}) => {
  const baseStyles = 'inline-flex items-center justify-center font-medium transition-colors rounded focus:outline-none disabled:opacity-50 disabled:cursor-not-allowed';
  
  const variantStyles = {
    primary: 'bg-[#800020] hover:bg-[#991032] text-white',
    secondary: 'bg-zinc-800 hover:bg-zinc-700 text-zinc-200',
    danger: 'bg-red-900/80 hover:bg-red-800 text-red-200 border border-red-800',
    outline: 'border border-zinc-700 bg-transparent hover:bg-zinc-800 text-zinc-300'
  };

  const sizeStyles = {
    sm: 'px-3 py-1 text-xs',
    md: 'px-4 py-1.5 text-sm',
    lg: 'px-5 py-2 text-base'
  };

  return (
    <button
      className={`${baseStyles} ${variantStyles[variant]} ${sizeStyles[size]} ${className}`}
      disabled={disabled || loading}
      {...props}
    >
      {loading ? 'Processing...' : (label || children)}
    </button>
  );
};
