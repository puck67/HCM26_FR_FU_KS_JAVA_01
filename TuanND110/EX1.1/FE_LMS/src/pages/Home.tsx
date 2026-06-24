export default function Home() {
  return (
    <div className="flex flex-col items-center justify-center flex-1 w-full animate-fade-in py-12">
      {/* Content Container */}
      <div className="bg-white rounded-lg shadow-sm border border-outline-variant w-full max-w-5xl p-16 text-center">
        <p className="text-on-surface-variant font-medium mb-12 uppercase tracking-widest opacity-40">Body</p>
        <h2 className="text-4xl md:text-5xl font-extrabold text-on-surface leading-tight">
          WELCOME TO LMS MANAGEMENT SYSTEM - <br />
          <span className="text-primary">BASED ON SPRING FRAMEWORK</span>
        </h2>
        <p className="mt-16 text-on-surface-variant font-medium uppercase tracking-widest opacity-40">Footer Placeholder</p>
      </div>
    </div>
  );
}
