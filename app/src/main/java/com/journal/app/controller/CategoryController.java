package com.journal.app.controller;

import com.journal.app.dto.request.CategoryRequest;
import com.journal.app.dto.response.MessageResponse;
import com.journal.app.model.Category;
import com.journal.app.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public ResponseEntity<Category> createCategory(@Valid @RequestBody CategoryRequest categoryRequest) {
        Category category = categoryService.createCategory(categoryRequest);
        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(
            @PathVariable String id,
            @Valid @RequestBody CategoryRequest categoryRequest) {
        Category category = categoryService.updateCategory(id, categoryRequest);
        return ResponseEntity.ok(category);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteCategory(@PathVariable String id) {
        MessageResponse response = categoryService.deleteCategory(id);

        if (!response.isSuccess()) {
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable String id) {
        Category category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }

    @GetMapping("/public/all")
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Category> getCategoryByName(@PathVariable String name) {
        Category category = categoryService.getCategoryByName(name);
        return ResponseEntity.ok(category);
    }
}