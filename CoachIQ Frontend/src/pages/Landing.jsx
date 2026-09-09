import { Link } from "react-router-dom";
import { Button } from "../components/Ui.jsx";

export default function Landing() {
  return (
    <div className="bg-paper">
      <header className="mx-auto flex max-w-6xl items-center justify-between px-6 py-6">
        <span className="font-display text-lg font-bold tracking-tight">CoachIQ</span>
        <nav className="flex items-center gap-3">
          <Link to="/login" className="px-3 py-2 text-sm font-medium text-ink hover:opacity-70">
            Log in
          </Link>
          <Link to="/register">
            <Button variant="accent">Start free</Button>
          </Link>
        </nav>
      </header>

      <Hero />
      <HowItWorks />
      <Features />
      <ProofBand />
      <Pricing />
      <FinalCta />
      <Footer />
    </div>
  );
}

function Hero() {
  return (
    <section className="mx-auto grid max-w-6xl gap-12 px-6 pb-20 pt-8 md:grid-cols-2 md:items-center md:pt-16">
      <div>
        <h1 className="font-display text-4xl font-bold leading-[1.08] tracking-tight md:text-5xl">
          A training plan that adjusts to how your week actually goes.
        </h1>
        <p className="mt-5 max-w-md text-base leading-relaxed text-muted">
          CoachIQ builds your workout and nutrition plan from your goals and equipment, then
          rewrites its advice every month based on the progress you actually log — not a template
          you outgrow in two weeks.
        </p>
        <div className="mt-8 flex flex-wrap items-center gap-4">
          <Link to="/register">
            <Button variant="accent" className="px-6 py-3 text-base">
              Build my plan
            </Button>
          </Link>
          <Link to="/login" className="text-sm font-medium underline underline-offset-4">
            I already have an account
          </Link>
        </div>
      </div>

      <ReadoutCard />
    </section>
  );
}

function ReadoutCard() {
  return (
    <div className="rounded-xl border border-line bg-white p-6 shadow-[0_1px_0_0_#DDD7C8]">
      <div className="flex items-center justify-between border-b border-line pb-4">
        <div>
          <p className="text-xs font-medium text-muted">Today · Push day</p>
          <p className="font-display text-lg font-bold">Chest, shoulders, triceps</p>
        </div>
        <span className="rounded-full bg-pine-light px-3 py-1 text-xs font-medium text-pine">
          On track
        </span>
      </div>

      <ul className="divide-y divide-line">
        {[
          { name: "Barbell bench press", sets: "4 × 6-8", note: "RPE 8" },
          { name: "Incline dumbbell press", sets: "3 × 10-12", note: "RPE 7" },
          { name: "Cable lateral raise", sets: "3 × 15", note: "RPE 8" },
        ].map((ex) => (
          <li key={ex.name} className="flex items-center justify-between py-3 text-sm">
            <span className="font-medium">{ex.name}</span>
            <span className="tabular text-muted">
              {ex.sets} <span className="ml-2 text-xs">{ex.note}</span>
            </span>
          </li>
        ))}
      </ul>

      <div className="mt-4 grid grid-cols-3 gap-3 border-t border-line pt-4">
        {[
          { label: "Calories", value: "2,340" },
          { label: "Protein", value: "182g" },
          { label: "Sleep avg", value: "7.1h" },
        ].map((s) => (
          <div key={s.label}>
            <p className="tabular font-display text-lg font-bold">{s.value}</p>
            <p className="text-xs text-muted">{s.label}</p>
          </div>
        ))}
      </div>
    </div>
  );
}

