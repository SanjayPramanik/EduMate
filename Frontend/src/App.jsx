import {
  BrowserRouter as Router,
  Routes,
  Route,
  Navigate,
} from "react-router-dom";
import Navbar from "./components/Navbar";
import Footer from "./components/Footer";
import Login from "./pages/Login";
import Register from "./pages/Register";
import Home from "./pages/Home";
import Profile from "./pages/Profile";
import Notes from "./pages/Notes";
import Summaries from "./pages/Summaries";
import Quizzes from "./pages/Quizzes";
import Flashcards from "./pages/Flashcards";
import Reports from "./pages/Reports";
import Reminders from "./pages/Reminders";
import Projects from "./pages/Projects";
import authService from "./services/authService";

function App() {
  return (
    <Router>
      <div className="min-h-screen flex flex-col">
        <Navbar />
        <main className="flex-grow">
          <Routes>
            {/* Public routes */}
            <Route path="/login" element={<Login />} />
            <Route path="/register" element={<Register />} />

            {/* Protected routes */}
            <Route
              path="/home"
              element={
                authService.isAuthenticated() ? (
                  <Home />
                ) : (
                  <Navigate to="/login" />
                )
              }
            />
            <Route
              path="/profile"
              element={
                authService.isAuthenticated() ? (
                  <Profile />
                ) : (
                  <Navigate to="/login" />
                )
              }
            />
            <Route
              path="/notes"
              element={
                authService.isAuthenticated() ? (
                  <Notes />
                ) : (
                  <Navigate to="/login" />
                )
              }
            />
            <Route
              path="/summaries"
              element={
                authService.isAuthenticated() ? (
                  <Summaries />
                ) : (
                  <Navigate to="/login" />
                )
              }
            />
            <Route
              path="/quizzes"
              element={
                authService.isAuthenticated() ? (
                  <Quizzes />
                ) : (
                  <Navigate to="/login" />
                )
              }
            />
            <Route
              path="/flashcards"
              element={
                authService.isAuthenticated() ? (
                  <Flashcards />
                ) : (
                  <Navigate to="/login" />
                )
              }
            />
            <Route
              path="/reports"
              element={
                authService.isAuthenticated() ? (
                  <Reports />
                ) : (
                  <Navigate to="/login" />
                )
              }
            />
            <Route
              path="/reminders"
              element={
                authService.isAuthenticated() ? (
                  <Reminders />
                ) : (
                  <Navigate to="/login" />
                )
              }
            />
            <Route
              path="/projects"
              element={
                authService.isAuthenticated() ? (
                  <Projects />
                ) : (
                  <Navigate to="/login" />
                )
              }
            />

            {/* Default route */}
            <Route
              path="/"
              element={
                authService.isAuthenticated() ? (
                  <Navigate to="/home" />
                ) : (
                  <Navigate to="/login" />
                )
              }
            />
          </Routes>
        </main>
        <Footer />
      </div>
    </Router>
  );
}

export default App;
