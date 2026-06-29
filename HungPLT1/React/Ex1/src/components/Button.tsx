export function Button({ title, action, style }: {
  title: string;
  action: () => void;
  style?: string;
}) {
  return (
    <button
      className={`px-4 py-2 rounded-lg font-semibold transition-all duration-200 active:scale-95 text-sm cursor-pointer ${style || "bg-indigo-600 hover:bg-indigo-500 text-white"}`}
      onClick={action}
    >
      {title}
    </button>
  );
}
