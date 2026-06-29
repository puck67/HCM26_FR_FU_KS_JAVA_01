export function Footer() {
  return (
    <footer className="border-t py-8" style={{ borderColor: "var(--border)" }}>
      <div className="max-w-6xl mx-auto px-6 flex flex-col sm:flex-row items-center justify-between gap-4">
        <span className="font-mono text-sm font-semibold" style={{ color: "var(--accent)" }}>FlowCRUD</span>
        <p className="text-xs" style={{ color: "var(--muted)" }}>
          Open source React CRUD component system. MIT license.
        </p>
        <div className="flex gap-6">
          <a href="https://github.com" className="text-xs transition-opacity hover:opacity-70" style={{ color: "var(--muted)" }}>GitHub</a>
          <a href="#components" className="text-xs transition-opacity hover:opacity-70" style={{ color: "var(--muted)" }}>Docs</a>
        </div>
      </div>
    </footer>
  );
}
