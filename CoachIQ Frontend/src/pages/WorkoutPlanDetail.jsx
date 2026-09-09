import { useEffect, useState } from "react";
import { useParams, Link } from "react-router-dom";
import { useAuth } from "../context/AuthContext.jsx";
import { api } from "../lib/api.js";
import { PageHeader, Spinner, ErrorBanner } from "../components/Ui.jsx";

export default function WorkoutPlanDetail() {
  const { id } = useParams();
  const { token } = useAuth();
  const [plan, setPlan] = useState(null);
  const [activeDay, setActiveDay] = useState(0);
  const [error, setError] = useState("");

  useEffect(() => {
    api
      .getWorkoutPlan(id, token)
      .then(setPlan)
      .catch((err) => setError(err.message));
  }, [id, token]);

  if (error) return <ErrorBanner message={error} />;
  if (!plan) {
    return (
      <div className="flex items-center gap-3 text-sm text-muted">
        <Spinner /> Loading plan...
      </div>
    );
  }

  const day = plan.days?.[activeDay];

  return (
    <div>
      <Link to="/app/workouts" className="text-sm text-muted hover:underline">
        ← All workout plans
      </Link>
      <PageHeader title={plan.goal} subtitle={`${plan.experienceLevel} · ${plan.trainingDaysPerWeek} days/week`} />

      {plan.overallStrategy && (
        <p className="mb-8 max-w-2xl text-sm leading-relaxed text-muted">{plan.overallStrategy}</p>
      )}

      <div className="flex flex-wrap gap-2 border-b border-line pb-4">
        {plan.days?.map((d, i) => (
          <button
            key={d.dayName + i}
            onClick={() => setActiveDay(i)}
            className={`rounded-md px-3 py-2 text-sm font-medium transition-colors ${
              i === activeDay ? "bg-ink text-paper" : "border border-line hover:bg-ink/5"
            }`}
          >
            {d.dayName}
          </button>
        ))}
      </div>

      {day && (
        <div className="mt-6">
          <p className="font-display text-lg font-bold">{day.focusArea}</p>
          {day.dayNotes && <p className="mt-1 text-sm text-muted">{day.dayNotes}</p>}

          <div className="mt-4 divide-y divide-line rounded-lg border border-line bg-white">
            {day.exercises?.map((ex, i) => (
              <div key={ex.name + i} className="p-5">
                <div className="flex flex-wrap items-baseline justify-between gap-2">
                  <p className="font-medium">{ex.name}</p>
                  <p className="tabular text-sm text-muted">
                    {ex.sets} × {ex.reps} · rest {ex.restSeconds}s
                  </p>
                </div>
                {ex.intensity && <p className="mt-1 text-xs text-muted">Intensity: {ex.intensity}</p>}
                {ex.techniqueTips && <p className="mt-2 text-sm">{ex.techniqueTips}</p>}
                {ex.alternativeExercise && (
                  <p className="mt-2 text-xs text-muted">Alternative: {ex.alternativeExercise}</p>
                )}
              </div>
            ))}
          </div>
        </div>
      )}

      <div className="mt-10 grid gap-6 md:grid-cols-2">
        {plan.progressionGuidelines && (
          <InfoBlock title="Progression" body={plan.progressionGuidelines} />
        )}
        {plan.recoveryGuidelines && <InfoBlock title="Recovery" body={plan.recoveryGuidelines} />}
        {plan.trainerNotes && <InfoBlock title="Trainer notes" body={plan.trainerNotes} />}
      </div>
    </div>
  );
}

function InfoBlock({ title, body }) {
  return (
    <div className="rounded-lg border border-line bg-white p-5">
      <p className="font-display font-bold">{title}</p>
      <p className="mt-2 text-sm leading-relaxed text-muted">{body}</p>
    </div>
  );
}
