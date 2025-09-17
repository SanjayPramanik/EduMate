import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import noteService from "../services/noteService";
import projectService from "../services/projectService";

const Notes = () => {
  const navigate = useNavigate();
  const [notes, setNotes] = useState([]);
  const [projects, setProjects] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showUploadModal, setShowUploadModal] = useState(false);
  const [uploading, setUploading] = useState(false);
  const [selectedFile, setSelectedFile] = useState(null);
  const [uploadData, setUploadData] = useState({
    title: "",
    projectId: "",
  });

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    try {
      const [notesData, projectsData] = await Promise.all([
        noteService.getNotes(),
        projectService.getProjects(),
      ]);
      setNotes(notesData);
      setProjects(projectsData);
    } catch (error) {
      console.error("Failed to load data:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleFileUpload = async (e) => {
    e.preventDefault();
    if (!selectedFile || !uploadData.title || !uploadData.projectId) {
      alert("Please fill in all fields and select a file");
      return;
    }

    setUploading(true);
    try {
      await noteService.uploadFile(
        selectedFile,
        uploadData.title,
        parseInt(uploadData.projectId)
      );
      setShowUploadModal(false);
      setSelectedFile(null);
      setUploadData({ title: "", projectId: "" });
      loadData(); // Reload notes
    } catch (error) {
      console.error("Upload failed:", error);
      alert("Upload failed. Please try again.");
    } finally {
      setUploading(false);
    }
  };

  const handleGenerateMCQs = async (noteId) => {
    try {
      const mcqs = await noteService.generateMCQs(noteId, 5);
      console.log("Generated MCQs:", mcqs);
      alert("MCQs generated successfully! Check the console for details.");
    } catch (error) {
      console.error("MCQ generation failed:", error);
      alert("MCQ generation failed. Please try again.");
    }
  };

  const handleGenerateFlashcards = async (noteId) => {
    try {
      const flashcards = await noteService.generateFlashcards(noteId, 10);
      console.log("Generated flashcards:", flashcards);
      alert(
        "Flashcards generated successfully! Check the console for details."
      );
    } catch (error) {
      console.error("Flashcard generation failed:", error);
      alert("Flashcard generation failed. Please try again.");
    }
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

  return (
    <div className="min-h-screen bg-gray-50">
      <div className="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
        <div className="px-4 py-6 sm:px-0">
          <div className="flex justify-between items-center mb-6">
            <h1 className="text-3xl font-bold text-gray-900">Notes</h1>
            <button
              onClick={() => setShowUploadModal(true)}
              className="bg-indigo-600 text-white px-4 py-2 rounded-md hover:bg-indigo-700"
            >
              Upload File
            </button>
          </div>

          {notes.length === 0 ? (
            <div className="text-center py-12">
              <p className="text-gray-600 mb-4">
                No notes found. Upload a file to get started!
              </p>
            </div>
          ) : (
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
              {notes.map((note) => (
                <div
                  key={note.noteId}
                  className="bg-white rounded-lg shadow-md p-6"
                >
                  <h3 className="text-lg font-semibold text-gray-900 mb-2">
                    {note.title}
                  </h3>
                  <p className="text-sm text-gray-600 mb-4">
                    {note.summary
                      ? note.summary.substring(0, 100) + "..."
                      : "No summary available"}
                  </p>
                  <div className="flex space-x-2">
                    <button
                      onClick={() => handleGenerateMCQs(note.noteId)}
                      className="bg-blue-600 text-white px-3 py-1 rounded text-sm hover:bg-blue-700"
                    >
                      Generate MCQs
                    </button>
                    <button
                      onClick={() => handleGenerateFlashcards(note.noteId)}
                      className="bg-green-600 text-white px-3 py-1 rounded text-sm hover:bg-green-700"
                    >
                      Generate Flashcards
                    </button>
                  </div>
                </div>
              ))}
            </div>
          )}

          {/* Upload Modal */}
          {showUploadModal && (
            <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
              <div className="bg-white rounded-lg p-6 w-full max-w-md">
                <h2 className="text-xl font-bold mb-4">Upload File</h2>
                <form onSubmit={handleFileUpload}>
                  <div className="mb-4">
                    <label className="block text-sm font-medium text-gray-700 mb-2">
                      Title
                    </label>
                    <input
                      type="text"
                      value={uploadData.title}
                      onChange={(e) =>
                        setUploadData({ ...uploadData, title: e.target.value })
                      }
                      className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
                      required
                    />
                  </div>
                  <div className="mb-4">
                    <label className="block text-sm font-medium text-gray-700 mb-2">
                      Project
                    </label>
                    <select
                      value={uploadData.projectId}
                      onChange={(e) =>
                        setUploadData({
                          ...uploadData,
                          projectId: e.target.value,
                        })
                      }
                      className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
                      required
                    >
                      <option value="">Select a project</option>
                      {projects.map((project) => (
                        <option
                          key={project.projectId}
                          value={project.projectId}
                        >
                          {project.projectName}
                        </option>
                      ))}
                    </select>
                  </div>
                  <div className="mb-4">
                    <label className="block text-sm font-medium text-gray-700 mb-2">
                      File
                    </label>
                    <input
                      type="file"
                      onChange={(e) => setSelectedFile(e.target.files[0])}
                      accept=".txt,.md,.pdf,.doc,.docx"
                      className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
                      required
                    />
                  </div>
                  <div className="flex space-x-3">
                    <button
                      type="submit"
                      disabled={uploading}
                      className="bg-indigo-600 text-white px-4 py-2 rounded-md hover:bg-indigo-700 disabled:opacity-50"
                    >
                      {uploading ? "Uploading..." : "Upload"}
                    </button>
                    <button
                      type="button"
                      onClick={() => setShowUploadModal(false)}
                      className="bg-gray-600 text-white px-4 py-2 rounded-md hover:bg-gray-700"
                    >
                      Cancel
                    </button>
                  </div>
                </form>
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default Notes;
