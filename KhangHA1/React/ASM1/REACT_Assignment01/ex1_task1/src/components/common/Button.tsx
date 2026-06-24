export function Button({ title, action, style, disabled, type = 'button' }: {
  title: string;
  action: () => void;
  style?: string;
  disabled?: boolean;
  type?: 'button' | 'submit' | 'reset';
}) {
  return (
    <button
      type={type}
      className={`px-4 py-2 rounded font-medium transition-colors focus:outline-none focus:ring-2 focus:ring-offset-2 ${style} ${disabled ? 'opacity-50 cursor-not-allowed' : ''}`}
      onClick={action}
      disabled={disabled}
    >
      {title}
    </button>
  );
}