function HowItWorks() {
  const steps = [
    {
      n: "01",
      title: "Tell us where you're starting",
      body: "Goal, experience level, equipment, injuries — the same intake a real coach would ask for.",
    },
    {
      n: "02",
      title: "Get a full plan in seconds",
      body: "A complete training split and a daily meal structure, built to your numbers, not a generic template.",
    },
    {
      n: "03",
      title: "Log progress, get monthly direction",
      body: "Daily check-ins and weekly measurements feed a monthly report on what to change next.",
    },
  ];
  return (
    <section className="border-t border-line bg-white py-20">
      <div className="mx-auto max-w-6xl px-6">
        <h2 className="font-display text-2xl font-bold tracking-tight">How it works</h2>
        <div className="mt-10 grid gap-10 md:grid-cols-3">
          {steps.map((s) => (
            <div key={s.n}>
              <p className="font-display text-sm text-coral">{s.n}</p>
              <p className="mt-2 font-display text-lg font-bold">{s.title}</p>
              <p className="mt-2 text-sm leading-relaxed text-muted">{s.body}</p>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
}

function Features() {
  const items = [
    {
      title: "Workout plans",
      body: "A weekly split built around your training days, equipment and any injuries you flag — with progression built in, not left for you to guess.",
    },
    {
      title: "Diet plans",
      body: "Daily meals mapped to your calorie and macro targets, adjusted for allergies and dietary preferences.",
    },
    {
      title: "Progress tracking",
      body: "Log sleep, protein, water, steps and mood daily; weight and waist weekly; full body composition on your own schedule.",
    },
    {
      title: "Monthly report",
      body: "One AI-written summary of the month — what worked, what to change, delivered as a PDF to your inbox.",
    },
  ];
  return (
    <section className="mx-auto max-w-6xl px-6 py-20">
      <h2 className="font-display text-2xl font-bold tracking-tight">
        Everything a coach would track, without the hourly rate
      </h2>
      <div className="mt-10 grid gap-px overflow-hidden rounded-lg border border-line bg-line md:grid-cols-2">
        {items.map((f) => (
          <div key={f.title} className="bg-paper p-8">
            <p className="font-display text-lg font-bold">{f.title}</p>
            <p className="mt-2 text-sm leading-relaxed text-muted">{f.body}</p>
          </div>
        ))}
      </div>
    </section>
  );
}

function ProofBand() {
  const stats = [
    { value: "6", unit: "months", label: "of plan structure generated per request" },
    { value: "3", unit: "modules", label: "workout, nutrition and progress in one account" },
    { value: "1", unit: "report", label: "AI monthly analysis, sent automatically" },
  ];
  return (
    <section className="bg-ink py-16 text-paper">
      <div className="mx-auto grid max-w-6xl gap-8 px-6 md:grid-cols-3">
        {stats.map((s) => (
          <div key={s.label}>
            <p className="tabular font-display text-4xl font-bold">
              {s.value}
              <span className="ml-1 text-lg text-paper/60">{s.unit}</span>
            </p>
            <p className="mt-2 text-sm text-paper/70">{s.label}</p>
          </div>
        ))}
      </div>
    </section>
  );
}

function Pricing() {
  return (
    <section className="mx-auto max-w-6xl px-6 py-20">
      <h2 className="font-display text-2xl font-bold tracking-tight">Simple pricing</h2>
      <div className="mt-10 max-w-md rounded-lg border border-line bg-white p-8">
        <p className="font-display text-lg font-bold">Free, while we're in beta</p>
        <p className="mt-2 text-sm text-muted">
          Full access to workout plans, diet plans, progress tracking and monthly reports.
        </p>
        <p className="tabular mt-6 font-display text-4xl font-bold">
          $0<span className="text-base font-normal text-muted"> / month</span>
        </p>
        <Link to="/register" className="mt-6 block">
          <Button variant="primary" className="w-full">
            Create account
          </Button>
        </Link>
      </div>
    </section>
  );
}

function FinalCta() {
  return (
    <section className="border-t border-line bg-white py-20 text-center">
      <h2 className="font-display text-3xl font-bold tracking-tight">
        Your next plan is a form away.
      </h2>
      <Link to="/register" className="mt-6 inline-block">
        <Button variant="accent" className="px-6 py-3 text-base">
          Build my plan
        </Button>
      </Link>
    </section>
  );
}

function Footer() {
  return (
    <footer className="border-t border-line px-6 py-10 text-sm text-muted">
      <div className="mx-auto flex max-w-6xl items-center justify-between">
        <span>CoachIQ</span>
        <span>© {new Date().getFullYear()}</span>
      </div>
    </footer>
  );
}
