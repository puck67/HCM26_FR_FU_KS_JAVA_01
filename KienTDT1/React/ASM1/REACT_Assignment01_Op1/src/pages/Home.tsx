import { Link } from "react-router-dom";

export default function Home() {
  return (
    <div className="relative overflow-hidden rounded-[32px] border border-slate-200/80 bg-white p-8 shadow-md sm:p-12">
      {/* Decorative blurred backgrounds */}
      <div className="absolute -top-24 -right-24 h-96 w-96 rounded-full bg-indigo-200/30 blur-3xl" />
      <div className="absolute -bottom-24 -left-24 h-96 w-96 rounded-full bg-violet-200/20 blur-3xl" />

      <div className="relative space-y-6 text-center max-w-3xl mx-auto">
        <span className="inline-flex items-center gap-1.5 rounded-full bg-indigo-50 px-3 py-1 text-xs font-semibold uppercase tracking-[0.2em] text-indigo-700">
          <span className="h-1.5 w-1.5 rounded-full bg-indigo-600 animate-pulse" />
          Assignment Submission
        </span>
        <h1 className="text-4xl font-extrabold text-slate-900 sm:text-5xl lg:text-6xl tracking-tight !leading-[1.15]">
          Manage Tasks with{" "}
          <span className="bg-gradient-to-r from-indigo-600 via-indigo-500 to-violet-600 bg-clip-text text-transparent">
            React & TypeScript
          </span>
        </h1>
        <p className="text-base leading-relaxed text-slate-600 sm:text-lg lg:text-xl">
          An exceptionally crafted task dashboard featuring client-side state management,
          asynchronous REST API fetching, dynamic routing, and validation using Formik & Yup.
        </p>
      </div>

      <div className="relative mt-12 grid gap-6 sm:grid-cols-2 lg:grid-cols-3">
        <div className="group rounded-2xl border border-slate-100 bg-slate-50/60 p-6 transition duration-300 hover:bg-white hover:shadow-lg hover:shadow-slate-100/80 hover:-translate-y-1">
          <div className="flex h-12 w-12 items-center justify-center rounded-xl bg-indigo-50 text-indigo-600 group-hover:bg-indigo-600 group-hover:text-white transition duration-300">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth="2" stroke="currentColor" className="w-6 h-6">
              <path strokeLinecap="round" strokeLinejoin="round" d="M9 12h3.75M9 15h3.375c.621 0 1.125-.504 1.125-1.125V11.25c0-.621-.504-1.125-1.125-1.125H9.75M8.25 21h8.25c.621 0 1.125-.504 1.125-1.125V5.625c0-.621-.504-1.125-1.125-1.125H8.25c-.621 0-1.125.504-1.125 1.125v14.25c0 .621.504 1.125 1.125 1.125z" />
            </svg>
          </div>
          <h3 className="mt-4 text-lg font-bold text-slate-800">Dynamic Task List</h3>
          <p className="mt-2 text-sm leading-relaxed text-slate-500">
            Browse and manage all active tasks from an interactive list powered by a central reducer store.
          </p>
        </div>

        <div className="group rounded-2xl border border-slate-100 bg-slate-50/60 p-6 transition duration-300 hover:bg-white hover:shadow-lg hover:shadow-slate-100/80 hover:-translate-y-1">
          <div className="flex h-12 w-12 items-center justify-center rounded-xl bg-indigo-50 text-indigo-600 group-hover:bg-indigo-600 group-hover:text-white transition duration-300">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth="2" stroke="currentColor" className="w-6 h-6">
              <path strokeLinecap="round" strokeLinejoin="round" d="M9 12.75L11.25 15 15 9.75M21 12c0 1.268-.63 2.39-1.593 3.068a3.745 3.745 0 01-1.043 3.296 3.745 3.745 0 01-3.296 1.043A3.745 3.745 0 0112 21c-1.268 0-2.39-.63-3.068-1.593a3.746 3.746 0 01-3.296-1.043 3.745 3.745 0 01-1.043-3.296A3.745 3.745 0 013 12c0-1.268.63-2.39 1.593-3.068a3.745 3.745 0 011.043-3.296 3.746 3.746 0 013.296-1.043A3.746 3.746 0 0112 3c1.268 0 2.39.63 3.068 1.593a3.746 3.746 0 013.296 1.043 3.746 3.746 0 011.043 3.296A3.745 3.745 0 0121 12z" />
            </svg>
          </div>
          <h3 className="mt-4 text-lg font-bold text-slate-800">Formik Validation</h3>
          <p className="mt-2 text-sm leading-relaxed text-slate-500">
            Create or edit tasks using real-time validation schemas powered by Formik and Yup libraries.
          </p>
        </div>

        <div className="group rounded-2xl border border-slate-100 bg-slate-50/60 p-6 transition duration-300 hover:bg-white hover:shadow-lg hover:shadow-slate-100/80 hover:-translate-y-1 sm:col-span-2 lg:col-span-1">
          <div className="flex h-12 w-12 items-center justify-center rounded-xl bg-indigo-50 text-indigo-600 group-hover:bg-indigo-600 group-hover:text-white transition duration-300">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth="2" stroke="currentColor" className="w-6 h-6">
              <path strokeLinecap="round" strokeLinejoin="round" d="M12 21a9.004 9.004 0 008.716-6.747M12 21a9.004 9.004 0 01-8.716-6.747M12 21c2.485 0 4.5-4.03 4.5-9S14.485 3 12 3m0 18c-2.485 0-4.5-4.03-4.5-9S9.515 3 12 3m0 0a8.997 8.997 0 017.843 4.582M12 3a8.997 8.997 0 00-7.843 4.582m15.686 0A11.953 11.953 0 0112 10.5c-2.998 0-5.74-1.1-7.843-2.918m15.686 0A8.959 8.959 0 0121 12c0 .778-.099 1.533-.284 2.253m0 0A17.919 17.919 0 0112 16.5c-3.162 0-6.133-.815-8.716-2.247m0 0A9.015 9.015 0 013 12c0-.778.099-1.533.284-2.253" />
            </svg>
          </div>
          <h3 className="mt-4 text-lg font-bold text-slate-800">REST API Fetching</h3>
          <p className="mt-2 text-sm leading-relaxed text-slate-500">
            Fetch initial data on application start utilizing React useEffect hooks paired with Axios requests.
          </p>
        </div>
      </div>

      <div className="relative mt-12 flex flex-col items-center justify-center gap-4 sm:flex-row">
        <Link
          to="/tasks"
          className="group inline-flex items-center justify-center gap-2 rounded-2xl bg-indigo-600 px-8 py-4 text-base font-semibold text-white shadow-md shadow-indigo-200 transition duration-200 hover:bg-indigo-700 hover:shadow-lg hover:shadow-indigo-300"
        >
          View Dashboard
          <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth="2" stroke="currentColor" className="w-5 h-5 transition duration-200 group-hover:translate-x-1">
            <path strokeLinecap="round" strokeLinejoin="round" d="M13.5 4.5L21 12m0 0l-7.5 7.5M21 12H3" />
          </svg>
        </Link>
      </div>
    </div>
  );
}

