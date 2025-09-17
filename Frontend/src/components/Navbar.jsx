import { useState } from "react";
import { Link, useNavigate, useLocation } from "react-router-dom";
import authService from "../services/authService";

const Navbar = () => {
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const navigate = useNavigate();
  const location = useLocation();
  const user = authService.getCurrentUser();

  const handleLogout = () => {
    authService.logout();
    navigate("/login");
  };

  const isActive = (path) => {
    return location.pathname === path;
  };

  if (!user) {
    return null; // Don't show navbar on login/register pages
  }

  return (
    <nav className="bg-white shadow-lg">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between h-16">
          <div className="flex items-center">
            <Link to="/home" className="flex-shrink-0 flex items-center">
              <span className="text-2xl font-bold text-indigo-600">
                EduMate
              </span>
            </Link>
          </div>

          {/* Desktop Navigation */}
          <div className="hidden md:flex items-center space-x-8">
            <Link
              to="/home"
              className={`px-3 py-2 rounded-md text-sm font-medium ${
                isActive("/home")
                  ? "bg-indigo-100 text-indigo-700"
                  : "text-gray-700 hover:text-indigo-600"
              }`}
            >
              Home
            </Link>
            <Link
              to="/projects"
              className={`px-3 py-2 rounded-md text-sm font-medium ${
                isActive("/projects")
                  ? "bg-indigo-100 text-indigo-700"
                  : "text-gray-700 hover:text-indigo-600"
              }`}
            >
              Projects
            </Link>
            <Link
              to="/notes"
              className={`px-3 py-2 rounded-md text-sm font-medium ${
                isActive("/notes")
                  ? "bg-indigo-100 text-indigo-700"
                  : "text-gray-700 hover:text-indigo-600"
              }`}
            >
              Notes
            </Link>
            <Link
              to="/summaries"
              className={`px-3 py-2 rounded-md text-sm font-medium ${
                isActive("/summaries")
                  ? "bg-indigo-100 text-indigo-700"
                  : "text-gray-700 hover:text-indigo-600"
              }`}
            >
              Summaries
            </Link>
            <Link
              to="/quizzes"
              className={`px-3 py-2 rounded-md text-sm font-medium ${
                isActive("/quizzes")
                  ? "bg-indigo-100 text-indigo-700"
                  : "text-gray-700 hover:text-indigo-600"
              }`}
            >
              Quizzes
            </Link>
            <Link
              to="/flashcards"
              className={`px-3 py-2 rounded-md text-sm font-medium ${
                isActive("/flashcards")
                  ? "bg-indigo-100 text-indigo-700"
                  : "text-gray-700 hover:text-indigo-600"
              }`}
            >
              Flashcards
            </Link>
            <Link
              to="/reports"
              className={`px-3 py-2 rounded-md text-sm font-medium ${
                isActive("/reports")
                  ? "bg-indigo-100 text-indigo-700"
                  : "text-gray-700 hover:text-indigo-600"
              }`}
            >
              Reports
            </Link>
            <Link
              to="/reminders"
              className={`px-3 py-2 rounded-md text-sm font-medium ${
                isActive("/reminders")
                  ? "bg-indigo-100 text-indigo-700"
                  : "text-gray-700 hover:text-indigo-600"
              }`}
            >
              Reminders
            </Link>
          </div>

          {/* User Menu */}
          <div className="hidden md:flex items-center space-x-4">
            <div className="flex items-center space-x-2">
              <span className="text-sm text-gray-700">
                Welcome, {user.firstName}!
              </span>
            </div>
            <Link
              to="/profile"
              className={`px-3 py-2 rounded-md text-sm font-medium ${
                isActive("/profile")
                  ? "bg-indigo-100 text-indigo-700"
                  : "text-gray-700 hover:text-indigo-600"
              }`}
            >
              Profile
            </Link>
            <button
              onClick={handleLogout}
              className="px-3 py-2 rounded-md text-sm font-medium text-red-600 hover:text-red-700"
            >
              Logout
            </button>
          </div>

          {/* Mobile menu button */}
          <div className="md:hidden flex items-center">
            <button
              onClick={() => setIsMenuOpen(!isMenuOpen)}
              className="inline-flex items-center justify-center p-2 rounded-md text-gray-700 hover:text-indigo-600 hover:bg-gray-100 focus:outline-none focus:ring-2 focus:ring-inset focus:ring-indigo-500"
            >
              <span className="sr-only">Open main menu</span>
              {!isMenuOpen ? (
                <svg
                  className="block h-6 w-6"
                  xmlns="http://www.w3.org/2000/svg"
                  fill="none"
                  viewBox="0 0 24 24"
                  stroke="currentColor"
                >
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    strokeWidth={2}
                    d="M4 6h16M4 12h16M4 18h16"
                  />
                </svg>
              ) : (
                <svg
                  className="block h-6 w-6"
                  xmlns="http://www.w3.org/2000/svg"
                  fill="none"
                  viewBox="0 0 24 24"
                  stroke="currentColor"
                >
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    strokeWidth={2}
                    d="M6 18L18 6M6 6l12 12"
                  />
                </svg>
              )}
            </button>
          </div>
        </div>
      </div>

      {/* Mobile menu */}
      {isMenuOpen && (
        <div className="md:hidden">
          <div className="px-2 pt-2 pb-3 space-y-1 sm:px-3 bg-gray-50">
            <Link
              to="/home"
              className={`block px-3 py-2 rounded-md text-base font-medium ${
                isActive("/home")
                  ? "bg-indigo-100 text-indigo-700"
                  : "text-gray-700 hover:text-indigo-600"
              }`}
              onClick={() => setIsMenuOpen(false)}
            >
              Home
            </Link>
            <Link
              to="/projects"
              className={`block px-3 py-2 rounded-md text-base font-medium ${
                isActive("/projects")
                  ? "bg-indigo-100 text-indigo-700"
                  : "text-gray-700 hover:text-indigo-600"
              }`}
              onClick={() => setIsMenuOpen(false)}
            >
              Projects
            </Link>
            <Link
              to="/notes"
              className={`block px-3 py-2 rounded-md text-base font-medium ${
                isActive("/notes")
                  ? "bg-indigo-100 text-indigo-700"
                  : "text-gray-700 hover:text-indigo-600"
              }`}
              onClick={() => setIsMenuOpen(false)}
            >
              Notes
            </Link>
            <Link
              to="/summaries"
              className={`block px-3 py-2 rounded-md text-base font-medium ${
                isActive("/summaries")
                  ? "bg-indigo-100 text-indigo-700"
                  : "text-gray-700 hover:text-indigo-600"
              }`}
              onClick={() => setIsMenuOpen(false)}
            >
              Summaries
            </Link>
            <Link
              to="/quizzes"
              className={`block px-3 py-2 rounded-md text-base font-medium ${
                isActive("/quizzes")
                  ? "bg-indigo-100 text-indigo-700"
                  : "text-gray-700 hover:text-indigo-600"
              }`}
              onClick={() => setIsMenuOpen(false)}
            >
              Quizzes
            </Link>
            <Link
              to="/flashcards"
              className={`block px-3 py-2 rounded-md text-base font-medium ${
                isActive("/flashcards")
                  ? "bg-indigo-100 text-indigo-700"
                  : "text-gray-700 hover:text-indigo-600"
              }`}
              onClick={() => setIsMenuOpen(false)}
            >
              Flashcards
            </Link>
            <Link
              to="/reports"
              className={`block px-3 py-2 rounded-md text-base font-medium ${
                isActive("/reports")
                  ? "bg-indigo-100 text-indigo-700"
                  : "text-gray-700 hover:text-indigo-600"
              }`}
              onClick={() => setIsMenuOpen(false)}
            >
              Reports
            </Link>
            <Link
              to="/reminders"
              className={`block px-3 py-2 rounded-md text-base font-medium ${
                isActive("/reminders")
                  ? "bg-indigo-100 text-indigo-700"
                  : "text-gray-700 hover:text-indigo-600"
              }`}
              onClick={() => setIsMenuOpen(false)}
            >
              Reminders
            </Link>
            <Link
              to="/profile"
              className={`block px-3 py-2 rounded-md text-base font-medium ${
                isActive("/profile")
                  ? "bg-indigo-100 text-indigo-700"
                  : "text-gray-700 hover:text-indigo-600"
              }`}
              onClick={() => setIsMenuOpen(false)}
            >
              Profile
            </Link>
            <button
              onClick={() => {
                handleLogout();
                setIsMenuOpen(false);
              }}
              className="block w-full text-left px-3 py-2 rounded-md text-base font-medium text-red-600 hover:text-red-700"
            >
              Logout
            </button>
          </div>
        </div>
      )}
    </nav>
  );
};

export default Navbar;
