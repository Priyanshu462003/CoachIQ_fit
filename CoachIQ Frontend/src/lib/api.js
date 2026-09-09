// Thin wrapper around fetch(). Every function here maps 1:1 to a real
// endpoint on the CoachIQ backend (see the README for the full list).
//
// Nothing clever happens here on purpose: we build a URL, attach the
// bearer token if we have one, send JSON, and throw on non-2xx so callers
// can catch a single error type.

const BASE_URL = import.meta.env.VITE_API_BASE_URL || "http://localhost:8080";

let onUnauthorized = () => {};
// Lets AuthContext register a "log the user out" callback without api.js
// needing to import React or the context itself.
export function setUnauthorizedHandler(fn) {
  onUnauthorized = fn;
}

async function request(path, { method = "GET", body, token, isForm = false } = {}) {
  const headers = {};
  if (!isForm) headers["Content-Type"] = "application/json";
  if (token) headers["Authorization"] = `Bearer ${token}`;

  const res = await fetch(`${BASE_URL}${path}`, {
    method,
    headers,
    body: body ? (isForm ? body : JSON.stringify(body)) : undefined,
  });

  if (res.status === 401) {
    onUnauthorized();
    throw new ApiError("Your session expired. Please log in again.", 401);
  }

  const text = await res.text();
  const data = text ? safeJson(text) : null;

  if (!res.ok) {
    const message = (data && (data.error || data.message)) || `Request failed (${res.status})`;
    throw new ApiError(message, res.status);
  }

  return data;
}

function safeJson(text) {
  try {
    return JSON.parse(text);
  } catch {
    return text; // some endpoints (e.g. create workout plan) return a plain string
  }
}

export class ApiError extends Error {
  constructor(message, status) {
    super(message);
    this.status = status;
  }
}

// Local-date "YYYY-MM-DD", used to default form dates and to check
// "has today been logged yet". Deliberately NOT using
// `new Date().toISOString()` — that converts to UTC, which returns the
// wrong calendar date for part of the day in any timezone ahead of UTC
// (e.g. IST, roughly midnight-5:30am local time).
export function todayIso() {
  const d = new Date();
  const pad = (n) => String(n).padStart(2, "0");
  return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())}`;
}

export const api = {
  // ---- Auth ----
  register: (payload) => request("/api/users/register", { method: "POST", body: payload }),
  login: (payload) => request("/api/users/login", { method: "POST", body: payload }),
  getProfile: (userId, token) => request(`/api/users/${userId}`, { token }),

  // ---- Workout plans ----
  createWorkoutPlan: (payload, token) =>
    request("/api/workout-plans", { method: "POST", body: payload, token }),
  getWorkoutPlans: (token) => request("/api/workout-plans", { token }),
  getWorkoutPlan: (id, token) => request(`/api/workout-plans/${id}`, { token }),

  // ---- Diet plans ----
  createDiet: (payload, token) => request("/api/diets", { method: "POST", body: payload, token }),
  getDiets: (token) => request("/api/diets", { token }),
  getDiet: (id, token) => request(`/api/diets/${id}`, { token }),

  // ---- Progress ----
  saveDailyProgress: (payload, token) =>
    request("/api/progress/daily", { method: "POST", body: payload, token }),
  getDailyProgress: (token) => request("/api/progress/daily", { token }),

  saveWeeklyProgress: (payload, token) =>
    request("/api/progress/weekly", { method: "POST", body: payload, token }),
  getWeeklyProgress: (token) => request("/api/progress/weekly", { token }),

  saveAssessment: (payload, token) =>
    request("/api/progress/assessment", { method: "POST", body: payload, token }),
  getAssessments: (token) => request("/api/progress/assessment", { token }),

  getLatestAnalysis: (token) => request("/api/progress/analysis/latest", { token }),
};
