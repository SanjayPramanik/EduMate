import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import authService from "../services/authService";

const Home = () => {
  const [user, setUser] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    const currentUser = authService.getCurrentUser();
    if (!currentUser) {
      navigate("/login");
    } else {
      setUser(currentUser);
    }
  }, [navigate]);

  const handleLogout = () => {
    authService.logout();
    navigate("/login");
  };

  if (!user) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="text-center">
          <div className="animate-spin rounded-full h-32 w-32 border-b-2 border-indigo-600 mx-auto"></div>
          <p className="mt-4 text-gray-600">Loading...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50">
      <div className="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
        <div className="px-4 py-6 sm:px-0">
          <div className="border-4 border-dashed border-gray-200 rounded-lg p-8">
            <div className="text-center">
              <h1 className="text-3xl font-bold text-gray-900 mb-4">
                Welcome to EduMate, {user.firstName}!
              </h1>
              <p className="text-lg text-gray-600 mb-8">
                Your personalized learning companion is ready to help you
                succeed.
              </p>

              <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 mb-8">
                <div className="bg-white p-6 rounded-lg shadow-md">
                  <h3 className="text-lg font-semibold text-gray-900 mb-2">
                    Projects
                  </h3>
                  <p className="text-gray-600">
                    Organize your learning materials by project
                  </p>
                  <button 
                    onClick={() => navigate("/projects")}
                    className="mt-4 w-full bg-indigo-600 text-white py-2 px-4 rounded-md hover:bg-indigo-700"
                  >
                    Manage Projects
                  </button>
                </div>

                <div className="bg-white p-6 rounded-lg shadow-md">
                  <h3 className="text-lg font-semibold text-gray-900 mb-2">
                    Notes & AI Summaries
                  </h3>
                  <p className="text-gray-600">
                    Upload files and get AI-powered summaries
                  </p>
                  <button 
                    onClick={() => navigate("/notes")}
                    className="mt-4 w-full bg-indigo-600 text-white py-2 px-4 rounded-md hover:bg-indigo-700"
                  >
                    View Notes
                  </button>
                </div>

                <div className="bg-white p-6 rounded-lg shadow-md">
                  <h3 className="text-lg font-semibold text-gray-900 mb-2">
                    AI Quizzes
                  </h3>
                  <p className="text-gray-600">
                    Take AI-generated quizzes from your notes
                  </p>
                  <button 
                    onClick={() => navigate("/quizzes")}
                    className="mt-4 w-full bg-indigo-600 text-white py-2 px-4 rounded-md hover:bg-indigo-700"
                  >
                    Start Quiz
                  </button>
                </div>

                <div className="bg-white p-6 rounded-lg shadow-md">
                  <h3 className="text-lg font-semibold text-gray-900 mb-2">
                    AI Flashcards
                  </h3>
                  <p className="text-gray-600">
                    Study with AI-generated flashcards
                  </p>
                  <button 
                    onClick={() => navigate("/flashcards")}
                    className="mt-4 w-full bg-indigo-600 text-white py-2 px-4 rounded-md hover:bg-indigo-700"
                  >
                    Study Flashcards
                  </button>
                </div>

                <div className="bg-white p-6 rounded-lg shadow-md">
                  <h3 className="text-lg font-semibold text-gray-900 mb-2">
                    Reports
                  </h3>
                  <p className="text-gray-600">
                    Track your learning progress and analytics
                  </p>
                  <button 
                    onClick={() => navigate("/reports")}
                    className="mt-4 w-full bg-indigo-600 text-white py-2 px-4 rounded-md hover:bg-indigo-700"
                  >
                    View Reports
                  </button>
                </div>

                <div className="bg-white p-6 rounded-lg shadow-md">
                  <h3 className="text-lg font-semibold text-gray-900 mb-2">
                    Reminders
                  </h3>
                  <p className="text-gray-600">
                    Set study reminders and deadlines
                  </p>
                  <button 
                    onClick={() => navigate("/reminders")}
                    className="mt-4 w-full bg-indigo-600 text-white py-2 px-4 rounded-md hover:bg-indigo-700"
                  >
                    Manage Reminders
                  </button>
                </div>
              </div>

              <div className="flex justify-center space-x-4">
                <button
                  onClick={() => navigate("/profile")}
                  className="bg-gray-600 text-white py-2 px-6 rounded-md hover:bg-gray-700"
                >
                  View Profile
                </button>
                <button
                  onClick={handleLogout}
                  className="bg-red-600 text-white py-2 px-6 rounded-md hover:bg-red-700"
                >
                  Logout
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Home;
