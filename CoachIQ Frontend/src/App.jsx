import { Routes, Route } from "react-router-dom";
import ProtectedRoute from "./components/ProtectedRoute.jsx";
import AppShell from "./components/AppShell.jsx";

import Landing from "./pages/Landing.jsx";
import Login from "./pages/Login.jsx";
import Register from "./pages/Register.jsx";
import Dashboard from "./pages/Dashboard.jsx";
import WorkoutPlans from "./pages/WorkoutPlans.jsx";
import WorkoutPlanNew from "./pages/WorkoutPlanNew.jsx";
import WorkoutPlanDetail from "./pages/WorkoutPlanDetail.jsx";
import DietPlans from "./pages/DietPlans.jsx";
import DietPlanNew from "./pages/DietPlanNew.jsx";
import DietPlanDetail from "./pages/DietPlanDetail.jsx";
import Progress from "./pages/Progress.jsx";
import NotFound from "./pages/NotFound.jsx";

function Protected({ children }) {
  return (
    <ProtectedRoute>
      <AppShell>{children}</AppShell>
    </ProtectedRoute>
  );
}

export default function App() {
  return (
    <Routes>
      <Route path="/" element={<Landing />} />
      <Route path="/login" element={<Login />} />
      <Route path="/register" element={<Register />} />

      <Route path="/app" element={<Protected><Dashboard /></Protected>} />
      <Route path="/app/workouts" element={<Protected><WorkoutPlans /></Protected>} />
      <Route path="/app/workouts/new" element={<Protected><WorkoutPlanNew /></Protected>} />
      <Route path="/app/workouts/:id" element={<Protected><WorkoutPlanDetail /></Protected>} />
      <Route path="/app/diets" element={<Protected><DietPlans /></Protected>} />
      <Route path="/app/diets/new" element={<Protected><DietPlanNew /></Protected>} />
      <Route path="/app/diets/:id" element={<Protected><DietPlanDetail /></Protected>} />
      <Route path="/app/progress" element={<Protected><Progress /></Protected>} />

      <Route path="*" element={<NotFound />} />
    </Routes>
  );
}
