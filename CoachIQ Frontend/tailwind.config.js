/** @type {import('tailwindcss').Config} */
export default {
  content: ["./index.html", "./src/**/*.{js,jsx}"],
  theme: {
    extend: {
      colors: {
        ink: "#14181C",
        paper: "#F6F4EF",
        line: "#DDD7C8",
        coral: {
          DEFAULT: "#FF4F2E",
          dark: "#E23F20",
        },
        pine: {
          DEFAULT: "#1F6F54",
          light: "#E4EFE9",
        },
        muted: "#6B675E",
      },
      fontFamily: {
        display: ["Space Grotesk", "sans-serif"],
        body: ["Inter", "sans-serif"],
      },
    },
  },
  plugins: [],
};
