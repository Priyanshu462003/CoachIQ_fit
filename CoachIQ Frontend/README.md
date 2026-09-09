# CoachIQ Frontend

A React (Vite) frontend for the CoachIQ Spring Boot backend. Plain fetch calls,
React Context for auth, no state-management library — built to be easy to read
and easy to explain, not to show off tooling.

## Stack

- React 18 + React Router
- Tailwind CSS
- Vite

No Redux, no React Query, no UI kit. Every screen is a normal function
component with `useState`/`useEffect`.

## Run it

```bash
npm install
cp .env.example .env      # point VITE_API_BASE_URL at your backend
npm run dev
```

The backend must be running (default `http://localhost:8080`) with Keycloak,
MySQL and MongoDB up, per the backend's own README.

## How it's organized

```
src/
├── lib/api.js            # every backend call lives here, one function per endpoint
├── context/AuthContext.jsx  # login/register/logout + holds the JWT
├── components/
│   ├── ProtectedRoute.jsx   # redirects to /login if not authenticated
│   ├── AppShell.jsx         # sidebar layout for logged-in pages
│   └── Ui.jsx                # small shared pieces: buttons, fields, cards
└── pages/                 # one file per screen, routed in App.jsx
```

### Auth flow

1. `Login`/`Register` call `AuthContext.login()` / `.register()`.
2. The backend's `/api/users/login` proxies a Keycloak password-grant login
   and returns a JWT `access_token`.
3. The frontend decodes the token's `sub` claim client-side (no library, it's
   just base64) to get the user id, and stores `{ accessToken, refreshToken,
   userId }` in `localStorage`.
4. Every subsequent API call sends `Authorization: Bearer <token>`.
5. If any call gets a 401, `api.js` calls a handler registered by
   `AuthContext` that clears the session — the next protected route redirect
   sends the person back to `/login`.

There's no refresh-token rotation implemented — when the access token
expires, the person is logged out and has to log back in. Wiring up silent
refresh using the stored `refreshToken` is the natural next step once you're
past the prototype stage.

### A known gap this frontend works around

`POST /api/workout-plans` and `POST /api/diets` only return a confirmation
string, not the new plan's id. After generating a plan, the frontend refetches
the list and opens the most recently created one. If you add an id to those
create responses on the backend, simplify `WorkoutPlanNew.jsx` /
`DietPlanNew.jsx` to navigate straight there instead.

### Dashboard

There's no aggregate `/dashboard` endpoint on the backend yet, so
`Dashboard.jsx` fires off several requests in parallel with
`Promise.allSettled` and combines them client-side. Fine at this scale; move
it server-side if the number of calls becomes a real cost.

## Production build

```bash
npm run build
```

Outputs static files to `dist/` — deploy that directory to any static host
(Vercel, Netlify, S3 + CloudFront, nginx, etc.). Set `VITE_API_BASE_URL` to
your deployed backend's URL at build time, since Vite inlines env vars into
the build.

## Not implemented yet (see backend README for context)

- Password reset / email verification
- Editing or deleting logged entries, plans
- Pagination on history lists (fine for a demo account, will get slow with
  months of real data)
