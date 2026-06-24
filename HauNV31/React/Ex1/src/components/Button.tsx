export function Button({ title, action, style }: {
  title: string;
  action: () => void;
  style?: string;
}) {
  // Map inline styles/colors to semantic classes
  let btnClass = style || "btn btn-ghost";
  
  // Convert old legacy simple colors if they exist
  if (style === "blue" || style === "primary") btnClass = "btn btn-primary";
  if (style === "green" || style === "success") btnClass = "btn btn-success";
  if (style === "red" || style === "danger") btnClass = "btn btn-danger";
  if (style === "gray" || style === "ghost") btnClass = "btn btn-ghost";

  // Auto styling based on title if no explicit style was provided
  if (!style) {
    const lowerTitle = title.toLowerCase();
    if (lowerTitle.includes("delete") || lowerTitle.includes("xóa") || lowerTitle.includes("remove")) {
      btnClass = "btn btn-danger";
    } else if (lowerTitle.includes("add") || lowerTitle.includes("thêm") || lowerTitle.includes("lưu") || lowerTitle.includes("save") || lowerTitle.includes("confirm")) {
      btnClass = "btn btn-primary";
    } else if (lowerTitle.includes("xong") || lowerTitle.includes("hoàn thành") || lowerTitle.includes("done") || lowerTitle.includes("complete")) {
      btnClass = "btn btn-success";
    } else {
      btnClass = "btn btn-ghost";
    }
  }

  return (
    <button
      className={btnClass}
      onClick={action}
    >
      {title}
    </button>
  );
}
