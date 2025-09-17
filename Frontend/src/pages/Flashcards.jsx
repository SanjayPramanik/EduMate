import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import noteService from "../services/noteService";

const Flashcards = () => {
  const navigate = useNavigate();
  const [notes, setNotes] = useState([]);
  const [loading, setLoading] = useState(true);
  const [flashcardsData, setFlashcardsData] = useState({});
  const [selectedNote, setSelectedNote] = useState(null);
  const [showFlashcards, setShowFlashcards] = useState(false);
  const [generating, setGenerating] = useState(false);

  useEffect(() => {
    loadNotes();
  }, []);

  const loadNotes = async () => {
    try {
      const notesData = await noteService.getNotes();
      setNotes(notesData);
    } catch (error) {
      console.error("Failed to load notes:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleGenerateFlashcards = async (note) => {
    setGenerating(true);
    try {
      const flashcards = await noteService.generateFlashcards(note.noteId, 10);
      const parsedFlashcards = parseFlashcardsResponse(flashcards);
      setFlashcardsData(prev => ({
        ...prev,
        [note.noteId]: {
          flashcards: parsedFlashcards,
          title: note.title
        }
      }));
      setSelectedNote(note.noteId);
      setShowFlashcards(true);
    } catch (error) {
      console.error("Failed to generate flashcards:", error);
      alert("Failed to generate flashcards. Please try again.");
    } finally {
      setGenerating(false);
    }
  };

  const parseFlashcardsResponse = (response) => {
    try {
      const parsed = JSON.parse(response);
      return parsed.flashcards || [];
    } catch (error) {
      // If JSON parsing fails, try to extract flashcards from text
      const lines = response.split('\n').filter(line => line.trim());
      const flashcards = [];
      let current = null;

      for (const line of lines) {
        if (line.toLowerCase().includes('front:') || line.toLowerCase().includes('question:')) {
          if (current && current.front && current.back) {
            flashcards.push(current);
          }
          current = { front: line.split(':').slice(1).join(':').trim(), back: '' };
        } else if (line.toLowerCase().includes('back:') || line.toLowerCase().includes('answer:')) {
          if (current) {
            current.back = line.split(':').slice(1).join(':').trim();
          }
        }
      }
      if (current && current.front && current.back) {
        flashcards.push(current);
      }

      // If no flashcards found, create some from the response
      if (flashcards.length === 0) {
        return [{
          front: "Generated Flashcard",
          back: response
        }];
      }

      return flashcards;
    }
  };

  const handleStudyFlashcards = (noteId) => {
    setSelectedNote(noteId);
    setShowFlashcards(true);
  };

  const handleBackToList = () => {
    setSelectedNote(null);
    setShowFlashcards(false);
  };

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="text-center">
          <div className="animate-spin rounded-full h-32 w-32 border-b-2 border-indigo-600 mx-auto"></div>
          <p className="mt-4 text-gray-600">Loading notes...</p>
        </div>
      </div>
    );
  }

  if (showFlashcards && selectedNote && flashcardsData[selectedNote]) {
    return (
      <FlashcardStudyComponent
        flashcardsData={flashcardsData[selectedNote]}
        onBack={handleBackToList}
      />
    );
  }

  return (
    <div className="min-h-screen bg-gray-50">
      <div className="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
        <div className="px-4 py-6 sm:px-0">
          <div className="flex justify-between items-center mb-6">
            <h1 className="text-3xl font-bold text-gray-900">Flashcards</h1>
            <button
              onClick={() => navigate("/notes")}
              className="bg-indigo-600 text-white px-4 py-2 rounded-md hover:bg-indigo-700"
            >
              Upload Notes
            </button>
          </div>

          {notes.length === 0 ? (
            <div className="text-center py-12">
              <div className="bg-white p-8 rounded-lg shadow-md max-w-md mx-auto">
                <h3 className="text-lg font-semibold text-gray-900 mb-4">
                  No Notes Available
                </h3>
                <p className="text-gray-600 mb-4">
                  Upload some notes first to generate AI-powered flashcards!
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
              {notes.map((note) => (
                <div key={note.noteId} className="bg-white rounded-lg shadow-md p-6">
                  <h3 className="text-lg font-semibold text-gray-900 mb-2">
                    {note.title}
                  </h3>
                  <p className="text-sm text-gray-600 mb-4">
                    {note.summary
                      ? note.summary.substring(0, 100) + "..."
                      : "No summary available"}
                  </p>
                  <div className="flex flex-col space-y-2">
                    {flashcardsData[note.noteId] ? (
                      <button
                        onClick={() => handleStudyFlashcards(note.noteId)}
                        className="bg-green-600 text-white px-3 py-2 rounded text-sm hover:bg-green-700"
                      >
                        Study Flashcards ({flashcardsData[note.noteId].flashcards.length})
                      </button>
                    ) : (
                      <button
                        onClick={() => handleGenerateFlashcards(note)}
                        disabled={generating}
                        className="bg-blue-600 text-white px-3 py-2 rounded text-sm hover:bg-blue-700 disabled:opacity-50"
                      >
                        {generating ? "Generating..." : "Generate Flashcards"}
                      </button>
                    )}
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

// Flashcard Study Component
const FlashcardStudyComponent = ({ flashcardsData, onBack }) => {
  const [currentCard, setCurrentCard] = useState(0);
  const [showBack, setShowBack] = useState(false);
  const [studiedCards, setStudiedCards] = useState(new Set());

  const flashcards = flashcardsData.flashcards || [];

  const handleNext = () => {
    if (currentCard < flashcards.length - 1) {
      setCurrentCard(currentCard + 1);
      setShowBack(false);
    }
  };

  const handlePrevious = () => {
    if (currentCard > 0) {
      setCurrentCard(currentCard - 1);
      setShowBack(false);
    }
  };

  const handleFlip = () => {
    setShowBack(!showBack);
    if (!showBack) {
      setStudiedCards(prev => new Set([...prev, currentCard]));
    }
  };

  const resetSession = () => {
    setCurrentCard(0);
    setShowBack(false);
    setStudiedCards(new Set());
  };

  if (flashcards.length === 0) {
    return (
      <div className="min-h-screen bg-gray-50 flex items-center justify-center">
        <div className="bg-white p-8 rounded-lg shadow-md max-w-md w-full text-center">
          <h2 className="text-xl font-bold mb-4">No Flashcards Available</h2>
          <button
            onClick={onBack}
            className="bg-indigo-600 text-white py-2 px-4 rounded-md hover:bg-indigo-700"
          >
            Back to Flashcards
          </button>
        </div>
      </div>
    );
  }

  const progress = ((studiedCards.size) / flashcards.length) * 100;

  return (
    <div className="min-h-screen bg-gray-50">
      <div className="max-w-4xl mx-auto py-6 sm:px-6 lg:px-8">
        <div className="mb-6">
          <div className="flex justify-between items-center mb-4">
            <h1 className="text-2xl font-bold text-gray-900">
              {flashcardsData.title} - Flashcards
            </h1>
            <div className="flex space-x-2">
              <button
                onClick={resetSession}
                className="bg-yellow-600 text-white px-4 py-2 rounded-md hover:bg-yellow-700"
              >
                Reset
              </button>
              <button
                onClick={onBack}
                className="bg-gray-600 text-white px-4 py-2 rounded-md hover:bg-gray-700"
              >
                Back
              </button>
            </div>
          </div>
          
          <div className="mb-4">
            <div className="flex justify-between items-center mb-2">
              <span className="text-sm text-gray-600">
                Card {currentCard + 1} of {flashcards.length}
              </span>
              <span className="text-sm text-gray-600">
                Progress: {studiedCards.size}/{flashcards.length} ({Math.round(progress)}%)
              </span>
            </div>
            <div className="w-full bg-gray-200 rounded-full h-2">
              <div 
                className="bg-green-600 h-2 rounded-full transition-all duration-300"
                style={{ width: `${progress}%` }}
              ></div>
            </div>
          </div>
        </div>

        <div className="bg-white rounded-lg shadow-lg">
          <div 
            className="flashcard-container h-96 flex items-center justify-center p-8 cursor-pointer transform transition-transform hover:scale-105"
            onClick={handleFlip}
            style={{
              background: showBack 
                ? 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)' 
                : 'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)'
            }}
          >
            <div className="text-center text-white">
              <div className="text-sm font-medium mb-4 opacity-75">
                {showBack ? 'Answer' : 'Question'} - Click to flip
              </div>
              <div className="text-xl font-bold leading-relaxed">
                {showBack 
                  ? flashcards[currentCard].back 
                  : flashcards[currentCard].front}
              </div>
            </div>
          </div>
          
          <div className="p-6 bg-gray-50 rounded-b-lg">
            <div className="flex justify-between items-center">
              <button
                onClick={handlePrevious}
                disabled={currentCard === 0}
                className="bg-gray-600 text-white px-6 py-2 rounded-md hover:bg-gray-700 disabled:opacity-50 disabled:cursor-not-allowed"
              >
                Previous
              </button>
              
              <div className="text-center">
                <button
                  onClick={handleFlip}
                  className="bg-indigo-600 text-white px-8 py-2 rounded-md hover:bg-indigo-700 mx-4"
                >
                  {showBack ? 'Show Question' : 'Show Answer'}
                </button>
              </div>
              
              <button
                onClick={handleNext}
                disabled={currentCard === flashcards.length - 1}
                className="bg-gray-600 text-white px-6 py-2 rounded-md hover:bg-gray-700 disabled:opacity-50 disabled:cursor-not-allowed"
              >
                Next
              </button>
            </div>
            
            {studiedCards.size === flashcards.length && (
              <div className="mt-4 p-4 bg-green-100 rounded-md text-center">
                <p className="text-green-800 font-medium">
                  🎉 Great job! You've studied all {flashcards.length} cards!
                </p>
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default Flashcards;
