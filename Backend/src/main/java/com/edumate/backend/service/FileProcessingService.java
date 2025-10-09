package com.edumate.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileProcessingService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    public String extractTextFromFile(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        if (fileName == null) {
            throw new IllegalArgumentException("File name cannot be null");
        }

        String fileExtension = getFileExtension(fileName).toLowerCase();
        
        switch (fileExtension) {
            case "txt":
                return extractTextFromTxt(file);
            case "pdf":
                return extractTextFromPdf(file);
            case "doc":
            case "docx":
                return extractTextFromDoc(file);
            case "md":
                return extractTextFromMarkdown(file);
            default:
                throw new UnsupportedOperationException("Unsupported file type: " + fileExtension);
        }
    }

    public String saveFile(MultipartFile file) throws IOException {
        // Create upload directory if it doesn't exist
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Generate unique filename
        String originalFileName = file.getOriginalFilename();
        String fileExtension = getFileExtension(originalFileName);
        String uniqueFileName = UUID.randomUUID().toString() + "." + fileExtension;

        // Save file
        Path filePath = uploadPath.resolve(uniqueFileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return uniqueFileName;
    }

    private String extractTextFromTxt(MultipartFile file) throws IOException {
        return new String(file.getBytes(), StandardCharsets.UTF_8);
    }

    private String extractTextFromPdf(MultipartFile file) throws IOException {
        // For now, return a placeholder. In production, you'd use Apache PDFBox or similar
        return "PDF content extraction not implemented yet. Please upload a .txt file for now.";
    }

    private String extractTextFromDoc(MultipartFile file) throws IOException {
        // For now, return a placeholder. In production, you'd use Apache POI
        return "DOC/DOCX content extraction not implemented yet. Please upload a .txt file for now.";
    }

    private String extractTextFromMarkdown(MultipartFile file) throws IOException {
        return new String(file.getBytes(), StandardCharsets.UTF_8);
    }

    private String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return "";
        }
        return fileName.substring(lastDotIndex + 1);
    }

    public void deleteFile(String fileName) throws IOException {
        Path filePath = Paths.get(uploadDir, fileName);
        if (Files.exists(filePath)) {
            Files.delete(filePath);
        }
    }
}


