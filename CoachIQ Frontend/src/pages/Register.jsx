import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext.jsx";
import { Button, ErrorBanner, Field, inputClass } from "../components/Ui.jsx";

export default function Register() {
  const { register } = useAuth();
  const navigate = useNavigate();
  const [form, setForm] = useState({ firstname: "", lastname: "", email: "", password: "" });
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
      await register(form);
      navigate("/app");
    } catch (err) {
      setError(err.message || "Could not create your account.");
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <div className="flex min-h-screen items-center justify-center bg-paper px-6 py-10">
      <div className="w-full max-w-sm">
        <Link to="/" className="font-display text-lg font-bold">
          CoachIQ
        </Link>
        <h1 className="mt-8 font-display text-2xl font-bold">Create your account</h1>
        <p className="mt-1 text-sm text-muted">One form, then straight to your first plan.</p>

        <form onSubmit={handleSubmit} className="mt-8 space-y-4">
          <ErrorBanner message={error} />
          <div className="grid grid-cols-2 gap-4">
            <Field label="First name">
              <input
                required
                className={inputClass}
                value={form.firstname}
                onChange={update("firstname")}
              />
            </Field>
            <Field label="Last name">
              <input
                required
                className={inputClass}
                value={form.lastname}
                onChange={update("lastname")}
              />
            </Field>
          </div>
          <Field label="Email">
            <input
              type="email"
              required
              className={inputClass}
              value={form.email}
              onChange={update("email")}
            />
          </Field>
          <Field label="Password">
            <input
              type="password"
              required
              minLength={6}
              className={inputClass}
              value={form.password}
              onChange={update("password")}
            />
            <span className="mt-1 block text-xs text-muted">At least 6 characters.</span>
          </Field>
          <Button type="submit" variant="accent" className="w-full" disabled={submitting}>
            {submitting ? "Creating account..." : "Create account"}
          </Button>
        </form>

        <p className="mt-6 text-sm text-muted">
          Already have an account?{" "}
          <Link to="/login" className="font-medium text-ink underline underline-offset-4">
            Log in
          </Link>
        </p>
      </div>
    </div>
  );
}
