export function Button({ title, action, style }: {
  title: string;
  action: () => void;
  style?: string;
}) {
  return (
    <button
      className={style}
      onClick={action}
    >
      {title}
    </button>
  );
}
