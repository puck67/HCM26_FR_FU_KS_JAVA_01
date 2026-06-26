"use client";
import { motion, useScroll, useTransform } from "motion/react";

export function Nav() {
  const { scrollY } = useScroll();
  const bg = useTransform(scrollY, [0, 80], ["rgba(15,14,12,0)", "rgba(15,14,12,0.92)"]);
  const blur = useTransform(scrollY, [0, 80], ["blur(0px)", "blur(12px)"]);

  return (
    <motion.header
      style={{ backgroundColor: bg, backdropFilter: blur }}
      className="fixed top-0 left-0 right-0 z-50 border-b border-transparent"
    >
      <nav className="max-w-6xl mx-auto px-6 h-16 flex items-center justify-between">
        <span className="font-mono text-sm font-semibold tracking-tight" style={{ color: "var(--accent)" }}>
          FlowCRUD
        </span>
        <div className="flex items-center gap-8">
          <a href="#features" className="text-sm hidden md:block" style={{ color: "var(--muted)" }}>
            Features
          </a>
          <a href="#components" className="text-sm hidden md:block" style={{ color: "var(--muted)" }}>
            Components
          </a>
          <a href="#usage" className="text-sm hidden md:block" style={{ color: "var(--muted)" }}>
            Usage
          </a>
          <a
            href="https://github.com"
            className="text-sm px-4 py-1.5 rounded-[var(--radius)] border font-medium transition-all active:scale-[0.98]"
            style={{ borderColor: "var(--border)", color: "var(--text)" }}
          >
            GitHub
          </a>
        </div>
      </nav>
    </motion.header>
  );
}
