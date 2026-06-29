import React from "react";
import ButtonList from "./ButtonList";
import type { ButtonConfig } from "./ButtonList";

interface CrudActionBarProps {
  onAdd?: () => void;
  onImport?: () => void;
  onExport?: () => void;
  extraActions?: Array<{
    label: string;
    onClick: () => void;
    variant?: "primary" | "secondary" | "danger" | "success" | "warning" | "cyan" | "neon";
    disabled?: boolean;
  }>;
}

const CrudActionBar: React.FC<CrudActionBarProps> = ({
  onAdd,
  onImport,
  onExport,
  extraActions = [],
}) => {
  const buttons: ButtonConfig[] = [];

  if (onAdd) {
    buttons.push({
      label: "＋ Thêm mới",
      onClick: onAdd,
      variant: "neon",
    });
  }

  if (onImport) {
    buttons.push({
      label: "📥 Nhập File (Import)",
      onClick: onImport,
      variant: "secondary",
    });
  }

  if (onExport) {
    buttons.push({
      label: "📤 Xuất File (Export)",
      onClick: onExport,
      variant: "cyan",
    });
  }

  // Thêm các custom actions từ bên ngoài
  extraActions.forEach((action, idx) => {
    buttons.push({
      label: action.label,
      onClick: action.onClick,
      variant: action.variant || "secondary",
      disabled: action.disabled,
    });
  });

  if (buttons.length === 0) return null;

  return (
    <div className="crud-action-bar">
      <ButtonList buttons={buttons} />
    </div>
  );
};

export default CrudActionBar;
