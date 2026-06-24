export default function Header() {
  return (
    <header className="h-[76px] bg-white/70 backdrop-blur-xl border-b border-slate-200/60 flex items-center justify-end px-8 z-20 sticky top-0">
      <div className="flex items-center gap-6">
        <div className="flex items-center gap-3 cursor-pointer group">
          <div className="w-10 h-10 rounded-full bg-gradient-to-tr from-blue-600 to-indigo-500 text-white flex items-center justify-center font-bold shadow-md shadow-blue-500/20 group-hover:scale-105 transition-transform">
            A
          </div>
          <div className="flex flex-col text-right">
            <span className="font-semibold text-slate-900 text-[0.9rem] leading-tight">Admin</span>
            <span className="text-[0.75rem] text-slate-500 font-medium">Administrator</span>
          </div>
        </div>
      </div>
    </header>
  );
}
