import type { Metadata } from "next";
import { Plus_Jakarta_Sans } from "next/font/google";
import "./globals.css";
import Navigation from "@/components/Navigation";

const jakartaSans = Plus_Jakarta_Sans({
  subsets: ["latin"],
  variable: "--font-jakarta",
});

export const metadata: Metadata = {
  title: "AetherTasks — Next-Gen Task Management",
  description: "A premium, glassmorphic Next.js 15 dashboard for managing tasks with Server Actions, Formik, and Tailwind CSS.",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en" className={`${jakartaSans.variable} font-sans antialiased h-full`}>
      <body className="flex flex-col min-h-screen bg-[#080710] text-slate-100 font-sans">
        <Navigation />
        <main className="flex-1 max-w-7xl w-full mx-auto px-4 sm:px-6 lg:px-8 py-8 animate-fade-in">
          {children}
        </main>
        <footer className="border-t border-slate-900 bg-black/20 py-6 text-center text-xs text-slate-500">
          <p>© 2026 AetherTasks. Created for REACT Assignment 02.</p>
        </footer>
      </body>
    </html>
  );
}
