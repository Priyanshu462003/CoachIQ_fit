import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { useAuth } from "../context/AuthContext.jsx";
import { api } from "../lib/api.js";
import { PageHeader, EmptyState, Button, Spinner, ErrorBanner } from "../components/Ui.jsx";

export default function DietPlans() {
  const { token } = useAuth();
  const [diets, setDiets] = useState(null);
  const [error, setError] = useState("");

  useEffect(() => {
    api
      .getDiets(token)
      .then(setDiets)
      .catch((err) => setError(err.message));
  }, [token]);

  return (
    <div>
      <PageHeader
        title="Diet plans"
        subtitle="Every nutrition plan CoachIQ has generated for you."
        action={
          <Link to="/app/diets/new">
            <Button variant="accent">Generate new plan</Button>
          </Link>
        }
      />

      <ErrorBanner message={error} />

      {!diets && !error && (
        <div className="flex items-center gap-3 text-sm text-muted">
          <Spinner /> Loading plans...
        </div>
      )}

      {diets && diets.length === 0 && (
        <EmptyState
          title="No diet plans yet"
          description="Tell us your goal, activity level and any allergies, and CoachIQ will build daily meals around it."
          action={
            <Link to="/app/diets/new">
              <Button variant="accent">Generate my first plan</Button>
            </Link>
          }
        />
      )}

      <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
        {diets?.map((diet) => (
          <Link
            key={diet.id}
            to={`/app/diets/${diet.id}`}
            className="rounded-lg border border-line bg-white p-5 transition-colors hover:border-ink"
          >
            <p className="font-display text-lg font-bold">{diet.goal}</p>
            <p className="tabular mt-1 text-sm text-muted">
              {diet.dailyCalories} kcal · {diet.protein}g protein · {diet.carbs}g carbs ·{" "}
              {diet.fats}g fats
            </p>
            <p className="mt-4 text-xs text-muted">{diet.days?.length || 0} days planned</p>
          </Link>
        ))}
      </div>
    </div>
  );
}