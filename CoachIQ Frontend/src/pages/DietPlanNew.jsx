import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext.jsx";
import { api } from "../lib/api.js";
import { PageHeader, Button, ErrorBanner, Field, inputClass } from "../components/Ui.jsx";

const initial = {
  age: "",
  weight: "",
  height: "",
  gender: "",
  goal: "",
  activityLevel: "",
  allergies: "",
  dietaryPreferences: "",
  medicalConditions: "",
  mealsPerDay: "",
  additionalNotes: "",
};

function toList(value) {
  return value
    .split(",")
    .map((v) => v.trim())
    .filter(Boolean);
}

export default function DietPlanNew() {
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
      await api.createDiet(
        {
          ...form,
          age: Number(form.age),
          weight: Number(form.weight),
          height: Number(form.height),
          mealsPerDay: Number(form.mealsPerDay),
          allergies: toList(form.allergies),
          dietaryPreferences: toList(form.dietaryPreferences),
          medicalConditions: toList(form.medicalConditions),
        },
        token
      );
      // Same story as workout plans: the endpoint returns a message, not
      // an id, so we refetch and open the newest diet plan.
      const diets = await api.getDiets(token);
      const newest = [...diets].sort((a, b) => b.createdAt.localeCompare(a.createdAt))[0];
      navigate(newest ? `/app/diets/${newest.id}` : "/app/diets");
    } catch (err) {
      setError(err.message || "Could not generate your plan. Try again.");
      setSubmitting(false);
    }
  }

  return (
    <div className="max-w-2xl">
      <PageHeader title="Generate a diet plan" subtitle="Takes about 20–30 seconds." />
      <ErrorBanner message={error} />

      <form onSubmit={handleSubmit} className="space-y-6">
        <div className="grid grid-cols-2 gap-4 sm:grid-cols-4">
          <Field label="Age">
            <input required type="number" className={inputClass} value={form.age} onChange={update("age")} />
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
              <option>Fat Loss</option>
              <option>Muscle Gain</option>
              <option>Maintenance</option>
              <option>Performance</option>
            </select>
          </Field>
          <Field label="Activity level">
            <select
              required
              className={inputClass}
              value={form.activityLevel}
              onChange={update("activityLevel")}
            >
              <option value="">Select</option>
              <option>Sedentary</option>
              <option>Lightly Active</option>
              <option>Moderately Active</option>
              <option>Very Active</option>
            </select>
          </Field>
        </div>

        <Field label="Meals per day">
          <input
            required
            type="number"
            min="1"
            max="8"
            className={inputClass}
            value={form.mealsPerDay}
            onChange={update("mealsPerDay")}
          />
        </Field>

        <div className="grid gap-4 sm:grid-cols-2">
          <Field label="Allergies (comma-separated)">
            <input className={inputClass} value={form.allergies} onChange={update("allergies")} />
          </Field>
          <Field label="Dietary preferences (comma-separated)">
            <input
              className={inputClass}
              placeholder="Vegetarian, Halal"
              value={form.dietaryPreferences}
              onChange={update("dietaryPreferences")}
            />
          </Field>
        </div>

        <Field label="Medical conditions (comma-separated)">
          <input
            className={inputClass}
            value={form.medicalConditions}
            onChange={update("medicalConditions")}
          />
        </Field>

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
