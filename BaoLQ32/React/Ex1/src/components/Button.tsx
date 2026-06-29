export function Button({ title, action, style }: {
  title: string;
  action: () => void;
  style?: string;
}) {
  return (
    <button
      className={`btn-custom ${style || ""}`}
      onClick={action}
    >
      {title}
    </button>
  );
}
