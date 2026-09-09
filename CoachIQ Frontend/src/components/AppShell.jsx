import { NavLink, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext.jsx";

const links = [
  { to: "/app", label: "Dashboard", end: true },
  { to: "/app/workouts", label: "Workout plans" },
  { to: "/app/diets", label: "Diet plans" },
  { to: "/app/progress", label: "Progress" },
];

export default function AppShell({ children }) {
  const { profile, logout } = useAuth();
  const navigate = useNavigate();

  function handleLogout() {
    logout();
    navigate("/");
  }

  return (
    <div className="min-h-screen bg-paper">
      <div className="mx-auto flex max-w-6xl">
        <aside className="sticky top-0 hidden h-screen w-56 shrink-0 flex-col justify-between border-r border-line px-5 py-6 md:flex">
          <div>
            <a href="/" className="font-display text-lg font-bold tracking-tight">
              CoachIQ
            </a>
            <nav className="mt-10 flex flex-col gap-1">
              {links.map((link) => (
                <NavLink
                  key={link.to}
                  to={link.to}
                  end={link.end}
                  className={({ isActive }) =>
                    `rounded-md px-3 py-2 text-sm font-medium transition-colors ${
                      isActive
                        ? "bg-ink text-paper"
                        : "text-muted hover:bg-ink/5 hover:text-ink"
                    }`
                  }
                >
                  {link.label}
                </NavLink>
              ))}
            </nav>
          </div>

          <div className="border-t border-line pt-4">
            <p className="truncate text-sm font-medium">
              {profile ? `${profile.firstName} ${profile.lastName}` : "..."}
            </p>
            <p className="truncate text-xs text-muted">{profile?.email}</p>
            <button
              onClick={handleLogout}
              className="mt-3 text-sm font-medium text-coral hover:underline"
            >
              Log out
            </button>
          </div>
        </aside>

        {/* Mobile top bar */}
        <div className="fixed inset-x-0 top-0 z-10 flex items-center justify-between border-b border-line bg-paper px-4 py-3 md:hidden">
          <a href="/" className="font-display text-lg font-bold">
            CoachIQ
          </a>
          <button onClick={handleLogout} className="text-sm font-medium text-coral">
            Log out
          </button>
        </div>

        <main className="min-h-screen w-full px-4 pb-24 pt-20 md:px-10 md:pb-10 md:pt-10">
          {children}
        </main>

        {/* Mobile bottom nav */}
        <nav className="fixed inset-x-0 bottom-0 z-10 grid grid-cols-4 border-t border-line bg-paper md:hidden">
          {links.map((link) => (
            <NavLink
              key={link.to}
              to={link.to}
              end={link.end}
              className={({ isActive }) =>
                `py-3 text-center text-xs font-medium ${isActive ? "text-coral" : "text-muted"}`
              }
            >
              {link.label}
            </NavLink>
          ))}
        </nav>
      </div>
    </div>
  );
}
