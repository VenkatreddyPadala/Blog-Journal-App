package com.journal.app.repository;

import com.journal.app.model.Media;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MediaRepository extends MongoRepository<Media, String> {

    List<Media> findByUploadedBy(String userId);

    void deleteByUploadedBy(String userId);
}