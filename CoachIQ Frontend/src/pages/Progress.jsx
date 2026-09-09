import { useEffect, useState } from "react";
import { useAuth } from "../context/AuthContext.jsx";
import { api, todayIso } from "../lib/api.js";
import { PageHeader, Button, ErrorBanner, Field, inputClass } from "../components/Ui.jsx";

const TABS = ["Daily", "Weekly", "Assessment"];

export default function Progress() {
  const [tab, setTab] = useState("Daily");

  return (
    <div>
      <PageHeader title="Progress" subtitle="Log check-ins and see your history." />

      <div className="mb-8 flex gap-2 border-b border-line">
        {TABS.map((t) => (
          <button
            key={t}
            onClick={() => setTab(t)}
            className={`-mb-px border-b-2 px-1 pb-3 text-sm font-medium ${
              tab === t ? "border-coral text-ink" : "border-transparent text-muted"
            }`}
          >
            {t}
          </button>
        ))}
      </div>

      {tab === "Daily" && <DailyTab />}
      {tab === "Weekly" && <WeeklyTab />}
      {tab === "Assessment" && <AssessmentTab />}
    </div>
  );
}

// ---- Daily ----

function DailyTab() {
  const { token } = useAuth();
  const [entries, setEntries] = useState([]);
  const [error, setError] = useState("");
  const [saving, setSaving] = useState(false);
  const [form, setForm] = useState({
    date: todayIso(),
    sleepHours: "",
    waterIntake: "",
    proteinIntake: "",
    caloriesConsumed: "",
    workoutCompleted: false,
    cardioMinutes: "",
    steps: "",
    energyLevel: "",
    mood: "",
    notes: "",
  });

  function refresh() {
    api
      .getDailyProgress(token)
      .then((data) => setEntries([...data].sort((a, b) => b.date.localeCompare(a.date))))
      .catch((err) => setError(err.message));
  }

  useEffect(refresh, [token]);

  async function handleSubmit(e) {
    e.preventDefault();
    setError("");
    setSaving(true);
    try {
      await api.saveDailyProgress(
        {
          ...form,
          sleepHours: numOrNull(form.sleepHours),
          waterIntake: numOrNull(form.waterIntake),
          proteinIntake: numOrNull(form.proteinIntake),
          caloriesConsumed: numOrNull(form.caloriesConsumed),
          cardioMinutes: numOrNull(form.cardioMinutes),
          steps: numOrNull(form.steps),
          energyLevel: numOrNull(form.energyLevel),
        },
        token
      );
      refresh();
    } catch (err) {
      setError(err.message);
    } finally {
      setSaving(false);
    }
  }

  function update(field) {
    return (e) => {
      const value = field === "workoutCompleted" ? e.target.checked : e.target.value;
      setForm((f) => ({ ...f, [field]: value }));
    };
  }

  return (
    <div className="grid gap-10 lg:grid-cols-2">
      <form onSubmit={handleSubmit} className="space-y-4">
        <ErrorBanner message={error} />
        <Field label="Date">
          <input type="date" required className={inputClass} value={form.date} onChange={update("date")} />
        </Field>
        <div className="grid grid-cols-2 gap-4">
          <Field label="Sleep (hours)">
            <input type="number" step="0.1" className={inputClass} value={form.sleepHours} onChange={update("sleepHours")} />
          </Field>
          <Field label="Water (liters)">
            <input type="number" step="0.1" className={inputClass} value={form.waterIntake} onChange={update("waterIntake")} />
          </Field>
          <Field label="Protein (g)">
            <input type="number" className={inputClass} value={form.proteinIntake} onChange={update("proteinIntake")} />
          </Field>
          <Field label="Calories">
            <input type="number" className={inputClass} value={form.caloriesConsumed} onChange={update("caloriesConsumed")} />
          </Field>
          <Field label="Cardio (min)">
            <input type="number" className={inputClass} value={form.cardioMinutes} onChange={update("cardioMinutes")} />
          </Field>
          <Field label="Steps">
            <input type="number" className={inputClass} value={form.steps} onChange={update("steps")} />
          </Field>
          <Field label="Energy (1-10)">
            <input type="number" min="1" max="10" className={inputClass} value={form.energyLevel} onChange={update("energyLevel")} />
          </Field>
          <Field label="Mood">
            <input className={inputClass} value={form.mood} onChange={update("mood")} />
          </Field>
        </div>
        <label className="flex items-center gap-2 text-sm font-medium">
          <input type="checkbox" checked={form.workoutCompleted} onChange={update("workoutCompleted")} />
          Workout completed
        </label>
        <Field label="Notes">
          <textarea rows={2} className={inputClass} value={form.notes} onChange={update("notes")} />
        </Field>
        <Button type="submit" variant="accent" disabled={saving}>
          {saving ? "Saving..." : "Save today's log"}
        </Button>
      </form>

      <HistoryTable
        rows={entries}
        columns={[
          ["date", "Date"],
          ["sleepHours", "Sleep"],
          ["waterIntake", "Water"],
          ["caloriesConsumed", "Calories"],
          ["steps", "Steps"],
        ]}
      />
    </div>
  );
}

