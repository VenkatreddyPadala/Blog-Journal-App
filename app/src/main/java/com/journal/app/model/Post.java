package com.journal.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "posts")
public class Post {

    @Id
    private String id;

    @NotBlank(message = "Title is required")
    @Size(max = 200, message = "Title cannot exceed 200 characters")
    private String title;

    @NotBlank(message = "Content is required")
    private String content;

    private String excerpt;

    @DBRef
    private User author;

    private String authorId;

    private List<String> tags = new ArrayList<>();

    @DBRef
    private List<Category> categories = new ArrayList<>();

    private List<String> categoryIds = new ArrayList<>();

    private List<Media> mediaFiles = new ArrayList<>();

    private boolean published = true;

    private boolean Draft = false;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime publishedAt;

    private int viewCount = 0;

    private boolean deleted = false;

    private LocalDateTime deletedAt;
}