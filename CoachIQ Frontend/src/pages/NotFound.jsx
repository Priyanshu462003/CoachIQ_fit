import { Link } from "react-router-dom";

export default function NotFound() {
  return (
    <div className="flex min-h-screen flex-col items-center justify-center gap-3 bg-paper px-6 text-center">
      <p className="font-display text-3xl font-bold">Page not found</p>
      <p className="text-sm text-muted">The page you're looking for doesn't exist.</p>
      <Link to="/" className="mt-2 text-sm font-medium underline underline-offset-4">
        Back home
      </Link>
    </div>
  );
}
