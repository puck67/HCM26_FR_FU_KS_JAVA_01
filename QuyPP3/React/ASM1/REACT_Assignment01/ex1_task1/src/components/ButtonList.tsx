export type ButtonConfig = {
  title: string;
  action: () => void;
  style?: string;
};

export function Button({ title, action, style }: ButtonConfig) {
  return (
    <button
      onClick={action}
      className={style || "px-3 py-1.5 bg-blue-600 hover:bg-blue-700 text-white text-xs font-semibold rounded-md transition"}
    >
      {title}
    </button>
  );
}

export function ButtonList({ items }: { items: ButtonConfig[] }) {
  return (
    <div className="flex gap-2">
      {items.map((btn, idx) => (
        <Button key={idx} {...btn} />
      ))}
    </div>
  );
}
