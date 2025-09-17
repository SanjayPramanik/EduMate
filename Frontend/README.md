## EduMate Frontend

### Prerequisites

- Node.js 18+
- npm 9+

### Install

```bash
npm install
```

### Configure API URL (optional)

Create `.env` and set:

```
VITE_API_URL=http://localhost:3001/api
```

Defaults to `http://localhost:3001/api` if not set.

### Run Dev Server

```bash
npm run dev
```

App runs at `http://localhost:5173`.

### Auth Flow

- Register/Login forms call `/api/auth/register` and `/api/auth/login`
- On success, token is saved in `localStorage` and routes are protected

### Scripts

- `npm run dev` – start dev server
- `npm run build` – production build
- `npm run preview` – preview build

# EduMate Frontend

A modern React application built with Vite, React, and TailwindCSS for the EduMate learning platform.

## Features

- **Authentication**: Login and Register pages with form validation
- **Responsive Design**: Mobile-first approach with TailwindCSS
- **Modular Structure**: Clean separation of components, pages, services, and context
- **Protected Routes**: Authentication-based routing
- **Placeholder Pages**: Ready-to-implement pages for all EduMate features

## Tech Stack

- **React 18** - UI library
- **Vite** - Build tool and dev server
- **TailwindCSS** - Utility-first CSS framework
- **React Router** - Client-side routing
- **JavaScript** - Programming language

## Project Structure

```
src/
├── components/          # Reusable UI components
│   ├── Navbar.jsx      # Navigation component
│   └── Footer.jsx      # Footer component
├── pages/              # Main application pages
│   ├── Login.jsx       # User login page
│   ├── Register.jsx    # User registration page
│   ├── Home.jsx        # Dashboard/home page
│   ├── Profile.jsx     # User profile page
│   ├── Notes.jsx       # Notes management (placeholder)
│   ├── Summaries.jsx   # AI summaries (placeholder)
│   ├── Quizzes.jsx     # Quiz system (placeholder)
│   ├── Flashcards.jsx  # Flashcard system (placeholder)
│   ├── Reports.jsx     # Analytics/reports (placeholder)
│   └── Reminders.jsx   # Study reminders (placeholder)
├── services/           # API and external service integrations
│   └── authService.js  # Authentication service
├── context/            # React Context for state management
│   └── AuthContext.jsx # Authentication context (placeholder)
└── App.jsx             # Main application component
```

## Getting Started

### Prerequisites

- Node.js (v16 or higher)
- npm or yarn

### Installation

1. Navigate to the Frontend directory:

   ```bash
   cd Frontend
   ```

2. Install dependencies:

   ```bash
   npm install
   ```

3. Start the development server:

   ```bash
   npm run dev
   ```

4. Open your browser and visit `http://localhost:5173`

### Available Scripts

- `npm run dev` - Start development server
- `npm run build` - Build for production
- `npm run preview` - Preview production build
- `npm run lint` - Run ESLint

## Authentication

The application includes a complete authentication system with:

- **Login Page**: Email and password validation
- **Register Page**: Full registration form with password confirmation
- **Protected Routes**: Automatic redirection for unauthenticated users
- **Auth Service**: Centralized authentication logic
- **Token Management**: Local storage for user sessions

## Styling

The application uses TailwindCSS for styling with:

- **Responsive Design**: Mobile-first approach
- **Component Styling**: Utility classes for consistent design
- **Form Validation**: Visual feedback for form errors
- **Modern UI**: Clean, professional interface

## API Integration

The `authService.js` is configured to work with a backend API:

- **Base URL**: Configurable via environment variables
- **Error Handling**: Comprehensive error management
- **Token Storage**: Secure token management
- **User State**: Persistent user session

## Future Development

The project is structured to easily accommodate:

- **React Context**: State management for complex applications
- **Additional Services**: API services for different features
- **Component Library**: Reusable UI components
- **Testing**: Unit and integration tests
- **TypeScript**: Type safety (if needed)

## Team Collaboration

This structure is designed for team development:

- **Modular Components**: Easy to work on independently
- **Clear Separation**: Services, components, and pages are well-organized
- **Placeholder Pages**: Ready for feature implementation
- **Consistent Patterns**: Following React best practices

## Environment Variables

Create a `.env.local` file in the Frontend directory:

```env
VITE_API_URL=http://localhost:3001/api
```

**Note:** In Vite, environment variables must be prefixed with `VITE_` to be accessible in the browser.

## Contributing

1. Follow the existing code structure
2. Use TailwindCSS for styling
3. Implement proper form validation
4. Add error handling for API calls
5. Keep components modular and reusable
