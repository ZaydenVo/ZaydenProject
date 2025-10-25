package com.zayden.post_service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.zayden.post_service.entity.Post;

import java.util.List;

@Repository
public interface PostRepository extends MongoRepository<Post, String> {
    Page<Post> findAllByUserId(String userId, Pageable pageable);

    @Query("{ $text: { $search: ?0 } }")
    Page<Post> searchText(String text, Pageable pageable);

    List<Post> findTop5ByOrderByIdDesc();

    List<Post> findTop5ByIdLessThanOrderByIdDesc(String beforeId);

    Page<Post> findByUserIdInOrderByCreatedDateDesc(List<String> userIds, Pageable pageable);
}
