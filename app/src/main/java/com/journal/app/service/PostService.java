package com.journal.app.service;

import com.journal.app.dto.request.PostRequest;
import com.journal.app.exception.ResourceNotFoundException;
import com.journal.app.model.Category;
import com.journal.app.model.Post;
import com.journal.app.model.User;
import com.journal.app.repository.CategoryRepository;
import com.journal.app.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AuthService authService;

    public Post createPost(PostRequest postRequest) {
        User currentUser = authService.getCurrentUser();

        Post post = new Post();
        post.setTitle(postRequest.getTitle());
        post.setContent(postRequest.getContent());
        post.setExcerpt(postRequest.getExcerpt());
        post.setTags(postRequest.getTags());
        post.setAuthor(currentUser);
        post.setAuthorId(currentUser.getId());
        post.setPublished(postRequest.isPublished());
        post.setDraft(postRequest.isDraft());
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());

        if (postRequest.isPublished()) {
            post.setPublishedAt(LocalDateTime.now());
        }

        // Handle categories
        if (postRequest.getCategoryIds() != null && !postRequest.getCategoryIds().isEmpty()) {
            List<Category> categories = categoryRepository.findAllById(postRequest.getCategoryIds());
            post.setCategories(categories);
            post.setCategoryIds(postRequest.getCategoryIds());

            // Update category post counts
            categories.forEach(category -> {
                category.setPostCount(category.getPostCount() + 1);
                categoryRepository.save(category);
            });
        }

        return postRepository.save(post);
    }

    public Post updatePost(String id, PostRequest postRequest) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));

        User currentUser = authService.getCurrentUser();

        // Check if current user is the author
        if (!post.getAuthorId().equals(currentUser.getId())) {
            throw new RuntimeException("You don't have permission to update this post");
        }

        post.setTitle(postRequest.getTitle());
        post.setContent(postRequest.getContent());
        post.setExcerpt(postRequest.getExcerpt());
        post.setTags(postRequest.getTags());
        post.setPublished(postRequest.isPublished());
        post.setDraft(postRequest.isDraft());
        post.setUpdatedAt(LocalDateTime.now());

        // Update published date if changing from draft to published
        if (postRequest.isPublished() && post.getPublishedAt() == null) {
            post.setPublishedAt(LocalDateTime.now());
        }

        // Handle category updates
        if (postRequest.getCategoryIds() != null) {
            // Decrease count for old categories
            if (post.getCategories() != null) {
                post.getCategories().forEach(category -> {
                    category.setPostCount(Math.max(0, category.getPostCount() - 1));
                    categoryRepository.save(category);
                });
            }

            // Set new categories and increase their counts
            List<Category> newCategories = categoryRepository.findAllById(postRequest.getCategoryIds());
            post.setCategories(newCategories);
            post.setCategoryIds(postRequest.getCategoryIds());

            newCategories.forEach(category -> {
                category.setPostCount(category.getPostCount() + 1);
                categoryRepository.save(category);
            });
        }

        return postRepository.save(post);
    }

    public void deletePost(String id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));

        User currentUser = authService.getCurrentUser();

        if (!post.getAuthorId().equals(currentUser.getId())) {
            throw new RuntimeException("You don't have permission to delete this post");
        }

        // Soft delete
        post.setDeleted(true);
        post.setDeletedAt(LocalDateTime.now());

        // Update category counts
        if (post.getCategories() != null) {
            post.getCategories().forEach(category -> {
                category.setPostCount(Math.max(0, category.getPostCount() - 1));
                categoryRepository.save(category);
            });
        }

        postRepository.save(post);
    }

    public Post getPostById(String id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));

        if (post.isDeleted()) {
            throw new ResourceNotFoundException("Post", "id", id);
        }

        // Increment view count
        post.setViewCount(post.getViewCount() + 1);
        return postRepository.save(post);
    }

    public Page<Post> getAllPosts(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sortBy));
        return postRepository.findByPublishedTrueAndDeletedFalse(pageable);
    }

    public Page<Post> getMyPosts(int page, int size, String sortBy) {
        User currentUser = authService.getCurrentUser();
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sortBy));
        return postRepository.findByAuthorIdAndDeletedFalse(currentUser.getId(), pageable);
    }

    public Page<Post> getMyDrafts(int page, int size) {
        User currentUser = authService.getCurrentUser();
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "updatedAt"));
        return postRepository.findByAuthorIdAndDraftTrueAndDeletedFalse(currentUser.getId(), pageable);
    }

    public Page<Post> getPostsByCategory(String categoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "publishedAt"));
        return postRepository.findByCategoryIdsContainingAndDeletedFalse(categoryId, pageable);
    }

    public Page<Post> getPostsByTag(String tag, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "publishedAt"));
        return postRepository.findByTagsContainingAndDeletedFalse(tag, pageable);
    }

    public Page<Post> searchPosts(String searchTerm, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "publishedAt"));
        return postRepository.searchPosts(searchTerm, pageable);
    }

    public Page<Post> searchMyPosts(String searchTerm, int page, int size) {
        User currentUser = authService.getCurrentUser();
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "updatedAt"));
        return postRepository.searchUserPosts(currentUser.getId(), searchTerm, pageable);
    }
}