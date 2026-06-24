import { useCallback } from "react";
import Particles from "react-tsparticles";
import { loadSlim } from "tsparticles-slim";
import type { Engine } from "tsparticles-engine";

export function ParticlesBackground() {
  const particlesInit = useCallback(async (engine: Engine) => {
    await loadSlim(engine);
  }, []);

  return (
    <Particles
      id="tsparticles"
      init={particlesInit}
      options={{
        fullScreen: { enable: false },
        fpsLimit: 60,
        particles: {
          color: { value: ["#ffffff", "#e2f1ff", "#ffd5a4"] },
          links: { enable: false },
          move: { enable: true, speed: 0.3, direction: "none", random: true, straight: false, outModes: { default: "out" } },
          number: { density: { enable: true, area: 1000 }, value: 150 },
          opacity: { 
            value: { min: 0.1, max: 0.8 },
            animation: { enable: true, speed: 0.5, sync: false }
          },
          shape: { type: "circle" },
          size: { 
            value: { min: 0.5, max: 2.5 },
            animation: { enable: true, speed: 1, sync: false }
          },
        },
        interactivity: {
          events: {
            onHover: { enable: true, mode: "bubble" },
            onClick: { enable: true, mode: "repulse" },
          },
          modes: {
            bubble: { distance: 200, size: 4, duration: 2, opacity: 1 },
            repulse: { distance: 200, duration: 0.4 },
          },
        },
        detectRetina: true,
      }}
      style={{
        position: "fixed",
        top: 0,
        left: 0,
        width: "100vw",
        height: "100vh",
        zIndex: -1, /* Very back */
        pointerEvents: "auto"
      }}
    />
  );
}
