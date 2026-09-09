// A handful of tiny, boring, reusable pieces shared across pages.
// Kept in one file on purpose — each is a few lines, splitting them out
// would just add import noise for no benefit.

export function PageHeader({ title, subtitle, action }) {
  return (
    <div className="mb-8 flex flex-wrap items-end justify-between gap-4">
      <div>
        <h1 className="font-display text-2xl font-bold tracking-tight">{title}</h1>
        {subtitle && <p className="mt-1 text-sm text-muted">{subtitle}</p>}
      </div>
      {action}
    </div>
  );
}

export function StatCard({ label, value, unit }) {
  return (
    <div className="rounded-lg border border-line bg-white px-5 py-4">
      <p className="text-xs font-medium text-muted">{label}</p>
      <p className="tabular mt-1 font-display text-2xl font-bold">
        {value}
        {unit && <span className="ml-1 text-sm font-normal text-muted">{unit}</span>}
      </p>
    </div>
  );
}

export function EmptyState({ title, description, action }) {
  return (
    <div className="rounded-lg border border-dashed border-line px-8 py-14 text-center">
      <p className="font-display text-lg font-semibold">{title}</p>
      <p className="mx-auto mt-1 max-w-sm text-sm text-muted">{description}</p>
      {action && <div className="mt-5">{action}</div>}
    </div>
  );
}

export function ErrorBanner({ message }) {
  if (!message) return null;
  return (
    <div className="mb-6 rounded-md border border-coral/30 bg-coral/5 px-4 py-3 text-sm text-coral-dark">
      {message}
    </div>
  );
}

export function Spinner() {
  return (
    <div
      className="h-5 w-5 animate-spin rounded-full border-2 border-ink/20 border-t-ink"
      role="status"
      aria-label="Loading"
    />
  );
}

export function Button({ children, variant = "primary", className = "", ...props }) {
  const styles = {
    primary: "bg-ink text-paper hover:bg-ink/90",
    accent: "bg-coral text-white hover:bg-coral-dark",
    ghost: "border border-line text-ink hover:bg-ink/5",
  };
  return (
    <button
      className={`inline-flex items-center justify-center gap-2 rounded-md px-4 py-2.5 text-sm font-medium transition-colors disabled:cursor-not-allowed disabled:opacity-50 ${styles[variant]} ${className}`}
      {...props}
    >
      {children}
    </button>
  );
}

export function Field({ label, children }) {
  return (
    <label className="block">
      <span className="mb-1.5 block text-sm font-medium">{label}</span>
      {children}
    </label>
  );
}

export const inputClass =
  "w-full rounded-md border border-line bg-white px-3 py-2.5 text-sm outline-none transition-colors focus:border-ink";
