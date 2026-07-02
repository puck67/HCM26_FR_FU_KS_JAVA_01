
export type ButtonConfig = {
  title: string;
  action: () => void;
  style?: string;
  variant?: "primary" | "secondary" | "danger" | "success" | "warning";
};

export function Button({
  title,
  action,
  style,
  variant = "primary"
}: {
  title: string;
  action: () => void;
  style?: string;
  variant?: "primary" | "secondary" | "danger" | "success" | "warning";
}) {
  const getVariantClass = () => {
    switch (variant) {
      case "primary":
        return "btn-primary";
      case "secondary":
        return "btn-secondary";
      case "danger":
        return "btn-danger";
      case "success":
        return "btn-success";
      case "warning":
        return "btn-warning";
      default:
        return "btn-primary";
    }
  };

  return (
    <button
      className={`btn ${getVariantClass()} ${style || ""}`}
      onClick={action}
    >
      {title}
    </button>
  );
}

export function ButtonList({ items }: { items: ButtonConfig[] }) {
  return (
    <div className="button-list">
      {items.map((btn, i) => (
        <Button
          key={i}
          title={btn.title}
          action={btn.action}
          style={btn.style}
          variant={btn.variant}
        />
      ))}
    </div>
  );
}
