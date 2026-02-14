package com.journal.app.controller;

import com.journal.app.dto.request.PostRequest;
import com.journal.app.dto.response.MessageResponse;
import com.journal.app.model.Post;
import com.journal.app.service.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping
    public ResponseEntity<Post> createPost(@Valid @RequestBody PostRequest postRequest) {
        Post post = postService.createPost(postRequest);
        return new ResponseEntity<>(post, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(
            @PathVariable String id,
            @Valid @RequestBody PostRequest postRequest) {
        Post post = postService.updatePost(id, postRequest);
        return ResponseEntity.ok(post);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deletePost(@PathVariable String id) {
        postService.deletePost(id);
        return ResponseEntity.ok(new MessageResponse("Post deleted successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable String id) {
        Post post = postService.getPostById(id);
        return ResponseEntity.ok(post);
    }

    @GetMapping("/public/all")
    public ResponseEntity<Page<Post>> getAllPublicPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "publishedAt") String sortBy) {
        Page<Post> posts = postService.getAllPosts(page, size, sortBy);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/my-posts")
    public ResponseEntity<Page<Post>> getMyPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "updatedAt") String sortBy) {
        Page<Post> posts = postService.getMyPosts(page, size, sortBy);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/my-drafts")
    public ResponseEntity<Page<Post>> getMyDrafts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Post> posts = postService.getMyDrafts(page, size);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Page<Post>> getPostsByCategory(
            @PathVariable String categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Post> posts = postService.getPostsByCategory(categoryId, page, size);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/tag/{tag}")
    public ResponseEntity<Page<Post>> getPostsByTag(
            @PathVariable String tag,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Post> posts = postService.getPostsByTag(tag, page, size);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Post>> searchPosts(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Post> posts = postService.searchPosts(q, page, size);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/my-posts/search")
    public ResponseEntity<Page<Post>> searchMyPosts(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Post> posts = postService.searchMyPosts(q, page, size);
        return ResponseEntity.ok(posts);
    }
}