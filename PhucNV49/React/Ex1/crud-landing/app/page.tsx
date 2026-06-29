import { Nav } from "./components/Nav";
import { Hero } from "./components/Hero";
import { Features } from "./components/Features";
import { ComponentsSection } from "./components/ComponentsSection";
import { UsageSection } from "./components/UsageSection";
import { CTA } from "./components/CTA";
import { Footer } from "./components/Footer";

export default function Page() {
  return (
    <>
      <Nav />
      <main>
        <Hero />
        <Features />
        <ComponentsSection />
        <UsageSection />
        <CTA />
      </main>
      <Footer />
    </>
  );
}
