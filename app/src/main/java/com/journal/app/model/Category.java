package com.journal.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "categories")
public class Category {

    @Id
    private String id;

    @NotBlank(message = "Category name is required")
    @Size(max = 50, message = "Category name cannot exceed 50 characters")
    @Indexed(unique = true)
    private String name;

    private String description;

    private String color;

    private String icon;

    private int postCount = 0;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public Category(String name, String description) {
        this.name = name;
        this.description = description;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}