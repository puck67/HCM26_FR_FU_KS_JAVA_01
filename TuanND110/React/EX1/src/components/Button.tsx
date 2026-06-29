import React from "react";

const BUTTON_DEFAULT_STYLE: React.CSSProperties = {
    margin: '4px',
    padding: '8px 16px',
    cursor: 'pointer',
    border: '1px solid transparent',
    display: 'inline-flex',
    alignItems: 'center',
    justifyContent: 'center',
    gap: '8px'
};

export function Button({ title, action, style }: {
    title: React.ReactNode;
    action: () => void;
    style?: string;
}) {
    const isActionBtn = style?.includes("action-btn");
    const customStyle: React.CSSProperties = isActionBtn
        ? {
            margin: '2px',
            padding: '6px',
            cursor: 'pointer',
            border: '1px solid transparent',
            display: 'inline-flex',
            alignItems: 'center',
            justifyContent: 'center',
            borderRadius: '50%',
            width: '32px',
            height: '32px',
            boxSizing: 'border-box'
          }
        : BUTTON_DEFAULT_STYLE;

    return (
        <button
            type="button"
            className={style}
            onClick={action}
            style={customStyle}
        >
            {title}
        </button>
    );
}