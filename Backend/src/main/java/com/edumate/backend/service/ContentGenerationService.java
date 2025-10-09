package com.edumate.backend.service;

import com.edumate.backend.entity.mongodb.MCQ;
import com.edumate.backend.entity.sql.Note;
import com.edumate.backend.entity.sql.Project;
import com.edumate.backend.entity.sql.User;
import com.edumate.backend.repository.mongodb.MCQRepository;
import com.edumate.backend.repository.sql.NoteRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ContentGenerationService {

    private final FileProcessingService fileProcessingService;
    private final GeminiAIService geminiAIService;
    private final NoteRepository noteRepository;
    private final MCQRepository mcqRepository;
    private final ObjectMapper objectMapper;

    public ContentGenerationService(FileProcessingService fileProcessingService,
                                  GeminiAIService geminiAIService,
                                  NoteRepository noteRepository,
                                  MCQRepository mcqRepository) {
        this.fileProcessingService = fileProcessingService;
        this.geminiAIService = geminiAIService;
        this.noteRepository = noteRepository;
        this.mcqRepository = mcqRepository;
        this.objectMapper = new ObjectMapper();
    }

    public Note processUploadedFile(MultipartFile file, String title, User user, Project project) throws Exception {
        // Extract text from uploaded file
        String originalContent = fileProcessingService.extractTextFromFile(file);
        
        // Save file to disk
        String savedFileName = fileProcessingService.saveFile(file);
        
        // Generate summary using AI
        String summary = geminiAIService.generateSummary(originalContent);
        
        // Create and save note
        Note note = new Note();
        note.setNoteId(UUID.randomUUID().toString());
        note.setTitle(title);
        note.setSummary(summary);
        note.setOriginalContent(originalContent);
        note.setFileName(savedFileName);
        note.setFileType(file.getContentType());
        note.setFileSize(file.getSize());
        note.setUser(user);
        note.setProject(project);
        
        return noteRepository.save(note);
    }

    public List<MCQ> generateMCQsForNote(String noteId, int numberOfQuestions) throws Exception {
        Note note = noteRepository.findById(noteId).orElseThrow(() -> 
            new IllegalArgumentException("Note not found with ID: " + noteId));
        
        String content = note.getOriginalContent();
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Note content is empty");
        }
        
        // Generate MCQs using AI
        String mcqResponse = geminiAIService.generateMCQs(content, numberOfQuestions);
        
        // Parse AI response
        List<MCQ> mcqs = parseMCQResponse(mcqResponse, noteId, note.getUser().getUserId().toString(), 
                                         note.getProject().getProjectId().toString());
        
        // Save MCQs to database
        return mcqRepository.saveAll(mcqs);
    }

    public String generateFlashcardsForNote(String noteId, int numberOfCards) throws Exception {
        Note note = noteRepository.findById(noteId).orElseThrow(() -> 
            new IllegalArgumentException("Note not found with ID: " + noteId));
        
        String content = note.getOriginalContent();
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Note content is empty");
        }
        
        // Generate flashcards using AI
        return geminiAIService.generateFlashcards(content, numberOfCards);
    }

    private List<MCQ> parseMCQResponse(String response, String noteId, String userId, String projectId) throws Exception {
        List<MCQ> mcqs = new ArrayList<>();
        
        try {
            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode questionsNode = rootNode.path("questions");
            
            if (questionsNode.isArray()) {
                for (JsonNode questionNode : questionsNode) {
                    MCQ mcq = new MCQ();
                    mcq.setMcqId(UUID.randomUUID().toString());
                    mcq.setNoteId(noteId);
                    mcq.setUserId(userId);
                    mcq.setProjectId(projectId);
                    
                    // Extract question and options
                    String question = questionNode.path("question").asText();
                    JsonNode optionsNode = questionNode.path("options");
                    List<String> options = new ArrayList<>();
                    
                    if (optionsNode.isArray()) {
                        for (JsonNode option : optionsNode) {
                            options.add(option.asText());
                        }
                    }
                    
                    // Create formatted question with options
                    StringBuilder questionText = new StringBuilder();
                    questionText.append(question).append("\n");
                    for (int i = 0; i < options.size(); i++) {
                        questionText.append((char)('A' + i)).append(". ").append(options.get(i)).append("\n");
                    }
                    questionText.append("Correct Answer: ").append((char)('A' + questionNode.path("correctAnswer").asInt()));
                    questionText.append("\nExplanation: ").append(questionNode.path("explanation").asText());
                    
                    mcq.setQuestions(List.of(questionText.toString()));
                    mcqs.add(mcq);
                }
            }
        } catch (Exception e) {
            // If JSON parsing fails, create a simple MCQ with the raw response
            MCQ mcq = new MCQ();
            mcq.setMcqId(UUID.randomUUID().toString());
            mcq.setNoteId(noteId);
            mcq.setUserId(userId);
            mcq.setProjectId(projectId);
            mcq.setQuestions(List.of(response));
            mcqs.add(mcq);
        }
        
        return mcqs;
    }
}


