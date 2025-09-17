import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import projectService from "../services/projectService";

const Projects = () => {
  const navigate = useNavigate();
  const [projects, setProjects] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [editingProject, setEditingProject] = useState(null);
  const [formData, setFormData] = useState({
    projectName: "",
    projectSummary: "",
  });

  useEffect(() => {
    loadProjects();
  }, []);

  const loadProjects = async () => {
    try {
      const data = await projectService.getProjects();
      setProjects(data);
    } catch (error) {
      console.error("Failed to load projects:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (editingProject) {
        await projectService.updateProject(editingProject.projectId, formData);
      } else {
        await projectService.createProject(formData);
      }
      setShowModal(false);
      setEditingProject(null);
      setFormData({ projectName: "", projectSummary: "" });
      loadProjects();
    } catch (error) {
      console.error("Failed to save project:", error);
      alert("Failed to save project. Please try again.");
    }
  };

  const handleEdit = (project) => {
    setEditingProject(project);
    setFormData({
      projectName: project.projectName,
      projectSummary: project.projectSummary || "",
    });
    setShowModal(true);
  };

  const handleDelete = async (projectId) => {
    if (confirm("Are you sure you want to delete this project?")) {
      try {
        await projectService.deleteProject(projectId);
        loadProjects();
      } catch (error) {
        console.error("Failed to delete project:", error);
        alert("Failed to delete project. Please try again.");
      }
    }
  };

  const closeModal = () => {
    setShowModal(false);
    setEditingProject(null);
    setFormData({ projectName: "", projectSummary: "" });
  };

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="text-center">
          <div className="animate-spin rounded-full h-32 w-32 border-b-2 border-indigo-600 mx-auto"></div>
          <p className="mt-4 text-gray-600">Loading projects...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50">
      <div className="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
        <div className="px-4 py-6 sm:px-0">
          <div className="flex justify-between items-center mb-6">
            <h1 className="text-3xl font-bold text-gray-900">Projects</h1>
            <button
              onClick={() => setShowModal(true)}
              className="bg-indigo-600 text-white px-4 py-2 rounded-md hover:bg-indigo-700"
            >
              Create Project
            </button>
          </div>

          {projects.length === 0 ? (
            <div className="text-center py-12">
              <div className="bg-white p-8 rounded-lg shadow-md max-w-md mx-auto">
                <h3 className="text-lg font-semibold text-gray-900 mb-4">
                  No Projects Found
                </h3>
                <p className="text-gray-600 mb-4">
                  Create your first project to organize your learning materials!
                </p>
                <button
                  onClick={() => setShowModal(true)}
                  className="bg-indigo-600 text-white px-4 py-2 rounded-md hover:bg-indigo-700"
                >
                  Create Project
                </button>
              </div>
            </div>
          ) : (
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
              {projects.map((project) => (
                <div
                  key={project.projectId}
                  className="bg-white rounded-lg shadow-md p-6"
                >
                  <h3 className="text-lg font-semibold text-gray-900 mb-2">
                    {project.projectName}
                  </h3>
                  <p className="text-sm text-gray-600 mb-4">
                    {project.projectSummary || "No description available"}
                  </p>
                  <p className="text-xs text-gray-500 mb-4">
                    Created: {new Date(project.createdAt).toLocaleDateString()}
                  </p>
                  <div className="flex space-x-2">
                    <button
                      onClick={() => handleEdit(project)}
                      className="bg-blue-600 text-white px-3 py-1 rounded text-sm hover:bg-blue-700"
                    >
                      Edit
                    </button>
                    <button
                      onClick={() => handleDelete(project.projectId)}
                      className="bg-red-600 text-white px-3 py-1 rounded text-sm hover:bg-red-700"
                    >
                      Delete
                    </button>
                    <button
                      onClick={() => navigate(`/notes?project=${project.projectId}`)}
                      className="bg-green-600 text-white px-3 py-1 rounded text-sm hover:bg-green-700"
                    >
                      View Notes
                    </button>
                  </div>
                </div>
              ))}
            </div>
          )}

          {/* Create/Edit Modal */}
          {showModal && (
            <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
              <div className="bg-white rounded-lg p-6 w-full max-w-md">
                <h2 className="text-xl font-bold mb-4">
                  {editingProject ? "Edit Project" : "Create New Project"}
                </h2>
                <form onSubmit={handleSubmit}>
                  <div className="mb-4">
                    <label className="block text-sm font-medium text-gray-700 mb-2">
                      Project Name *
                    </label>
                    <input
                      type="text"
                      value={formData.projectName}
                      onChange={(e) =>
                        setFormData({ ...formData, projectName: e.target.value })
                      }
                      className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
                      required
                    />
                  </div>
                  <div className="mb-6">
                    <label className="block text-sm font-medium text-gray-700 mb-2">
                      Description
                    </label>
                    <textarea
                      value={formData.projectSummary}
                      onChange={(e) =>
                        setFormData({
                          ...formData,
                          projectSummary: e.target.value,
                        })
                      }
                      className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
                      rows="3"
                      placeholder="Describe your project..."
                    />
                  </div>
                  <div className="flex space-x-3">
                    <button
                      type="submit"
                      className="bg-indigo-600 text-white px-4 py-2 rounded-md hover:bg-indigo-700"
                    >
                      {editingProject ? "Update Project" : "Create Project"}
                    </button>
                    <button
                      type="button"
                      onClick={closeModal}
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

export default Projects;
