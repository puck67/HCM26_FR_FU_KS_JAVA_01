"use client";
import { motion } from "motion/react";
import { Stack, Table, Rows, ArrowsOut } from "@phosphor-icons/react";

const features = [
  {
    icon: Stack,
    title: "Fully generic",
    body: "CrudTable<T> works with any TypeScript object. Columns auto-render from object keys.",
  },
  {
    icon: Table,
    title: "Zero duplication",
    body: "One table component handles students, teachers, tasks, products. Write it once.",
  },
  {
    icon: Rows,
    title: "Row-level actions",
    body: "Pass Edit, Delete, or any custom action per row. Composable ButtonList under the hood.",
  },
  {
    icon: ArrowsOut,
    title: "Action bar",
    body: "CrudActionBar renders Add, Import, Export buttons. All optional, all composable.",
  },
];

const fadeUp = {
  hidden: { opacity: 0, y: 24 },
  show: (i: number) => ({
    opacity: 1,
    y: 0,
    transition: { duration: 0.55, delay: i * 0.08, ease: [0.16, 1, 0.3, 1] },
  }),
};

export function Features() {
  return (
    <section id="features" className="py-32">
      <div className="max-w-6xl mx-auto px-6">
        {/* Asymmetric header - full width headline, no split */}
        <motion.h2
          initial={{ opacity: 0, y: 20 }}
          whileInView={{ opacity: 1, y: 0 }}
          viewport={{ once: true }}
          transition={{ duration: 0.6, ease: [0.16, 1, 0.3, 1] }}
          className="text-4xl md:text-5xl font-bold tracking-tighter leading-none mb-4"
          style={{ color: "var(--text)" }}
        >
          Built for scale,<br />designed for reuse.
        </motion.h2>
        <motion.p
          initial={{ opacity: 0 }}
          whileInView={{ opacity: 1 }}
          viewport={{ once: true }}
          transition={{ duration: 0.6, delay: 0.15 }}
          className="text-base leading-relaxed max-w-[55ch] mb-16"
          style={{ color: "var(--muted)" }}
        >
          No more copy-pasting table logic across every page. One generic system, typed end-to-end.
        </motion.p>

        {/* Bento: 4 cells, 2 wide + 2 narrow */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
          {features.map((f, i) => (
            <motion.div
              key={f.title}
              custom={i}
              initial="hidden"
              whileInView="show"
              viewport={{ once: true }}
              variants={fadeUp}
              className="p-6 rounded-[var(--radius)] border flex flex-col gap-4 group"
              style={{ background: "var(--surface)", borderColor: "var(--border)" }}
            >
              <div
                className="w-10 h-10 rounded-[var(--radius)] flex items-center justify-center"
                style={{ background: "var(--accent-dim)" }}
              >
                <f.icon size={20} weight="duotone" style={{ color: "var(--accent)" }} />
              </div>
              <div>
                <p className="font-semibold text-sm mb-1" style={{ color: "var(--text)" }}>{f.title}</p>
                <p className="text-sm leading-relaxed" style={{ color: "var(--muted)" }}>{f.body}</p>
              </div>
            </motion.div>
          ))}
        </div>
      </div>
    </section>
  );
}
