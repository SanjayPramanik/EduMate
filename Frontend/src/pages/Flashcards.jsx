import { useNavigate } from "react-router-dom";

const Flashcards = () => {
  const navigate = useNavigate();

  return (
    <div className="min-h-screen bg-gray-50">
      <div className="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
        <div className="px-4 py-6 sm:px-0">
          <div className="border-4 border-dashed border-gray-200 rounded-lg p-8">
            <div className="text-center">
              <h1 className="text-3xl font-bold text-gray-900 mb-4">
                Flashcards
              </h1>
              <p className="text-lg text-gray-600 mb-8">
                Create and study with digital flashcards. This feature is coming
                soon!
              </p>
              <div className="bg-white p-6 rounded-lg shadow-md max-w-md mx-auto">
                <h3 className="text-lg font-semibold text-gray-900 mb-2">
                  Planned Features:
                </h3>
                <ul className="text-left text-gray-600 space-y-2">
                  <li>• Create flashcard decks</li>
                  <li>• Spaced repetition algorithm</li>
                  <li>• Progress tracking</li>
                  <li>• Import/export decks</li>
                  <li>• Collaborative studying</li>
                </ul>
              </div>
              <button
                onClick={() => navigate("/home")}
                className="mt-6 bg-indigo-600 text-white py-2 px-6 rounded-md hover:bg-indigo-700"
              >
                Back to Home
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Flashcards;