// ---- Weekly ----

function WeeklyTab() {
  const { token } = useAuth();
  const [entries, setEntries] = useState([]);
  const [error, setError] = useState("");
  const [saving, setSaving] = useState(false);
  const [form, setForm] = useState({ weekStartDate: todayIso(), weight: "", waist: "" });

  function refresh() {
    api
      .getWeeklyProgress(token)
      .then((data) => setEntries([...data].sort((a, b) => b.weekStartDate.localeCompare(a.weekStartDate))))
      .catch((err) => setError(err.message));
  }

  useEffect(refresh, [token]);

  async function handleSubmit(e) {
    e.preventDefault();
    setError("");
    setSaving(true);
    try {
      await api.saveWeeklyProgress(
        { ...form, weight: numOrNull(form.weight), waist: numOrNull(form.waist) },
        token
      );
      refresh();
    } catch (err) {
      setError(err.message);
    } finally {
      setSaving(false);
    }
  }

  return (
    <div className="grid gap-10 lg:grid-cols-2">
      <form onSubmit={handleSubmit} className="space-y-4">
        <ErrorBanner message={error} />
        <Field label="Week starting">
          <input
            type="date"
            required
            className={inputClass}
            value={form.weekStartDate}
            onChange={(e) => setForm((f) => ({ ...f, weekStartDate: e.target.value }))}
          />
        </Field>
        <div className="grid grid-cols-2 gap-4">
          <Field label="Weight (kg)">
            <input
              type="number"
              step="0.1"
              className={inputClass}
              value={form.weight}
              onChange={(e) => setForm((f) => ({ ...f, weight: e.target.value }))}
            />
          </Field>
          <Field label="Waist (cm)">
            <input
              type="number"
              step="0.1"
              className={inputClass}
              value={form.waist}
              onChange={(e) => setForm((f) => ({ ...f, waist: e.target.value }))}
            />
          </Field>
        </div>
        <Button type="submit" variant="accent" disabled={saving}>
          {saving ? "Saving..." : "Save this week"}
        </Button>
      </form>

      <HistoryTable
        rows={entries}
        columns={[
          ["weekStartDate", "Week of"],
          ["weight", "Weight"],
          ["waist", "Waist"],
        ]}
      />
    </div>
  );
}

// ---- Assessment ----

