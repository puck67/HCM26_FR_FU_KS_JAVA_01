import { ButtonList, type ButtonConfig } from "./ButtonList";

export function CrudActionBar({
  onAdd,
  onImport,
  onExport
}: {
  onAdd?: () => void;
  onImport?: () => void;
  onExport?: () => void;
}) {
  const buttons: ButtonConfig[] = [
    ...(onAdd ? [{ title: "Thêm Mới", action: onAdd, style: "btn-primary" }] : []),
    ...(onImport ? [{ title: "Nhập Mẫu", action: onImport, style: "btn-secondary" }] : []),
    ...(onExport ? [{ title: "Xuất JSON", action: onExport, style: "btn-secondary" }] : []),
  ];
  return <ButtonList items={buttons} />;
}
