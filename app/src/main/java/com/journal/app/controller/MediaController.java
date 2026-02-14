package com.journal.app.controller;

import com.journal.app.dto.response.MessageResponse;
import com.journal.app.model.Media;
import com.journal.app.service.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/media")
public class MediaController {

    @Autowired
    private MediaService mediaService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new MessageResponse("Please select a file to upload", false));
            }

            // Validate file size (10MB max)
            if (file.getSize() > 10 * 1024 * 1024) {
                return ResponseEntity.badRequest()
                        .body(new MessageResponse("File size must not exceed 10MB", false));
            }

            // Validate file type (images only for now)
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity.badRequest()
                        .body(new MessageResponse("Only image files are allowed", false));
            }

            Media media = mediaService.uploadFile(file);
            return new ResponseEntity<>(media, HttpStatus.CREATED);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Failed to upload file: " + e.getMessage(), false));
        }
    }

    @GetMapping("/my-media")
    public ResponseEntity<List<Media>> getMyMedia() {
        List<Media> mediaList = mediaService.getMyMedia();
        return ResponseEntity.ok(mediaList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Media> getMediaById(@PathVariable String id) {
        Media media = mediaService.getMediaById(id);
        return ResponseEntity.ok(media);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteMedia(@PathVariable String id) {
        try {
            mediaService.deleteMedia(id);
            return ResponseEntity.ok(new MessageResponse("Media deleted successfully"));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Failed to delete media: " + e.getMessage(), false));
        }
    }
}