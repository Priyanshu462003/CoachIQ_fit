import { createContext, useContext, useEffect, useState, useCallback } from "react";
import { api, setUnauthorizedHandler } from "../lib/api";

const AuthContext = createContext(null);
const STORAGE_KEY = "coachiq_session";

// Decode the JWT payload just enough to read "sub" (the Keycloak user id
// the whole backend uses as the userId). We don't need a library for this
// — a JWT payload is just base64url-encoded JSON.
function decodeSub(token) {
  try {
    const payload = token.split(".")[1];
    const json = atob(payload.replace(/-/g, "+").replace(/_/g, "/"));
    return JSON.parse(json).sub;
  } catch {
    return null;
  }
}

export function AuthProvider({ children }) {
  const [session, setSession] = useState(() => {
    const raw = localStorage.getItem(STORAGE_KEY);
    return raw ? JSON.parse(raw) : null;
  });
  const [profile, setProfile] = useState(null);
  const [loading, setLoading] = useState(true);

  const logout = useCallback(() => {
    localStorage.removeItem(STORAGE_KEY);
    setSession(null);
    setProfile(null);
  }, []);

  // If any API call gets a 401, the session is dead — clear it everywhere.
  useEffect(() => {
    setUnauthorizedHandler(logout);
  }, [logout]);

  useEffect(() => {
    if (!session) {
      setLoading(false);
      return;
    }
    api
      .getProfile(session.userId, session.accessToken)
      .then(setProfile)
      .catch(() => logout())
      .finally(() => setLoading(false));
  }, [session, logout]);

  async function login(email, password) {
    const tokens = await api.login({ email, password });
    const userId = decodeSub(tokens.access_token);
    const next = { accessToken: tokens.access_token, refreshToken: tokens.refresh_token, userId };
    localStorage.setItem(STORAGE_KEY, JSON.stringify(next));
    setSession(next);
  }

  async function register(payload) {
    await api.register(payload);
    // Registration doesn't log the user in on its own, so chain a login
    // for a smooth "one form, then you're in" signup flow.
    await login(payload.email, payload.password);
  }

  const value = {
    session,
    profile,
    loading,
    isAuthenticated: !!session,
    token: session?.accessToken,
    userId: session?.userId,
    login,
    register,
    logout,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error("useAuth must be used inside <AuthProvider>");
  return ctx;
}
