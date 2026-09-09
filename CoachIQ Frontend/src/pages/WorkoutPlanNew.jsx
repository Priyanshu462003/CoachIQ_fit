import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext.jsx";
import { api } from "../lib/api.js";
import { PageHeader, Button, ErrorBanner, Field, inputClass } from "../components/Ui.jsx";

const initial = {
  age: "",
  gender: "",
  weight: "",
  height: "",
  goal: "",
  experienceLevel: "",
  trainingDaysPerWeek: "",
  sessionDurationMinutes: "",
  workoutLocation: "",
  availableEquipment: "",
  injuries: "",
  medicalConditions: "",
  focusAreas: "",
  cardioPreference: "",
  additionalNotes: "",
};

// Comma-separated text fields get split into arrays before hitting the API
// — simpler to build and explain than a multi-select widget.
function toList(value) {
  return value
    .split(",")
    .map((v) => v.trim())
    .filter(Boolean);
}

export default function WorkoutPlanNew() {
  const { token } = useAuth();
  const navigate = useNavigate();
  const [form, setForm] = useState(initial);
  const [error, setError] = useState("");
  const [submitting, setSubmitting] = useState(false);

  function update(field) {
    return (e) => setForm((f) => ({ ...f, [field]: e.target.value }));
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setError("");
    setSubmitting(true);
    try {
      await api.createWorkoutPlan(
        {
          ...form,
          age: Number(form.age),
          weight: Number(form.weight),
          height: Number(form.height),
          trainingDaysPerWeek: Number(form.trainingDaysPerWeek),
          sessionDurationMinutes: Number(form.sessionDurationMinutes),
          availableEquipment: toList(form.availableEquipment),
          injuries: toList(form.injuries),
          medicalConditions: toList(form.medicalConditions),
          focusAreas: toList(form.focusAreas),
        },
        token
      );
      // The create endpoint only returns a confirmation message, not the
      // new plan's id — so we refetch the list and open the newest plan.
      const plans = await api.getWorkoutPlans(token);
      const newest = [...plans].sort((a, b) => b.createdAt.localeCompare(a.createdAt))[0];
      navigate(newest ? `/app/workouts/${newest.id}` : "/app/workouts");
    } catch (err) {
      setError(err.message || "Could not generate your plan. Try again.");
      setSubmitting(false);
    }
  }

  return (
    <div className="max-w-2xl">
      <PageHeader title="Generate a workout plan" subtitle="Takes about 20–30 seconds." />
      <ErrorBanner message={error} />

      <form onSubmit={handleSubmit} className="space-y-6">
        <div className="grid grid-cols-2 gap-4 sm:grid-cols-4">
          <Field label="Age">
            <input required type="number" min="13" className={inputClass} value={form.age} onChange={update("age")} />
          </Field>
          <Field label="Gender">
            <select required className={inputClass} value={form.gender} onChange={update("gender")}>
              <option value="">Select</option>
              <option>Male</option>
              <option>Female</option>
              <option>Other</option>
            </select>
          </Field>
          <Field label="Weight (kg)">
            <input required type="number" className={inputClass} value={form.weight} onChange={update("weight")} />
          </Field>
          <Field label="Height (cm)">
            <input required type="number" className={inputClass} value={form.height} onChange={update("height")} />
          </Field>
        </div>

        <div className="grid gap-4 sm:grid-cols-2">
          <Field label="Main goal">
            <select required className={inputClass} value={form.goal} onChange={update("goal")}>
              <option value="">Select</option>
              <option>Strength</option>
              <option>Hypertrophy</option>
              <option>Fat Loss</option>
              <option>Endurance</option>
              <option>General Fitness</option>
            </select>
          </Field>
          <Field label="Experience level">
            <select
              required
              className={inputClass}
              value={form.experienceLevel}
              onChange={update("experienceLevel")}
            >
              <option value="">Select</option>
              <option>Beginner</option>
              <option>Intermediate</option>
              <option>Advanced</option>
            </select>
          </Field>
        </div>

        <div className="grid grid-cols-2 gap-4 sm:grid-cols-3">
          <Field label="Training days / week">
            <input
              required
              type="number"
              min="1"
              max="7"
              className={inputClass}
              value={form.trainingDaysPerWeek}
              onChange={update("trainingDaysPerWeek")}
            />
          </Field>
          <Field label="Session length (min)">
            <input
              required
              type="number"
              className={inputClass}
              value={form.sessionDurationMinutes}
              onChange={update("sessionDurationMinutes")}
            />
          </Field>
          <Field label="Location">
            <select
              required
              className={inputClass}
              value={form.workoutLocation}
              onChange={update("workoutLocation")}
            >
              <option value="">Select</option>
              <option>Gym</option>
              <option>Home</option>
              <option>Both</option>
            </select>
          </Field>
        </div>

        <Field label="Available equipment (comma-separated)">
          <input
            className={inputClass}
            placeholder="Dumbbells, Barbell, Bench"
            value={form.availableEquipment}
            onChange={update("availableEquipment")}
          />
        </Field>

        <div className="grid gap-4 sm:grid-cols-2">
          <Field label="Previous injuries (comma-separated)">
            <input className={inputClass} value={form.injuries} onChange={update("injuries")} />
          </Field>
          <Field label="Medical conditions (comma-separated)">
            <input
              className={inputClass}
              value={form.medicalConditions}
              onChange={update("medicalConditions")}
            />
          </Field>
        </div>

        <div className="grid gap-4 sm:grid-cols-2">
          <Field label="Focus areas (comma-separated)">
            <input
              className={inputClass}
              placeholder="Chest, Shoulders"
              value={form.focusAreas}
              onChange={update("focusAreas")}
            />
          </Field>
          <Field label="Cardio preference">
            <input className={inputClass} value={form.cardioPreference} onChange={update("cardioPreference")} />
          </Field>
        </div>

        <Field label="Anything else CoachIQ should know">
          <textarea
            rows={3}
            className={inputClass}
            value={form.additionalNotes}
            onChange={update("additionalNotes")}
          />
        </Field>

        <Button type="submit" variant="accent" disabled={submitting}>
          {submitting ? "Generating your plan..." : "Generate plan"}
        </Button>
      </form>
    </div>
  );
}
