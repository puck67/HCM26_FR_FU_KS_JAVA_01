"use client";
import { motion } from "motion/react";

const usages = [
  {
    label: "Task management",
    entity: "tasks",
    imports: `import { CrudTable } from "./CrudTable";
import { CrudActionBar } from "./CrudActionBar";`,
    code: `<CrudActionBar
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
  }]}
/>`,
  },
  {
    label: "Student management",
    entity: "students",
    imports: `import { CrudTable } from "./CrudTable";
import { CrudActionBar } from "./CrudActionBar";`,
    code: `<CrudActionBar
  onAdd={() => openStudentForm()}
  onImport={() => importStudents()}
  onExport={() => exportStudents()}
/>

<CrudTable
  data={students}
  onEdit={(s) => editStudent(s)}
  onDelete={(s) => deleteStudent(s.id)}
/>`,
  },
  {
    label: "Teacher management",
    entity: "teachers",
    imports: `import { CrudTable } from "./CrudTable";`,
    code: `<CrudTable
  data={teachers}
  onEdit={(t) => editTeacher(t)}
  onDelete={(t) => deleteTeacher(t.id)}
/>`,
  },
];

export function UsageSection() {
  return (
    <section id="usage" className="py-32 border-t" style={{ borderColor: "var(--border)" }}>
      <div className="max-w-6xl mx-auto px-6">
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-16 items-start">
          {/* Left */}
          <div className="lg:sticky lg:top-24">
            <motion.h2
              initial={{ opacity: 0, y: 20 }}
              whileInView={{ opacity: 1, y: 0 }}
              viewport={{ once: true }}
              transition={{ duration: 0.6, ease: [0.16, 1, 0.3, 1] }}
              className="text-4xl md:text-5xl font-bold tracking-tighter leading-none mb-6"
              style={{ color: "var(--text)" }}
            >
              Same components,<br />different data.
            </motion.h2>
            <motion.p
              initial={{ opacity: 0 }}
              whileInView={{ opacity: 1 }}
              viewport={{ once: true }}
              transition={{ duration: 0.6, delay: 0.15 }}
              className="text-base leading-relaxed max-w-[48ch]"
              style={{ color: "var(--muted)" }}
            >
              Swap out the data prop. Everything else is the same. TypeScript catches mismatches at compile time, not runtime.
            </motion.p>

            <motion.div
              initial={{ opacity: 0 }}
              whileInView={{ opacity: 1 }}
              viewport={{ once: true }}
              transition={{ duration: 0.6, delay: 0.25 }}
              className="mt-10 flex flex-col gap-3"
            >
              {["Strong TypeScript generics", "No copy-paste boilerplate", "Composable row actions", "Optional action bar props"].map((item) => (
                <div key={item} className="flex items-center gap-3">
                  <div className="w-1.5 h-1.5 rounded-full" style={{ background: "var(--accent)" }} />
                  <span className="text-sm" style={{ color: "var(--muted)" }}>{item}</span>
                </div>
              ))}
            </motion.div>
          </div>

          {/* Right: stacked usage cards */}
          <div className="flex flex-col gap-6">
            {usages.map((u, i) => (
              <motion.div
                key={u.label}
                initial={{ opacity: 0, y: 24 }}
                whileInView={{ opacity: 1, y: 0 }}
                viewport={{ once: true }}
                transition={{ duration: 0.55, delay: i * 0.1, ease: [0.16, 1, 0.3, 1] }}
                className="rounded-[var(--radius)] border overflow-hidden"
                style={{ background: "var(--surface)", borderColor: "var(--border)" }}
              >
                <div className="flex items-center justify-between px-5 py-3 border-b" style={{ borderColor: "var(--border)" }}>
                  <span className="font-mono text-xs font-semibold" style={{ color: "var(--accent)" }}>{u.label}</span>
                  <span className="font-mono text-xs" style={{ color: "var(--muted)" }}>{u.entity}.tsx</span>
                </div>
                <pre className="p-5 text-xs leading-relaxed overflow-x-auto font-mono" style={{ color: "var(--text)" }}>
                  <code className="opacity-40">{u.imports}{"\n\n"}</code>
                  <code>{u.code}</code>
                </pre>
              </motion.div>
            ))}
          </div>
        </div>
      </div>
    </section>
  );
}
