/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        primary: '#1a73e8',
        'on-primary': '#ffffff',
        'primary-container': '#d3e3fd',
        'on-primary-container': '#041e49',
        surface: '#f7f9ff',
        'surface-dim': '#d7dae0',
        'surface-container-highest': '#e1e3e8',
        'on-surface': '#1b1b1f',
        'on-surface-variant': '#44474e',
        'outline-variant': '#c4c6d0',
        'inverse-surface': '#2f3033',
        'inverse-on-surface': '#f1f0f4',
        'secondary-container': '#d3e3fd',
        'on-secondary-container': '#001c38',
      },
      fontFamily: {
        sans: ['Inter', 'sans-serif'],
      },
      spacing: {
        'sidebar-width': '280px',
        'margin-desktop': '2rem',
      },
      borderRadius: {
        'none': '0',
        'sm': '0.125rem',
        DEFAULT: '4px', // Academic Precision ROUND_FOUR
        'md': '0.375rem',
        'lg': '0.5rem',
        'full': '9999px',
      }
    },
  },
  plugins: [],
}