function AssessmentTab() {
  const { token } = useAuth();
  const [entries, setEntries] = useState([]);
  const [error, setError] = useState("");
  const [saving, setSaving] = useState(false);
  const [form, setForm] = useState({
    assessmentDate: todayIso(),
    bodyFatPercentage: "",
    muscleMass: "",
    skeletalMuscle: "",
    bodyWaterPercentage: "",
    visceralFat: "",
    metabolicAge: "",
  });

  function refresh() {
    api
      .getAssessments(token)
      .then((data) =>
        setEntries([...data].sort((a, b) => b.assessmentDate.localeCompare(a.assessmentDate)))
      )
      .catch((err) => setError(err.message));
  }

  useEffect(refresh, [token]);

  function update(field) {
    return (e) => setForm((f) => ({ ...f, [field]: e.target.value }));
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setError("");
    setSaving(true);
    try {
      await api.saveAssessment(
        {
          ...form,
          bodyFatPercentage: numOrNull(form.bodyFatPercentage),
          muscleMass: numOrNull(form.muscleMass),
          skeletalMuscle: numOrNull(form.skeletalMuscle),
          bodyWaterPercentage: numOrNull(form.bodyWaterPercentage),
          visceralFat: numOrNull(form.visceralFat),
          metabolicAge: numOrNull(form.metabolicAge),
        },
        token
      );
      refresh();
    } catch (err) {
      setError(err.message);
    } finally {
      setSaving(false);
    }
  }

  return (
    <div className="grid gap-10 lg:grid-cols-2">
      <form onSubmit={handleSubmit} className="space-y-4">
        <ErrorBanner message={error} />
        <Field label="Assessment date">
          <input type="date" required className={inputClass} value={form.assessmentDate} onChange={update("assessmentDate")} />
        </Field>
        <div className="grid grid-cols-2 gap-4">
          <Field label="Body fat %">
            <input type="number" step="0.1" className={inputClass} value={form.bodyFatPercentage} onChange={update("bodyFatPercentage")} />
          </Field>
          <Field label="Muscle mass (kg)">
            <input type="number" step="0.1" className={inputClass} value={form.muscleMass} onChange={update("muscleMass")} />
          </Field>
          <Field label="Skeletal muscle (kg)">
            <input type="number" step="0.1" className={inputClass} value={form.skeletalMuscle} onChange={update("skeletalMuscle")} />
          </Field>
          <Field label="Body water %">
            <input type="number" step="0.1" className={inputClass} value={form.bodyWaterPercentage} onChange={update("bodyWaterPercentage")} />
          </Field>
          <Field label="Visceral fat">
            <input type="number" step="0.1" className={inputClass} value={form.visceralFat} onChange={update("visceralFat")} />
          </Field>
          <Field label="Metabolic age">
            <input type="number" className={inputClass} value={form.metabolicAge} onChange={update("metabolicAge")} />
          </Field>
        </div>
        <Button type="submit" variant="accent" disabled={saving}>
          {saving ? "Saving..." : "Save assessment"}
        </Button>
      </form>

      <HistoryTable
        rows={entries}
        columns={[
          ["assessmentDate", "Date"],
          ["bodyFatPercentage", "Body fat %"],
          ["muscleMass", "Muscle mass"],
          ["metabolicAge", "Metabolic age"],
        ]}
      />
    </div>
  );
}

// ---- Shared bits ----

function numOrNull(value) {
  return value === "" ? null : Number(value);
}

function HistoryTable({ rows, columns }) {
  if (rows.length === 0) {
    return <p className="text-sm text-muted">Nothing logged yet — your entries will show up here.</p>;
  }
  return (
    <div className="overflow-hidden rounded-lg border border-line">
      <table className="w-full text-sm">
        <thead>
          <tr className="border-b border-line bg-white text-left text-xs text-muted">
            {columns.map(([key, label]) => (
              <th key={key} className="px-4 py-2 font-medium">
                {label}
              </th>
            ))}
          </tr>
        </thead>
        <tbody className="divide-y divide-line bg-white">
          {rows.map((row) => (
            <tr key={row.id} className="tabular">
              {columns.map(([key]) => (
                <td key={key} className="px-4 py-2">
                  {row[key] ?? "—"}
                </td>
              ))}
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
