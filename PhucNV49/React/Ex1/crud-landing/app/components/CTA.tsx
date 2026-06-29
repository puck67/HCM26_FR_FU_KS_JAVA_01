"use client";
import { motion } from "motion/react";

export function CTA() {
  return (
    <section className="py-32 border-t" style={{ borderColor: "var(--border)" }}>
      <div className="max-w-6xl mx-auto px-6">
        <div
          className="rounded-[var(--radius)] border p-12 md:p-16 relative overflow-hidden"
          style={{ background: "var(--surface)", borderColor: "var(--border)" }}
        >
          {/* Background accent blob */}
          <div
            className="absolute -top-24 -right-24 w-64 h-64 rounded-full blur-3xl pointer-events-none"
            style={{ background: "var(--accent-dim)" }}
          />

          <div className="relative grid grid-cols-1 lg:grid-cols-[1fr_auto] gap-10 items-center">
            <div>
              <motion.h2
                initial={{ opacity: 0, y: 20 }}
                whileInView={{ opacity: 1, y: 0 }}
                viewport={{ once: true }}
                transition={{ duration: 0.6, ease: [0.16, 1, 0.3, 1] }}
                className="text-4xl md:text-5xl font-bold tracking-tighter leading-none mb-4"
                style={{ color: "var(--text)" }}
              >
                Stop writing tables.<br />Start shipping features.
              </motion.h2>
              <motion.p
                initial={{ opacity: 0 }}
                whileInView={{ opacity: 1 }}
                viewport={{ once: true }}
                transition={{ duration: 0.6, delay: 0.15 }}
                className="text-base leading-relaxed max-w-[50ch]"
                style={{ color: "var(--muted)" }}
              >
                Copy the four components. Plug in your data. Done in minutes, not days.
              </motion.p>
            </div>

            <motion.div
              initial={{ opacity: 0, scale: 0.95 }}
              whileInView={{ opacity: 1, scale: 1 }}
              viewport={{ once: true }}
              transition={{ duration: 0.5, delay: 0.2 }}
              className="flex flex-col sm:flex-row lg:flex-col gap-3"
            >
              <a
                href="https://github.com"
                className="px-7 py-3.5 rounded-[var(--radius)] font-semibold text-sm text-center transition-all active:scale-[0.98]"
                style={{ background: "var(--accent)", color: "var(--bg)" }}
              >
                View on GitHub
              </a>
              <a
                href="#components"
                className="px-7 py-3.5 rounded-[var(--radius)] font-semibold text-sm text-center border transition-all active:scale-[0.98]"
                style={{ borderColor: "var(--border)", color: "var(--text)" }}
              >
                Browse components
              </a>
            </motion.div>
          </div>
        </div>
      </div>
    </section>
  );
}
