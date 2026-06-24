import { Button } from "./Button";
import React from "react";

export type ButtonConfig = {
    title: React.ReactNode;
    action: () => void;
    style?: string;
    key?: string;
};

export function ButtonList({ items }: { items: ButtonConfig[] }) {
    return (
        <>
            {items.map((btn, i) => (
                <Button
                    key={btn.key || (typeof btn.title === 'string' ? btn.title : `btn-${i}`)}
                    title={btn.title}
                    action={btn.action}
                    style={btn.style}
                />
            ))}
        </>
    );
}