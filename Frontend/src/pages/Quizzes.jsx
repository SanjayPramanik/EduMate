import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";

// MCQ Service for API calls
const API_BASE_URL = import.meta.env.VITE_API_URL || "http://localhost:3001/api";

const mcqService = {
  async getMCQs() {
    const token = localStorage.getItem("token");
    const response = await fetch(`${API_BASE_URL}/mcqs`, {
      headers: { Authorization: `Bearer ${token}` },
    });
    if (!response.ok) throw new Error("Failed to fetch MCQs");
    return await response.json();
  },

  async deleteMCQ(id) {
    const token = localStorage.getItem("token");
    const response = await fetch(`${API_BASE_URL}/mcqs/${id}`, {
      method: "DELETE",
      headers: { Authorization: `Bearer ${token}` },
    });
    if (!response.ok) throw new Error("Failed to delete MCQ");
    return true;
  },
};

const Quizzes = () => {
  const navigate = useNavigate();
  const [mcqs, setMcqs] = useState([]);
  const [loading, setLoading] = useState(true);
  const [selectedMCQ, setSelectedMCQ] = useState(null);
  const [showQuiz, setShowQuiz] = useState(false);

  useEffect(() => {
    loadMCQs();
  }, []);

  const loadMCQs = async () => {
    try {
      const data = await mcqService.getMCQs();
      setMcqs(data);
    } catch (error) {
      console.error("Failed to load MCQs:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleDeleteMCQ = async (id) => {
    if (confirm("Are you sure you want to delete this MCQ set?")) {
      try {
        await mcqService.deleteMCQ(id);
        loadMCQs();
      } catch (error) {
        alert("Failed to delete MCQ");
      }
    }
  };

  const handleStartQuiz = (mcq) => {
    setSelectedMCQ(mcq);
    setShowQuiz(true);
  };

  const handleBackToList = () => {
    setSelectedMCQ(null);
    setShowQuiz(false);
  };

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="text-center">
          <div className="animate-spin rounded-full h-32 w-32 border-b-2 border-indigo-600 mx-auto"></div>
          <p className="mt-4 text-gray-600">Loading quizzes...</p>
        </div>
      </div>
    );
  }

  if (showQuiz && selectedMCQ) {
    return (
      <QuizComponent mcq={selectedMCQ} onBack={handleBackToList} />
    );
  }

  return (
    <div className="min-h-screen bg-gray-50">
      <div className="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
        <div className="px-4 py-6 sm:px-0">
          <div className="flex justify-between items-center mb-6">
            <h1 className="text-3xl font-bold text-gray-900">Quizzes</h1>
            <button
              onClick={() => navigate("/notes")}
              className="bg-indigo-600 text-white px-4 py-2 rounded-md hover:bg-indigo-700"
            >
              Generate More MCQs
            </button>
          </div>

          {mcqs.length === 0 ? (
            <div className="text-center py-12">
              <div className="bg-white p-8 rounded-lg shadow-md max-w-md mx-auto">
                <h3 className="text-lg font-semibold text-gray-900 mb-4">
                  No Quizzes Available
                </h3>
                <p className="text-gray-600 mb-4">
                  Upload some notes and generate MCQs to start taking quizzes!
                </p>
                <button
                  onClick={() => navigate("/notes")}
                  className="bg-indigo-600 text-white px-4 py-2 rounded-md hover:bg-indigo-700"
                >
                  Go to Notes
                </button>
              </div>
            </div>
          ) : (
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
              {mcqs.map((mcq) => (
                <div key={mcq.mcqId} className="bg-white rounded-lg shadow-md p-6">
                  <h3 className="text-lg font-semibold text-gray-900 mb-2">
                    Quiz from Note: {mcq.noteId}
                  </h3>
                  <p className="text-sm text-gray-600 mb-4">
                    Questions: {mcq.questions?.length || 0}
                  </p>
                  <p className="text-sm text-gray-500 mb-4">
                    Created: {new Date(mcq.createdAt).toLocaleDateString()}
                  </p>
                  <div className="flex space-x-2">
                    <button
                      onClick={() => handleStartQuiz(mcq)}
                      className="bg-green-600 text-white px-3 py-1 rounded text-sm hover:bg-green-700"
                    >
                      Start Quiz
                    </button>
                    <button
                      onClick={() => handleDeleteMCQ(mcq.mcqId)}
                      className="bg-red-600 text-white px-3 py-1 rounded text-sm hover:bg-red-700"
                    >
                      Delete
                    </button>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

// Quiz Component for taking the quiz
const QuizComponent = ({ mcq, onBack }) => {
  const [currentQuestion, setCurrentQuestion] = useState(0);
  const [score, setScore] = useState(0);
  const [showResults, setShowResults] = useState(false);
  const [answers, setAnswers] = useState({});

  const questions = mcq.questions || [];

  const handleAnswer = (questionIndex, answer) => {
    setAnswers(prev => ({ ...prev, [questionIndex]: answer }));
  };

  const handleNext = () => {
    if (currentQuestion < questions.length - 1) {
      setCurrentQuestion(currentQuestion + 1);
    } else {
      calculateScore();
      setShowResults(true);
    }
  };

  const handlePrevious = () => {
    if (currentQuestion > 0) {
      setCurrentQuestion(currentQuestion - 1);
    }
  };

  const calculateScore = () => {
    // Since questions are in text format, we can't calculate exact score
    // This is a placeholder for demonstration
    const answeredQuestions = Object.keys(answers).length;
    setScore(Math.round((answeredQuestions / questions.length) * 100));
  };

  if (showResults) {
    return (
      <div className="min-h-screen bg-gray-50 flex items-center justify-center">
        <div className="bg-white p-8 rounded-lg shadow-md max-w-md w-full">
          <h2 className="text-2xl font-bold text-center mb-6">Quiz Results</h2>
          <div className="text-center">
            <div className="text-4xl font-bold text-green-600 mb-4">
              {score}%
            </div>
            <p className="text-gray-600 mb-6">
              You answered {Object.keys(answers).length} out of {questions.length} questions
            </p>
            <div className="space-y-3">
              <button
                onClick={onBack}
                className="w-full bg-indigo-600 text-white py-2 px-4 rounded-md hover:bg-indigo-700"
              >
                Back to Quizzes
              </button>
            </div>
          </div>
        </div>
      </div>
    );
  }

  if (questions.length === 0) {
    return (
      <div className="min-h-screen bg-gray-50 flex items-center justify-center">
        <div className="bg-white p-8 rounded-lg shadow-md max-w-md w-full text-center">
          <h2 className="text-xl font-bold mb-4">No Questions Available</h2>
          <button
            onClick={onBack}
            className="bg-indigo-600 text-white py-2 px-4 rounded-md hover:bg-indigo-700"
          >
            Back to Quizzes
          </button>
        </div>
      </div>
    );
  }

  const question = questions[currentQuestion];

  return (
    <div className="min-h-screen bg-gray-50">
      <div className="max-w-4xl mx-auto py-6 sm:px-6 lg:px-8">
        <div className="bg-white rounded-lg shadow-md p-8">
          <div className="mb-6">
            <div className="flex justify-between items-center mb-4">
              <h1 className="text-2xl font-bold text-gray-900">Quiz</h1>
              <span className="text-sm text-gray-600">
                Question {currentQuestion + 1} of {questions.length}
              </span>
            </div>
            <div className="w-full bg-gray-200 rounded-full h-2">
              <div 
                className="bg-indigo-600 h-2 rounded-full transition-all duration-300"
                style={{ width: `${((currentQuestion + 1) / questions.length) * 100}%` }}
              ></div>
            </div>
          </div>

          <div className="mb-8">
            <div className="bg-gray-50 p-6 rounded-lg">
              <pre className="whitespace-pre-wrap text-gray-800 text-sm leading-relaxed">
                {question}
              </pre>
            </div>
          </div>

          <div className="mb-6">
            <h3 className="text-lg font-medium mb-3">Your Answer:</h3>
            <textarea
              value={answers[currentQuestion] || ""}
              onChange={(e) => handleAnswer(currentQuestion, e.target.value)}
              className="w-full p-3 border border-gray-300 rounded-md focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500"
              rows="4"
              placeholder="Type your answer here..."
            />
          </div>

          <div className="flex justify-between">
            <button
              onClick={handlePrevious}
              disabled={currentQuestion === 0}
              className="bg-gray-600 text-white py-2 px-4 rounded-md hover:bg-gray-700 disabled:opacity-50 disabled:cursor-not-allowed"
            >
              Previous
            </button>
            <div className="space-x-3">
              <button
                onClick={onBack}
                className="bg-red-600 text-white py-2 px-4 rounded-md hover:bg-red-700"
              >
                Exit Quiz
              </button>
              <button
                onClick={handleNext}
                className="bg-indigo-600 text-white py-2 px-4 rounded-md hover:bg-indigo-700"
              >
                {currentQuestion === questions.length - 1 ? "Finish Quiz" : "Next"}
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Quizzes;
