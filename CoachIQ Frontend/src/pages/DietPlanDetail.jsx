import { useEffect, useState } from "react";
import { useParams, Link } from "react-router-dom";
import { useAuth } from "../context/AuthContext.jsx";
import { api } from "../lib/api.js";
import { PageHeader, StatCard, Spinner, ErrorBanner } from "../components/Ui.jsx";

export default function DietPlanDetail() {
  const { id } = useParams();
  const { token } = useAuth();
  const [diet, setDiet] = useState(null);
  const [activeDay, setActiveDay] = useState(0);
  const [error, setError] = useState("");

  useEffect(() => {
    api
      .getDiet(id, token)
      .then(setDiet)
      .catch((err) => setError(err.message));
  }, [id, token]);

  if (error) return <ErrorBanner message={error} />;
  if (!diet) {
    return (
      <div className="flex items-center gap-3 text-sm text-muted">
        <Spinner /> Loading plan...
      </div>
    );
  }

  const day = diet.days?.[activeDay];

  return (
    <div>
      <Link to="/app/diets" className="text-sm text-muted hover:underline">
        ← All diet plans
      </Link>
      <PageHeader title={diet.goal} />

      <div className="mb-8 grid grid-cols-2 gap-4 sm:max-w-lg sm:grid-cols-4">
        <StatCard label="Calories" value={diet.dailyCalories} />
        <StatCard label="Protein" value={diet.protein} unit="g" />
        <StatCard label="Carbs" value={diet.carbs} unit="g" />
        <StatCard label="Fats" value={diet.fats} unit="g" />
      </div>

      {diet.recommendations && (
        <p className="mb-8 max-w-2xl text-sm leading-relaxed text-muted">{diet.recommendations}</p>
      )}

      <div className="flex flex-wrap gap-2 border-b border-line pb-4">
        {diet.days?.map((d, i) => (
          <button
            key={d.dayNumber}
            onClick={() => setActiveDay(i)}
            className={`rounded-md px-3 py-2 text-sm font-medium transition-colors ${
              i === activeDay ? "bg-ink text-paper" : "border border-line hover:bg-ink/5"
            }`}
          >
            Day {d.dayNumber}
          </button>
        ))}
      </div>

      {day && (
        <div className="mt-6 space-y-4">
          {day.meals?.map((meal, i) => (
            <div key={meal.mealType + i} className="rounded-lg border border-line bg-white p-5">
              <div className="flex items-baseline justify-between">
                <p className="font-display font-bold">{meal.mealType}</p>
                {meal.time && <p className="text-xs text-muted">{meal.time}</p>}
              </div>
              <ul className="mt-3 space-y-1 text-sm">
                {meal.foods?.map((food, j) => (
                  <li key={food.name + j} className="flex justify-between">
                    <span>{food.name}</span>
                    <span className="tabular text-muted">{food.quantity}</span>
                  </li>
                ))}
              </ul>
              {meal.recipe && <p className="mt-3 text-sm text-muted">{meal.recipe}</p>}
            </div>
          ))}
        </div>
      )}
    </div>
  );
}