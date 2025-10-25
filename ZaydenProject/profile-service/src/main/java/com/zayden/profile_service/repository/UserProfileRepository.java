package com.zayden.profile_service.repository;

import java.util.List;
import java.util.Optional;

import feign.Param;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import com.zayden.profile_service.entity.UserProfile;

@Repository
public interface UserProfileRepository extends Neo4jRepository<UserProfile, String> {
    Optional<UserProfile> findByUserId(String userId);

    Optional<UserProfile> findByUsername(String username);

    List<UserProfile> findAllByUsernameLike(String username);

    @Query("MATCH (u:user_profile) WHERE u.userId IN $userIds RETURN u")
    List<UserProfile> findByUserIdIn(@Param("userIds") List<String> userIds);

    @Query("MATCH (u:user_profile {userId: $userId}) RETURN count(u) > 0")
    boolean existsByUserId(@Param("userId") String userId);
}
