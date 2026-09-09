import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { useAuth } from "../context/AuthContext.jsx";
import { api, todayIso } from "../lib/api.js";
import { PageHeader, StatCard, EmptyState, Button, Spinner } from "../components/Ui.jsx";

// There's no single "/dashboard" endpoint on the backend yet, so this page
// makes a handful of calls in parallel and combines them client-side.
// If usage grows, this logic is the first candidate to move server-side.
export default function Dashboard() {
  const { token, profile } = useAuth();
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    let active = true;
    Promise.allSettled([
      api.getWorkoutPlans(token),
      api.getDiets(token),
      api.getDailyProgress(token),
      api.getLatestAnalysis(token),
    ]).then(([workouts, diets, daily, analysis]) => {
      if (!active) return;
      setData({
        workouts: workouts.status === "fulfilled" ? workouts.value : [],
        diets: diets.status === "fulfilled" ? diets.value : [],
        daily: daily.status === "fulfilled" ? daily.value : [],
        analysis: analysis.status === "fulfilled" ? analysis.value : null,
      });
      setLoading(false);
    });
    return () => {
      active = false;
    };
  }, [token]);

  if (loading) {
    return (
      <div className="flex items-center gap-3 text-sm text-muted">
        <Spinner /> Loading your dashboard...
      </div>
    );
  }

  const latestDaily = [...(data.daily || [])].sort((a, b) => b.date.localeCompare(a.date))[0];
  const hasAnyPlan = data.workouts.length > 0 || data.diets.length > 0;

  return (
    <div>
      <PageHeader
        title={`Welcome back${profile ? `, ${profile.firstName}` : ""}`}
        subtitle="Here's where things stand today."
      />

      {!hasAnyPlan && (
        <EmptyState
          title="You don't have a plan yet"
          description="Generate a workout plan and a diet plan to get started — it takes about a minute."
          action={
            <div className="flex justify-center gap-3">
              <Link to="/app/workouts/new">
                <Button variant="accent">Build workout plan</Button>
              </Link>
              <Link to="/app/diets/new">
                <Button variant="ghost">Build diet plan</Button>
              </Link>
            </div>
          }
        />
      )}

      <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-4">
        <StatCard label="Workout plans" value={data.workouts.length} />
        <StatCard label="Diet plans" value={data.diets.length} />
        <StatCard label="Days logged" value={data.daily.length} />
        <StatCard
          label="Today's log"
          value={latestDaily?.date === todayIso() ? "Done" : "Not yet"}
        />
      </div>

      <div className="mt-10 grid gap-6 lg:grid-cols-2">
        <div className="rounded-lg border border-line bg-white p-6">
          <div className="flex items-center justify-between">
            <h2 className="font-display text-lg font-bold">Latest check-in</h2>
            <Link to="/app/progress" className="text-sm font-medium underline underline-offset-4">
              Log today
            </Link>
          </div>
          {latestDaily ? (
            <dl className="mt-4 grid grid-cols-2 gap-4 text-sm">
              <Row label="Date" value={latestDaily.date} />
              <Row label="Sleep" value={fmt(latestDaily.sleepHours, "h")} />
              <Row label="Water" value={fmt(latestDaily.waterIntake, "L")} />
              <Row label="Calories" value={fmt(latestDaily.caloriesConsumed, "kcal")} />
              <Row label="Steps" value={fmt(latestDaily.steps)} />
              <Row label="Workout" value={latestDaily.workoutCompleted ? "Completed" : "Skipped"} />
            </dl>
          ) : (
            <p className="mt-4 text-sm text-muted">No check-ins logged yet.</p>
          )}
        </div>

        <div className="rounded-lg border border-line bg-white p-6">
          <h2 className="font-display text-lg font-bold">Monthly analysis</h2>
          {data.analysis ? (
            <div className="mt-4 space-y-4 text-sm">
              <p className="text-muted">For {data.analysis.reportMonth}</p>
              <p>{data.analysis.summary}</p>

              <AnalysisSection label="Strengths" body={data.analysis.strengths} />
              <AnalysisSection label="Areas to improve" body={data.analysis.improvements} />
              <AnalysisSection label="Workout advice" body={data.analysis.workoutAdvice} />
              <AnalysisSection label="Nutrition advice" body={data.analysis.nutritionAdvice} />
              <AnalysisSection label="Recovery advice" body={data.analysis.recoveryAdvice} />

              {data.analysis.motivation && (
                <p className="border-t border-line pt-4 text-sm italic text-muted">
                  {data.analysis.motivation}
                </p>
              )}
            </div>
          ) : (
            <p className="mt-4 text-sm text-muted">
              Your first monthly report will appear here once it's generated — keep logging daily
              progress so it has something to analyze.
            </p>
          )}
        </div>
      </div>
    </div>
  );
}

function AnalysisSection({ label, body }) {
  if (!body) return null;
  return (
    <div>
      <p className="text-xs font-medium uppercase tracking-wide text-muted">{label}</p>
      <p className="mt-1">{body}</p>
    </div>
  );
}

function Row({ label, value }) {
  return (
    <div>
      <dt className="text-xs text-muted">{label}</dt>
      <dd className="tabular font-medium">{value}</dd>
    </div>
  );
}

function fmt(value, unit = "") {
  if (value === null || value === undefined) return "—";
  return unit ? `${value}${unit}` : value;
}
