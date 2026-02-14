package com.journal.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "media")
public class Media {

    @Id
    private String id;

    private String fileName;

    private String originalFileName;

    private String fileType;

    private long fileSize;

    private String fileUrl;

    private String thumbnailUrl;

    private int width;

    private int height;

    private String uploadedBy;

    private LocalDateTime uploadedAt;

    private String storageType;

    private String metadata;
}