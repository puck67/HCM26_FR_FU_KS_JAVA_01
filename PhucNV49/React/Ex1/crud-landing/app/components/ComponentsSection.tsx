"use client";
import { useState } from "react";
import { motion, AnimatePresence } from "motion/react";

const components = [
  {
    name: "Button",
    description: "Base interactive atom. Accepts title, action callback, and optional style.",
    code: `export function Button({ title, action, style }: {
  title: string;
  action: () => void;
  style?: string;
}) {
  return (
    <button className={style} onClick={action}>
      {title}
    </button>
  );
}`,
  },
  {
    name: "ButtonList",
    description: "Renders any array of ButtonConfig items. Powers both action bars and row actions.",
    code: `export type ButtonConfig = {
  title: string;
  action: () => void;
  style?: string;
};

export function ButtonList({ items }: {
  items: ButtonConfig[]
}) {
  return (
    <>
      {items.map((btn, i) => (
        <Button
          key={i}
          title={btn.title}
          action={btn.action}
          style={btn.style}
        />
      ))}
    </>
  );
}`,
  },
  {
    name: "CrudTable<T>",
    description: "Generic table for any TypeScript object. Auto-renders columns. Edit, Delete, and custom row actions.",
    code: `export function CrudTable<T extends object>({
  data,
  onEdit,
  onDelete,
  extraActions
}: CrudTableProps<T>) {
  if (data.length === 0) return <p>No data.</p>;
  const columns = Object.keys(data[0]);

  return (
    <table>
      <thead>
        <tr>
          {columns.map((col) => (
            <th key={col}>{col.toUpperCase()}</th>
          ))}
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        {data.map((item, i) => (
          <tr key={i}>
            {columns.map((col) => (
              <td key={col}>{String(item[col])}</td>
            ))}
            <td>
              <ButtonList items={rowActions(item)} />
            </td>
          </tr>
        ))}
      </tbody>
    </table>
  );
}`,
  },
  {
    name: "CrudActionBar",
    description: "Top bar with optional Add, Import, Export buttons. Pass only what you need.",
    code: `export function CrudActionBar({
  onAdd,
  onImport,
  onExport
}: {
  onAdd?: () => void;
  onImport?: () => void;
  onExport?: () => void;
}) {
  const buttons: ButtonConfig[] = [
    ...(onAdd
      ? [{ title: "Add", action: onAdd }]
      : []),
    ...(onImport
      ? [{ title: "Import", action: onImport }]
      : []),
    ...(onExport
      ? [{ title: "Export", action: onExport }]
      : []),
  ];

  return <ButtonList items={buttons} />;
}`,
  },
];

export function ComponentsSection() {
  const [active, setActive] = useState(0);

  return (
    <section id="components" className="py-32 border-t" style={{ borderColor: "var(--border)" }}>
      <div className="max-w-6xl mx-auto px-6">
        <motion.h2
          initial={{ opacity: 0, y: 20 }}
          whileInView={{ opacity: 1, y: 0 }}
          viewport={{ once: true }}
          transition={{ duration: 0.6, ease: [0.16, 1, 0.3, 1] }}
          className="text-4xl md:text-5xl font-bold tracking-tighter leading-none mb-16"
          style={{ color: "var(--text)" }}
        >
          Four components,<br />infinite models.
        </motion.h2>

        <div className="grid grid-cols-1 lg:grid-cols-[280px_1fr] gap-8">
          {/* Tab list */}
          <div className="flex flex-row lg:flex-col gap-2 overflow-x-auto lg:overflow-visible pb-2 lg:pb-0">
            {components.map((c, i) => (
              <button
                key={c.name}
                onClick={() => setActive(i)}
                className="text-left px-4 py-3 rounded-[var(--radius)] text-sm font-mono font-medium whitespace-nowrap transition-all active:scale-[0.98]"
                style={{
                  background: active === i ? "var(--surface-2)" : "transparent",
                  color: active === i ? "var(--accent)" : "var(--muted)",
                  borderLeft: active === i ? "2px solid var(--accent)" : "2px solid transparent",
                }}
              >
                {c.name}
              </button>
            ))}
          </div>

          {/* Code panel */}
          <AnimatePresence mode="wait">
            <motion.div
              key={active}
              initial={{ opacity: 0, x: 16 }}
              animate={{ opacity: 1, x: 0 }}
              exit={{ opacity: 0, x: -16 }}
              transition={{ duration: 0.3, ease: [0.16, 1, 0.3, 1] }}
              className="rounded-[var(--radius)] border overflow-hidden"
              style={{ background: "var(--surface)", borderColor: "var(--border)" }}
            >
              <div className="px-5 py-4 border-b" style={{ borderColor: "var(--border)" }}>
                <p className="font-mono text-sm font-semibold" style={{ color: "var(--accent)" }}>
                  {components[active].name}
                </p>
                <p className="text-sm mt-1" style={{ color: "var(--muted)" }}>
                  {components[active].description}
                </p>
              </div>
              <pre className="p-5 text-xs leading-relaxed overflow-x-auto font-mono" style={{ color: "var(--text)" }}>
                <code>{components[active].code}</code>
              </pre>
            </motion.div>
          </AnimatePresence>
        </div>
      </div>
    </section>
  );
}
