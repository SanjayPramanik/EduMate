# WARP.md

This file provides guidance to WARP (warp.dev) when working with code in this repository.

## Project Overview

EduMate is a full-stack learning platform with a React frontend and a planned backend. The project is currently in early development with the frontend implemented and backend planned but not yet developed.

## Development Commands

### Frontend (React + Vite)

Navigate to the Frontend directory before running these commands:

```bash
cd Frontend
```

**Development:**
- `npm run dev` - Start development server (runs on http://localhost:5173)
- `npm install` - Install dependencies
- `npm run lint` - Run ESLint for code quality checks
- `npm run build` - Build for production
- `npm run preview` - Preview production build locally

**Environment Setup:**
Create a `.env.local` file in the Frontend directory:
```env
VITE_API_URL=http://localhost:3001/api
```

### Backend

Currently, the backend directory only contains an empty README.md. Backend development needs to be implemented.

## Architecture Overview

### Frontend Architecture

**Tech Stack:**
- React 19 with React DOM
- Vite as build tool and dev server
- TailwindCSS for styling
- React Router for client-side routing
- ESLint for code quality

**Project Structure:**
```
Frontend/src/
├── components/          # Reusable UI components
│   ├── Navbar.jsx      # Main navigation with authentication state
│   └── Footer.jsx      # Site footer
├── pages/              # Route-based page components
│   ├── Login.jsx       # Authentication - login form
│   ├── Register.jsx    # Authentication - registration form  
│   ├── Home.jsx        # Dashboard with feature overview
│   ├── Profile.jsx     # User profile management
│   └── [feature].jsx  # Feature pages (Notes, Summaries, Quizzes, etc.)
├── services/           # API and external services
│   └── authService.js  # Authentication service with localStorage
├── context/            # React Context providers
│   └── AuthContext.jsx # Authentication context (ready but not used)
└── App.jsx            # Main app with routing and protected routes
```

**Authentication Flow:**
- Uses a service-based architecture with `authService.js`
- Token stored in localStorage with user data
- Protected routes implemented with conditional rendering
- Each protected route checks authentication via `authService.isAuthenticated()`
- Navbar conditionally renders based on authentication state

**Routing Strategy:**
- Public routes: `/login`, `/register`
- Protected routes: All feature routes require authentication
- Default route redirects to `/home` if authenticated, `/login` if not
- Manual route protection implemented in App.jsx rather than using a ProtectedRoute component

### Backend Architecture

The backend is planned but not yet implemented. Based on the frontend's API expectations:
- Expected to run on port 3001
- Should provide `/auth/login`, `/auth/register`, `/auth/verify` endpoints
- Token-based authentication expected

## Development Guidelines

### Authentication Implementation

The project has two authentication approaches prepared:
1. **Service-based** (currently used): Direct calls to `authService` in components
2. **Context-based** (ready but unused): `AuthContext.jsx` is implemented but not integrated

**To switch to Context-based authentication:**
- Wrap App in `AuthProvider` in `main.jsx`
- Replace `authService` calls with `useAuth()` hook in components
- Update protected routes to use context state

### Component Patterns

**Form Handling:**
- Forms use controlled components with validation
- Error states are handled with visual feedback
- Loading states implemented with disabled buttons and spinners
- Form validation occurs on submit and clears on input change

**Navigation:**
- Navbar shows/hides based on authentication state
- Active route highlighting implemented
- Mobile-responsive hamburger menu
- User welcome message displays user.firstName

### Styling Approach

- TailwindCSS with utility-first approach
- Responsive design with mobile-first methodology
- Gradient backgrounds and modern glass-morphism effects
- Consistent color scheme using indigo/purple gradients
- Error states use red color variants
- Loading states with spin animations

### Feature Pages Structure

All feature pages (Notes, Summaries, Quizzes, Flashcards, Reports, Reminders) are currently placeholder pages ready for implementation. They follow the same structure as the Home page.

## API Integration

The frontend expects a REST API with these characteristics:
- JSON request/response format
- Bearer token authentication
- Error responses with `message` field
- User object returned on successful auth with `firstName` field minimum

## Environment Configuration

**Vite Environment Variables:**
- Must be prefixed with `VITE_` to be accessible in browser
- Default API URL: `http://localhost:3001/api`
- Override with `VITE_API_URL` in `.env.local`

## Current Development Status

**Completed:**
- Complete frontend authentication system
- Responsive navigation and routing
- Form validation and error handling
- Protected route implementation
- Modern UI with TailwindCSS

**Ready for Implementation:**
- All feature page placeholders are ready
- AuthContext is implemented but not integrated
- Backend API structure is defined by frontend expectations
- Environment configuration is properly set up

**Next Development Steps:**
1. Implement backend API with authentication endpoints
2. Implement individual feature functionality (Notes, Quizzes, etc.)
3. Optionally switch to Context-based state management
4. Add proper error boundaries and loading states
