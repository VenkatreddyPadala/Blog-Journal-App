package com.journal.app.repository;

import com.journal.app.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends MongoRepository<Post, String> {


    Page<Post> findByAuthorIdAndDeletedFalse(String authorId, Pageable pageable);

    List<Post> findByAuthorIdAndDeletedFalse(String authorId);

    Page<Post> findByPublishedTrueAndDeletedFalse(Pageable pageable);

    Page<Post> findByAuthorIdAndDraftTrueAndDeletedFalse(String authorId, Pageable pageable);

    Page<Post> findByCategoryIdsContainingAndDeletedFalse(String categoryId, Pageable pageable);

    Page<Post> findByTagsContainingAndDeletedFalse(String tag, Pageable pageable);

    @Query("{'$and': [{'deleted': false}, {'$or': [{'title': {$regex: ?0, $options: 'i'}}, {'content': {$regex: ?0, $options: 'i'}}, {'tags': {$regex: ?0, $options: 'i'}}]}]}")
    Page<Post> searchPosts(String searchTerm, Pageable pageable);

    @Query("{'$and': [{'authorId': ?0}, {'deleted': false}, {'$or': [{'title': {$regex: ?1, $options: 'i'}}, {'content': {$regex: ?1, $options: 'i'}}, {'tags': {$regex: ?1, $options: 'i'}}]}]}")
    Page<Post> searchUserPosts(String authorId, String searchTerm, Pageable pageable);

    long countByAuthorIdAndDeletedFalse(String authorId);

    long countByPublishedTrueAndDeletedFalse();
}