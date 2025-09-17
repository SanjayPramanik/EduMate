// Project service for handling project-related API calls
const API_BASE_URL =
  import.meta.env.VITE_API_URL || "http://localhost:3001/api";

class ProjectService {
  // Get all projects for the current user
  async getProjects() {
    try {
      const token = localStorage.getItem("token");
      const response = await fetch(`${API_BASE_URL}/projects`, {
        method: "GET",
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (!response.ok) {
        throw new Error("Failed to fetch projects");
      }

      return await response.json();
    } catch (error) {
      console.error("Get projects error:", error);
      throw error;
    }
  }

  // Get a specific project by ID
  async getProject(projectId) {
    try {
      const token = localStorage.getItem("token");
      const response = await fetch(`${API_BASE_URL}/projects/${projectId}`, {
        method: "GET",
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (!response.ok) {
        throw new Error("Failed to fetch project");
      }

      return await response.json();
    } catch (error) {
      console.error("Get project error:", error);
      throw error;
    }
  }

  // Create a new project
  async createProject(projectData) {
    try {
      const token = localStorage.getItem("token");
      const response = await fetch(`${API_BASE_URL}/projects`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(projectData),
      });

      if (!response.ok) {
        throw new Error("Failed to create project");
      }

      return await response.json();
    } catch (error) {
      console.error("Create project error:", error);
      throw error;
    }
  }

  // Update a project
  async updateProject(projectId, projectData) {
    try {
      const token = localStorage.getItem("token");
      const response = await fetch(`${API_BASE_URL}/projects/${projectId}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(projectData),
      });

      if (!response.ok) {
        throw new Error("Failed to update project");
      }

      return await response.json();
    } catch (error) {
      console.error("Update project error:", error);
      throw error;
    }
  }

  // Delete a project
  async deleteProject(projectId) {
    try {
      const token = localStorage.getItem("token");
      const response = await fetch(`${API_BASE_URL}/projects/${projectId}`, {
        method: "DELETE",
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (!response.ok) {
        throw new Error("Failed to delete project");
      }

      return true;
    } catch (error) {
      console.error("Delete project error:", error);
      throw error;
    }
  }
}

export default new ProjectService();


