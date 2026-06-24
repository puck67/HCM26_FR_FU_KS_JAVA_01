import { useEffect, useRef } from "react";

interface NotificationToastProps {
  message: string;
  type: "success" | "info" | "error" | "warning";
  isOpen: boolean;
  onClose: () => void;
  durationMs?: number;
}

export function NotificationToast({
  message,
  type,
  isOpen,
  onClose,
  durationMs = 4000
}: NotificationToastProps) {
  const onCloseRef = useRef(onClose);
  
  useEffect(() => {
    onCloseRef.current = onClose;
  }, [onClose]);

  useEffect(() => {
    if (isOpen) {
      const timer = setTimeout(() => {
        onCloseRef.current();
      }, durationMs);
      return () => clearTimeout(timer);
    }
  }, [isOpen, durationMs]);

  if (!isOpen) return null;

  const iconName = 
    type === "success" ? "check_circle" : 
    type === "error" ? "error" : 
    type === "warning" ? "warning" : "info";

  return (
    <div className={`notification-toast ${type}`}>
      <span className="material-symbols-outlined">{iconName}</span>
      <span className="notification-message">{message}</span>
      <button type="button" className="notification-close" onClick={onClose}>
        <span className="material-symbols-outlined">close</span>
      </button>
    </div>
  );
}
