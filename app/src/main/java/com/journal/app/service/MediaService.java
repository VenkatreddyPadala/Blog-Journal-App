package com.journal.app.service;

import com.journal.app.exception.ResourceNotFoundException;
import com.journal.app.model.Media;
import com.journal.app.model.User;
import com.journal.app.repository.MediaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class MediaService {

    @Autowired
    private MediaRepository mediaRepository;

    @Autowired
    private AuthService authService;

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    public Media uploadFile(MultipartFile file) throws IOException {
        User currentUser = authService.getCurrentUser();

        // Create upload directory if it doesn't exist
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Generate unique filename
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String filename = UUID.randomUUID().toString() + extension;

        // Save file to disk
        Path filePath = uploadPath.resolve(filename);
        Files.copy(file.getInputStream(), filePath);

        // Create media record
        Media media = new Media();
        media.setFileName(filename);
        media.setOriginalFileName(originalFilename);
        media.setFileType(file.getContentType());
        media.setFileSize(file.getSize());
        media.setFileUrl("/uploads/" + filename);
        media.setUploadedBy(currentUser.getId());
        media.setUploadedAt(LocalDateTime.now());
        media.setStorageType("LOCAL");

        return mediaRepository.save(media);
    }

    public List<Media> getMyMedia() {
        User currentUser = authService.getCurrentUser();
        return mediaRepository.findByUploadedBy(currentUser.getId());
    }

    public Media getMediaById(String id) {
        return mediaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Media", "id", id));
    }

    public void deleteMedia(String id) throws IOException {
        Media media = mediaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Media", "id", id));

        User currentUser = authService.getCurrentUser();

        if (!media.getUploadedBy().equals(currentUser.getId())) {
            throw new RuntimeException("You don't have permission to delete this media");
        }

        // Delete file from disk
        Path filePath = Paths.get(uploadDir).resolve(media.getFileName());
        Files.deleteIfExists(filePath);

        // Delete from database
        mediaRepository.delete(media);
    }
}