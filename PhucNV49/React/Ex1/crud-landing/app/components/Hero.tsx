"use client";
import { motion } from "motion/react";

const fadeUp = {
  hidden: { opacity: 0, y: 28 },
  show: (i: number) => ({
    opacity: 1,
    y: 0,
    transition: { duration: 0.6, delay: i * 0.1, ease: [0.16, 1, 0.3, 1] },
  }),
};

export function Hero() {
  return (
    <section className="min-h-[100dvh] flex items-center pt-16">
      <div className="max-w-6xl mx-auto px-6 w-full grid grid-cols-1 lg:grid-cols-2 gap-16 items-center py-24">
        {/* Left: text */}
        <div>
          <motion.p
            custom={0}
            initial="hidden"
            animate="show"
            variants={fadeUp}
            className="font-mono text-xs uppercase tracking-widest mb-6"
            style={{ color: "var(--accent)" }}
          >
            React CRUD System
          </motion.p>

          <motion.h1
            custom={1}
            initial="hidden"
            animate="show"
            variants={fadeUp}
            className="text-5xl md:text-6xl lg:text-7xl font-bold tracking-tighter leading-none mb-6"
            style={{ color: "var(--text)" }}
          >
            Three<br />
            components.<br />
            <span style={{ color: "var(--accent)" }}>Any model.</span>
          </motion.h1>

          <motion.p
            custom={2}
            initial="hidden"
            animate="show"
            variants={fadeUp}
            className="text-lg leading-relaxed max-w-[52ch] mb-10"
            style={{ color: "var(--muted)" }}
          >
            Generic, type-safe CRUD for tasks, students, teachers, products. Zero duplication across your entire app.
          </motion.p>

          <motion.div
            custom={3}
            initial="hidden"
            animate="show"
            variants={fadeUp}
            className="flex flex-wrap gap-3"
          >
            <a
              href="#usage"
              className="px-6 py-3 rounded-[var(--radius)] font-semibold text-sm transition-all active:scale-[0.98]"
              style={{ background: "var(--accent)", color: "var(--bg)" }}
            >
              Get started
            </a>
            <a
              href="#components"
              className="px-6 py-3 rounded-[var(--radius)] font-semibold text-sm border transition-all active:scale-[0.98]"
              style={{ borderColor: "var(--border)", color: "var(--text)" }}
            >
              View components
            </a>
          </motion.div>
        </div>

        {/* Right: code preview */}
        <motion.div
          custom={2}
          initial="hidden"
          animate="show"
          variants={fadeUp}
          className="rounded-[var(--radius)] border overflow-hidden"
          style={{ borderColor: "var(--border)", background: "var(--surface)" }}
        >
          <div className="flex items-center gap-1.5 px-4 py-3 border-b" style={{ borderColor: "var(--border)" }}>
            <span className="w-3 h-3 rounded-full" style={{ background: "#ff5f57" }} />
            <span className="w-3 h-3 rounded-full" style={{ background: "#febc2e" }} />
            <span className="w-3 h-3 rounded-full" style={{ background: "#28c840" }} />
            <span className="ml-3 font-mono text-xs" style={{ color: "var(--muted)" }}>TaskPage.tsx</span>
          </div>
          <pre className="p-5 text-xs leading-relaxed overflow-x-auto font-mono" style={{ color: "var(--text)" }}>
{`<CrudActionBar
  onAdd={() => openForm()}
  onImport={() => importTasks()}
  onExport={() => exportTasks()}
/>

<CrudTable
  data={tasks}
  onEdit={(t) => editTask(t)}
  onDelete={(t) => deleteTask(t.id)}
  extraActions={(t) => [{
    title: "Complete",
    action: () => complete(t),
    style: "text-green-500"
  }]}
/>`}
          </pre>
        </motion.div>
      </div>
    </section>
  );
}
