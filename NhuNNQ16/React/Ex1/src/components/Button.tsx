export function Button({ title, action, style, icon }: {
    title: string;
    action: () => void;
    style?: string;
    icon?: string;
}) {
    return (
        <button
            className={style}
            onClick={action}
        >
            {icon && <span className="btn-icon">{icon}</span>}
            {title}
        </button>
    );
}