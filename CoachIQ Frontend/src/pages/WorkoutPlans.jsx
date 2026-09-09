import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { useAuth } from "../context/AuthContext.jsx";
import { api } from "../lib/api.js";
import { PageHeader, EmptyState, Button, Spinner, ErrorBanner } from "../components/Ui.jsx";

export default function WorkoutPlans() {
  const { token } = useAuth();
  const [plans, setPlans] = useState(null);
  const [error, setError] = useState("");

  useEffect(() => {
    api
      .getWorkoutPlans(token)
      .then(setPlans)
      .catch((err) => setError(err.message));
  }, [token]);

  return (
    <div>
      <PageHeader
        title="Workout plans"
        subtitle="Every plan CoachIQ has generated for you."
        action={
          <Link to="/app/workouts/new">
            <Button variant="accent">Generate new plan</Button>
          </Link>
        }
      />

      <ErrorBanner message={error} />

      {!plans && !error && (
        <div className="flex items-center gap-3 text-sm text-muted">
          <Spinner /> Loading plans...
        </div>
      )}

      {plans && plans.length === 0 && (
        <EmptyState
          title="No workout plans yet"
          description="Tell us your goal, experience level and equipment, and CoachIQ will build a full split around it."
          action={
            <Link to="/app/workouts/new">
              <Button variant="accent">Generate my first plan</Button>
            </Link>
          }
        />
      )}

      <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
        {plans?.map((plan) => (
          <Link
            key={plan.id}
            to={`/app/workouts/${plan.id}`}
            className="rounded-lg border border-line bg-white p-5 transition-colors hover:border-ink"
          >
            <p className="font-display text-lg font-bold">{plan.goal}</p>
            <p className="mt-1 text-sm text-muted">
              {plan.experienceLevel} · {plan.trainingDaysPerWeek} days/week
            </p>
            <p className="mt-4 text-xs text-muted">
              {plan.days?.length || 0} training days planned
            </p>
          </Link>
        ))}
      </div>
    </div>
  );
}
