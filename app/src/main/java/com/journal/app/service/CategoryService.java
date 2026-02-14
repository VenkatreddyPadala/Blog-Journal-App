package com.journal.app.service;

import com.journal.app.dto.request.CategoryRequest;
import com.journal.app.dto.response.MessageResponse;
import com.journal.app.exception.ResourceNotFoundException;
import com.journal.app.model.Category;
import com.journal.app.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public Category createCategory(CategoryRequest categoryRequest) {
        if (categoryRepository.existsByName(categoryRequest.getName())) {
            throw new RuntimeException("Category with name '" + categoryRequest.getName() + "' already exists");
        }

        Category category = new Category();
        category.setName(categoryRequest.getName());
        category.setDescription(categoryRequest.getDescription());
        category.setColor(categoryRequest.getColor());
        category.setIcon(categoryRequest.getIcon());
        category.setCreatedAt(LocalDateTime.now());
        category.setUpdatedAt(LocalDateTime.now());

        return categoryRepository.save(category);
    }

    public Category updateCategory(String id, CategoryRequest categoryRequest) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));

        // Check if new name conflicts with existing category
        if (!category.getName().equals(categoryRequest.getName()) &&
                categoryRepository.existsByName(categoryRequest.getName())) {
            throw new RuntimeException("Category with name '" + categoryRequest.getName() + "' already exists");
        }

        category.setName(categoryRequest.getName());
        category.setDescription(categoryRequest.getDescription());
        category.setColor(categoryRequest.getColor());
        category.setIcon(categoryRequest.getIcon());
        category.setUpdatedAt(LocalDateTime.now());

        return categoryRepository.save(category);
    }

    public MessageResponse deleteCategory(String id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));

        if (category.getPostCount() > 0) {
            return new MessageResponse("Cannot delete category with existing posts", false);
        }

        categoryRepository.delete(category);
        return new MessageResponse("Category deleted successfully");
    }

    public Category getCategoryById(String id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "name", name));
    }
}