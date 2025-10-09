// Note service for handling note-related API calls
const API_BASE_URL =
  import.meta.env.VITE_API_URL || "http://localhost:3001/api";

class NoteService {
  // Get all notes for the current user
  async getNotes() {
    try {
      const token = localStorage.getItem("token");
      const response = await fetch(`${API_BASE_URL}/notes`, {
        method: "GET",
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (!response.ok) {
        throw new Error("Failed to fetch notes");
      }

      return await response.json();
    } catch (error) {
      console.error("Get notes error:", error);
      throw error;
    }
  }

  // Get a specific note by ID
  async getNote(noteId) {
    try {
      const token = localStorage.getItem("token");
      const response = await fetch(`${API_BASE_URL}/notes/${noteId}`, {
        method: "GET",
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (!response.ok) {
        throw new Error("Failed to fetch note");
      }

      return await response.json();
    } catch (error) {
      console.error("Get note error:", error);
      throw error;
    }
  }

  // Create a new note
  async createNote(noteData) {
    try {
      const token = localStorage.getItem("token");
      const response = await fetch(`${API_BASE_URL}/notes`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(noteData),
      });

      if (!response.ok) {
        throw new Error("Failed to create note");
      }

      return await response.json();
    } catch (error) {
      console.error("Create note error:", error);
      throw error;
    }
  }

  // Upload a file and generate summary
  async uploadFile(file, title, projectId) {
    try {
      const token = localStorage.getItem("token");
      const formData = new FormData();
      formData.append("file", file);
      formData.append("title", title);
      formData.append("projectId", projectId);

      const response = await fetch(`${API_BASE_URL}/notes/upload`, {
        method: "POST",
        headers: {
          Authorization: `Bearer ${token}`,
        },
        body: formData,
      });

      if (!response.ok) {
        throw new Error("Failed to upload file");
      }

      return await response.json();
    } catch (error) {
      console.error("Upload file error:", error);
      throw error;
    }
  }

  // Generate MCQs for a note
  async generateMCQs(noteId, numberOfQuestions = 5) {
    try {
      const token = localStorage.getItem("token");
      const response = await fetch(
        `${API_BASE_URL}/notes/${noteId}/generate-mcqs?numberOfQuestions=${numberOfQuestions}`,
        {
          method: "POST",
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      if (!response.ok) {
        throw new Error("Failed to generate MCQs");
      }

      return await response.json();
    } catch (error) {
      console.error("Generate MCQs error:", error);
      throw error;
    }
  }

  // Generate flashcards for a note
  async generateFlashcards(noteId, numberOfCards = 10) {
    try {
      const token = localStorage.getItem("token");
      const response = await fetch(
        `${API_BASE_URL}/notes/${noteId}/generate-flashcards?numberOfCards=${numberOfCards}`,
        {
          method: "POST",
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      if (!response.ok) {
        throw new Error("Failed to generate flashcards");
      }

      return await response.text();
    } catch (error) {
      console.error("Generate flashcards error:", error);
      throw error;
    }
  }

  // Update a note
  async updateNote(noteId, noteData) {
    try {
      const token = localStorage.getItem("token");
      const response = await fetch(`${API_BASE_URL}/notes/${noteId}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(noteData),
      });

      if (!response.ok) {
        throw new Error("Failed to update note");
      }

      return await response.json();
    } catch (error) {
      console.error("Update note error:", error);
      throw error;
    }
  }

  // Delete a note
  async deleteNote(noteId) {
    try {
      const token = localStorage.getItem("token");
      const response = await fetch(`${API_BASE_URL}/notes/${noteId}`, {
        method: "DELETE",
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (!response.ok) {
        throw new Error("Failed to delete note");
      }

      return true;
    } catch (error) {
      console.error("Delete note error:", error);
      throw error;
    }
  }
}

export default new NoteService();


