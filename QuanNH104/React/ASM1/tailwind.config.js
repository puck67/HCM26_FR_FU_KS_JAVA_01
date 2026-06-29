/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      fontFamily: {
        sans: ['Outfit', 'sans-serif'],
        orbitron: ['Orbitron', 'sans-serif'],
      },
      colors: {
        cyber: {
          bg: '#08090d',
          card: 'rgba(17, 20, 28, 0.7)',
          cardHover: 'rgba(26, 30, 43, 0.85)',
          purple: '#7c3aed',
          pink: '#ff007f',
          cyan: '#00f2fe',
          success: '#10b981',
          warning: '#f59e0b',
          danger: '#ef4444',
        }
      }
    },
  },
  plugins: [],
}
